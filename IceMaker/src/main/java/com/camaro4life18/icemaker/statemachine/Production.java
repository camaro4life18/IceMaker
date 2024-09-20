package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Production extends State {
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.info("Entering Production State");
		this.waterPump.on();
		this.fan.on();
		this.hotGas.off();
		this.compressor.on();
	}
	public void update() throws InterruptedException {
		logger.debug("Cutting Ice for 35mins");
		
		
		Thread.sleep(2100000);
		this.gridCutter.off();
		
		logger.debug("Waiting on ice tray temp");
		while(true) {
			if(evapTemp.getTemp() <= 11) {
				current = harvest;
				return;
			}
		}
	}
}
