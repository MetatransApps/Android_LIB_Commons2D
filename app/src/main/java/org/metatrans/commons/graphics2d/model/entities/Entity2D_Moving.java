package org.metatrans.commons.graphics2d.model.entities;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.graphics2d.model.World;

import android.graphics.RectF;


public abstract class Entity2D_Moving extends Entity2D_Base {
	
	
	private static final long serialVersionUID = 5936821405527936582L;

	protected static float ENVELOP_DRAW_EXTENSION = 1.3f;
	protected static float ENVELOP_DRAW_UPSIDE 	  = 0.605f;
	protected static float ENVELOP_DRAW_DOWNSIDE  = 1 - ENVELOP_DRAW_UPSIDE;
	
	private float WORLD_SIZE_X;
	private float WORLD_SIZE_Y;
	
	private float dx;
	private float dy;
	
	private List<? extends IEntity2D> blockerEntities;
	private List<? extends IEntity2D> killerEntities;
	
	
	private transient RectF test_newposition;
	
	//TODO HACK: blockers are static - MOVE IT TO THE WORLD OBJECT
	//private transient IShapeSet blockers;
	
	private List<IEntity2D> blockers_tester;
	
	private RectF envelop_ForDraw;


	public Entity2D_Moving(World _world, RectF _evelop, int _subtype, List<? extends IEntity2D> _blockerEntities, List<? extends IEntity2D> _killerEntities) {
		
		super(_world, _evelop, _subtype);
		
		blockerEntities 	= _blockerEntities;
		killerEntities 		= _killerEntities;
		
		blockers_tester 	= new ArrayList<IEntity2D>();
		
		//movingHistory 		= new ArrayList<PointF>();
	}


	@Override
	public RectF getEnvelop_ForDraw() {

		RectF rect_org = getEnvelop();

		float width = 1 * (rect_org.right - rect_org.left);
		float height = 1 * (rect_org.bottom - rect_org.top);

		float shift_y = height * (ENVELOP_DRAW_DOWNSIDE - ENVELOP_DRAW_UPSIDE);


		if (envelop_ForDraw == null) {

			envelop_ForDraw = new RectF();
		}

		envelop_ForDraw.left = rect_org.left - (width * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.top = shift_y + rect_org.top - (height * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.right = rect_org.right + (width * (ENVELOP_DRAW_EXTENSION - 1));
		envelop_ForDraw.bottom = shift_y + rect_org.bottom + (height * (ENVELOP_DRAW_EXTENSION - 1));

		return envelop_ForDraw;
	}


	@Override
	public int getType() {
		return TYPE_MOVING;
	}
	
	
	public void setWorldSize(float X, float Y) {
		WORLD_SIZE_X = X;
		WORLD_SIZE_Y = Y;
	}
	
	
	public void setSpeed(float _dx, float _dy) {
		dx = _dx;
		dy = _dy;
	}


	public float getDx() {
		return dx;
	}


	public float getDy() {
		return dy;
	}
	
	
	public void clearDx() {
		dx = 0f;
	}
	
	
	public void clearDy() {
		dy = 0f;
	}
	
	
	public int getTestIterations() {
		return 1;
	}
	
	
	//static float all;
	//static float col;
	
	public void nextMoment(float takts) {
		
		//all++;
		
		if (killerEntities != null) {
			for (IEntity2D killer: killerEntities) {
				if (RectF.intersects(getEnvelop(), killer.getEnvelop())) {
					killed((Entity2D_Moving) killer);
					return;
				}
			}
		}
		
		
		//test x and y axis
		float new_DX = 0f;
		float new_DY = 0f;
		
		int test_iterations = getTestIterations();
		
		//test dx
		if (getDx() != 0f) {
			
			float incDX = (takts * getDx()) / (float) test_iterations;
			
			for (int i=0; i < test_iterations; i++) {
				
				new_DX += incDX;
				
				getTest_newposition().set(getEnvelop());
				getTest_newposition().offset(new_DX, new_DY);
				
				boolean ok = notIntersectsWithBlockers(getTest_newposition());
				
				if (!ok) {
					
					new_DX -= incDX;
					
					if (new_DX == 0f) {
						groundContact_X();
						//col++;
					}
					
					break;
				}
			}
		}
		
		//test dy
		if (getDy() != 0f) {
			
			float incDY = (takts * getDy()) / (float) test_iterations;
			
			for (int i=0; i < test_iterations; i++) {
				
				new_DY += incDY;
				
				getTest_newposition().set(getEnvelop());
				getTest_newposition().offset(new_DX, new_DY);
				
				boolean ok = notIntersectsWithBlockers(getTest_newposition());
				
				if (!ok) {
					
					new_DY -= incDY;
					
					if (new_DY == 0f) {
						groundContact_Y();
						//col++;
					}
					
					break;
				}
			}
		}
		
		
		//System.out.println("RATE: " + (col/(float)all));
		
		//test inside world
		if (getEnvelop().left + new_DX < 0) {
			new_DX = -getEnvelop().left;
			clearDx();
			groundContact_X();
		}
		if (getEnvelop().right + new_DX > WORLD_SIZE_X) {
			new_DX = WORLD_SIZE_X - getEnvelop().right;
			clearDx();
			groundContact_X();
		}
		if (getEnvelop().top + new_DY < 0) {
			new_DY = -getEnvelop().top;
			clearDy();
			groundContact_Y();
		}
		if (getEnvelop().bottom + new_DY > WORLD_SIZE_Y) {
			new_DY = WORLD_SIZE_Y - getEnvelop().bottom;
			clearDy();
			groundContact_Y();
		}
		
		
		if (new_DX != 0f || new_DY != 0f) {
			
			getEnvelop().offset(new_DX, new_DY);
			
			/*
			movingHistory.add(new PointF(new_DX, new_DY));
			if (movingHistory.size() > movingHistorySize) {
				movingHistory.remove(0);
			}
			*/
		}
	}
	
	
	/*
	protected float getDx_History() {
		float result = 0;
		for (PointF cur: movingHistory) {
			result += cur.x;
		}
		return result / (float) movingHistorySize;
	}
	
	
	protected float getDy_History() {
		float result = 0;
		for (PointF cur: movingHistory) {
			result += cur.y;
		}
		return result / (float) movingHistorySize;
	}
	*/
	
	
	protected void groundContact_X() {
		//System.out.println(this + " groundContact_X");
	}
	
	
	protected void groundContact_Y() {
		//System.out.println(this + " groundContact_Y");
	}
	
	
	protected void killed(Entity2D_Moving killer) {
		//System.out.println(this + " groundContact_Y");
	}
	
	
	private boolean notIntersectsWithBlockers(RectF test) {
		
		blockers_tester.clear();
		
		getWorld().getBlockersSet().intersect(blockers_tester, test, true);
		
		return blockers_tester.size() == 0;
	}


	protected List<? extends IEntity2D> getBlockerEntities() {
		return blockerEntities;
	}


	protected World getWorld() {
		return (World) world;
	}


	protected RectF getTest_newposition() {
		
		if (test_newposition == null) {
			test_newposition = new RectF();
		}
		return test_newposition;
	}
}
