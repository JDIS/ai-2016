// Copyright (c) 2005-2016, Coveo Solutions Inc.

using System;
using System.Collections.Generic;
using System.Linq;

namespace CoveoBlitz.Bot
{
    /// <summary>
    /// Bot
    ///
    /// This bot controls your player.
    /// </summary>
    public class Bot : ISimpleBot
    {
        private readonly Random random = new Random();

        /// <summary>
        /// This will be run before the game starts
        /// </summary>
        public void Setup()
        {
            Console.WriteLine("Coveo's C# RandomBot");
        }

        /// <summary>
        /// This will be run on each turns. It must return a direction fot the bot to follow
        /// </summary>
        /// <param name="state">The game state</param>
        /// <returns></returns>
        public string Move(GameState state)
        {
            var pathfinder = new Pathfinder (state.board);
            var mines = new List<Pos> ();
            for (int x = 0; x < state.board.Length; x++) {
                for (int y = 0; y < state.board [x].Length; y++) {
                    if (state.board [x] [y] == Tile.GOLD_MINE_NEUTRAL) {
                        mines.Add (new Pos(x, y));
                    }
                }
            }
            var closest = mines.OrderBy (mine => pathfinder.ShortestPath (state.myHero.pos, mine).Count).First();

            string direction = pathfinder.NavigateTowards (state.myHero.pos, closest);

            Console.WriteLine("Completed turn {0}, going {1}", state.currentTurn, direction);
            return direction;
        }

        /// <summary>
        /// This is run after the game.
        /// </summary>
        public void Shutdown()
        {
            Console.WriteLine("Done");
        }
    }
}
