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

import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ParseException;
import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * MapsourceLineParser.java (UTF-8)
 *
 * <p>Implementation of a LineParser.</p>
 *
 * 2013/06/23
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.1.0
 * @version 1.1.0
 */
public class MapsourceLineParser implements LineParser {
    
    private Logger logger;

    public MapsourceLineParser() {
        logger = Logger.getLogger(MapsourceLineParser.class);
    }
    

    @Override
    public Waypoint parseLine(String line, String seperator) throws ParseException {
        boolean north, east;
        double longitude, latitude;
        String name;

        String[] parts = line.split(seperator);
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
    
}
