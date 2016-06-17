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
  const hero = map[state.hero.pos.x][state.hero.pos.y];

  // TODO implement SkyNet here
  // Pathinding example:
  // const dir = Pathfinding.navigateTowards(map, hero, map[0][0]);
  const dir = directions[Math.floor(Math.random() * directions.length)];

  console.log(dir);
  callback(null, dir);
};


module.exports = bot;
if (require.main === module)
  require('./client/index').cli(bot);
