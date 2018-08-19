package poppyfanboy.snakegame.logic;

/**
 * Class "Block"
 * provides method for drawing blocks on game field
 * is not used currently
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Block {
    private int x = 0;
    private int y = 0;

    Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void paint(GraphicsContext gc, int blockSize, Color color) {
        gc.setFill(color);
        gc.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }
}
