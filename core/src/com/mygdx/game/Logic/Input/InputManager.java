package com.mygdx.game.Logic.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.Controladores.PadController;
import com.mygdx.game.Logic.GameObjects.Characters.JellyObj;

import static com.mygdx.game.Logic.Enums.Direction.DOWN;
import static com.mygdx.game.Logic.Enums.Direction.LEFT;
import static com.mygdx.game.Logic.Enums.Direction.RIGHT;
import static com.mygdx.game.Logic.Enums.Direction.UP;

public class InputManager extends InputAdapter {

    PadController moveController;
    PadController shootController;
    PadController interactController;
    JellyObj jelly;
    public boolean changeLevel;

    public InputManager(PadController moveController, PadController shootController, JellyObj jelly){
        this.moveController = moveController;
        this.shootController = shootController;
        this.jelly = jelly;
    }

    public InputManager(PadController moveController, PadController shootController, PadController interactController, JellyObj jelly){
        this.moveController = moveController;
        this.shootController = shootController;
        this.interactController = interactController;
        this.jelly = jelly;
    }

    public void manageInput(){
        //PC Input
        managePCInput();
        //Android input
        Gdx.input.setInputProcessor(new InputAdapter(){

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer){
                if(moveController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    moveController.lastX = screenX*GameConfig.WIDTHPERCENTAGETOSCREEN;
                    moveController. lastY = GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN;
                }else{
                    moveController.lastX = moveController.touchX;
                    moveController. lastY = moveController.touchY;
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button){
                moveController.lastX = moveController.touchX;
                moveController. lastY = moveController.touchY;
                if(shootController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    jelly.shoot(shootController.getOrientationPercentageX(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN), shootController.getOrientationPercentageY(GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN));
                    jelly.bullet.setToLastVel();
                }
                if(jelly.dead)
                    jelly.dead = false;
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(moveController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    moveController.lastX = screenX*GameConfig.WIDTHPERCENTAGETOSCREEN;
                    moveController. lastY = GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN;
                }else{
                    moveController.lastX = moveController.touchX;
                    moveController. lastY = moveController.touchY;
                    if(interactController != null)
                        interactController.isPressed = false;
                }

                if(jelly.isCollision(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    changeLevel = true;
                if(shootController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    jelly.weaponReturn();
                if(interactController != null)
                    if(interactController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                        interactController.isPressed = true;
                    }

                return true;
            }

        });
    }
    public void manageInput2(){
        //PC Input
        managePCInput();
        //Android input
        Gdx.input.setInputProcessor(new InputAdapter(){

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer){
                if(moveController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    moveController.lastY = GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN;
                }else{
                    moveController.lastY = moveController.touchY;
                }
                if(jelly.isCollision(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    changeLevel = true;
                if(shootController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    shootController.lastX = screenX*GameConfig.WIDTHPERCENTAGETOSCREEN;
                else
                    shootController.lastX = shootController.touchX;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button){
                moveController.lastY = moveController.touchY;
                shootController. lastX = shootController.touchX;
                interactController.isPressed = false;
                return true;
            }
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(moveController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    moveController. lastY = GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN;
                }else{
                    moveController. lastY = moveController.touchY;
                }

                if(jelly.isCollision(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    changeLevel = true;
                if(shootController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN))
                    shootController.lastX = screenX*GameConfig.WIDTHPERCENTAGETOSCREEN;
                else
                    shootController.lastX = shootController.touchX;

                if(interactController.estaPulsado(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN)){
                    interactController.isPressed = true;
                }

                return true;
            }

        });
    }
    private void managePCInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            jelly.transform();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            jelly.move(UP);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            jelly.move(DOWN);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            jelly.move(RIGHT);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            jelly.move(LEFT);
        }if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            jelly.shoot(0,30);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            jelly.shoot(0,-30);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            jelly.shoot(30, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            jelly.shoot(-30, 0);
        }
        if(Gdx.input.isKeyPressed((Input.Keys.K))){
            jelly.weaponReturn();
        }
    }

}
