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
package com.pietervaneeckhout.waypointcoverter.view;

import com.pietervaneeckhout.waypointcoverter.controller.DomainFacade;
import com.pietervaneeckhout.waypointcoverter.controller.property.PropertiesController;
import com.pietervaneeckhout.waypointcoverter.util.BaseObserver;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * SettingsGUIInternalFrame.java (UTF-8)
 *
 * <p>Graphical UI representation for changing setting.</p>
 *
 * 2013/06/24
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.2.1
 * @version 1.2.1
 */
public class SettingsGUIInternalFrame extends BaseObserver<PropertiesController, Map<String, String>> {

    private DomainFacade domainFacade;
    private JInternalFrame settingsFrame;
    private JPanel contentPanel;
    private JScrollPane contentScrollPane;
    private JCheckBox appendBox, overwriteBox;
    private JComboBox languageComboBox;
    private JLabel appendLabel, overwriteLabal, languageLabel;

    public SettingsGUIInternalFrame(DomainFacade domainFacade) {
        this.domainFacade = domainFacade;
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's settingsGUIWindow.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGUI();
            }
        });
    }

    private void initGUI() {
        settingsFrame = new JInternalFrame("Settings", false, true);
    }
    
    @Override
    public void update(Map<String, String> data) {
        
    }
}
