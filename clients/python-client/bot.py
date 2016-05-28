from random import choice
from game import Game
from pathfinding import navigate_towards, shortest_path
import webbrowser

class Bot:

    def __init__(self):
        self.viewUrl = None

    def move(self, state):
        game = Game(state)

        if not self.viewUrl:
            self.viewUrl = state['viewUrl']
            webbrowser.open(self.viewUrl,new=2)

        # TODO implement SkyNet here
        # Pathfinding example:
        # dir = navigate_towards(game.board, game.hero.pos, (0, 0))
        dirs = ['Stay', 'North', 'South', 'East', 'West']
        return choice(dirs)
