package org.metatrans.commons.graphics2d.menu;


import java.util.HashMap;
import java.util.Map;


public class ConfigurationUtils_Joysticks {
	
	
	private static final IConfigurationJoystick[] ALL_CFGs = new IConfigurationJoystick[] {
		new Config_Joystick_MoveRight(),
		new Config_Joystick_MoveLeft(),
	};
	

	private static final Map<Integer, IConfigurationJoystick> mapping = new HashMap<Integer, IConfigurationJoystick>();
	
	
	static {
		for (int i=0; i<ALL_CFGs.length; i++) {

			IConfigurationJoystick cfg = ALL_CFGs[i];
			Integer id = cfg.getID();

			if (mapping.containsKey(id)) {
				throw new IllegalStateException("Duplicated cfg id: " + id);
			}
			
			mapping.put(id, cfg);
		}
	}
	
	
	public static IConfigurationJoystick[] getAll() {
		return ALL_CFGs;
	}
	
	
	public static int getID(int orderNumber) {
		return ALL_CFGs[orderNumber].getID();
	}
	
	
	public static IConfigurationJoystick getConfigByID(int id) {

		IConfigurationJoystick result = mapping.get(id);
		
		if (result == null) {
			throw new IllegalStateException("Config with id = " + id + " not found.");
		}
		
		return result;
	}
	
	
	public static int getOrderNumber(int cfgID) {
		for (int i=0; i<ALL_CFGs.length; i++) {
			Integer id = ALL_CFGs[i].getID();
			if (id == cfgID) {
				return i;
			}
		}

		throw new IllegalStateException("CFG identifier " + cfgID + " not found.");
	}
}
