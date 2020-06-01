package com.mygdx.game.Logic.GameObjects.PowerUps;

import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;

public abstract class PowerUp extends GameObj{

    JellyObj jelly;
    int durability;
    public boolean destroy;

    public PowerUp(float x, float y){
        super(180,180, x, y);
    }

    public abstract void applyOn(JellyObj jelly);

    public abstract void run();

}
