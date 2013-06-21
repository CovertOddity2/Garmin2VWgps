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
package com.pietervaneeckhout.waypointcoverter.controller.repository;

import com.pietervaneeckhout.waypointcoverter.model.Waypoint;
import com.pietervaneeckhout.waypointcoverter.model.WaypointUI;
import com.pietervaneeckhout.waypointcoverter.util.BaseObservable;
import com.pietervaneeckhout.waypointcoverter.util.BaseObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WaypointRepository.java (UTF-8)
 *
 * <p>Repository for storing GPS coordinate Objects.</p>
 *
 * 2013/06/08
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.0
 * @version 1.0.1
 */
public class WaypointRepository extends BaseObservable<WaypointRepository, List<WaypointUI>> {

    private Map<String, Waypoint> waypoints;

    public WaypointRepository() {
        this(new HashMap<String, Waypoint>());
    }

    public WaypointRepository(Map<String, Waypoint> waypoints) {
        super();
        obsevers = new ArrayList<>();
        this.waypoints = waypoints;
    }

    public List<Waypoint> getWaypoitsToExport() {
        List<Waypoint> exportList = new ArrayList<>();
        for (Map.Entry<String, Waypoint> entry : waypoints.entrySet()) {
            Waypoint waypoint = entry.getValue();
            if (waypoint.isExport()) {
                exportList.add(waypoint);
            }
        }
        return exportList;
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.getName(), waypoint);
        notifyObservers();
    }

    public void removeWaypoint(Waypoint waypoint) {
        waypoints.remove(waypoint.getName());
    }

    public Waypoint getWaypoint(String waypointName) {
        return waypoints.get(waypointName);
    }

    public void clear() {
        waypoints.clear();
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        List<WaypointUI> list = new ArrayList<>();
        for (Map.Entry<String, Waypoint> entry : waypoints.entrySet()) {
            Waypoint waypoint = entry.getValue();
            list.add(waypoint.toWaypointUIModel());
        }

        for (BaseObserver<WaypointRepository, List<WaypointUI>> obsever : obsevers) {
            obsever.update(list);
        }
    }
}
