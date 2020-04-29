package poppyfanboy.snakegame.data;

/**
 * Class "IntVector"
 *
 * Represents coordinates of a dot in a 2-d array
 *
 * @author PoppyFanboy
 */

public class IntVector {
    private int x;
    private int y;

    private IntVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static IntVector vector(int x, int y) {
        return new IntVector(x, y);
    }

    public static IntVector vector(double x, double y) {
        return new IntVector((int) x, (int) y);
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

    public IntVector invert() {
        return new IntVector(-this.x, -this.y);
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

    // Unlike the % operator, this method returns vectors
    // only with positive values
    public IntVector mod(int moduloX, int moduloY) {
        int newX = x % moduloX;
        int newY = y % moduloY;
        if (newX < 0) { newX += moduloX; }
        if (newY < 0) { newY += moduloY; }

        return new IntVector(newX, newY);
    }

    public IntVector mod(int modulo) {
        return this.mod(modulo, modulo);
    }
}