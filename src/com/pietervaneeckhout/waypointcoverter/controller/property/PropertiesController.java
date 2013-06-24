/*
 * The MIT License
 *
 * Copyright 2013 Pieter Van Eeckhout.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.pietervaneeckhout.waypointcoverter.controller.property;

import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * PropertiesController.java (UTF-8)
 *
 * <p>Controller for reading and writing the app settings to a properties
 * file</p>
 *
 * 2013/06/24
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.2.0
 * @version 1.2.0
 */
public class PropertiesController {

    private final String PROPERTIESLOCATION = "settings/WaypointConverter.properties";
    private Properties properties;
    private Logger logger;

    public PropertiesController() throws FileException {
        logger = Logger.getLogger(PropertiesController.class);
        properties = new Properties();

        File propertiesFile = new File(PROPERTIESLOCATION);
        if (propertiesFile.exists()) {
            loadProperties(propertiesFile);
        } else {
            createDefaultProperties(propertiesFile);
            loadProperties(propertiesFile);
        }
    }

    private void loadProperties(File propertiesFile) throws FileException {
        try {
            properties.load(new FileInputStream(propertiesFile));
        } catch (IOException ex) {
            String errorMessage = "error reading properties file";
            logger.error(errorMessage);
            throw new FileException(errorMessage);
        }
    }

    private void createDefaultProperties(File propertiesFile) throws FileException {
        logger.info("Properties file not found, creating new");

        properties.setProperty("WaypointConverter.language", "eng");
        properties.setProperty("WaypointConverter.appendWaypoints", "true");
        properties.setProperty("WaypointConverter.overWriteExisting", "false");

        persistPropertiesFile(propertiesFile);
    }

    private void persistPropertiesFile() throws FileException {
        persistPropertiesFile(new File(PROPERTIESLOCATION));
    }

    private void persistPropertiesFile(File propertiesFile) throws FileException {
        try {
            properties.store(new FileOutputStream(propertiesFile), null);
        } catch (IOException ex) {
            String errorMessage = "error writing new properties file";
            logger.error(errorMessage);
            throw new FileException(errorMessage);
        }
    }

    public String getLanguage() throws FileException {
        try {
            return properties.getProperty("WaypointConverter.language");
        } catch (NullPointerException ex) {
            String errormMessage = "Property not found: WaypointConverter.language";
            logger.error(errormMessage);
            throw new FileException(errormMessage);
        }
    }

    public void setLanguage(String language) throws FileException {
        properties.setProperty("WaypointConverter.language", language);
        persistPropertiesFile();
    }

    public boolean getAppend() throws FileException {
        try {
            return properties.getProperty("WaypointConverter.appendWaypoints").equalsIgnoreCase("true");
        } catch (NullPointerException ex) {
            String errormMessage = "Property not found: WaypointConverter.appendWaypoints";
            logger.error(errormMessage);
            throw new FileException(errormMessage);
        }
    }

    public void setAppend(boolean append) throws FileException {
        if (append) {
            properties.setProperty("WaypointConverter.appendWaypoints", "true");
        } else {
            properties.setProperty("WaypointConverter.appendWaypoints", "false");
        }
    }

    public boolean getOverwrite() throws FileException {
        try {
        return properties.getProperty("WaypointConverter.overWriteExisting").equalsIgnoreCase("true");
        } catch (NullPointerException ex) {
            String errormMessage = "Property not found: WaypointConverter.overWriteExisting";
            logger.error(errormMessage);
            throw new FileException(errormMessage);
        }
    }

    public void setOverwrite(boolean overwrite) throws FileException {
        if (overwrite) {
            properties.setProperty("WaypointConverter.overWriteExisting", "true");
        } else {
            properties.setProperty("WaypointConverter.overWriteExisting", "false");
        }
        persistPropertiesFile();
    }

    public void setProperties(Map<String, String> propertiesValues) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
