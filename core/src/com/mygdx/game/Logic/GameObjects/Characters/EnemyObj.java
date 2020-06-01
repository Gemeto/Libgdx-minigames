package com.mygdx.game.Logic.GameObjects.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Logic.Enums.Direction;
import com.mygdx.game.Logic.GameObjects.GameObj;

import java.util.Random;

public class EnemyObj extends GameObj {

    public EnemyObj(int width, int heght) {
        super(width, heght);
        img5 = new Texture("coche.jpg");
        sprite = new Sprite(img5);
    }

    int tiempoPulsando = 0;
    int direccion = 0; //0 - Abajo; 1 - Arriba; 2 - Izquierda; 3 - Derecha
    public void moveAuto() {
        if(tiempoPulsando <= 0){
            int max = 3;
            int min = 1;
            Random random = new Random();
            direccion = random.nextInt((max - min + 1) + min);
            if(direccion==0)
                System.out.print("JODER");
            max = 25;
            min = 2;
           tiempoPulsando = random.nextInt((max - min + 1) + min);
        }
        if(direccion != 0)
            move(Direction.values()[direccion]);
        tiempoPulsando--;
        move(Direction.NONE);
    }

}
