package edu.hitsz.application.Menu;

import edu.hitsz.application.Status;
import edu.hitsz.dao.ScoreDao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JButton yes;
    private JLabel gameScore;
    private String userName;


public Input(ScoreDao scoreDao,int score,String pathName) {
    gameScore.setText(String.valueOf(score));
    yes.addActionListener(new ActionListener() {
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
            //Status.inputOver = true;
            JFrame frame = new JFrame("排行榜");
            if(pathName == "easy.txt"){
                frame.setContentPane(new EasyTable(scoreDao).getMainPanel());
            }
            else if (pathName == "simple.txt") {
                frame.setContentPane(new SimpleTable(scoreDao).getMainPanel());
            }
            else {
                frame.setContentPane(new DifficultTable(scoreDao).getMainPanel());
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    });
}
}
