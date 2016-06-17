package com.coveo.blitz.server
package system

case class Pov(gameId: String, token: String)

case class PlayerInput(game: Game, token: String) {

  def hero = game heroByToken token
}

case class AiTimeoutException(pov: Pov) extends GameException {
  def message = s"$pov timeout"
}
