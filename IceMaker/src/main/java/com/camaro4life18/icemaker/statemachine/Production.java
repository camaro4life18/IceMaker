package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Production extends State {
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.debug("Entering Production State");
		this.waterPump.on();
		this.fan.on();
		this.hotGas.off();
		this.compressor.on();
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Production State Stuff");
		
		Thread.sleep(2100000);
		this.gridCutter.off();
		
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
