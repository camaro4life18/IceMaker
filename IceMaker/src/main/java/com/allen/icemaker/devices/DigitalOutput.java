package com.allen.icemaker.devices;

import com.pi4j.Pi4J;

public class DigitalOutput {
	private Pi4J pi4j;
	private int pin;
	
	public DigitalOutput(Pi4J pi4j, int pin) {
		this.pi4j = pi4j;
		this.pin = pin;
	}

}
