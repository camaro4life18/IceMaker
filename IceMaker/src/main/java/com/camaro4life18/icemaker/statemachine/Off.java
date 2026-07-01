package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

/**
 * Represents the Off / Shutdown state.
 * <p>
 * This state ensures the system is safely shut down:
 * 1. Turns off all relays.
 * 2. Drains the water trough.
 * 3. Waits for user input (On Switch or Clean Switch) to transition to active states.
 * </p>
 */
public class Off extends State{
	private Logger logger = LogManager.getLogger(Off.class);

	public void run() throws InterruptedException {
		logger.info("Running Off State");
		Utils.initializeProperties();
		// Ensure everything is off before starting
		this.everythingOff();
		
		// Drain any remaining water to prevent stagnation
		drain.on();
		Thread.sleep(Utils.getDrainTime());
		drain.off();
		
		// Monitor switches to determine next state
		while(true) {			
			if(onSwitch.isOn()) {
				current = initial;
				return;
			}

			if(cleanSwitch.isOn()) {
				current = clean;
				return;
			}
			Thread.sleep(1000);
		}
	}
}
