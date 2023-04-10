package edu.hitsz.dao;

public class ScoreRecord {
    private String userName;
    private int score;
    private int time;
    ScoreRecord(String userName, int score, int time){
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

}
