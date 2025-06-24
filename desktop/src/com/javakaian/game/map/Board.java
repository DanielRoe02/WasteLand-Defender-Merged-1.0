package com.javakaian.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.map.Grid.EnumGridType;
import com.javakaian.game.util.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board {

    private final List<Grid> gridList;
    private final Set<Vector2> pathPoints;

    private boolean render = false;

    public Board(Set<Vector2> pathPoints) {
        this.pathPoints = pathPoints;
        gridList = new ArrayList<>();
        createGridList();
    }

    private void createGridList() {
        float gridWidth = GameConstants.GRID_WIDTH;
        float gridHeight = GameConstants.GRID_HEIGHT;
        float gridX, gridY;

        for (int i = 0; i < GameConstants.MAP_ROW_SIZE; i++) {
            for (int j = 0; j < GameConstants.COLUMN_SIZE; j++) {
                gridX = j * gridWidth;
                gridY = i * gridHeight + GameConstants.GRID_HEIGHT;

                Vector2 pos = new Vector2(i, j);
                EnumGridType type;

                if (pathPoints.contains(pos)) {
                    type = EnumGridType.PATH;
                }
                // Manually place TOXIC tiles
                else if ((i == 3 && j == 4)) {
                    type = EnumGridType.TOXIC;
                }
                // Manually place WATER tiles
                else if (((i >= 4 && i <= 5) && (j >= 7 && j <= 15)) ) {
                    type = EnumGridType.POLLWATER;
                }
                // Default to LAND
                else {
                    type = EnumGridType.LAND;
                }

                gridList.add(new Grid(gridX, gridY, gridWidth, gridHeight, type));
            }
        }
    }


    public void transformToGrasslandAnimated() {
        new Thread(() -> {
            // Sort row by row, right to left
            for (int row = GameConstants.MAP_ROW_SIZE - 1; row >= 0; row--) {
                for (int col = GameConstants.COLUMN_SIZE - 1; col >= 0; col--) {
                    int index = row * GameConstants.COLUMN_SIZE + col;
                    if (index >= gridList.size()) continue;

                    Grid grid = gridList.get(index);
                    Grid.EnumGridType type = grid.getType();

                    if (type != EnumGridType.PATH && type != EnumGridType.POLLWATER) {
                        Grid finalGrid = grid;
                        Gdx.app.postRunnable(() -> finalGrid.setType(EnumGridType.GRASS));
                    }
                    if (type == EnumGridType.POLLWATER) {
                        Grid finalGrid = grid;
                        Gdx.app.postRunnable(() -> finalGrid.setType(EnumGridType.WATER));
                    }

                    try {
                        Thread.sleep(15); // 15 ms delay per tile (adjust for speed)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }




    public void render(ShapeRenderer sr) {
        if (render) {
            for (Grid grid : gridList) {
                grid.render(sr);
            }
        }
    }

    public void render(SpriteBatch sb) {
        for (Grid grid : gridList) {
            grid.render(sb);
        }
    }

    public void update(float deltaTime) {
        for (Grid grid : gridList) {
            grid.update(deltaTime);
        }
    }

    public List<Grid> getGridList() {
        return gridList;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}
