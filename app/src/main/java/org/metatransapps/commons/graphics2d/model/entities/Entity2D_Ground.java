package org.metatransapps.commons.graphics2d.model.entities;


import android.graphics.RectF;


public class Entity2D_Ground extends Entity2D_Base {
	
	
	private static final long serialVersionUID = -1773380701450381753L;
	
	
	public Entity2D_Ground(RectF _evelop, int _subtype) {
		super(_evelop, _subtype);
	}
	
	
	@Override
	public int getType() {
		return TYPE_GROUND;
	}
}
