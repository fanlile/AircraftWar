package edu.hitsz.dao;

import java.util.ArrayList;
import java.util.List;

public class RankingsImpl implements Rankings {
    private List<ScoreRecord> scoreRecords;
    public RankingsImpl(){
        scoreRecords = new ArrayList<ScoreRecord>();
    }
    /**
     * 获取所有得分记录
     */
    @Override
    public List<ScoreRecord> getAllRecord(){
        return scoreRecords;
    }
    /**
     * 增加分数记录
     */
    @Override
    public void doAdd(ScoreRecord scoreRecord){
        scoreRecords.add(scoreRecord);
    }
}
