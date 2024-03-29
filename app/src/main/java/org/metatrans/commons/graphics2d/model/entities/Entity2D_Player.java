package org.metatrans.commons.graphics2d.model.entities;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.model.GameData;
import org.metatrans.commons.graphics2d.model.UserSettings;
import org.metatrans.commons.graphics2d.model.World;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.RectF;


public abstract class Entity2D_Player extends Entity2D_Moving {
	
	
	private static final long serialVersionUID = -4178124995621289322L;
	
	
	private List<Entity2D_Collectible> collectibleEntities;

	private List<Entity2D_Collectible> collectibleEntities_buffer;

	private List<Entity2D_Collectible> collectedEntities;

	private boolean killed_final;


	public Entity2D_Player(World _world, RectF _evelop,
						   List<? extends IEntity2D> blockersEntities, List<? extends IEntity2D> _killerEntities,
						   int bitmap_id, int rotation_angle_in_degrees) {
		
		super(_world, _evelop, SUBTYPE_MOVING_PLAYER, blockersEntities, _killerEntities, bitmap_id, rotation_angle_in_degrees);
		
		collectibleEntities 		= getWorld().getCollectibleEntities();
		collectibleEntities_buffer 	= new ArrayList<Entity2D_Collectible>();
		
		collectedEntities 			= new ArrayList<Entity2D_Collectible>();
		
	}


	protected boolean isInBornTolerance() {
		return System.currentTimeMillis() < Application_2D_Base.getInstance().getGameData().timestamp_lastborn + getWorld().getTimeInterval_BornTolerance();
	}
	
	
	@Override
	public void draw(Canvas c) {

		if (isInBornTolerance() && !Application_2D_Base.getInstance().getGameData().paused) {

			if (Math.random() < 0.75f) {

				super.draw(c);
			}

		} else {

			super.draw(c);
		}
	}


	@Override
	protected void killed(Entity2D_Moving killer) {
		
		if (!isInBornTolerance()) {
			
			Application_2D_Base.getInstance().getGameData().count_lives--;
			
			if (Application_2D_Base.getInstance().getGameData().count_lives > 0) {
				
				Application_2D_Base.getInstance().getGameData().timestamp_lastborn = System.currentTimeMillis();
				
			} else {
				
				killedFinal();
			}
		}
	}
	
	
	protected void killedFinal() {

		if (killed_final) {

			return;
		}

		killed_final = true;

		System.out.println("Entity2D_Player.killedFinal(): " + "called");

		Application_2D_Base.getInstance().getGameData().total_count_steps += Application_2D_Base.getInstance().getGameData().count_steps;

		Application_2D_Base.getInstance().getGameData().count_steps = 0;
		
		Activity currentActivity = Application_Base.getInstance().getCurrentActivity();
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		
		eventsManager.handleGameEvents_OnFinish(currentActivity,
				Application_2D_Base.getInstance().getGameData(),
				Application_2D_Base.getInstance().getUserSettings(), -1);
		
		Application_Base.getInstance().storeGameData();


		//Check for new record
		GameData gamedata = (GameData) Application_Base.getInstance().getGameData();

		UserSettings settings = (UserSettings) Application_Base.getInstance().getUserSettings();

		if (gamedata.count_killed_balls >= settings.best_scores
				|| gamedata.total_count_steps >= settings.best_scores
				//|| TODO For Gravity Game
			) {

			System.out.println("Entity2D_Player.killedFinal(): " + "new record");

			Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().openLeaderboard(
					Application_Base.getInstance().getUserSettings().modeID
				);
		}


		//TODO: If the ad is not opened and game end, than make sure the game is paused before opening the interstitial ad!
		Application_Base_Ads.getInstance().openInterstitial();
	}


	protected Class<? extends Activity> getActivityResult_Class() {
		return null;
	}


	@Override
	public int getTestIterations() {
		return 5;
	}
	
	
	public void addCollectedEntity(Entity2D_Collectible collectedEntity) {
		collectedEntities.add(collectedEntity);
	}


	protected List<Entity2D_Collectible> getCollectedEntities() {

		return collectedEntities;
	}

	
	public boolean hasKey() {
		
		for (Entity2D_Collectible cur: collectedEntities) {
			if (cur.getType() == TYPE_COLLECTIBLE && cur.getSubType() == SUBTYPE_COLLECTIBLE_KEY) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public void nextMoment(float takts) {
		
		super.nextMoment(takts);
		
		collectibleEntities_buffer.addAll(collectibleEntities);
		
		for (Entity2D_Collectible cur: collectibleEntities_buffer) {
			
			if (RectF.intersects(getEnvelop(), cur.getEnvelop())) {
				
				getWorld().removeCollectibleEntity(cur);
				
				addCollectedEntity(cur);
				
				if (cur.getType() == TYPE_COLLECTIBLE && cur.getSubType() == SUBTYPE_COLLECTIBLE_BULLET) {
					Application_2D_Base.getInstance().getGameData().count_bullets++;
				}
				
				if (cur.getType() == TYPE_COLLECTIBLE && cur.getSubType() == SUBTYPE_COLLECTIBLE_STAR) {
					Application_2D_Base.getInstance().getGameData().count_stars++;
				}
			}
		}
		
		collectibleEntities_buffer.clear();
	}
}
