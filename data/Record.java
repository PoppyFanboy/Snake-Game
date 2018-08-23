package poppyfanboy.snakegame.data;

import java.io.*;
import static java.lang.Math.min;

/**
 * Class "Record"
 * Used to represent a single record in a Scoreboard
 * (int score and String name)
 *
 * Constructors save the nickname without leading/closing whitespaces
 * and with no double whitespaces
 *
 * @author PoppyFanboy
 */

public class Record {
    private String nick;
    private int score;

    public Record(String nick, int score) {
        this.nick = removeExcessWhitespaces(nick);
        this.score = score;
    }

    // Converts the String line (that contains record)
    // line = "\"Nickname\" Score"
    // quotation marks can be omitted if Nickname does not contain
    // whitespaces and does not consists only of digits
    public Record(String line) {
        nick = "";
        score = -1;

        try {
            StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(line));
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

        nick = removeExcessWhitespaces(nick);
    }

    public String getNick() {
        return nick;
    }

    public int getScore() {
        return score;
    }

    // Truncates given string to the given length
    public static String truncateString(String s, int newLength) {
        int allowedLength = newLength - "...".length();
        if (allowedLength <= 0) {
            throw new IllegalArgumentException(
                    String.format(  "Length of the original string %s" +
                            "is less, than specified new length (%d)", s, newLength));
        }

        boolean needElipsis = allowedLength < s.length();
        s = s.substring(0, min(allowedLength, s.length()));
        if (needElipsis) {
            s += "...";
        }

        return s;
    }

    // returns Record represented as a String
    // (nick is closed in a quotation marks if needed)
    @Override
    public String toString() {
        if (needsQuotation(nick)) {
            return quoteString(nick) + " " + score;
        } else {
            return nick + " " + score;
        }
    }

    // Adds quotation marks on the both sides of the string
    // and escapes '\\', '\'', '"' characters
    private static String quoteString(String original) {
        StringBuilder builder = new StringBuilder("\"");

        for (int i = 0; i < original.length(); i++) {
            char nextChar = original.charAt(i);
            if (nextChar == '"' ||
                    nextChar == '\'' || nextChar == '\\') {
                builder.append("\\");
            }
            builder.append(nextChar);
        }
        builder.append("\"");

        return builder.toString();
    }

    // Removes: leading whitespaces, whitespaces after the last string
    // in a nickname, double whitespaces
    // Changes tabs and new-line-characters om whitespaces
    private static String removeExcessWhitespaces(String s) {
        int firstCharIndex = -1;
        int lastCharIndex = -1;
        int spacesInARow = 0;

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (firstCharIndex == -1) {
                if (s.charAt(i) == ' ') {
                    continue;
                } else if (firstCharIndex == -1) {
                    firstCharIndex = i;
                }
            }

            if (s.charAt(i) == ' ' || s.charAt(i) == '\t' || s.charAt(i) == '\n') {
                spacesInARow++;
                continue;
            }

            if (firstCharIndex != -1 && i > lastCharIndex) {
                if (spacesInARow > 0) {
                    builder.append(' ');
                }
                builder.append(s.charAt(i));
                lastCharIndex = i;
                spacesInARow = 0;
            }
        }

        return builder.toString();
    }

    private static boolean needsQuotation(String s) {
        boolean onlyNumbers = true;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                onlyNumbers = false;
            }
            if (s.charAt(i) == ' ') {
                return true;
            }
        }
        return onlyNumbers;
    }
}