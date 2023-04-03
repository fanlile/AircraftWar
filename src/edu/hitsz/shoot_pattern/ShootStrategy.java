package edu.hitsz.shoot_pattern;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootStrategy {
    /**
     *射击策略
     * @param LocationX x轴坐标
     * @param LocationY y轴坐标
     * @param speedY y轴方向速度
     * @param direction 飞行方向
     * @param power 子弹伤害
     * @param shootNum 每次发射子弹数量
     * @return 子弹集合
     */
    List<BaseBullet> shoot(int LocationX,int LocationY,int speedY,int direction,int power,int shootNum);
}
