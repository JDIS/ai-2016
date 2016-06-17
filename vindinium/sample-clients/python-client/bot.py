from random import choice
from game import Game
from pathfinding import navigate_towards, shortest_path

class Bot:
    def move(self, state):
        game = Game(state)

        us = game.hero.id
        our_loc = game.hero.pos

        mines = game.mines_locs
        targets = [loc for loc, mine in game.mines_locs.items()
                   if mine != str(us)]
        print('mines ', len(mines), len(targets), "".join(mines.values()))

        move = 'Stay'
        if targets:
            target = min(targets,
                         key=lambda target: len(shortest_path(game.board, our_loc, target)))
            move = navigate_towards(game.board, our_loc, target)

        return move

class RandomBot(Bot):
    def move(self, state):
        game = Game(state)
        dirs = ['Stay', 'North', 'South', 'East', 'West']
        return choice(dirs)

