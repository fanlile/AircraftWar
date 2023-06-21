package edu.hitsz.application.Game;

import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Card.Main;
import edu.hitsz.application.Card.Input;
import edu.hitsz.application.Card.StartMenu;
import edu.hitsz.application.MusicThread;
import edu.hitsz.dao.*;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author fll
 */
public abstract class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    public static List<Enemy> enemyAircrafts;
    public static List<BaseBullet> heroBullets;
    public static List<BaseBullet> enemyBullets;
    protected final List<BaseProp> baseProps;
    protected EnemyFactory enemyFactory;
    protected Enemy enemy;

    /**
     * 当前得分
     */
    public static int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示英雄机子弹的发射、敌机的产生频率
     */
    private final int heroCycleDuration = 450;
    private int heroCycleTime = 0;

    protected int enemyCycleTime = 0;

    /**
     * 周期（ms）
     * 指示游戏难度提升的频率
     */

    private final int difficultyCycleDuration = 10000;
    private int difficultyCycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    public ScoreDao scoreDao;

    /**
     * 游戏难度等级
     */
    protected static int difficulty;
    /**
     * 音频
     */
    MusicThread bgm;
    MusicThread bossBgm;
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
        /*
          Scheduled 线程池，用于定时任务调度
          关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
          apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    public static void setDifficulty(int difficulty){
        Game.difficulty = difficulty;
    }

    public static void setPathName(String pathName){
        Game.pathName = pathName;
    }
    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            time += timeInterval;
            // 周期性执行（控制游戏难度提升频率）
            if (difficultyTimeCountAndNewCycleJudge()) {
                    difficulty += 1;
                    System.out.println("------------------------");
                    System.out.println("游戏难度提升！ 当前游戏难度为："+difficulty);
                    System.out.println("最大敌机数量："+difficulty);
                    System.out.println("敌机血量提升倍率："+(1+difficulty*0.1));
                    System.out.println("Boss机血量提升！");
                    System.out.println("敌机生成加快！");
                    System.out.println("敌机射击加快！");
            }
            // 周期性执行（控制英雄机射击频率）
            if (heroTimeCountAndNewCycleJudge()) {
                // 新敌机产生
                newEnemy();
                // 英雄机射出子弹
                heroAircraftShootAction();
            }

            // 周期性执行（控制敌机生成频率和敌机射击频率）
            if (enemyTimeCountAndNewCycleJudge()) {
                newEnemy();
                enemyShootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 检查音乐
            if (StartMenu.needMusic) {
                musicCheck();
            }

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();

            // 游戏结束检查及处理
            overCheckAndProcess();
        };

        /*
          以固定延迟时间进行执行
          本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    protected boolean difficultyTimeCountAndNewCycleJudge() {
        difficultyCycleTime += timeInterval;
        if (difficultyCycleTime >= difficultyCycleDuration && difficultyCycleTime - timeInterval < difficultyCycleTime) {
            // 跨越到新的周期
            difficultyCycleTime %= difficultyCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private boolean heroTimeCountAndNewCycleJudge() {
        heroCycleTime += timeInterval;
        if (heroCycleTime >= heroCycleDuration && heroCycleTime - timeInterval < heroCycleTime) {
            // 跨越到新的周期
            heroCycleTime %= heroCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean enemyTimeCountAndNewCycleJudge();

    /**
     * 敌机的生成方法，在不同难度下有不同实现
     */
    protected abstract void newEnemy();
    private void heroAircraftShootAction(){
        heroBullets.addAll(heroAircraft.shoot());
    }

    /**
     * 敌机的射击方法，在不同难度下有不同实现
     */
    protected abstract void enemyShootAction();

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
                    if (StartMenu.needMusic) {
                        new MusicThread("src/videos/bullet_hit.wav").start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
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
                baseProp.active(heroAircraft,StartMenu.needMusic);
                baseProp.vanish();
            }
        }
    }

    /**
     * 控制背景音乐与boss音乐播放
     */
    private void musicCheck() {
        bossVaild = false;
        for (Enemy enemyAircraft : enemyAircrafts){
            if (enemyAircraft instanceof BossEnemy) {
                bossVaild = true;
                bossVailded = true;
                break;
            }
        }
        //boss音乐
        if (bossVaild) {
            // 存在boss机，停止播放背景音乐
            bgm.stop();
            // 播放boss机背景音乐
            if (bossBgm == null || !bossBgm.isAlive()) {
                bossBgm = new MusicThread("src/videos/Trouble_Within.wav");
                bossBgm.start();
            }
        }
        else {
            if (bossVailded) {
                bossBgm.stop();
            }
            // 循环播放背景音乐
            if ((bgm == null || !bgm.isAlive()) && !bossVaild) {
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
    /**
     * 游戏结束检查及处理
     */
    private void overCheckAndProcess() {
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            executorService.shutdown();
            // 停止播放背景音乐和boss音乐,并播放结束音乐
            if (StartMenu.needMusic) {
                bgm.stop();
                if (bossVailded) {
                    bossBgm.stop();
                }
                new MusicThread("src/videos/game_over.wav").start();
            }
            // 保存游戏记录并展示排行榜
            scoreDao = new ScoreDaoImpl();
            Main.cardPanel.add(new Input(scoreDao,score,pathName).mainPanel);
            Main.cardLayout.last(Main.cardPanel);

            System.out.println("Game Over!");
        }
    }
    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
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
        g.drawString("SCORE:" + score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
