package com.mygdx.game.Logic.GameWorld.Levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.Controladores.PadController;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.PowerUps.PowerUp;
import com.mygdx.game.Logic.GameObjects.PowerUps.TripleSizePU;
import com.mygdx.game.Logic.GameObjects.PowerUps.TripleVelocityPU;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.Input.InputManager;
import com.mygdx.game.Logic.MovementStrategies.CarMovementStrategy;
import com.mygdx.game.Logic.MovementStrategies.JellyfishMovementStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JellyLevel extends Level {

    ArrayList<TileObj> tiles;

    public JellyLevel(SpriteBatch batch){
        super(batch);

        //Font
        font = new BitmapFont();

        //Tiles
        tiles = new ArrayList<>();


        //MechanicsSection
        //Controllers for android
        gameObjects.put("shootController", new PadController(320, 320, GameConfig.WORLDWIDTH * 0.85, GameConfig.WORLDHEIGHT * 0.15));
        gameObjects.put("moveController", new PadController(320, 320, GameConfig.WORLDWIDTH * 0.15, GameConfig.WORLDHEIGHT * 0.15));

        //Players
        gameObjects.put("jelly", new JellyObj(320, 320, GameConfig.WORLDWIDTH * 0.5, GameConfig.WORLDHEIGHT * 0.15));
        gameObjects.get("jelly").transform();
        JellyfishMovementStrategy jMS = (JellyfishMovementStrategy) gameObjects.get("jelly").movementStrategy;
        jMS.setTiles(tiles);
        JellyObj j = (JellyObj) gameObjects.get("jelly");
        CarMovementStrategy jMSC = (CarMovementStrategy)j.bullet.movementStrategy;
        jMSC.setTiles(tiles);

        //PowerUps
        powerUps = new ArrayList<>();

        //Enemies
        onlineEnemies = new HashMap<>();

        //Input
        inputManager = new InputManager((PadController)gameObjects.get("moveController"), (PadController)gameObjects.get("shootController"), (JellyObj) gameObjects.get("jelly"));

    }


    public void draw() {

        //Tiles
        for(TileObj t: tiles)
            t.draw(batch);

        JellyObj jelly;
        for (Map.Entry<String, GameObj> entry : gameObjects.entrySet())
            if (entry.getValue() instanceof JellyObj) {
                jelly = (JellyObj) entry.getValue();
                jelly.drawJelly(batch);
                GlyphLayout g = new GlyphLayout();
                g.setText(font, "Muertes: " + jelly.nDeaths);
                font.draw(batch, "Muertes: " + jelly.nDeaths, entry.getValue().positionX - g.width/2, entry.getValue().positionY + entry.getValue().posicionZ + entry.getValue().height/3);
            }else {
                entry.getValue().draw(batch);
            }

        //Online enemies
        for (Map.Entry<Integer, JellyObj> jE : onlineEnemies.entrySet())
            jE.getValue().drawJelly(batch);
    }

    public void manageInput(){
        inputManager.manageInput();
        PadController movement = (PadController)gameObjects.get("moveController");
        JellyObj character = (JellyObj)gameObjects.get("jelly");
        if(movement.getDirectionX() != Direction.NONE && Math.abs(movement.getOrientationPercentageX(movement.lastX)) > Math.abs(movement.getOrientationPercentageY(movement.lastY)))
            character.moveJelly(movement.getDirectionX());
        else if(movement.getDirectionY() != Direction.NONE && Math.abs(movement.getOrientationPercentageY(movement.lastY)) > Math.abs(movement.getOrientationPercentageX(movement.lastX)))
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