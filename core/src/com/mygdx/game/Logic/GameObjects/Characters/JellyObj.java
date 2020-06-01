package com.mygdx.game.Logic.GameObjects.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.BulletObj;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.PowerUps.PowerUp;

import java.util.ArrayList;

public class JellyObj extends GameObj {

    public BulletObj bullet;
    public boolean dead = false;
    public ArrayList<PowerUp> pUps;
    public int nDeaths;

    public JellyObj(int width, int heght) {
        super(width, heght);
        initializeJelly();
    }

    public JellyObj(int width, int heght, double x, double y) {
        super(width, heght, x, y);
        initializeJelly();
    }

    public void runPowerUps(){
        for(PowerUp p:pUps)
            p.run();
    }

    public void indexPowerUp(PowerUp p){
        pUps.add(p);
    }

    public void shoot(float vX, float vY){
        if(weaponAttached) {
            weaponAttached = false;

            bullet.positionX = this.positionX;
            bullet.positionY = this.positionY;

            bullet.velocityX = vX*bullet.defaultVelX;
            bullet.velocityY = vY*bullet.defaultVelY;
            runPowerUps();
        }
    }

    public boolean weaponReturn;
    public boolean weaponAttached = true;
    public void weaponReturn(){
        weaponReturn = true;
    }

    public void weaponReturnExe() {
        if (weaponReturn) {
            bullet.angle = (float) Math.atan2(positionY - bullet.positionY, positionX - bullet.positionX);
            if (bullet.angle < 0) {
                bullet.angle += 360;
            }
            bullet.velocityX += (Math.sin(bullet.angle * 2));
            bullet.velocityY -= (Math.cos(bullet.angle * 2));
        }
        if (isCollision(bullet.positionX, bullet.positionY) == true && weaponReturn){
            bullet.velocityX = 0;
            bullet.velocityY = 0;
            weaponReturn = false;
            weaponAttached = true;
    }
    }

    public void moveJelly(Direction direction){
        weaponReturnExe();
        if(weaponAttached) {
            bullet.positionX = positionX;
            bullet.positionY = positionY+posicionZ;
        }
        bullet.move(Direction.NONE);
        move(direction);
    }

    public void drawJelly(SpriteBatch batch){
        if(!dead) {
            bullet.draw(batch);
            this.draw(batch);
        }
    }

    public void initializeJelly(){
        //Creamos una textura por cada imagen del objeto
        img = new Texture("MedusaWalk1.png");
        img1 = new Texture("MedusaWalk2.png");
        img2 = new Texture("MedusaWalk3.png");
        img3 = new Texture("MedusaWalk4.png");
        img5 = new Texture("car.png");
        sprite = new Sprite(img5);
        //Y las pasamos a una lista de sprites
        sprites = new Sprite[4];
        sprites[0] = new Sprite(img);
        sprites[1] = new Sprite(img1);
        sprites[2] = new Sprite(img2);
        sprites[3] = new Sprite(img3);
        positionX = GameConfig.WORLDWIDTH*0.5f;

        bullet = new BulletObj(25, 25);
        bullet.positionY = positionY+posicionZ;
        bullet.positionX = positionX;
        canTransform = true;
        pUps = new ArrayList<PowerUp>();
    }

    public void kill(){
        if(!dead) {
            dead = true;
            nDeaths++;
        }
    }
}
