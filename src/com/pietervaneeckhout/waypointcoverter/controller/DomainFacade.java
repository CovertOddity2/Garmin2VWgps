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
package com.pietervaneeckhout.waypointcoverter.controller;

import com.pietervaneeckhout.waypointcoverter.exceptions.ProcessingException;
import com.pietervaneeckhout.waypointcoverter.controller.file.FileController;
import com.pietervaneeckhout.waypointcoverter.controller.waypoint.WaypointController;
import com.pietervaneeckhout.waypointcoverter.exceptions.FatalException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ParseException;
import com.pietervaneeckhout.waypointcoverter.exceptions.WaypointAlreadyExistsException;
import com.pietervaneeckhout.waypointcoverter.exceptions.WaypointDoesNotExistException;
import org.apache.log4j.Logger;

/**
 * DomainFacade.java (UTF-8)
 *
 * <p>Entry point into the domain layer. Delegates and coordinates the tasks to
 * the respective controllers</p>
 *
 * 2013/06/21
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.2
 * @version 1.0.2
 */
public class DomainFacade {

    private WaypointController waypointController;
    private FileController fileController;
    private Logger logger;

    public DomainFacade(WaypointController waypointController, FileController fileController) {
        logger = Logger.getLogger("FILE");
        this.waypointController = waypointController;
        this.fileController = fileController;
    }

    public WaypointController getWaypointController() {
        return waypointController;
    }

    public void setWaypointController(WaypointController waypointController) {
        this.waypointController = waypointController;
    }

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }

    public void loadFile(String filePath) throws FileException, FatalException, ProcessingException {
        try {
            waypointController.addWaypoints(fileController.readGarminWaypointsFromFile(filePath));

        } catch (WaypointAlreadyExistsException | InvalidModelStateException | ParseException ex) {
            throw new ProcessingException(ex.getMessage());
        }
    }

    public void toggleWaypointExport(String waypointName) throws WaypointDoesNotExistException {
        waypointController.toggleWaypointExport(waypointName);
    }

    public void exportWaypoints(String filePath, boolean overwrite) throws FileException, ProcessingException {
        try {
            fileController.writeOpelWaypointsToFile(filePath, waypointController.getWaypointRepository().getWaypointsToExport(), overwrite);
        } catch (InvalidModelStateException e) {
            throw new ProcessingException(e.getMessage());
        }
        
    }
}
