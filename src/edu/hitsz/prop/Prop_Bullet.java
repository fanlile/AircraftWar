package edu.hitsz.prop;

import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.aircraft.HeroAircraft;

public class Prop_Bullet extends BaseProp {
    public Prop_Bullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void active(HeroAircraft heroAircraft) {
        heroAircraft.setShootNum(3);
        heroAircraft.setShootStrategy(new ScatterShoot());
    }
}

