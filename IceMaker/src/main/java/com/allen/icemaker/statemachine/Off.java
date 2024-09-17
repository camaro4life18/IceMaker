package com.allen.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Off extends State{
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.debug("Entering Off State");
		//TODO turn off circulation pump
		//TODO turn off condensor fan
		//TODO turn off hot gas solenoid
		//TODO turn off drain solenoid
		//TODO turn off water solenoid
		//TODO turn off compressor
		//TODO turn off grid cutter
	}
	public void update() throws InterruptedException {
		logger.debug("Doing BOff State Stuff");
		
		while(true) {
			//TODO get switch state
			boolean switchstate = false;
			
			if(switchstate == true) {
				current = production;
				return;
			}
		}
	}
}
