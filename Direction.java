package poppyfanboy.snakegame;

/**
 * Enumeration "Direction"
 * Provides methods that return offsets by x and y axises
 * according to the direction type (up, right, left or down)
 *
 * original idea does not belong to me
 */

enum Direction {
    UP, RIGHT, DOWN, LEFT;

    private static final Offset[] OFFSETS = { new Offset(0, -1), new Offset(1, 0),
                                              new Offset(0, 1),  new Offset(-1, 0)};

    int offsetX() {
        return OFFSETS[this.ordinal()].offsetX;
    }

    int offsetY() {
        return OFFSETS[this.ordinal()].offsetY;
    }

    boolean ableToChange(Direction newDir) {
        if ((newDir.ordinal() - this.ordinal() + 4) % 4 == 2) {
            return false;
        }
        return true;
    }
}