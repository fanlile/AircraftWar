package edu.hitsz.dao;

import java.util.List;

public interface ScoreDao {
    List<ScoreRecord> getAllRecord();
    void doAdd(ScoreRecord scoreRecord);
    void sort();
    void writeFile(int score);
    void outputFile();
}
