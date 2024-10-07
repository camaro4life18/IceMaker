package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class Off extends State{
	private Logger logger = LogManager.getLogger(Off.class);

	public void run() throws InterruptedException {
		logger.info("Running Off State");
		Utils.initializeProperties();
		this.everythingOff();
		
		this.drain.on();
		Thread.sleep(Utils.getDrainTime());
		this.drain.off();
		
		while(true) {			
			if(onSwitch.isOn()) {
				current = initial;
				return;
			}

			if(cleanSwitch.isOn()) {
				current = clean;
				return;
			}
		}
	}
}
