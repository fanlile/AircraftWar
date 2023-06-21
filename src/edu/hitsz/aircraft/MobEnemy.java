package edu.hitsz.aircraft;

import edu.hitsz.application.Card.Main;
import edu.hitsz.application.Game.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends Enemy{

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public int increaseScore(int score) {
        //普通敌机被击落加10分
        return score+10;
    }
    @Override
    public List<BaseProp> drop_prop() {
        //普通机不掉落道具
        return new LinkedList<>();
    }
    @Override
    public void bombActive(){
        // 避免无效加分
        if(isValid){
            decreaseHp(hp);
            Game.score += 10;
        }
    }
}




