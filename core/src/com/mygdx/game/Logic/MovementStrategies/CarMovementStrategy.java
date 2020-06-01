package com.mygdx.game.Logic.MovementStrategies;

import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.MovementStrategies.MovementStrategyInterface;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.toRadians;

public class CarMovementStrategy implements MovementStrategyInterface {

    GameObj gameObj;
    ArrayList<TileObj> tiles;
    public ArrayList<JellyObj> enemies;
    public int driftPoints;

    public CarMovementStrategy(GameObj gameObj){
        gameObj.power = 0;
        this.gameObj = gameObj;
        this.gameObj.velocityX = 0;
        this.gameObj.velocityY = 0;
        enemies = new ArrayList<>();
    }

    private void moveUp() {
        gameObj.power += (gameObj.power>=0.25) ? 0:0.03;
    }

    public void moveDown() {
        gameObj.velocityX *= 0.65f;
        gameObj.velocityY *= 0.65f;
        gameObj.power *= 0.65f;
        //	car.power -= (car.power<=-0.25) ? 0:0.03;
    }

    public void moveRight() {
        gameObj.angularVelocity -= gameObj.turnSpeed;
        //gameObj.power -= (gameObj.power<=0.075) ? gameObj.power:0.07;
    }

    public void moveLeft() {
        gameObj.angularVelocity += gameObj.turnSpeed;
        //gameObj.power -= (gameObj.power<=0.075) ? gameObj.power:0.07;
    }

    public void move(Direction direction) {
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
        if (move) {
            gameObj.positionX += gameObj.velocityX;
            gameObj.positionY += gameObj.velocityY;

            //if(gameObj.positionX < 15 ||
            if(collisionLeft()){
                impactReaction(Direction.LEFT);
            } //else if(gameObj.positionY < 0 ||
            else if(collisionDown()){
                impactReaction(Direction.DOWN);
            } //else if(gameObj.positionY > GameConfig.WORLDHEIGHT ||
            else if(collisionUp()){
                impactReaction(Direction.UP);
            } //else if(gameObj.positionX > GameConfig.WORLDWIDTH ||
            else if(collisionRight()){
                impactReaction(Direction.RIGHT);
            }
            gameObj.velocityX *= gameObj.drag;
            gameObj.velocityY *= gameObj.drag;
            gameObj.angle += gameObj.angularVelocity;
            gameObj.angularVelocity *= gameObj.angularDrag;

            gameObj.velocityX += (Math.sin(Math.toRadians(-gameObj.angle)) * gameObj.power);
            gameObj.velocityY += (Math.cos(Math.toRadians(gameObj.angle)) * gameObj.power);

            float velAngle = (float) Math.toDegrees(Math.atan2(gameObj.positionY - (gameObj.positionY+gameObj.velocityY), gameObj.positionX - (gameObj.positionX+gameObj.velocityX)));
            float angle2 = (float) Math.toDegrees(Math.atan2(gameObj.positionY - (gameObj.positionY+(gameObj.height/2)*Math.cos(Math.toRadians(gameObj.angle))), gameObj.positionX -(gameObj.positionX+(gameObj.height/2)*Math.sin(Math.toRadians(-gameObj.angle)))));

            if((Math.abs(velAngle - angle2) > 30) && (Math.abs(gameObj.velocityX) > 7.5 || Math.abs(gameObj.velocityY) > 7.5)) {
                driftPoints++;
                System.out.println(driftPoints + " " + velAngle + " " + angle2);
            }

        }
    }

    public void impactReaction(Direction direction){
        Random random = new Random();
        int newAngle = random.nextInt((60 - 0 + 1) + 0);
        switch(direction){
            case UP:
                gameObj.velocityY *= -1;
                gameObj.velocityX = (float)(gameObj.velocityY * sin(toRadians(-newAngle)));
                break;
            case DOWN:
                gameObj.velocityY *= -1;
                gameObj.velocityX = (float)(gameObj.velocityY * sin(toRadians(-newAngle)));
                break;
            case RIGHT:
                gameObj.velocityX *= -1;
                gameObj.velocityY = (float)(gameObj.velocityX * cos(toRadians(newAngle)));
                break;
            case LEFT:
                gameObj.velocityX *= -1;
                gameObj.velocityY = (float)(gameObj.velocityX * cos(toRadians(newAngle)));
                break;
        }
    }

    public void setTiles(ArrayList<TileObj> tiles){
        this.tiles = tiles;
    }

    private boolean collisionUp(){
        for(TileObj t : tiles) {
            if ((gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY) && (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        for(JellyObj t:enemies){
            if ((gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY) && (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        return false;
    }

    private boolean collisionDown(){
        for(TileObj t : tiles) {
            if ((gameObj.positionY >= t.positionY && gameObj.positionY <= t.positionY + t.height/2) && (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        for(JellyObj t : enemies) {
            if ((gameObj.positionY >= t.positionY && gameObj.positionY <= t.positionY + t.height/2) && (gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX + t.width / 2))
                return true;
        }
        return false;
    }

    private boolean collisionRight(){
        for(TileObj t : tiles) {
            if ((gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX) && (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        for(JellyObj t : enemies) {
            if ((gameObj.positionX >= t.positionX - t.width/2 && gameObj.positionX <= t.positionX) && (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        return false;
    }

    private boolean collisionLeft(){
        for(TileObj t : tiles) {
            if ((gameObj.positionX >= t.positionX && gameObj.positionX <= t.positionX + t.width/2) && (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        for(JellyObj t : enemies) {
            if ((gameObj.positionX >= t.positionX && gameObj.positionX <= t.positionX + t.width/2) && (gameObj.positionY >= t.positionY - t.height/2 && gameObj.positionY <= t.positionY + t.height / 2))
                return true;
        }
        return false;
    }

}
