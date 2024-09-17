package com.allen.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Initial extends State{
	private Logger logger = LogManager.getLogger(Initial.class.getName());
	
	public void enter() throws InterruptedException
	{
		logger.debug("Entering Initial State");
		//TODO Turn off all devices
		
		//Wait 9 minutes for Compressor Safety
		//Thread.sleep(540000);
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Initial State Stuff");
		
		//TODO Get Bin Temp
		int bintemp = 30;
		if(bintemp <= 25) {
			current = binfull;
			return;
		}
		
		//TODO Turn on Fan
		//TODO Turn on Compressor
		
		//TODO Turn on water solenoid
		//Thread.sleep(90000);
		//TODO Turn off water solenoid
		
		//Loop until the evaporator temp is at or below harvest setpoint
		int evaptemp = 10;
		while(true) {
			if(evaptemp <= 11) {
				current = harvest;
				return;
			}
		}
		
		
	}
}
