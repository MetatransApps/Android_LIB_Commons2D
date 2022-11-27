package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.graphics2d.model.World;
import org.metatrans.commons.ui.utils.BitmapUtils;


public abstract class Entity2D_Base implements IEntity2D {
	
	
	private static final long serialVersionUID = -2083232671109801129L;


	public static float ENVELOP_DRAW_EXTENSION = 1.3f;
	public static float ENVELOP_DRAW_UPSIDE = 0.605f;
	public static float ENVELOP_DRAW_DOWNSIDE = 1 - ENVELOP_DRAW_UPSIDE;


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

		envelop_left = envelop.left;
		envelop_top = envelop.top;
		envelop_right = envelop.right;
		envelop_bottom = envelop.bottom;
		
		subtype = _subtype;

	}


	protected World getWorld() {

		return (World) world;
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

			envelop = new RectF(envelop_left, envelop_top, envelop_right, envelop_bottom);
		}

		return envelop;
	}

	protected boolean hasCustomEnvelopForDraw() {

		return false;
	}


	public RectF getEnvelop_ForDraw() {

		RectF rect_org = getEnvelop();

		if (!hasCustomEnvelopForDraw()) {

			return rect_org;
		}

		float width = 1 * (rect_org.right - rect_org.left);
		float height = 1 * (rect_org.bottom - rect_org.top);

		float shift_y = height * (ENVELOP_DRAW_DOWNSIDE - ENVELOP_DRAW_UPSIDE);


		if (envelop_ForDraw == null) {

			envelop_ForDraw = new RectF();
		}

		envelop_ForDraw.left = rect_org.left - (width * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.top = shift_y + rect_org.top - (height * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.right = rect_org.right + (width * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.bottom = shift_y + rect_org.bottom + (height * (ENVELOP_DRAW_EXTENSION - 1));

		return envelop_ForDraw;
	}


	public float getX() {
		return getEnvelop().left;
	}
	
	
	public float getY() {
		return getEnvelop().top;
	}


	protected Bitmap scaleBitmapToRectangleForDrawing(Bitmap org) {

		return BitmapUtils.createScaledBitmap(org,
				(int) getEnvelop_ForDraw().width(),
				(int) getEnvelop_ForDraw().height()
		);
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
