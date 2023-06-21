package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fll
 */
public class Prop_Bomb extends BaseProp {
    private List<AbstractFlyingObject> subscribersList = new ArrayList<>();
    public Prop_Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    public void addSubscriber(AbstractFlyingObject subscriber){
        subscribersList.add(subscriber);
    }
    public void addSubscriber(List<BaseBullet> bullets) { subscribersList.addAll(bullets); }
    public void removeSubscriber(AbstractFlyingObject subscriber){
        subscribersList.remove(subscriber);
    }
    @Override
    public void active(HeroAircraft heroAircraft,boolean needMusic) {
        if (needMusic) {
            new MusicThread("src/videos/bomb_explosion.wav").start();
        }
        // 生效后通知所有观察者
        for(AbstractFlyingObject subscriber : subscribersList){
            subscriber.bombActive();
        }
    }
}

