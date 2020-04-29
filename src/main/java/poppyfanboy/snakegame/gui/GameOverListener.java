package poppyfanboy.snakegame.gui;

/**
 * Interface for classes which have to be notified of the end of the game
 * In this project instances of SnakeGame class invoke gameOver() method
 */

public interface GameOverListener {
    // int score - the score which has been reached at the end
    // of the game
    void gameOver(int score);
}
