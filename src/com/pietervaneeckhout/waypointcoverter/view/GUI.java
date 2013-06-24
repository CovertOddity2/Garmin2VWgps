/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pietervaneeckhout.waypointcoverter.view;

import com.pietervaneeckhout.waypointcoverter.controller.DomainFacade;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * GUI.java (UTF-8)
 *
 * <p>Graphical UI representation.</p>
 *
 * 2013/06/24
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.2.1
 * @version 1.2.1
 */
public class GUI {

    private JFrame programFrame;
    private MainGUIDesktopPane mainGUIDesktopPane;
    private SettingsGUIInternalFrame settingsGUIInternalFrame;
    private MenubarGUI menubarGUI;

    public GUI(DomainFacade domainFacade) {
        programFrame = new JFrame("WaypointConverter");
        mainGUIDesktopPane = new MainGUIDesktopPane(programFrame, domainFacade);
        settingsGUIInternalFrame = new SettingsGUIInternalFrame(domainFacade);
        menubarGUI = new MenubarGUI(programFrame);
        
        programFrame.setContentPane(mainGUIDesktopPane);
        
        programFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension minDimension = new Dimension(350, 250);
        Dimension prefDimension = new Dimension(700, 500);
        programFrame.setMinimumSize(minDimension);
        programFrame.setPreferredSize(prefDimension);
        programFrame.setSize(prefDimension);

        programFrame.pack();
        programFrame.setVisible(true);
    }

    public MainGUIDesktopPane getMainGUIDesktopPane() {
        return mainGUIDesktopPane;
    }

    public SettingsGUIInternalFrame getSettingsGUIInternalFrame() {
        return settingsGUIInternalFrame;
    }
}
