package poppyfanboy.snakegame.logic;

import javafx.scene.paint.Color;

public class SnakeBlock extends Block {
    private Direction dir;

    SnakeBlock(IntVector coords, int size, Color color, double fract, Direction dir) {
        super(coords, size, color, fract);
        this.dir = dir;
    }

    SnakeBlock (IntVector coords, int size, Color color, Direction dir) {
        super(coords, size, color, 1.0);
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }
}
