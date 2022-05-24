package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.ui.utils.BitmapUtils;


public abstract class Entity2D_Base implements IEntity2D {
	
	
	private static final long serialVersionUID = -2083232671109801129L;
	
	
	private int subtype;
	
	private float evelop_left;
	private float evelop_top;
	private float evelop_right;
	private float evelop_bottom;
	
	private transient RectF envelop;
	private transient Paint paint;

	protected IWorld world;

	
	public Entity2D_Base(IWorld _world, RectF _evelop, int _subtype) {

		world = _world;

		envelop = _evelop;
		
		evelop_left = envelop.left;
		evelop_top = envelop.top;
		evelop_right = envelop.right;
		evelop_bottom = envelop.bottom;
		
		subtype = _subtype;

	}
	
	
	public abstract int getType();
	
	
	public int getBackgroundColour() {
		return -1;
	}
	
	
	public abstract Bitmap getBitmap();
	
	
	public int getBitmapTransparency() {
		return 255;
	}
	
	
	public int getSubType() {
		return subtype;
	}
	
	
	public RectF getEnvelop() {

		if (envelop == null) {

			envelop = new RectF(evelop_left, evelop_top, evelop_right, evelop_bottom);
		}

		return envelop;
	}


	public RectF getEnvelop_ForDraw() {

		return getEnvelop();
	}


	protected Bitmap scaleBitmapToRectangleForDrawing(Bitmap org) {

		return BitmapUtils.createScaledBitmap(org,
				(int) getEnvelop_ForDraw().width(),
				(int) getEnvelop_ForDraw().height(),
				false
		);
	}


	public float getX() {
		return getEnvelop().left;
	}
	
	
	public float getY() {
		return getEnvelop().top;
	}
	
	
	public void draw(Canvas c) {
		
		
		int b_color = getBackgroundColour();
		if (b_color != -1) {
			getPaint().setColor(b_color);
			getPaint().setAlpha(255);
			c.drawRect(getEnvelop_ForDraw(), getPaint());
		}

		
		if (getBitmap() != null) {
			getPaint().setAlpha(getBitmapTransparency());
			c.drawBitmap(getBitmap(), null, getEnvelop_ForDraw(), null);
			//c.drawBitmap(getBitmap(), getEnvelop_ForDraw().left, getEnvelop_ForDraw().top, getPaint());
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
