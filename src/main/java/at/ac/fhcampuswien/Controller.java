package at.ac.fhcampuswien;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {
    // Model
    private Board board;
    private boolean isActive;

    @FXML
    private Label message;
    @FXML
    private GridPane grid;
    @FXML
    private Button restart;
    @FXML
    public Label gameDifficulty;
    @FXML
    public MenuButton difficulty;

    // standard value of mines
    private int mines = 50;

    /**
     * initialise the cells for the board
     */
    @FXML
    public void initialize() {
        Board.NUM_MINES = mines;

        isActive = true;
        this.board = new Board();
        Cell[][] cells = this.board.getCells();
        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLS; j++) {
                grid.add(cells[i][j], i, j);
            }
        }
        message.setText("Good Luck!");
    }

    /**
     * calculates x, y coordinates for pressing a cell
     * uncovers / marks a cell given it's click
     * also checks if the game is already over
     *
     * @param event (onMouseClick)
     */
    @FXML
    public void update(MouseEvent event) {
        if (isActive) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int row = (int) event.getX() / Board.CELL_SIZE;
                int col = (int) event.getY() / Board.CELL_SIZE;
                if (event.getButton() == MouseButton.PRIMARY) {
                    board.uncover(row, col);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    board.markCell(row, col);
                }
                if (board.isGameOver()) {
                    message.setText("YOU LOST THE GAME");
                    isActive = false;
                }
                if (board.getCellsUncovered() == (Board.ROWS * Board.COLS - Board.NUM_MINES)) {
                    message.setText("GG WP - YOU WON");
                    isActive = false;
                }
                if (isActive)
                    message.setText(" Marker: " + board.getMinesMarked() + "/" + Board.NUM_MINES);
            }
        }
    }

    /**
     * resets the grid
     */
    @FXML
    public void restart() {
        this.grid.getChildren().clear();
        initialize();
    }

    /**
     * used for the difficulty (mines increase)
     */
    public void easy() {
        this.mines = 50;
        board.setShowMines(false);
        this.gameDifficulty.setText("easy mode");
        restart();
    }

    public void medium() {
        this.mines = 100;
        board.setShowMines(false);
        this.gameDifficulty.setText("medium mode");
        restart();
    }

    public void hardcore() {
        this.mines = 150;
        board.setShowMines(false);
        this.gameDifficulty.setText("hardcore mode");
        restart();
    }

    public void forTesting() {
        this.mines = 1;
        board.setShowMines(true);
        this.gameDifficulty.setText("testing mode");
        restart();
    }
}
