package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the Production (Freezing) state.
 * <p>
 * In this state, the machine creates ice by:
 * 1. Circulating water over the freezing evaporator plate.
 * 2. Monitoring the evaporator temperature.
 * 3. Transitioning to the Harvest state when the temperature drops below the set threshold.
 * </p>
 */
public class Production extends State{
	private Logger logger = LogManager.getLogger(Production.class);
	
	public void run() throws InterruptedException {		
		logger.info("Entering Production State");
		Utils.initializeProperties();
		// Safety check: if switch turned off, go to Off state
		if(!onSwitch.isOn()) {
			current = off;
			return;
		}

		// Prepare for freezing: Fan ON, Hot Gas OFF
		fan.on();
		hotGas.off();
		
		// Fill the trough with water
		water.on();
		Thread.sleep(Utils.getWaterFillTime());
		water.off();
		
		// Start water circulation over the evaporator
		waterPump.on();
		
		logger.info("Waiting on evap temp");
		double harvestTemp = getHarvestTemp();
		
		// Monitor evaporator temperature to determine when ice is ready
		while(true) {
			double evapTemperature = readTempOrFault(evapTemp, "Evaporator thermistor");
			if(Double.isNaN(evapTemperature)) {
				return;
			}
			if(evapTemperature <= harvestTemp) {
				current = harvest;
				return;
			}
			Thread.sleep(10000);
		}
	}
}
