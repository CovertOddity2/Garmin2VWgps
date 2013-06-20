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
package com.pietervaneeckhout.garmin2vwgps.view;

import com.pietervaneeckhout.garmin2vwgps.controller.WaypointController;
import com.pietervaneeckhout.garmin2vwgps.controller.repository.WaypointRepository;
import com.pietervaneeckhout.garmin2vwgps.model.WaypointUIModel;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * GUI.java (UTF-8)
 *
 * <p>Graphical UI representation.</p>
 *
 * 2013/06/19
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.1
 * @version 1.0.1
 */
public class GUI extends BaseUI {

    private final static boolean shouldFill = true;
    private final static boolean shouldWeightX = true;
    private final static boolean shouldWeightY = true;
    private final static boolean RIGHT_TO_LEFT = false;
    private JFrame mainFrame;
    private JPanel contentPanel;
    private JList<WaypointUIModel> waypointList;
    private JLabel lblInput, lblOutput;
    private JButton btnBrowseInput, btnBrowseOutput, btnLoad, btnExport;
    private JTextField txtInput, txtOutput;
    private JMenuBar menuBar;
    private JMenu fileMenu, settingsMenu, infoMenu;
    private JMenuItem resetMenuItem, helpMenuItem, sourceMenuItem, licenseMenuItem, exitMenuItem;

    public GUI(WaypointController waypointController) {
        super(waypointController);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGUI();
            }
        });
    }

    private void initGUI() {
        //intialise elements
        mainFrame = new JFrame("Garmin2Volkswagen");
        contentPanel = new JPanel();
        waypointList = new JList();
        btnBrowseInput = new JButton("Browse");
        btnBrowseOutput = new JButton("Browse");
        btnLoad = new JButton("load");
        btnExport = new JButton("export");
        txtInput = new JTextField();
        txtOutput = new JTextField();
        lblInput = new JLabel("input file:");
        lblOutput = new JLabel("output file:");

        //list special settings
        waypointList.setCellRenderer(new WaypointUIModelListRenderer());
        waypointList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //build the menu
        buildMenu();

        //set frame variables
        JFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add items to panel
        addComponentsToPanel();

        //add panel and menubar to frame
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(contentPanel);

        //addActionListeners
        addActionListeners();

        //display the window
        mainFrame.pack();
        mainFrame.setVisible(true);

        //set sizes
        Dimension dim = new Dimension(500, 325);
        contentPanel.setMinimumSize(dim);
        contentPanel.setPreferredSize(dim);
        contentPanel.setSize(dim);
        mainFrame.setSize(contentPanel.getSize());
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

    private void addComponentsToPanel() {
        //set a margin around components
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        if (RIGHT_TO_LEFT) {
            contentPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints listConstraints = new GridBagConstraints();
        GridBagConstraints lblInputConstraints = new GridBagConstraints();
        GridBagConstraints txtInputConstraints = new GridBagConstraints();
        GridBagConstraints btnBrowseInputConstraints = new GridBagConstraints();
        GridBagConstraints btnLoadConstraints = new GridBagConstraints();
        GridBagConstraints lblOutputConstraints = new GridBagConstraints();
        GridBagConstraints txtOutputConstraints = new GridBagConstraints();
        GridBagConstraints btnBrowseOutputConstraints = new GridBagConstraints();
        GridBagConstraints btnExportConstraints = new GridBagConstraints();

        //list portion
        if (shouldFill) {
            listConstraints.fill = GridBagConstraints.BOTH;
        }

        if (shouldWeightX) {
            listConstraints.weightx = 0.5;
        }
        if (shouldWeightY) {
            listConstraints.weighty = 1.0;
        }
        listConstraints.gridx = 0;
        listConstraints.gridy = 0;
        listConstraints.gridwidth = 4;
        listConstraints.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(new JScrollPane(waypointList), listConstraints);

        //file chooser portion
        if (shouldFill) {
            lblInputConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            lblInputConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            lblInputConstraints.weighty = 0.0;
        }
        lblInputConstraints.gridx = 0;
        lblInputConstraints.gridy = 1;
        lblInputConstraints.insets = new Insets(10, 0, 0, 10);
        contentPanel.add(lblInput, lblInputConstraints);

        if (shouldFill) {
            txtInputConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        if (shouldWeightX) {
            txtInputConstraints.weightx = 1.0;
        }
        if (shouldWeightY) {
            txtInputConstraints.weighty = 0.0;
        }
        txtInputConstraints.gridx = 1;
        txtInputConstraints.gridy = 1;
        txtInputConstraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(txtInput, txtInputConstraints);

        if (shouldFill) {
            btnBrowseInputConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            btnBrowseInputConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            btnBrowseInputConstraints.weighty = 0.0;
        }
        btnBrowseInputConstraints.gridx = 2;
        btnBrowseInputConstraints.gridy = 1;
        btnBrowseInputConstraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(btnBrowseInput, btnBrowseInputConstraints);

        if (shouldFill) {
            btnLoadConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            btnLoadConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            btnLoadConstraints.weighty = 0.0;
        }
        btnLoadConstraints.gridx = 3;
        btnLoadConstraints.gridy = 1;
        btnLoadConstraints.insets = new Insets(10, 10, 0, 0);
        contentPanel.add(btnLoad, btnLoadConstraints);

        if (shouldFill) {
            lblOutputConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            lblOutputConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            lblOutputConstraints.weighty = 0.0;
        }
        lblOutputConstraints.gridx = 0;
        lblOutputConstraints.gridy = 2;
        lblOutputConstraints.insets = new Insets(10, 0, 0, 10);
        contentPanel.add(lblOutput, lblOutputConstraints);

        if (shouldFill) {
            txtOutputConstraints.fill = GridBagConstraints.HORIZONTAL;
        }
        if (shouldWeightX) {
            txtOutputConstraints.weightx = 1.0;
        }
        if (shouldWeightY) {
            txtOutputConstraints.weighty = 0.0;
        }
        txtOutputConstraints.gridx = 1;
        txtOutputConstraints.gridy = 2;
        txtOutputConstraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(txtOutput, txtOutputConstraints);

        if (shouldFill) {
            btnBrowseOutputConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            btnBrowseOutputConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            btnBrowseOutputConstraints.weighty = 0.0;
        }
        btnBrowseOutputConstraints.gridx = 2;
        btnBrowseOutputConstraints.gridy = 2;
        btnBrowseOutputConstraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(btnBrowseOutput, btnBrowseOutputConstraints);

        if (shouldFill) {
            btnExportConstraints.fill = GridBagConstraints.NONE;
        }
        if (shouldWeightX) {
            btnExportConstraints.weightx = 0.0;
        }
        if (shouldWeightY) {
            btnExportConstraints.weighty = 0.0;
        }
        btnExportConstraints.gridx = 3;
        btnExportConstraints.gridy = 2;
        btnExportConstraints.insets = new Insets(10, 10, 0, 0);
        contentPanel.add(btnExport, btnExportConstraints);
    }

    private void addActionListeners() {
        //waypointlist
        waypointList.addMouseListener(new WaypointUIModelListMouseListener());

        //browseInput
        btnBrowseInput.addActionListener(new BrowseInputListener());

        //load
        btnLoad.addActionListener(new LoadInputListener());

        //browseOutput
        btnBrowseOutput.addActionListener(new BrowseOutputListener());

        //export
        btnExport.addActionListener(new ExportOutputListener());
    }
    
    @Override
    public void update(List<WaypointUIModel> data) {
        WaypointUIModel[] waypointUIModels = new WaypointUIModel[data.size()];
        waypointList = new JList<>(data.toArray(waypointUIModels));
                
        waypointList.repaint();
        mainFrame.validate();
        mainFrame.pack();
        mainFrame.repaint();
    }

    // Handles rendering cells in the list using a check box
    class WaypointUIModelListRenderer extends JCheckBox implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(((WaypointUIModel) value).isExport());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(value.toString());
            return this;
        }
    }

    class WaypointUIModelListMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            JList list = (JList) event.getSource();

            // Get index of item clicked
            int index = list.locationToIndex(event.getPoint());
            WaypointUIModel item = (WaypointUIModel) list.getModel().getElementAt(index);

            // Toggle selected state
            waypointController.toggleWaypointExport(item.getName());
        }
    }

    class BrowseInputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = fileChooser.showOpenDialog(mainFrame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                txtInput.setText(filePath);
                waypointController.loadWaypointsFromGarminFile(filePath);
            }
        }
    }

    class LoadInputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            waypointController.loadWaypointsFromGarminFile(txtInput.getText());
        }
    }

    class BrowseOutputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showSaveDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                txtOutput.setText(filePath);
            }
        }
    }

    class ExportOutputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            waypointController.writeWaypointsToVolkswagenFile(txtOutput.getText());
        }
    }
}