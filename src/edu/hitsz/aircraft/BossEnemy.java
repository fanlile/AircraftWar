package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;
import edu.hitsz.factory.BasePropFactory;
import edu.hitsz.factory.Prop_BloodFactory;
import edu.hitsz.factory.Prop_BombFactory;
import edu.hitsz.factory.Prop_BulletFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BossEnemy extends Enemy{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 3;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;
    private BasePropFactory basepropFactory;
    private BaseProp baseProp;
    /**
     * @param locationX 精英敌机位置x坐标
     * @param locationY 精英敌机位置y坐标
     * @param speedX 精英敌机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 精英敌机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    生命值
     */
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }
    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y+95, speedX+i-1, speedY, power);
            res.add(bullet);
        }
        return res;
    }

    @Override
    public int increaseScore(int score) {
        //击落Boss敌机加200分
        return score+200;
    }
    @Override
    public List<BaseProp> drop_prop() {
        List<BaseProp> props = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseProp prop;
        Random r = new Random();
        //q为道具的横向偏移量
        int q = -45;
        for(int i = 0; i < 3 ;i++) {
            int m = r.nextInt(3);
            if(m == 0){
                basepropFactory = new Prop_BloodFactory();
                baseProp = basepropFactory.createBaseProp(x+q,y);
                props.add(baseProp);
            }
            else if(m == 1){
                basepropFactory = new Prop_BombFactory();
                baseProp = basepropFactory.createBaseProp(x+q,y);
                props.add(baseProp);
            }
            else {
                basepropFactory = new Prop_BulletFactory();
                baseProp = basepropFactory.createBaseProp(x+q,y);
                props.add(baseProp);
            }
            q += 45;
        }
        return props;
    }
}
