package com.camaro4life18.icemaker.devices;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TempSensor extends Thread{
	private static Logger logger = LogManager.getLogger(TempSensor.class);
	
	private String sensorName = null;
	private String filePath = "/sys/bus/w1/devices";
	
	public TempSensor(String sensorID, String sensorName) {

		
		this.sensorName = sensorName;
		filePath = filePath + "/" + sensorID;
	}
	
	public double getTemp() {
		String temp = readFile(filePath + "/temperature");
		Double celsius = Double.parseDouble(temp) / 1000;
		Double farenheit = (celsius * 1.8) + 32;
			
		logger.debug("Temp Sensor - " + this.sensorName + " - is: " + farenheit);
		return farenheit;
	}
	
	private String readFile(String fileName) {	     
		String line = "0";
	    try {
		    BufferedReader br = new BufferedReader(new FileReader(fileName));
			line = br.readLine();
			br.close();
		} catch (Exception e) {
			logger.error("Exception thrown from - " + sensorName + " - sensor: " + e.getMessage());
		}
	    return line;
	}
}
