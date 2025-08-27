package org.metatrans.commons.graphics2d.utils;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;


public class DrawUtils {


    public static void drawTextInRectangle(Canvas c, Paint p, RectF envelop, String text, int color) {

        p.setAntiAlias(true);
        p.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(color);

        // measure full text width including spaces
        float probeSize = 100f;
        p.setTextSize(probeSize);

        float w = p.measureText(text);                 // keeps spaces
        Paint.FontMetrics fm = p.getFontMetrics();
        float h = fm.descent - fm.ascent;

        float sx = envelop.width() / w;
        float sy = envelop.height() / h;
        float scale = Math.min(sx, sy) * 0.98f;

        float finalSize = probeSize * scale;
        p.setTextSize(finalSize);

        w = p.measureText(text);
        fm = p.getFontMetrics();

        float x = envelop.centerX(); // - w / 2f;
        float baseline = envelop.centerY() - (fm.ascent + fm.descent) / 2f;

        c.drawText(text, x, baseline, p);
    }
}
