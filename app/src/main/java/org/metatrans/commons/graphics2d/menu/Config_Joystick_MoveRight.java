package org.metatrans.commons.graphics2d.menu;


import com.commons2d.R;

import org.metatrans.commons.cfg.ConfigurationEntry_Base;


public class Config_Joystick_MoveRight extends ConfigurationEntry_Base implements IConfigurationJoystick {

	
	@Override
	public int getID() {
		return MOVE_RIGHTJOYSTICK;
	}

	@Override
	public int getName() {
		return R.string.menu_joystick_moveright;
	}

	@Override
	public int getIconResID() {
		return R.drawable.joystick_transparent;
	}
}
