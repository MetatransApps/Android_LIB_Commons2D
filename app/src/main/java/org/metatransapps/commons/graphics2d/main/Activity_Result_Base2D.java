package org.metatransapps.commons.graphics2d.main;


import org.metatransapps.commons.app.Application_Base;
import org.metatransapps.commons.events.api.IEventsManager;
import org.metatransapps.commons.graphics2d.app.Application_2D_Base;
import org.metatransapps.commons.main.Activity_Result_Base_Ads;


public abstract class Activity_Result_Base2D extends Activity_Result_Base_Ads {
	
	
	@Override
	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		eventsManager.handleGameEvents_OnExit(this, ((Application_Base)getApplication()).getGameData(), ((Application_Base)getApplication()).getUserSettings());
		
		Application_2D_Base.getInstance().recreateGameDataObject();
	}
}
