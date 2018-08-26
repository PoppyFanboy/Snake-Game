package poppyfanboy.snakegame.logic;

/**
 * Class "Block"
 * provides method for drawing blocks on game field
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Block {
    private IntVector coords;

    Block(IntVector coords) {
        this.coords = coords.copy();
    }

    void paint(GraphicsContext gc, int blockSize, Color color) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * blockSize, coords.getY() * blockSize, blockSize, blockSize);
    }

    IntVector getCoords() {
        return coords;
    }
}
