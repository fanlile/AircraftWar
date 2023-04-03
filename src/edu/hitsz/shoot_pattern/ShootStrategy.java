package edu.hitsz.shoot_pattern;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootStrategy {
    List<BaseBullet> shoot(int LocationX,int LocationY,int speedX,int speedY,int direction,int power);
}
