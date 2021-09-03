package Panels;
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
			setPanel(new JPanel());
			getPanel().setLayout(new GridBagLayout());
			setFileName("");
			setFile(null);
			setDirectory(null);
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
			addToPanel(getJtext(),5,0);
			addButtonForLoadingFile(label);
			addToPanel(getJbutton(),7,0);
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
        return this.panel;
    }
    /**
     * adds the specified component at the specified coordinates using GridBag Layout
     * @param component component to be added
     * @param xcoord integer specifying xcoordinate in the display space
     * @param ycoord integer specifying y-coordinate in the display space
     */
    public void addToPanel(JComponent component, int xcoord, int ycoord) {
        try {
			this.getPanel().add(component, new GridBagConstraints(xcoord, ycoord, 1, 1, 0.0, 0.0
			        ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 5, 0, 0), 0, 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void addTextAreaForLoadingFile() {
        try {
			setJtext(new JTextField());
			getJtext().setColumns(15);
			getJtext().setEnabled(false);
			getJtext().setToolTipText("File containing tetrad data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void addButtonForLoadingFile(String label) {
        // button for loading file
        try {
			setJbutton(new JButton(label));
			getJbutton().setFont(new Font("ArialBold",Font.BOLD, 15));
			getJbutton().setToolTipText("Load file containing tetrad data");
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
        return getJtext();
    }
    /**
     * method that calls action listener that performs desired action
     * @param parent the component on which the button was pressed
     */
    public void ButtonPressed(Component parent) {
        try {
			JFileChooser jfilechooser =  new JFileChooser();
			jfilechooser.setVisible(true);
			jfilechooser.setFont(new Font("ArialBold",Font.BOLD, 15)); //$NON-NLS-1$
			int returnVal = jfilechooser.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			    setFile(jfilechooser.getSelectedFile());
			    setDirectory(jfilechooser.getCurrentDirectory());
			    setFileName(getFile().getName());
			    
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
			    if(getFile() !=null)
			        getJtext().setText(getFile().getName());           
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
        return this.file;
    }
    /**
     * returns directory containing the file
     * @return Directory containing the file
     */
    public File getDirectory() {
        return this.directory;
    }
    /**
     * returns button
     * @return button
     */
    public JButton getButton() {
        return this.jbutton;
    }
    /**
     * returns string representation of file name
     * @return string containing file name
     */
    public String getFilename() {
        return this.fileName;
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
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	private void setFileName(String fileName) {
		try {
			this.fileName = fileName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param file the file to set
	 */
	private void setFile(File file) {
		try {
			this.file = file;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param directory the directory to set
	 */
	private void setDirectory(File directory) {
		try {
			this.directory = directory;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the jtext
	 */
	public JTextField getJtext() {
		return this.jtext;
	}
	/**
	 * @param jtext the jtext to set
	 */
	private void setJtext(JTextField jtext) {
		try {
			this.jtext = jtext;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the jbutton
	 */
	public JButton getJbutton() {
		return this.jbutton;
	}
	/**
	 * @param jbutton the jbutton to set
	 */
	private void setJbutton(JButton jbutton) {
		try {
			this.jbutton = jbutton;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
