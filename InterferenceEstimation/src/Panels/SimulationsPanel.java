package Panels;
/*
 * SimulationsPanel.java
 *
 * Initially Created on January 17, 2005, 9:44 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import InterferenceEstimation.UIUtility;

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
        try {
			setMainPanel(new JPanel());
			getMainPanel().setLayout(new GridBagLayout());
			
			setFileAndButtonPanel(new FileAndButtonPanel());
			setPanelWithNullMComboBoxes(new UIUtility());
			
			setAltpTextBox(new JTextField());
			getAltpTextBox().setColumns(5);
			getAltpTextBox().setText("0"); //$NON-NLS-1$
			
			setAnalyseButtonPanel(new panelAnalyseButton());
			getAnalyseButtonPanel().getUtility();
			UIUtility.createPanel("Analyze"); //$NON-NLS-1$
			
			setAltPPanel(new JPanel());
			setPanelWithCheckBoxForAnalyseOptions(new panelCheckBoxes("Analyze using counting model", "Analyze using extended counting model")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * returns combo box containing m values for the null model
     * @return JComboBox
     */
    public UIUtility getNullMComboBoxes() {
        return getPanelWithNullMComboBoxes();
    }
  
    /**
     * returns panel created
     * @return JPanel
     */
    public JPanel getPanel() {
        return getMainPanel();
    }
    
    /**
     * returns file and button panel created as a part of the simulations panel
     * @return FileAndButtonPanel
     */
    public FileAndButtonPanel getFilePanel() {
        return getFileAndButtonPanel();
    }
    
    /**
     * adds a scrollpane to the results panel
     * @param jscrollPane JScrollPane to be added
     * @param xcoord x coordinate where scroll pane is to be added
     * @param ycoord y coordinate where scroll pane is to be added
     */
    public void add(JScrollPane jscrollPane, int xcoord, int ycoord) {
        try {
			getMainPanel().add(jscrollPane, new GridBagConstraints(xcoord, ycoord, 100, 100, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0), 8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * create simulations panel
     */
    public void createPanel() {
        try {
			getFileAndButtonPanel().createPanel("Load distances"); //$NON-NLS-1$
			getFileAndButtonPanel().getButton().setToolTipText("Enter a file containing intermarker distances"); //$NON-NLS-1$
			getFileAndButtonPanel().getTextArea().setToolTipText("File containing intermarker distances"); //$NON-NLS-1$
			
			getMainPanel().add(getFileAndButtonPanel().getPanel(),new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),0);
			
			getPanelWithComboBox().createPanel("Number of Simulations",5,500,5,5); //$NON-NLS-1$
			getMainPanel().add(getPanelWithComboBox().getPanel(),new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),1);
			setSampleSize(new JLabel("Sample Size: ")); //$NON-NLS-1$
			getSampleSize().setFont(new Font("ArialBold",Font.BOLD, 15)); //$NON-NLS-1$
			getSampleSize().setToolTipText("Choose sample size"); //$NON-NLS-1$
			
			setSampleSizeTextBox(new JTextField());
			getSampleSizeTextBox().setColumns(5);
			getSampleSizeTextBox().setToolTipText("Enter an integer sample size greater than 0"); //$NON-NLS-1$
			
			setSampleSizePanel(new JPanel());
			getSampleSizePanel().setLayout(new GridBagLayout());
			getSampleSizePanel().add(getSampleSize(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
			getSampleSizePanel().add(getSampleSizeTextBox(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
			
			getMainPanel().add(getSampleSizePanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10), 2);

			JLabel title = new JLabel("  \n  Parameters for the model for the simulation  \n  "); //$NON-NLS-1$
			title.setFont(new Font("ArialBold",Font.ITALIC, 16)); //$NON-NLS-1$
			getMainPanel().add(title,new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 40),3);
			
			getPanelWithNullMComboBoxes().createPanel("Interference parameter (m): ", 0, 20,1,0); //$NON-NLS-1$
			getMainPanel().add(getPanelWithNullMComboBoxes().getPanel(),new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0),4);
   
			setPvalue(new JLabel("Proportion of non-interfering crossovers (p): ")); //$NON-NLS-1$
			getPvalue().setFont(new Font("ArialBold",Font.BOLD, 15)); //$NON-NLS-1$
			getPvalue().setToolTipText("Enter a value between 0 and 1"); //$NON-NLS-1$
			
			getAltPPanel().add(getPvalue(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 0);
			getAltPPanel().add(getAltpTextBox(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 1);
			getMainPanel().add(getAltPPanel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),5);
			getPanelWithCheckBoxForAnalyseOptions().getUtility().createPanel(2);
			getMainPanel().add(getPanelWithCheckBoxForAnalyseOptions().getPanel(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10),6);
			getMainPanel().add(getAnalyseButtonPanel().getPanel(),new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 10), 7);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * returns panel with check boxes
     * @return panelCheckBoxes
     */
    public panelCheckBoxes getPanelWithCheckBoxesForAnalyseOptions() {
        return getPanelWithCheckBoxForAnalyseOptions();
    }
    /**
     * returns panel containing Altp label and associated text field
     * @return returns panel containing Altp label and associated text field
     */
    public JPanel getAltpPanel() {
        return getAltPPanel();
    }
    /**
     * returns the label for the Altp panel
     * @return JLabel
     */
    public JTextField getAltP() {
        return getAltpTextBox();
    }
    // Null m combo boxes
    /**
     * returns panel created as a part of the simulations panel
     * @return JPanel
     */
    public JPanel getPanel1() {
        return this.panel1;
    }
    /**
     * returns label for the sample size field
     * @return JLabel
     */
    public JLabel getSampleSizeLabel() {
        return getSampleSize();
    }
    /**
     * returns text box reading in the sample size values
     * @return JTextBox
     */
    public JTextField getSampleSizeTextBox() {
        return this.sampleSizeTextBox;
    }
    /**
     * returns label
     * @return JLabel
     */
    public JLabel getLabel() {
        return getPvalue();
    }
    // Alt m combo boxes
    /**
     * 
     * @return 
     */
    public JPanel getPanel2() {
        return this.panel2;
    }
    /**
     * returns panel containing NullM combo boxes
     * @return panelWithComboBoxes
     */
    public UIUtility getpanelWithNullMComboBoxes() {
        return getPanelWithNullMComboBoxes();
    }
    
    /**
     * returns panel containing combo box for number of simulations
     * @return UIUtility
     */
    public UIUtility getpanelWithNumberOfSimulations() {
        return getPanelWithComboBox();
    }
    
    /**
     * returns panel containing Analyse button
     * @return panelAnalyseButton
     */
    public panelAnalyseButton getpanelWithAnalyseButton() {
        return getAnalyseButtonPanel();
    }
    
    public ButtonGroup getButtonGroup() {
		return this.buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	/**
	 * @return the fileAndButtonPanel
	 */
	public FileAndButtonPanel getFileAndButtonPanel() {
		return this.fileAndButtonPanel;
	}

	/**
	 * @param fileAndButtonPanel the fileAndButtonPanel to set
	 */
	public void setFileAndButtonPanel(FileAndButtonPanel fileAndButtonPanel) {
		try {
			this.fileAndButtonPanel = fileAndButtonPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the mainPanel
	 */
	public JPanel getMainPanel() {
		return this.mainPanel;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(JPanel mainPanel) {
		try {
			this.mainPanel = mainPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the panelWithComboBox
	 */
	public UIUtility getPanelWithComboBox() {
		return this.panelWithComboBox;
	}

	/**
	 * @param panelWithComboBox the panelWithComboBox to set
	 */
	public void setPanelWithComboBox(UIUtility panelWithComboBox) {
		try {
			this.panelWithComboBox = panelWithComboBox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the sampleSize
	 */
	public JLabel getSampleSize() {
		return this.sampleSize;
	}

	/**
	 * @param sampleSize the sampleSize to set
	 */
	public void setSampleSize(JLabel sampleSize) {
		try {
			this.sampleSize = sampleSize;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param sampleSizeTextBox the sampleSizeTextBox to set
	 */
	public void setSampleSizeTextBox(JTextField sampleSizeTextBox) {
		try {
			this.sampleSizeTextBox = sampleSizeTextBox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the sampleSizePanel
	 */
	public JPanel getSampleSizePanel() {
		return this.sampleSizePanel;
	}

	/**
	 * @param sampleSizePanel the sampleSizePanel to set
	 */
	public void setSampleSizePanel(JPanel sampleSizePanel) {
		try {
			this.sampleSizePanel = sampleSizePanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the panelWithNullMComboBoxes
	 */
	public UIUtility getPanelWithNullMComboBoxes() {
		return this.panelWithNullMComboBoxes;
	}

	/**
	 * @param panelWithNullMComboBoxes the panelWithNullMComboBoxes to set
	 */
	public void setPanelWithNullMComboBoxes(UIUtility panelWithNullMComboBoxes) {
		try {
			this.panelWithNullMComboBoxes = panelWithNullMComboBoxes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the pvalue
	 */
	public JLabel getPvalue() {
		return this.pvalue;
	}

	/**
	 * @param pvalue the pvalue to set
	 */
	public void setPvalue(JLabel pvalue) {
		try {
			this.pvalue = pvalue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the altPPanel
	 */
	public JPanel getAltPPanel() {
		return this.altPPanel;
	}

	/**
	 * @param altPPanel the altPPanel to set
	 */
	public void setAltPPanel(JPanel altPPanel) {
		try {
			this.altPPanel = altPPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the analyseButtonPanel
	 */
	public panelAnalyseButton getAnalyseButtonPanel() {
		return this.analyseButtonPanel;
	}

	/**
	 * @param analyseButtonPanel the analyseButtonPanel to set
	 */
	public void setAnalyseButtonPanel(panelAnalyseButton analyseButtonPanel) {
		try {
			this.analyseButtonPanel = analyseButtonPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the altpTextBox
	 */
	public JTextField getAltpTextBox() {
		return this.altpTextBox;
	}

	/**
	 * @param altpTextBox the altpTextBox to set
	 */
	public void setAltpTextBox(JTextField altpTextBox) {
		try {
			this.altpTextBox = altpTextBox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the panelWithCheckBoxForAnalyseOptions
	 */
	public panelCheckBoxes getPanelWithCheckBoxForAnalyseOptions() {
		return this.panelWithCheckBoxForAnalyseOptions;
	}

	/**
	 * @param panelWithCheckBoxForAnalyseOptions the panelWithCheckBoxForAnalyseOptions to set
	 */
	public void setPanelWithCheckBoxForAnalyseOptions(panelCheckBoxes panelWithCheckBoxForAnalyseOptions) {
		try {
			this.panelWithCheckBoxForAnalyseOptions = panelWithCheckBoxForAnalyseOptions;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FileAndButtonPanel fileAndButtonPanel;
    private JPanel mainPanel, altPPanel;
    private JLabel pvalue, sampleSize;
    private UIUtility panelWithNullMComboBoxes = new UIUtility();
    private UIUtility panelWithComboBox = new UIUtility();
    private panelCheckBoxes panelWithCheckBoxForAnalyseOptions = new panelCheckBoxes("Analyze using counting model", "Analyze using extended counting model"); //$NON-NLS-1$ //$NON-NLS-2$
    private panelAnalyseButton analyseButtonPanel = new panelAnalyseButton();
    private JPanel panel1, panel2, sampleSizePanel;
    private ButtonGroup buttonGroup;
    private JTextField altpTextBox, sampleSizeTextBox;
}

