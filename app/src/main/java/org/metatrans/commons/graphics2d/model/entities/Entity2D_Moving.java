package org.metatrans.commons.graphics2d.model.entities;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.graphics2d.model.BitmapCache_Base;
import org.metatrans.commons.graphics2d.model.World;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;


public abstract class Entity2D_Moving extends Entity2D_Base {


	private static final long serialVersionUID = 5936821405527936582L;


	private float WORLD_SIZE_X;
	private float WORLD_SIZE_Y;

	private float dx;
	private float dy;

	private List<? extends IEntity2D> blockerEntities;
	private List<? extends IEntity2D> killerEntities;

	private transient RectF test_newposition;

	private List<IEntity2D> blockers_tester;


	protected int bitmap_id;
	protected int bitmap_id_backup;
	protected transient Bitmap bitmap_org;

	protected int cur_bitmap_rotation_degrees;
	protected int cur_bitmap_rotation_step 				= 15;
	protected transient Bitmap[] bitmap_rotated_cache 	= new Bitmap[360 / cur_bitmap_rotation_step + 1];

	protected volatile boolean removed;

	private SecureRandom random1;
	private SecureRandom random2;


	public Entity2D_Moving(World _world, RectF _evelop, int _subtype,
						   List<? extends IEntity2D> _blockerEntities,
						   List<? extends IEntity2D> _killerEntities,
						   int _bitmap_id,
						   int _rotation_angle_in_degrees) {

		super(_world, _evelop, _subtype);

		blockerEntities = _blockerEntities;
		killerEntities = _killerEntities;

		blockers_tester = new ArrayList<IEntity2D>();

		bitmap_id = _bitmap_id;
		cur_bitmap_rotation_degrees = _rotation_angle_in_degrees;
	}


	protected void setRotationStep(int step) {

		cur_bitmap_rotation_step = step;

		if (cur_bitmap_rotation_step == 0) {

			bitmap_rotated_cache = new Bitmap[1];

		} else {

			bitmap_rotated_cache = new Bitmap[360 / Math.abs(cur_bitmap_rotation_step) + 1];
		}
	}


	public boolean isRemoved() {

		return removed;
	}


	public int getRotationDegrees() {

		return cur_bitmap_rotation_degrees;
	}


	protected void rotate() {

		cur_bitmap_rotation_degrees += cur_bitmap_rotation_step;

		if (cur_bitmap_rotation_degrees < 0) {

			cur_bitmap_rotation_degrees = 360 + cur_bitmap_rotation_degrees;
		}

		cur_bitmap_rotation_degrees = cur_bitmap_rotation_degrees % 360;
	}


	@Override
	public Bitmap getBitmap() {

		if (bitmap_id != bitmap_id_backup || bitmap_org == null) {

			bitmap_id_backup = bitmap_id;

			bitmap_org = getWorld().getBitmapCache().get(bitmap_id);

			setRotationStep(cur_bitmap_rotation_step);
		}

		if (bitmap_org == null) {

			return null;
		}

		int bitmap_index = Math.abs(cur_bitmap_rotation_degrees) / Math.max(1, Math.abs(cur_bitmap_rotation_step));

		if (bitmap_rotated_cache == null) {

			setRotationStep(cur_bitmap_rotation_step);
		}

		if (bitmap_rotated_cache[bitmap_index] == null) {

			int rotation_in_degrees = 360 - cur_bitmap_rotation_degrees;
			int rotated_bitmap_id = 1000 * bitmap_id + rotation_in_degrees;

			Bitmap bitmap_rotated = getWorld().getBitmapCache().getRotated(rotated_bitmap_id, rotation_in_degrees);

			if (bitmap_rotated == null) {

				Matrix matrix = new Matrix();

				matrix.postRotate(rotation_in_degrees);

				bitmap_rotated = Bitmap.createBitmap(bitmap_org, 0, 0, bitmap_org.getWidth(), bitmap_org.getHeight(), matrix, true);

				((BitmapCache_Base) getWorld().getBitmapCache()).add(rotated_bitmap_id, bitmap_rotated);
			}

			bitmap_rotated_cache[bitmap_index] = bitmap_rotated;
		}

		Bitmap bitmap_rotated = bitmap_rotated_cache[bitmap_index];

		return bitmap_rotated;
	}


	@Override
	public void draw(Canvas c) {

		//super.draw(c);

		if (drawContourCircle()) {

			/*if (random1 == null) {

				random1 = new SecureRandom();
			}

			getPaint().setColor(getRandomColor(random1));
			*/

			getPaint().setColor(Color.argb(128, 255, 255, 255));
			getPaint().setStyle(Paint.Style.STROKE);
			getPaint().setStrokeWidth(9);

			c.drawCircle(getEnvelop_ForDraw().left + (getEnvelop_ForDraw().right - getEnvelop_ForDraw().left) / 2,
					getEnvelop_ForDraw().top + (getEnvelop_ForDraw().bottom - getEnvelop_ForDraw().top) / 2,
					1.3f * (float) Math.sqrt(getEnvelop_ForDraw().width() * getEnvelop_ForDraw().width() + getEnvelop_ForDraw().height() * getEnvelop_ForDraw().height()) / 2,
					getPaint());
		}


		Bitmap bitmap = getBitmap();

		if (bitmap_id == -1 || bitmap == null) {

			if (random2 == null) {

				random2 = new SecureRandom();
			}

			getPaint().setColor(getRandomColor(random2));
			getPaint().setStyle(Paint.Style.FILL);

			c.drawCircle(getEnvelop_ForDraw().left + (getEnvelop_ForDraw().right - getEnvelop_ForDraw().left) / 2,
					getEnvelop_ForDraw().top + (getEnvelop_ForDraw().bottom - getEnvelop_ForDraw().top) / 2,
					(getEnvelop_ForDraw().right - getEnvelop_ForDraw().left) / 2,
					getPaint());


		} else {

			RectF envelop = getEnvelop_ForDraw();

			if (envelop != null) {

				c.drawBitmap(bitmap, null, envelop, null);
			}
		}
	}


	private int getRandomColor(SecureRandom random) {

		int rand = random.nextInt();
		byte[] bytes = intToByteArray(rand);
		return Color.argb(bytes[0], bytes[1], bytes[2], bytes[3]);
	}


	byte[] intToByteArray(int data) {

		byte[] bytes = new byte[4];

		bytes[0] = (byte) ((data & 0xFF000000) >> 24);
		bytes[1] = (byte) ((data & 0x00FF0000) >> 16);
		bytes[2] = (byte) ((data & 0x0000FF00) >> 8);
		bytes[3] = (byte) ((data & 0x000000FF) >> 0);

		return bytes;
	}


	protected boolean drawContourCircle() {

		return false;
	}


	@Override
	public int getType() {
		return TYPE_MOVING;
	}


	public void setWorldSize(float X, float Y) {
		WORLD_SIZE_X = X;
		WORLD_SIZE_Y = Y;
	}


	public void setSpeed(float _dx, float _dy) {
		dx = _dx;
		dy = _dy;
	}


	public float getDx() {
		return dx;
	}


	public float getDy() {
		return dy;
	}


	public void clearDx() {
		dx = 0f;
	}


	public void clearDy() {
		dy = 0f;
	}


	public int getTestIterations() {
		return 1;
	}


	//static float all;
	//static float col;

	public void nextMoment(float takts) {

		//all++;

		if (killerEntities != null) {
			for (IEntity2D killer : killerEntities) {
				if (RectF.intersects(getEnvelop(), killer.getEnvelop())) {
					killed((Entity2D_Moving) killer);
					return;
				}
			}
		}


		//test x and y axis
		float new_DX = 0f;
		float new_DY = 0f;

		int test_iterations = getTestIterations();

		//test dx
		if (getDx() != 0f) {

			float incDX = (takts * getDx()) / (float) test_iterations;

			for (int i = 0; i < test_iterations; i++) {

				new_DX += incDX;

				getTest_newposition().set(getEnvelop());
				getTest_newposition().offset(new_DX, new_DY);

				boolean ok = notIntersectsWithBlockers(getTest_newposition());

				if (!ok) {

					new_DX -= incDX;

					if (new_DX == 0f) {
						groundContact_X();
						//col++;
					}

					break;
				}
			}
		}

		//test dy
		if (getDy() != 0f) {

			float incDY = (takts * getDy()) / (float) test_iterations;

			for (int i = 0; i < test_iterations; i++) {

				new_DY += incDY;

				getTest_newposition().set(getEnvelop());
				getTest_newposition().offset(new_DX, new_DY);

				boolean ok = notIntersectsWithBlockers(getTest_newposition());

				if (!ok) {

					new_DY -= incDY;

					if (new_DY == 0f) {
						groundContact_Y();
						//col++;
					}

					break;
				}
			}
		}


		//System.out.println("RATE: " + (col/(float)all));

		//test inside world
		if (getEnvelop().left + new_DX < 0) {
			new_DX = -getEnvelop().left;
			clearDx();
			groundContact_X();
		}
		if (getEnvelop().right + new_DX > WORLD_SIZE_X) {
			new_DX = WORLD_SIZE_X - getEnvelop().right;
			clearDx();
			groundContact_X();
		}
		if (getEnvelop().top + new_DY < 0) {
			new_DY = -getEnvelop().top;
			clearDy();
			groundContact_Y();
		}
		if (getEnvelop().bottom + new_DY > WORLD_SIZE_Y) {
			new_DY = WORLD_SIZE_Y - getEnvelop().bottom;
			clearDy();
			groundContact_Y();
		}


		if (new_DX != 0f || new_DY != 0f) {

			getEnvelop().offset(new_DX, new_DY);
			
			/*
			movingHistory.add(new PointF(new_DX, new_DY));
			if (movingHistory.size() > movingHistorySize) {
				movingHistory.remove(0);
			}
			*/
		}

		rotate();
	}
	
	
	/*
	protected float getDx_History() {
		float result = 0;
		for (PointF cur: movingHistory) {
			result += cur.x;
		}
		return result / (float) movingHistorySize;
	}
	
	
	protected float getDy_History() {
		float result = 0;
		for (PointF cur: movingHistory) {
			result += cur.y;
		}
		return result / (float) movingHistorySize;
	}
	*/


	protected void groundContact_X() {
		//System.out.println(this + " groundContact_X");
	}


	protected void groundContact_Y() {
		//System.out.println(this + " groundContact_Y");
	}


	protected void killed(Entity2D_Moving killer) {
		//System.out.println(this + " groundContact_Y");
	}


	private boolean notIntersectsWithBlockers(RectF test) {

		blockers_tester.clear();

		getWorld().getBlockersSet().intersect(blockers_tester, test, true);

		return blockers_tester.size() == 0;
	}


	protected List<? extends IEntity2D> getBlockerEntities() {
		return blockerEntities;
	}


	protected RectF getTest_newposition() {

		if (test_newposition == null) {
			test_newposition = new RectF();
		}
		return test_newposition;
	}
}