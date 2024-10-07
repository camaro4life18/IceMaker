package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class Production extends State{
	private Logger logger = LogManager.getLogger(Production.class);
	
	public void run() throws InterruptedException {		
		logger.info("Entering Production State");
		Utils.initializeProperties();
		if(!onSwitch.isOn()) {
			current = off;
			return;
		}

		this.fan.on();
		this.hotGas.off();
		
		this.water.on();
		Thread.sleep(Utils.getWaterFillTime());
		this.water.off();
		
		this.waterPump.on();
		
		logger.info("Waiting on evap temp");
		while(true) {
			if(evapTemp.getTemp() <= Utils.getHarvestTemp()) {
				current = harvest;
				return;
			}
			Thread.sleep(10000);
		}
	}
}
