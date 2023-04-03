package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

public class Prop_BombFactory implements BasePropFactory{
    @Override
    public BaseProp createBaseProp(int x, int y){
        return new Prop_Bomb(
                x,
                y,
                0,
                6
        );
    }
}
