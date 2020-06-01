package com.mygdx.game.Logic.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TileObj extends GameObj{
    public int i;
    public int j;

    public TileObj(int width, int heght, double x, double y) {
        super(width, heght, x, y);
        img5 = new Texture("badlogic.jpg");
        sprite = new Sprite(img5);
    }

}
