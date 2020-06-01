package com.mygdx.game.Logic.Online;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class P2PManager {

    public static boolean multiplayer;
    private JellyObj jelly;
    public Map<Integer, JellyObj> jellyEnemies;
    private static int sessionID;
    private static ArrayList<String> sessEnemyIDs;
    private static String ip = "84.120.3.62";
    int nEnemies;

    public P2PManager(JellyObj j, Map<Integer, JellyObj> jEnemies){
        jelly=j;
        jellyEnemies=jEnemies;
    }

    private void connectServer(){
        sessEnemyIDs = new ArrayList<>();
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://"+ip+":4555/getSessionID");
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String[] data = httpResponse.getHeader("valor").toString().split(" ");
                if(!data[0].equals("fullServer"))
                    sessionID = Integer.parseInt(data[0]);
            }

            @Override
            public void failed (Throwable t) {
                Gdx.app.error("HttpRequestExample", "something went wrong getting ID", t);
            }

            @Override
            public void cancelled () {
                Gdx.app.log("HttpRequestExample", "cancelled");
            }
        });
    }

    public int tries = 0;

    public void manageP2P(){
        if(sessionID == 0 && tries == 0) {
            connectServer();
            tries++;
        }else if(sessionID != 0) {
            String textToSend = jelly.positionX + " " + jelly.positionY + " " + jelly.bullet.positionX + " " + jelly.bullet.positionY + " " + sessionID + " " + jelly.dead + " " + jelly.bullet.width + " " + jelly.bullet.height + " " + jelly.angle;
            final Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
            httpRequest.setUrl("http://" + ip + ":4555/");
            httpRequest.setHeader("value", textToSend);
            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    nEnemies = Integer.parseInt(httpResponse.getHeader("nEnemies").toString());//Numero de enemigos en la partida.
                    sessEnemyIDs = new ArrayList<>();
                    if(httpResponse.getHeader("enemyIDs")!=null)
                        sessEnemyIDs.addAll(Arrays.asList(httpResponse.getHeaders().get("enemyIDs").toString().replace("[","").replace("]","").replace(" ", "").split(",")));//IDs de los enemigos de la partida.
                    for (int i = 1; i <= httpResponse.getHeaders().size(); i++) {
                        if (httpResponse.getHeader("valor" + i) != null) {
                            String[] data = httpResponse.getHeader("valor" + i).toString().split(" ");
                            int sessID = Integer.parseInt(data[8]);
                            multiplayer = true;
                            if(jellyEnemies.containsKey(sessID)){
                                jellyEnemies.get(sessID).positionX = Float.parseFloat(data[0]);
                                jellyEnemies.get(sessID).positionY = Float.parseFloat(data[1]);
                                jellyEnemies.get(sessID).bullet.positionX = Float.parseFloat(data[2]);
                                jellyEnemies.get(sessID).bullet.positionY = Float.parseFloat(data[3]);
                                jellyEnemies.get(sessID).dead = Boolean.parseBoolean(data[4]);
                                jellyEnemies.get(sessID).bullet.width = Integer.parseInt(data[5]);
                                jellyEnemies.get(sessID).bullet.height = Integer.parseInt(data[6]);
                                jellyEnemies.get(sessID).angle = Float.parseFloat(data[7]);
                                if (jellyEnemies.get(sessID).bullet.isCollision(jelly.positionX, jelly.positionY))
                                    jelly.kill();
                            }
                        }
                    }
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("HttpRequestExample", "something went wrong", t);
                }

                @Override
                public void cancelled() {
                    Gdx.app.log("HttpRequestExample", "cancelled");
                }
            });
            moreEnemies();
        }
    }

    public static void closeSession(){
        String textToSend = sessionID+"";
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
        httpRequest.setUrl("http://"+ip+":4555/closeSession");
        httpRequest.setHeader("value", textToSend);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {

            }

            @Override
            public void failed (Throwable t) {
                Gdx.app.error("HttpRequestExample", "something went wrong closing session", t);
            }

            @Override
            public void cancelled () {
                Gdx.app.log("HttpRequestExample", "cancelled");
            }
        });
    }

    private void moreEnemies(){
        if(jellyEnemies.size()<nEnemies&&nEnemies==sessEnemyIDs.size())
            for (int i = jellyEnemies.size(); i < nEnemies; i++) {
            JellyObj enemy = new JellyObj(320, 320);
            enemy.transform();
            jellyEnemies.put(Integer.parseInt(sessEnemyIDs.get(i)), enemy);
        }
    }

}
