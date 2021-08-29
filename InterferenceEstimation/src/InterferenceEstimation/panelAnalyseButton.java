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
			panel = new JPanel();
			analyseButton = new JButton();
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
			analyseButton = new JButton(label);
			analyseButton.setFont(new Font("ArialBold",Font.BOLD, 15));
			analyseButton.setToolTipText("Computes inteference parameter under the chosen model");
			panel.add(analyseButton);        
			labelOnButton = label;
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
        return panel;
    }
    /**
     * returns the button added to the panel
     * @return JPanel
     */
    public JButton getButton() {
        return analyseButton;
    }
    /**
     * returns Label displayed alongside the button
     * @return JLabel containing the displayed label
     */
    public String getLabel() {
        return labelOnButton;
    }
    private JPanel panel;
    private JButton analyseButton;
    private String labelOnButton;
}
