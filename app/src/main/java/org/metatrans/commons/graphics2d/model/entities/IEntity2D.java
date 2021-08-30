package org.metatrans.commons.graphics2d.model.entities;


import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.RectF;


public interface IEntity2D extends Serializable {
	
	
	public static final int TYPE_MOVING					= 1;
	public static final int TYPE_GROUND		 			= 2;
	public static final int TYPE_COLLECTIBLE			= 3;
	public static final int TYPE_SPECIAL				= 4;
	
	
	public static final int SUBTYPE_MOVING_PLAYER		= 1;
	public static final int SUBTYPE_MOVING_CHALLENGER	= 2;
	public static final int SUBTYPE_MOVING_BULLET		= 3;
	
	public static final int SUBTYPE_GROUND_WALL			= 1;
	public static final int SUBTYPE_GROUND_EMPTY		= 2;
	
	public static final int SUBTYPE_COLLECTIBLE_KEY 	= 1;
	public static final int SUBTYPE_COLLECTIBLE_BULLET 	= 2;
	public static final int SUBTYPE_COLLECTIBLE_STAR 	= 3;
	
	public static final int SUBTYPE_SPECIAL_EXIT		= 1;
	
	
	public int getType();
	public int getSubType();
	public RectF getEvelop();
	public void draw(Canvas canvas);
	public boolean isSolid();
}
