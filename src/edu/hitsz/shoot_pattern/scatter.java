package edu.hitsz.shoot_pattern;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;
public class scatter extends AbstractAircraft {
    /**
     * 散射子弹的数量
     */
    private int shootNum = 3;

    @Override
    public List<BaseBullet> shoot(int LocationX,int LocationY,int speedX,int speedY,int direction,int power){
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        speedX = 0;
        speedY = this.getSpeedY() + direction*15;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }
}
