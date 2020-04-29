package poppyfanboy.snakegame.logic;

/**
 * Enumeration "Direction"
 * Provides methods that return offsets by x and y axises
 * according to the direction type (up, right, left or down)
 *
 * original idea does not belong to me
 */

import poppyfanboy.snakegame.data.IntVector;

import static poppyfanboy.snakegame.data.IntVector.vector;

enum Direction {
    UP, RIGHT, DOWN, LEFT;

    private static final IntVector[] OFFSETS = { vector(0, -1), vector(1, 0),
                                                 vector(0, 1),  vector(-1, 0) };

    IntVector getOffset() {
        return OFFSETS[this.ordinal()];
    }

    // The current direction is "ableToChange" from current direction
    // to "newDir", if "newDir" is not opposite to current direction
    boolean ableToChange(Direction newDir) {
        if ((newDir.ordinal() - this.ordinal() + 4) % 4 == 2) {
            return false;
        }
        return true;
    }

    Direction inverse() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        return UP;
    }
}