package org.metatrans.commons.graphics2d.model.logic;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.graphics2d.model.entities.IEntity2D;

import android.graphics.RectF;


public class ShapeSet_Matrix implements IShapeSet {
	
	
	private int cells;
	private RectF[][] matrix_rects;
	private List<IEntity2D>[][] matrix_entities;
	
	
	public ShapeSet_Matrix(List<? extends IEntity2D> _all) {
		
		cells = (int) (0.5 + Math.sqrt(Math.sqrt(_all.size())));
		
		//System.out.println("ShapeSet_Matrix: cells=" + cells);
		
		matrix_entities = new ArrayList[cells][cells];
		matrix_rects = new RectF[cells][cells];
		
		
		float min_x = Float.MAX_VALUE;
		float min_y = Float.MAX_VALUE;
		float max_x = Float.MIN_VALUE;
		float max_y = Float.MIN_VALUE;
		
		for (IEntity2D cur: _all) {
			if (min_x > cur.getEnvelop().left) {
				min_x = cur.getEnvelop().left;
			}
			if (max_x < cur.getEnvelop().right) {
				max_x = cur.getEnvelop().right;
			}
			if (min_y > cur.getEnvelop().top) {
				min_y = cur.getEnvelop().top;
			}
			if (max_y < cur.getEnvelop().bottom) {
				max_y = cur.getEnvelop().bottom;
			}
		}
		
		
		float cell_x = (max_x - min_x) / (float) cells;
		float cell_y = (max_y - min_y) / (float) cells;
		
		//System.out.println("ShapeSet_Matrix: cell_x=" + cell_x + ", cell_y=" + cell_y);
		
		for (int x=0; x < cells; x++) {
			
			for (int y=0; y < cells; y++) {
				
				matrix_rects[x][y] 		= new RectF(min_x + x * cell_x, min_y + y * cell_y, min_x + (x + 1) * cell_x, min_y + (y + 1) * cell_y);
				
				matrix_entities[x][y] 	= new ArrayList<IEntity2D>();
				
				for (IEntity2D cur: _all) {
					if (RectF.intersects(cur.getEnvelop(), matrix_rects[x][y])) {
						matrix_entities[x][y].add(cur);
					}
				}
				
				//System.out.println("ShapeSet_Matrix: size[" + x + "][" + y + "] = " + matrix_entities[x][y].size());
			}
		}
	}
	
	
	@Override
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first) {
		
		for (int x=0; x < cells; x++) {
			
			for (int y=0; y < cells; y++) {
				
				if (RectF.intersects(test, matrix_rects[x][y])) {
					
					List<IEntity2D> bucket = matrix_entities[x][y];

					for (IEntity2D cur: bucket) {

						if (cur.getEnvelop().equals(test)) {

							continue;
						}

						if (RectF.intersects(cur.getEnvelop(), test)) {

							result.add(cur);

							if (stop_after_first) return; 
						}
					}
				}
			}
		}
	}
}
