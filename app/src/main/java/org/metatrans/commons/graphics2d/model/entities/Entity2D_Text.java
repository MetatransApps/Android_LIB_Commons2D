package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import org.metatrans.commons.graphics2d.model.World;
import org.metatrans.commons.graphics2d.utils.DrawUtils;

import java.util.ArrayList;


public class Entity2D_Text extends Entity2D_Moving {


    protected String text;
    private int color;

    private int color_background;


    public Entity2D_Text(World _world, RectF _envelop, String text_initial, int color_initial, int _color_background) {

        super(_world, _envelop, SUBTYPE_MOVING_TEXT, new ArrayList<>(), new ArrayList<>(), -1, 0);

        text = text_initial;
        color = color_initial;
        color_background = _color_background;
    }


    public void setText(String _text) {

        text = _text;
    }


    public void setColor_Text(int _color) {

        color = _color;
    }


    @Override
    public void draw(Canvas c) {

        //super.draw(c);

        if (color_background != -1) {

            getPaint().setColor(color_background);
            c.drawRoundRect(getEnvelop(), 20, 20, getPaint());
        }

        DrawUtils.drawTextInRectangle(c, getPaint(), getEnvelop(), text, color);
    }
}
