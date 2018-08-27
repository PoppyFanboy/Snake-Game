package poppyfanboy.snakegame.logic;

/**
 * Class "Block"
 * provides method for drawing blocks on game field
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Block implements ObjectOnField {
    private IntVector coords;
    private int size;
    private Color color;

    Block(IntVector coords, int size, Color color) {
        this.coords = coords;
        this.size = size;
        this.color = color;
    }

    public void paint(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * size, coords.getY() * size, size, size);
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * size, coords.getY() * size, size, size);
    }

    public boolean collision(IntVector point) {
        return point.equals(coords);
    }

    IntVector getCoords() {
        return coords;
    }
}
