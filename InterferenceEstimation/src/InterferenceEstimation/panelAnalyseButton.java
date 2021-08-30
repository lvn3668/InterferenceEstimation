package InterferenceEstimation;
/*
 * panelAnalyseButton.java
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/**
 * creates a panel containing Analyse Button
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington
 */
public class panelAnalyseButton{
    
    /**
     * Creates a new instance of panelAnalyseButton.
     * Default constructor
     */
    public panelAnalyseButton() {
        try {
			this.setPanel(new JPanel());
			this.setAnalyseButton(new JButton());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns the panel created
     * @return JPanel
     */
    public JPanel getPanel() {
        return this.panel;
    }
    /**
     * returns the button added to the panel
     * @return JPanel
     */
    public JButton getButton() {
        return this.analyseButton;
    }

    private JPanel panel;
    private JButton analyseButton;
    private String labelValue;
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
	 * @return the analyseButton
	 */
	protected JButton getAnalyseButton() {
		return this.analyseButton;
	}
	/**
	 * @param analyseButton the analyseButton to set
	 */
	protected void setAnalyseButton(JButton analyseButton) {
		try {
			this.analyseButton = analyseButton;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the labelValue
	 */
	private String getLabelValue() {
		return this.labelValue;
	}
	/**
	 * @param labelValue the labelValue to set
	 */
	protected void setLabelValue(String labelOnButton) {
		try {
			this.labelValue = labelOnButton;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param panel the panel to set
	 */
	private void setPanel(JPanel panel) {
		try {
			this.panel = panel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
