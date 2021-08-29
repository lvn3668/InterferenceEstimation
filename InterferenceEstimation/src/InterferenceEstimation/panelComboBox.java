package InterferenceEstimation;
/*
 * panelComboBox.java
 *
 * Initially Created on January 17, 2005, 8:21 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

/**
 * class that generates a panel containing a label and an instance of a ComboBox class
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington 
 */
public class panelComboBox{
    
    /** Creates a new instance of panelComboBox */
    public panelComboBox() {
        try {
			panel = new JPanel();
			panel.setLayout(new FlowLayout());
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
    public void addToPanel(JComponent component, int xcoord, int ycoord) {
        try {
			panel.add(component);
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
    public void createComboBoxes(int lowerLimit, int upperLimit, int step) {
        try {
			comboBox = new ComboBoxClass();
			comboBox.createVector(lowerLimit,upperLimit,step);
			comboBox.setSelectedNumber(5);
			comboBox.getComboBox().setToolTipText("Choose number of simulations");
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
			createLabels(labelName);
			addToPanel(label,0,0);
			createComboBoxes(lower,upper, step);
			addToPanel(comboBox.getComboBox(),1,0);
			comboBox.setSelectedNumber(defaultValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates panel containing label and an instance of ComboBox class
     */
    public void createPanel() {
        try {
			createLabels("Number Of Simulations");
			addToPanel(label,0,0);
			createComboBoxes(0,100,1);
			addToPanel(comboBox.getComboBox(),1,0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates JLabel with specified string
     * @param labelName String to be displayed in the label
     */
    public void createLabels(String labelName) {
        try {
			label = new JLabel(labelName);
			label.setFont(new Font("ArialBold",Font.BOLD, 15));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns combobox class
     * @return ComboBox class
     */
    public ComboBoxClass getComboBoxClass() {
        return comboBox;
    }
    /**
     * returns label displayed alongside combo box
     * @return JLabel
     */
    public JLabel getLabel() {
        return label;
    }
    /**
     * returns panel with label and combo box
     * @return JPanel
     */
    public JPanel getPanel() {
        return panel;
    }
    
    private JPanel panel;
    private ComboBoxClass comboBox;
    private JLabel label;
}
