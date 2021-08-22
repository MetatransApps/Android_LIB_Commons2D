package com.apps.mobile.android.commons.graphics2d.main;


import com.apps.mobile.android.commons.app.Application_Base;
import com.apps.mobile.android.commons.events.api.IEventsManager;
import com.apps.mobile.android.commons.graphics2d.app.Application_2D_Base;
import com.apps.mobile.android.commons.main.Activity_Result_Base_Ads;


public abstract class Activity_Result_Base2D extends Activity_Result_Base_Ads {
	
	
	@Override
	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		eventsManager.handleGameEvents_OnExit(this, ((Application_Base)getApplication()).getGameData(), ((Application_Base)getApplication()).getUserSettings());
		
		Application_2D_Base.getInstance().recreateGameDataObject();
	}
}
