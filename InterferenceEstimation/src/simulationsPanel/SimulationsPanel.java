package simulationsPanel;
/*
 * SimulationsPanel.java
 *
 * Initially Created on January 17, 2005, 9:44 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import InterferenceEstimation.FileAndButtonPanel;
import InterferenceEstimation.panelAnalyseButton;
import InterferenceEstimation.panelCheckBoxes;
import InterferenceEstimation.panelComboBox;

import java.io.*;
import java.util.*;

/**
 * Creates Simulations panel
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington 
 */
public class SimulationsPanel{
    
    /**
     * Creates a new instance of SimulationsPanel; default constructor for Simulations panel
     */
    public SimulationsPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        fileAndButtonPanel = new FileAndButtonPanel();
        panelWithNullMComboBoxes = new panelComboBox();
        
        altpTextBox = new JTextField();
        altpTextBox.setColumns(5);
        altpTextBox.setText("0");
        
        analyseButtonPanel  = new panelAnalyseButton();
        analyseButtonPanel.createPanel("Analyze");
        
        altPPanel = new JPanel();
        
        panelWithCheckBoxForAnalyseOptions = new panelCheckBoxes("Analyze using counting model", "Analyze using extended counting model");
    }
    
    /**
     * returns combo box containing m values for the null model
     * @return JComboBox
     */
    public panelComboBox getNullMComboBoxes() {
        return panelWithNullMComboBoxes;
    }
  
    /**
     * returns panel created
     * @return JPanel
     */
    public JPanel getPanel() {
        return mainPanel;
    }
    
    /**
     * returns file and button panel created as a part of the simulations panel
     * @return FileAndButtonPanel
     */
    public FileAndButtonPanel getFilePanel() {
        return fileAndButtonPanel;
    }
    
    /**
     * adds a scrollpane to the results panel
     * @param jscrollPane JScrollPane to be added
     * @param xcoord x coordinate where scroll pane is to be added
     * @param ycoord y coordinate where scroll pane is to be added
     */
    public void add(JScrollPane jscrollPane, int xcoord, int ycoord) {
        mainPanel.add(jscrollPane, new GridBagConstraints(xcoord, ycoord, 100, 100, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0), 8);
    }
    
    /**
     * create simulations panel
     */
    public void createPanel() {
        fileAndButtonPanel.createPanel("Load distances");
        fileAndButtonPanel.getButton().setToolTipText("Enter a file containing intermarker distances");
        fileAndButtonPanel.getTextArea().setToolTipText("File containing intermarker distances");
        
        mainPanel.add(fileAndButtonPanel.getPanel(),new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),0);
        
        panelWithComboBox.createPanel("Number of Simulations",5,500,5,5);
        mainPanel.add(panelWithComboBox.getPanel(),new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),1);
        sampleSize = new JLabel("Sample Size: ");
        sampleSize.setFont(new Font("ArialBold",Font.BOLD, 15));
        sampleSize.setToolTipText("Choose sample size");
        
        sampleSizeTextBox = new JTextField();
        sampleSizeTextBox.setColumns(5);
        sampleSizeTextBox.setToolTipText("Enter an integer sample size greater than 0");
        
        sampleSizePanel = new JPanel();
        sampleSizePanel.setLayout(new GridBagLayout());
        sampleSizePanel.add(sampleSize, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
        sampleSizePanel.add(sampleSizeTextBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
        
        mainPanel.add(sampleSizePanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10), 2);

        JLabel title = new JLabel("  \n  Parameters for the model for the simulation  \n  ");
        title.setFont(new Font("ArialBold",Font.ITALIC, 16));
        mainPanel.add(title,new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 40),3);
        
        panelWithNullMComboBoxes.createPanel("Interference parameter (m): ", 0, 20,1,0);
        mainPanel.add(panelWithNullMComboBoxes.getPanel(),new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0),4);
    
        pvalue = new JLabel("Proportion of non-interfering crossovers (p): ");
        pvalue.setFont(new Font("ArialBold",Font.BOLD, 15));
        pvalue.setToolTipText("Enter a value between 0 and 1");
        
        altPPanel.add(pvalue, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
        altPPanel.add(altpTextBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
        mainPanel.add(altPPanel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),5);
        panelWithCheckBoxForAnalyseOptions.createPanel(2);
        mainPanel.add(panelWithCheckBoxForAnalyseOptions.getPanel(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),6);
        mainPanel.add(analyseButtonPanel.getPanel(),new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10), 7);
    }
    
    /**
     * returns panel with check boxes
     * @return panelCheckBoxes
     */
    public panelCheckBoxes getPanelWithCheckBoxesForAnalyseOptions() {
        return panelWithCheckBoxForAnalyseOptions;
    }
    /**
     * returns panel containing Altp label and associated text field
     * @return returns panel containing Altp label and associated text field
     */
    public JPanel getAltpPanel() {
        return altPPanel;
    }
    /**
     * returns the label for the Altp panel
     * @return JLabel
     */
    public JTextField getAltP() {
        return altpTextBox;
    }
    // Null m combo boxes
    /**
     * returns panel created as a part of the simulations panel
     * @return JPanel
     */
    public JPanel getPanel1() {
        return panel1;
    }
    /**
     * returns label for the sample size field
     * @return JLabel
     */
    public JLabel getSampleSizeLabel() {
        return sampleSize;
    }
    /**
     * returns text box reading in the sample size values
     * @return JTextBox
     */
    public JTextField getSampleSizeTextBox() {
        return sampleSizeTextBox;
    }
    /**
     * returns label
     * @return JLabel
     */
    public JLabel getLabel() {
        return pvalue;
    }
    // Alt m combo boxes
    /**
     * 
     * @return 
     */
    public JPanel getPanel2() {
        return panel2;
    }
    /**
     * returns panel containing NullM combo boxes
     * @return panelWithComboBoxes
     */
    public panelComboBox getpanelWithNullMComboBoxes() {
        return panelWithNullMComboBoxes;
    }
    
    /**
     * returns panel containing combo box for number of simulations
     * @return panelComboBox
     */
    public panelComboBox getpanelWithNumberOfSimulations() {
        return panelWithComboBox;
    }
    
    /**
     * returns panel containing Analyse button
     * @return panelAnalyseButton
     */
    public panelAnalyseButton getpanelWithAnalyseButton() {
        return analyseButtonPanel;
    }
    
    public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	private FileAndButtonPanel fileAndButtonPanel;
    private JPanel mainPanel, altPPanel;
    private JLabel pvalue, sampleSize;
    private panelComboBox panelWithNullMComboBoxes = new panelComboBox();
    private panelComboBox panelWithComboBox = new panelComboBox();
    private panelCheckBoxes panelWithCheckBoxForAnalyseOptions = new panelCheckBoxes("Analyze using counting model", "Analyze using extended counting model");
    private panelAnalyseButton analyseButtonPanel = new panelAnalyseButton();
    private JPanel panel1, panel2, sampleSizePanel;
    private ButtonGroup buttonGroup;
    private JTextField altpTextBox, sampleSizeTextBox;
}

