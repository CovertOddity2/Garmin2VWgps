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
import com.pietervaneeckhout.waypointcoverter.exceptions.FileAlreadyExistsException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ParseException;
import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
        logger = Logger.getLogger("FILE");
    }

    public List<Waypoint> readWaypointsFromFile(String filePath) throws FileException, FatalException, InvalidModelStateException, ParseException {

        if (filePath == null || filePath.isEmpty()) {
            logger.error("inputtput file path is empty or null");
            throw new InvalidModelStateException("intput filePath cannot be null or empty");
        }

        if (filePath.endsWith("txt")) {
            return parseTxtFile(filePath);
        } else if (filePath.endsWith("gpx")) {
            return parseGpxFile(filePath);
        } else {
            throw new ParseException("unsupported File extention");
        }
    }

    private Waypoint parseGarminMapsourceTxtWaypoint(String line) throws ParseException {
        boolean north, east;
        double longitude, latitude;
        String name;

        String[] parts = line.split("\\t");
        String[] position = parts[4].split(" ");

        name = parts[1];

        try {
            if (position.length == 2) {
                logger.info("recognized digital coordinate format");
                north = position[0].startsWith("N");
                east = position[1].startsWith("E");

                latitude = Double.parseDouble(position[0].substring(1));
                longitude = Double.parseDouble(position[1].substring(1));
            } else if (position.length == 4) {
                logger.info("recognized degree coordinate format");
                north = position[0].startsWith("N");
                east = position[2].startsWith("E");

                latitude = Double.parseDouble(position[0].substring(1));
                latitude += Double.parseDouble(position[1]) / 60;
                longitude = Double.parseDouble(position[2].substring(1));
                longitude += Double.parseDouble(position[3]) / 60;
            } else {
                String errorString = "There was a problem parsing the waypoint: could not parse The coordinate formatting";
                logger.error(errorString);
                logger.debug(Arrays.deepToString(position));
                throw new ParseException(errorString);
            }
        } catch (NumberFormatException e) {
            String errorString = "There was a problem parsing the waypoint: could not parse The coordinate formatting";
            logger.error(errorString);
            logger.debug(e.getMessage());
            throw new ParseException(errorString);
        }

        Waypoint waypoint = null;

        try {
            waypoint = new Waypoint(name, longitude, east, latitude, north);
        } catch (InvalidModelStateException ex) {
            throw new ParseException("There was a problem parsing the waypoint: " + ex.getMessage());
        }

        logger.debug(waypoint.toString());

        return waypoint;
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

    private List<Waypoint> parseTxtFile(String filePath) throws ParseException, FileException, FatalException {
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
        } catch (FileNotFoundException e) {
            String error = "The requested file was not found";
            logger.error(error);
            logger.debug(e.getMessage());
            throw new FileException(error);
        } catch (IOException e) {
            String error = "There was a problem reading the requested file";
            logger.debug(e.getMessage());
            logger.error(error);
            throw new FileException(error);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    String errorMessage = "Could not successfully close the file reader";
                    logger.fatal(errorMessage);
                    logger.debug(e.getMessage());
                    throw new FatalException(errorMessage);
                }
            }
        }

        return waypointList;
    }

    private List<Waypoint> parseGpxFile(String filePath) throws FileException, ParseException {
        List<Waypoint> waypointList = new ArrayList<>();
        try {

            File file = new File(filePath);

            if (!file.exists()) {
                String error = "The requested file was not found";
                logger.error(error);
                logger.debug(filePath);
                throw new FileException(error);
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
                       
            Element rootElement = document.getDocumentElement();
            rootElement.normalize();

            NodeList waypointNodeLists = rootElement.getElementsByTagName("wpt");
            logger.debug("found " + waypointNodeLists.getLength() + "waypoints in the file");
            
            if (waypointNodeLists.getLength() > 0) {
                for (int i = 0; i < waypointNodeLists.getLength(); i++) {
                    Node waypointNode = waypointNodeLists.item(i);
                    
                    if (waypointNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element waypointElement = (Element) waypointNode;
                        NodeList nameNodeList = waypointElement.getElementsByTagName("name");
                        String name = nameNodeList.item(0).getChildNodes().item(0).getNodeValue();
                        double latitude = Double.parseDouble(waypointElement.getAttribute("lat"));
                        double longitude = Double.parseDouble(waypointElement.getAttribute("lon"));
                        boolean north, east;
                        
                        if (latitude<0){
                            north = false;
                            latitude *= -1;
                        } else {
                            north = true;
                        }
                        
                        if (longitude<0) {
                            east = false;
                            longitude *= -1;
                        } else {
                            east = true;
                        }
                        
                        waypointList.add(new Waypoint(name, longitude, east, latitude, north));
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | InvalidModelStateException ex) {
            String error = "There was a problem parsing the requested file: " + ex.getMessage();
            logger.debug(ex.getMessage());
            logger.error(error);
            throw new ParseException(error);
        } catch (IOException e) {
            String error = "There was a problem reading the requested file";
            logger.debug(e.getMessage());
            logger.error(error);
            throw new FileException(error);
        }

        return waypointList;
    }
}
