package InterferenceEstimation;
/*
 * ResultsPanel.java
 *
 * Initially Created on January 23, 2005, 8:00 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
/**
 * Class for generating results panel
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington 
 */
public class ResultsPanel {
    
    /**
     * Creates a new instance of ResultsPanel
     * @param rows number of rows to be added in the results pane
     * @param isSims boolean specifying whether the panel is being generated as a part of the simulations panel
     */
    public ResultsPanel(int rows, boolean isSims) {
        try {
			panel = new JPanel();
			columnNames = new Object[6];
			resultsArray = new Object[rows][6];
			exportResults1 = new JButton("Export Results");
			exportResults2 = new JButton("Export Results");
			exportIntermarkerDistances = new JButton("Export Distances");
			isSimsPanel = isSims;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns scroll pane
     * @return JScrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPanePanel;
    }
    /**
     * returns table containing results
     * @return JTable
     */
    public JTable getTable() {
        return table;
    }
    /**
     * returns button displaying 'Export Results'
     * @return JButton
     */
    public JButton getResults1Button() {
        return exportResults1;
    }
    public JButton getResults2Button() {
        return exportResults2;
    }
    /**
     * returns button 'Export distances'
     * @return JButton
     */
    public JButton getDistancesButton() {
        return exportIntermarkerDistances;
    }
    /**
     * returns array of values added to the display panel for displaying results
     * @return Object[]
     */
    Vector<String> getArray() {
        try {
			Vector<String> results = new Vector<String>();
			String resultLine = "";
			for(int counter=0;counter<resultsArray.length;counter++) {
			    resultLine="";
			    for(int innercounter= 0;innercounter < 6;innercounter++) {
			        if(resultsArray[counter][innercounter]==null)
			            resultsArray[counter][innercounter]="-";
			        resultLine =  resultLine + resultsArray[counter][innercounter].toString()+"  ";
			    }
			    results.add(resultLine);
			}
			return results;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    /**
     * adds the table to the results panel
     */
    void addTableToPanel() {
        try {
			table = new JTable(resultsArray, columnNames);
			table.setBackground(Color.white);
			table.setFont(new Font("ArialBold",Font.BOLD, 12));
			table.setCellSelectionEnabled(false);
			panel.setBorder(BorderFactory.createEtchedBorder());
			scrollPanePanel = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			panel.removeAll();
			panel.setLayout(new GridBagLayout());
			panel.add(table, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
			
			JPanel intermediatePanel = new JPanel();
			intermediatePanel.setLayout(new BoxLayout(intermediatePanel,BoxLayout.X_AXIS));
			if(!isSimsPanel){
			intermediatePanel.add(exportResults1);
			intermediatePanel.add(exportIntermarkerDistances);
			                 } else {
			intermediatePanel.add(exportResults2);}
			
			panel.add(intermediatePanel,new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * sepcifying whether the panel is being displayed in the simulations pane or in the analyses pane
     * @param operation sepcifying whether the panel is being displayed in the simulations pane or in the analyses pane
     */
    void initialiseTable(String operation) {
        try {
			columnNames[0] = "Operation";
			columnNames[1] = "mNullModel";
			columnNames[2] = "logNullModel";
			columnNames[3] = "mAltModel";
			columnNames[4] = "pAltModel";
			columnNames[5] =  "logAltModel";
			
			resultsArray[0][0] = "Results";
			resultsArray[0][1] = "counting_model";
			resultsArray[0][2] = "counting_model";
			resultsArray[0][3] = "extended_model";
			resultsArray[0][4] = "extended_model";
			resultsArray[0][5] = "extended_model";

			resultsArray[1][1] = "m";
			resultsArray[1][2] = "negative_log_likelihood";
			resultsArray[1][3] = "m";
			resultsArray[1][4] = "p";
			resultsArray[1][5] = "negative_log_likelihood";
			
			resultsArray[1][0] = operation;

			for(int row=2;row < resultsArray.length; row++)
			    for(int counter=0; ((counter < 6 && row !=1) || (counter >0 && counter < 6)) ; counter++)
			        resultsArray[row][counter]=new String();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * adds specified set of values to results pane
     * @param nullMValue m value under null model
     * @param nullMNegLog Neg log likelihood under null model
     */

    void addSampleSize(int SampleSize) {
        try {
			resultsArray[2][0] = new Integer(SampleSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    void addNullData(int nullMValue, double nullMNegLog) {
        try {
			resultsArray[2][1] = new Integer(nullMValue);
			resultsArray[2][2] = new Double(nullMNegLog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * adds specified set of values to the results panel
     * @param altMValue m value under alternate model
     * @param altPValue p value under alternate model
     * @param altMNegLog neg log likelihood under alternate model
     * @param testStat test statistics under alternate model
     */
    void addAltData(int altMValue, double altPValue, double altMNegLog, double testStat) {
        try {
			resultsArray[2][3] = new Integer(altMValue);
			resultsArray[2][4] = new Double(altPValue);
			resultsArray[2][5] = new Double(altMNegLog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param SimulatedNullMVals 
     * @param SimulatedNegLogLikeVals 
     */
    void addSimulationsData(int[] SimulatedNullMVals, double[] SimulatedNegLogLikeVals) {
        
        try {
			for(int count = 2; count < resultsArray.length;count++) {
			    resultsArray[count][0]="" + (count-1) + "";
			    resultsArray[count][1]=new Integer(SimulatedNullMVals[count-2]);
			    resultsArray[count][2]=new Double(SimulatedNegLogLikeVals[count-2]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param SimulatedAltMVals 
     * @param SimulatedAltPVals 
     * @param SimulatedAltNegLogLikeVals 
     */
    void addSimulationsData(int[] SimulatedAltMVals, double[] SimulatedAltPVals, double[] SimulatedAltNegLogLikeVals) {
        try {
			for(int count = 2; count < resultsArray.length;count++) {
			    resultsArray[count][0]="" + (count-1) + "";
			    resultsArray[count][3]=new Integer(SimulatedAltMVals[count-2]);
			    resultsArray[count][4]=new Double(SimulatedAltPVals[count-2]);
			    resultsArray[count][5]=new Double(SimulatedAltNegLogLikeVals[count-2]);
			    
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private JPanel panel;
    private JButton exportResults1, exportResults2,  exportIntermarkerDistances;
    private JScrollPane scrollPanePanel;
    private Object[] columnNames;
    private Object[][] resultsArray;
    private boolean isSimsPanel;
    private JTable table;
}
