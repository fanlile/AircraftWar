package edu.hitsz.application.Game;

import edu.hitsz.aircraft.Enemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

import java.util.List;
import java.util.Random;

/**
 * @author fll
 */
public class EasyGame extends Game {
    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber = 3;

    /**
     * 周期（ms）
     * 指示敌机子弹的发射
     */
    private int enemyCycleDuration = 1600;
    @Override
    protected void newEnemy() {
        Random r1 = new Random();
        if (enemyAircrafts.size() < enemyMaxNumber) {
            // 生成随机数
            int i1 = r1.nextInt(6);
            // 生成精英敌机
            if(i1 == 4) {
                enemyFactory = new EliteEnemyFactory();
            }
            // 生成普通敌机
            else {
                enemyFactory = new MobEnemyFactory();
            }
            enemy = enemyFactory.createEnemy();
            enemyAircrafts.add(enemy);
            //将新生成的敌机加入爆炸道具的观察者列表中（如果有爆炸道具）
            for(BaseProp prop :baseProps){
                if(prop instanceof Prop_Bomb){
                    ((Prop_Bomb) prop).addSubscriber(enemy);
                }
            }
        }
    }
    @Override
    protected boolean enemyTimeCountAndNewCycleJudge() {
        enemyCycleTime += timeInterval;
        if (enemyCycleTime >= enemyCycleDuration && enemyCycleTime - timeInterval < enemyCycleTime) {
            // 跨越到新的周期
            enemyCycleTime %= enemyCycleDuration;
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected boolean difficultyTimeCountAndNewCycleJudge() {
        return false;
    }
    @Override
    protected void enemyShootAction() {
        for(Enemy enemyAircraft : enemyAircrafts) {
            List<BaseBullet> bullets= enemyAircraft.shoot();
            enemyBullets.addAll(bullets);
            for(BaseProp prop :baseProps){
                if(prop instanceof Prop_Bomb){ ((Prop_Bomb) prop).addSubscriber(bullets); }
            }
        }
    }
}