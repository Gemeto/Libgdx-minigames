package com.mygdx.game.Logic.GameObjects.PowerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;

public class TripleVelocityPU extends PowerUp{

    public TripleVelocityPU(int x, int y) {
        super(x, y);
        img5 = new Texture("badlogic.jpg");
        sprite = new Sprite(img5);
        durability = 1;
    }

    public void applyOn(JellyObj jelly) {
        this.jelly = jelly;
        jelly.indexPowerUp(this);
        jelly.bullet.defaultVelX = 300;
        jelly.bullet.defaultVelY = 300;
    }

    @Override
    public void run() {
        durability--;
        if(durability<=0&&!destroy) {
            jelly.bullet.defaultVelX = 100;
            jelly.bullet.defaultVelY = 100;
            destroy = true;
        }
    }
}