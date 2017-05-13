
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

import com.honeyedoak.waypointcoverter.exceptions.FatalException;
import com.honeyedoak.waypointcoverter.model.Waypoint;
import com.honeyedoak.waypointcoverter.exceptions.FileException;
import com.honeyedoak.waypointcoverter.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * TxtFileParser.java (UTF-8)
 *
 * <p>A FileParser implementation.</p>
 *
 * 2013/06/23
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.1.0
 * @version 1.1.0
 */
public class TxtFileParser implements FileParser {
    
    private Logger logger;
    private LineParser lineParser;

    public TxtFileParser() {
        logger = Logger.getLogger(TxtFileParser.class);
    }
    
    @Override
    public List<Waypoint> parseFile(File file) throws FileException, ParseException, FatalException {
        BufferedReader br = null;
        List<Waypoint> waypointList = new ArrayList<>();

        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            if (line == null) {
                logger.error("The file is empty");
                throw new ParseException("The file is empty");
            } else {
                if (line.equals("\uFEFF")) {
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("wpt")) {
                            logger.info("recognized basecamp txt file");
                            lineParser = new BasecampLineParser();
                            br.readLine();
                            line = br.readLine();
                            while (!line.isEmpty() && !line.equals("\uFEFF")) {
                                waypointList.add(lineParser.parseLine(line, "\\t"));
                                line = br.readLine();
                            }
                            break;
                        }
                    }
                } else if (line.startsWith("Grid")) {
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("Waypoint")) {
                            logger.info("recognized Mapsource txt file");
                            lineParser = new MapsourceLineParser();
                            waypointList.add(lineParser.parseLine(line, "\\t"));
                        }
                    }
                } else {
                    throw new ParseException("The received file format was not recognised");
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
    
}
