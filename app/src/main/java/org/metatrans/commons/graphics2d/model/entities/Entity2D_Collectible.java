package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.RectF;


public class Entity2D_Collectible extends Entity2D_Base {
	
	
	private static final long serialVersionUID = 4227757660747502858L;


	public Entity2D_Collectible(RectF _evelop, int _subtype) {
		super(_evelop, _subtype);
	}
	
	
	@Override
	public int getType() {
		return TYPE_COLLECTIBLE;
	}
}
