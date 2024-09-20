package com.camaro4life18.icemaker.statemachine;

import com.camaro4life18.icemaker.devices.GpioRelay;
import com.camaro4life18.icemaker.devices.GpioSwitch;
import com.camaro4life18.icemaker.devices.TempSensor;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public abstract class State extends Thread{
	protected Context pi4j = Pi4J.newAutoContext();
	
	public static State initial;
	public static State harvest;
	public static State production;
	public static State binfull;
	public static State clean;
	public static State off;
	public static State current;
	
	protected GpioRelay gridCutter = new GpioRelay(pi4j, "23", "GridCutter 1");
	protected GpioRelay waterPump = new GpioRelay(pi4j, "24", "WaterPump 2");
	protected GpioRelay fan = new GpioRelay(pi4j, "25", "Fan 3");
	protected GpioRelay hotGas = new GpioRelay(pi4j, "8", "HotGas Solenoid 4");
	protected GpioRelay drain = new GpioRelay(pi4j, "7", "Drain Solenoid 5");
	protected GpioRelay water = new GpioRelay(pi4j, "20", "Water Solenoid 6");
	protected GpioRelay compressor = new GpioRelay(pi4j, "21", "Compressor 7");
	
	protected GpioSwitch cleanSwitch = new GpioSwitch(pi4j, "", "Clean");
	
	protected TempSensor binTemp = new TempSensor("28-00000094c09c", "Bin");
	protected TempSensor evapTemp = new TempSensor("28-0000008660fc", "Evap Tray");
	
	public State() {
		
	}
	
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
	
	protected void deviceStatus() {
		this.gridCutter.status();
		this.waterPump.status();
		this.fan.status();
		this.hotGas.status();
		this.drain.status();
		this.water.status();
		this.compressor.status();
		this.binTemp.getTemp();
		this.evapTemp.getTemp();
	}
	
	public void run() {
		
	}
}
