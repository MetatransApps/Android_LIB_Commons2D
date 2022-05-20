package org.metatrans.commons.graphics2d.model;


import java.io.Serializable;
import java.util.List;

import org.metatrans.commons.graphics2d.model.entities.Entity2D_Collectible;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Ground;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Moving;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Player;
import org.metatrans.commons.graphics2d.model.entities.Entity2D_Special;
import org.metatrans.commons.graphics2d.model.entities.IEntity2D;

import android.graphics.Canvas;
import android.graphics.RectF;


public interface IWorld extends Serializable {

	//public abstract void setActivity(Context context);
	
	//public abstract Context getActivity();

	public abstract void addEntity(IEntity2D entity);
	public abstract void removeMovingEntity(Entity2D_Moving entity);
	public abstract void removeCollectibleEntity(Entity2D_Collectible entity);

	
	public abstract List<Entity2D_Ground> getGroundEntities();
	public abstract List<Entity2D_Ground> getGroundEntities_SolidOnly();
	public abstract List<Entity2D_Ground> getGroundEntities_NotSolidOnly();
	public abstract List<Entity2D_Collectible> getCollectibleEntities();
	public abstract List<Entity2D_Special> getSpecialEntities();
	public abstract List<Entity2D_Moving> getMovingEntities();
	public abstract Entity2D_Player getPlayerEntity();
	
	
	public abstract void update();
	
	public abstract void draw(Canvas canvas);
	
	public abstract RectF getCamera();
	
	public abstract void setPlayerSpeed(float dx, float dy);
	public abstract void button1(float dx, float dy);

	public void setCellSize(float cell_size);
	public abstract float getCellSize();

	public Entity2D_Ground getTerrainCell(int x, int y);

	/*public abstract void setCellsCount(int x, int y);
	public abstract int getCellsCount_X();
	public abstract int getCellsCount_Y();*/
	
	public abstract String getEntitiesCount();

	public abstract boolean isDirty();
	
	public int getMaxSpeed_CHALLENGER();
	public int getMaxSpeed_BULLET();
	
	public int getTimeInterval_BornTolerance();

    boolean isOuterBorder(int cell_x, int cell_y);
}