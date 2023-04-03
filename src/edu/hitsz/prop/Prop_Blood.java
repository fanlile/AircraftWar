package edu.hitsz.prop;

import edu.hitsz.application.Main;
import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;

public class Prop_Blood extends BaseProp {
    protected int blood;
    public Prop_Blood(int locationX, int locationY, int speedX, int speedY, int blood) {
        super(locationX, locationY, speedX, speedY);
        this.blood = blood;
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(blood);
    }
}