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
package com.honeyedoak.waypointcoverter.controller.file;

import com.honeyedoak.waypointcoverter.controller.file.parser.CsvFileParser;
import com.honeyedoak.waypointcoverter.controller.file.parser.FileParser;
import com.honeyedoak.waypointcoverter.controller.file.parser.GpxFileParser;
import com.honeyedoak.waypointcoverter.controller.file.parser.TxtFileParser;
import com.honeyedoak.waypointcoverter.exceptions.*;
import com.honeyedoak.waypointcoverter.model.Waypoint;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GPSCoordinateControler.java (UTF-8)
 * <p>
 * <p>Controller for manipulating GPS coordinates.</p>
 * <p>
 * 2013/06/08
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @version 1.1.0
 * @since 1.0.0
 */
public class FileController {

	private Logger logger;
	private FileParser fileParser;

	public FileController() {
		logger = Logger.getLogger(FileController.class);
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
			fileParser = new CsvFileParser();
		} else {
			throw new ParseException("unsupported File extention");
		}

		return fileParser.parseFile(new File(filePath));
	}

	public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList) throws InvalidModelStateException, IOException {
		writeOpelWaypointsToFile(filePath, waypointList, false);
	}

	public void writeOpelWaypointsToFile(String filePath, List<Waypoint> waypointList, boolean overwrite) throws InvalidModelStateException, IOException {

		if (filePath == null || filePath.isEmpty()) {
			logger.error("output file path is empty or null");
			throw new InvalidModelStateException("output filePath cannot be null or empty.");
		}

		//auto add file extention
		if (!filePath.endsWith(".txt")) {
			logger.info("file was missing extion, added .txt");
			filePath += ".txt";
		}

		try (OutputStream outstream = Files.newOutputStream(Paths.get(filePath))) {
			File file = new File(filePath);

			if (file.exists() && !overwrite) {
				logger.info(filePath + " already exists");
				throw new FileAlreadyExistsException(filePath + " already exists");
			} else {
				logger.info(filePath + " already exists. Cleared for overwrite");
			}

			final String collect = waypointList.stream().map(Waypoint::toOpelWaypoint).collect(Collectors.joining(String.format("%n")));
			outstream.write(collect.getBytes(StandardCharsets.ISO_8859_1));

		} catch (IOException e) {
			logger.error(e);
			throw e;
		}
	}
}
