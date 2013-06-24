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
package com.pietervaneeckhout.waypointcoverter.controller.file.parser;

import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ParseException;
import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
 * GpxFileParser.java (UTF-8)
 *
 * <p>A FileParser implementation.</p>
 *
 * 2013/06/23
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.1.0
 * @version 1.1.0
 */
public class GpxFileParser implements FileParser {
    
    private Logger logger;

    public GpxFileParser() {
        logger = Logger.getLogger(GpxFileParser.class);
    }
    
    @Override
    public List<Waypoint> parseFile(File file) throws FileException, ParseException {
        List<Waypoint> waypointList = new ArrayList<>();
        try {
            if (!file.exists()) {
                String error = "The requested file was not found";
                logger.error(error);
                logger.debug(file.getAbsolutePath());
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

                        if (latitude < 0) {
                            north = false;
                            latitude *= -1;
                        } else {
                            north = true;
                        }

                        if (longitude < 0) {
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
        } catch (NumberFormatException e) {
            String errorString = "There was a problem parsing the waypoint: could not parse The coordinate formatting";
            logger.error(errorString);
            logger.debug(e.getMessage());
            throw new ParseException(errorString);
        }

        return waypointList;
    }   
}
