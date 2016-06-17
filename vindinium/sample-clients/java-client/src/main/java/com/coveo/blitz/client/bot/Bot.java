package com.coveo.blitz.client.bot;

import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Position;

/**
 * Example bot
 */
public class Bot implements SimpleBot {
	private static final Logger logger = LogManager.getLogger(Bot.class);

    private BoardParser parser = new BoardParser();

    @Override
    public BotMove move(GameState gameState) {
    	logger.info(gameState.getHero().getPos().toString());

        List<Tile> tiles = parser.parse(gameState.getGame().getBoard().getTiles());
        List<List<Tile>> map = new ArrayList<List<Tile>>();
        int size = gameState.getGame().getBoard().getSize();
        for (int rowIndex = 0; rowIndex < size; ++rowIndex) {
            List<Tile> row = new ArrayList<Tile>();
            for (int colIndex = 0; colIndex < size; ++colIndex) {
                row.add(tiles.get(rowIndex * size + colIndex));
            }
            map.add(row);
        }

        Position target = new Position(0, 0);
        for (int r = 0; r < size; ++r) {
            for (int c = 0; c < size; ++c) {
                if (map.get(r).get(c) == Tile.MineNeutral) {
                    target = new Position(r, c);
                }
            }
        }

        Pathfinder pathfinder = new Pathfinder(map);
        return pathfinder.navigateTowards(gameState.getHero().getPos(), target);
    }

    @Override
    public void setup() {
    }

    @Override
    public void shutdown() {
    }
}
