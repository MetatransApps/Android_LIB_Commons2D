package org.metatrans.commons.graphics2d.model.entities;


import android.graphics.RectF;

import org.metatrans.commons.graphics2d.model.IWorld;


public abstract class Entity2D_Ground extends Entity2D_Base {
	
	
	private static final long serialVersionUID = -1773380701450381753L;

	private int index_x;
	private int index_y;

	
	public Entity2D_Ground(IWorld world, RectF _evelop, int _subtype, int _index_x, int _index_y) {

		super(world, _evelop, _subtype);

		index_x = _index_x;
		index_y = _index_y;
	}
	
	
	@Override
	public int getType() {
		return TYPE_GROUND;
	}


	public int getCellIndex_X() {

		return index_x;
	}


	public int getCellIndex_Y() {

		return index_y;
	}
}
