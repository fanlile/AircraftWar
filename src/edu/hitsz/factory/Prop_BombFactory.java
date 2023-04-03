package edu.hitsz.factory;

import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.Prop_Bomb;

public class Prop_BombFactory implements BasePropFactory {
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
