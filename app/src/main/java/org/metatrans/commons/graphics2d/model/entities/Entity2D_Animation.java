package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;

import java.util.ArrayList;


public abstract class Entity2D_Animation extends Entity2D_Moving {


    private static final long serialVersionUID = -1177114995621289333L;


    private static final long LIFETIME_IN_MS = 1111;


    private int fixed_angle;
    private int[] bitmap_ids;
    private boolean[] bitmap_shown_at_lest_once;
    private long timestamp_created;

    private int bitmap_id_backup;


    public Entity2D_Animation(World _world, RectF _envelop,
                              int[] _bitmap_ids,
                              int rotation_in_degrees) {

        super(_world, _envelop, SUBTYPE_MOVING_CHALLENGER, new ArrayList<>(), new ArrayList<>(),
                _bitmap_ids[0], rotation_in_degrees);

        bitmap_ids = new int[_bitmap_ids.length + 1];

        bitmap_ids[0] = -1;
        for (int i = 0; i < _bitmap_ids.length; i++) {

            bitmap_ids[i + 1] = _bitmap_ids[i];
        }

        bitmap_shown_at_lest_once = new boolean[bitmap_ids.length];

        timestamp_created = System.currentTimeMillis();

        fixed_angle = rotation_in_degrees;
    }


    protected abstract Bitmap scaleBitmap(int BITMAP_ID, Bitmap bitmap);


    @Override
    protected void rotate() {

        cur_bitmap_rotation_degrees = fixed_angle;
    }


    @Override
    public Bitmap getBitmap() {

        int bitmap_index = getBitmap_index();

        if (bitmap_index == -1) {

            return null;
        }

        bitmap_id = bitmap_ids[bitmap_index];

        if (bitmap_id == -1) {

            bitmap_shown_at_lest_once[0] = true;

            return null;
        }

        if (bitmap_id != bitmap_id_backup) {

            bitmap_id_backup = bitmap_id;

            bitmap_org = getWorld().getBitmapCache().get(bitmap_id);
            bitmap_org = scaleBitmap(bitmap_id, bitmap_org);

            bitmap_shown_at_lest_once[bitmap_index] = true;

            setRotationStep(getRotationDegrees());
        }


        return super.getBitmap();
    }


    @Override
    public void nextMoment(float takts) {

        int bitmap_index = getBitmap_index();

        if (bitmap_index == -1) {

            removed = true;

            getWorld().removeMovingEntity(this);

        } else {

            super.nextMoment(takts);
        }
    }


    private int getBitmap_index() {

        long until_now = System.currentTimeMillis() - timestamp_created;

        int bitmap_index_by_time = (int) (until_now / (LIFETIME_IN_MS / bitmap_ids.length));

        bitmap_index_by_time = Math.min(bitmap_ids.length, bitmap_index_by_time);

        int bitmap_index_by_showing = 0;
        for (int i = 0; i < bitmap_shown_at_lest_once.length; i++) {

            if (!bitmap_shown_at_lest_once[i]) {

                break;
            }

            bitmap_index_by_showing++;
        }

        int bitmap_index = Math.min(bitmap_index_by_showing, bitmap_index_by_time);

        if (bitmap_index >= bitmap_ids.length) {

            return -1;
        }

        return bitmap_index;
    }
}
