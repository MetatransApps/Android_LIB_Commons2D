package com.apps.mobile.android.commons.graphics2d.ui;


import com.apps.mobile.android.commons.graphics2d.app.Application_2D_Base;
import com.apps.mobile.android.commons.graphics2d.main.Activity_Main_Base2D;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;


@SuppressLint("ViewConstructor")
public abstract class View_Main_4Controls_4Fire extends View_Main_Base {
	
	
	private float control_move_X;
	private float control_move_Y;
	private float control_move_R;
	private float control_move_X_touch;
	private float control_move_Y_touch;
	
	private float control_button1_X;
	private float control_button1_Y;
	private float control_button1_R;
	
	
	private RectF control_player_bitmap;
	private RectF control_shot_bitmap;
	
	
	private float[] control_move_vector = new float[2];
	private float[] control_shot_vector = new float[2];
	
	
	public View_Main_4Controls_4Fire(Activity_Main_Base2D activity) {
		super(activity);
	}
	
	
	@Override
	protected void initializeDimensions() {
		
		super.initializeDimensions();
		
		float delim1 = 5.95f;
		float delim2 = 7.95f;
		
		control_move_R = Math.min((getRectangle_Main().right - getRectangle_Main().left) / delim1, (getRectangle_Main().bottom - getRectangle_Main().top) / delim1);
		control_button1_R = Math.min((getRectangle_Main().right - getRectangle_Main().left) / delim2, (getRectangle_Main().bottom - getRectangle_Main().top) / delim2);
		
		float control_margin = (4 * Math.min(control_move_R, control_button1_R)) / 5;
		
		control_move_X = getRectangle_Main().right - (control_button1_R + control_margin);
		control_move_Y = getRectangle_Main().bottom - (control_button1_R + control_margin);
		
		control_move_X_touch = control_move_X;
		control_move_Y_touch = control_move_Y;
		
		control_button1_X = control_move_R + control_margin;
		control_button1_Y = getRectangle_Main().bottom - (control_move_R + control_margin);
		
		
		float bitmap1_size = control_move_R / 2;
		float bitmap2_size = (2 * control_button1_R) / 3;
		control_player_bitmap = new RectF(control_move_X - bitmap1_size, control_move_Y - bitmap1_size, control_move_X + bitmap1_size, control_move_Y + bitmap1_size);
		control_shot_bitmap = new RectF(control_button1_X - bitmap2_size, control_button1_Y - bitmap2_size, control_button1_X + bitmap2_size, control_button1_Y + bitmap2_size);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		if (Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			
			//Draw Controls
			getDefaultPaint().setColor(Color.BLUE);
			getDefaultPaint().setAlpha(99);
			canvas.drawCircle(control_move_X,		control_move_Y, control_move_R,	getDefaultPaint());
			canvas.drawCircle(control_button1_X,	control_button1_Y, control_button1_R,	getDefaultPaint());
			
			///getDefaultPaint().setColor(Color.BLUE);
			//getDefaultPaint().setAlpha(11);
			//canvas.drawCircle(control_move_X,		control_move_Y, control_move_R - 5,	getDefaultPaint());
			//canvas.drawCircle(control_button1_X,	control_button1_Y, control_button1_R - 5,	getDefaultPaint());
			
			getDefaultPaint().setAlpha(137);
			canvas.drawBitmap(getBitmapControl_Player(), 	null, control_player_bitmap, getDefaultPaint());
			canvas.drawBitmap(getBitmapControl_Shot(), 		null, control_shot_bitmap, getDefaultPaint());
			
			//if (control_move_X_touch != -1 && control_move_Y_touch != -1) {
				
				getDefaultPaint().setColor(Color.WHITE | Color.BLUE);
				getDefaultPaint().setAlpha(177);
				//getDefaultPaint().setColor(Color.BLUE);
				//getDefaultPaint().setAlpha(255);
				canvas.drawCircle(control_move_X_touch,		control_move_Y_touch, 	control_move_R / 10,	getDefaultPaint());
			//}
		}
	}
	
	
	protected abstract Bitmap getBitmapControl_Player();
	protected abstract Bitmap getBitmapControl_Shot();
	
	
	private void fillControlVector(float x, float y) {
		
		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_move_X;
		float dy = y - control_move_Y;
		if (Math.pow(dx, 2) + Math.pow(dy, 2) < 2 * Math.pow(control_move_R, 2)) {
			
			control_move_vector[0] = Math.min(1f, dx / control_move_R);
			control_move_vector[1] = Math.min(1f, dy / control_move_R);
			
			control_move_X_touch = x;
			control_move_Y_touch = y;
			
			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}
	
	
	private void fillShotVector(float x, float y) {
		
		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_button1_X;
		float dy = y - control_button1_Y;
		if (Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_button1_R, 2)) {
			control_shot_vector[0] = dx / control_button1_R;
			control_shot_vector[1] = dy / control_button1_R;
			
			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}
	
	
	private boolean isInsideShot(float x, float y) {
		float dx = x - control_button1_X;
		float dy = y - control_button1_Y;
		return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_button1_R, 2);
	}
	
	
	@Override
	protected void processEvent_DOWN(MotionEvent event) {
		
		super.processEvent_DOWN(event);
		
		if (!Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			return;
		}
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
		
		if (isInsideShot(x, y)) {
			
			fillShotVector(x, y);
			
			getWorld().button1(control_shot_vector[0], control_shot_vector[1]);
			
			//System.out.println("SHOT");
		}
	}
	
	
	@Override
	protected void processEvent_MOVE(MotionEvent event) {
		
		super.processEvent_MOVE(event);
		
		if (!Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			return;
		}
		
		float x = event.getX();
		float y = event.getY();
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
	}
	
	
	@Override
	protected void processEvent_UP(MotionEvent event) {
		
		super.processEvent_UP(event);
		
		if (!Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			return;
		}
		
		float x = event.getX();
		float y = event.getY();
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
	}
}
