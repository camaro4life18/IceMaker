package com.camaro4life18.icemaker.statemachine;

import com.camaro4life18.icemaker.devices.GpioRelay;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public abstract class State {
	protected Context pi4j;
	
	public State() {
		this.pi4j = Pi4J.newAutoContext();
	}
	public static State initial;
	public static State harvest;
	public static State production;
	public static State binfull;
	public static State clean;
	public static State off;
	public static State current;
	
	protected GpioRelay gridCutter = new GpioRelay(pi4j, "10", "GridCutter");
	protected GpioRelay waterPump = new GpioRelay(pi4j, "10", "WaterPump");
	protected GpioRelay fan = new GpioRelay(pi4j, "10", "Fan");
	protected GpioRelay hotGas = new GpioRelay(pi4j, "10", "HotGas Solenoid");
	protected GpioRelay drain = new GpioRelay(pi4j, "10", "Drain Solenoid");
	protected GpioRelay water = new GpioRelay(pi4j, "10", "Water Solenoid");
	protected GpioRelay compressor = new GpioRelay(pi4j, "10", "Compressor");
	
	public void enter() throws InterruptedException {}
	public void update() throws InterruptedException {}
	
	protected void everythingOff() {
		this.gridCutter.off();
		this.waterPump.off();
		this.fan.off();
		this.hotGas.off();
		this.drain.off();
		this.water.off();
		this.compressor.off();
	}
}
