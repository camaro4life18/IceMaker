package com.allen.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinFull extends State{
	Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.debug("Entering Bin Full State");
		//TODO turn off circulation pump
		//TODO turn off condensor fan
		//TODO turn off hot gas solenoid
		//TODO turn off drain solenoid
		//TODO turn off water solenoid
		//TODO turn off compressor
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Bin Full State Stuff");
		
		Thread.sleep(2100000);
		//TODO turn off grid cutter
		
		while(true) {
			//TODO get bin temperature
			int bintemp = 45;
			if(bintemp >= 43) {
				current = production;
				return;
			}
		}
	}
}
