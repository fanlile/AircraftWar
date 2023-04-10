package edu.hitsz.dao;

import java.util.ArrayList;
import java.util.List;

public class ScoreDaoImpl implements ScoreDao {
    private List<ScoreRecord> scoreRecords;
    public ScoreDaoImpl() {
        scoreRecords = new ArrayList<ScoreRecord>();
    }
    /**
     * 获取所有得分记录
     */
    @Override
    public List<ScoreRecord> getAllRecord() {
        return scoreRecords;
    }
    /**
     * 增加分数记录
     */
    @Override
    public void doAdd(ScoreRecord scoreRecord) {
        scoreRecords.add(scoreRecord);
    }
    /**
     * 按照score的高低，对List<ScoreRecord>进行排序
     */
    @Override
    public void sort() {

    }
}
