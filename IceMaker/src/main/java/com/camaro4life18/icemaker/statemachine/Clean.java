package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the Cleaning Cycle state.
 * <p>
 * This maintenance state runs a predefined sequence of filling, circulating,
 * and draining to flush the system with cleaning solution or fresh water.
 * The cycle can be interrupted by turning off the clean switch.
 * </p>
 */
public class Clean extends State{
	private Logger logger = LogManager.getLogger(Clean.class);
	
	public void run() throws InterruptedException {
		logger.info("Running clean cycle");
		Utils.initializeProperties();
		// Ensure everything is off before starting cleaning cycle
		this.everythingOff();
		
		// Step 1: Fill with water
		water.on();
		if(!waitAndCheck(180000)) return;
		water.off();
		
		// Step 2: Circulate water (cleaning solution)
		waterPump.on();
		if(!waitAndCheck(1800000)) return;
		waterPump.off();
		
		// Step 3: Drain dirty water
		drain.on();
		if(!waitAndCheck(75000)) return;
		drain.off();
		
		// Step 4: Rinse Fill
		water.on();
		if(!waitAndCheck(180000)) return;
		water.off();
		
		// Step 5: Rinse Circulate
		waterPump.on();
		if(!waitAndCheck(600000)) return;
		waterPump.off();
		
		// Step 6: Final Drain
		drain.on();
		if(!waitAndCheck(75000)) return;
		drain.off();
		
		current = initial;
	}

	// Helper to wait for a duration while checking if the clean switch is turned off
	private boolean waitAndCheck(long duration) throws InterruptedException {
		long end = System.currentTimeMillis() + duration;
		while(System.currentTimeMillis() < end) {
			if(!cleanSwitch.isOn()) {
				current = off;
				return false;
			}
			Thread.sleep(1000);
		}
		return true;
	}
}
