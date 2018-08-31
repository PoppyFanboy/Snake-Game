package poppyfanboy.snakegame.data;

import java.io.*;

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

        try (StringReader sReader = new StringReader(line)) {
            StreamTokenizer tokenizer = new StreamTokenizer(sReader);
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

        if (allowedLength < s.length()) {
            s = s.substring(0, allowedLength);
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
    private static String quoteString(String s) {
        // $1 is a reference to ('|"|\\) group
        return "\"" + s.replace("(\'|\"|\\\\)", "\\\\$1") + "\"";
    }

    // Removes: leading whitespaces, whitespaces after the last string
    // in a nickname, double whitespaces
    // Changes tabs and new-line-characters om whitespaces
    public static String removeExcessWhitespaces(String s) {
        // trim() method truncates leading/trailing whitespaces
        // \\s includes [ \t\n\x0B\f\r]
        return s.replaceAll("\\s+", " ").trim();
    }

    private static boolean needsQuotation(String s) {
        boolean onlyNumbers = true;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                onlyNumbers = false;
            }
            if (s.charAt(i) == ' ' || s.charAt(i) == '@'
                    || (s.charAt(0) >= '0' && s.charAt(0) <= '9')) {
                return true;
            }
        }
        return onlyNumbers;
    }
}