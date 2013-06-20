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
package com.pietervaneeckhout.garmin2vwgps.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Waypoint.java (UTF-8)
 *
 * <p>Model for storing waypoints.</p>
 *
 * 2013/06/08
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.0
 * @version 1.0.1
 */
public class Waypoint implements Serializable {

    private static final long serialVersionUID = 688432065L;
    private String name;
    private double longitude, latitude;
    private boolean east, north;
    private transient boolean export;

    public Waypoint(String name, double longitude, boolean east, double latitude, boolean north) throws IllegalArgumentException {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.east = east;
        this.north = north;
        this.export = true;
        validateState();
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name value
     * @throws IllegalArgumentException
     */
    public void setName(String name) throws IllegalArgumentException {
        validateName(name);
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude the longitude value
     * @throws IllegalArgumentException
     */
    public void setLongitude(double longitude) throws IllegalArgumentException {
        validateLatitude(longitude);
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude the latitude value
     * @throws IllegalArgumentException
     */
    public void setLatitude(double latitude) throws IllegalArgumentException {
        validateLatitude(latitude);
        this.latitude = latitude;
    }

    public boolean isEast() {
        return east;
    }

    /**
     *
     * @param east is east
     * @throws IllegalArgumentException
     */
    public void setEast(boolean east) throws IllegalArgumentException {
        validateEast(east);
        this.east = east;
    }

    public boolean isNorth() {
        return north;
    }

    public void setNorth(boolean north) throws IllegalArgumentException {
        validateNorth(north);
        this.north = north;
    }

    private void validateState() throws IllegalArgumentException {
        validateNorth(north);
        validateEast(east);
        validateName(name);
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    public boolean isExport() {
        return export;
    }
    
    public void toggleExport() {
       this.export ^= true;
    }
    
    private void validateNorth(boolean north) throws IllegalArgumentException {
    }

    private void validateEast(boolean east) throws IllegalArgumentException {
    }

    private void validateName(String name) throws IllegalArgumentException {
        boolean nameHasContent = (name != null) && (!name.equals(""));
        if (!nameHasContent) {
            throw new IllegalArgumentException("Names must be non-null and non-empty.");
        }
    }

    private void validateLongitude(double longitude) throws IllegalArgumentException {
        if (longitude < 0 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between 0 and 180");
        }
    }

    private void validateLatitude(double latitude) throws IllegalArgumentException {
        if (latitude < 0 || latitude > 180) {
            throw new IllegalArgumentException("Latitude must be between 0 and 180");
        }
    }

    public String toVolkswagenWaypoint() {
        StringBuilder builder = new StringBuilder();
        if (!east) {
            builder.append('-');
        }
        builder.append(longitude)
                .append(',');
        if (!north) {
            builder.append('-');
        }
        builder.append(latitude)
                .append(',');
        builder.append("\"")
                .append(name)
                .append("\"");
        return builder.toString();
    }
    
    public WaypointUIModel toWaypointUIModel() {
        String latitude, longitude;
        
        if (north) {
            latitude = "N";
        } else {
            latitude = "S";
        }
        latitude += this.latitude;
        
        if(east) {
            longitude = "E";
        } else {
            longitude = "W";
        }
        longitude += this.longitude;
        
        return new WaypointUIModel(export, name, latitude, longitude);
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
        validateState();
        this.export = false;
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
    }
}
