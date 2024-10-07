package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class BinFull extends State{
	private Logger logger = LogManager.getLogger(BinFull.class);
	
	public void run() throws InterruptedException {
		logger.info("Running Bin Full State");
		Utils.initializeProperties();
		
		this.waterPump.off();
		this.fan.off();
		this.hotGas.off();
		this.drain.off();
		this.water.off();
		compressorOff();
		
		cutIce();
		
		logger.info("Wait for Bin temp to reach " + Utils.getStartIce());
		while(true) {
			if(binTemp.getTemp() >= Utils.getStartIce()) {
				current = initial;
				return;
			}
		}
	}
}
