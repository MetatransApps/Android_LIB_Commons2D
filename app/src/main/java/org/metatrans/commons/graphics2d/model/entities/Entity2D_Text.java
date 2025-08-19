package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.World;

import java.util.ArrayList;


public class Entity2D_Text extends Entity2D_Moving {


    private String text;
    private int color;


    public Entity2D_Text(World _world, RectF _envelop, String text_initial, int color_initial) {

        super(_world, _envelop, SUBTYPE_MOVING_TEXT, new ArrayList<>(), new ArrayList<>(), -1, 0);

        text = text_initial;
        color = color_initial;
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

        getPaint().setAntiAlias(true);
        getPaint().setTextAlign(Paint.Align.LEFT);        // we'll handle centering manually
        getPaint().setColor(color);

        // measure full text width including spaces
        float probeSize = 100f;
        getPaint().setTextSize(probeSize);

        float w = getPaint().measureText(text);                 // keeps spaces
        Paint.FontMetrics fm = getPaint().getFontMetrics();
        float h = fm.descent - fm.ascent;

        float sx = getEnvelop().width() / w;
        float sy = getEnvelop().height() / h;
        float scale = Math.min(sx, sy) * 0.98f;

        float finalSize = probeSize * scale;
        getPaint().setTextSize(finalSize);

        w = getPaint().measureText(text);
        fm = getPaint().getFontMetrics();

        float x = getEnvelop().centerX() - w / 2f;
        float baseline = getEnvelop().centerY() - (fm.ascent + fm.descent) / 2f;

        c.drawText(text, x, baseline, getPaint());
    }
}
