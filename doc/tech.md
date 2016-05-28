# Game architecture
```
            +------------+                             +-----------------+
            |            |  1)HTTP Request             |                 |
            |            | (initial state, move order) |                 |
            |   Client   +---------------------------->|                 |
            |            |                             |                 |
            | scala,     |                             |    Game server  |
            | python,    |  2)Json Response            |                 |
            | java,      |(new game state)             |                 |
            | …          |<----------------------------+                 |
            |            |                             |                 |
            |            |                             |                 |
            +------------+                             +-----------------+
```
As you may already have noticed with the above diagram, your bot will basically be a HTTP client. You send a HTTP request (to have the initial game state or to move your bot) and then you got a HTTP Response encoded in Json.
Ok fine, let's see what those requests look like.

# API
The requests you will have to send to the server will depend on the mode you are using. You can either train your bot by using the "training mode" or play for real using the "arena mode". Each mode is using a different URL for the requests.

# Game modes
## Training
In training mode, you can play against some dummy bots, you can specify the number of turns you want to play and, if you want, you can provide a specific map to test your algorithms. The games played in Training mode are not scored, so feel free to use it as much as you want.

In training mode, the url you will need to use is:
`http://dinf-jdis-ia.dinf.fsci.usherbrooke.ca:80/api/training`

Here are the steps you need to follow to play in training mode

1. Send a POST HTTP request to the http://dinf-jdis-ia.dinf.fsci.usherbrooke.ca:80/api/training URL to get the initial game state. Here are the parameters you can pass in the POST body:
  * key (required)
    * The secret key you were given
  * turns
    * The number of turns you want to play. If you don't specify this parameter, 300 turns will be played.
  * map
    * The map id corresponding to the map you want to use. Possible values are: m1, m2, m3, m4, m5, m6. You can look at these maps at the end of this document. If you don't specify this parameter, a random map will be generated for you.

2. Parse the JSON you've received as a response of the first step. See Json documentation for more details.

3. Send your move to the URL extracted from the Json (see Json urls for more details) using a POST request. Here are the parameters you need to send in the POST body:
  * key (required)
    * The secret key you were given
  * dir (required)
    * Can be one of: 'Stay', 'North', 'South', 'East', 'West'

4. Parse the JSON received in the same way you did in the second step. Repeat until the game is finished. See the Json documentation to know when a game is finished.

## Arena / Competition
In arena mode, you will play against other players. Your games will be rated and your rank will be updated accordingly.

In arena mode, the url you will need to use is:
`http://dis-ia.dinf.fsci.usherbrooke.ca:80/api/arena`

Here are the steps you need to follow to play in arena mode:

1. Send a POST HTTP request to the http://dis-ia.dinf.fsci.usherbrooke.ca:80/api/arena URL to get the initial game state. There are two required parameters for this call:
  * key (required)
    * The secret key your team was given
  * gameId
    * The ID of the game you want to connect to. Will be provided by the organizers
2. Parse the JSON you've received as a response of the first step. See Json documentation for more details.
3. Send your move to the URL extracted from the Json (see Json urls for more details) using a POST request. Here are the parameters you need to send in the POST body:
  * key (required)
    * The secret key you were given
  * dir (required)
    * Can be one of: 'Stay', 'North', 'South', 'East', 'West'
4. Parse the JSON received in the same way you did in the second step. Repeat until the game is finished. See the Json documentation to know when a game is finished.

## HTTP response codes
When you issue a call to the server, it will respond using normal HTTP codes:

- 200: Everything went well, good job!
- 4xx (400, 404, …): You did something wrong (wrong secret key, trying to play when the game is already finished, too slow to send the move, …). Be sure to check the response body to know what the exact error is.
- 500: Something went wrong on the server side. How could it be possible? ;)

# Timeout
You have a maximum of 1 second to play a move. If you exceed this limit, your bot is marked as "crashed". The game is over for you, you can't issue any move anymore. You should be aware that you can still be the winner of the game if nobody is able to earn more gold than the gold you had when your bot crashed.

# Json Documentation
Below is a full sample of the Json that the server will send to your client. Each object is described in details later in the documentation.
```json
{
  "game":{
     "id":"s2xh3aig",
     "turn":1100,
     "maxTurns":1200,
     "heroes":[
        {
           "id":1,
           "name":"gsimard",
           "userId":"j07ws669",
           "elo":1200,
           "pos":{
              "x":5,
              "y":6
           },
           "life":60,
           "gold":0,
           "mineCount":0,
           "spawnPos":{
              "x":5,
              "y":6
           },
           "crashed":true
        },
        {
           "id":2,
           "name":"gsimard",
           "userId":"j07ws669",
           "elo":1200,
           "pos":{
              "x":12,
              "y":6
           },
           "life":100,
           "gold":0,
           "mineCount":0,
           "spawnPos":{
              "x":12,
              "y":6
           },
           "crashed":true
        },
        {
           "id":3,
           "name":"gsimard",
           "userId":"j07ws669",
           "elo":1200,
           "pos":{
              "x":12,
              "y":11
           },
           "life":80,
           "gold":0,
           "mineCount":0,
           "spawnPos":{
              "x":12,
              "y":11
           },
           "crashed":true
        },
        {
           "id":4,
           "name":"gsimard",
           "userId":"j07ws669",
           "elo":1200,
           "pos":{
              "x":4,
              "y":8
           },
           "lastDir": "South",
           "life":38,
           "gold":1078,
           "mineCount":6,
           "spawnPos":{
              "x":5,
              "y":11
           },
           "crashed":false
        }
     ],
     "board":{
        "size":18,
        "tiles":"##############        ############################        ##############################
        ##############################$4    $4############################  @4    ######################## @1##    ##
        ####################  []        []  ##################        ####
        #################### $4####$4  ########################  $4####$4  ####################        ####
        ##################  [] []  ####################  @2##    ##@3  ########################        ############################$-
        $-##############################    ##############################        ############################ ##############"
     },
     "finished":true
  },
  "hero":{
     "id":4,
     "name":"gsimard",
     "userId":"j07ws669",
     "elo":1200,
     "pos":{
        "x":4,
        "y":8
     },
     "lastDir": "South",
     "life":38,
     "gold":1078,
     "mineCount":6,
     "spawnPos":{
        "x":5,
        "y":11
     },
     "crashed":false
  },
  "token":"lte0",
  "viewUrl":"http://localhost:9000/s2xh3aig",
  "playUrl":"http://localhost:9000/api/s2xh3aig/lte0/play"
}
```

## Game object
```json
  "game":{
     "id":"s2xh3aig",
     "turn":1100,
     "maxTurns":1200,
     "heroes":[...],
     "board":{
        "size":18,
        "tiles":"##############        ############################        ##############################
        ##############################$4    $4############################  @4    ########################  @1##    ##
        ####################  []        []  ##################        ####        ####################  $4####$4
        ########################  $4####$4  ####################        ####        ##################  []        []
        ####################  @2##    ##@3  ########################        ############################$-
        $-##############################    ##############################        ############################
        ##############"
     },
     "finished":true
     }
```

### id
Unique identifier of the game

### turn
Current number of moves since the beginning. This is the total number of moves done at this point. Each turn contains 4 move (one for each player). So if you want to know the "real" turn number, you need to divide this number by 4.

### maxTurns
Maximum number of turns. Same as above, you may need to divide this number by 4.

### heroes
An array of Hero objects.

### board
A Json object with two values. size is the size of the map: the number of horizontal/vertical tiles. As the map is always a square, this number is the same for X and Y. tiles is a string representing the map. Each tile is coded using two chars (see the rules legend for more information). As you may already have noticed, to get each line of the map, you just have to use a %size (modulo) on the tiles.

### finished
If the game is finished or not.

## Hero object
```json
        {
           "id":1,
           "name":"gsimard",
           "userId":"j07ws669",
           "elo":1200,
           "pos":{
              "x":5,
              "y":6
           },
           "lastDir": "South",
           "life":60,
           "gold":0,
           "mineCount":0,
           "spawnPos":{
              "x":5,
              "y":6
           },
           "crashed":true
        }
```
Nothing special here, pos represents your initial position on the map. spawnPos represents the position where you will respawn when you die. For now, it's the same position. elo is the current rating of the player (the higher the better). lastDir is the last order this hero issued (if any).

## Urls
```json
  "viewUrl":"http://localhost:9000/s2xh3aig",
  "playUrl":"http://localhost:9000/api/s2xh3aig/lte0/play"
```

### viewUrl
An URL that you can open in your browser to view a replay of the game. This is useful if you want to see a replay of a training.

### playUrl
The URL you need to use to send your move orders to the server.

## Maps
```
 val m1 = """
##@1    ####    @4##
      ########
        ####
    []        []
$-    ##    ##    $-
$-    ##    ##    $-
    []        []
        ####  @3
      ########
##@2    ####      ##"""
  val m2 = """
########################
########        ########
####$-            $-####
####  @1        @4  ####
##    []  $-$-  []    ##
##    ##  ####  ##    ##
##    ##  ####  ##    ##
##    []  $-$-  []    ##
####  @2        @3  ####
####$-            $-####
########        ########
########################"""
val m3 = """
          ##############
          ##  $-  $-  ##
          ##  $-  $-  ##
          ##  $-  $-  ##
          ##  $-  $-  ##
            []              []
##########  ##################
$-                    $-
##########  ####  ############
@1  @2                            @3  @4
            ################
$-  $-                            $-  $-
"""
val m4 = """
####################################
################$-$-################
##########@1[]##    ##[]@4##########
##########  ##        ##  ##########
######$-    ####    ####    $-######
##########                ##########
########                    ########
######    ##  ########  ##    ######
######  $-##  ########  ##$-  ######
######  $-##  ########  ##$-  ######
######    ##  ########  ##    ######
########                    ########
##########                ##########
######$-    ####    ####    $-######
##########  ##        ##  ##########
##########@2[]##    ##[]@3##########
################$-$-################
####################################
"""
val m5 = """
    ########@1        @4########
      ######            ######
        ##    $-    $-    ##
##      ##    ##    ##    ##      ##
[]##$-                        $-##[]
  $-                            $-
    ####  ##            ##  ####
          $-##  ####  ##$-
      ########$-####$-########
      ########$-####$-########
          $-##  ####  ##$-
    ####  ##            ##  ####
  $-                            $-
[]##$-                        $-##[]
##      ##    ##    ##    ##      ##
        ##    $-    $-    ##
      ######            ######
    ########@2        @3########
"""
val m6 = """
      ##    ########    ##
  ##      ##  ####  ##      ####
    ##
  ##    ##$-##    ##$-##    ##
  []    ##@1        @4##    []
      ##                ##
  ##  ##                ##  ##
  ####                    ####
  ####                    ####
  ##  ##                ##  ##
      ##                ##
  []    ##@2        @3##    []
  ##    ##$-##    ##$-##    ##
    ##
  ##      ##  ####  ##      ##
      ##    ########    ##
"""
```
