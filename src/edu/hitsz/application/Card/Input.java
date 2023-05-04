package edu.hitsz.application.Card;

import edu.hitsz.dao.ScoreDao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author fll
 */
public class Input {
    public JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel nameLable;
    private JLabel score;
    private JTextField inputName;
    private JButton confirm;
    private JLabel gameScore;
    private String userName;


public Input(ScoreDao scoreDao,int score,String pathName) {

    //显示得分
    gameScore.setText(String.valueOf(score));

    confirm.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 获取玩家昵称
            userName = inputName.getText();
            // 获取当前时间
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDateTime = formatter.format(date);
            //
            scoreDao.setPathName(pathName);
            // 将得分记录写入本地文件
            scoreDao.writeFile(userName,score,formattedDateTime);

            //展示排行榜
            if(Objects.equals(pathName, "easy.txt")){
                Main.cardPanel.add(new EasyTable(scoreDao).getMainPanel());
            }
            else if (Objects.equals(pathName, "simple.txt")) {
                Main.cardPanel.add(new SimpleTable(scoreDao).getMainPanel());
            }
            else {
                Main.cardPanel.add(new DifficultTable(scoreDao).getMainPanel());
            }
            Main.cardLayout.last(Main.cardPanel);
        }
    });
}
}
