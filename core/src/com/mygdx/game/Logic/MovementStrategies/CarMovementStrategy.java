package com.mygdx.game.Logic.MovementStrategies;

import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.MovementStrategies.MovementStrategyInterface;
import com.mygdx.game.Logic.Physics.CarColManager;
import com.mygdx.game.Logic.Physics.CollisionManager;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.toRadians;

public class CarMovementStrategy implements MovementStrategyInterface {

    GameObj gameObj;
    public ArrayList<JellyObj> enemies;
    public int driftPoints;
    private CollisionManager cM;

    public CarMovementStrategy(GameObj gameObj){
        gameObj.power = 0;
        this.gameObj = gameObj;
        this.gameObj.velocityX = 0;
        this.gameObj.velocityY = 0;
        enemies = new ArrayList<>();
        this.cM = new CarColManager(gameObj);
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
            this.cM.manageCollisions();
            this.gameObj.positionX += this.gameObj.velocityX;
            this.gameObj.positionY += this.gameObj.velocityY;
            this.gameObj.velocityX *= this.gameObj.drag;
            this.gameObj.velocityY *= this.gameObj.drag;
            this.gameObj.angle += this.gameObj.angularVelocity;
            this.gameObj.angularVelocity *= this.gameObj.angularDrag;
            GameObj gameObj2 = this.gameObj;
            gameObj2.velocityX = (float) (((double) gameObj2.velocityX) + (Math.sin(Math.toRadians((double) (-this.gameObj.angle))) * ((double) this.gameObj.power)));
            GameObj gameObj3 = this.gameObj;
            gameObj3.velocityY = (float) (((double) gameObj3.velocityY) + (Math.cos(Math.toRadians((double) this.gameObj.angle)) * ((double) this.gameObj.power)));
            float velAngle = (float) Math.toDegrees(Math.atan2((double) (this.gameObj.positionY - (this.gameObj.positionY + this.gameObj.velocityY)), (double) (this.gameObj.positionX - (this.gameObj.positionX + this.gameObj.velocityX))));
            float angle2 = (float) Math.toDegrees(Math.atan2(((double) this.gameObj.positionY) - (((double) this.gameObj.positionY) + (((double) (this.gameObj.height / 2)) * Math.cos(Math.toRadians((double) this.gameObj.angle)))), ((double) this.gameObj.positionX) - (((double) this.gameObj.positionX) + (((double) (this.gameObj.height / 2)) * Math.sin(Math.toRadians((double) (-this.gameObj.angle)))))));
            if (Math.abs(velAngle - angle2) <= 30.0f) {
                return;
            }
            if (((double) Math.abs(this.gameObj.velocityX)) > 7.5d || ((double) Math.abs(this.gameObj.velocityY)) > 7.5d) {
                this.driftPoints++;
            }
        }
    }

    public void setSolidObjects(ArrayList<GameObj> gameObjs) {
        this.cM.setPosColl(gameObjs);
    }

}
