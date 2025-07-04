package com.javakaian.game.map;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.util.GameConstants;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class MapMaker {

    private int rowSize;
    private int columnSize;
    private int[][] matrix;
    private final Set<Vector2> pathPoints;
    private final LinkedList<Direction> directionList;

    public MapMaker(int levelNumber) {
        directionList = new LinkedList<>();
        pathPoints = new HashSet<>();
        if (levelNumber == 2) {
            initialize_lvl2();}
        else if (levelNumber == 3){
            initialize_lvl3();}
        else {
            initialize(); // Level 1
        }
    }
//// These code for random path
//    public void initialize() {
//
//        columnSize = GameConstants.COLUMN_SIZE;
//        rowSize = GameConstants.MAP_ROW_SIZE;
//        matrix = generateMap(rowSize, columnSize);
//        loadPathPoints();
//        loadDirectionList(0, 0);
//
//        // add last direction to the list one more time
//        // so enemy can go outside of screen
//        directionList.add(0, Direction.RIGHT);
//        directionList.add(directionList.get(directionList.size() - 1));
//    }

//Manual path generator
public void initialize() {
    columnSize = GameConstants.COLUMN_SIZE;
    rowSize = GameConstants.MAP_ROW_SIZE;
    matrix = new int[rowSize][columnSize];
    int row = 0, col = 0;
    // 1. Right 2 tiles
    for (int i = 0; i < 2; i++) {
        matrix[row][col] = 1;
        col++;
    }
    // 2. Down 5 tiles
    for (int i = 0; i < 5; i++) {
        matrix[row][col] = 1;
        row++;
    }
    // 3. Right 3 tiles
    for (int i = 0; i < 3; i++) {
        matrix[row][col] = 1;
        col++;
    }
    // 4. Up 3 tiles
    for (int i = 0; i < 3; i++) {
        matrix[row][col] = 1;
        row--;
    }
    // 5. Right until the end
    while (col < columnSize && row < rowSize) {
        matrix[row][col] = 1;
        col++;
    }

    loadPathPoints();
    loadDirectionList(0, 0); // starting tile: row 0, column 0

    // Add extra direction to let enemies exit the screen
        directionList.add(0, Direction.RIGHT);
        directionList.add(directionList.get(directionList.size() - 1));
}

    public void initialize_lvl2() {
        columnSize = GameConstants.COLUMN_SIZE;
        rowSize = GameConstants.MAP_ROW_SIZE;
        matrix = new int[rowSize][columnSize];
        int row = 0, col = 0;
        // 1. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 2. Down 5 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row++;
        }
        // 3. Right 3 tiles
        for (int i = 0; i < 3; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 4. Up 5 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row--;
        }
        // 5. Right 3 tiles
        for (int i = 0; i < 3; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 6. Down 3 tiles
        for (int i = 0; i < 3; i++) {
            matrix[row][col] = 1;
            row++;
        }
        // 5. Right until the end
        while (col < columnSize && row < rowSize) {
            matrix[row][col] = 1;
            col++;
        }

        loadPathPoints();
        loadDirectionList(0, 0); // starting tile: row 0, column 0

        // Add extra direction to let enemies exit the screen
        directionList.add(0, Direction.RIGHT);
        directionList.add(directionList.get(directionList.size() - 1));
    }

    public void initialize_lvl3() {
        columnSize = GameConstants.COLUMN_SIZE;
        rowSize = GameConstants.MAP_ROW_SIZE;
        matrix = new int[rowSize][columnSize];
        int row = 0, col = 0;
        // 1. Right 1 tiles
        for (int i = 0; i < 1; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 2. Down 5 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row++;
        }
        // 3. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 4. Up 5 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row--;
        }
        // 5. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 6. Down 3 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row++;
        }
        // 5. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 6. up 5 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row--;
        }
        // 5. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 6. Down 3 tiles
        for (int i = 0; i < 5; i++) {
            matrix[row][col] = 1;
            row++;
        }

        // 5. Right 2 tiles
        for (int i = 0; i < 2; i++) {
            matrix[row][col] = 1;
            col++;
        }
        // 6. up 5 tiles
        for (int i = 0; i < 4; i++) {
            matrix[row][col] = 1;
            row--;
        }
        // 7. Right until the end
        while (col < columnSize && row < rowSize) {
            matrix[row][col] = 1;
            col++;
        }


        loadPathPoints();
        loadDirectionList(0, 0); // starting tile: row 0, column 0

        // Add extra direction to let enemies exit the screen
        directionList.add(0, Direction.RIGHT);
        directionList.add(directionList.get(directionList.size() - 1));
    }




    public void loadDirectionList(int x, int y) {
        matrix[x][y] = 0;
        if (!(x + 1 >= rowSize) && matrix[x + 1][y] == 1) {
            matrix[x + 1][y] = 0;
            directionList.add(Direction.DOWN);
            loadDirectionList(x + 1, y);
        } else if (!(x - 1 < 0) && matrix[x - 1][y] == 1) {
            matrix[x - 1][y] = 0;
            directionList.add(Direction.UP);
            loadDirectionList(x - 1, y);
        } else if (!(y + 1 >= columnSize) && matrix[x][y + 1] == 1) {
            matrix[x][y + 1] = 0;
            directionList.add(Direction.RIGHT);
            loadDirectionList(x, y + 1);
        }

    }

    public void fillMap(int col, int start, int finish) {

        if (start > finish) {
            int temp = finish;
            finish = start;
            start = temp;
        }
        for (int i = start; i < finish + 1; i++) {
            matrix[i][col] = 1;
        }
    }

    public int[][] generateMap(int row, int col) {

        matrix = new int[row][col];
        int start = 0;
        int finish = 0;
        Random rand = new Random();

        for (int i = 0; i < col; i++) {
            if (i % 2 == 0) {
                finish = rand.nextInt(row);
                fillMap(i, start, finish);
                start = finish;
            } else {
                fillMap(i, start, finish);
            }
        }
        return matrix;
    }

    public void loadPathPoints() {

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (matrix[i][j] == 1) {
                    pathPoints.add(new Vector2(i, j));
                }
            }
        }
    }

    public Set<Vector2> getPathPoints() {
        return pathPoints;
    }

    public LinkedList<Direction> getDirectionList() {
        return directionList;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
