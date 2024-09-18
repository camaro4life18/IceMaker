package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinFull extends State{
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.debug("Entering Bin Full State");
		this.waterPump.off();
		this.fan.off();
		this.hotGas.off();
		this.drain.off();
		this.water.off();
		this.compressor.off();
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Bin Full State Stuff");
		
		Thread.sleep(2100000);
		this.gridCutter.off();
		
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
