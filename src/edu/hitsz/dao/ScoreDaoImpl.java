package edu.hitsz.dao;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fll
 */
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
        Collections.sort(scoreRecords, (b,a)->{return a.getScore() - b.getScore();});
    }

    /**
     *将得分数据写入本地文件
     */
    @Override
    public void writeFile(int score) {
        String score_ = Integer.toString(score);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDateTime = formatter.format(date);
        File file = new File("scoreRecord.txt");
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("testUserName，"+score_+"，"+formattedDateTime);
            bw.newLine();

            bw.close();
            fw.close();

            System.out.println("Data has been written to " + file);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    /**
     * 从文件中读出数据并输出
     */
    @Override
    public void outputFile() {
        System.out.println("********************************************");
        System.out.println("                  得分排行榜                  ");
        System.out.println("********************************************");
        //将存放在本地文件中的数据读入到程序中
        try {
            File file = new File("scoreRecord.txt");
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
        sort();
        int i = 1;
        for(ScoreRecord scoreRecord: scoreRecords) {
            System.out.println(
                    "第"+i+"名："+"\t"+
                            scoreRecord.getUserName()+"\t"+
                            scoreRecord.getScore()+"\t"+"\t"+
                            scoreRecord.getTime()
            );
            i += 1;
        }
    }
}
