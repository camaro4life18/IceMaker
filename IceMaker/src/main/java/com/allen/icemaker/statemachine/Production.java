package com.allen.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Production extends State {
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.debug("Entering Production State");
		//TODO turn on circulation pump
		//TODO turn on condensor fan
		//TODO turn off hot gas solenoid
		//TODO turn on compressor relay
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Production State Stuff");
		
		Thread.sleep(2100000);
		//TODO turn off grid cutter
		
		while(true) {
			//TODO get evap temp
			int evap = 20;
			if(evap <= 11) {
				current = harvest;
				return;
			}
		}
	}
}
