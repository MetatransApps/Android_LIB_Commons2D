package org.metatrans.commons.graphics2d.model.entities;


import java.util.List;

import org.metatrans.commons.graphics2d.model.World;

import android.graphics.RectF;


public class Entity2D_Challenger extends Entity2D_Moving {
	
	
	private static final long serialVersionUID = -6135971939617929909L;

	
	private float new_dx;
	private float new_dy;
	
	
	public Entity2D_Challenger(World _world, RectF _evelop, List<Entity2D_Ground> _blockerEntities, List<? extends IEntity2D> _killerEntities) {
		super(_world, _evelop, SUBTYPE_MOVING_CHALLENGER, _blockerEntities, _killerEntities);
	}


	public void setSpeed(float _dx, float _dy) {
		
		super.setSpeed(_dx, _dy);
		
		new_dx = getDx();
		new_dy = getDy();
	}
	
	
	@Override
	public void nextMoment(float takts) {
		
		setSpeed(new_dx, new_dy);
		
		super.nextMoment(takts);
	}
	
	
	@Override
	protected void groundContact_X() {
		//System.out.println(this + " groundContact_X");
		/*if (Math.random() < 0.5) {
			new_dx = 0f;
			new_dy = SPEED_Y;
		} else {
			new_dx = 0f;
			new_dy = -SPEED_Y;
		}*/
		
		new_dx = -getDx();
	}
	
	
	@Override
	protected void groundContact_Y() {
		//System.out.println(this + " groundContact_Y");
		/*if (Math.random() < 0.5) {
			new_dx = SPEED_X;
			new_dy = 0;
		} else {
			new_dx = -SPEED_X;
			new_dy = 0;
		}*/
		
		new_dy = -getDy();
	}
	
	
	@Override
	protected void killed() {
		getWorld().removeMovingEntity(this);
	}
}
