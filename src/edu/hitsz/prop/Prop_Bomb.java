package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicThread;

public class Prop_Bomb extends BaseProp {
    public Prop_Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft,boolean needMusic) {
        if (needMusic) {
            new MusicThread("src/videos/bomb_explosion.wav").start();
        }
        System.out.println("BombSupply active!");
    }
}

