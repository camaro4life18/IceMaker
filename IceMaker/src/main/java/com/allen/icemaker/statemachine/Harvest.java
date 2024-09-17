package com.allen.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Harvest extends State{
	private Logger logger = LogManager.getLogger(Initial.class.getName());
	
	public void enter() throws InterruptedException
	{
		logger.debug("Entering Harvest State");
		//TODO Turn off circulation pump
		//TODO Turn off compressor fan
		//TODO enable hot gas solenoid
		//TODO enable grid cutter
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Harvest State Stuff");
		
		//TODO enable drain solenoid
		Thread.sleep(45000);
		//TODO disable drain solenoid
		
		//TODO enable water solenoid
		Thread.sleep(120000);
		//TODO disable water solenoid
		
		while(true) {
			//TODO get evap temp
			int evap = 20;
			//TODO get bin temp
			int bin = 45;
			
			if(bin <= 35) {
				current = binfull;
				return;
			}
			else if(evap >= 45) {
				current = production;
				return;
			}
		}
		
	}
}
