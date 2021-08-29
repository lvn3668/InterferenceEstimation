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
    
    /**
     * Creates a new instance of panelCheckBoxes
     * @param stringForCheckbox1 String to be displayed alongside first checkbox
     * @param stringForCheckbox2 String to be displayed alongside second checkbox
     */
    public panelCheckBoxes(String stringForCheckbox1, String stringForCheckbox2) {
        try {
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			nullmodel = new String(stringForCheckbox1);
			altmodel = new String(stringForCheckbox2);
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
        return panel;
    }
    private JCheckBox createCheckBoxes(String label, boolean selectedState){
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
        return nullModelcheckbox;
    }
    /**
     * returns checkbox corresponding to alternate model
     * @return JCheckBox
     */
    public JCheckBox getAltModelCheckBox() {
        return alternateModelcheckbox;
    }
    
    /**
     * creates a panel with the specified number of checkboxes
     * @param numberOfCheckBoxes integer specifying number of checkboxes to be created
     */
    public void createPanel(int numberOfCheckBoxes) {
        try {
			nullModelcheckbox = createCheckBoxes(nullmodel, true);
			alternateModelcheckbox = createCheckBoxes(altmodel, true);
			panel.add(nullModelcheckbox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
			panel.add(alternateModelcheckbox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private JPanel panel;
    private JCheckBox nullModelcheckbox, alternateModelcheckbox;
    private String nullmodel, altmodel;
}
