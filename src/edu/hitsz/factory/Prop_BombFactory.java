package edu.hitsz.factory;

import edu.hitsz.aircraft.Enemy;
import edu.hitsz.application.Game.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

/**
 * @author fll
 */
public class Prop_BombFactory implements BasePropFactory {
    @Override
    public BaseProp createBaseProp(int x, int y){
        Prop_Bomb prop = new Prop_Bomb(x,y,0,6);
        for(Enemy enemy:Game.enemyAircrafts){
            prop.addSubscriber(enemy);
        }
        for(BaseBullet bullet :Game.enemyBullets){
            prop.addSubscriber(bullet);
        }
        return prop;
    }
}
