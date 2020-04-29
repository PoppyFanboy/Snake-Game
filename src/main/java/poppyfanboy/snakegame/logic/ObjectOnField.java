package poppyfanboy.snakegame.logic;

/**
 * An interface which must be implemented by all
 * the objects located on the game field
 */

import javafx.scene.canvas.GraphicsContext;
import poppyfanboy.snakegame.data.IntVector;

public abstract class ObjectOnField {
    protected double blockSize;

    void setBlockSize(double blockSize) {
        this.blockSize = blockSize;
    }
    abstract boolean collision(IntVector point);
    abstract void paint(GraphicsContext gc);
}
