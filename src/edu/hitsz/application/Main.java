package edu.hitsz.application;

import edu.hitsz.application.Game.*;
import edu.hitsz.application.Menu.EasyTable;
import edu.hitsz.application.Menu.StartMenu;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    private static Game game;
    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartMenu startMenu = new StartMenu();
        JPanel menu = startMenu.getMainPanel();

        frame.add(menu);
        frame.setVisible(true);
        // 选择难度后关闭该窗口
        new Thread(()->{
            synchronized (Status.class) {
                while(!Status.menuOver) {
                    try {
                        Status.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (Game.getDifficulty() == "Easy") {
                game = new EasyGame();
            } else if (Game.getDifficulty() == "Simple") {
                game = new SimpleGame();
            } else if (Game.getDifficulty() == "Difficult") {
                game = new DifficultGame();
            } else {
                throw new RuntimeException("难度选择异常");
            }
            frame.remove(menu);
            // 打开游戏窗口
            Game game = new Game();
            frame.add(game);
            frame.setVisible(true);
            game.action();
        }).start();
        /*
        // 游戏结束后弹出输入框
        new Thread(()->{
            synchronized (Status.class) {
                while (!Status.gameOver) {
                    try {
                        Status.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(123);
            //展示排行榜
            frame.remove(game);

        }).start();

        // 游戏结束后展示排行榜
        new Thread(()->{
            synchronized (Status.class) {
                while (!Status.inputOver) {
                    try {
                        Status.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(123);
            //展示排行榜
            frame.remove(game);
            if (Game.getDifficulty() == "Easy") {
                frame.add(new EasyTable(game.scoreDao).getMainPanel());
            } else if (Game.getDifficulty() == "Simple") {
                game = new SimpleGame();
            } else if (Game.getDifficulty() == "Difficult") {
                game = new DifficultGame();
            } else {
                throw new RuntimeException("难度选择异常");
            }
        }).start();
        */
    }
}
