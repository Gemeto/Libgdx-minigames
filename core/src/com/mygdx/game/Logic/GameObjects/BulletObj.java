package com.mygdx.game.Logic.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BulletObj extends GameObj{

    public float defaultVelX;
    public float defaultVelY;
    public float lastVelocityX;
    public float lastVelocityY;

    public BulletObj(int width, int heght) {
        super(width, heght);
        defaultVelX = 100;
        defaultVelY = 100;
        lastVelocityX = defaultVelX;
        lastVelocityY = defaultVelY;
        //Creamos una textura por cada imagen del objeto
        img5 = new Texture("bullet.jpg");
        sprite = new Sprite(img5);
    }

    public void setToLastVel(){
        defaultVelY = lastVelocityY;
        defaultVelX = lastVelocityX;
    }
}
