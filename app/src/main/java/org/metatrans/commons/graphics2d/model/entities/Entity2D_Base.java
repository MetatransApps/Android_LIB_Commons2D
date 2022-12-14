package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.graphics2d.model.World;


public abstract class Entity2D_Base implements IEntity2D {
	
	
	private static final long serialVersionUID = -2083232671109801129L;


	private int subtype;
	
	private float envelop_left;
	private float envelop_top;
	private float envelop_right;
	private float envelop_bottom;
	
	private transient RectF envelop;
	protected transient RectF envelop_ForDraw;
	private transient Paint paint;

	protected IWorld world;

	
	public Entity2D_Base(IWorld _world, RectF _envelop, int _subtype) {

		world = _world;

		envelop = _envelop;

		if (envelop != null) {

			envelop_left = envelop.left;
			envelop_top = envelop.top;
			envelop_right = envelop.right;
			envelop_bottom = envelop.bottom;
		}
		
		subtype = _subtype;

	}


	protected World getWorld() {

		return (World) world;
	}


	public abstract int getType();


	public int getSubType() {
		return subtype;
	}


	public boolean isSolid() {
		return getType() == TYPE_GROUND && getSubType() == SUBTYPE_GROUND_WALL;
	}


	public float getX() {
		return getEnvelop().left;
	}


	public float getY() {
		return getEnvelop().top;
	}


	public int getBackgroundColour() {
		return -1;
	}
	
	
	public abstract Bitmap getBitmap();

	
	public RectF getEnvelop() {

		if (envelop == null) {

			envelop = new RectF(envelop_left, envelop_top, envelop_right, envelop_bottom);
		}

		return envelop;
	}


	public RectF getEnvelop_ForDraw() {

		RectF rect_org = getEnvelop();

		return rect_org;
	}


	public void draw(Canvas c) {
		
		
		int b_color = getBackgroundColour();

		if (b_color != -1) {

			getPaint().setColor(b_color);
			getPaint().setAlpha(255);
			c.drawRect(getEnvelop_ForDraw(), getPaint());
		}

		Bitmap bitmap = getBitmap();

		if (bitmap != null) {

			c.drawBitmap(bitmap, null, getEnvelop_ForDraw(), null);
		}
	}


	protected Paint getPaint() {
		
		if (paint == null) {
			paint = new Paint();
		}
		
		return paint;
	}
}
