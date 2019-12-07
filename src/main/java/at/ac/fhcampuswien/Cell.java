package at.ac.fhcampuswien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class Cell extends Pane {
    private ImageView view;
    private List<Cell> neighbours;

    // TODO add addtional variables you need. for state...

    public Cell(Image img, int state) {
        view = new ImageView(img);
        getChildren().add(view);
        // TODO add stuff here if needed.
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
