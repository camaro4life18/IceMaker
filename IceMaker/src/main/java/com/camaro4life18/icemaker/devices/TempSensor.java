package com.camaro4life18.icemaker.devices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TempSensor extends Thread {
    private static Logger logger = LogManager.getLogger(TempSensor.class);

    private String sensorName = null;
    private String filePath = "/sys/bus/w1/devices";
    private int maxRetries = 10; // Maximum number of retries
    private long retryDelay = 1000; // Delay between retries in milliseconds
    private GpioRelay w1Power;

    public TempSensor(String sensorID, String sensorName, GpioRelay w1Power) {
        this.sensorName = sensorName;
        filePath = filePath + "/" + sensorID;
        this.w1Power = w1Power;
    }

    public double getTemp() {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            String temp = readFile(filePath + "/temperature");

            if (temp != null) {
                try {
                    Double celsius = Double.parseDouble(temp) / 1000;
                    DecimalFormat df = new DecimalFormat("####0.00");
                    Double farenheit = Double.parseDouble(df.format((celsius * 1.8) + 32));

                    logger.debug("Temp Sensor - " + this.sensorName + " - is: " + farenheit);
                    return farenheit;
                } catch (NumberFormatException e) {
                    logger.error("Invalid temperature data for sensor: " + this.sensorName + ", data: " + temp, e);
                    return Double.NaN;
                }
            } else {
                logger.warn("Temperature data is null for sensor: " + this.sensorName + ". Retrying...");
                retryCount++;
                resetW1Power();
                try {
                    Thread.sleep(retryDelay); // Wait before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    logger.error("Thread interrupted during retry delay", e);
                    return Double.NaN;
                }
            }
        }

        logger.error("Failed to get temperature after " + maxRetries + " retries for sensor: " + this.sensorName);
        return Double.NaN; // Return NaN after all retries fail so the caller can enter a safe state
    }

    private String readFile(String fileName) {
        String line = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            line = br.readLine();
        } catch (Exception e) {
            logger.error("Exception thrown from - " + sensorName + " - sensor, file: " + fileName + ": " + e.getMessage(), e);
        }
        return line;
    }
    
    private void resetW1Power() {
    	w1Power.off();
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while resetting W1 power", e);
            return;
		}
    	w1Power.on();
    }
}