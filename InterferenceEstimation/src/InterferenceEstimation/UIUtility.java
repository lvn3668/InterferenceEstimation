package InterferenceEstimation;
/*
 * UIUtility.java
 *
 * Initially Created on January 17, 2005, 8:21 PM
 */

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * class that generates a panel containing a label and an instance of a ComboBox class
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington 
 */
public class UIUtility{
    
    /** Creates a new instance of UIUtility */
    public UIUtility() {
        try {
			this.setPanel(new JPanel());
			this.getPanel().setLayout(new FlowLayout());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * adds specified component at the x and y coordinates specified using GridBag Layout
     * @param component component to be added
     * @param xcoord x coordinate where the component is to be added
     * @param ycoord y coordinate where component is to be added
     */
    private void addToPanel(JComponent component, int xcoord, int ycoord) {
        try {
			this.getPanel().add(component);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates an instance of ComboBox class with specified values
     * @param lowerLimit lower limit in combo box
     * @param upperLimit upper limit in combo box
     * @param step value for stepping through in the combo box
     */
    private void createComboBoxes(int lowerLimit, int upperLimit, int step) {
        try {
			this.setComboBox(new ComboBoxClass());
			this.getComboBox().createVector(lowerLimit,upperLimit,step);
			this.getComboBox().setSelectedNumber(5);
			this.getComboBox().getComboBox().setToolTipText("Choose number of simulations"); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * overloaded method for creating the panel
     * @param labelName string to be displayed near combo box
     * @param lower lower limit in the combo box
     * @param upper upper limit in the combo box
     * @param step step through value
     * @param defaultValue default value to be selected in the combo box
     */
    public void createPanel(String labelName, int lower, int upper, int step, int defaultValue) {
        try {
			this.createLabels(labelName);
			this.addToPanel(this.getLabel(),0,0);
			this.createComboBoxes(lower,upper, step);
			this.addToPanel(getComboBox().getComboBox(),1,0);
			this.getComboBox().setSelectedNumber(defaultValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates panel containing label and an instance of ComboBox class
     */
    private void createPanel() {
        try {
			this.createLabels("Number Of Simulations"); //$NON-NLS-1$
			this.addToPanel(this.getLabel(),0,0);
			this.createComboBoxes(0,100,1);
			this.addToPanel(this.getComboBox().getComboBox(),1,0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates JLabel with specified string
     * @param labelName String to be displayed in the label
     */
    private void createLabels(String labelName) {
        try {
			this.label = new JLabel(labelName);
			this.label.setFont(new Font("ArialBold",Font.BOLD, 15)); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns combobox class
     * @return ComboBox class
     */
    ComboBoxClass getComboBoxClass() {
        return getComboBox();
    }
    /**
     * returns label displayed alongside combo box
     * @return JLabel
     */
    private JLabel getLabel() {
        return this.label;
    }
    /**
     * returns panel with label and combo box
     * @return JPanel
     */
    public JPanel getPanel() {
        try {
			return this.panel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return this.panel;
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
	 * @return the comboBox
	 */
	private ComboBoxClass getComboBox() {
		return this.comboBox;
	}
	/**
	 * @param comboBox the comboBox to set
	 */
	private void setComboBox(ComboBoxClass comboBox) {
		try {
			this.comboBox = comboBox;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * creates a panel with the specified number of checkboxes
	 * @param numberOfCheckBoxes integer specifying number of checkboxes to be created
	 */
	public void createPanel(int numberOfCheckBoxes) {
	    try {
	    	panelCheckBoxes panelchkboxes = new panelCheckBoxes();
			panelchkboxes.setNullModelcheckbox(panelCheckBoxes.createCheckBoxes(panelchkboxes.getNullmodel(), true));
			panelchkboxes.setAlternateModelcheckbox(panelCheckBoxes.createCheckBoxes(panelchkboxes.getAltmodel(), true));
			this.getPanel().add(panelchkboxes.getNullModelcheckbox(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
			this.getPanel().add(panelchkboxes.getAlternateModelcheckbox(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * creates a panel with the specified string on the button
	 * @param label Label to be displayed on the button in the panel
	 */
	public void createPanel(String label){
	    try {
	    	panelAnalyseButton panelanalysebutton = new panelAnalyseButton();
			panelanalysebutton.setAnalyseButton(new JButton(label));
			panelanalysebutton.getButton().setFont(new Font("ArialBold",Font.BOLD, 15)); //$NON-NLS-1$
			panelanalysebutton.getButton().setToolTipText("Computes inteference parameter under the chosen model"); //$NON-NLS-1$
			panelanalysebutton.getPanel().add(panelanalysebutton.getAnalyseButton());        
			panelanalysebutton.setLabelValue(label);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JPanel panel;
    private ComboBoxClass comboBox;
    private JLabel label;
}
