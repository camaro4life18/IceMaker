package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Initial extends State{
	private Logger logger = LogManager.getLogger(Initial.class.getName());

	public void enter() throws InterruptedException
	{
		logger.info("Entering Initial State");
		this.everythingOff();
		
		logger.info("Waiting 9 minutes for compressor safety");
		Thread.sleep(540000);
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Initial State Stuff");

		if(binTemp.getTemp() <= 25) {
			current = binfull;
			return;
		}
		
		this.fan.on();
		this.compressor.on();
		
		this.water.on();
		Thread.sleep(90000);
		this.water.off();
		this.waterPump.on();
		
		while(true) {
			if(evapTemp.getTemp() <= 11) {
				current = harvest;
				return;
			}
		}
		
		
	}
}
