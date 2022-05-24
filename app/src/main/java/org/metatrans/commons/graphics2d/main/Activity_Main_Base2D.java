package org.metatrans.commons.graphics2d.main;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.model.GameData;
import org.metatrans.commons.graphics2d.model.UserSettings;

import com.commons2d.R;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


public abstract class Activity_Main_Base2D extends Activity_Base_Ads_Banner {
	
	
	private static final int MAIN_VIEW_ID = 32565779;
	
	
	private ExecutorService executor;
	private Handler uiHandler;
	private boolean isActivityActive;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		System.out.println("Activity_Main_Base2D: onCreate()");
		
		super.onCreate(savedInstanceState);
		
	    requestWindowFeature(Window.FEATURE_NO_TITLE);

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	    
		initUI();

		System.out.println("getWindow().getAttributes()=" + getWindow().getAttributes());
	}


	@Override
	protected int getGravity() {
		return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
	}


	private void initUI() {
		
		setContentView(R.layout.main_frame);
		
		ViewGroup frame = getFrame();
		
		View old = frame.findViewById(MAIN_VIEW_ID);
		
		if (old != null) {
			throw new IllegalStateException("Old view is not null");
		}
		
		View main = createMainView();
		main.setId(MAIN_VIEW_ID);
		
		frame.addView(main, 0);
	}
	
	
	protected abstract View createMainView();
	
	
	@Override
	protected String getBannerName() {
		return IAdsConfiguration.AD_ID_BANNER2;
	}
	
	
	@Override
	protected FrameLayout getFrame() {
		return (FrameLayout) findViewById(R.id.main_frame);
	}
	
	
	@Override
	protected void onResume() {
		
		
		super.onResume();
		
		
		isActivityActive = true;
		
		executor = Executors.newCachedThreadPool();

		uiHandler = new Handler(Looper.getMainLooper());
		
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("Activity_Main_Base2D: RunnableBannerCheck > in loop");
				
				try {
					while (isActivityActive) {
						
						//System.out.println("Activity_Main_Base2D: RunnableBannerCheck > active=" + Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen() + ", attached=" + isBannerAttached());
						
						if (!Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
							
							if (!isBannerAttached()) {
								
								if (uiHandler != null) uiHandler.post(new Runnable() {
									
									@Override
									public void run() {
										System.out.println("Activity_Main_Base2D: RunnableBannerCheck > attaching");
										attachBanner();
									}
								});
							}
							
						} else {
							
							if (isBannerAttached()) {
								
								if (uiHandler != null) uiHandler.post(new Runnable() {
									
									@Override
									public void run() {
										System.out.println("Activity_Main_Base2D: RunnableBannerCheck > detaching");
										detachBanner();
									}
								});
							}
						}
						
						try {
							Thread.sleep(333);
						} catch (InterruptedException e) {}
					}
					
					System.out.println("Activity_Main_Base2D: RunnableBannerCheck > exit loop");
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		executor.execute(runnable);
	}
	
	
	
	@Override
	protected void onPause() {
		
		Application_2D_Base.getInstance().storeGameData();
		
		
		isActivityActive = false;
		
		if (uiHandler != null) {
			uiHandler.removeCallbacksAndMessages(null);
			uiHandler = null;
		}
		
		List<Runnable> rejected = executor.shutdownNow();
		executor = null;
		
		
		super.onPause();
	}
	
	
	
	public void startNewGame() {

		GameData gamedata = (GameData) ((Application_Base) getApplication()).getGameData();

		//UserSettings settings = (UserSettings) ((Application_Base) getApplication()).getUserSettings();

		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();

		eventsManager.handleGameEvents_OnExit(this, gamedata, ((Application_Base)getApplication()).getUserSettings());
		
		Application_2D_Base.getInstance().recreateGameDataObject();

		//Application_Base_Ads.getInstance().openInterstitial();
	}
	
	
	@Override
	public void onBackPressed() {
		
		Application_2D_Base.getInstance().storeGameData();
		
		super.onBackPressed();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	
	public IConfigurationColours getColoursCfg() {
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base)getApplication()).getUserSettings().uiColoursID);
		return coloursCfg;
	}
}
