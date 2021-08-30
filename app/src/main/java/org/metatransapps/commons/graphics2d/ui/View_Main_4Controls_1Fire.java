package org.metatransapps.commons.graphics2d.ui;


import org.metatransapps.commons.ui.utils.ScreenUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;


@SuppressLint("ViewConstructor")
public class View_Main_4Controls_1Fire extends View_Surface_Base {
	
	
	private boolean initialized = false;
	
	protected Paint default_paint;
	
	protected RectF rectf_main;
	
	private float control_move_X;
	private float control_move_Y;
	private float control_move_R;
	private float[] control_move_vector = new float[2];
	
	private float control_button1_X;
	private float control_button1_Y;
	private float control_button1_R;
	
	
	public View_Main_4Controls_1Fire(Context context) {
		
		super(context);
		
		
		default_paint = new Paint();
		//default_paint.setColor(Color.BLACK);
		
		rectf_main = new RectF();
		
		//System.out.println("New View_Main_4Controls_1Fire created: " + this);
		
		//setOnTouchListener(this);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (!initialized) {
			
			initializeDimensions();
			
			initialized = true;
		}
		
		this.setMeasuredDimension( (int) (rectf_main.right - rectf_main.left), (int) (rectf_main.bottom - rectf_main.top) );
	}
	
	
	private void initializeDimensions() {
		
		int[] screen_size = ScreenUtils.getScreenSize((Activity) getContext());
		int main_width = Math.max(screen_size[0], screen_size[1]);
		int main_height = Math.min(screen_size[0], screen_size[1]);
		
		//int main_width = Math.max(getMeasuredWidth(), getMeasuredHeight());
		//int main_height = Math.min(getMeasuredWidth(), getMeasuredHeight());

		//int main_width = getMeasuredWidth();
		//int main_height = getMeasuredHeight();
		
		rectf_main.left = 0;
		rectf_main.top = 0;
		rectf_main.right = main_width;
		rectf_main.bottom = main_height;
	
		control_move_R = Math.min((rectf_main.right - rectf_main.left) / 5, (rectf_main.bottom - rectf_main.top) / 5);
		control_move_X = control_move_R + 10;
		control_move_Y = rectf_main.bottom - (control_move_R + 10);
		
		control_button1_R = Math.min((rectf_main.right - rectf_main.left) / 10, (rectf_main.bottom - rectf_main.top) / 10);
		control_button1_X = rectf_main.right - (control_button1_R + 10);
		control_button1_Y = rectf_main.bottom - (control_button1_R + 10);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		//Draw Controls
		default_paint.setColor(Color.BLUE);
		default_paint.setAlpha(77);
		
		canvas.drawCircle(control_move_X,	control_move_Y, control_move_R,	default_paint);
		
		canvas.drawCircle(control_button1_X,	control_button1_Y, control_button1_R,	default_paint);
	}
	
	
	private void fillControlVector(float x, float y) {
		
		//In general, x and y must satisfy (x - center_x)^2 + (y - center_y)^2 < radius^2
		float dx = x - control_move_X;
		float dy = y - control_move_Y;
		float norm = 7;//7
		if (Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_move_R, 2)) {
			control_move_vector[0] = dx / norm;
			control_move_vector[1] = dy / norm;
			
			//System.out.println("control_vector[0]=" + control_move_vector[0] + ", control_vector[1]=" + control_move_vector[1]);
		}
	}
	
	
	private boolean isInsideShot(float x, float y) {
		float dx = x - control_button1_X;
		float dy = y - control_button1_Y;
		return Math.pow(dx, 2) + Math.pow(dy, 2) < Math.pow(control_button1_R, 2);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		synchronized (this) {
			
			//int action = event.getAction();
			int action = event.getActionMasked() & MotionEvent.ACTION_MASK;
			//System.out.println("action=" + action);
			
			if (action == MotionEvent.ACTION_DOWN
					|| action == MotionEvent.ACTION_POINTER_DOWN) {

				processEvent_DOWN(event);

			} else if (action == MotionEvent.ACTION_MOVE) {
				
				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {

				processEvent_UP(event);

			}
		}
		
		return true;
	}
	
	
	private void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
		
		if (isInsideShot(x, y)) {
			
			getWorld().button1(1, 1);
			
			//System.out.println("SHOT");
		}
	}
	
	
	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		fillControlVector(x, y);
		if (control_move_vector[0] != 0f || control_move_vector[1] != 0f) {
			getWorld().setPlayerSpeed(control_move_vector[0], control_move_vector[1]);
		}
	}
}
