package org.metatrans.commons.graphics2d.logic;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.graphics2d.model.entities.IEntity2D;

import android.graphics.RectF;


public class ShapeSet_Quad implements IShapeSet {
	
	
	private RectF[][] matrix_rects;
	private List<IEntity2D>[][] matrix_entities;
	private IShapeSet[][] matrix_set;
	
	
	public ShapeSet_Quad(List<? extends IEntity2D> _all) {
		
		//System.out.println(this + " ShapeSet_Quad: constructor all.size=" + _all.size());
		
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
		
		
		matrix_rects 		= new RectF[2][2];
		matrix_entities 	= new List[2][2];
		matrix_set			= new IShapeSet[2][2];
		
		
		float cell_x = (max_x - min_x) / (float) 2;
		float cell_y = (max_y - min_y) / (float) 2;
		
		for (int x=0; x < 2; x++) {
			
			for (int y=0; y < 2; y++) {
				
				matrix_rects[x][y] 		= new RectF(min_x + x * cell_x, min_y + y * cell_y, min_x + (x + 1) * cell_x, min_y + (y + 1) * cell_y);
				matrix_entities[x][y]	= new ArrayList<IEntity2D>();
				
				for (IEntity2D cur: _all) {
					if (RectF.intersects(cur.getEnvelop(), matrix_rects[x][y])) {
						matrix_entities[x][y].add(cur);
					}
				}
			}
		}
		
		
		for (int x=0; x < 2; x++) {
			
			for (int y=0; y < 2; y++) {
				
				List<IEntity2D> cur = matrix_entities[x][y];
				
				//System.out.println(this + " ShapeSet_Quad: cur.size=" + cur.size());
				
				if (cur.size() >= 64) {
					matrix_set[x][y] = new ShapeSet_Quad(cur);
					
				} else if (cur.size() >= 16) {
					matrix_set[x][y] = new ShapeSet_Matrix(cur);
					
				} else {
					matrix_set[x][y] = new ShapeSet_Linear(cur);
				}
			}
		}
	}
	
	
	@Override
	public void intersect(List<IEntity2D> result, RectF test, boolean stop_after_first) {
		
		for (int x=0; x < 2; x++) {
			
			for (int y=0; y < 2; y++) {
				
				if (RectF.intersects(test, matrix_rects[x][y])) {
					
					matrix_set[x][y].intersect(result, test, stop_after_first);
					
					if (result.size() > 0) {
						if (stop_after_first) return; 
					}
				}
			}
		}
	}
}
