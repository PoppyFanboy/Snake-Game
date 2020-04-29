package poppyfanboy.snakegame.logic;

/**
 * Representation of a single segment of the snake on a game field
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import poppyfanboy.snakegame.data.IntVector;

public class SnakeBlock extends Block {
    private Direction dir;

    SnakeBlock(IntVector coords, double size, Color color, double fract, Direction dir) {
        super(coords, size, color, fract);
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }

    // Draws a line between "first" and "second" vector points
    // "proportion" is the measure of "how much of the line should be drawn?"
    // (ex: if proportion = 0.25 then one fourth of the line will be drawn in
    // the direction from first to second point (if invert is "true", then
    // the direction is inverted))
    public static void fillLine(GraphicsContext gc, double gameWidth, IntVector first, IntVector second,
                                double size, double fract, Color color, double proportion, boolean invert) {
        double x, y;
        Direction dir;
        x = size * first.getX();
        y = size * first.getY();

        if (first.getX() < second.getX()) {
            x += size * fract;
            dir = Direction.RIGHT;
        } else if (first.getY() < second.getY()) {
            y += size * fract;
            dir = Direction.DOWN;
        } else if (first.getX() > second.getX()) {
            x = size * second.getX();
            dir = Direction.LEFT;
        } else {
            y = size * second.getY();
            dir = Direction.UP;
        }

        if (invert) {
            dir = dir.inverse();
        }

        if (first.getX() == second.getX()) {
            fillRect(gc, gameWidth, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size * fract, size, color, proportion, dir);
        } else {
            fillRect(gc, gameWidth, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size, size * fract, color, proportion, dir);
        }
    }

    public static void fillRect(GraphicsContext gc, double gameWIdth, double x, double y,
                                double width, double height, Color color, double proportion, Direction dir) {
        // feel bad for this
        switch (dir) {
            case LEFT:
                x += width * (1 - proportion);
            case RIGHT:
                width *= proportion;
                break;
            case UP:
                y += height * (1 - proportion);
            case DOWN:
                height *= proportion;
                break;
        }

        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        if (x < 0) {
            gc.fillRect(x + gameWIdth, y, width, height);
        }
        if (x + width > gameWIdth) {
            gc.fillRect(x - gameWIdth, y, width, height);
        }
        if (y < 0) {
            gc.fillRect(x, y + gameWIdth, width, height);
        }
        if (y + height > gameWIdth) {
            gc.fillRect(x, y - gameWIdth, width, height);
        }
    }
}
