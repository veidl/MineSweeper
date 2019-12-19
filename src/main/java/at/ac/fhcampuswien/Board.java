package at.ac.fhcampuswien;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Board {

    // board
    static final int CELL_SIZE = 15;
    static final int ROWS = 25;
    static final int COLS = 25;
    // not final -> change of difficulty
    static int NUM_MINES;

    // images
    private static final int NUM_IMAGES = 13;
    private static final int COVERED_MINE_CELL = 10;
    private static final int MINE_CELL = 9;
    private static final int MARKED_CELL = 10;
    private static final int FLAG = 11;

    // states
    private static final int EMPTY = 0;
    private static final int COVERED = 1;
    private static final int UNCOVERED = 3;
    private static final int MARKED = 4;
    private static final int UNMARKED = 5;

    // Add further constants or let the cell keep track of its state.
    private Cell[][] cells;
    private Image[] images;
    private int cellsUncovered;
    private int minesMarked;
    private boolean gameOver;
    private boolean showMines;


    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board() {
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


    /**
     * initiates cells with cover image, no further states are set
     */
    private void initCells() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                //init cells -> all covered
                cells[i][j] = new Cell(images[COVERED_MINE_CELL], COVERED);
            }
        }
    }

    /**
     * initiates mines with the proper mine status
     */
    private void initMines() {
        int rem = NUM_MINES;
        while (rem > 0) {
            // place them in a random position
            Cell c = cells[getRandomNumberInts(0, ROWS - 1)][getRandomNumberInts(0, COLS - 1)];
            c.setMine(true);
            if (isShowMines()){
                c.update(images[MINE_CELL]);
            }
            rem--;
        }
    }

    /**
     * calculates nearby mines for a given cell neighbours
     *
     * @param row (x coordinate)
     * @param col (y coordinate)
     * @return a sum of mines
     */
    private int computeMineNeighbour(int row, int col) {
        int mines = 0;
        for (Cell c : cells[row][col].getNeighbours()) {
            if (c.isMine()) {
                mines++;
            }
        }
        return mines;
    }

    /**
     * checks if its a mine, if so it sets gameOver and uncovers all cells.
     * If its an empty cell it will uncover all empty cells within range
     * if its near an mine it will just uncover the cell
     *
     * @param row (x coordinate)
     * @param col (y coordinate)
     */
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
        } else if (c.getState() == COVERED || c.getState() == MARKED) {
            c.update(images[minesCount]);
            c.setState(UNCOVERED);
            cellsUncovered++;
        }
    }

    /**
     * marks a cell to help find mines
     *
     * @param row (x coordinate)
     * @param col (y coordinate)
     */
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

    /**
     * using flood fill to uncover empty cells https://de.wikipedia.org/wiki/Floodfill
     * it will check all horizontal paths until a cell is already uncovered or a mine is nearby
     *
     * @param row (x coordinate)
     * @param col (y coordinate)
     */
    public void uncoverEmptyCells(int row, int col) {
        // check borders
        if (row < 0 || row >= COLS || col < 0 || col >= ROWS)
            return;

        // parameters in case of endless loop
        if (cells[row][col].getState() == UNCOVERED || computeMineNeighbour(row, col) != 0)
            return;

        // Replace the image at (x, y)
        cells[row][col].update(images[EMPTY]);
        cells[row][col].setState(UNCOVERED);
        cellsUncovered++;

        // redo for all positions arround me
        uncoverEmptyCells(row + 1, col);
        uncoverEmptyCells(row, col + 1);
        uncoverEmptyCells(row - 1, col);
        uncoverEmptyCells(row, col - 1);

    }

    /**
     * if the game is over all cells will be uncovered
     */
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

    /**
     * check all sites around a cell for neighbours; celestial coordinates (S,SE,E,SE etc...)
     *
     * @param row (x coordinate)
     * @param col (y coordinate)
     * @return a list of Cells per Cell
     */
    private List<Cell> computeNeighbours(int row, int col) {
        List<Cell> neighbours = new ArrayList<>();

        if ((row - 1) >= 0 && (col - 1) >= 0) {
            neighbours.add(cells[row - 1][col - 1]);
        }

        if ((col - 1) >= 0) {
            neighbours.add(cells[row][col - 1]);
        }

        if ((row + 1) < COLS && col - 1 >= 0) {
            neighbours.add(cells[row + 1][col - 1]);
        }

        if ((row + 1) < COLS) {
            neighbours.add(cells[row + 1][col]);
        }

        if ((row + 1) < COLS && (col + 1) < ROWS) {
            neighbours.add(cells[row + 1][col + 1]);
        }

        if ((col + 1) < ROWS) {
            neighbours.add(cells[row][col + 1]);
        }

        if ((col + 1) < ROWS && (row - 1) >= 0) {
            neighbours.add(cells[row - 1][col + 1]);
        }

        if ((row - 1) >= 0) {
            neighbours.add(cells[row - 1][col]);
        }
        return neighbours;
    }

    /**
     * Loads the given images into memory. Of course you may use your own images and layouts.
     */
    private void loadImages() {
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

    public boolean isShowMines() {
        return showMines;
    }

    public void setShowMines(boolean showMines) {
        this.showMines = showMines;
    }


}
