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
package com.pietervaneeckhout.waypointcoverter.controller.file;

import com.pietervaneeckhout.waypointcoverter.controller.file.parser.FileParser;
import com.pietervaneeckhout.waypointcoverter.controller.file.parser.GpxFileParser;
import com.pietervaneeckhout.waypointcoverter.controller.file.parser.TxtFileParser;
import com.pietervaneeckhout.waypointcoverter.exceptions.FatalException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileAlreadyExistsException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ParseException;
import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import java.io.*;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * GPSCoordinateControler.java (UTF-8)
 *
 * <p>Controller for manipulating GPS coordinates.</p>
 *
 * 2013/06/08
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.0
 * @version 1.1.0
 */
public class FileController {

    private Logger logger;
    private FileParser fileParser;

    public FileController() {
        logger = Logger.getLogger("FILE");
    }

    public List<Waypoint> readWaypointsFromFile(String filePath) throws FileException, FatalException, InvalidModelStateException, ParseException {

        if (filePath == null || filePath.isEmpty()) {
            logger.error("input file path is empty or null");
            throw new InvalidModelStateException("intput filePath cannot be null or empty");
        }

        if (filePath.endsWith("txt")) {
            fileParser = new TxtFileParser();
        } else if (filePath.endsWith("gpx")) {
            fileParser = new GpxFileParser();
        } else if (filePath.endsWith("csv")) {
            throw new UnsupportedOperationException("not implemented yet");
        } else {
            throw new ParseException("unsupported File extention");
        }
        
        return fileParser.parseFile(new File(filePath));
    }
    
    public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList) throws InvalidModelStateException, FileException {
        writeOpelWaypointsToFile(filePath, waypointList, false);
    }

    public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList, boolean overwrite) throws InvalidModelStateException, FileException {

        PrintWriter pw = null;

        if (filePath == null || filePath.isEmpty()) {
            logger.error("output file path is empty or null");
            throw new InvalidModelStateException("output filePath cannot be null or empty.");
        }

        //auto add file extention
        if (!filePath.endsWith(".txt")) {
            logger.info("file was missing extion, added .txt");
            filePath += ".txt";
        }

        try {
            File file = new File(filePath);

            if (file.exists() && !overwrite) {
                logger.info(filePath + " already exists");
                throw new FileAlreadyExistsException(filePath + " already exists");
            } else {
                logger.info(filePath + " already exists. Cleared for overwrite");
            }

            logger.debug("writing " + waypointList.size() + " waypoints to file");
            FileWriter fw = new FileWriter(file);
            pw = new PrintWriter(fw);
            for (Waypoint waypoint : waypointList) {
                pw.println(waypoint.toOpelWaypoint());
            }
        } catch (IOException ex) {
            logger.error("IO problem: " + ex.getMessage());
            throw new FileException("There was an error writing to file");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
