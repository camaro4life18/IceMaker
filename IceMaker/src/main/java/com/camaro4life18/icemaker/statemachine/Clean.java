package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.camaro4life18.icemaker.Utils;

public class Clean extends State{
	private Logger logger = LogManager.getLogger(Clean.class);
	
	public void run() throws InterruptedException {
		logger.info("Running clean cycle");
		Utils.initializeProperties();
		this.everythingOff();
		
		this.water.on();
		Thread.sleep(180000);
		this.water.off();
		
		this.waterPump.on();
		Thread.sleep(1800000);
		this.waterPump.off();
		
		this.drain.on();
		Thread.sleep(75000);
		this.drain.off();
		
		this.water.on();
		Thread.sleep(180000);
		this.water.off();
		
		this.waterPump.on();
		Thread.sleep(600000);
		this.waterPump.off();
		
		this.drain.on();
		Thread.sleep(75000);
		this.drain.off();
		
		current = initial;
	}
}
