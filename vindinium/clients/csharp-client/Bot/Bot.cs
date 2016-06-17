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

            // TODO implement SkyNet here
            // Pathfinding example:
            // string direction = pathfinder.NavigateTowards(state.myHero.pos, new Pos(0, 0));
            string direction;

            switch (random.Next(0, 5)) {
                case 0:
                    direction = Direction.East;
                    break;

                case 1:
                    direction = Direction.West;
                    break;

                case 2:
                    direction = Direction.North;
                    break;

                case 3:
                    direction = Direction.South;
                    break;

                default:
                    direction = Direction.Stay;
                    break;
            }

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
