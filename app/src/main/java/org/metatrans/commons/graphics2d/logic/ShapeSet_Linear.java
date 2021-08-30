package org.metatrans.commons.graphics2d.logic;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.graphics2d.model.entities.IEntity2D;

import android.graphics.RectF;


public class ShapeSet_Linear implements IShapeSet {
	
	
	private List<IEntity2D> all;
	
	
	public ShapeSet_Linear(List<? extends IEntity2D> _all) {
		
		all = new ArrayList<IEntity2D>();
		all.addAll(_all);
	}
	
	
	@Override
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first) {
		for (IEntity2D cur: all) {
			if (RectF.intersects(cur.getEvelop(), test)) {
				result.add(cur);
				if (stop_after_first) return; 
			}
		}
	}
}
