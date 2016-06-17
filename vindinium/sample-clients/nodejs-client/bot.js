"use strict";

const open = require("open");

const MapUtils = require('./map');
const Types = require('./types');
const Pathfinding = require('./pathfinding');

const directions = ["north", "south", "east", "west", "stay"];
let first = true;
function bot(state, callback) {
  if (first) {
    console.log('Open Browser at ' + state.viewUrl);
    open(state.viewUrl);
    first = false;
  }

  const map = MapUtils.parseBoard(state.game.board);

  var mines = [];
  for (var row = 0; row < map.length; ++row) {
      for (var col = 0; col < map[row].length; ++col) {
          if (map[row][col].type == Types.Mine &&
              map[row][col].owner != state.hero.id) {
              mines.push(map[row][col]);
          }
      }
  }
  var dir = "stay";
  if (mines) {
      const hero = map[state.hero.pos.x][state.hero.pos.y];
      const mine = mines.reduce((a, b) =>
                                (Pathfinding.shortestPath(map, hero, a).length <
                                 Pathfinding.shortestPath(map, hero, b).length) ?
                                 a : b);
      
      dir = Pathfinding.navigateTowards(map, hero, mine);
  }

  console.log(dir);
  callback(null, dir);
};


module.exports = bot;
if (require.main === module)
  require('./client/index').cli(bot);
