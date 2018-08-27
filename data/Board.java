package poppyfanboy.snakegame.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

import static poppyfanboy.snakegame.Main.HOME;

public class Board {
    private ArrayList<Record> records = new ArrayList<Record>();
    private int maxSize = 0;
    private int boardNumber;

    public Board(int maxSize, int boardNumber) {
        this.maxSize = maxSize;
        this.boardNumber = boardNumber;
    }

    public void add(Record record) {
        for (int i = 0; i < records.size(); i++) {
            if (record.getNick().equals(records.get(i).getNick()) &&
                    record.getScore() > records.get(i).getScore()) {
                records.remove(i);
                break;
            }
        }
        records.add(record);
        records.sort((record1, record2) ->
                -Integer.compare(record1.getScore(), record2. getScore()));
        if (records.size() > maxSize) {
            records.remove(records.size() - 1);
        }
    }

    public int size() {
        return records.size();
    }

    public Record get(int index) throws IllegalArgumentException {
        if (index < records.size()) {
            return records.get(index);
        } else {
            throw new IllegalArgumentException(
                    String.format(  "There are only %d records in a board %d",
                                    records.size(), boardNumber));
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

    public static Board getScoreBoard(int n) throws IllegalArgumentException {
        boolean boardFound = false;
        Board board = new Board(10, n);

        try (Scanner in = new Scanner(Paths.get(HOME + "data/Scoreboard.tab"))) {
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

    public static void saveRecord(Record record, int boardNumber) {
        Board board = getScoreBoard(boardNumber);
        board.add(record);

        String buffer = "";
        // "true" if we are going through records related to the board
        // with a number specified in "boardNumber" argument
        boolean updatedBoard = false;

        try (Scanner in = new Scanner(Paths.get(HOME + "data/Scoreboard.tab"))) {
            while (in.hasNext()) {
                String line = in.nextLine();
                String[] tokens = line.split(" ");

                if (tokens[0].equals("@board") && tokens.length > 1) {
                    if (Integer.valueOf(tokens[1]) == boardNumber) {
                        updatedBoard = true;
                    } else {
                        updatedBoard = false;
                        buffer += "@board " + Integer.valueOf(tokens[1]) + "\n";
                    }
                    continue;
                }

                if (!updatedBoard) {
                    buffer += line + "\n";
                }
            }
            in.close();

            PrintWriter writer = new PrintWriter(HOME + "data/Scoreboard.tab");
            writer.write(buffer + board.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String s = "@board " + boardNumber + "\n";
        for (Record record : records) {
            s += record.toString() + "\n";
        }
        return s;
    }
}
