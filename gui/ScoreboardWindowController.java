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

import static poppyfanboy.snakegame.Main.HOME;

public class ScoreboardWindowController {
    @FXML private Scene scoreboardScene;
    @FXML private FlowPane scoreboardPane1, scoreboardPane2, scoreboardPane3;
    
    @FXML
    private void initialize() {
        FlowPane[] boards = { this.scoreboardPane1, this.scoreboardPane2, this.scoreboardPane3 };
        for (int i = 0; i < boards.length; i++) {
            try {
                Score[] scores = getScoreBoard(i);
                for (int j = 0; j < scores.length; j++) {
                    Label score = new Label(String.format(" %d. ", j + 1) + String.format("%-20s", scores[j].nick)+ String.format("%d", scores[j].score));
                    score.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
                    boards[i].getChildren().add(score);
                }
            } catch (IllegalArgumentException ex) { }
        }
    }
    
    // bugs:
    //  - long nicks are not supported
    //  - whitespace among the nick leads to the unhandled exception
    private Score[] getScoreBoard(int n) throws IllegalArgumentException {
        try {
            n++;
            Scanner in = new Scanner(Paths.get(HOME + "data/Scoreboard.tab"));
            while (in.hasNext()) {
                String[] tag = in.nextLine().split(" ");
                
                if (tag[0].equals("@board") && Integer.valueOf(tag[1]) == n) {
                    int length = Integer.valueOf(tag[2]);
                    Score[] scores = new Score[length];
                    for (int i = 0; i < length; i++) {
                        String line = in.nextLine();
                        scores[i] = new Score(line.split(" ")[0], Integer.valueOf(line.split(" ")[1]));
                    }
                    in.close();
                    return scores;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        throw new IllegalArgumentException("There is no scoreboard with order " + n);
    }
    
    class Score {
        public String nick;
        public int score;
        
        Score(String nick, int score) {
            this.nick = nick;
            this.score = score;
        }
    }
}
