package poppyfanboy.snakegame.logic;

/**
 * Class "Block"
 * provides methods for drawing blocks on a game field
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import poppyfanboy.snakegame.data.IntVector;

public class Block extends ObjectOnField {
    // Integer coordinates of the block
    private IntVector coords;
    // Size of the cell of a grid of the game field
    private Color color;
    // finally, the block of size (fract * size)
    // will be drawn at the center of the cell of size "size"
    private double fract;

    public Block(IntVector coords, double blockSize, Color color, double fract) {
        this.coords = coords;
        this.blockSize = blockSize;
        this.color = color;
        this.fract = fract;
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * blockSize + blockSize * (1 - fract) / 2.0,
                coords.getY() * blockSize + blockSize * (1 - fract) / 2.0, blockSize * fract, blockSize * fract);
    }

    public boolean collision(IntVector point) {
        return point.equals(coords);
    }

    IntVector getCoords() {
        return coords;
    }
}
