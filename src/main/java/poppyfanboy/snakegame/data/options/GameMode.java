package poppyfanboy.snakegame.data.options;

public enum GameMode {
    EASY(10, 30.0), MEDIUM(20, 15.0), HARD(40, 7.0), ZEN(1, Double.POSITIVE_INFINITY);

    private final int mult;
    private final double speedUpInterval;

    GameMode(int mult, double speedUpInterval) {
        this.mult = mult;
        this.speedUpInterval = speedUpInterval;
    }

    public int getMult() {
        return mult;
    }

    public double getSpeedUpInterval() {
        return speedUpInterval;
    }

    public static int getMult(int gameModeType) {
        return values()[gameModeType].mult;
    }
}
