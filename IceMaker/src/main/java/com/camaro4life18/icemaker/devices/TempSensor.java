package com.camaro4life18.icemaker.devices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TempSensor extends Thread {
    private static Logger logger = LogManager.getLogger(TempSensor.class);

    private static final String W1_MASTER_SEARCH = "/sys/bus/w1/devices/w1_bus_master1/w1_master_search";
    private String sensorName = null;
    private String filePath = "/sys/bus/w1/devices";
    private int maxRetries = 30; // Maximum number of retries
    private long retryDelay = 1000; // Delay between retries in milliseconds

    public TempSensor(String sensorID, String sensorName) {
        this.sensorName = sensorName;
        filePath = filePath + "/" + sensorID;
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
                // Every 5 retries, trigger a kernel 1-wire bus search to re-enumerate sensors
                if (retryCount % 5 == 0) {
                    triggerBusSearch();
                }
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

    private void triggerBusSearch() {
        try (FileWriter fw = new FileWriter(W1_MASTER_SEARCH)) {
            fw.write("1");
            logger.info("Triggered 1-wire bus search to re-enumerate sensors");
        } catch (Exception e) {
            logger.warn("Could not trigger 1-wire bus search: " + e.getMessage());
        }
    }
}