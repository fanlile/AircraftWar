package edu.hitsz.dao;

import java.util.List;

public interface ScoreDao {
    List<ScoreRecord> getAllRecord();
    void doAdd(ScoreRecord scoreRecord);
    void sort();

    void setPathName(String pathName);

    void writeFile(String userName, int score, String formattedDateTime);
    void outputFile();
}
