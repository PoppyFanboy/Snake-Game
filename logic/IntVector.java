package poppyfanboy.snakegame.logic;

import static java.lang.Math.abs;

/**
 * Class "IntVector"
 *
 * Represents coordinates of a dot in a 2-d array
 * All arithmetical operations are computed modulo
 * "moduloX" or "moduloY"
 *
 * @author PoppyFanboy
 */

public class IntVector {
    private int x;
    private int y;

    private static int moduloX = 15;
    private static int moduloY = 15;

    public IntVector(int x, int y) {
        x = x % moduloX;
        y = y % moduloY;
        if (x < 0) { x += moduloX; }
        if (y < 0) { y += moduloY; }

        this.x = x;
        this.y = y;
    }

    public IntVector add(IntVector term) {
        return new IntVector(this.x + term.x, this.y + term.y);
    }

    public IntVector incX(int x) {
        return new IntVector(this.x + x, this.y);
    }

    public IntVector incY(int y) {
        return new IntVector(this.x, this.y + y);
    }

    public IntVector copy() {
        return new IntVector(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(IntVector vector) {
        return this.x == vector.x && this.y == vector.y;
    }

    public static boolean setModuloX(int moduloX) {
        if (moduloX < 0) {
            return false;
        } else {
            IntVector.moduloX = moduloX;
            return true;
        }
    }

    public static boolean setModuloY(int moduloY) {
        if (moduloY < 0) {
            return false;
        } else {
            IntVector.moduloY = moduloY;
            return true;
        }
    }
}