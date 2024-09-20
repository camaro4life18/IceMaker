package com.camaro4life18.icemaker.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class GpioSwitch {
	private static Logger logger = LogManager.getLogger(GpioRelay.class);
	
	private DigitalInput gswitch = null;
	private boolean switchStatus = false;
	private String name;
	
	public GpioSwitch(Context pi4j, String gpioPin, String name) {
		this.name = name;
		var switchConfig = DigitalInput.newConfigBuilder(pi4j)
				.id("relay-" + name)
				.name(name)
				.address(Integer.parseInt(gpioPin))
				.provider("pigpio-digital-input");
		this.gswitch = pi4j.create(switchConfig);
		
	}
	
	public boolean isOn() {
		if(gswitch.isOn()) {
			logger.info(this.name + " is switched on");
		}
		return gswitch.isOn();
	}
}
