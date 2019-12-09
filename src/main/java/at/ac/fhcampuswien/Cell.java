package at.ac.fhcampuswien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class Cell extends Pane {
    private ImageView view;
    private List<Cell> neighbours;
    private boolean isMine;
    private boolean isFlag;
    private boolean isOpened;
    private int state;


    // TODO add addtional variables you need. for state...

    public Cell(Image img, int state) {
        view = new ImageView(img);
        this.state = state;
        getChildren().add(view);

        // TODO add stuff here if needed.
    }


    public int getState() {
        return state;
    }

    public void setState(int s) {
        this.state = s;

    }


    public void setNeighbours(List<Cell> neighbours) {
        this.neighbours = neighbours;
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public void update(Image img) {
        this.view.setImage(img);
    }
}
