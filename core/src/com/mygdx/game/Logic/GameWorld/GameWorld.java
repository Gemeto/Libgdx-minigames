package com.mygdx.game.Logic.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.Controladores.PadController;
import com.mygdx.game.Logic.GameWorld.Levels.CarLevel;
import com.mygdx.game.Logic.GameWorld.Levels.JellyLevel;
import com.mygdx.game.Logic.GameWorld.Levels.Level;
import com.mygdx.game.Logic.MovementStrategies.CarMovementStrategy;
import com.mygdx.game.Logic.Online.P2PManager;
import com.mygdx.game.Utils.Logger;

import java.util.ArrayList;
import java.util.Map;

public class GameWorld {
    Level state; //<- Gestion de niveles
    SpriteBatch batch;
    Logger logger;
    P2PManager p2PManager;

    public GameWorld() {
        //Plane to draw
        batch = new SpriteBatch();

        //Level
        state = new JellyLevel(batch);

        //Game stuff needed
        logger = new Logger();

        //Online
        p2PManager = new P2PManager((JellyObj) state.gameObjects.get("jelly"), state.onlineEnemies);
    }

    private void changeLevel(Level level){
        p2PManager.closeSession();
        this.state = level;
        p2PManager = new P2PManager((JellyObj) state.gameObjects.get("jelly"), state.onlineEnemies);
    }

    public void draw() {
        if(state.inputManager.changeLevel){
            if(state instanceof JellyLevel) {
                changeLevel(new CarLevel(batch));
            }else {
                changeLevel(new JellyLevel(batch));
            }
        }
        //Level functions
        state.managePowerUps();
        state.manageInput();

        //Online
        if(p2PManager.tries < 20)//Si no se conecta al servidor crashea el juego así que le damos 20 intentos como máximo.
            p2PManager.manageP2P();

        if(state instanceof  CarLevel && state.gameObjects.get("jelly").movementStrategy instanceof CarMovementStrategy){
            CarMovementStrategy cMS = (CarMovementStrategy) state.gameObjects.get("jelly").movementStrategy;
            cMS.enemies = new ArrayList<>();
            for(Map.Entry e:p2PManager.jellyEnemies.entrySet())
                cMS.enemies.add((JellyObj) e.getValue());
        }

        //Drawing
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(state.ortographicCamera.combined);
        batch.begin();
        state.draw();
        batch.end();

        //Debug log
        logger.logGameInfo();
    }
}