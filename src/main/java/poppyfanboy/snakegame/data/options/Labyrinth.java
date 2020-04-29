package poppyfanboy.snakegame.data.options;

import poppyfanboy.snakegame.Main;
import poppyfanboy.snakegame.data.IntVector;
import poppyfanboy.snakegame.logic.Field;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public final class Labyrinth {
    private static final int[] MULTIPLIERS = { 1, 2, 2, 2, 3 };
    public static final int NUM_OF_LABS = 5;

    private int labyrinthType;
    private HashSet<IntVector> blocksCoords;
    private String name;

    public Labyrinth(int labyrinthType) {
        if (labyrinthType >= 0 && labyrinthType < NUM_OF_LABS) {
            this.labyrinthType = labyrinthType;
        } else {
            throw new IllegalArgumentException("There is no labyrinth with index " + labyrinthType);
        }

        blocksCoords = new HashSet<>();

        try (Scanner in = new Scanner(getClass().getResourceAsStream(
                "/labyrinths/" + labyrinthType + ".lab"))) {
            name = in.nextLine();

            int i = 0;
            while (in.hasNextLine()) {
                String[] blocks = in.nextLine().split(" ");
                for (int j = 0; j < blocks.length; j++) {
                    if (blocks[j].equals("1")) {
                        blocksCoords.add(IntVector.vector(j, i));
                    }
                }
                i++;
            }
        }
    }

    public int getMult() {
        return getMult(labyrinthType);
    }

    public int getLabyrinthType() {
        return labyrinthType;
    }

    public HashSet<IntVector> getBlocksCoords() {
        return new HashSet<>(blocksCoords);
    }

    @Override
    public String toString() {
        return name;
    }

    public static int getMult(int labyrinthType) {
        return MULTIPLIERS[labyrinthType];
    }
}
