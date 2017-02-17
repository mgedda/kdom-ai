#!/bin/bash

newGameResult=$(eval "curl -X POST http://localhost/new-games/?playerCount=2")
#echo $newGameResult
uuid=${newGameResult:12:36}
#echo ""
#echo $uuid
eval "curl -X POST http://localhost/new-games/${uuid}/join/player1"
eval "curl -X POST http://localhost/new-games/${uuid}/join/player2"
eval "curl http://localhost/games/${uuid}"
