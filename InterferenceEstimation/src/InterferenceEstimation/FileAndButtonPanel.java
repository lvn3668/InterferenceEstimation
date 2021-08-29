package InterferenceEstimation;
/*
 * FileAndButtonPanel.java
 *
 * Initially Created on January 17, 2005, 7:35 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
/**
 * Panel for creating text box and 'load' button
 * @authors Elizabeth Housworth (Department of Mathematics Indiana University, Bloomington) 
 * and Lalitha Viswanath
 */
public class FileAndButtonPanel implements MouseListener, ActionListener{
    
    /** Creates a new instance of FileAndButtonPanel */
    public FileAndButtonPanel() {
        try {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			fileName = "";
			file =null;
			directory = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * creates a panel with the specified label on the button
     * @param label string that should appear on the button
     */
    public void createPanel(String label) {
        try {
			addTextAreaForLoadingFile();
			addToPanel(jtext,5,0);
			addButtonForLoadingFile(label);
			addToPanel(jbutton,7,0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
    /**
     * returns panel
     * @return JPanel (panel)
     */
    public JPanel getPanel() {
        return panel;
    }
    /**
     * adds the specified component at the specified coordinates using GridBag Layout
     * @param component component to be added
     * @param xcoord integer specifying xcoordinate in the display space
     * @param ycoord integer specifying y-coordinate in the display space
     */
    public void addToPanel(JComponent component, int xcoord, int ycoord) {
        try {
			panel.add(component, new GridBagConstraints(xcoord, ycoord, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void addTextAreaForLoadingFile() {
        try {
			jtext = new JTextField();
			jtext.setColumns(15);
			jtext.setEnabled(false);
			jtext.setToolTipText("File containing tetrad data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void addButtonForLoadingFile(String label) {
        // button for loading file
        try {
			jbutton = new JButton(label);
			jbutton.setFont(new Font("ArialBold",Font.BOLD, 15));
			jbutton.setToolTipText("Load file containing tetrad data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * returns text area
     * @return text area 
     */
    public JTextField getTextArea() {
        return jtext;
    }
    /**
     * method that calls action listener that performs desired action
     * @param parent the component on which the button was pressed
     */
    public void ButtonPressed(Component parent) {
        try {
			JFileChooser jfilechooser =  new JFileChooser();
			jfilechooser.setVisible(true);
			jfilechooser.setFont(new Font("ArialBold",Font.BOLD, 15));
			int returnVal = jfilechooser.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			    file = jfilechooser.getSelectedFile();
			    directory = jfilechooser.getCurrentDirectory();
			    fileName = file.getName();
			    
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * method that performs the desired action
     * @param actionEvent event that trigged the action
     */
    @Override
	public void actionPerformed(ActionEvent actionEvent) {
        
        try {
			if(actionEvent.getSource().getClass().getName().equals("javax.swing.JButton")) {
			    ButtonPressed((JComponent)actionEvent.getSource());
			    if(file !=null)
			        jtext.setText(file.getName());           
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns file loaded
     * @return File
     */
    public File getFile() {
        return file;
    }
    /**
     * returns directory containing the file
     * @return Directory containing the file
     */
    public File getDirectory() {
        return directory;
    }
    /**
     * returns button
     * @return button
     */
    public JButton getButton() {
        return jbutton;
    }
    /**
     * returns string representation of file name
     * @return string containing file name
     */
    public String getFilename() {
        return fileName;
    }
    /**
     * method for action to be performed when mouseclicked on a component
     * @param mouseEvent event that triggers action
     */
    @Override
	public void mouseClicked(MouseEvent mouseEvent) {
        //HelpPanel.create("Load a file containing the tetrad data");
    }
    
    private JPanel panel;
    private JTextField jtext;
    private JButton jbutton;
    private File file, directory;
    private String fileName;
    /**
     * method that specifies action to be taken when mouse enters a component
     * @param mouseEvent event that triggers action
     */
    @Override
	public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    /**
     * method that specifies action to be taken when mouse exits a component
     * @param mouseEvent event that triggers action
     */
    @Override
	public void mouseExited(MouseEvent mouseEvent) {
    }
    
    /**
     * method that specifies action to be taken when mouse is pressed over a component
     * @param mouseEvent event that triggers action
     */
    @Override
	public void mousePressed(MouseEvent mouseEvent) {
    }
    
    /**
     * method that specifies action to be taken when mouse is released from a component
     * @param mouseEvent event that triggers action
     */
    @Override
	public void mouseReleased(MouseEvent mouseEvent) {
    }
}
