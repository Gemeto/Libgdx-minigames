package com.mygdx.game.Utils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameConfig.GameConfig;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.TileObj;
import com.mygdx.game.Logic.GameWorld.GameWorld;
import com.mygdx.game.Logic.Online.P2PManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


public class Mapper extends ApplicationAdapter {
	Texture t;
	Sprite s;
	SpriteBatch sB;
	ArrayList<TileObj> tiles;
	Scanner sc;
	FileWriter fw;
	int rows = 85;
	int columns = 85;
	int [][] myArray;
	public OrthographicCamera ortographicCamera;
	Viewport viewport;

	@Override
	public void create () {
		//Camera view
		ortographicCamera = new OrthographicCamera(GameConfig.WORLDWIDTH, GameConfig.WORLDHEIGHT);
		viewport = new ExtendViewport(10, 10, ortographicCamera);
		ortographicCamera.position.set(ortographicCamera.viewportWidth / 2, ortographicCamera.viewportHeight / 2, 0);
		ortographicCamera.update();

		t = new Texture("rm.jpg");
		s = new Sprite(t);
		sB = new SpriteBatch();

		//Tiles
		tiles = new ArrayList<>();
		try {
			sc = new Scanner(new BufferedReader(new FileReader("sample2.txt")));
		}catch(Exception e) {
			System.out.print(e.toString());
		}
		int rows = 85;
		int columns = 85;
		myArray = new int[rows][columns];
		int i = 0;
		int j;
		int tileHalfWidth = (GameConfig.WORLDWIDTH*4)/rows;
		int tileHalfHeight = (GameConfig.WORLDHEIGHT*4)/columns;
		boolean space = false;



		float posX = tileHalfWidth/2;
		float posY;
		for(int y = 0; y < rows; y++) {
			if(!sc.hasNextLine()) {
				System.out.print(i);
				break;
			}
			String[] line = sc.nextLine().trim().split(" ");
			j = 0;
			posY = tileHalfHeight/2;
			for (int x = 0; x < columns; x++) {
				myArray[i][j] = Integer.parseInt(line[j]);
				TileObj tile = new TileObj(tileHalfWidth, tileHalfHeight, posX, posY);
				tile.i = i;
				tile.j = j;
				tile.inactive = line[j].equals("0");
				tiles.add(tile);
				j++;
				posY += tileHalfHeight;
			}
			i++;
			posX += tileHalfWidth;
		}
		sc.close();
	}

	@Override
	public void render () {
		sB.setProjectionMatrix(ortographicCamera.combined);
		Gdx.gl.glClearColor(0, 1, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sB.begin();
		Color c = sB.getColor();
		sB.setColor(c.r, c.g, c.b, 1);
		sB.draw(s, 0, 0, 0, 0, GameConfig.WORLDWIDTH*4, GameConfig.WORLDHEIGHT*4, 1, 1, 0);
		for(TileObj tile:tiles)
			tile.draw(sB);
		sB.end();


		Gdx.input.setInputProcessor(new InputAdapter(){
			float desviacionX = (ortographicCamera.position.x - ortographicCamera.viewportWidth/2);
			float desviacionY = (ortographicCamera.position.y - ortographicCamera.viewportHeight/2);

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer){
				for(TileObj tile: tiles)
					if(tile.isCollision(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN + desviacionX, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN + desviacionY)) {
						tile.inactive = !tile.inactive;
						myArray[tile.i][tile.j] = tile.inactive ? 0:1;
					}
				return true;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button){
				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				for(TileObj tile: tiles)
					if(tile.isCollision(screenX*GameConfig.WIDTHPERCENTAGETOSCREEN + desviacionX, GameConfig.WORLDHEIGHT - screenY*GameConfig.HEIGHTPERCENTAGETOSCREEN + desviacionY)) {
						tile.inactive = !tile.inactive;
						myArray[tile.i][tile.j] = tile.inactive ? 0:1;
					}
				return true;
			}
		});
		save();
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			ortographicCamera.position.y += 5;
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			ortographicCamera.position.x -= 5;
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			ortographicCamera.position.y -= 5;
		if(Gdx.input.isKeyPressed(Input.Keys.D))
			ortographicCamera.position.x += 5;
		ortographicCamera.update();

	}
	
	@Override
	public void dispose () {

	}

	public void save(){
		try {
			fw = new FileWriter("sample2.txt");
		}catch(Exception e) {
			System.out.print(e.toString());
		}
		boolean space = false;
		int i = 0;
		int j;
		for(int y = 0; y < rows; y++) {
			space = false;
			j = 0;
			for (int x = 0; x < columns; x++) {
				try{fw.write(myArray[i][j]==1 ? "1 ":"0 ");}catch(Exception e){System.out.print(e.toString());}
				space = !space;
				j++;
			}
			try{fw.write(System.getProperty( "line.separator" ));}catch(Exception e){System.out.print(e.toString());}
			i++;
		}
		try {
			fw.close();
		}catch (Exception e){
			System.out.print("No se ha podido cerrar el archivo tras escribir.");
		}
	}
}
