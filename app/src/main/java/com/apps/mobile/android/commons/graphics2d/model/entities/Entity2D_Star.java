package com.apps.mobile.android.commons.graphics2d.model.entities;


import android.graphics.RectF;


public class Entity2D_Star extends Entity2D_Collectible {
	
	
	private static final long serialVersionUID = -9053392210797938995L;
	
	
	public Entity2D_Star(RectF _evelop) {
		super(_evelop, SUBTYPE_COLLECTIBLE_STAR);
	}
}
