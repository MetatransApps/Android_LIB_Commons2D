package org.metatrans.commons.graphics2d.ui;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.main.Activity_Main_Base2D;
import org.metatrans.commons.graphics2d.menu.IConfigurationJoystick;
import org.metatrans.commons.graphics2d.model.UserSettings;
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
	private float control_joystick_left_X_touch;
	private float control_joystick_left_Y_touch;


	private RectF rect_control_image_right;
	private RectF rect_control_image_left;
	private RectF rect_control_arrows_left;
	private RectF rect_control_arrows_right;

	private Bitmap control_arrows_bitmap;


	private float[] control_move_vector = new float[2];
	private float[] control_shot_vector = new float[2];
	
	
	public View_Main_4Controls_4Fire(Activity_Main_Base2D activity) {
		super(activity);
	}
	
	
	@Override
	protected void initializeDimensions() {
		
		super.initializeDimensions();

		float joystick_part = 5.5f;//8f;
		
		control_joystick_right_R = Math.min((getRectangle_Main().right - getRectangle_Main().left) / joystick_part, (getRectangle_Main().bottom - getRectangle_Main().top) / joystick_part);
		control_joystick_left_R = control_joystick_right_R;
		
		float controls_margin = (2 * Math.min(control_joystick_right_R, control_joystick_left_R)) / 6;
		
		control_joystick_right_X = getRectangle_Main().right - (control_joystick_left_R + controls_margin);
		control_joystick_right_Y = getRectangle_Main().bottom - (control_joystick_right_R + controls_margin);

		control_joystick_left_X = control_joystick_right_R + controls_margin;
		control_joystick_left_Y = control_joystick_right_Y;

		control_joystick_right_X_touch = control_joystick_right_X;
		control_joystick_right_Y_touch = control_joystick_right_Y;

		control_joystick_left_X_touch = control_joystick_left_X;
		control_joystick_left_Y_touch = control_joystick_left_Y;


		float bitmap1_size = (1 * control_joystick_right_R) / 2;
		float bitmap2_size = (1 * control_joystick_left_R) / 2;
		float bitmap3_size = (1 * control_joystick_left_R) / 1;

		rect_control_image_right = new RectF(control_joystick_right_X - bitmap1_size, control_joystick_right_Y_touch - bitmap1_size, control_joystick_right_X + bitmap1_size, control_joystick_left_Y + bitmap1_size);
		rect_control_image_left = new RectF(control_joystick_left_X - bitmap2_size, control_joystick_left_Y - bitmap2_size, control_joystick_left_X + bitmap2_size, control_joystick_left_Y + bitmap2_size);

		rect_control_arrows_left = new RectF(control_joystick_left_X - bitmap3_size, control_joystick_left_Y - bitmap3_size, control_joystick_left_X + bitmap3_size, control_joystick_left_Y + bitmap3_size);
		rect_control_arrows_right = new RectF(control_joystick_right_X - bitmap3_size, control_joystick_right_Y - bitmap3_size, control_joystick_right_X + bitmap3_size, control_joystick_right_Y + bitmap3_size);

		//control_arrows_bitmap = BitmapUtils.fromResource(Application_Base.getInstance(), R.drawable.joystick_transparent);
		control_arrows_bitmap = BitmapUtils.fromResource(Application_Base.getInstance(), R.drawable.joystick_full);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		if (Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {
			
			//Draw Controls
			getDefaultPaint().setColor(Color.BLUE);
			getDefaultPaint().setAlpha(30);
			//canvas.drawCircle(control_joystick_right_X, control_joystick_right_Y, control_joystick_right_R,	getDefaultPaint());
			//canvas.drawCircle(control_joystick_left_X,	control_joystick_left_Y, control_joystick_left_R,	getDefaultPaint());

			getDefaultPaint().setAlpha(70);
			canvas.drawBitmap(getBitmapControl_Arrows(), 	null, rect_control_arrows_left, getDefaultPaint());
			canvas.drawBitmap(getBitmapControl_Arrows(), 	null, rect_control_arrows_right, getDefaultPaint());

			getDefaultPaint().setAlpha(90);

			if (getMoveJoystickID() == IConfigurationJoystick.MOVE_RIGHTJOYSTICK) {

				canvas.drawBitmap(getBitmapControl_Player(), null, rect_control_image_right, getDefaultPaint());
				canvas.drawBitmap(getBitmapControl_Shot(), null, rect_control_image_left, getDefaultPaint());

				//Draw point where the screen is touched
				getDefaultPaint().setColor(Color.WHITE | Color.BLUE);
				getDefaultPaint().setAlpha(90);
				canvas.drawCircle(control_joystick_right_X_touch, control_joystick_right_Y_touch, control_joystick_right_R / 10, getDefaultPaint());

			} else if (getMoveJoystickID() == IConfigurationJoystick.MOVE_LEFTJOYSTICK) {

				canvas.drawBitmap(getBitmapControl_Shot(), 	null, rect_control_image_right, getDefaultPaint());
				canvas.drawBitmap(getBitmapControl_Player(), null, rect_control_image_left, getDefaultPaint());

				//Draw point where the screen is touched
				getDefaultPaint().setColor(Color.WHITE | Color.BLUE);
				getDefaultPaint().setAlpha(90);
				canvas.drawCircle(control_joystick_left_X_touch, control_joystick_left_Y_touch, 	control_joystick_left_R / 10,	getDefaultPaint());

			} else {

				throw new IllegalStateException("getMoveJoystickID()=" + getMoveJoystickID());
			}
		}
	}
	
	
	protected abstract Bitmap getBitmapControl_Player();


	protected abstract Bitmap getBitmapControl_Shot();


	protected Bitmap getBitmapControl_Arrows() {

		return control_arrows_bitmap;
	}


	private int getMoveJoystickID() {

		return ((UserSettings) Application_Base.getInstance().getUserSettings()).movejoystick_side;
	}


	private void fillVector_RightJoystick(float x, float y) {

		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_joystick_right_X;
		float dy = y - control_joystick_right_Y;

		if (Math.pow(dx, 2) + Math.pow(dy, 2) < 4 * Math.pow(control_joystick_right_R, 2)) {

			if (getMoveJoystickID() == IConfigurationJoystick.MOVE_RIGHTJOYSTICK) {

				control_move_vector[0] = Math.min(1f, dx / control_joystick_right_R);
				control_move_vector[1] = Math.min(1f, dy / control_joystick_right_R);

			} else if (getMoveJoystickID() == IConfigurationJoystick.MOVE_LEFTJOYSTICK) {

				control_shot_vector[0] = Math.min(1f, dx / control_joystick_right_R);
				control_shot_vector[1] = Math.min(1f, dy / control_joystick_right_R);

			} else {

				throw new IllegalStateException("getMoveJoystickID()=" + getMoveJoystickID());
			}

			control_joystick_right_X_touch = x;
			control_joystick_right_Y_touch = y;

			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}


	private void fillVector_LeftJoystick(float x, float y) {

		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_joystick_left_X;
		float dy = y - control_joystick_left_Y;

		if (Math.pow(dx, 2) + Math.pow(dy, 2) < 4 * Math.pow(control_joystick_left_R, 2)) {

			if (getMoveJoystickID() == IConfigurationJoystick.MOVE_RIGHTJOYSTICK) {

				control_shot_vector[0] = dx / control_joystick_left_R;
				control_shot_vector[1] = dy / control_joystick_left_R;

			} else if (getMoveJoystickID() == IConfigurationJoystick.MOVE_LEFTJOYSTICK) {

				control_move_vector[0] = Math.min(1f, dx / control_joystick_right_R);
				control_move_vector[1] = Math.min(1f, dy / control_joystick_right_R);

			} else {

				throw new IllegalStateException();
			}

			control_joystick_left_X_touch = x;
			control_joystick_left_Y_touch = y;

			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}


	@Override
	protected void processEvent_DOWN(MotionEvent event) {
		
		super.processEvent_DOWN(event);
		
		if (!Application_2D_Base.getInstance().isCurrentlyGameActiveIntoTheMainScreen()) {

			return;
		}
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());


		control_shot_vector[0] = 0;
		control_shot_vector[1] = 0;

		fillVector_RightJoystick(x, y);

		fillVector_LeftJoystick(x, y);


		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {

			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}

		if (control_shot_vector[0] != 0f || control_shot_vector[1] != 0f) {

			getWorld().button1(control_shot_vector[0], control_shot_vector[1]);
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
		

		if (getMoveJoystickID() == IConfigurationJoystick.MOVE_RIGHTJOYSTICK) {

			fillVector_RightJoystick(x, y);

		} else if (getMoveJoystickID() == IConfigurationJoystick.MOVE_LEFTJOYSTICK) {

			fillVector_LeftJoystick(x, y);

		} else {

			throw new IllegalStateException();
		}

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

		if (getMoveJoystickID() == IConfigurationJoystick.MOVE_RIGHTJOYSTICK) {

			fillVector_RightJoystick(x, y);

		} else if (getMoveJoystickID() == IConfigurationJoystick.MOVE_LEFTJOYSTICK) {

			fillVector_LeftJoystick(x, y);

		} else {

			throw new IllegalStateException();
		}

		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
	}
}
