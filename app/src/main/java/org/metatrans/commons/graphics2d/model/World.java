package org.metatrans.commons.graphics2d.model;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.graphics2d.logic.IShapeSet;
import org.metatrans.commons.graphics2d.logic.ShapeSet_Quad;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Collectible;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Feeding;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Ground;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Moving;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Player;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Special;
import org.metatrans.commons.graphics2d.model.entities.IEntity2D;
import org.metatrans.commons.ui.utils.ScreenUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public class World implements IWorld {
	
	
	private static final long serialVersionUID = -1126863413955857516L;
	
	
	private static int MOMENTS_MAX_COUNT = 5;
	private static long MOMENT_LENGTH = 35;
	private static int TIME_INTERVAL_BornTolerance = 5 * 1000;
	
	private float minX = Integer.MAX_VALUE;
	private float maxX = Integer.MIN_VALUE;
	private float minY = Integer.MAX_VALUE;
	private float maxY = Integer.MIN_VALUE;
	
	private float WORLD_SIZE_X;
	private float WORLD_SIZE_Y;
	
	private int VIEWPORT_SIZE_X;
	private int VIEWPORT_SIZE_Y;
	
	private float offsetMaxX;
	private float offsetMaxY;
	private float offsetMinX;
	private float offsetMinY;

	private int maze_size_x;
	private int maze_size_y;

	private List<Entity2D_Ground> groundEntities;
	private List<Entity2D_Ground> groundEntities_Solid;
	private List<Entity2D_Ground> groundEntities_NotSolid;
	private List<Entity2D_Ground> groundEntities_Feeding;
	private List<Entity2D_Collectible> collectibleEntities;
	private List<Entity2D_Moving> movingEntities;
	private List<Entity2D_Moving> movingEntities_buffer;
	private List<Entity2D_Special> specialEntities;
	
	private Entity2D_Player playerEntity;

	private Entity2D_Ground[][] terrain_entities;

	public volatile boolean isDirty;
	//public boolean isUpdated;
	
	private float cell_size;
	
	private long timestamp_lastupdate;
	
	private float SPEED_MAX_PLAYER;
	protected float SPEED_MAX_CHALLENGER;
	private float SPEED_MAX_BULLET;
	
	
	//private transient Context activity;
	private transient RectF camera;
	
	private transient IShapeSet blockers;
	private transient IShapeSet ground;
	private transient List<IEntity2D> buffer_ground;
	
	private transient Paint paint_background;

	
	public World(Context _activity, int _maze_size_x, int _maze_size_y) {

		maze_size_x = _maze_size_x;
		maze_size_y = _maze_size_y;

		init(_activity);
	}
	
	
	private void init(Context _activity) {

		int[] size_xy = ScreenUtils.getScreenSize(_activity);
		VIEWPORT_SIZE_X = Math.max(size_xy[0], size_xy[1]);
		VIEWPORT_SIZE_Y = Math.min(size_xy[0], size_xy[1]);
		
		groundEntities 			= new ArrayList<Entity2D_Ground>();
		groundEntities_Solid	= new ArrayList<Entity2D_Ground>();
		groundEntities_NotSolid = new ArrayList<Entity2D_Ground>();
		groundEntities_Feeding 	= new ArrayList<Entity2D_Ground>();
		collectibleEntities 	= new ArrayList<Entity2D_Collectible>();
		movingEntities 			= new ArrayList<Entity2D_Moving>();
		movingEntities_buffer	= new ArrayList<Entity2D_Moving>();
		specialEntities 		= new ArrayList<Entity2D_Special>();
		
		SPEED_MAX_PLAYER 		= Math.min(VIEWPORT_SIZE_X, VIEWPORT_SIZE_Y) / 50;
		SPEED_MAX_BULLET 		= 2 * SPEED_MAX_PLAYER;
		SPEED_MAX_CHALLENGER 	= (2 * SPEED_MAX_PLAYER) / 3;
		
		paint_background = new Paint();
	}
	
	
	public IShapeSet getBlockersSet() {
		if (blockers == null) {
			//blockers = new ShapeSet_Linear(getGroundEntities_SolidOnly());
			//blockers = new ShapeSet_Matrix(getGroundEntities_SolidOnly());
			blockers = new ShapeSet_Quad(getGroundEntities_SolidOnly());
		}
		return blockers;
	}
	
	
	private IShapeSet getGroundSet_NotSolid() {
		if (ground == null) {
			//blockers = new ShapeSet_Linear(getGroundEntities_SolidOnly());
			//blockers = new ShapeSet_Matrix(getGroundEntities_SolidOnly());
			ground = new ShapeSet_Quad(getGroundEntities_NotSolidOnly());
		}
		return ground;
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#getGroundEntities()
	 */
	@Override
	public List<Entity2D_Ground> getGroundEntities() {
		return groundEntities;
	}

	@Override
	public List<Entity2D_Ground> getGroundEntities_SolidOnly() {
		return groundEntities_Solid;
	}

	
	@Override
	public List<Entity2D_Ground> getGroundEntities_NotSolidOnly() {
		return groundEntities_NotSolid;
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#getCollectibleEntities()
	 */
	@Override
	public List<Entity2D_Collectible> getCollectibleEntities() {
		return collectibleEntities;
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#getSpecialEntities()
	 */
	@Override
	public List<Entity2D_Special> getSpecialEntities() {
		return specialEntities;
	}
	
	
	@Override
	public List<Entity2D_Moving> getMovingEntities() {
		return movingEntities;
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#addEntity(org.metatrans.commons.graphics2d.model.entities.IEntity2D)
	 */
	@Override
	public synchronized void addEntity(IEntity2D entity) {
		
		//System.out.println("Adding 2D entity: " + entity);
		
		if (entity instanceof Entity2D_Ground) {

			groundEntities.add((Entity2D_Ground) entity);

			if (entity.isSolid()) {

				groundEntities_Solid.add((Entity2D_Ground) entity);

			} else if (entity instanceof Entity2D_Feeding) {

				groundEntities_Feeding.add((Entity2D_Ground) entity);

			} else {

				groundEntities_NotSolid.add((Entity2D_Ground) entity);
			}
			
		} else if (entity instanceof Entity2D_Collectible) {
			
			collectibleEntities.add((Entity2D_Collectible) entity);
			
		} else if (entity instanceof Entity2D_Moving) {
			
			movingEntities.add((Entity2D_Moving) entity);
			
			//System.out.println("Adding 2D moving entity: " + entity);
			
			if (entity instanceof Entity2D_Player) {
				
				if (entity.getType() != IEntity2D.TYPE_MOVING || entity.getSubType() != IEntity2D.SUBTYPE_MOVING_PLAYER) {
					throw new IllegalStateException();
				}
				
				playerEntity = (Entity2D_Player) entity;
				//updateCamera();
			}
			
		} else if (entity instanceof Entity2D_Special) {
			
			specialEntities.add((Entity2D_Special) entity);
			
		}
		
		
		if (entity.getEnvelop().left < minX) {
			minX = entity.getEnvelop().left;
		}
		if (entity.getEnvelop().right > maxX) {
			maxX = entity.getEnvelop().right;
		}
		
		if (entity.getEnvelop().top < minY) {
			minY = entity.getEnvelop().top;
		}
		if (entity.getEnvelop().bottom > maxY) {
			maxY = entity.getEnvelop().bottom;
		}
		
		WORLD_SIZE_X = maxX - minX;
		WORLD_SIZE_Y = maxY - minY;
		
		offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
		offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
		offsetMinX = 0;
		offsetMinY = 0;
		
		if (playerEntity != null) {
			playerEntity.setWorldSize(WORLD_SIZE_X, WORLD_SIZE_Y);
		}
		
		for (Entity2D_Moving cur: movingEntities) {
			cur.setWorldSize(WORLD_SIZE_X, WORLD_SIZE_Y);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#draw(android.graphics.Canvas)
	 */
	@Override
	public synchronized void draw(Canvas canvas) {
		
		canvas.save();
		
		canvas.translate(-getCamera().left, -getCamera().top);
		//System.out.println("camX=" + camX + ", camY=" + camY);
		
		if (paint_background == null) {
			paint_background = new Paint();
		}
		
		paint_background.setColor(ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID).getColour_Background());
		canvas.drawRect(getCamera().left, getCamera().top, getCamera().right, getCamera().bottom,
						paint_background);
		
		if (buffer_ground == null) {
			buffer_ground = new ArrayList<IEntity2D>();
		} else {
			buffer_ground.clear();	
		}
		getGroundSet_NotSolid().intersect(buffer_ground, getCamera(), false);
		getBlockersSet().intersect(buffer_ground, getCamera(), false);
		
		for (int i = 0; i < buffer_ground.size(); i++) {

			IEntity2D entity = buffer_ground.get(i);

			if (isInsideCamera(entity.getEnvelop())) {

				entity.draw(canvas);
			} //else {
				//throw new IllegalStateException("Outside camera");
			//}
		}

		if(groundEntities_Feeding == null) {

			groundEntities_Feeding = new ArrayList<Entity2D_Ground>();
		}

		for (int i = 0; i < groundEntities_Feeding.size(); i++) {

			IEntity2D entity = groundEntities_Feeding.get(i);

			if (isInsideCamera(entity.getEnvelop())) {

				entity.draw(canvas);
			} //else {
			//throw new IllegalStateException("Outside camera");
			//}
		}

		for (int i=0; i<collectibleEntities.size(); i++) {
			IEntity2D entity = collectibleEntities.get(i);
			if (isInsideCamera(entity.getEnvelop())) {
				entity.draw(canvas);
			}
		}

		for (int i=0; i<specialEntities.size(); i++) {
			IEntity2D entity = specialEntities.get(i);
			if (isInsideCamera(entity.getEnvelop())) {
				entity.draw(canvas);
			}
		}

		for (int i=0; i<movingEntities.size(); i++) {
			IEntity2D entity = movingEntities.get(i);

			if (entity.getType() == IEntity2D.TYPE_MOVING && entity.getSubType() == IEntity2D.SUBTYPE_MOVING_PLAYER) {
				continue;
			}

			if (isInsideCamera(entity.getEnvelop())) {
				entity.draw(canvas);
			}
		}
		
		playerEntity.draw(canvas);
		
		canvas.restore();
		
		isDirty = false;
	}
	
	
	private boolean isInsideCamera(RectF entityEnvelop) {
		return RectF.intersects(entityEnvelop, getCamera());
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#setPlayerSpeed(float, float)
	 */
	@Override
	public synchronized void setPlayerSpeed(float dx, float dy) {
		
		dx *= SPEED_MAX_PLAYER;
		dy *= SPEED_MAX_PLAYER;
		
		if (dx > 0) {
			
			if (dx < SPEED_MAX_PLAYER / 3) {
				dx = SPEED_MAX_PLAYER / 3;
				
			} else if (dx < SPEED_MAX_PLAYER / 2) {
				dx = SPEED_MAX_PLAYER / 2;
				
			} else {
				dx = SPEED_MAX_PLAYER;
			}
			
		} else if (dx < 0) {
			
			if (dx > -SPEED_MAX_PLAYER / 3) {
				dx = -SPEED_MAX_PLAYER / 3;
				
			} else if (dx > -SPEED_MAX_PLAYER / 2) {
				dx = -SPEED_MAX_PLAYER / 2;
				
			} else {
				dx = -SPEED_MAX_PLAYER;
			}
		}
		
		if (dy > 0) {
			
			if (dy < SPEED_MAX_PLAYER / 3) {
				dy = SPEED_MAX_PLAYER / 3;
				
			} else if (dy < SPEED_MAX_PLAYER / 2) {
				dy = SPEED_MAX_PLAYER / 2;
				
			} else {
				dy = SPEED_MAX_PLAYER;
			}
			
		} else if (dy < 0) {
			
			if (dy > -SPEED_MAX_PLAYER / 3) {
				dy = -SPEED_MAX_PLAYER / 3;
				
			} else if (dy > -SPEED_MAX_PLAYER / 2) {
				dy = -SPEED_MAX_PLAYER / 2;
				
			} else {
				dy = -SPEED_MAX_PLAYER;
			}
		}
		
		playerEntity.setSpeed(dx, dy);
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#button1()
	 */
	@Override
	public synchronized void button1(float dx, float dy) {
		//Do nothing
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#removeMovingEntity(org.metatrans.commons.graphics2d.model.entities.Entity2D_Moving)
	 */
	@Override
	public synchronized void removeMovingEntity(Entity2D_Moving entity) {
		movingEntities.remove(entity);
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#removeCollectibleEntity(org.metatrans.commons.graphics2d.model.entities.Entity2D_Collectible)
	 */
	@Override
	public synchronized void removeCollectibleEntity(Entity2D_Collectible entity) {
		collectibleEntities.remove(entity);
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#getCamera()
	 */
	@Override
	public RectF getCamera() {
		
		if (camera == null) {
			camera = new RectF();
			camera.left = 0;
			camera.right = VIEWPORT_SIZE_X;
			camera.top = 0;
			camera.bottom = VIEWPORT_SIZE_Y;
		}
		
		return camera;
	}
	
	
	/* (non-Javadoc)
	 * @see org.metatrans.commons.graphics2d.model.IWorld#nextMoment()
	 */
	@Override
	public synchronized void update() {
		
		float takts = (System.currentTimeMillis() - timestamp_lastupdate) / (float) MOMENT_LENGTH;
		
		if (takts < 1f) {
			return;
		}
		
		if (takts > MOMENTS_MAX_COUNT) {
			takts = MOMENTS_MAX_COUNT;
		}
		
		//System.out.println("Enter nextMoment");
		
		movingEntities_buffer.addAll(movingEntities);
		
		for (Entity2D_Moving cur: movingEntities_buffer) {
			//System.out.println("nextMoment: before " + cur);
			cur.nextMoment(takts);
			//System.out.println("nextMoment: after " + cur);
		}
		
		movingEntities_buffer.clear();
		
		//System.out.println("nextMoment: before camera");
		updateCamera();
		//System.out.println("nextMoment: after camera");
		
		isDirty = true;
		
		timestamp_lastupdate = System.currentTimeMillis();
		
		//System.out.println("Exit nextMoment");
	}
	
	
	private void updateCamera() {
		
		
		float camX = playerEntity.getX() + (playerEntity.getEnvelop().right - playerEntity.getEnvelop().left) / 2 - VIEWPORT_SIZE_X / 2;
		float camY = playerEntity.getY() + (playerEntity.getEnvelop().bottom - playerEntity.getEnvelop().top) / 2 - VIEWPORT_SIZE_Y / 2;
		
		
		if (camX > offsetMaxX) {
		    camX = offsetMaxX;
		} else if (camX < offsetMinX) {
		    camX = offsetMinX;
		}
		
		
		if (camY > offsetMaxY) {
			camY = offsetMaxY;
		} else if (camY < offsetMinY) {
			camY = offsetMinY;
		}
		
		
		getCamera().left = (int) camX;
		getCamera().right = getCamera().left +  VIEWPORT_SIZE_X;
		
		getCamera().top = (int) camY;
		getCamera().bottom = getCamera().top + VIEWPORT_SIZE_Y;
	}
	
	
	@Override
	public Entity2D_Player getPlayerEntity() {
		return playerEntity;
	}


	@Override
	public float getCellSize() {
		return cell_size;
	}

	@Override
	public synchronized Entity2D_Ground getTerrainCell(int x, int y) {

		if (terrain_entities == null) {

			terrain_entities = new Entity2D_Ground[maze_size_x][maze_size_y];

			for (Entity2D_Ground entity: groundEntities) {


				if (terrain_entities[entity.getCellIndex_X()][entity.getCellIndex_Y()] != null) {

					throw new IllegalStateException();
				}

				terrain_entities[entity.getCellIndex_X()][entity.getCellIndex_Y()] = entity;
			}
		}

		return terrain_entities[x][y];
	}


	@Override
	public void setCellSize(float _cell_size) {

		cell_size = _cell_size;
	}


	@Override
	public String getEntitiesCount() {
		
		return movingEntities.size() + "/" + (groundEntities.size() + collectibleEntities.size() + movingEntities.size() + specialEntities.size());
	}


	@Override
	public boolean isDirty() {

		return isDirty;
	}


	@Override
	public int getMaxSpeed_CHALLENGER() {

		return (int) SPEED_MAX_CHALLENGER;
	}


	@Override
	public int getMaxSpeed_BULLET() {

		return (int) SPEED_MAX_BULLET;
	}
	
	
	@Override
	public int getTimeInterval_BornTolerance() {

		return TIME_INTERVAL_BornTolerance;
	}


	@Override
	public boolean isOuterBorder(int cell_x, int cell_y) {

		return cell_x == 0
				|| cell_x == terrain_entities.length - 1
				|| cell_y == 0
				|| cell_y == terrain_entities[0].length - 1;
	}
}
