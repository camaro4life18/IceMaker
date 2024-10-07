package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class Harvest extends State{
	private Logger logger = LogManager.getLogger(Harvest.class);

	public void run() throws InterruptedException {
		Utils.initializeProperties();
		logger.info("Running Harvest State");
		if(!onSwitch.isOn()) {
			current = off;
			return;
		}
		
		this.waterPump.off();
		this.fan.off();
		this.hotGas.on();
		cutIce();
		
		this.drain.on();
		Thread.sleep(Utils.getDrainTime());
		this.drain.off();
		
		while(true) {
			if(binTemp.getTemp() <= Utils.getStopIce()) {
				current = binfull;
				return;
			}
			else if(evapTemp.getTemp() >= Utils.getProductionTemp()) {
				current = production;
				return;
			}
		}
		
	}
}
