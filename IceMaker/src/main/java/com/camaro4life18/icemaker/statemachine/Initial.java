package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class Initial extends State{
	private Logger logger = LogManager.getLogger(Initial.class);

	public void run() throws InterruptedException {
		logger.info("Running Initial State");		
		Utils.initializeProperties();
		w1Power.on();
		
		if(!onSwitch.isOn()) {
			current = off;
			return;
		}

		if(binTemp.getTemp() <= Utils.getStopIce()) {
			current = binfull;
			return;
		}

		compressorOn();
		this.fan.on();
			
		this.water.on();
		Thread.sleep(Utils.getWaterFillTime());
		this.water.off();
		this.waterPump.on();
		
		//Wait for water to run over evap tray. On BinFull -> Initial, the evap tray is cooling off faster than the water fill.
		Thread.sleep(60000);		

		logger.info("Waiting on evap temp");
		double harvestTemp = getHarvestTemp();
		
		while(true) {
			if(evapTemp.getTemp() <= harvestTemp) {
				current = harvest;
				return;
			}
		}
	}
}
