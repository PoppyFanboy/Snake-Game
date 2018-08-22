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
                Record[] scores = getScoreBoard(i);
                for (int j = 0; j < scores.length; j++) {
                    Label score =   new Label(String.format(" %d. ", j + 1) +
                                    String.format("%-25s", scores[j].nick) +
                                    String.format("%d", scores[j].score));

                    score.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
                    boards[i].getChildren().add(score);
                }
            } catch (IllegalArgumentException ex) { }
        }
    }


    // при вводе никнейма пользователем нужно учесть, чтобы ник
    // был ненулевой длины, если ник состоит только из цифр или содержит пробелы (ведущие пробелы нужно убирать),
    // то его надо брать в кавычки

    // чет странное получилось
    private Record[] getScoreBoard(int n) throws IllegalArgumentException {
        int currBoard = -1;
        boolean boardFound = false;
        ArrayList<Record> records = new ArrayList<Record>();

        try {
            Scanner in = new Scanner(Paths.get(HOME + "data/Scoreboard.tab"));

            while (in.hasNext()) {
                String line = in.nextLine();

                if (line.equals("@board")) {
                    currBoard++;
                    if (currBoard == n) {
                        boardFound = true;
                    } else if (currBoard > n) {
                        //return records.toArray(new Record[records.size()]);
                        break;
                    }
                    continue;
                }

                if (boardFound) {
                    records.add(StringUtils.unescapeRecord(line));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!boardFound) {
            throw new IllegalArgumentException("There is no scoreboard with order " + n);
        }

        return records.toArray(new Record[records.size()]);
    }

    public static void main(String[] args) {
    }
}

// два класса ниже надо, скорее всего, совместить, вынести в отдельный файл,
// добавить методы getEscapedNick() и getUnescapedNick(),
// и потом добавить метод save, который бы сохранял новый рекорд в файл

class StringUtils {
    public static String escapeString(String original) {
        StringBuilder builder = new StringBuilder("\"");

        for (int i = 0; i < original.length(); i++) {
            char nextChar = original.charAt(i);
            if (nextChar == ' ' || nextChar == '"' || nextChar == '\'' ||
                nextChar == '\\') {
                builder.append("\\");
            }
            builder.append(nextChar);
        }
        builder.append("\"");

        return builder.toString();
    }

    public static Record unescapeRecord(String original) {
        String nick = "";
        int score = -1;

        try {
            StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(original));
            tokenizer.wordChars(0, 255);        // allow any characters (except for whitespaces)
            tokenizer.whitespaceChars(32, 32);  // to be a part of a nickname
            tokenizer.quoteChar('"');           // inside the quotation characters pair "\\t" will be interpreted as tab,
                                                // "\\\"" will be interpreted as a single quotation mark and so on...

            while (tokenizer.ttype != StreamTokenizer.TT_EOF) {
                int tokenType = tokenizer.nextToken();
                if ((tokenType == '"' || tokenType == StreamTokenizer.TT_WORD) && nick.equals("")) {
                    nick = tokenizer.sval;
                }

                if (tokenType == StreamTokenizer.TT_NUMBER && score < 0) {
                    score = (int) tokenizer.nval;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Record(truncateString(nick, 24), score);
    }

    public static String truncateString(String s, int newLength) {
        int allowedLength = newLength - "...".length();
        if (allowedLength <= 0) {
            throw new IllegalArgumentException(
                    String.format(  "Length of the original string %s" +
                                    "is less, than specified new length (%d)", s, newLength));
        }

        boolean needElipsis = allowedLength < s.length();
        s = s.substring(0, Math.min(allowedLength, s.length()));
        if (needElipsis) {
            s += "...";
        }

        return s;
    }

    // недоделанный
    public static String removeLeadingWhitespaces() {
        return "";
    }
}

class Record {
    public String nick;
    public int score;

    Record(String nick, int score) {
        this.nick = nick;
        this.score = score;
    }
}