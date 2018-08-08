package poppyfanboy.snakegame;

/**
 * Class "Direction"
 * Instance of a class stores current direction of a snake
 * and provides methods getOffsetX() and getOffsetX() which
 * return coordinates of snake's head block on the next step
 *
 * This class allows us not to use switch or else-if statements
 * twice (when we save direction after key pressed and when
 * we decide which coordinate to shift in move() method
 *
 * @author PoppyFanboy
 */

import javafx.scene.input.KeyCode;

public class Direction {
    private int offsetX = 0;
    private int offsetY = 0;

    // 0 - North, 1 - East, 2 - South, 3 - West
    private int dir;

    Direction(KeyCode code) {
        if (code == KeyCode.UP) {
            offsetY = -1;
            dir = 0;
        } else if (code == KeyCode.RIGHT) {
            offsetX = 1;
            dir = 1;
        } else if (code == KeyCode.DOWN) {
            offsetY = 1;
            dir = 2;
        } else if (code == KeyCode.LEFT) {
            offsetX = -1;
            dir = 3;
        }
    }

    // returns false if new direction is on the same
    // line as the old direction of a snake
    boolean ableToChange(Direction newDir) {
        // this part may cause bugs if dir is initially negative
        // but since dir field is encapsulated in Direction class
        // its value is always non-negative and less than 4
        if ((dir - newDir.getDirInt() + 4) % 4 == 2) {
            return false;
        }
        return true;
    }

    int getOffsetX() {
        return offsetX;
    }

    int getOffsetY() {
        return offsetY;
    }

    int getDirInt() {
        return dir;
    }
}