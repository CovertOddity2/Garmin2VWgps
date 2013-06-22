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
package com.pietervaneeckhout.waypointcoverter;

import com.pietervaneeckhout.waypointcoverter.controller.DomainFacade;
import com.pietervaneeckhout.waypointcoverter.controller.file.FileController;
import com.pietervaneeckhout.waypointcoverter.controller.waypoint.WaypointController;
import com.pietervaneeckhout.waypointcoverter.controller.waypoint.WaypointRepository;
import com.pietervaneeckhout.waypointcoverter.view.BaseUI;
import com.pietervaneeckhout.waypointcoverter.view.GUI;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * WaypointConverter.java (UTF-8)
 *
 * <p>This class is the main class to start this application.</p>
 *
 * 2013/06/08
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.0
 * @version 1.0.2
 */
public class WaypointConverter {
    
    private Logger logger;

    /**
     * Main method
     * <p/>
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new WaypointConverter();
    }

    /**
     * Constructor
     */
    public WaypointConverter() {
        
        //load log4j.properties file.
        PropertyConfigurator.configure("settings/log4j.properties");
        
        //make logger
        logger = Logger.getLogger("FILE");
        
        logger.trace("creating waypoint repository");
        WaypointRepository waypointRepository = new WaypointRepository();
        
        logger.trace("creating waypoint controller");
        WaypointController waypointController = new WaypointController(waypointRepository);
        
        logger.trace("creating file controller");
        FileController fileController = new FileController();
        
        logger.trace("creating domain facade");
        DomainFacade domainFacade = new DomainFacade(waypointController, fileController);
        
        logger.trace("creating GUI");
        BaseUI ui = new GUI(domainFacade);
        
        logger.trace("adding ui as waypoint repository obsever");
        waypointRepository.addObsever(ui);
    }
}
