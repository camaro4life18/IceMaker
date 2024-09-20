package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Clean extends State{
	private Logger logger = LogManager.getLogger(Production.class.getName());
	
	public void enter() {
		logger.info("Entering Clean State");
		this.everythingOff();
	}
	public void update() throws InterruptedException {
		logger.debug("Running clean cycle");
		
		//this.water.on();
		//Thread.sleep(180000);
		//this.water.off();
		
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
		
		this.water.on();
		Thread.sleep(120000);
		this.water.off();
		
		current = production;
	}
}
