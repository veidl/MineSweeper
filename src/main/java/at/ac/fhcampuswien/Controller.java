package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class Controller {

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

    @FXML
    public void initialize() throws IOException {
        isActive = true;
        this.board = new Board();
        Cell[][] cells = this.board.getCells();
        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLS; j++) {
                // uncomment below as soon as the Cells are initialized by class Board.
                grid.add(cells[i][j], i, j);
            }
        }
    }

    @FXML
    public void update(MouseEvent event) {
        if (isActive) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int row = (int) event.getX() / Board.CELL_SIZE;
                int col = (int) event.getY() / Board.CELL_SIZE;
                if (event.getButton() == MouseButton.PRIMARY) {
                    // TODO Uncover...
                    System.out.println("test");

                } else if (event.getButton() == MouseButton.SECONDARY) {
                    // TODO Mark...
                }
                // TODO 1. Check if the player has already won

                // TODO 2. If the game is still in active mode show used mine markers.
                if (isActive)
                    message.setText(" Marker: " + board.getMinesMarked() + "/" + Board.NUM_MINES);
            }
        }
    }

    @FXML
    public void restart(ActionEvent actionEvent) throws IOException {
        grid.getChildren().clear();
        initialize();
    }
}
