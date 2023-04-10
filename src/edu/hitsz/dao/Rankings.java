package edu.hitsz.dao;

import java.util.List;

public interface Rankings {
    List<ScoreRecord> getAllRecord();
    void doAdd(ScoreRecord scoreRecord);
}
