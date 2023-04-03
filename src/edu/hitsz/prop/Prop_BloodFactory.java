package edu.hitsz.prop;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;
public class Prop_BloodFactory implements BasePropFactory{
    @Override
    public BaseProp createBaseProp(int x, int y){
        return new Prop_Blood(
                x,
                y,
                0,
                6,
                100
        );
    }
}
