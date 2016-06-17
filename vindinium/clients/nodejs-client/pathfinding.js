const Types = require('./types');

/**
 * Finds the shortest path from 'source' to 'destination' and returns the
 * direction to take a single step towards that path.
 *
 * @param map         map of the game, extracted with the help of MapUtils
 * @param source      source *tile* (not just a position!) (e.g. location of
 *                    your hero)
 * @param destination destination *tile* (e.g. location of a mine)
 * @return {String}   direction to take to follow the optimal path
 */
function navigateTowards(map, source, destination) {
    const path = shortestPath(map, source, destination);
    const target = path.length > 0 ? path[0] : source;
    return directionTowards(source, target);
}

/**
 * Finds the shortest path from 'source' to 'destination' and returns the
 * sequence of tiles to follow to take the optimal path (excluding
 * 'source', including 'destination').
 * @param map         map of the game, extracted with the help of MapUtils
 * @param source      source *tile* (not just a position!) (e.g. location of
 *                    your hero)
 * @param destination destination *tile* (e.g. location of a mine)
 * @return {Array}    list of tiles to go to in order to reach the destination
 */
function shortestPath(map, source, destination) {
    var nodes = new Set([source]);
    var distances = new Map([[source, 0]]);
    var predecessors = new Map();

    while (nodes.size > 0) {
        var u = Array.from(nodes).reduce(function(a, b) {
            return (distances.get(a) < distances.get(b)) ? a : b;
        });
        nodes.delete(u);

        const neighborTiles = getNeighborTiles(map, u);

        if (neighborTiles.includes(destination)) {
            predecessors.set(destination, u);
            break;
        }

        var neighbors = neighborTiles.filter(tile => isWalkable(tile) &&
                                             !distances.has(tile));

        for (var v of neighbors) {
            distances.set(v, distances.get(u) + 1);
            predecessors.set(v, u);

            nodes.add(v);
        }
    }
    return buildPath(destination, predecessors);
}

function buildPath(destination, predecessors) {
    var path = [];
    var n = destination;

    while (predecessors.has(n)) {
        path.push(n);
        n = predecessors.get(n);
    }

    path.reverse();
    
    return path;
}

function isWalkable(tile) {
    return [Types.Player, Types.Nothing, Types.Spike].includes(tile.type);
}

function getNeighborTiles(map, tile) {
    return [to(map, tile, {x:0,y:1}),
            to(map, tile, {x:0,y:-1}),
            to(map, tile, {x:1,y:0}),
            to(map, tile, {x:-1,y:0})].filter(tile => tile != null);
}

function to(map, pos, movement) {
    pos = { x: pos.x + movement.x, y: pos.y + movement.y };
    if (pos.x < 0 || pos.x >= map.length ||
        pos.y < 0 || pos.y >= map[pos.x].length) {
        return null;
    }
    return map[pos.x][pos.y];
}

function directionTowards(source, destination) {
    if (source.x > destination.x) return 'north';
    if (source.x < destination.x) return 'south';
    if (source.y > destination.y) return 'west';
    if (source.y < destination.y) return 'east';
    return 'stay';
}

module.exports = {
    navigateTowards: navigateTowards,
    shortestPath: shortestPath
};
