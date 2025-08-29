package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;


public abstract class Entity2D_Frame extends Entity2D_Clickable {


    private static final long serialVersionUID  = -1291486995621289565L;


    protected int envelop_custom_left;
    protected int envelop_custom_top;
    protected int envelop_custom_right;
    protected int envelop_custom_bottom;

    protected int frame_height;

    protected int frame_width;

    //For testing last_clicked_x and last_clicked_y
    private int last_clicked_x = -1;
    private int last_clicked_y = -1;


    public Entity2D_Frame(World _world, int _frame_height, int _frame_width) {

        super(_world, null, -1);

        frame_height = _frame_height;
        frame_width = _frame_width;
    }


    @Override
    public void doAction(int x, int y) {

        if (x > getCustomEnvelop().left && x < getCustomEnvelop().right
                && y > getCustomEnvelop().top && y < getCustomEnvelop().bottom) {

            getWorld().removeMovingEntity(this);
        }

        last_clicked_x = (int) (getWorld().getCamera().left + x);
        last_clicked_y = (int) (getWorld().getCamera().top + y);
    }


    protected int getRounding() {

        return 100;
    }


    protected int getBorderMargin() {

        return 15;
    }


    @Override
    public void draw(Canvas c) {


        int rounding = getRounding();
        int border_margin = getBorderMargin();

        int color_bg1 = Color.argb(255, 185,28,84);
        int color_bg2 = Color.argb(255, 171,232,246);


        RectF envelop_frame = getEnvelop_ForDraw();

        getPaint().setColor(color_bg1);

        c.drawRoundRect(envelop_frame, rounding, rounding, getPaint());


        getPaint().setColor(color_bg2);

        c.drawRoundRect(
                new RectF(
                        envelop_frame.left + border_margin,
                        envelop_frame.top + border_margin,
                        envelop_frame.right - border_margin,
                        envelop_frame.bottom - border_margin),
                rounding,
                rounding,
                getPaint()
        );


        envelop_custom_left = (int) (envelop_frame.left + getRounding());
        envelop_custom_top = (int) (envelop_frame.top + getRounding());
        envelop_custom_right = (int) (envelop_frame.right - getRounding());
        envelop_custom_bottom = (int) (envelop_frame.bottom - getRounding());


        /*if (last_clicked_x != -1 && last_clicked_y != -1) {

            getPaint().setColor(Color.argb(200, 0, 0, 0));

            c.drawCircle(last_clicked_x, last_clicked_y, 50, getPaint());
        }*/
    }


    @Override
    public RectF getEnvelop() {

        return getEnvelop_ForDraw();
    }


    @Override
    public RectF getEnvelop_ForDraw() {

        RectF envelop_camera = getWorld().getCamera();

        //int frame_width = (int) (2 * (envelop_camera.right - envelop_camera.left) / 5);

        int cell_size = (int) getWorld().getCellSize();
        int bar_height = 2 * cell_size;

        float left = envelop_camera.left + (envelop_camera.right - envelop_camera.left) / 2 - frame_width / 2;
        float top = envelop_camera.top + bar_height / 2 + (envelop_camera.bottom - envelop_camera.top) / 2 - frame_height / 2;
        float right = envelop_camera.left + (envelop_camera.right - envelop_camera.left) / 2 + frame_width / 2;
        float bottom = envelop_camera.top + bar_height / 2 + (envelop_camera.bottom - envelop_camera.top) / 2 + frame_height / 2;

        RectF envelop = new RectF(left, top, right, bottom);

        return envelop;
    }


    protected RectF getCustomEnvelop() {

        return new RectF(
                envelop_custom_left,
                envelop_custom_top,
                envelop_custom_right,
                envelop_custom_bottom
        );
    }
}
