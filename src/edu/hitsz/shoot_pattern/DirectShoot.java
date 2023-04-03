package edu.hitsz.shoot_pattern;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class DirectShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(int LocationX,int LocationY,int speedY,int direction,int power,int shootNum) {
        List<BaseBullet> res = new LinkedList<>();
        int x = LocationX;
        int y = LocationY + direction*2;
        int speedX = 0;
        speedY = speedY + direction*3;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }
}
