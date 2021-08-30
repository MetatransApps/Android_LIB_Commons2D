package org.metatrans.commons.graphics2d.model;


import org.metatrans.commons.model.GameData_Base;


public class GameData extends GameData_Base {
	
	
	private static final long serialVersionUID = 3487116550711673913L;
	
	
	public IWorld world;
	
	public long timestamp_lastborn;
	public int count_lives;
	public int count_bullets;
	public int count_stars;
	public int count_steps;
	public int total_count_steps;
	public int last_count_stars;
	//public int last_count_steps;
	
	public boolean paused;
	
	public boolean level_completed;
	
	
	public GameData() {
		
		timestamp_lastborn 			= System.currentTimeMillis();
		
		count_lives 				= 5;
		count_bullets 				= 5;
		count_stars					= 0;
		count_steps 				= 0;
		total_count_steps			= 0;
		last_count_stars			= 0;
		//last_count_steps 			= 0;
		
		paused 						= false;
	}
}
