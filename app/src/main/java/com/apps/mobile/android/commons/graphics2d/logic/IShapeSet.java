package com.apps.mobile.android.commons.graphics2d.logic;


import java.util.List;

import android.graphics.RectF;

import com.apps.mobile.android.commons.graphics2d.model.entities.IEntity2D;


public interface IShapeSet {
	
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first);
}
