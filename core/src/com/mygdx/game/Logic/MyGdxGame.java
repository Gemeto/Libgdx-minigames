package com.mygdx.game.Logic;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.game.Logic.GameWorld.GameWorld;
import com.mygdx.game.Logic.Online.P2PManager;


public class MyGdxGame extends ApplicationAdapter {
	long tiempo, tiempoAnterior, tiempoEspera;
	int framesSaltados, frameTiempo;
	GameWorld gameWorld;

	//Online

	@Override
	public void create () {
		gameWorld = new GameWorld();
	}

	@Override
	public void render () {
		tiempo = System.currentTimeMillis();
		frameTiempo++;
		framesSaltados = 0;
		gameWorld.draw();
		tiempoAnterior = System.currentTimeMillis() - tiempo;
		tiempoEspera = (int) (frameTiempo - tiempoAnterior);
		while (tiempoEspera < 0 && framesSaltados <= 5) {//Ajustamos el numero de frames a 60
			tiempoEspera++;
			framesSaltados++;
			frameTiempo++;
		}
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//jelly.dispose();
		P2PManager.closeSession();//BUG EN ANDROID - Si el juego se minimiza antes de cerrarlo no se ejecuta este metodo
	}

	private void dibujar(){
		gameWorld.draw();
	}
}
