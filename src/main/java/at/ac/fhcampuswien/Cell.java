package at.ac.fhcampuswien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Cell extends Pane {
    private ImageView view;
    private List<Cell> neighbours;
    // neded boolean for mine cause of the flag
    private boolean isMine;
    private int state;

    public Cell(Image img, int state) {
        view = new ImageView(img);
        this.state = state;

        this.isMine = false;
        getChildren().add(view);

    }

    public int getState() {
        return state;
    }

    public void setState(int s) {
        this.state = s;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
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
