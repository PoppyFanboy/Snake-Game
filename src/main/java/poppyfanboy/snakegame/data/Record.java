package poppyfanboy.snakegame.data;

/**
 * Class "Record"
 * Is used to represent a single record in a Scoreboard
 * (int score and String name)
 *
 * Constructors save the nickname without leading/closing whitespaces
 * and with no double whitespaces
 *
 * @author PoppyFanboy
 */

import java.io.*;

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
    // whitespaces and does not consist only of digits
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
            return "";
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
        return "\"" + s.replaceAll("(\'|\"|\\\\)", "\\\\$1") + "\"";
    }

    // Removes: leading whitespaces, whitespaces after the last
    // non-whitespace character in a nickname, double whitespaces
    // Changes tabs and new-line-characters on whitespaces
    public static String removeExcessWhitespaces(String s) {
        // trim() method truncates leading/trailing whitespaces
        // \\s includes [ \t\n\x0B\f\r]
        return s.replaceAll("\\s+", " ").trim();
    }

    // If the nickname contains whitespace-characters, '@'s,
    // quotes, back slashes or the first character is a digit
    // then the string needs to be quoted before writing it
    // into the Scoreboard.tab
    private static boolean needsQuotation(String s) {
        return s.matches("(.*(\\s+|@|\"|\'|\\\\).*|\\d.*)");
    }
}