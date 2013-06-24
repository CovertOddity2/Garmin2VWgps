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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * MenubarGUI.java (UTF-8)
 *
 * <p>Graphical UI menubar.</p>
 *
 * 2013/06/24
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.2.1
 * @version 1.2.1
 */
public class MenubarGUI {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem resetMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu settingsMenu;
    private JMenu infoMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem sourceMenuItem;
    private JMenuItem licenseMenuItem;

    public MenubarGUI(JFrame parentFrame) {
        buildMenu();
        parentFrame.setJMenuBar(menuBar);
    }
    
    private void buildMenu() {
        //Create the menuBar
        menuBar = new JMenuBar();

        //Build the fileMenu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        //resetMenuItem
        resetMenuItem = new JMenuItem("Reset", KeyEvent.VK_R);
        resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        resetMenuItem.getAccessibleContext().setAccessibleDescription("Reset the application");
        fileMenu.add(resetMenuItem);
        //exitMenuItem
        exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitMenuItem.getAccessibleContext().setAccessibleDescription("Exit the application");
        fileMenu.add(exitMenuItem);
        //add to menuBar
        menuBar.add(fileMenu);

        //build the settings menu
        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        settingsMenu.getAccessibleContext().setAccessibleDescription("Opens the Settings");
        //add to menuBar
        menuBar.add(settingsMenu);

        //build the info menu
        infoMenu = new JMenu("Help");
        infoMenu.setMnemonic(KeyEvent.VK_I);
        //helpMenuItem
        helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
        helpMenuItem.getAccessibleContext().setAccessibleDescription("open the help function");
        infoMenu.add(helpMenuItem);
        //sourceMenuItem
        sourceMenuItem = new JMenuItem("Source", KeyEvent.VK_S);
        sourceMenuItem.getAccessibleContext().setAccessibleDescription("go to the application source");
        infoMenu.add(sourceMenuItem);
        //licenseMenuItem
        licenseMenuItem = new JMenuItem("License", KeyEvent.VK_L);
        licenseMenuItem.getAccessibleContext().setAccessibleDescription("Open the application license");
        infoMenu.add(licenseMenuItem);
        //add to menuBar
        menuBar.add(infoMenu);

        menuBar.setVisible(true);        
    }
}
