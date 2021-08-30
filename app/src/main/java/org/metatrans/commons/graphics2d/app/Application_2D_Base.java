package org.metatrans.commons.graphics2d.app;


import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.graphics2d.model.GameData;
import org.metatrans.commons.graphics2d.model.IWorld;
import org.metatrans.commons.graphics2d.model.UserSettings;
import org.metatrans.commons.model.LevelsResults;
import org.metatrans.commons.storage.StorageUtils;


public abstract class Application_2D_Base extends Application_Base_Ads {
	
	
	@Override
	public GameData getGameData() {
		
		GameData data = (GameData) super.getGameData();
		
		return data;
	}
	
	
	public abstract void setNextLevel();
	
	public abstract IWorld createNewWorld();
	
	
	public static Application_2D_Base getInstance() {
		return (Application_2D_Base) Application_Base_Ads.getInstance();
	}
	
	
	@Override
	public UserSettings getUserSettings() {
		return (UserSettings) super.getUserSettings();
	}
	
	
	public boolean isCurrentlyGameActiveIntoTheMainScreen() {
		
		return !getGameData().isCountedAsCompleted()
				&& !getGameData().isCountedAsExited()
				&& !getGameData().level_completed
				&& !getGameData().paused;
	}
	
	
	public LevelsResults getLevelsResults() {
		LevelsResults data = (LevelsResults) StorageUtils.readStorage(this, LevelsResults.FILE_NAME_LEVELS_RESULTS);
		if (data == null) {
			data = new LevelsResults();
			StorageUtils.writeStore(this, LevelsResults.FILE_NAME_LEVELS_RESULTS, data);
			data = (LevelsResults) StorageUtils.readStorage(this, LevelsResults.FILE_NAME_LEVELS_RESULTS);
		}
		return data;
	}
	
	
	public void storeLevelsResults() {
		
		System.out.println("LevelsResults Store");
		
		StorageUtils.writeStore(this, LevelsResults.FILE_NAME_LEVELS_RESULTS, getLevelsResults());
	}
}
