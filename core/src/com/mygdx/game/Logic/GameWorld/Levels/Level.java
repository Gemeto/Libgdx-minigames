package com.mygdx.game.Logic.GameWorld.Levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.Characters.EnemyObj;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.PowerUps.PowerUp;
import com.mygdx.game.Logic.Input.InputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Level {
    public Map<String, GameObj> gameObjects;
    public Map<Integer, JellyObj> onlineEnemies;
    public InputManager inputManager;
    SpriteBatch batch;
    ArrayList<PowerUp> powerUps;
    BitmapFont font;
    EnemyObj[] enemies;
    public OrthographicCamera ortographicCamera;
    Viewport viewport;

    public Level(SpriteBatch batch){
        this.batch = batch;
        gameObjects = new HashMap<String, GameObj>();
        //Camera view
        ortographicCamera = new OrthographicCamera(GameConfig.WORLDWIDTH, GameConfig.WORLDHEIGHT);
        viewport = new ExtendViewport(10, 10, ortographicCamera);
        ortographicCamera.position.set(ortographicCamera.viewportWidth / 2, ortographicCamera.viewportHeight / 2, 0);
        ortographicCamera.update();
    }


    public abstract void draw();

    public abstract void manageInput();

    public abstract void managePowerUps();

}
