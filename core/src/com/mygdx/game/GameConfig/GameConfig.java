package com.mygdx.game.GameConfig;

import com.badlogic.gdx.Gdx;

public class GameConfig {

    public static final int WORLDWIDTH = 1920;
    public static final int WORLDHEIGHT = 1080;
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final float WIDTHPERCENTAGETOWORLD = (float)SCREENWIDTH/(float)WORLDWIDTH;
    public static final float HEIGHTPERCENTAGETOWORLD = (float)SCREENHEIGHT/(float)WORLDHEIGHT;
    public static final float WIDTHPERCENTAGETOSCREEN = (float)WORLDWIDTH/(float)SCREENWIDTH;
    public static final float HEIGHTPERCENTAGETOSCREEN = (float)WORLDHEIGHT/(float)SCREENHEIGHT;

}
