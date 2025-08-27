package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;

import java.util.ArrayList;


public abstract class Entity2D_Clickable extends Entity2D_Moving {


    private static final long serialVersionUID  = -7011354995621289565L;


    public Entity2D_Clickable(World _world, RectF _envelop, int _bitmap_id) {

        super(_world, _envelop, SUBTYPE_MOVING_CLICKABLE,
                new ArrayList<>(), new ArrayList<>(), _bitmap_id, 0);

        setRotationStep(0);
        setSpeed(0, 0);
    }


    public abstract void doAction(int x, int y);
}
