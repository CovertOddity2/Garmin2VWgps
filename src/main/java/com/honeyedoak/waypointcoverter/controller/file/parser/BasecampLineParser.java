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
package com.honeyedoak.waypointcoverter.controller.file.parser;

import com.honeyedoak.waypointcoverter.exceptions.InvalidModelStateException;
import com.honeyedoak.waypointcoverter.model.Waypoint;
import com.honeyedoak.waypointcoverter.exceptions.ParseException;
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
public class BasecampLineParser implements LineParser {

    private Logger logger;

    public BasecampLineParser() {
        logger = Logger.getLogger(BasecampLineParser.class);
    }

    @Override
    public Waypoint parseLine(String line, String seperator) throws ParseException {
        try {
            String[] parts = line.split(seperator);

            String name = parts[7].replaceAll("^\"|\"$", "");;
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);
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

            return new Waypoint(name, longitude, east, latitude, north);
        } catch (InvalidModelStateException ex) {
            throw new ParseException("There was a problem parsing the waypoint: " + ex.getMessage());
        } catch (NumberFormatException e) {
            String errorString = "There was a problem parsing the waypoint: could not parse The coordinate formatting";
            logger.error(errorString);
            logger.debug(e.getMessage());
            throw new ParseException(errorString);
        }
    }
}
