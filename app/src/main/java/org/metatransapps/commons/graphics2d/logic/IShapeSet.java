package org.metatransapps.commons.graphics2d.logic;


import java.util.List;

import org.metatransapps.commons.graphics2d.model.entities.IEntity2D;

import android.graphics.RectF;


public interface IShapeSet {
	
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first);
}
