package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class Controller {

    public Label difficult;
    public MenuButton difficulty;
    // Model
    private Board board;
    // private
    private boolean isActive;
    // View Fields
    @FXML
    private Label message;
    @FXML
    private GridPane grid;
    @FXML
    private Button restart;

    private int mines = 50;

    /**
     * initialise the cells for the board
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException {
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
     * @param event
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
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void restart(ActionEvent actionEvent) throws IOException {
        this.grid.getChildren().clear();
        initialize();
    }

    /**
     * used for the difficulty (mines increase)
     * @param actionEvent
     * @throws IOException
     */
    public void easy(ActionEvent actionEvent) throws IOException {
        this.mines = 50;
        this.difficult.setText("easy mode");
        restart(actionEvent);
    }

    public void medium(ActionEvent actionEvent) throws IOException {
        this.mines = 100;
        this.difficult.setText("medium mode");
        restart(actionEvent);
    }

    public void hardcore(ActionEvent actionEvent) throws IOException {
        this.mines = 150;
        this.difficult.setText("hardcore mode");
        restart(actionEvent);
    }

    public void forTesting(ActionEvent actionEvent) throws IOException {
        this.mines = 1;
        this.difficult.setText("testing mode");
        restart(actionEvent);
    }
}
