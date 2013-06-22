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
package com.pietervaneeckhout.waypointcoverter.model;

import com.pietervaneeckhout.waypointcoverter.exceptions.InvalidModelStateException;

/**
 * WaypointUIModel.java (UTF-8)
 *
 * <p>Model for passing waypoints to the ui.</p>
 *
 * 2013/06/19
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.1
 * @version 1.0.2
 */
public class WaypointUI {

    private final Boolean export;
    private final String name;
    private final String latitude;
    private final String longitude;

    public WaypointUI(boolean export, String name, String latitude, String longitude) throws InvalidModelStateException {
        if (name == null || name.isEmpty()) {
            throw new InvalidModelStateException("name is null or empty");
        }
        if (latitude == null || latitude.isEmpty()) {
            throw new InvalidModelStateException("latitude is null or empty");
        }
        if (longitude == null || longitude.isEmpty()) {
            throw new InvalidModelStateException("longitude is null or empty");
        }
        this.export = export;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Boolean isExport() {
        return export;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        String output;
        if (export) {
            output = "export";
        } else {
            output = "withhold";
        }
        return output + " " + name + " " + latitude + " " + longitude;
    }
}
