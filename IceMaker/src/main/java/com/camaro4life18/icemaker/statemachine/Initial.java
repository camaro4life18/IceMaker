package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the startup state of the Ice Maker.
 * <p>
 * This state handles the initialization sequence:
 * 1. Checks if the bin is already full.
 * 2. Starts the cooling system (Compressor/Fan).
 * 3. Fills the water trough.
 * 4. Waits for the system to stabilize before transitioning to the Harvest state.
 * </p>
 */
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

		// Check if bin is already full upon startup
		double binTemperature = readTempOrFault(binTemp, "Bin thermistor");
		if(Double.isNaN(binTemperature)) {
			return;
		}
		if(binTemperature <= Utils.getStopIce()) {
			current = binfull;
			return;
		}

		// Start cooling system
		compressorOn();
		fan.on();
			
		// Fill water trough
		water.on();
		Thread.sleep(Utils.getWaterFillTime());
		water.off();
		waterPump.on();
		
		//Wait for water to run over evap tray. On BinFull -> Initial, the evap tray is cooling off faster than the water fill.
		Thread.sleep(60000);		

		logger.info("Waiting on evap temp");
		double harvestTemp = getHarvestTemp();
		
		// Wait for initial batch of ice to freeze
		while(true) {
			double evapTemperature = readTempOrFault(evapTemp, "Evaporator thermistor");
			if(Double.isNaN(evapTemperature)) {
				return;
			}
			if(evapTemperature <= harvestTemp) {
				current = harvest;
				return;
			}
			Thread.sleep(1000);
		}
	}
}
