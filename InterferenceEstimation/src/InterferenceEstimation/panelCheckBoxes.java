package InterferenceEstimation;
/*
 * panelCheckBoxes.java
 *
 * Initially Created on January 17, 2005, 9:18 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/**
 * class creates a set of checkboxes
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington 
 */
public class panelCheckBoxes{
	
	public panelCheckBoxes()
	{
		
	}
    
    /**
     * Creates a new instance of panelCheckBoxes
     * @param stringForCheckbox1 String to be displayed alongside first checkbox
     * @param stringForCheckbox2 String to be displayed alongside second checkbox
     */
    public panelCheckBoxes(String stringForCheckbox1, String stringForCheckbox2) {
        try {
			this.setPanel(new JPanel());
			this.getPanel().setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
			this.setNullmodel(new String(stringForCheckbox1));
			this.setAltmodel(new String(stringForCheckbox2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * returns panel with checkboxes
     * @return JPanel
     */
    public JPanel getPanel() {
        return this.panel;
    }
    static JCheckBox createCheckBoxes(String label, boolean selectedState){
        // checkboxes for options
        try {
			JCheckBox checkbox = new JCheckBox(label);
			checkbox.setToolTipText(label);
			checkbox.setSelected(selectedState);
			checkbox.setFont(new Font("ArialBold",Font.BOLD, 15));
			return checkbox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    /**
     * returns checkbox corresponding to null model
     * @return JCheckBox
     */
    public JCheckBox getNullCheckBox() {
        return getNullModelcheckbox();
    }
    /**
     * returns checkbox corresponding to alternate model
     * @return JCheckBox
     */
    public JCheckBox getAltModelCheckBox() {
        return getAlternateModelcheckbox();
    }
    
    /**
	 * @return the nullModelcheckbox
	 */
	JCheckBox getNullModelcheckbox() {
		return this.nullModelcheckbox;
	}

	/**
	 * @param nullModelcheckbox the nullModelcheckbox to set
	 */
	void setNullModelcheckbox(JCheckBox nullModelcheckbox) {
		try {
			this.nullModelcheckbox = nullModelcheckbox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the alternateModelcheckbox
	 */
	JCheckBox getAlternateModelcheckbox() {
		return this.alternateModelcheckbox;
	}

	/**
	 * @param alternateModelcheckbox the alternateModelcheckbox to set
	 */
	void setAlternateModelcheckbox(JCheckBox alternateModelcheckbox) {
		try {
			this.alternateModelcheckbox = alternateModelcheckbox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JPanel panel;
    private JCheckBox nullModelcheckbox, alternateModelcheckbox;
    private String nullmodel, altmodel;
    private UIUtility utility;
	/**
	 * @return the nullmodel
	 */
	String getNullmodel() {
		return this.nullmodel;
	}

	/**
	 * @param nullmodel the nullmodel to set
	 */
	private void setNullmodel(String nullmodel) {
		this.nullmodel = nullmodel;
	}

	/**
	 * @return the altmodel
	 */
	String getAltmodel() {
		return this.altmodel;
	}

	/**
	 * @param altmodel the altmodel to set
	 */
	private void setAltmodel(String altmodel) {
		try {
			this.altmodel = altmodel;
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

	/**
	 * @return the utility
	 */
	public UIUtility getUtility() {
		return utility;
	}

	/**
	 * @param utility the utility to set
	 */
	public void setUtility(UIUtility utility) {
		this.utility = utility;
	}
}
