package edu.hitsz.application.Card;

import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author fanlile
 */
public class SimpleTable {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton deleteButton;
    private JTable scoreTable;
    private JLabel headerLable;
    private JScrollPane tableScrollPanel;
    private List<ScoreRecord> scoreRecords;
    public SimpleTable(ScoreDao scoreDao) {
        String[] columnName = {"名次","玩家昵称","分数","时间"};
        scoreDao.outputFile();
        scoreDao.sort();
        scoreRecords = scoreDao.getAllRecord();
        String[][]tableData = new String[scoreRecords.size()][4];
        int i = 0;
        for (ScoreRecord scoreRecord : scoreRecords){
            tableData [i][0] = String.valueOf(i+1);
            tableData [i][1] = scoreRecord.getUserName();
            tableData [i][2] = String.valueOf(scoreRecord.getScore());
            tableData [i][3] = scoreRecord.getTime();
            i++;
        }
        //表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnName){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        scoreTable.setModel(model);
        tableScrollPanel.setViewportView(scoreTable);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                int result = JOptionPane.showConfirmDialog(deleteButton, "是否确定中删除？");
                if (JOptionPane.YES_OPTION == result && row != -1) {
                    scoreRecords.remove(row);
                    // 将本地文件中的排行榜清空
                    try {
                        FileWriter writer = new FileWriter("simple.txt", false);
                    } catch (IOException ex) {throw new RuntimeException(ex);}
                    // 将删除指定行后的得分记录重新写入本地文件
                    for(ScoreRecord scoreRecord : scoreRecords){
                        scoreDao.writeFile(scoreRecord.getUserName(),scoreRecord.getScore(),scoreRecord.getTime());
                    }
                    model.removeRow(row);
                }
            }
        });
    }
    public JPanel getMainPanel(){
        return mainPanel;
    }
}
