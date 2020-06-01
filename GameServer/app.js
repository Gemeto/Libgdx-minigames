var app = require('express')();
        var http = require('http').Server(app);
        var io = require('socket.io')(http);
        var port = 4555;
        var jellyRequestID;
        var gameInfo = new Object();
        var lastSessionID = 0;

        gameInfo["sessIDs"] = [];

        http.listen(port, function()
        {
                //Message shown on program start
                console.log("app running");
        });

        app.get('/reset', function(req, res){
              gameInfo["sessIDs"] = [];
        });

        app.get('/getSessionID', function(req, res)
        {
                lastSessionID++;
                res.setHeader("valor", lastSessionID);
                console.log(lastSessionID)
                res.send();
                console.log("Session opened by " + lastSessionID);
        });

        app.put('/closeSession', function(req, res)
        {
                gameInfo["PlayerX"+req.headers.value] = undefined;
                gameInfo["sessIDs"].splice(gameInfo["sessIDs"].indexOf(req.headers.value), 1);
                console.log("Session closed by"+req.headers.value + " Array=> " + gameInfo["sessIDs"]);

        });

        app.put('/', function(req, res)
        {
                var str = req.headers.value;
                var result = str.split(" ");
                jellyRequestID = result[4];
                if(!gameInfo["sessIDs"].includes(jellyRequestID)&&jellyRequestID!=0)
                        gameInfo["sessIDs"].push(jellyRequestID);
                gameInfo["PlayerX"+jellyRequestID] = result[0];
                gameInfo["PlayerY"+jellyRequestID] = result[1];
                gameInfo["PlayerBulletX"+jellyRequestID] = result[2];
                gameInfo["PlayerBulletY"+jellyRequestID] = result[3];
                gameInfo["PlayerDead"+jellyRequestID] = result[5];
                gameInfo["PlayerBulletWidth"+jellyRequestID] = result[6];
                gameInfo["PlayerBulletHeight"+jellyRequestID] = result[7];
                gameInfo["PlayerAngle"+jellyRequestID] = result[8];
                gameInfo["sessIDs"].forEach( function(valor, indice, array) {
                        if(gameInfo["PlayerX"+valor] != undefined&&jellyRequestID!=valor)
                                res.setHeader("valor"+valor, gameInfo["PlayerX"+valor] + " " + gameInfo["PlayerY"+valor] + " " +
                                gameInfo["PlayerBulletX"+valor] + " " + gameInfo["PlayerBulletY"+valor] + " " + gameInfo["PlayerDead"+valor] + " " + gameInfo["PlayerBulletWidth"+valor] + " " +
                                    gameInfo["PlayerBulletHeight"+valor] + " " + gameInfo["PlayerAngle"+valor] + " " + valor + " " + gameInfo["PlayerZ"+valor] + " " + gameInfo["PlayerBulletZ"+valor]);
                });

                var enemies = gameInfo["sessIDs"];
                console.log(enemies)
                enemies = enemies.filter( e => e !== jellyRequestID );
                res.setHeader("enemyIDs", enemies);
                res.setHeader("nEnemies", enemies.length)
                res.send();
        });