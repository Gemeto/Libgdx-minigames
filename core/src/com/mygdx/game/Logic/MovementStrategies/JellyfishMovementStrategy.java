package com.mygdx.game.Logic.MovementStrategies;

import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.MovementStrategies.MovementStrategyInterface;

import java.util.ArrayList;
import java.util.Arrays;

public class JellyfishMovementStrategy implements MovementStrategyInterface {

    GameObj gameObj;
    ArrayList<GameObj> tiles;
    private boolean screenIsLimit;

    public JellyfishMovementStrategy(GameObj gameObj, boolean screenIsLimit){
        this.gameObj = gameObj;
        this.gameObj.power = 10;
        this.gameObj.angle = 0;
        this.screenIsLimit = screenIsLimit;
    }

    public void moveUp() {
        if(!collisionUp())
            if(screenIsLimit) {
                if (gameObj.positionY < 1080)
                    gameObj.positionY += gameObj.power;
            }else
                gameObj.positionY += gameObj.power;
    }

    public void moveDown() {
        if(!collisionDown())
            if(screenIsLimit) {
                if (gameObj.positionY > 0)
                    gameObj.positionY -= gameObj.power;
            }else
                gameObj.positionY -= gameObj.power;
    }

    public void moveRight() {
        if(!collisionRight())
            if(screenIsLimit) {
                if (gameObj.positionX < 1920)
                    gameObj.positionX += gameObj.power;
            }else
                gameObj.positionX += gameObj.power;
    }

    public void moveLeft() {
        if(gameObj.positionX > 0)
        if(!collisionLeft())
            if(screenIsLimit) {
                if (gameObj.positionX > 0)
                    gameObj.positionX -= gameObj.power;
            }else
                gameObj.positionX -= gameObj.power;
    }
    int frame = 0;
    boolean arriba = true;

    @Override
    public void move(Direction direction){
        boolean move = true;
        switch(direction){
            case RIGHT:
                moveRight();
                move = false;
                break;
            case LEFT:
                moveLeft();
                move = false;
                break;
            case UP:
                moveUp();
                move = false;
                break;
            case DOWN:
                moveDown();
                move = false;
                break;
        }
        if(move) {
            frame++;
            if (frame % 60 == 0)
                arriba = !arriba;
            if (frame % 5 == 0) {
                float pZ = gameObj.posicionZ;
                gameObj.posicionZ += (pZ < 120 && pZ > -120) ? (arriba) ? 10 : -10 : (pZ == 120) ? -10 : 10;
            }
        }
    }

    public void setTiles(ArrayList<GameObj> tiles){
        this.tiles = tiles;
    }

    private boolean collisionUp(){
        for(GameObj t : tiles) {
            if ((gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY) &&
                    (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        return false;
    }

    private boolean collisionDown(){
        for(GameObj t : tiles) {
            if ((gameObj.positionY >= t.positionY && gameObj.positionY <= t.positionY + t.height/2) &&
                    (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        return false;
    }

    private boolean collisionRight(){
        for(GameObj t : tiles) {
            if ((gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX) &&
                    (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        return false;
    }

    private boolean collisionLeft(){
        for(GameObj t : tiles) {
            if ((gameObj.positionX >= t.positionX && gameObj.positionX <= t.positionX + t.width/2) &&
                    (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        return false;
    }

}
