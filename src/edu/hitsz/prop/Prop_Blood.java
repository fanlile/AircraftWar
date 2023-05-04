package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.MusicThread;

public class Prop_Blood extends BaseProp {
    protected int blood;
    public Prop_Blood(int locationX, int locationY, int speedX, int speedY, int blood) {
        super(locationX, locationY, speedX, speedY);
        this.blood = blood;
    }
    @Override
    public void active(HeroAircraft heroAircraft,boolean needMusic) {
        if (needMusic) {
           new MusicThread("src/videos/get_supply.wav").start();
        }
        heroAircraft.increaseHp(blood);
    }
}