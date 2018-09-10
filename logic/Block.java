package poppyfanboy.snakegame.logic;

/**
 * Class "Block"
 * provides method for drawing blocks on game field
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import poppyfanboy.snakegame.Main;

public class Block implements ObjectOnField {
    private IntVector coords;
    private int size;
    private Color color;
    private double fract;

    Block(IntVector coords, int size, Color color, double fract) {
        this.coords = coords;
        this.size = size;
        this.color = color;
        this.fract = fract;
    }

    Block(IntVector coords, int size, Color color) {
        this(coords, size, color, 1.0);
    }

    public void paint(GraphicsContext gc, Color color, Double fract) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * size + size * (1 - fract) / 2.0,
                coords.getY() * size + size * (1 - fract) / 2.0, size * fract, size * fract);
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(coords.getX() * size + size * (1 - fract) / 2.0,
                coords.getY() * size + size * (1 - fract) / 2.0, size * fract, size * fract);
    }

    public void paint(GraphicsContext gc, Color color) {
        paint(gc, color, fract);
    }

    public static void fillLine(GraphicsContext gc, IntVector first, IntVector second, int size, double fract, Color color) {
        double x, y, width, height;

        if (first.getX() < second.getX()) {
            x = size * first.getX() + size * fract;
            y = size * first.getY();
        } else if (first.getY() < second.getY()) {
            x = size * first.getX();
            y = size * first.getY() + size * fract;
        } else {
            x = size * second.getX();
            y = size * second.getY();
        }

        if (first.getX() == second.getX()) {
            fillRect(gc, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size * fract, size, color);
        } else {
            fillRect(gc, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size, size * fract, color);
        }
    }

    public static void fillRect(GraphicsContext gc, double x, double y, double width, double height, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        if (x < 0) {
            gc.fillRect(x + Main.GAME_WIDTH, y, width, height);
        }
        if (x + width > Main.GAME_WIDTH) {
            gc.fillRect(x - Main.GAME_WIDTH, y, width, height);
        }
        if (y < 0) {
            gc.fillRect(x, y + Main.GAME_HEIGHT, width, height);
        }
        if (y + height > Main.GAME_HEIGHT) {
            gc.fillRect(x, y - Main.GAME_HEIGHT, width, height);
        }
    }

    public boolean collision(IntVector point) {
        return point.equals(coords);
    }

    IntVector getCoords() {
        return coords;
    }
}
