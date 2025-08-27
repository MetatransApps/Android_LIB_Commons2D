package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;
import org.metatrans.commons.graphics2d.utils.DrawUtils;


public abstract class Entity2D_Frame extends Entity2D_Clickable {


    private static final long serialVersionUID  = -1291486995621289565L;


    int last_clicked_x = -1;
    int last_clicked_y = -1;

    int envelop_close_left;
    int envelop_close_top;
    int envelop_close_right;
    int envelop_close_bottom;


    public Entity2D_Frame(World _world) {

        super(_world, null, -1);

        getWorld().setPopupFrame(this);
    }


    @Override
    public void doAction(int x, int y) {

        if (x > envelop_close_left && x < envelop_close_right
            && y > envelop_close_top && y < envelop_close_bottom) {

            getWorld().removeMovingEntity(this);
            getWorld().clearPopupFrame();
        }


        last_clicked_x = x;
        last_clicked_y = y;
    }


    @Override
    public void draw(Canvas c) {

        int rounding = 70;
        int border_margin = 15;

        int color_bg1 = Color.argb(255, 64,191,233);
        int color_bg2 = Color.argb(255, 29,103,167);
        int color_bg3 = Color.argb(255, 21,82,135);
        int color_red = Color.argb(255,188,15,77);

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


        int title_height = (int) (1 * (envelop_frame.bottom - envelop_frame.top) / 5);

        getPaint().setColor(color_bg1);

        c.drawRoundRect(
                new RectF(
                        envelop_frame.left,
                        envelop_frame.top,
                        envelop_frame.right,
                        envelop_frame.top + title_height),
                rounding,
                rounding,
                getPaint()
        );


        getPaint().setColor(color_bg3);

        RectF header = new RectF(
                envelop_frame.left + border_margin,
                envelop_frame.top + border_margin,
                envelop_frame.right - border_margin,
                envelop_frame.top + title_height - border_margin
        );

        c.drawRoundRect(
                header,
                rounding,
                rounding,
                getPaint()
        );


        getPaint().setColor(color_bg1);

        c.drawCircle(envelop_frame.right - title_height / 2, envelop_frame.top + title_height / 2, title_height / 2, getPaint());


        getPaint().setColor(color_bg3);

        c.drawCircle(envelop_frame.right - title_height / 2, envelop_frame.top + title_height / 2, title_height / 2 - border_margin, getPaint());


        // Temporarily switch paint to stroke
        android.graphics.Paint.Style oldStyle = getPaint().getStyle();
        float oldStroke = getPaint().getStrokeWidth();
        android.graphics.Paint.Cap oldCap = getPaint().getStrokeCap();

        getPaint().setStyle(android.graphics.Paint.Style.STROKE);
        getPaint().setStrokeWidth(border_margin);
        getPaint().setStrokeCap(android.graphics.Paint.Cap.ROUND);

        getPaint().setColor(color_red);

        // Draw red "X" inside the small circle (close button)
        float cx = envelop_frame.right - title_height / 2f;
        float cy = envelop_frame.top + title_height / 2f;
        float r  = title_height / 2f - border_margin;
        float a  = r * 0.55f; // keep a bit of padding from the circle edge

        // Two lines forming an X
        c.drawLine(cx - a, cy - a, cx + a, cy + a, getPaint());
        c.drawLine(cx - a, cy + a, cx + a, cy - a, getPaint());

        envelop_close_left = (int) (cx - 2 * a);
        envelop_close_right = (int) (cx + 2 * a);
        envelop_close_top = (int) (cy - 2 * a);
        envelop_close_bottom = (int) (cy + 2 * a);

        // Restore paint
        getPaint().setStyle(oldStyle);
        getPaint().setStrokeWidth(oldStroke);
        getPaint().setStrokeCap(oldCap);


        RectF title = new RectF((float) (header.left + 0.25 * title_height), header.top, (float) (header.right - 1.25 * title_height), header.bottom);

        DrawUtils.drawTextInRectangle(c, getPaint(), title, getTitle(), Color.WHITE);


        if (last_clicked_x != -1 && last_clicked_y != -1) {

            getPaint().setColor(Color.argb(200, 0, 0, 0));
            //getPaint().setStyle(Paint.Style.STROKE);
            //getPaint().setStrokeWidth(9);

            c.drawCircle(last_clicked_x,
                    last_clicked_y,
                    50,
                    getPaint());

        }
    }


    @Override
    public RectF getEnvelop() {

        return getEnvelop_ForDraw();
    }


    @Override
    public RectF getEnvelop_ForDraw() {

        RectF envelop_camera = getWorld().getCamera();

        int frame_height = (int) (4 * (envelop_camera.bottom - envelop_camera.top) / 5);
        int frame_width = (int) (2 * (envelop_camera.right - envelop_camera.left) / 5);

        int cell_size = (int) getWorld().getCellSize();
        int bar_height = 2 * cell_size;

        float left = envelop_camera.left + (envelop_camera.right - envelop_camera.left) / 2 - frame_width / 2;
        float top = envelop_camera.top + bar_height / 2 + (envelop_camera.bottom - envelop_camera.top) / 2 - frame_height / 2;
        float right = envelop_camera.left + (envelop_camera.right - envelop_camera.left) / 2 + frame_width / 2;
        float bottom = envelop_camera.top + bar_height / 2 + (envelop_camera.bottom - envelop_camera.top) / 2 + frame_height / 2;

        RectF envelop = new RectF(left, top, right, bottom);

        return envelop;
    }


    public abstract String getTitle();
}
