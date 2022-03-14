package org.metatrans.commons.graphics2d.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.metatrans.commons.graphics2d.menu.IConfigurationJoystick;
import org.metatrans.commons.model.UserSettings_Base;


public class UserSettings extends UserSettings_Base {
	
	
	private static final long serialVersionUID = 869150857012453808L;

	
	public long best_scores;

	public int movejoystick_side = IConfigurationJoystick.MOVE_RIGHTJOYSTICK;

	
	public UserSettings() {
		
		super();
	}
	
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		
	    fixFields("writeObject");
	    
	    // default serialization 
	    oos.defaultWriteObject();
	}
	

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    
	    // default deserialization
	    ois.defaultReadObject();
	    
	    fixFields("readObject");
	}
	
	
	private void fixFields(String op) {
		
	}
}
