package edu.hitsz.dao;

import java.io.*;
import java.util.*;

/**
 * @author fll
 */
public class ScoreDaoImpl implements ScoreDao {
    private String pathName;
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
        Collections.sort(scoreRecords, (b,a)->{return a.getScore() - b.getScore();});
    }
    /**
     * 设置文件路径
     */
    @Override
    public void setPathName(String pathName){
        this.pathName = pathName;
    }
    /**
     *将得分数据写入本地文件
     */
    @Override
    public void writeFile(String userName,int score,String formattedDateTime) {
        String score_ = Integer.toString(score);
        File file = new File(pathName);
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(userName+"，"+score_+"，"+formattedDateTime);
            bw.newLine();

            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    /**
     * 从文件中读出数据并输出
     */
    @Override
    public void outputFile() {
        //将存放在本地文件中的数据读入到程序中
        try {
            File file = new File(pathName);
            Scanner scanner = new Scanner(file);
            // 判断文件是否还有下一行，如果有则读取
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //将字符串按逗号分隔，放入字符串数组中
                String[] parts = line.split("，");
                scoreRecords.add(new ScoreRecord(parts[0],Integer.parseInt(parts[1]),parts[2]));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
