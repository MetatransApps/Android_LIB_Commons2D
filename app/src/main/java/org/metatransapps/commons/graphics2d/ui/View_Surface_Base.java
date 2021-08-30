package org.metatransapps.commons.graphics2d.ui;


import org.metatransapps.commons.TimeUtils;
import org.metatransapps.commons.app.Application_Base;
import org.metatransapps.commons.graphics2d.app.Application_2D_Base;
import org.metatransapps.commons.graphics2d.model.IWorld;
import org.metatransapps.commons.ui.utils.ScreenUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class View_Surface_Base extends SurfaceView implements SurfaceHolder.Callback {
	
	
	private UIUpdaterThread _thread;
	
	
    //Measure frames per second.
    long now;
    int framesCount=0;
    int framesCountAvg=0;
    long framesTimer=0;
    Paint fpsPaint=new Paint();
    Paint fpsPaint_background =new Paint();

    //Frame speed
    long timeNow;
    long timePrev = 0;
    long timePrevFrame = 0;
    long timeDelta;
    
    long buffer_time_interval_playing_ms;
    
    
	public View_Surface_Base(Context context) {
		
		super(context);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		

		setFocusable(true);
		
		fpsPaint.setColor(Color.RED);
		fpsPaint_background.setColor(Color.WHITE);
	}
	
	
	protected IWorld getWorld() {
		return Application_2D_Base.getInstance().getGameData().world;
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		if (_thread == null) {
			//holder.setKeepScreenOn(true);
			_thread = new UIUpdaterThread(holder, this);
			_thread.setRunning(true);
			_thread.start();
		}
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// resize canvas here
	}
	
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// simply copied from sample application LunarLander:
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		_thread.setRunning(false);
		while (retry) {
			try {
				_thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}
		
		_thread = null;
	}
	
	
	protected boolean isGameOver() {
		return Application_Base.getInstance().getGameData().isCountedAsCompleted() || Application_Base.getInstance().getGameData().isCountedAsExited();
	}
	
	
	protected boolean isLevelCompleted() {
		return Application_2D_Base.getInstance().getGameData().level_completed;
	}
	
	
    @Override
    public void onDraw(Canvas canvas) {
	   
        super.onDraw(canvas);
        
        now=System.currentTimeMillis();
        
        framesCount++;
        if(now-framesTimer>1000) {
                framesTimer=now;
                framesCountAvg=framesCount;
                framesCount=0;
        }
        
        getWorld().draw(canvas);
		
		if (Application_Base.getInstance().isTestMode()) {
			
			int[] screen_size = ScreenUtils.getScreenSize((Activity) getContext());
			int screen_height = Math.min(screen_size[0], screen_size[1]);
			int cell_size = (int) getWorld().getCellSize();
			int y = screen_height - cell_size / 2 - 20 / 2;//455;
			
			canvas.drawRect(5, y, 150, y + 20, fpsPaint_background);
			
			canvas.drawText(framesCountAvg + " fps", 10, y + 15, fpsPaint);
			canvas.drawText(getWorld().getEntitiesCount() + " entities", 50, y + 15, fpsPaint);
			
			canvas.drawRect(160, y, 220, y + 20, fpsPaint_background);
			canvas.drawText(TimeUtils.time2string( ((Application_Base)Application_Base.getInstance()).getGameData().getAccumulated_time_inmainscreen()),
					160 + 10, y + 15, fpsPaint);
			
			//canvas.drawBitmap(World_Labyrints.bitmap_, null, rect_time_icon, default_paint);
		}
   }
    
	
	class UIUpdaterThread extends Thread {
		
	    
		private SurfaceHolder _surfaceHolder;
	    private View_Surface_Base _panel;
	    private Rect camera;
	    
	    private boolean running = false;
	    
	    
	    public UIUpdaterThread(SurfaceHolder surfaceHolder, View_Surface_Base panel) {
	    	
	        _surfaceHolder = surfaceHolder;
	        _panel = panel;
	        
	        camera = new Rect();
	        
			int[] screen_size = ScreenUtils.getScreenSize((Activity) getContext());
			int main_width = Math.max(screen_size[0], screen_size[1]);
			int main_height = Math.min(screen_size[0], screen_size[1]);
			
        	camera.left = 0;
        	camera.top = 0;
        	camera.right = main_width;
        	camera.bottom = main_height;
	    }
	    
	    
	    public void setRunning(boolean run) {
	        running = run;
	    }
	    
	    
	    public boolean isRunning() {
	    	return running;
	    }
	    
		
		@SuppressLint("WrongCall")
		@Override
        public void run() {
			
        	buffer_time_interval_playing_ms = System.currentTimeMillis();
        	
	        //Looper.prepare();
			
        	final int UPDATE_INTERVAL = 33;//17 in ms
        	
            while (running) {
            	
            	try {
	                timeNow = System.currentTimeMillis();
	                timeDelta = timeNow - timePrevFrame;
	                long sleepTime;
	                if (timeDelta < UPDATE_INTERVAL) {
	                	//Limit frame rate to max 30fps
	                    sleepTime = UPDATE_INTERVAL - timeDelta;
	                } else {
	                	//Give time to other threads in order to prevent screen freeze
	                	sleepTime = 10;//ms
	                }
	                
                    try {
                        Thread.sleep(sleepTime);
                    } catch(InterruptedException e) {
                    	//Do nothing
                    }
	                
	                timePrevFrame = System.currentTimeMillis();
	                
	            	Canvas c = null;
	                try {
	                	
	                    c = _surfaceHolder.lockCanvas(camera);
	                    //System.out.println("canvas=" + c);
	                    if (c != null) {
		                    //synchronized (_surfaceHolder) {
		                       //call methods to draw and process next fame
		                    	_panel.onDraw(c);
		                    //}
	                    }
	                    
	                    if (Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
	                    	
		                	getWorld().update();
		                	
		                    if (timeNow - buffer_time_interval_playing_ms >= 1000) {
		                    	Application_2D_Base.getInstance().getGameData().addAccumulated_time_inmainscreen(timeNow - buffer_time_interval_playing_ms); 
		                    	buffer_time_interval_playing_ms = timeNow;
		                    }
	                    }
	                    
	                } finally {
	                    if (c != null) {
	                    	_surfaceHolder.unlockCanvasAndPost(c);
	                    }
	                }
            	} catch(Exception e) {
            		//Continue to update the world and draw it.
            		//In rare cases there is a java.lang.IllegalArgumentException at android.view.Surface.nativeUnlockCanvasAndPost (Surface.java)
            		e.printStackTrace();
            	}
            }
        }
	}
}
