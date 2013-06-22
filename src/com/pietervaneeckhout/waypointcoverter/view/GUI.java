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
import com.pietervaneeckhout.waypointcoverter.exceptions.FatalException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileExistsException;
import com.pietervaneeckhout.waypointcoverter.model.WaypointUI;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

/**
 * GUI.java (UTF-8)
 *
 * <p>Graphical UI representation.</p>
 *
 * 2013/06/19
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.1
 * @version 1.0.2
 */
public class GUI extends BaseUI {

    private final static boolean shouldFill = true;
    private final static boolean shouldWeightX = true;
    private final static boolean shouldWeightY = true;
    private final static boolean RIGHT_TO_LEFT = false;
    private JFrame mainFrame;
    private JPanel contentPanel;
    private JScrollPane tableScrollPane;
    private WaypointTable waypointTable;
    private JLabel lblInput, lblOutput;
    private JButton btnBrowseInput, btnBrowseOutput, btnLoad, btnExport;
    private JTextField txtInput, txtOutput;
    private JMenuBar menuBar;
    private JMenu fileMenu, settingsMenu, infoMenu;
    private JMenuItem resetMenuItem, helpMenuItem, sourceMenuItem, licenseMenuItem, exitMenuItem;

    public GUI(DomainFacade domainFacade) {
        super(domainFacade);
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
        waypointTable = new WaypointTable();
        tableScrollPane = new JScrollPane(waypointTable);
        btnBrowseInput = new JButton("Browse");
        btnBrowseOutput = new JButton("Browse");
        btnLoad = new JButton("load");
        btnExport = new JButton("export");
        txtInput = new JTextField();
        txtOutput = new JTextField();
        lblInput = new JLabel("input file:");
        lblOutput = new JLabel("output file:");

        //table special settings
        waypointTable.setPreferredScrollableViewportSize(new Dimension(470, 200));
        waypointTable.setFillsViewportHeight(true);

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
        Dimension minDimension = new Dimension(350, 250);
        Dimension prefDimension = new Dimension(700, 500);
        contentPanel.setMinimumSize(minDimension);
        contentPanel.setPreferredSize(prefDimension);
        contentPanel.setSize(prefDimension);
        mainFrame.setMinimumSize(minDimension);
        mainFrame.setSize(prefDimension);
        mainFrame.setPreferredSize(prefDimension);
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
        GridBagConstraints tableConstraints = new GridBagConstraints();
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
            tableConstraints.fill = GridBagConstraints.BOTH;
        }

        if (shouldWeightX) {
            tableConstraints.weightx = 0.5;
        }
        if (shouldWeightY) {
            tableConstraints.weighty = 1.0;
        }
        tableConstraints.gridx = 0;
        tableConstraints.gridy = 0;
        tableConstraints.gridwidth = 4;
        tableConstraints.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(tableScrollPane, tableConstraints);

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
        //waypointTable
        //TODO

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
    public void update(List<WaypointUI> data) {
        if (waypointTable != null) {
            waypointTable.setData(data);
            //waypointTable.revalidate();
            //waypointTable.repaint();
            tableScrollPane.revalidate();
            tableScrollPane.repaint();
        }
    }

    class WaypointUITableModel extends AbstractTableModel {

        private final String[] columnNames = {"Export", "Name", "Latitude", "Longgitude"};
        private List<WaypointUI> data;

        public WaypointUITableModel() {
            data = new ArrayList<>();
        }

        public void setData(List<WaypointUI> data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            WaypointUI waypoint = data.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return waypoint.isExport();
                case 1:
                    return waypoint.getName();
                case 2:
                    return waypoint.getLatitude();
                case 3:
                    return waypoint.getLongitude();
                default:
                    throw new IndexOutOfBoundsException("columnIndex:" + columnIndex + " is out of bounds");
            }
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if (columnIndex ==0) {
                if (value instanceof Boolean) {
                   domainFacade.toggleWaypointExport(data.get(rowIndex).getName());
                }
            }
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return (col == 0);
        }
    }

    class WaypointTable extends JTable {

        public WaypointTable() {
            super(new WaypointUITableModel());
        }

        public void setData(List<WaypointUI> data) {
            WaypointUITableModel tableModel = (WaypointUITableModel) super.getModel();
            tableModel.setData(data);
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
                try {
                    domainFacade.loadFile(filePath);
                } catch (IOException ex) {
                    //TODO
                } catch (FatalException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class LoadInputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                domainFacade.loadFile(txtInput.getText());
            } catch (IOException ex) {
                    //TODO
                } catch (FatalException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
            try {
                domainFacade.exportWaypoints(txtOutput.getText(), false);
                JOptionPane.showMessageDialog(mainFrame, "Export completed", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileExistsException ex) {
                int overwriteResponse = JOptionPane.showConfirmDialog(mainFrame, ex.getMessage()+"\\Do you want to overwrite this file?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (overwriteResponse == JOptionPane.OK_OPTION) {
                    try {
                        domainFacade.exportWaypoints(txtOutput.getText(), true);
                        JOptionPane.showMessageDialog(mainFrame, "Export completed", "Export", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex1) {
                        JOptionPane.showMessageDialog(mainFrame, "Export Failed: " + ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                     JOptionPane.showMessageDialog(mainFrame, "Export cancelled", "Export", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Export Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}