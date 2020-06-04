package com.mygdx.game.Logic.Physics;

import com.mygdx.game.Logic.GameObjects.GameObj;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class CarColManager implements CollisionManager {
    private GameObj gameObj;
    Stack<Float[]> lastSpots = new Stack<>();
    private ArrayList<GameObj> possibleCollides;
    float reactAngle;

    public CarColManager(GameObj g) {
        this.gameObj = g;
        this.lastSpots.push(new Float[]{Float.valueOf(this.gameObj.positionX), Float.valueOf(this.gameObj.positionY)});
    }

    public void setPosColl(ArrayList<GameObj> posColl) {
        this.possibleCollides = posColl;
    }

    public void impactReact() {
        this.reactAngle = Math.abs(this.reactAngle);
        if ((this.reactAngle < 45.0f || this.reactAngle > 135.0f) && (this.reactAngle < 225.0f || this.reactAngle > 315.0f)) {
            this.gameObj.velocityX *= -1.0f;
        } else {
            this.gameObj.velocityY *= -1.0f;
        }
        this.gameObj.power = 0.0f;
    }

    public boolean impactReaction(boolean isImpact) {
        if (!isImpact) {
            return false;
        }
        impactReact();
        return true;
    }

    private void refreshSquareVertexes() {
        this.gameObj.topX = (float) (((double) this.gameObj.positionX) + (((double) (this.gameObj.height / 2)) * Math.sin(Math.toRadians( (-this.gameObj.angle)))));
        this.gameObj.topY = (float) (((double) this.gameObj.positionY) + (((double) (this.gameObj.height / 2)) * Math.cos(Math.toRadians(this.gameObj.angle))));
        this.gameObj.bottomX = (float) (((double) this.gameObj.positionX) - (((double) (this.gameObj.height / 2)) * Math.sin(Math.toRadians((-this.gameObj.angle)))));
        this.gameObj.bottomY = (float) (((double) this.gameObj.positionY) - (((double) (this.gameObj.height / 2)) * Math.cos(Math.toRadians(this.gameObj.angle))));
        this.gameObj.rightX = (float) (((double) this.gameObj.positionX) + (((double) (this.gameObj.width / 2)) * Math.cos(Math.toRadians((-this.gameObj.angle)))));
        this.gameObj.rightY = (float) (((double) this.gameObj.positionY) + (((double) (this.gameObj.width / 2)) * Math.sin(Math.toRadians(this.gameObj.angle))));
        this.gameObj.leftX = (float) (((double) this.gameObj.positionX) - (((double) (this.gameObj.width / 2)) * Math.cos(Math.toRadians((-this.gameObj.angle)))));
        this.gameObj.leftY = (float) (((double) this.gameObj.positionY) - (((double) (this.gameObj.width / 2)) * Math.sin(Math.toRadians( this.gameObj.angle))));
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x005d  */
    public boolean isCollision() {
        int i;
        refreshSquareVertexes();
        Iterator it = this.possibleCollides.iterator();
        while (it.hasNext()) {
            GameObj t = (GameObj) it.next();
            if (t.isCollision(this.gameObj.topX, this.gameObj.topY) || t.isCollision(this.gameObj.bottomX, this.gameObj.bottomY) || t.isCollision(this.gameObj.rightX, this.gameObj.rightY) ||t.isCollision(this.gameObj.leftX, this.gameObj.leftY)) {
                List<Float> p = new ArrayList<>();
                for (i = 0; i < this.lastSpots.size(); i++) {
                    Float[] pos = this.lastSpots.pop();
                    this.gameObj.setPositionX(pos[0].floatValue());
                    this.gameObj.setPositionY(pos[1].floatValue());
                    refreshSquareVertexes();
                    if (!t.isCollision(this.gameObj.topX, this.gameObj.topY) || t.isCollision(this.gameObj.bottomX, this.gameObj.bottomY) || t.isCollision(this.gameObj.rightX, this.gameObj.rightY) || t.isCollision(this.gameObj.leftX, this.gameObj.leftY)) {
                        p.add(Float.valueOf(Math.abs(this.gameObj.positionX - t.positionY)));
                        p.add(Float.valueOf(Math.abs(this.gameObj.positionY - t.positionY)));
                        this.reactAngle = (float) Math.toDegrees(Math.atan2((this.gameObj.positionY - t.positionY), (this.gameObj.positionX - t.positionX)));
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void manageCollisions() {
        if(!lastSpots.empty())
        if (!impactReaction(isCollision()) && this.gameObj.positionX != (this.lastSpots.peek())[0].floatValue() && this.gameObj.positionY != (this.lastSpots.peek())[1].floatValue()) {
            this.lastSpots.push(new Float[]{Float.valueOf(this.gameObj.positionX), Float.valueOf(this.gameObj.positionY)});
        }
    }
}
