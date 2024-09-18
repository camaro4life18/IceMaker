package com.camaro4life18.icemaker.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class GpioRelay {
	private static Logger logger = LogManager.getLogger(GpioRelay.class);
	
	private DigitalOutput relay = null;
	private boolean relayStatus = false;
	private String name;
	
	public GpioRelay(Context pi4j, String gpioPin, String name) {
		this.name = name;
		var relayConfig = DigitalOutput.newConfigBuilder(pi4j)
				.id("relay-" + name)
				.name(name)
				.address(Integer.parseInt(gpioPin))
				.shutdown(DigitalState.HIGH)
				.initial(DigitalState.HIGH)
				.provider("pigpio-digital-output");
		this.relay = pi4j.create(relayConfig);
		
	}
	
	public void on() {
		if(!this.relay.isLow()) {
			this.relay.low();
			relayStatus = true;
			logger.info(name + " turned on");
		}
	}
	
	public void off() {
		if(!this.relay.isHigh()) {
			this.relay.high();
			relayStatus = false;
			logger.info(name + " turned off");
		}
	}
	
	public boolean status() {
		return relayStatus;
	}
}
