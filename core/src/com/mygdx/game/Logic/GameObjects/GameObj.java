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
    public float positionX, positionY, posicionZ;
    public float velocityX, velocityY;
    public float angularVelocity;
    public float angularDrag, drag;
    public int animationFrame, frameNum;
    public boolean isSkidding;
    public float angle;
    public float power;
    public float turnSpeed;
    public int width, height;
    public MovementStrategyInterface movementStrategy;
    public Sprite[] sprites;
    public Sprite sprite;
    public Texture img;
    public Texture img1;
    public Texture img2;
    public Texture img3;
    public Texture img5;
    public boolean canTransform;

    public GameObj(int width, int heght) {
        movementStrategy = new CarMovementStrategy(this);

        //Adaptamos el ancho y alto a los valores pasados por parametro
        this.width = width;
        this.height = heght;

        //Inicializamos la velcidad a la que se detiene el coche y gira
        drag = 0.985f;
        turnSpeed = 0.65f;
        angularDrag = 0.9f;
    }

    public GameObj(int width, int heght, double x, double y){
        movementStrategy = new CarMovementStrategy(this);

        //Adaptamos el ancho y alto a los valores pasados por parametro
        this.width = width;
        this.height = heght;
        this.positionX = (float)x;
        this.positionY = (float)y;

        //Inicializamos la velcidad a la que se detiene el coche y gira
        drag = 0.985f;
        turnSpeed = 0.65f;
        angularDrag = 0.9f;
    }

    public void move(Direction direction) {
        movementStrategy.move(direction);
    }

    public boolean inactive;
    public void draw(SpriteBatch batch) {
        if(a) {
            batch.draw(sprites[animationFrame], positionX - (width / 2), (positionY + posicionZ) - (height / 2), width / 2, height / 2, width, height, 1, 1, angle);
        }else {
            Color c = batch.getColor();
            batch.setColor(c.r, c.g, c.b, inactive ? 0.1f:1);
            batch.draw(sprite, positionX - (width / 2), (positionY + posicionZ) - (height / 2), width / 2, height / 2, width, height, 1, 1, angle);
        }
        if (frameNum % 15 == 0)
            animationFrame = (animationFrame == 3) ? 0 : animationFrame + 1;
        frameNum = (frameNum == 60) ? 1 : frameNum + 1;
        timeUntilTransform++;
    }

    public void dispose() {
        img.dispose();
        img1.dispose();
        img2.dispose();
        img3.dispose();
        img5.dispose();
    }

    int timeUntilTransform = 120;
    public boolean a = false; //0 coche / 1 medusa

    public void transform(){

        if(timeUntilTransform >= 120 & canTransform) {
            if (!a) {
                movementStrategy = new JellyfishMovementStrategy(this);
            } else {
                movementStrategy = new CarMovementStrategy(this);
            }
            a = !a;
            timeUntilTransform = 0;
        }
    }

    public boolean isCollision(float x, float y){
        if (x > (positionX - width/2) && y > (positionY + posicionZ - height/2) && x < (positionX + width/2) && y < (positionY + posicionZ + height/2)){
            return true;
        }
        return false;
    }
}
