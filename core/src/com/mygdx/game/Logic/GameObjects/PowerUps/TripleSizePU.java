package com.mygdx.game.Logic.GameObjects.PowerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;

public class TripleSizePU extends PowerUp{

    public TripleSizePU(int x, int y) {
        super(x, y);
        img5 = new Texture("badlogic.jpg");
        sprite = new Sprite(img5);
        durability = 3;
    }

    public void applyOn(JellyObj jelly) {
        this.jelly=jelly;
        jelly.indexPowerUp(this);
        jelly.bullet.height *= 3;
        jelly.bullet.width *= 3;
    }

    @Override
    public void run() {
        durability--;
        if(durability<=0&&!destroy) {
            jelly.bullet.height /= 3;
            jelly.bullet.width /= 3;
            destroy = true;
        }
    }

}
