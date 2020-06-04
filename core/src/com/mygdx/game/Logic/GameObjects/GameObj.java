package com.mygdx.game.Logic.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.MovementStrategies.MovementStrategyInterface;
import com.mygdx.game.Logic.MovementStrategies.CarMovementStrategy;
import com.mygdx.game.Logic.MovementStrategies.JellyfishMovementStrategy;

public abstract class GameObj {
    //Propiedades del coche
    public boolean a = false;
    public float angle;
    public float angularDrag;
    public float angularVelocity;
    public int animationFrame;
    public float bottomX;
    public float bottomY;
    public boolean canTransform;
    public float drag;
    public int frameNum;
    public int height;
    public Texture img;
    public Texture img1;
    public Texture img2;
    public Texture img3;
    public Texture img5;
    public boolean inactive;
    public float leftX;
    public float leftY;
    public MovementStrategyInterface movementStrategy = new CarMovementStrategy(this);
    public float posicionZ;
    public float positionX;
    public float positionY;
    public float power = 0.0f;
    public float rightX;
    public float rightY;
    public Sprite sprite;
    public Sprite[] sprites;
    int timeUntilTransform = 120;
    public float topX;
    public float topY;
    public float turnSpeed;
    public float velocityX;
    public float velocityY;
    public int width;

    public GameObj(int width2, int heght) {
        this.width = width2;
        this.height = heght;
        this.drag = 0.985f;
        this.turnSpeed = 0.65f;
        this.angularDrag = 0.9f;
    }

    public GameObj(int width2, int heght, double x, double y) {
        this.width = width2;
        this.height = heght;
        this.positionX = (float) x;
        this.positionY = (float) y;
        this.drag = 0.985f;
        this.turnSpeed = 0.65f;
        this.angularDrag = 0.9f;
    }

    public void setPositionX(float x) {
        this.positionX = x;
    }

    public void setPositionY(float y) {
        this.positionY = y;
    }

    public void move(Direction direction) {
        this.movementStrategy.move(direction);
    }

    public void draw(SpriteBatch batch) {
        float f;
        if (this.a) {
            this.animationFrame = this.animationFrame > this.sprites.length - 1 ? 0 : this.animationFrame;
            batch.draw(this.sprites[this.animationFrame], this.positionX - ((float) (this.width / 2)), (this.positionY + this.posicionZ) - ((float) (this.height / 2)), (float) (this.width / 2), (float) (this.height / 2), (float) this.width, (float) this.height, 1.0f, 1.0f, this.angle);
            if (this.frameNum % (60/this.sprites.length) == 0) {
                this.animationFrame = this.animationFrame >= this.sprites.length - 1 ? 0 : this.animationFrame + 1;
            }
            this.frameNum = this.frameNum == 60 ? 1 : this.frameNum + 1;

        } else if (this.sprite != null) {
            Color c = batch.getColor();
            float f2 = c.r;
            float f3 = c.g;
            float f4 = c.b;
            if (this.inactive) {
                f = 0.1f;
            } else {
                f = 1.0f;
            }
            batch.setColor(f2, f3, f4, f);
            batch.draw(this.sprite, this.positionX - ((float) (this.width / 2)), (this.positionY + this.posicionZ) - ((float) (this.height / 2)), (float) (this.width / 2), (float) (this.height / 2), (float) this.width, (float) this.height, 1.0f, 1.0f, this.angle);
        }

        this.timeUntilTransform++;
    }

    public void dispose() {
        this.img.dispose();
        this.img1.dispose();
        this.img2.dispose();
        this.img3.dispose();
        this.img5.dispose();
    }

    public void transform() {
        boolean z = true;
        if ((this.timeUntilTransform >= 120) && this.canTransform) {
            if (!this.a) {
                this.movementStrategy = new JellyfishMovementStrategy(this, true);
            } else {
                this.movementStrategy = new CarMovementStrategy(this);
            }
            if (this.a) {
                z = false;
            }
            this.a = z;
            this.timeUntilTransform = 0;
        }
    }

    public boolean isCollision(float x, float y) {
        float oWidth = ((float) (this.width / 2)) * ((float) Math.cos(Math.toRadians((double) (-this.angle))));
        float oHeight = ((float) (this.height / 2)) * ((float) Math.cos(Math.toRadians((double) this.angle)));
        if (x <= this.positionX - oWidth || y <= (this.positionY + this.posicionZ) - oHeight || x >= this.positionX + oWidth || y >= this.positionY + this.posicionZ + oHeight) {
            return false;
        }
        return true;
    }

}
