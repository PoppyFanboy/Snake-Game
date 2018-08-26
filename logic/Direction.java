package poppyfanboy.snakegame.logic;

/**
 * Enumeration "Direction"
 * Provides methods that return offsets by x and y axises
 * according to the direction type (up, right, left or down)
 *
 * original idea does not belong to me
 */

enum Direction {
    UP, RIGHT, DOWN, LEFT;

    private static final IntVector[] OFFSETS = { new IntVector(0, -1), new IntVector(1, 0),
                                                 new IntVector(0, 1),  new IntVector(-1, 0) };

    IntVector getOffset() {
        return OFFSETS[this.ordinal()];
    }

    boolean ableToChange(Direction newDir) {
        if ((newDir.ordinal() - this.ordinal() + 4) % 4 == 2) {
            return false;
        }
        return true;
    }
}