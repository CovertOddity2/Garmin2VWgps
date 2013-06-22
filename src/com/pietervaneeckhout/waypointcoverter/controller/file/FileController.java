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

import com.pietervaneeckhout.waypointcoverter.exceptions.FatalException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileExistsException;
import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import java.io.*;
import java.util.ArrayList;
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
 * @version 1.0.2
 */
public class FileController {

    private Logger logger;

    public FileController() {
        logger = Logger.getLogger(FileController.class);
    }

    public List<Waypoint> readGarminWaypointsFromFile(String filePath) throws FileNotFoundException, IOException, FatalException {
        BufferedReader br = null;
        List<Waypoint> waypointList = new ArrayList<>();

        try {
            FileReader fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Waypoint")) {
                    waypointList.add(parseGarminMapsourceTxtWaypoint(line));
                }
            }
        } catch (FileNotFoundException ex) {
            logger.error("The requested file was not found");
            throw ex;
        } catch (IOException ex) {
            logger.error("IO problem: " + ex.getMessage());
            throw ex;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    String errorMessage = "Could not successfully close the file reader";
                    logger.fatal(errorMessage);
                    throw new FatalException(errorMessage);
                }
            }
        }

        return waypointList;
    }

    private Waypoint parseGarminMapsourceTxtWaypoint(String line) {
        boolean north, east;
        double longitude, latitude;
        String name;

        String[] parts = line.split("\\t");
        String[] position = parts[4].split(" ");

        name = parts[1];

        north = position[0].startsWith("N");
        east = position[1].startsWith("E");

        latitude = Double.parseDouble(position[0].substring(1));
        longitude = Double.parseDouble(position[1].substring(1));

        Waypoint waypoint = new Waypoint(name, longitude, east, latitude, north);
     

        logger.debug(waypoint.toString());

        return waypoint;
    }

    public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList) throws FileExistsException, IOException {
        writeOpelWaypointsToFile(filePath, waypointList, false);
    }

    public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList, boolean overwrite) throws FileExistsException, IOException {      
        
        PrintWriter pw = null;

        if (filePath == null || filePath.isEmpty()) {
            logger.error("file path is empty or null");
            throw new IllegalArgumentException("filePath cannot be null or empty.");
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
                throw new FileExistsException(filePath + " already exists");
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
            throw ex;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
