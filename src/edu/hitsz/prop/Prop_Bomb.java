package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

public class Prop_Bomb extends BaseProp {
    public Prop_Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }
}

