package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

public class Prop_BulletFactory implements BasePropFactory{
    @Override
    public BaseProp createBaseProp(int x, int y){
        return new Prop_Bullet(
                x,
                y,
                0,
                6
        );
    }
}
