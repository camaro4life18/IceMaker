package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Off extends State{
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.info("Entering Off State");
		this.everythingOff();
	}
	public void update() throws InterruptedException {
		logger.debug("Doing BOff State Stuff");
		
		while(true) {
			//TODO get switch state
			boolean switchstate = true;
			
			if(switchstate == true) {
				current = production;
				return;
			}
		}
	}
}
