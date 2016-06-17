package com.coveo.blitz.server

import scala.util.{ Random, Try, Success, Failure }

object Arbiter {

  def replay(game: Game, dir: Dir): Game = doMove(game, game.heroId, dir)

  def move(game: Game, token: String, dir: Dir): Try[Game] =
    validate(game, token) { hero =>
      doMove(game, hero.id, dir)
    }

  private def validate(game: Game, token: String)(f: Hero => Game): Try[Game] =
    (game.finished, game heroByToken token) match {
      case (true, _) =>
        Failure(RuleViolationException("Game is finished"))
      case (_, None) =>
        Failure(RuleViolationException("Token not found"))
      case (_, Some(hero)) if game.hero != Some(hero) =>
        Failure(RuleViolationException(s"Not your turn to move"))
      case (_, Some(hero)) => Success(f(hero))
    }

  private def doMove(game: Game, id: Int, dir: Dir) = {

    def reach(game: Game, destPos: Pos) = (game.board get destPos) match {
      case None => game
      case Some(tile) => (game hero destPos) match {
        case Some(_) => game
        case None => tile match {
          case Tile.Air    => game.withHero(id, _ moveTo destPos)
          case Tile.Tavern => game.withHero(id, _.drinkBeer)
          case Tile.Mine(n) if n != Some(id) =>
            val h = game.hero(id).fightMine
            if (h.isAlive) game.withHero(h).withBoard(_.transferMine(destPos, Some(h.id)))
            else reSpawn(game.withBoard(_.transferMines(h.id, None)), h)
          case Tile.Mine(n) => game
          case Tile.Wall    => game
          case Tile.Spikes =>
            // Spikes hurt the hero. If he survives, he can move onto the spikes, no problemo.
            val h = game.hero(id).stepOnSpike
            if (h.isAlive) game.withHero(h).withHero(id, _ moveTo destPos)
            else reSpawn(game.withBoard(_.transferMines(h.id, None)), h)

        }
      }
    }

    if (dir == Dir.Crash) finalize(game.setTimedOut, id).step
    else {
      val h = game.hero(id).withLastDir(dir)
      finalize(fights(reach(game.withHero(h), h.pos to dir), id), id).step
    }
  }

  private def reSpawn(game: Game, hero: Hero): Game = {
    val pos = game spawnPosOf hero
    val h = hero.reSpawn(pos, game.turn)
    game hero pos match {
      case Some(opponent) if opponent.id != h.id =>
        val g = game.withHero(h).withBoard(_.transferMines(opponent.id, Some(h.id)))
        reSpawn(g, opponent)
      case _ => game withHero h
    }
  }

  private def fights(game: Game, id: Int): Game =
    (game.hero(id).pos.neighbors map game.hero).flatten.foldLeft(game) {
      // stop the fighting if the attacking hero has been respawned
      // also, don't attack heroes that have been respawned in the process
      case (game, enemy)
          if game.hero(id).lastRespawn != Some(game.turn) &&
             game.hero(enemy.id).lastRespawn != Some(game.turn) => attack(id, game, enemy)
      case (game, _) => game
    }

  private def attack(id: Int, game: Game, enemy: Hero): Game = {
    val (h1, h2) = (game hero id) -> enemy.defend
    val g = game withHero h2
    if (h2.isDead) reSpawn(g.withBoard(_.transferMines(h2.id, Some(h1.id))), h2)
    else g
  }

  def finalize(game: Game, id: Int) = game withHero {
    (game hero id).day withGold game.board.countMines(id)
  }
}
