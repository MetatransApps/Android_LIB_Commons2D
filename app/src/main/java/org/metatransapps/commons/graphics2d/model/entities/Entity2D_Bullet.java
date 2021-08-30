package org.metatransapps.commons.graphics2d.model.entities;


import java.util.List;

import org.metatransapps.commons.graphics2d.model.World;

import android.graphics.RectF;


public class Entity2D_Bullet extends Entity2D_Moving {
	
	
	private static final long serialVersionUID = -6909037206508107744L;


	public Entity2D_Bullet(World _world, RectF _evelop, List<? extends IEntity2D> _blockerEntities) {
		
		this(_world, _evelop, _blockerEntities, null);
	}
	
	
	public Entity2D_Bullet(World _world, RectF _evelop, List<? extends IEntity2D> _blockerEntities, List<? extends IEntity2D> _killerEntities) {
		
		super(_world, _evelop, SUBTYPE_MOVING_BULLET, _blockerEntities, _killerEntities);
	}
	
	
	@Override
	public void nextMoment(float takts) {
		
		super.nextMoment(takts);
		
		//System.out .println("Entity2D_Bullet: getDx()=" + getDx() + ", getDy()=" + getDy());
	}
	
	
	@Override
	protected void groundContact_X() {
		//System.out.println(this + " groundContact_X");
		
		getWorld().removeMovingEntity(this);
	}
	
	
	@Override
	protected void groundContact_Y() {
		//System.out.println(this + " groundContact_Y");
		
		getWorld().removeMovingEntity(this);
	}
}
