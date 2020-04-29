package poppyfanboy.snakegame.data;

/**
 * An instance of this class contains a list of Records
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import poppyfanboy.snakegame.data.options.Labyrinth;

public class Board {
    private ArrayList<Record> records = new ArrayList<Record>();
    private int maxSize = 0;
    private int boardNumber;

    public Board(int maxSize, int boardNumber) {
        this.maxSize = maxSize;
        this.boardNumber = boardNumber;
    }

    // Adds the record to the board
    // There cannot be two records on a board with the same nicknames
    public int add(Record record) {
        for (int i = 0; i < records.size(); i++) {
            if (record.getNick().equals(records.get(i).getNick())) {
                if (record.getScore() > records.get(i).getScore()) {
                    records.remove(i);
                    break;
                } else {
                    return -(i + 1);
                }
            }
        }

        int position = records.size();
        for (int i = records.size() - 1; i >= 0; i--) {
            if (record.getScore() > records.get(i).getScore()) {
                position = i;
            }
        }

        records.add(position, record);
        if (records.size() > maxSize) {
            records.remove(records.size() - 1);
        }

        if (position < maxSize) {
            return position + 1;
        } else {
            return 0;
        }
    }

    public int size() {
        return records.size();
    }

    public Record get(int index) throws IllegalArgumentException {
        if (index < records.size() && index >= 0) {
            return records.get(index);
        } else {
            throw new IllegalArgumentException(
                    String.format("Invalid index %d for board %d",
                                    index, boardNumber));
        }
    }

    public boolean isNewHighScore(int newScore) {
        if (maxSize > records.size()) {
            return true;
        }

        for (Record record : records) {
            if (record.getScore() < newScore) {
                return true;
            }
        }
        return false;
    }

    public static Board getScoreBoard(int n, String path) throws IllegalArgumentException, IOException {
        boolean boardFound = false;
        Board board = new Board(10, n);

        File scoreBoard = new File(path);
        if (!scoreBoard.exists()) {
            scoreBoard.getParentFile().mkdirs();
            scoreBoard.createNewFile();
            FileWriter fw = new FileWriter(scoreBoard);
            for (int i = 0; i < Labyrinth.NUM_OF_LABS; i++)
                fw.write(String.format("@board %d\n", i));
            fw.close();
        }

        try (Scanner in = new Scanner(scoreBoard)) {
            while (in.hasNext()) {
                String line = in.nextLine();
                String[] tokens = line.split(" ");

                if (tokens[0].equals("@board") && tokens.length > 1) {
                    if (Integer.valueOf(tokens[1]) == n) {
                        boardFound = true;
                    } else if (boardFound) {
                        break;
                    }
                    continue;
                }

                if (boardFound) {
                    board.add(new Record(line));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!boardFound) {
            throw new IllegalArgumentException("There is no scoreboard with order " + n);
        }

        return board;
    }

    public static int saveRecord(Record record, int boardNumber, String path) throws IOException {
        Board board = getScoreBoard(boardNumber, path);
        int position = board.add(record);

        StringBuilder buffer = new StringBuilder();

        // "true" if we are going through records related to the board
        // with a number specified in "boardNumber" argument
        boolean boardToBeUpdated = false;

        try (Scanner in = new Scanner(Paths.get(path))) {
            while (in.hasNext()) {
                String line = in.nextLine();
                String[] tokens = line.split(" ");

                if (tokens[0].equals("@board") && tokens.length > 1) {
                    if (Integer.valueOf(tokens[1]) == boardNumber) {
                        boardToBeUpdated = true;
                    } else {
                        boardToBeUpdated = false;
                        buffer.append("@board ").append(Integer.valueOf(tokens[1])).append("\n");
                    }
                    continue;
                }

                if (!boardToBeUpdated) {
                    buffer.append(line).append("\n");
                }
            }
            in.close();

            PrintWriter writer = new PrintWriter(path);
            writer.write(buffer + board.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return position;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("@board ").append(boardNumber).append("\n");
        for (Record record : records) {
            builder.append(record.toString()).append("\n");
        }
        return builder.toString();
    }
}
