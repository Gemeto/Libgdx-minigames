package com.mygdx.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Logic.GameObjects.GameObj;
import com.mygdx.game.Logic.GameObjects.TileObj;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TileManager {
    static int columns = 85;
    static FileHandle file;
    static int i = 0;
    static String mappingFile;
    static int[][] myArray;
    public static int nActiveTiles;
    static int nTilesDrawed;
    static int rows = 85;
    static int tileHalfHeight = (4320 / columns);
    static int tileHalfWidth = (GL20.GL_KEEP / rows);
    static ArrayList<GameObj> tiles;
    static float posX = ((float) (tileHalfWidth / 2));

    public static ArrayList<GameObj> loadTilesFromTxtSlow(String fileName) {
        file = Gdx.files.internal(fileName);
        mappingFile = file.readString();
        tiles = new ArrayList<>();
        myArray = (int[][]) Array.newInstance(Integer.TYPE, new int[]{rows, columns});
        String[] lines = mappingFile.split(System.getProperty("line.separator"));
        for (int y = 0; y < rows; y++) {
            String[] line = lines[y].split(" ");
            int i2 = i;
            float posX2 = posX;
            int j2 = 0;
            float posY2 = (float) (tileHalfHeight / 2);
            for (int x = 0; x < columns; x++) {
                myArray[i2][j2] = Integer.parseInt(line[j2]);
                if (myArray[i2][j2] == 1) {
                    TileObj tile = new TileObj(tileHalfWidth, tileHalfHeight, (double) posX2, (double) posY2);
                    tile.i = i2;
                    tile.j = j2;
                    tile.inactive = line[j2].equals("0");
                    tiles.add(tile);
                    nActiveTiles++;
                }
                j2++;
                posY2 += (float) tileHalfHeight;
            }
            i++;
            posX += (float) tileHalfWidth;
        }
        return tiles;
    }

    public static void drawTiles() {
        if (nTilesDrawed < nActiveTiles) {
            ((TileObj) tiles.get(nTilesDrawed)).setDefaultImage();
            nTilesDrawed++;
        }
    }
}
