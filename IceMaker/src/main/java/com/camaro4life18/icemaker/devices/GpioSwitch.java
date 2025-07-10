package com.camaro4life18.icemaker.devices;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class GpioSwitch {
	private static Logger logger = LogManager.getLogger(GpioRelay.class);
	
	private DigitalInput gswitch = null;
	private String name;
	
	public GpioSwitch(Context pi4j, String gpioPin, String name) {
		this.name = name;
		Properties prop = new Properties();
		prop.put("id", "relay-" + name);
		prop.put("address", Integer.parseInt(gpioPin));
		prop.put("name", name);
		prop.put("pull", "UP");
		
		var switchConfig = DigitalInput.newConfigBuilder(pi4j).load(prop).build();
		this.gswitch = pi4j.din().create(switchConfig);		
	}
	
	public boolean isOn() {
		logger.info(this.name + " is " + gswitch.isOn());
		return gswitch.isOn();
	}
}
