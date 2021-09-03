package Panels;
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
			setPanel(new JPanel());
			setColumnNames(new Object[6]);
			setResultsArray(new Object[rows][6]);
			setExportResults1(new JButton(Messages.getString("ResultsPanel.0"))); //$NON-NLS-1$
			setExportResults2(new JButton(Messages.getString("ResultsPanel.1"))); //$NON-NLS-1$
			setExportIntermarkerDistances(new JButton(Messages.getString("ResultsPanel.2"))); //$NON-NLS-1$
			setSimsPanel(isSims);
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
        return getScrollPanePanel();
    }
    /**
     * returns table containing results
     * @return JTable
     */
    public JTable getTable() {
        return this.table;
    }
    /**
     * returns button displaying 'Export Results'
     * @return JButton
     */
    public JButton getResults1Button() {
        return getExportResults1();
    }
    public JButton getResults2Button() {
        return getExportResults2();
    }
    /**
     * returns button 'Export distances'
     * @return JButton
     */
    public JButton getDistancesButton() {
        return getExportIntermarkerDistances();
    }
    /**
     * returns array of values added to the display panel for displaying results
     * @return Object[]
     */
    public Vector<String> getArray() {
        try {
			Vector<String> results = new Vector<String>();
			String resultLine = ""; //$NON-NLS-1$
			for(int counter=0;counter<getResultsArray().length;counter++) {
			    resultLine=""; //$NON-NLS-1$
			    for(int innercounter= 0;innercounter < 6;innercounter++) {
			        if(getResultsArray()[counter][innercounter]==null)
			            getResultsArray()[counter][innercounter]="-"; //$NON-NLS-1$
			        resultLine =  resultLine + getResultsArray()[counter][innercounter].toString()+"  "; //$NON-NLS-1$
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
    public void addTableToPanel() {
        try {
			setTable(new JTable(getResultsArray(), getColumnNames()));
			getTable().setBackground(Color.white);
			getTable().setFont(new Font("ArialBold",Font.BOLD, 12)); //$NON-NLS-1$
			getTable().setCellSelectionEnabled(false);
			getPanel().setBorder(BorderFactory.createEtchedBorder());
			setScrollPanePanel(new JScrollPane(getPanel(),ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));
			getPanel().removeAll();
			getPanel().setLayout(new GridBagLayout());
			getPanel().add(getTable(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
			
			JPanel intermediatePanel = new JPanel();
			intermediatePanel.setLayout(new BoxLayout(intermediatePanel,BoxLayout.X_AXIS));
			if(!isSimsPanel()){
			intermediatePanel.add(getExportResults1());
			intermediatePanel.add(getExportIntermarkerDistances());
			                 } else {
			intermediatePanel.add(getExportResults2());}
			
			getPanel().add(intermediatePanel,new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * sepcifying whether the panel is being displayed in the simulations pane or in the analyses pane
     * @param operation sepcifying whether the panel is being displayed in the simulations pane or in the analyses pane
     */
    public void initialiseTable(String operation) {
        try {
			getColumnNames()[0] = Messages.getString("ResultsPanel.8"); //$NON-NLS-1$
			getColumnNames()[1] = Messages.getString("ResultsPanel.9"); //$NON-NLS-1$
			getColumnNames()[2] = Messages.getString("ResultsPanel.10"); //$NON-NLS-1$
			getColumnNames()[3] = Messages.getString("ResultsPanel.11"); //$NON-NLS-1$
			getColumnNames()[4] = Messages.getString("ResultsPanel.12"); //$NON-NLS-1$
			getColumnNames()[5] =  Messages.getString("ResultsPanel.13"); //$NON-NLS-1$
			
			getResultsArray()[0][0] = Messages.getString("ResultsPanel.14"); //$NON-NLS-1$
			getResultsArray()[0][1] = Messages.getString("ResultsPanel.15"); //$NON-NLS-1$
			getResultsArray()[0][2] = "counting_model"; //$NON-NLS-1$
			getResultsArray()[0][3] = "extended_model"; //$NON-NLS-1$
			getResultsArray()[0][4] = "extended_model"; //$NON-NLS-1$
			getResultsArray()[0][5] = Messages.getString("ResultsPanel.19"); //$NON-NLS-1$

			getResultsArray()[1][1] = "m"; //$NON-NLS-1$
			getResultsArray()[1][2] = Messages.getString("ResultsPanel.21"); //$NON-NLS-1$
			getResultsArray()[1][3] = Messages.getString("ResultsPanel.22"); //$NON-NLS-1$
			getResultsArray()[1][4] = Messages.getString("ResultsPanel.23"); //$NON-NLS-1$
			getResultsArray()[1][5] = "negative_log_likelihood"; //$NON-NLS-1$
			
			getResultsArray()[1][0] = operation;

			for(int row=2;row < getResultsArray().length; row++)
			    for(int counter=0; ((counter < 6 && row !=1) || (counter >0 && counter < 6)) ; counter++)
			        getResultsArray()[row][counter]=new String();
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

    public void addSampleSize(Integer sampleSize) {
        try {
			getResultsArray()[2][0] = sampleSize;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void addResultsFromNullModelSimulations(Integer nullMValue, Double nullMNegLog) {
        try {
			getResultsArray()[2][1] = nullMValue;
			getResultsArray()[2][2] = nullMNegLog;
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
     * @param teststatisticsunderalternatemodel test statistics under alternate model
     */
    public void addAltData(Integer altMValue, Double altPValue, Double altMNegLog, Integer teststatisticsunderalternatemodel) {
        try {
			getResultsArray()[2][3] = altMValue;
			getResultsArray()[2][4] = altPValue;
			getResultsArray()[2][5] = altMNegLog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param simulatedNullMVals 
     * @param simulatedNegLogLikeVals 
     */
    public void addSimulationsData(Integer[] simulatedNullMVals, Double[] simulatedNegLogLikeVals) {
        
        try {
			for(int count = 2; count < getResultsArray().length;count++) {
			    getResultsArray()[count][0]=String.valueOf(count-1); 
			    getResultsArray()[count][1]=simulatedNullMVals[count-2];
			    getResultsArray()[count][2]=simulatedNegLogLikeVals[count-2];
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param simulatedAltMVals 
     * @param simulatedAltPVals 
     * @param simulatedAltNegLogLikeVals 
     */
    public void addSimulationsData(Integer[] simulatedAltMVals, Double[] simulatedAltPVals, Double[] simulatedAltNegLogLikeVals) {
        try {
			for(int count = 2; count < getResultsArray().length;count++) {
			    getResultsArray()[count][0]=String.valueOf(count-1); 
			    getResultsArray()[count][3]=simulatedAltMVals[count-2];
			    getResultsArray()[count][4]=simulatedAltPVals[count-2];
			    getResultsArray()[count][5]=simulatedAltNegLogLikeVals[count-2];
			    
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
	 * @return the columnNames
	 */
	public Object[] getColumnNames() {
		return  this.columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(Object[] columnNames) {
		try {
			this.columnNames = columnNames;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the resultsArray
	 */
	public Object[][] getResultsArray() {
		return this.resultsArray;
	}
	/**
	 * @param resultsArray the resultsArray to set
	 */
	public void setResultsArray(Object[][] resultsArray) {
		try {
			this.resultsArray = resultsArray;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(JTable table) {
		try {
			this.table = table;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return this.panel;
	}
	/**
	 * @param panel the panel to set
	 */
	public void setPanel(JPanel panel) {
		try {
			this.panel = panel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the scrollPanePanel
	 */
	public JScrollPane getScrollPanePanel() {
		return this.scrollPanePanel;
	}
	/**
	 * @param scrollPanePanel the scrollPanePanel to set
	 */
	public void setScrollPanePanel(JScrollPane scrollPanePanel) {
		try {
			this.scrollPanePanel = scrollPanePanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the isSimsPanel
	 */
	public boolean isSimsPanel() {
		return this.isSimsPanel;
	}
	/**
	 * @param isSimsPanel the isSimsPanel to set
	 */
	public void setSimsPanel(boolean isSimsPanel) {
		try {
			this.isSimsPanel = isSimsPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the exportResults1
	 */
	public JButton getExportResults1() {
		return this.exportResults1;
	}
	/**
	 * @param exportResults1 the exportResults1 to set
	 */
	public void setExportResults1(JButton exportResults1) {
		try {
			this.exportResults1 = exportResults1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the exportIntermarkerDistances
	 */
	public JButton getExportIntermarkerDistances() {
		return this.exportIntermarkerDistances;
	}
	/**
	 * @param exportIntermarkerDistances the exportIntermarkerDistances to set
	 */
	public void setExportIntermarkerDistances(JButton exportIntermarkerDistances) {
		try {
			this.exportIntermarkerDistances = exportIntermarkerDistances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the exportResults2
	 */
	public JButton getExportResults2() {
		return this.exportResults2;
	}
	/**
	 * @param exportResults2 the exportResults2 to set
	 */
	public void setExportResults2(JButton exportResults2) {
		try {
			this.exportResults2 = exportResults2;
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
