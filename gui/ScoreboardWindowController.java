package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.io.*;
import java.nio.file.Paths;

import poppyfanboy.snakegame.data.*;
import static poppyfanboy.snakegame.Main.HOME;

public class ScoreboardWindowController {
    @FXML private Scene scoreboardScene;
    @FXML private FlowPane scoreboardPane1, scoreboardPane2, scoreboardPane3;
    
    @FXML
    private void initialize() {
        FlowPane[] boards = { this.scoreboardPane1, this.scoreboardPane2, this.scoreboardPane3 };
        for (int i = 0; i < boards.length; i++) {
            try {
                Board board = getScoreBoard(i);
                for (int j = 0; j < board.size(); j++) {
                    Record record = board.get(j);
                    Label score =   new Label(String.format(" %d. ", j + 1) +
                                    String.format("%-25s", Record.truncateString(record.getNick(), 24)) +
                                    String.format("%d", record.getScore()));

                    score.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
                    boards[i].getChildren().add(score);
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Board getScoreBoard(int n) throws IllegalArgumentException {
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

    private static void saveRecord(Record record, int boardNumber) {
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
}
