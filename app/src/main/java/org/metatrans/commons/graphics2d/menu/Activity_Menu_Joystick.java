package org.metatrans.commons.graphics2d.menu;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.metatrans.commons.Activity_Base;
import org.metatrans.commons.R;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.graphics2d.model.UserSettings;
import org.metatrans.commons.ui.list.ListViewFactory;
import org.metatrans.commons.ui.list.RowItem_CIdTD;
import org.metatrans.commons.ui.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;


public class Activity_Menu_Joystick extends Activity_Base {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		System.out.println("Activity_Menu_Colours_Base: onCreate()");
		
		super.onCreate(savedInstanceState);
		
		int currOrderNumber = ConfigurationUtils_Joysticks.getOrderNumber(((UserSettings)((Application_Base) getApplication()).getUserSettings()).movejoystick_side);
		
		LayoutInflater inflater = LayoutInflater.from(this);

		ViewGroup frame = ListViewFactory.create_CITD_ByXML(this, inflater, buildRows(currOrderNumber), -1, currOrderNumber, new OnItemClickListener_Menu());
		
		setContentView(frame);
		
		setBackgroundPoster(R.id.commons_listview_frame, 55);
	}
	
	
	@Override
	protected int getBackgroundImageID() {
		return 0;//R.drawable.ic_rainbow;
	}
	
	
	public List<RowItem_CIdTD> buildRows(int initialSelection) {
		
		List<RowItem_CIdTD> rowItems = new ArrayList<RowItem_CIdTD>();
		
		IConfigurationJoystick[] coloursCfg = ConfigurationUtils_Joysticks.getAll();
		
		for (int i = 0; i < coloursCfg.length; i++) {

			IConfigurationJoystick colourCfg = coloursCfg[i];

			Bitmap bitmap = BitmapUtils.fromResource(this, colourCfg.getIconResID());
			Bitmap old = bitmap;
			bitmap = BitmapUtils.createScaledBitmap(bitmap, getIconSize(), getIconSize(), false);
			BitmapUtils.recycle(bitmap, old);
			Drawable drawable = BitmapUtils.createDrawable(this, bitmap);
			
			RowItem_CIdTD item = new RowItem_CIdTD(i == initialSelection, drawable, getString(colourCfg.getName()), "");

			rowItems.add(item);
		}
		
		return rowItems;
	}
	
	
	private class OnItemClickListener_Menu implements
			AdapterView.OnItemClickListener {
		
		
		private OnItemClickListener_Menu() {
		}
		
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			//System.out.println("ColoursSelection POS=" + position + ", id=" + id);
			
			int currOrderNumber = ConfigurationUtils_Joysticks.getOrderNumber(((UserSettings)((Application_Base) getApplication()).getUserSettings()).movejoystick_side);

			if (position != currOrderNumber) {

				int newCfgID = ConfigurationUtils_Joysticks.getID(position);

				changeColours(newCfgID);
			}
			
			finish();
		}
	}
	
	
	public void changeColours(int movejoystick_id) {

		((UserSettings)((Application_Base) getApplication()).getUserSettings()).movejoystick_side = movejoystick_id;
		
		((Application_Base)getApplication()).storeUserSettings();
	}
}
