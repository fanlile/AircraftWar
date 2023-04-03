package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;

public interface BasePropFactory {
    BaseProp createBaseProp(int x, int y);
}
