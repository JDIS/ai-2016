[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/coveord/Blitz2016-Server/blob/master/LICENSE)

[![AI Logo](https://github.com/JDIS/ai-2016/blob/master/logo-grayscale.png)]()

# Credits
Credits to: [http://vindinium.org/](https://github.com/ornicar/vindinium) and to [Coveo](https://github.com/coveo).

Original sources available at: [https://github.com/ornicar/vindinium](https://github.com/ornicar/vindinium) and fork at [https://github.com/coveord/Blitz2016-Server](https://github.com/coveord/Blitz2016-Server)

# Why use this fork?
This fork is like the original vindinium version, but with ranked matches. If you don't need those, you should use the original vindinium since it is still updated.

# Clients
They are two sets of working client samples. The [first client set](https://github.com/JDIS/ai-2016/tree/master/vindinium/sample-clients) was forked by Coveo and should be working. There is also a [Node, Py, C# and Java version](https://github.com/JDIS/ai-2016/tree/master/clients) with a simple pathfinding. SO to [Jesse Emond](https://github.com/stars/JesseEmond) for the pathfinding.

# Ok, but I want to host a competition!
You are at the right place. You could use a vagrant file like [this one](https://github.com/micmro/Vagrant-play) or do a manual setup on a Unix box.

## Installation
- Clone this repository
- Install and start [MongoDB](https://www.mongodb.com/download-center?jmp=nav#community)
- Install Scala and [Activator](https://www.lightbend.com/community/core-tools/activator-and-sbt)

## Setup
Everything about the competitive part is hidden behind an API key. It's hardcoded to `unicorn_carnival`. You can change it in https://github.com/JDIS/ai-2016/blob/master/vindinium/server/conf/application.conf#L20.

## Compile and run
```
$ cd server
$ activator
[activator] dist
```

This will generate an `.jar` that you will be able to run everywhere.

Start MongoDB. Go to the compiled version's `bin` folder.
```
$ chmod u+x vindinium
$ ./vindinium -Dhttp.port=80
```
There you go, you have an up and running server.

## Nice, but how do I use it?
You are able to interact with the server with an JSON REST API. I recommand you to use some [REST client](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en).
You will need 4 commands to controle the server.
- Register a player
- Create a game
- Start a game
- Fetch game's states

### Registering a new player
`localhost:80/register?apiKey=unicorn_carnival&name=some_player_name`

This will return a user key that you will need to give to your new player.

### Creating a game
`localhost:80/games?apiKey=unicorn_carnival&category=arena`

This will create a ranked match. You will receive a key and you need to give it to every team that will be playing in this match.

### Fetching game state
`localhost:80/games/active?apiKey=unicorn_carnival`

This return all created but unfinished match.

### Starting a game
`localhost:80/games/game_id/start?apiKey=unicorn_carnival`

This will let you start a match. When a game has 4 waiting players you can get the game's id with the previous command and start it with this command.

## How to join a match
It's different for each client. So please read the client's manual. The player's ids will let your players do normal matches. To do ranked matches they will need their player's id and an arena's key that you receive when you create a match.
