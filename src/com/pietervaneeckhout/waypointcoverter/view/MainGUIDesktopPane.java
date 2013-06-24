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
import com.pietervaneeckhout.waypointcoverter.controller.waypoint.WaypointRepository;
import com.pietervaneeckhout.waypointcoverter.exceptions.FatalException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileAlreadyExistsException;
import com.pietervaneeckhout.waypointcoverter.exceptions.FileException;
import com.pietervaneeckhout.waypointcoverter.exceptions.ProcessingException;
import com.pietervaneeckhout.waypointcoverter.exceptions.WaypointDoesNotExistException;
import com.pietervaneeckhout.waypointcoverter.model.WaypointUI;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

/**
 * MainGUIDesktopPane.java (UTF-8)
 *
 * <p>Graphical UI representation of the main program window.</p>
 *
 * 2013/06/19
 *
 * @author Pieter Van Eeckhout <vaneeckhout.pieter@gmail.com>
 * @since 1.0.1
 * @version 1.2.1
 */
public class MainGUIDesktopPane extends JDesktopPane implements Observer {

    private final static boolean shouldFill = true;
    private final static boolean shouldWeightX = true;
    private final static boolean shouldWeightY = true;
    private final static boolean RIGHT_TO_LEFT = false;
    private Logger logger;
    private JFrame parentFrame;
    private JScrollPane tableScrollPane;
    private WaypointTable waypointTable;
    private JLabel lblInput, lblOutput;
    private JButton btnBrowseInput, btnBrowseOutput, btnLoad, btnExport;
    private JTextField txtInput, txtOutput;
    private DomainFacade domainFacade;

    public MainGUIDesktopPane(JFrame parentFrame, DomainFacade domainFacade) {
        logger = Logger.getLogger(MainGUIDesktopPane.class);
        this.parentFrame = parentFrame;
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's MainGUIDesktopPane.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGUI();
            }
        });
    }

    private void initGUI() {
        //intialise elements
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

        //add items to panel
        addComponentsToPane();

        //addActionListeners
        addActionListeners();

        //set sizes
        Dimension minDimension = new Dimension(350, 250);
        Dimension prefDimension = new Dimension(700, 500);
        this.setMinimumSize(minDimension);
        this.setPreferredSize(prefDimension);
        this.setSize(prefDimension);

        //set drag mode
        this.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
    }

    private void addComponentsToPane() {
        //set a margin around components
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        if (RIGHT_TO_LEFT) {
            this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        this.setLayout(new GridBagLayout());
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
        this.add(tableScrollPane, tableConstraints);

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
        this.add(lblInput, lblInputConstraints);

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
        this.add(txtInput, txtInputConstraints);

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
        this.add(btnBrowseInput, btnBrowseInputConstraints);

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
        this.add(btnLoad, btnLoadConstraints);

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
        this.add(lblOutput, lblOutputConstraints);

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
        this.add(txtOutput, txtOutputConstraints);

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
        this.add(btnBrowseOutput, btnBrowseOutputConstraints);

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
        this.add(btnExport, btnExportConstraints);
    }

    private void addActionListeners() {

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
    @SuppressWarnings("unchecked")
    public void update(Observable observerable, Object dataObject) {
        if (observerable instanceof WaypointRepository && dataObject instanceof List<?> && waypointTable != null && tableScrollPane != null) {
            List<WaypointUI> data = new ArrayList<>();
            try {
               data  = (ArrayList<WaypointUI>) dataObject;
            } catch (ClassCastException ex) {
                logger.error("error casting data for use in GUI");
                //ignore and show no data;
            }
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
            if (columnIndex == 0) {
                if (value instanceof Boolean) {
                    try {
                        domainFacade.toggleWaypointExport(data.get(rowIndex).getName());
                    } catch (WaypointDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(parentFrame, value);
                    }
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

            int returnVal = fileChooser.showOpenDialog(parentFrame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                txtInput.setText(filePath);
                try {
                    domainFacade.loadFile(filePath);
                } catch (FileException | ProcessingException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "import failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (FatalException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "A fatal error occured, closing application", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        }
    }

    class LoadInputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                domainFacade.loadFile(txtInput.getText());
            } catch (FileException | ProcessingException ex) {
                JOptionPane.showMessageDialog(parentFrame, "import failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FatalException ex) {
                JOptionPane.showMessageDialog(parentFrame, "A fatal error occured, closing application", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    class BrowseOutputListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showSaveDialog(parentFrame);
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
                JOptionPane.showMessageDialog(parentFrame, "Export completed", "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileAlreadyExistsException ex) {
                int overwriteResponse = JOptionPane.showConfirmDialog(parentFrame, ex.getMessage() + "\\Do you want to overwrite this file?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (overwriteResponse == JOptionPane.OK_OPTION) {
                    try {
                        domainFacade.exportWaypoints(txtOutput.getText(), true);
                        JOptionPane.showMessageDialog(parentFrame, "Export completed", "Export", JOptionPane.INFORMATION_MESSAGE);
                    } catch (FileException | ProcessingException ex1) {
                        JOptionPane.showMessageDialog(parentFrame, "Export Failed: " + ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Export cancelled", "Export", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (FileException | ProcessingException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Export Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}