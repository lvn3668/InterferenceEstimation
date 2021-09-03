package InterferenceEstimation;
/*
 * ComboBoxClass.java
 *
 * Initially Created on January 17, 2005, 8:09 PM
 */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/**
 * Creates a comboBox with the specified lower and upper limits
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics
 * Indiana University Bloomington
 */
public class ComboBoxClass implements ItemListener{
    
    /** Creates a new instance of ComboBoxClass */
    public ComboBoxClass() {
        try {
			setJcomboboxoptions(new JComboBox<String>());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * creates a vector of integers for the combo box with the specified lower and upper limits and the specified jumps
     * @param lowerLimit integer specifying lower limit of the combo box
     * @param upperLimit integer specifying the upper limit of the combo box
     * @param step integer specifying the number of steps between numbers in the combo box
     */
    protected final void createVector(int lowerLimit, int upperLimit, int step) {
        try {
			Vector<String> comboboxvalues = new Vector<String>();
			
			for(int x=lowerLimit;x<=upperLimit;x+=step)
			     comboboxvalues.add(String.valueOf(x));        
			setJcomboboxoptions(new JComboBox<String>(comboboxvalues));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
    /**
     * sets the selected value of the combo box to that of the specified integer
     * @param number number to set the selected number in the combo box to
     */
    protected final void setSelectedNumber(Integer number) {
        try {
			getJcomboboxoptions().setSelectedItem(number);
			setSelectedNumber(number);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns vector of integers to be displayed in the combo box
     * @return vector containing numbers in the combo box
     */
    final public Vector<String> getVector() {
        return this.choices;
    }
    /**
     * returns selected number in the combo box
     * @return integer containing selected value of the combo box
     */
    final public int getSelectedNumber() {
        return this.selectedNumber;
    }
    /**
     * listener for changes in items selected or deselected in the combobox
     * @param itemEvent event that triggered the change (change of number selected in the combo box)
     */
    @Override
	final public void itemStateChanged(ItemEvent itemEvent) {
        try {
			this.setSelectedNumber(new Integer((String)getJcomboboxoptions().getSelectedItem()).intValue());
			this.setSelectedNumber(getSelectedNumber());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns combo box
     * @return JComboBox 
     */
    final public JComboBox<String> getComboBox() {
        return getJcomboboxoptions();
    }
    /**
	 * @return the jcomboboxoptions
	 */
	public JComboBox<String> getJcomboboxoptions() {
		return this.jcomboboxoptions;
	}

	/**
	 * @param jcomboboxoptions the jcomboboxoptions to set
	 */
	private void setJcomboboxoptions(JComboBox<String> jcomboboxoptions) {
		try {
			this.jcomboboxoptions = jcomboboxoptions;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private JComboBox<String> jcomboboxoptions;
    private Vector<String> choices;
    private int selectedNumber;
}
