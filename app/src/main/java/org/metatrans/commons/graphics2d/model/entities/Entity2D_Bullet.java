package org.metatrans.commons.graphics2d.model.entities;


import java.util.List;

import org.metatrans.commons.graphics2d.model.World;

import android.graphics.RectF;


public abstract class Entity2D_Bullet extends Entity2D_Moving {
	
	
	private static final long serialVersionUID = -6909037206508107744L;

	
	public Entity2D_Bullet(
			World _world,
			RectF _envelop,
			List<? extends IEntity2D> _blockerEntities,
			List<? extends IEntity2D> _killerEntities,
			int bitmap_id,
			int rotation_angle_in_degrees) {
		
		super(_world, _envelop, SUBTYPE_MOVING_BULLET, _blockerEntities, _killerEntities,
				bitmap_id,
				rotation_angle_in_degrees);
	}
	
	
	@Override
	protected void groundContact_X() {

		System.out.println(this + " groundContact_X");
		
		getWorld().removeMovingEntity(this);
	}
	
	
	@Override
	protected void groundContact_Y() {

		System.out.println(this + " groundContact_Y");
		
		getWorld().removeMovingEntity(this);
	}
}
