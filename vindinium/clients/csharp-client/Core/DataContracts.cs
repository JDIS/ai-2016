using System.Collections.Generic;
using System.Runtime.Serialization;

namespace CoveoBlitz
{
    [DataContract]
        public class GameResponse
        {
            [DataMember]
                public Game game;

            [DataMember]
                public Hero hero;

            [DataMember]
                public string token;

            [DataMember]
                public string viewUrl;

            [DataMember]
                public string playUrl;
        }

    [DataContract]
        public class Game
        {
            [DataMember]
                public string id;

            [DataMember]
                public int turn;

            [DataMember]
                public int maxTurns;

            [DataMember]
                public List<Hero> heroes;

            [DataMember]
                public Board board;

            [DataMember]
                public bool finished;
        }

    [DataContract]
        public class Hero
        {
            [DataMember]
                public int id;

            [DataMember]
                public string name;

            [DataMember]
                public int elo;

            [DataMember]
                public Pos pos;

            [DataMember]
                public int life;

            [DataMember]
                public int gold;

            [DataMember]
                public int mineCount;

            [DataMember]
                public Pos spawnPos;

            [DataMember]
                public bool crashed;
        }

    [DataContract]
        public class Pos
        {
            [DataMember]
                public int x;

            [DataMember]
                public int y;

            public Pos(int x, int y)
            {
                this.x = x;
                this.y = y;
            }

            public override bool Equals(object obj)
            {
                if (obj == null) return false;

                Pos p = obj as Pos;
                if ((System.Object)p == null) return false;

                return (x == p.x) && (y == p.y);
            }

            public bool Equals(Pos p)
            {
                if ((object)p == null) return false;

                return (x == p.x) && (y == p.y);
            }

            public override string ToString ()
            {
                return "( " + x.ToString () + ", " + y.ToString () + " )";
            }

            public override int GetHashCode ()
            {
                int hash = 13;
                hash = (hash * 7) + x.GetHashCode ();
                hash = (hash * 7) + y.GetHashCode ();
                return hash;
            }
        }

    [DataContract]
        public class Board
        {
            [DataMember]
                public int size;

            [DataMember]
                public string tiles;
        }
}
