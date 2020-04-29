package poppyfanboy.snakegame.data.options;

/**
 * A simple class which represents
 * a set of Snake Game preferences
 */

public final class Options {
    private Labyrinth labyrinth;
    private GameMode gameMode;
    private int zenSpeedLevel = 1;

    public Options(Labyrinth labyrinth, GameMode gameMode, int zenSpeedLevel) {
        this.labyrinth = labyrinth;
        this.gameMode = gameMode;
        this.zenSpeedLevel = zenSpeedLevel;
    }

    public int getMult() {
        return labyrinth.getMult() * gameMode.getMult();
    }

    public Labyrinth getLabyrinth() {
        return labyrinth;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getZenSpeedLevel() {
        return zenSpeedLevel;
    }

    @Override
    public String toString() {
        return gameMode + "\n" + labyrinth;
    }
}
