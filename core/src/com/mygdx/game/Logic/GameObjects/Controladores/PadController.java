package com.mygdx.game.Logic.GameObjects.Controladores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.GameObj;

import static com.mygdx.game.Logic.Enums.Direction.DOWN;
import static com.mygdx.game.Logic.Enums.Direction.LEFT;
import static com.mygdx.game.Logic.Enums.Direction.RIGHT;
import static com.mygdx.game.Logic.Enums.Direction.UP;

public class PadController extends GameObj{

    public float lastY;
    public float lastX;
    public float touchX;
    public float touchY;

    public PadController(int width, int heght) {
        super(width, heght);
        img5 = new Texture("boton_disparar.png");
        sprite = new Sprite(img5);
        movementStrategy = null;
        lastX = positionX;
        lastY = positionY;
    }

    public PadController(int width, int heght, double x, double y){
        super(width, heght, x, y);
        img5 = new Texture("boton_disparar.png");
        sprite = new Sprite(img5);
        movementStrategy = null;
        lastX = positionX;
        lastY = positionY;
        touchX=positionX;
        touchY=positionY;
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;
        if (clickX <= (touchX + width / 2) && clickX >= (touchX - width / 2)
                && clickY <= (touchY + height / 2) && clickY >= (touchY - height / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }

    public void moveMControllerToCharacter(float jellyPositionX, float jellyPositionY){
        positionX = jellyPositionX - (GameConfig.WORLDWIDTH/2 - touchX);
        positionY = jellyPositionY - (GameConfig.WORLDHEIGHT/2 - touchY);
    }

    public int getOrientacionX(float clickX) {
        return (int) -(touchX - clickX);
    }

    public int getOrientacionY(float clickY) {
        return (int) -(touchY - clickY);
    }

    public float getOrientationPercentageX(float clickX) {
        return (clickX-touchX) / (width/2);
    }

    public float getOrientationPercentageY(float clickY) {
        return (clickY - touchY) / (height/2);
    }

    public Direction getDirectionX(){
        if (getOrientacionX(lastX) > 0)
            return RIGHT;
        if (getOrientacionX(lastX) < 0)
            return LEFT;
        return Direction.NONE;
    }

    public Direction getDirectionY(){
        if (getOrientacionY(lastY) < 0)
            return DOWN;
        if (getOrientacionY(lastY) > 0)
            return UP;
        return Direction.NONE;
    }
}
