package HelpPanel;
/*
 * HelpPanel.java
 *
 * Initially Created on April 15, 2005, 12:40 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import InterferenceEstimation.UI;
import InterferenceEstimation.UIUtility;
import Panels.FileAndButtonPanel;
import Panels.panelAnalyseButton;
import Panels.panelCheckBoxes;

import java.io.*;
import java.util.*;

/**
 * Panel that displays information about software and its authors
 * @authors Elizabeth Housworth (Dept of Mathematics, Indiana University Bloomington) and Lalitha Viswanath 
 */
public class HelpPanel {
    
    private UIUtility utility;
	/**
	 * @return the utility
	 */
	public UIUtility getUtility() {
		return this.utility;
	}
	/**
	 * @param utility the utility to set
	 */
	private void setUtility(UIUtility utility) {
		this.utility = utility;
	}
	/**
	 * @return the mainPanel
	 */
	private JPanel getMainPanel() {
		return this.mainPanel;
	}
	/**
	 * @param mainPanel the mainPanel to set
	 */
	private void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	/** Creates a new instance of HelpPanel */
    public HelpPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
    }
    /**
     * returns the panel that is created as an instance of the class
     * @return JPanel containing all the lines of text (components)
     */
    public JPanel getPanel()
    {
        return mainPanel;
    }
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param path path to specified file
     * @param description String: description that is to be displayed along with the icon
     * @return Image Icon that is returned
     */
    protected static ImageIcon createImageIcon(String path,
                                           String description) {
    java.net.URL imgURL = HelpPanel.class.getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL, description);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
    }
    /**
     * creates the panel with the specified text
     */
   /** private void createPanel()
    {
        
        topLabel = new JLabel("Version 1.0 comprises Lalitha Viswanath's thesis for a Masters Degree in Bioinformatics (2005)");
        topLabel.setFont(new Font("ArialBold",Font.BOLD, 12));
        mainPanel.add(topLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 20), 0);

        paperLabel1 = new JLabel("Funding for this work came from an NSF grant (DMS 0306243) to Elizabeth Housworth");
        paperLabel1.setFont(new Font("ArialBold",Font.BOLD, 12));
  
        paperLabel2 = new JLabel("Authors: Dr. Elizabeth Housworth and Lalitha Viswanath");
        paperLabel2.setFont(new Font("ArialBold",Font.BOLD+Font.ITALIC, 12));
        
        
        paperLabel3 = new JLabel("Contact: ehouswor@indiana.edu");
        paperLabel3.setFont(new Font("ArialBold",Font.BOLD, 12));
        
        paperLabel4 = new JLabel("For additional help please refer to the reference at http://mypage.iu.edu/InterferenceAnalyzer/InterferenceAnalyzer.pdf");
        paperLabel4.setFont(new Font("ArialBold",Font.BOLD, 12));
        
        mainPanel.add(paperLabel1,new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 10), 1);
        mainPanel.add(paperLabel2,new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 10), 2);
        mainPanel.add(paperLabel3,new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 10), 3);
        mainPanel.add(paperLabel4,new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 10), 4);
        
    } **/
     
 public void createPanel(UI ui) {
	    try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			ui.setSize((screenSize.width)*70/100, (screenSize.height)*70/100);
			ui.setVisible(true);
			ui.setTitle("TetradData Parameter Estimation");
			ui.panelForLoadingMainFile = new FileAndButtonPanel();
			ui.panelForLoadingMainFile.createPanel("Load File");
			ui.panelForLoadingMainFile.getButton().addActionListener(ui.panelForLoadingMainFile);
			
			ui.panelWithCheckBoxes = new panelCheckBoxes("Analyze using counting model", "Analyze using extended counting model");
			ui.panelWithCheckBoxes.getUtility().createPanel(2);
			
			ui.panelWithAnalyseButton = new panelAnalyseButton();
			UIUtility.createPanel("Analyze");
			
			ui.panelWithCheckBoxes.getNullCheckBox().addItemListener(ui);
			ui.panelWithCheckBoxes.getAltModelCheckBox().addItemListener(ui);
			ui.panelWithAnalyseButton.getButton().addMouseListener(ui);
			
			ui.mainPanel = new JPanel();
			ui.mainPanel.setLayout(new GridBagLayout());
			ui.mainPanel.add(ui.panelForLoadingMainFile.getPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0),0);
			ui.mainPanel.add(ui.panelWithCheckBoxes.getPanel(),  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0),  1);
			ui.mainPanel.add(ui.panelWithAnalyseButton.getPanel(),  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 2);       
			
	  ui.tabbedPane.addTab("Analyze Raw Data",ui.mainPanel);
			ui.getContentPane().add(ui.tabbedPane);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

private JPanel mainPanel;
}
