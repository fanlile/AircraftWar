package edu.hitsz.application.Game;

import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.Menu.Input;
import edu.hitsz.application.Menu.StartMenu;
import edu.hitsz.application.MusicThread;
import edu.hitsz.dao.*;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Date;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;
    /**
     * boss机生成分数阈值
     */
    private int boss_threshold = 600;

    private final HeroAircraft heroAircraft;
    private final List<Enemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseProp> baseProps;
    private EnemyFactory enemyFactory;
    private Enemy enemy;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    /**
     * 记录boss机已经产生的数量，防止反复生成
     */
    private int count = 0;
    public ScoreDao scoreDao;

    /**
     * 游戏难度等级
     */
    private static String difficulty;
    /**
     * 音频
     */
    MusicThread bgm;
    MusicThread bossBgm;
    private static boolean needMusic = true;
    private static boolean bossVaild = false;
    private static boolean bossVailded = false;

    /**
     * 排行榜文件路径
     */
    private static String pathName;
    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        baseProps = new LinkedList<>();
        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    public static void setDifficulty(String difficulty){
        Game.difficulty = difficulty;
    }
    public static String getDifficulty(){
        return Game.difficulty;
    }
    public static void setPathName(String pathName){
        Game.pathName = pathName;
    }
    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        Random r1 = new Random();
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                //生成随机数
                int i1 = r1.nextInt(5);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    //分数达到阈值，生成Boss敌机
                    if(score/boss_threshold == (count+1)) {
                        enemyFactory = new BossEnemyFactory();
                        enemy = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemy);
                        bossVaild = true;
                        bossVailded = true;
                        count += 1;
                    }
                    //生成精英敌机
                    else if(i1 == 4){
                        enemyFactory = new EliteEnemyFactory();
                        enemy = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemy);
                    }
                    //生成普通敌机
                    else{
                        enemyFactory = new MobEnemyFactory();
                        enemy = enemyFactory.createEnemy();
                        enemyAircrafts.add(enemy);
                    }
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            //检查音乐
            if (needMusic) {
                musicCheck();
            }

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                // 停止播放背景音乐和boss音乐,并播放结束音乐
                if (needMusic) {
                    bgm.stop();
                    if (bossVailded) {
                        bossBgm.stop();
                    }
                    new MusicThread("src/videos/game_over.wav").start();
                }
                scoreDao = new ScoreDaoImpl();
                // 保存游戏记录并展示排行榜
                JFrame frame = new JFrame("UserName");
                frame.setContentPane(new Input(scoreDao,score,pathName).mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                System.out.println("Game Over!");
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        for(Enemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (Enemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractFlyingObject baseProp : baseProps) {
            baseProp.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (Enemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if (needMusic) {
                        new MusicThread("src/videos/bullet_hit.wav").start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if (enemyAircraft instanceof BossEnemy) {
                           bossVaild = false;
                        }
                        baseProps.addAll(enemyAircraft.drop_prop());
                        score = enemyAircraft.increaseScore(score);
                    }
                }
                // 英雄机与敌机相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (BaseProp baseProp : baseProps) {
            if (baseProp.notValid()) {
                continue;
            }
            if (heroAircraft.crash(baseProp)) {
                baseProp.active(heroAircraft,needMusic);
                baseProp.vanish();
            }
        }
    }

    /**
     * 控制背景音乐与boss音乐播放
     */
    private void musicCheck() {
        //boss音乐
        if (bossVaild) {
            // 存在boss机，停止播放背景音乐
            bgm.stop();
            // 播放boss机背景音乐
            if (bossBgm == null || !bossBgm.isAlive()) {
                bossBgm = new MusicThread("src/videos/bgm_boss.wav");
                bossBgm.start();
            }
        }
        else {
            if (bossVailded) {
                bossBgm.stop();
            }
            // 循环播放背景音乐
            if ((bgm == null || !bgm.isAlive()) && bossVaild == false) {
                bgm = new MusicThread("src/videos/bgm.wav");
                bgm.start();
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        baseProps.removeIf(AbstractFlyingObject::notValid);
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        //绘制敌机
        paintImageWithPositionRevised(g, enemyAircrafts);

        //绘制道具
        paintImageWithPositionRevised(g, baseProps);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
