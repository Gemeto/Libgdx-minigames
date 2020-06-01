package com.mygdx.game.Logic.GameWorld.Levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.BulletObj;
import com.mygdx.game.Logic.GameObjects.Controladores.PadController;
import com.mygdx.game.Logic.GameObjects.Characters.EnemyObj;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.PowerUps.PowerUp;
import com.mygdx.game.Logic.GameObjects.PowerUps.TripleSizePU;
import com.mygdx.game.Logic.GameObjects.PowerUps.TripleVelocityPU;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.Input.InputManager;
import com.mygdx.game.Logic.MovementStrategies.CarMovementStrategy;
import com.mygdx.game.Logic.MovementStrategies.JellyfishMovementStrategy;
import com.mygdx.game.Utils.TileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CarLevel extends Level {

    ArrayList<GameObj> tiles;
    Sprite background;
    Texture bG;
    BulletObj b;
    BulletObj b2;
    TileManager tM;

    public CarLevel(SpriteBatch batch){
        super(batch);

        //Font
        font = new BitmapFont();
        font.getData().setScale(3f, 3f);

        //Background
        bG = new Texture("rm.jpg");
        background = new Sprite(bG);

        //Tiles
        tM = new TileManager();
        tiles = tM.loadTilesFromTxtSlow("sample2.txt");
        //MechanicsSection
        //Controllers for android
        gameObjects.put("shootController", new PadController(320, 320, GameConfig.WORLDWIDTH * 0.85, GameConfig.WORLDHEIGHT * 0.15));
        gameObjects.put("moveController", new PadController(320, 320, GameConfig.WORLDWIDTH * 0.15, GameConfig.WORLDHEIGHT * 0.15));
        //Players
        gameObjects.put("jelly", new JellyObj(100, 180, 960.0d, 432.0d));
        CarMovementStrategy jMS = (CarMovementStrategy)gameObjects.get("jelly").movementStrategy;
        jMS.setSolidObjects(tiles);
        JellyObj j = (JellyObj) gameObjects.get("jelly");
        jMS = (CarMovementStrategy)j.bullet.movementStrategy;
        jMS.setSolidObjects(tiles);

        //PowerUps
        powerUps = new ArrayList<>();

        //Enemies
        enemies = new EnemyObj[14];
        for (int i = 0; i < enemies.length; i++) {
          enemies[i] = new EnemyObj(120, 160);
          jMS = (CarMovementStrategy)enemies[i].movementStrategy;
          jMS.setSolidObjects(tiles);
        }
        onlineEnemies = new HashMap<>();

        //Input
        inputManager = new InputManager((PadController)gameObjects.get("moveController"), (PadController)gameObjects.get("shootController"), (JellyObj) gameObjects.get("jelly"));

        b = new BulletObj(10, 10);
        b2 = new BulletObj(10, 10);
    }

    public void draw() {
        tM.drawTiles();

        moveCameraToCharacter();

        //Background
        batch.draw(background, 0, 0, 0, 0, GameConfig.WORLDWIDTH*4, GameConfig.WORLDHEIGHT*4, 1, 1, 0);


        //Tiles
        GameObj jely = gameObjects.get("jelly");
        for(GameObj t: tiles) {
            if (t.positionX > jely.positionX - 960.0f && t.positionX < jely.positionX + 960.0f && t.positionY > jely.positionY - 540.0f && t.positionY < jely.positionY + 540.0f)
                t.draw(batch);
        }

        JellyObj jelly;
        for (Map.Entry<String, GameObj> entry : gameObjects.entrySet())
            if (entry.getValue() instanceof JellyObj) {
                jelly = (JellyObj) entry.getValue();
                jelly.drawJelly(batch);
                CarMovementStrategy cMS = (CarMovementStrategy) jelly.movementStrategy;
                GlyphLayout g = new GlyphLayout();
                g.setText(font, "Puntos de drift: " + cMS.driftPoints);
                font.draw(batch, "Puntos de drift: " + cMS.driftPoints, jelly.positionX - jelly.width, jelly.positionY + jelly.height/1.65f);
                debugDriftPointsDraw(jelly);
        }else {
                entry.getValue().draw(batch);
            }
        //Online enemies
        for (Map.Entry<Integer, JellyObj> jE : onlineEnemies.entrySet())
            jE.getValue().drawJelly(batch);

        for(EnemyObj enemy:enemies)
            if(enemy != null)
                enemy.draw(batch);
    }

    //Debug cosas
    private void debugDriftPointsDraw(JellyObj jelly) {
        b.positionX=(float)(jelly.positionX + (jelly.height/2) * Math.sin(Math.toRadians(-jelly.angle)));
        b.positionY=(float)(jelly.positionY + (jelly.height/2) * Math.cos(Math.toRadians(jelly.angle)));
        b.draw(batch);
        b2.positionX=(float)(jelly.positionX+jelly.velocityX);
        b2.positionY=(float)(jelly.positionY+jelly.velocityY);
        b2.draw(batch);
    }

    private void moveCameraToCharacter(){
        PadController mC = (PadController) gameObjects.get("moveController");
        mC.moveMControllerToCharacter(gameObjects.get("jelly").positionX, gameObjects.get("jelly").positionY);
        mC = (PadController) gameObjects.get("shootController");
        mC.moveMControllerToCharacter(gameObjects.get("jelly").positionX, gameObjects.get("jelly").positionY);
        ortographicCamera.position.x = gameObjects.get("jelly").positionX;
        ortographicCamera.position.y = gameObjects.get("jelly").positionY;
        ortographicCamera.update();
    }

    public void manageInput(){
        //Input
        inputManager.manageInput2();
        PadController movement = (PadController)gameObjects.get("moveController");
        PadController movementX = (PadController)gameObjects.get("shootController");
        JellyObj character = (JellyObj)gameObjects.get("jelly");
        if(movementX.getDirectionX() != Direction.NONE)
            character.moveJelly(movementX.getDirectionX());
        if(movement.getDirectionY() != Direction.NONE)
            character.moveJelly(movement.getDirectionY());
        character.moveJelly(Direction.NONE);


    }

    public void managePowerUps(){
        int x;
        int y;
        if(powerUps.size()==0) {
            x = new Random().nextInt((GameConfig.WORLDWIDTH - 0 + 1) + 0);
            y = new Random().nextInt((GameConfig.WORLDHEIGHT - 0 + 1) + 0);
            if (new Random().nextInt((2 - 1 + 1) + 1) == 1) {
                powerUps.add(new TripleVelocityPU(x, y));
                gameObjects.put("powerUp"+powerUps.size(), powerUps.get(powerUps.size()-1));
            }
            else {
                powerUps.add(new TripleSizePU(x, y));
                gameObjects.put("powerUp"+powerUps.size(), powerUps.get(powerUps.size()-1));
            }
        }

        for(PowerUp p:powerUps) {
            if (gameObjects.get("jelly").isCollision(p.positionX, p.positionY)) {
                p.applyOn((JellyObj) gameObjects.get("jelly"));
                powerUps.remove(p);
                gameObjects.remove(p);
                break;
            }
        }
    }

}