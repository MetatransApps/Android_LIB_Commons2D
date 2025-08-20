package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;

import java.util.ArrayList;
import java.util.List;


public abstract class Entity2D_Animation_Repeat extends Entity2D_Moving {


    private static final long serialVersionUID = -1189214995621289333L;


    private static final long LIFETIME_IN_MS = 1111;


    private int[] bitmap_ids;
    private long timestamp_created;

    private int bitmap_id_backup;


    public Entity2D_Animation_Repeat(World _world, RectF _envelop, List<? extends IEntity2D> blockerEntities,
                                     int[] _bitmap_ids) {

        super(_world, _envelop, SUBTYPE_MOVING_CHALLENGER, blockerEntities, new ArrayList<>(),
                _bitmap_ids[0], 0);

        bitmap_ids = _bitmap_ids;

        timestamp_created = System.currentTimeMillis();
    }


    protected abstract Bitmap scaleBitmap(int BITMAP_ID, Bitmap bitmap);



    @Override
    public Bitmap getBitmap() {

        int bitmap_index = getBitmap_index();

        bitmap_id = bitmap_ids[bitmap_index];

        if (bitmap_id != bitmap_id_backup) {

            bitmap_id_backup = bitmap_id;

            bitmap_org = getWorld().getBitmapCache().get(bitmap_id);
            bitmap_org = scaleBitmap(bitmap_id, bitmap_org);

            setRotationStep(getRotationDegrees());
        }


        return super.getBitmap();
    }


    private int getBitmap_index() {

        long until_now = System.currentTimeMillis() - timestamp_created;

        int bitmap_index_by_time = (int) (until_now / (LIFETIME_IN_MS / bitmap_ids.length));

        bitmap_index_by_time = bitmap_index_by_time % bitmap_ids.length;

        if (bitmap_index_by_time >= bitmap_ids.length) {

            throw new IllegalStateException("bitmap_index_by_time=" + bitmap_index_by_time);
        }

        return bitmap_index_by_time;
    }
}
