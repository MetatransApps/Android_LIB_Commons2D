package org.metatransapps.commons.graphics2d.ui;


import org.metatransapps.commons.Activity_Base;
import org.metatransapps.commons.app.Application_Base;
import org.metatransapps.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatransapps.commons.cfg.colours.IConfigurationColours;
import org.metatransapps.commons.events.api.IEventsManager;
import org.metatransapps.commons.graphics2d.app.Application_2D_Base;

import com.commons2d.R;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public abstract class Activity_Base2D extends Activity_Base {
	
	
	private int VIEW_ID = 32565779;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	    
		setContentView(R.layout.main_frame);
		
		ViewGroup frame = (ViewGroup) findViewById(R.id.main_frame);
		
		View old = frame.findViewById(VIEW_ID);
		if (old != null) {
			frame.removeView(old);
		}
		
		View main = createMainView();
		main.setId(VIEW_ID);
		frame.addView(main);
	}
		
	
	protected abstract View createMainView();
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		
		super.onPause();
		
		Application_2D_Base.getInstance().storeGameData();
	}
	
	
	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		eventsManager.handleGameEvents_OnExit(this, ((Application_Base)getApplication()).getGameData(), ((Application_Base)getApplication()).getUserSettings());
		
		Application_2D_Base.getInstance().recreateGameDataObject();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	

	@Override
	protected int getBackgroundImageID() {
		return 0;
	}

	
	public IConfigurationColours getColoursCfg() {
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base)getApplication()).getUserSettings().uiColoursID);
		return coloursCfg;
	}
}
