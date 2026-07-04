package com.camaro4life18.icemaker.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;

public class GpioSwitch {
	private static Logger logger = LogManager.getLogger(GpioRelay.class);
	
	private DigitalInput gswitch = null;
	private String name;
	
	public GpioSwitch(Context pi4j, String gpioPin, String name) {
		this.name = name;
		var switchConfig = DigitalInput.newConfigBuilder(pi4j)
				.id("switch-" + name)
				.name(name)
				.bcm(Integer.parseInt(gpioPin))
				.pull(PullResistance.PULL_DOWN)
				.build();
		this.gswitch = pi4j.din().create(switchConfig);		
	}
	
	public boolean isOn() {
		logger.info(this.name + " is " + gswitch.isOn());
		return gswitch.isOn();
	}
}
