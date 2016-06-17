package com.coveo.blitz.client.bot;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Position;

public class Pathfinder {
    private List<List<Tile>> map;

    /**
     * Creates a pathfinder.
     *
     * @param map list of rows
     */
    public Pathfinder(List<List<Tile>> map) {
        this.map = map;
    }

    /**
     * Finds the shortest path from 'source' to 'destination' and returns the
     * direction to take a single step towards that path.
     *
     * @param source source location (row, column) (e.g. location of your hero)
     * @param destination destination location (row, column) (e.g. location of a
     * mine)
     * @return direction to take to follow the optimal path
     */
    public BotMove navigateTowards(Position source, Position destination) {
        List<Position> path = shortestPath(source, destination);
        if (!path.isEmpty()) return directionTowards(source, path.get(0));
        else return BotMove.STAY;
    }

    /**
     * Finds the shortest path from 'source' to 'destination' and returns the
     * sequence of positions to follow to take the optimal path (excluding
     * 'source', including 'destination').
     *
     * @param source source location (row, column) (e.g. location of your hero)
     * @param destination destination location (row, column) (e.g. location of a
     * mine)
     * @return list of positions to go to in order to reach the destination
     */
    public List<Position> shortestPath(Position source, Position destination) {
        Set<Position> nodes = new HashSet<Position>();
        nodes.add(source);

        Map<Position, Integer> distances = new HashMap<Position, Integer>();
        distances.put(source, new Integer(0));

        Map<Position, Position> predecessors = new HashMap<Position, Position>();

        while (!nodes.isEmpty()) {
            Position u = null;
            for (Position p : nodes) {
                if (u == null || distances.get(p) < distances.get(u)) {
                    u = p;
                }
            }

            nodes.remove(u);

            List<Position> neighbors = getNeighbors(u);

            if (neighbors.contains(destination)) {
                predecessors.put(destination, u);
                break;
            }

            for (Position v : neighbors ) {
                if (isPassable(v) && !distances.containsKey(v)) {
                    distances.put(v, distances.get(u) + 1);
                    predecessors.put(v, u);
                    nodes.add(v);
                }
            }
        }

        return buildPath(destination, predecessors);
    }

    private List<Position> buildPath(Position destination,
                                     Map<Position, Position> predecessors) {
        List<Position> path = new ArrayList<Position>();
        Position n = destination;

        while (predecessors.containsKey(n)) {
            path.add(n);
            n = predecessors.get(n);
        }
        
        Collections.reverse(path);

        return path;
    }

    private List<Position> getNeighbors(Position pos) {
        List<Position> neighbors = new ArrayList<Position>();
        if (pos.getX() - 1 >= 0) neighbors.add(new Position(pos.getX() - 1, pos.getY()));
        if (pos.getX() + 1 < this.map.size()) neighbors.add(new Position(pos.getX() + 1, pos.getY()));
        if (pos.getY() - 1 >= 0) neighbors.add(new Position(pos.getX(), pos.getY() - 1));
        if (pos.getY() + 1 < this.map.get(0).size()) neighbors.add(new Position(pos.getX(), pos.getY() + 1));
        return neighbors;
    }

    private boolean isPassable(Position pos) {
        Tile t = this.map.get(pos.getX()).get(pos.getY());
        return t == Tile.Air || t == Tile.Spikes ||
               t == Tile.Hero1 || t == Tile.Hero2 ||
               t == Tile.Hero3 || t == Tile.Hero4;
    }

    private BotMove directionTowards(Position source, Position destination) {
        if (source.getX() > destination.getX()) return BotMove.NORTH;
        if (source.getX() < destination.getX()) return BotMove.SOUTH;
        if (source.getY() > destination.getY()) return BotMove.WEST;
        if (source.getY() < destination.getY()) return BotMove.EAST;
        return BotMove.STAY;
    }
}
