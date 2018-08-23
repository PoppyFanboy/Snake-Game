package poppyfanboy.snakegame.data;

import java.util.*;

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

    public boolean isNewHighScore(Record newRecord) {
        if (maxSize > records.size()) {
            return true;
        }

        for (Record record : records) {
            if (record.getScore() < newRecord.getScore()) {
                return true;
            }
        }

        return false;
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
