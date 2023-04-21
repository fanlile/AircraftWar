package edu.hitsz.application.Menu;

import edu.hitsz.application.Game.*;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.Status;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class StartMenu {
    private JPanel mainPanel;
    private JPanel startMenu;
    private JButton easy;
    private JButton simple;
    private JButton difficult;
    private JLabel label;
    private JRadioButton music;
    private static JFrame frame;
    public StartMenu() {
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg4.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Game.setDifficulty("Easy");
                Game.setPathName("easy.txt");
                Status.menuOver = true;
                synchronized (Status.class) {
                    Status.class.notifyAll();
                }
            }
        });
        simple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg3.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Game.setDifficulty("Simple");
                Game.setPathName("simple.txt");
                Status.menuOver = true;
                synchronized (Status.class) {
                    Status.class.notifyAll();
                }
            }
        });
        difficult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg2.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Game.setDifficulty("Difficult");
                Game.setPathName("difficult.txt");
                Status.menuOver = true;
                synchronized (Status.class) {
                    Status.class.notifyAll();
                }
            }
        });
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("音乐打开");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("StartMenu");
        frame.setContentPane(new StartMenu().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
