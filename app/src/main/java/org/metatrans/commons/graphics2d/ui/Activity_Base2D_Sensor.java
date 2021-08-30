package org.metatrans.commons.graphics2d.ui;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


public abstract class Activity_Base2D_Sensor extends Activity_Base2D implements SensorEventListener {
	
	
	private SensorManager mSensorManager;
	private float xy_angle;
    private float xz_angle;
    //private float zy_angle;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}
	
	
	@Override
    public void onSensorChanged(SensorEvent event) {
		
        synchronized (this) {
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    xy_angle = event.values[0];
                    xz_angle = event.values[1];
                    //zy_angle = event.values[2];
                    break;
            }
            
            //Log.d(TAG, "xy_angle:"+xy_angle+", xz_angle"+xz_angle+", zy_angle"+zy_angle);
 
            if (xy_angle < -2){
                Log.d("Activity_Base2D", "up");
                //world.setPlayerSpeed(0, -1);
                //getWorld().setPlayerSpeed(0, xy_angle);
            }
            if (xz_angle > 2){
            	Log.d("Activity_Base2D", "right");
            	//world.setPlayerSpeed(+1, 0);
            	//getWorld().setPlayerSpeed(xz_angle, 0);
            }
            if (xz_angle < -2) {
            	Log.d("Activity_Base2D", "left");
            	//world.setPlayerSpeed(-1, 0);
            	//getWorld().setPlayerSpeed(xz_angle, 0);
            }
            if (xy_angle > 2)   {
            	Log.d("Activity_Base2D", "down");
            	//world.setPlayerSpeed(0, +1);
            	//getWorld().setPlayerSpeed(0, xy_angle);
            }
            
        }
    }

	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
