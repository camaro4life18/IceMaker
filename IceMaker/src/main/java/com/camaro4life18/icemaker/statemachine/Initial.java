package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Initial extends State{
	private Logger logger = LogManager.getLogger(Initial.class.getName());
	
	public void enter() throws InterruptedException
	{
		logger.debug("Entering Initial State");
		this.everythingOff();
		
		//Wait 9 minutes for Compressor Safety
		Thread.sleep(540000);
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Initial State Stuff");
		
		//TODO Get Bin Temp
		int bintemp = 30;
		if(bintemp <= 25) {
			current = binfull;
			return;
		}
		
		this.fan.on();
		this.compressor.on();
		
		this.water.on();
		//Thread.sleep(90000);
		this.water.off();
		
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
