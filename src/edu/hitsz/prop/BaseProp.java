package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Card.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 *道具类
 * 加血道具、火力道具、炸弹道具
 * @author fanlile
 */
public abstract class BaseProp extends AbstractFlyingObject {
    public BaseProp(int locationX, int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    /**
     * 道具生效方法
     * @param heroAircraft
     */
    public abstract void active(HeroAircraft heroAircraft,boolean needMusic);

}

