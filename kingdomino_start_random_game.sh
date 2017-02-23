#!/bin/bash

newGameResult=$(eval "curl -X POST http://localhost/new-games/?playerCount=3")
#echo $newGameResult
uuid=${newGameResult:12:36}
#echo ""

eval "java -jar out/spawnplayer.jar RamdomCalrissian1 RANDOM ${uuid}" &

eval "java -jar out/spawnplayer.jar RamdomCalrissian2 RANDOM ${uuid}" &

echo $uuid
