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

    public static void fillLine(GraphicsContext gc, IntVector first, IntVector second, int size, double fract) {
        int x, y;

        if (first.getX() < second.getX() || first.getY() < second.getY()) {
            x = size * first.getX();
            y = size * first.getY();
        } else {
            x = size * second.getX();
            y = size * second.getY();
        }

        if (first.getX() == second.getX()) {
            fillRect(gc, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size * fract, size * fract + size);
        } else {
            fillRect(gc, x + size * (1 - fract) / 2.0,
                    y + size * (1 - fract) / 2.0, size * fract + size, size * fract);
        }
    }

    public static void fillRect(GraphicsContext gc, double aX, double aY, double aWidth, double aHeight) {
        // if we let arguments be doubles, then edges
        // of the shape would be surrounded with half-transparent border,
        // so when one "blurry" shape overlays another one,
        // "half-transparent borders" also overlay each other.
        // As a result the shape looks thicker
        int x = (int) aX;
        int y = (int) aY;
        int width = (int) aWidth;
        int height = (int) aHeight;

        gc.setFill(Color.BLACK);
        gc.fillRect((int) x, (int) y, (int) width, (int) height);

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
