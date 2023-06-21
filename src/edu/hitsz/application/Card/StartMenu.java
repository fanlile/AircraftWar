package edu.hitsz.application.Card;

import edu.hitsz.application.Game.*;
import edu.hitsz.application.ImageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author fll
 */
public class StartMenu {
    private JPanel mainPanel;
    private JPanel startMenu;
    private JButton easy;
    private JButton simple;
    private JButton difficult;
    private JLabel label;
    private JRadioButton music;
    private static JFrame frame;
    private static Game game;
    public static boolean needMusic = true;
    public StartMenu() {
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg1.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Game.setDifficulty(0);
                Game.setPathName("easy.txt");
                game = new EasyGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
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
                Game.setDifficulty(1);
                Game.setPathName("simple.txt");
                game = new SimpleGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });
        difficult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Game.setDifficulty(6);
                Game.setPathName("difficult.txt");
                game = new DifficultGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                needMusic = !needMusic;
            }
        });
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
