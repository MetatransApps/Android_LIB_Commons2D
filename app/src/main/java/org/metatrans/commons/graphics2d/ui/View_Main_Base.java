package org.metatrans.commons.graphics2d.ui;


import org.metatrans.commons.Alerts_Base;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.engagement.leaderboards.View_Achievements_And_Leaderboards_Base;
import org.metatrans.commons.graphics2d.app.Application_2D_Base;
import org.metatrans.commons.graphics2d.main.Activity_Main_Base2D;
import org.metatrans.commons.ui.ButtonAreaClick_Image;
import org.metatrans.commons.ui.IButtonArea;
import org.metatrans.commons.ui.TextArea;
import org.metatrans.commons.ui.utils.BitmapUtils;
import org.metatrans.commons.ui.utils.ScreenUtils;

import com.commons2d.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;


public abstract class View_Main_Base extends View_Surface_Base {
	
	
	private boolean initialized = false;
	
	
	private RectF rectf_main;
	private RectF rectf_button_pause;
	private RectF rectf_button_menu;
	private RectF rectf_button_new_small;
	
	private RectF rectf_button_center;
	private RectF rectf_button_center_bottom;
	
	private RectF rectf_top_center;
	private RectF rectf_top_center_star1;
	private RectF rectf_top_center_star2;
	private RectF rectf_top_center_star3;
	
	private IButtonArea button_pause;
	private IButtonArea button_resume;
	private IButtonArea button_replay;
	private IButtonArea button_menu;
	private IButtonArea button_new_small;
	private IButtonArea button_new_center;
	private IButtonArea button_next_level;
	private IButtonArea textarea_top_center1_gameover;
	
	private static Bitmap bitmap_pause;
	private static Bitmap bitmap_play;
	private static Bitmap bitmap_replay;
	private static Bitmap bitmap_new;
	private static Bitmap bitmap_menu;
	private static Bitmap bitmap_next;
	private static Bitmap bitmap_star;
	private static Bitmap bitmap_star_gray;
	
	private Paint default_paint;
	
	private View_Achievements_And_Leaderboards_Base view_leaderboards;
	
	
	public View_Main_Base(Activity_Main_Base2D activity) {
		
		super(activity);
		
		rectf_main 					= new RectF();
		rectf_button_pause			= new RectF();
		rectf_button_menu			= new RectF();
		rectf_button_new_small		= new RectF();
		rectf_button_center 		= new RectF();
		rectf_button_center_bottom 	= new RectF();
		rectf_top_center 			= new RectF();
		rectf_top_center_star1 		= new RectF();
		rectf_top_center_star2 		= new RectF();
		rectf_top_center_star3 		= new RectF();
		
		if (bitmap_pause == null) {
			
			bitmap_pause 				= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_pause);
			bitmap_play 				= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_play);
			bitmap_replay				= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_replay);
			bitmap_new					= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_replay);
			bitmap_menu 				= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_settings);
			bitmap_next					= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_next);
			bitmap_star					= BitmapUtils.fromResource(getContext(), R.drawable.ic_star_gold);
			bitmap_star_gray			= BitmapUtils.toGrayscale(bitmap_star);
		}
		
		default_paint 				= new Paint();
	}
	
	
	public abstract Class getMainMenuClass();
	
	
	protected Activity_Main_Base2D getActivity() {
		return (Activity_Main_Base2D) getContext();
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
	
	
	protected void initializeDimensions() {
		
		int[] screen_size = ScreenUtils.getScreenSize((Activity) getContext());
		int main_width = Math.max(screen_size[0], screen_size[1]);
		int main_height = Math.min(screen_size[0], screen_size[1]);
		
		int border = (int) (getWorld().getCellSize() / 10);
		//int border = 10;
		
		rectf_main.left = 0;
		rectf_main.top = 0;
		rectf_main.right = main_width;
		rectf_main.bottom = main_height;
		
		rectf_button_center.left = main_width / 2 - main_width / 11;
		rectf_button_center.top = main_height / 2 - main_height / 9;
		rectf_button_center.right = main_width / 2 + main_width / 11;
		rectf_button_center.bottom = main_height / 2 + main_height / 9;
		
		//rectf_button_center_bottom.left = main_width / 2 - 2 * (rectf_button_center.right - rectf_button_center.left) / (float) 3;
		//rectf_button_center_bottom.top = rectf_button_center.bottom + 3 * border;
		//rectf_button_center_bottom.right = main_width / 2 + 2 * (rectf_button_center.right - rectf_button_center.left) / (float) 3;
		//rectf_button_center_bottom.bottom = rectf_button_center_bottom.top + (rectf_button_center.bottom - rectf_button_center.top);

		float leaderboard_center_x = rectf_button_center.right + (rectf_main.right - rectf_button_center.right) / 2;
		float factor = 0.7f;
		rectf_button_center_bottom.left = leaderboard_center_x - factor * (rectf_button_center.right - rectf_button_center.left);
		rectf_button_center_bottom.top = main_height / 2 - factor * (rectf_button_center.bottom - rectf_button_center.top) / 2;
		rectf_button_center_bottom.right = leaderboard_center_x + factor * (rectf_button_center.right - rectf_button_center.left);
		rectf_button_center_bottom.bottom = main_height / 2 + factor * (rectf_button_center.bottom - rectf_button_center.top) / 2;
		
		
		int top_center_height = (int) ((rectf_button_center.top - 14 * border));
		
		rectf_top_center.left 			= main_width / 2 - main_width / 3;
		rectf_top_center.top 			= 7f * border;
		rectf_top_center.right 			= main_width / 2 + main_width / 3;
		rectf_top_center.bottom 		= rectf_top_center.top + top_center_height;
		
		rectf_top_center_star1.left 	= rectf_top_center.left;
		rectf_top_center_star1.top 		= rectf_top_center.top;
		rectf_top_center_star1.bottom 	= rectf_top_center.bottom;
		int star_width = (int) (rectf_top_center_star1.bottom - rectf_top_center_star1.top);
		rectf_top_center_star1.right 	= rectf_top_center_star1.left + star_width;
		
		rectf_top_center_star2.left 	= rectf_top_center.left + (rectf_top_center.right - rectf_top_center.left) / 2 - star_width / 2;
		rectf_top_center_star2.top 		= rectf_top_center.top;
		rectf_top_center_star2.bottom 	= rectf_top_center.bottom;
		rectf_top_center_star2.right 	= rectf_top_center_star2.left + star_width;
		
		rectf_top_center_star3.top 		= rectf_top_center.top;
		rectf_top_center_star3.bottom 	= rectf_top_center.bottom;
		rectf_top_center_star3.right 	= rectf_top_center.right;
		rectf_top_center_star3.left 	= rectf_top_center_star3.right - star_width;
		
		
		textarea_top_center1_gameover 				= new TextArea(rectf_top_center, true,
				Application_Base.getInstance().getString(R.string.game_over),
				-1, getActivity().getColoursCfg().getColour_Square_InvalidSelection());
		
		
		button_new_center = new ButtonAreaClick_Image(rectf_button_center, bitmap_replay,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		button_next_level = new ButtonAreaClick_Image(rectf_button_center, bitmap_next,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		
		rectf_button_pause.left = border;
		rectf_button_pause.top = border;
		rectf_button_pause.right = rectf_button_pause.left + getWorld().getCellSize() - 2 * border;
		rectf_button_pause.bottom = rectf_button_pause.top + getWorld().getCellSize() - 2 * border;
		
		button_pause = new ButtonAreaClick_Image(rectf_button_pause, bitmap_pause,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		button_resume = new ButtonAreaClick_Image(rectf_button_center, bitmap_play,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		button_replay = new ButtonAreaClick_Image(rectf_button_center, bitmap_replay,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		rectf_button_menu.left = main_width - getWorld().getCellSize() + border;
		rectf_button_menu.top = border;
		rectf_button_menu.right = main_width - border;
		rectf_button_menu.bottom = rectf_button_menu.top + getWorld().getCellSize() - 2 * border;
		
		button_menu = new ButtonAreaClick_Image(rectf_button_menu, bitmap_menu,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
		
		
		rectf_button_new_small.left = rectf_button_menu.left - getWorld().getCellSize();
		rectf_button_new_small.top = border;
		rectf_button_new_small.right = rectf_button_menu.left - border;
		rectf_button_new_small.bottom = rectf_button_new_small.top + getWorld().getCellSize() - 2 * border;
		
		button_new_small = new ButtonAreaClick_Image(rectf_button_new_small, bitmap_new,
				getActivity().getColoursCfg().getColour_Square_ValidSelection(),
				getActivity().getColoursCfg().getColour_Square_MarkingSelection(), true);
	}
	
	
	protected Paint getDefaultPaint() {
		return default_paint;
	}
	
	
	protected RectF getRectangle_Main() {
		return rectf_main;
	}
	
	
	private RectF getRectangle_LeaderBoards() {
		return rectf_button_center_bottom;
	}
	
	
	private void createLeaderBoardsView() {
		
		Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID);	
		final View _view_leaderboards = Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().getLeaderboardView(coloursCfg, getRectangle_LeaderBoards());
		
		View _view_achievements = Application_Base.getInstance().getEngagementProvider().getAchievementsProvider().getAchievementsView(coloursCfg, getRectangle_LeaderBoards());
		
		if (_view_leaderboards != null && _view_achievements != null) {
			if (_view_leaderboards != _view_achievements) {
				throw new IllegalStateException("_view_leaderboards != _view_achievements");
			}
		}
		
		((View_Achievements_And_Leaderboards_Base)_view_leaderboards).measure(0, 0);
		
		view_leaderboards = (View_Achievements_And_Leaderboards_Base) _view_leaderboards;
		
		//Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().setEnabled(true);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		button_menu.draw(canvas);
		
		if (isLevelCompleted()) {
			
			int count_stars = Application_2D_Base.getInstance().getGameData().last_count_stars;
			
			default_paint.setAlpha(count_stars <= 0 ? 127 : 255);
			canvas.drawBitmap(count_stars <= 0 ? bitmap_star_gray : bitmap_star, null, rectf_top_center_star1, default_paint);
			
			default_paint.setAlpha(count_stars <= 1 ? 127 : 255);
			canvas.drawBitmap(count_stars <= 1 ? bitmap_star_gray : bitmap_star, null, rectf_top_center_star2, default_paint);
			
			default_paint.setAlpha(count_stars <= 2 ? 127 : 255);
			canvas.drawBitmap(count_stars <= 2 ? bitmap_star_gray : bitmap_star, null, rectf_top_center_star3, default_paint);
			
			if (count_stars >= 3) {
				button_next_level.draw(canvas);
			} else {
				button_resume.draw(canvas);
				//button_replay.draw(canvas);
			}
			
		} else if (isGameOver()) {
			
			textarea_top_center1_gameover.draw(canvas);
			
			button_new_center.draw(canvas);
			
			if (view_leaderboards == null) {
				createLeaderBoardsView();
			}
			view_leaderboards.draw(canvas);
			
		} else {
			
			if (Application_2D_Base.getInstance().getGameData().paused) {
				button_resume.draw(canvas);
			} else {
				button_pause.draw(canvas);
			}
			
			button_new_small.draw(canvas);
			
			//Clear
			if (view_leaderboards != null) {
				Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);
				view_leaderboards = null;
			}
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		synchronized (this) {
			
			int action = event.getActionMasked() & MotionEvent.ACTION_MASK;
			
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
	
	
	protected void processEvent_DOWN(MotionEvent event) {
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());
		
		
		if (isGameOver()) {
			if (getRectangle_LeaderBoards().contains(x, y)) {
				if (view_leaderboards != null) {
					view_leaderboards.onTouch(this, event);
					return;
				}
			}
		}
		
		
		if (rectf_button_new_small.contains(x, y)) {
			button_new_small.select();
		} else {
			button_new_small.deselect();
		}
		
		if (rectf_button_menu.contains(x, y)) {
			button_menu.select();
		} else {
			button_menu.deselect();
		}
		
		if (rectf_button_pause.contains(x, y)) {
			button_pause.select();
		} else {
			button_pause.deselect();
		}
		
		if (rectf_button_center.contains(x, y)) {
			button_resume.select();
			button_new_center.select();
			button_next_level.select();
		} else {
			button_resume.deselect();
			button_new_center.deselect();
			button_next_level.deselect();
		}
	}
	
	
	protected void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());
		
		
		if (isGameOver()) {
			if (getRectangle_LeaderBoards().contains(x, y)) {
				if (view_leaderboards != null) {
					view_leaderboards.onTouch(this, event);
					return;
				}
			}
		}
		
		
		if (rectf_button_new_small.contains(x, y)) {
			button_new_small.select();
		} else {
			button_new_small.deselect();
		}
		
		if (rectf_button_menu.contains(x, y)) {
			button_menu.select();
		} else {
			button_menu.deselect();
		}
		
		if (rectf_button_pause.contains(x, y)) {
			button_pause.select();
		} else {
			button_pause.deselect();
		}
		
		if (rectf_button_center.contains(x, y)) {
			button_resume.select();
			button_new_center.select();
			button_next_level.select();
		} else {
			button_resume.deselect();
			button_new_center.deselect();
			button_next_level.deselect();
		}
	}
	
	
	protected void processEvent_UP(MotionEvent event) {
		
		float x = event.getX(event.getActionIndex());
		float y = event.getY(event.getActionIndex());
		
		
		if (isGameOver()) {
			if (getRectangle_LeaderBoards().contains(x, y)) {
				if (view_leaderboards != null) {
					view_leaderboards.onTouch(this, event);
					return;
				}
			}
		}
		
		
		button_new_small.deselect();
		button_menu.deselect();
		button_resume.deselect();
		button_pause.deselect();
		button_new_center.deselect();
		button_next_level.deselect();
		
		
		if (rectf_button_menu.contains(x, y)) {
			//System.out.prin tln("MENU clicked");
			Intent i = new Intent(getActivity(), getMainMenuClass());
			getActivity().startActivity(i);
		}
		
		
		if (isLevelCompleted()) {
			
			if (rectf_button_center.contains(x, y)) {
				
				Application_2D_Base.getInstance().getGameData().level_completed = false;
				Application_2D_Base.getInstance().getGameData().timestamp_lastborn = System.currentTimeMillis();
				
				Application_2D_Base.getInstance().storeGameData();
				
			}
			
		} else if (isGameOver()) {
			
			if (rectf_button_center.contains(x, y)) {
				
				//getActivity().startNewGame();
				
				AlertDialog.Builder adb = Alerts_Base.createAlertDialog_LoseGame(getActivity(),
						
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								
								getActivity().startNewGame();
								
							}
						},
						
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								
							}
						}
				);
	
				adb.show();
			}
			
		} else {
			
			if (rectf_button_new_small.contains(x, y)) {
				
				final boolean paused_backup = Application_2D_Base.getInstance().getGameData().paused;
				Application_2D_Base.getInstance().getGameData().paused = true;
				
				AlertDialog.Builder adb = Alerts_Base.createAlertDialog_LoseGame(getActivity(),
						
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {

								Application_2D_Base.getInstance().getGameData().paused = false;
								
								getActivity().startNewGame();
								
							}
						},
						
						new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								
								Application_2D_Base.getInstance().getGameData().paused = paused_backup;
								
							}
						}
				);
	
				adb.show();
			}
			
			if (rectf_button_pause.contains(x, y)) {
				Application_2D_Base.getInstance().getGameData().paused = true;
			}
			
			if (rectf_button_center.contains(x, y)) {
				Application_2D_Base.getInstance().getGameData().paused = false;
			}
		}
	}
}
