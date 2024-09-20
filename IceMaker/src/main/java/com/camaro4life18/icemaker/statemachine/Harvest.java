package com.camaro4life18.icemaker.statemachine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Harvest extends State{
	private Logger logger = LogManager.getLogger(Initial.class.getName());
	
	public void enter() throws InterruptedException
	{
		logger.info("Entering Harvest State");
		
		this.waterPump.off();
		this.fan.off();
		this.hotGas.on();
		this.gridCutter.on();
	}
	public void update() throws InterruptedException {
		logger.debug("Doing Harvest State Stuff");
		
		this.drain.on();
		Thread.sleep(45000);
		this.drain.off();
		
		this.water.on();
		Thread.sleep(120000);
		this.water.off();
		
		while(true) {
			if(binTemp.getTemp() <= 35) {
				current = binfull;
				return;
			}
			else if(evapTemp.getTemp() >= 45) {
				current = production;
				return;
			}
		}
		
	}
}
