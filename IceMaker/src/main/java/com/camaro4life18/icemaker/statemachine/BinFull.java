package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the Bin Full (Idle) state.
 * <p>
 * This state occurs when the ice storage bin is full (detected by low temperature).
 * It shuts down high-power components to save energy and monitors the bin temperature
 * to determine when to restart the ice-making cycle.
 * </p>
 */
public class BinFull extends State{
	private Logger logger = LogManager.getLogger(BinFull.class);
	
	public void run() throws InterruptedException {
		logger.info("Running Bin Full State");
		Utils.initializeProperties();
		
		// Shut down all active components to save energy
		waterPump.off();
		fan.off();
		hotGas.off();
		drain.off();
		water.off();
		compressorOff();
		
		// Ensure any hanging ice is cut
		cutIce();
		
		logger.info("Wait for Bin temp to reach " + Utils.getStartIce());
		// Wait for ice to melt/be used (temperature rise) before restarting
		while(true) {
			double binTemperature = readTempOrFault(binTemp, "Bin thermistor");
			if(Double.isNaN(binTemperature)) {
				return;
			}
			if(binTemperature >= Utils.getStartIce()) {
				current = initial;
				return;
			}
			Thread.sleep(5000);
		}
	}
}
