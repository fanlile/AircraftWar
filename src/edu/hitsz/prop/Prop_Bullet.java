package edu.hitsz.prop;

import edu.hitsz.application.MusicThread;
import edu.hitsz.strategy.DirectShoot;
import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.aircraft.HeroAircraft;

public class Prop_Bullet extends BaseProp {
    public Prop_Bullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft,boolean needMusic) {
        if (needMusic) {
            new MusicThread("src/videos/get_supply.wav").start();
        }
        heroAircraft.setShootNum(3);
        heroAircraft.setShootStrategy(new ScatterShoot());
        Runnable r = () -> {
            try {
                Thread.sleep(5000); // 等待5秒钟
                heroAircraft.setShootNum(1);
                heroAircraft.setShootStrategy(new DirectShoot());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 启动线程
        new Thread(r, "线程 1").start();
    }
}

