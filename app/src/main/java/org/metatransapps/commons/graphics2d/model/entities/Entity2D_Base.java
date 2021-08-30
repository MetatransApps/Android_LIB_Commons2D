package org.metatransapps.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public abstract class Entity2D_Base implements IEntity2D {
	
	
	private static final long serialVersionUID = -2083232671109801129L;
	
	
	private int subtype;
	
	private float evelop_left;
	private float evelop_top;
	private float evelop_right;
	private float evelop_bottom;
	
	private transient RectF evelop;
	private transient Paint paint;
	
	
	public Entity2D_Base(RectF _evelop, int _subtype) {
		
		evelop = _evelop;
		
		evelop_left = evelop.left;
		evelop_top = evelop.top;
		evelop_right = evelop.right;
		evelop_bottom = evelop.bottom;
		
		subtype = _subtype;

	}
	
	
	public abstract int getType();
	
	
	public int getBackgroundColour() {
		return -1;
	}
	
	
	public Bitmap getBitmap() {
		return null;
	}
	
	
	public int getBitmapTransparency() {
		return 255;
	}
	
	
	public int getSubType() {
		return subtype;
	}
	
	
	public RectF getEvelop() {
		if (evelop == null) {
			evelop = new RectF(evelop_left, evelop_top, evelop_right, evelop_bottom);
		}
		return evelop;
	}
	
	
	public float getX() {
		return getEvelop().left;
	}
	
	
	public float getY() {
		return getEvelop().top;
	}
	
	
	public void draw(Canvas c) {
		
		
		int b_color = getBackgroundColour();
		if (b_color != -1) {
			getPaint().setColor(b_color);
			getPaint().setAlpha(255);
			c.drawRect(getEvelop(), getPaint());
		}

		
		if (getBitmap() != null) {
			getPaint().setAlpha(getBitmapTransparency());
			c.drawBitmap(getBitmap(), null, getEvelop(), getPaint());
		}
	}


	public boolean isSolid() {
		return getType() == TYPE_GROUND && getSubType() == SUBTYPE_GROUND_WALL;
	}


	protected Paint getPaint() {
		
		if (paint == null) {
			paint = new Paint();
		}
		
		return paint;
	}
}
