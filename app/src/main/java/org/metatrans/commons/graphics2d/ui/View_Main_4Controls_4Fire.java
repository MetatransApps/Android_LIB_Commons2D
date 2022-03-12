package org.metatrans.commons.graphics2d.ui;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.main.Activity_Main_Base2D;
import org.metatrans.commons.ui.utils.BitmapUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.commons2d.R;


public abstract class View_Main_4Controls_4Fire extends View_Main_Base {
	
	
	private float control_joystick_right_X;
	private float control_joystick_right_R;
	private float control_joystick_right_Y;
	private float control_joystick_right_X_touch;
	private float control_joystick_right_Y_touch;
	
	private float control_joystick_left_X;
	private float control_joystick_left_R;
	private float control_joystick_left_Y;

	
	private RectF control_player_bitmap;
	private RectF control_shot_bitmap;
	private RectF control_arrows_bitmap_1;
	private RectF control_arrows_bitmap_2;

	private Bitmap control_arrows;


	private float[] control_move_vector = new float[2];
	private float[] control_shot_vector = new float[2];
	
	
	public View_Main_4Controls_4Fire(Activity_Main_Base2D activity) {
		super(activity);
	}
	
	
	@Override
	protected void initializeDimensions() {
		
		super.initializeDimensions();
		
		float delim1 = 5.95f;
		//float delim2 = 7.95f;
		
		control_joystick_right_R = Math.min((getRectangle_Main().right - getRectangle_Main().left) / delim1, (getRectangle_Main().bottom - getRectangle_Main().top) / delim1);
		control_joystick_left_R = Math.min((getRectangle_Main().right - getRectangle_Main().left) / delim1, (getRectangle_Main().bottom - getRectangle_Main().top) / delim1);
		
		float control_margin = (4 * Math.min(control_joystick_right_R, control_joystick_left_R)) / 5;
		
		control_joystick_right_X = getRectangle_Main().right - (control_joystick_left_R + control_margin);
		control_joystick_left_Y = getRectangle_Main().bottom - (control_joystick_left_R + control_margin);
		control_joystick_right_Y = getRectangle_Main().bottom - (control_joystick_right_R + control_margin);

		control_joystick_right_X_touch = control_joystick_right_X;
		control_joystick_right_Y_touch = control_joystick_left_Y;

		control_joystick_left_X = control_joystick_right_R + control_margin;

		
		float bitmap1_size = (1 * control_joystick_right_R) / 2;
		float bitmap2_size = (1 * control_joystick_left_R) / 2;
		float bitmap3_size = (1 * control_joystick_left_R) / 1;

		control_player_bitmap = new RectF(control_joystick_right_X - bitmap1_size, control_joystick_right_Y_touch - bitmap1_size, control_joystick_right_X + bitmap1_size, control_joystick_left_Y + bitmap1_size);
		control_shot_bitmap = new RectF(control_joystick_left_X - bitmap2_size, control_joystick_left_Y - bitmap2_size, control_joystick_left_X + bitmap2_size, control_joystick_left_Y + bitmap2_size);

		control_arrows_bitmap_1 = new RectF(control_joystick_left_X - bitmap3_size, control_joystick_left_Y - bitmap3_size, control_joystick_left_X + bitmap3_size, control_joystick_left_Y + bitmap3_size);
		control_arrows_bitmap_2 = new RectF(control_joystick_right_X - bitmap3_size, control_joystick_right_Y - bitmap3_size, control_joystick_right_X + bitmap3_size, control_joystick_right_Y + bitmap3_size);

		control_arrows = BitmapUtils.fromResource(Application_Base.getInstance(), R.drawable.joystick_transparent);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		if (Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			
			//Draw Controls
			getDefaultPaint().setColor(Color.BLUE);
			getDefaultPaint().setAlpha(61);
			canvas.drawCircle(control_joystick_right_X, control_joystick_right_Y, control_joystick_right_R,	getDefaultPaint());
			canvas.drawCircle(control_joystick_left_X,	control_joystick_left_Y, control_joystick_left_R,	getDefaultPaint());

			getDefaultPaint().setAlpha(99);
			canvas.drawBitmap(getBitmapControl_Arrows(), 	null, control_arrows_bitmap_1, getDefaultPaint());
			canvas.drawBitmap(getBitmapControl_Arrows(), 	null, control_arrows_bitmap_2, getDefaultPaint());

			getDefaultPaint().setAlpha(137);
			canvas.drawBitmap(getBitmapControl_Player(), 	null, control_player_bitmap, getDefaultPaint());
			canvas.drawBitmap(getBitmapControl_Shot(), 		null, control_shot_bitmap, getDefaultPaint());

			//Draw point where the screen is touched
			getDefaultPaint().setColor(Color.WHITE | Color.BLUE);
			getDefaultPaint().setAlpha(137);
			canvas.drawCircle(control_joystick_right_X_touch, control_joystick_right_Y_touch, 	control_joystick_right_R / 10,	getDefaultPaint());
		}
	}
	
	
	protected abstract Bitmap getBitmapControl_Player();

	protected abstract Bitmap getBitmapControl_Shot();

	protected Bitmap getBitmapControl_Arrows() {

		return control_arrows;
	}
	
	
	private void fillControlVector(float x, float y) {
		
		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_joystick_right_X;
		float dy = y - control_joystick_left_Y;
		if (Math.pow(dx, 2) + Math.pow(dy, 2) < 2 * Math.pow(control_joystick_right_R, 2)) {
			
			control_move_vector[0] = Math.min(1f, dx / control_joystick_right_R);
			control_move_vector[1] = Math.min(1f, dy / control_joystick_right_R);
			
			control_joystick_right_X_touch = x;
			control_joystick_right_Y_touch = y;
			
			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}
	
	
	private void fillShotVector(float x, float y) {
		
		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_joystick_left_X;
		float dy = y - control_joystick_left_Y;
		if (Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_joystick_left_R, 2)) {
			control_shot_vector[0] = dx / control_joystick_left_R;
			control_shot_vector[1] = dy / control_joystick_left_R;
			
			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}
	
	
	private boolean isInsideShot(float x, float y) {
		float dx = x - control_joystick_left_X;
		float dy = y - control_joystick_left_Y;
		return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_joystick_left_R, 2);
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
