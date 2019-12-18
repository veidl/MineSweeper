package at.ac.fhcampuswien;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    // board
    static final int CELL_SIZE = 15;
    static final int ROWS = 4;
    static final int COLS = 4;
    // want to change for dificulty
    static int NUM_MINES;

    // images
    private static final int NUM_IMAGES = 13;
    private static final int COVERED_MINE_CELL = 10;
    private static final int MINE_CELL = 9;
    private static final int MARKED_CELL = 10;
    private static final int FLAG = 11;

    // states
    private static final int COVERED = 1;
    private static final int UNCOVERED = 3;
    private static final int MARKED = 4;
    private static final int UNMARKED = 5;


    // Add further constants or let the cell keep track of its state.

    private Cell cells[][];
    private Image[] images;
    private int cellsUncovered;
    private int minesMarked;
    private boolean gameOver;

    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board() throws IOException {
        cells = new Cell[ROWS][COLS];
        cellsUncovered = 0;
        minesMarked = 0;
        gameOver = false;
        loadImages();

        initCells();
        // neighbours
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setNeighbours(computeNeighbours(i, j));
            }
        }

        initMines();
    }

    //init cells -> all covered
    private void initCells() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new Cell(images[COVERED_MINE_CELL], COVERED);
            }
        }
    }

    private void initMines() {
        // place them in a random position
        int rem = NUM_MINES;
        while (rem > 0) {
            Cell c = cells[getRandomNumberInts(0, ROWS - 1)][getRandomNumberInts(0, COLS - 1)];
            c.setMine(true);
            c.update(images[MINE_CELL]);
            rem--;
        }
    }

    private int computeMineNeighbour(int row, int col) {
        int mines = 0;
        for (Cell c : cells[row][col].getNeighbours()) {
            if (c.isMine()) {
                mines++;
            }
        }
        return mines;
    }

    public void uncover(int row, int col) {
        Cell c = cells[row][col];

        if (c.isMine()) {
            c.update(images[MINE_CELL]);
            c.setState(UNCOVERED);
            this.gameOver = true;
            uncoverAllCells();
            return;
        }

        int minesCount = computeMineNeighbour(row, col);
        if (minesCount == 0) {
            uncoverEmptyCells(row, col);
        } else if (c.getState() == COVERED) {
            c.update(images[minesCount]);
            c.setState(UNCOVERED);
            cellsUncovered++;
        }
    }

    public void markCell(int row, int col) {

        Cell cell = cells[row][col];

        if (cell.getState() == UNCOVERED) {
            return;
        }

        if (cell.getState() == MARKED) {
            cell.update(images[MARKED_CELL]);
            cell.setState(UNMARKED);
            this.minesMarked++;
            return;
        }

        cell.update(images[FLAG]);
        cell.setState(MARKED);
        this.minesMarked++;

    }

    public void uncoverEmptyCells(int x, int y) {
        // FLOOD FILL
        // https://de.wikipedia.org/wiki/Floodfill

        // check borders
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS)
            return;

        // parameters in case of endless loop
        if (cells[x][y].getState() == UNCOVERED || computeMineNeighbour(x, y) != 0)
            return;

        // Replace the image at (x, y)
        cells[x][y].update(images[0]);
        cells[x][y].setState(UNCOVERED);
        cellsUncovered++;

        // redo for all positions arround me
        uncoverEmptyCells(x + 1, y);
        uncoverEmptyCells(x - 1, y);
        uncoverEmptyCells(x, y + 1);
        uncoverEmptyCells(x, y - 1);

    }

    private void uncoverAllCells() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (cells[i][j].isMine()) {
                    cells[i][j].update(images[MINE_CELL]);
                } else {
                    int count = computeMineNeighbour(i, j);
                    cells[i][j].update(images[count]);
                }
            }
        }
    }

    private List<Cell> computeNeighbours(int x, int y) {
        List<Cell> neighbours = new ArrayList<>();

        // check all sites arround you for neighbours


        if ((x - 1) >= 0 && (y - 1) >= 0) {
            neighbours.add(cells[x - 1][y - 1]);
        }

        if ((y - 1) >= 0) {
            neighbours.add(cells[x][y - 1]);
        }

        if ((x + 1) < COLS && y - 1 >= 0) {
            neighbours.add(cells[x + 1][y - 1]);
        }

        if ((x + 1) < COLS) {
            neighbours.add(cells[x + 1][y]);
        }

        if ((x + 1) < COLS && (y + 1) < ROWS) {
            neighbours.add(cells[x + 1][y + 1]);
        }

        if ((y + 1) < ROWS) {
            neighbours.add(cells[x][y + 1]);
        }

        if ((y + 1) < ROWS && (x - 1) >= 0) {
            neighbours.add(cells[x - 1][y + 1]);
        }

        if ((x - 1) >= 0) {
            neighbours.add(cells[x - 1][y]);
        }
        return neighbours;
    }

    /**
     * Loads the given images into memory. Of course you may use your own images and layouts.
     */
    private void loadImages() throws IOException {
        images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            URL dict = getClass().getResource("pictures/");
            var path = dict.getPath() + i + ".png";
            FileInputStream fis;
            try {
                fis = new FileInputStream(path);
                this.images[i] = new Image(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Computes a random int number between min and max.
     *
     * @param min the lower bound. inclusive.
     * @param max the upper bound. inclusive.
     * @return a random int.
     */
    private int getRandomNumberInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }

    public int getMinesMarked() {
        return minesMarked;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCellsUncovered() {
        return cellsUncovered;
    }

}
