package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the Harvest state.
 * <p>
 * This state is responsible for releasing the ice from the evaporator:
 * 1. Stops water circulation and activates the Hot Gas valve to warm the plate.
 * 2. Activates the Grid Cutter to slice the ice sheet.
 * 3. Drains the remaining water to remove mineral buildup.
 * 4. Checks if the bin is full or if the cycle should repeat.
 * </p>
 */
public class Harvest extends State{
	private Logger logger = LogManager.getLogger(Harvest.class);

	public void run() throws InterruptedException {
		Utils.initializeProperties();
		logger.info("Running Harvest State");
		if(!onSwitch.isOn()) {
			current = off;
			return;
		}
		
		// Stop cooling the water and turn on Hot Gas to loosen ice
		waterPump.off();
		fan.off();
		hotGas.on();
		
		// Start the grid cutter to slice the ice sheet
		cutIce();
		
		// Drain the remaining cold/mineral-rich water
		drain.on();
		Thread.sleep(Utils.getDrainTime());
		drain.off();
		
		// Wait for harvest completion or bin full condition
		while(true) {
			// If bin is full (cold), stop making ice
			double binTemperature = readTempOrFault(binTemp, "Bin thermistor");
			if(Double.isNaN(binTemperature)) {
				return;
			}
			if(binTemperature <= Utils.getStopIce()) {
				current = binfull;
				return;
			}
			// If evaporator warms up enough, the ice has dropped. Go back to production.
			double evapTemperature = readTempOrFault(evapTemp, "Evaporator thermistor");
			if(Double.isNaN(evapTemperature)) {
				return;
			}
			else if(evapTemperature >= Utils.getProductionTemp()) {
				current = production;
				return;
			}
			Thread.sleep(1000);
		}
		
	}
}
