package InterferenceEstimation;
/*
 * UI.java
 * Initially Created on November 7, 2004, 6:44 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import HelpPanel.HelpPanel;
import Panels.FileAndButtonPanel;
import Panels.SimulationsPanel;
import Panels.panelAnalyseButton;
import Panels.panelCheckBoxes;

import javax.swing.*;
import java.io.*;
import java.util.*;


/**
 *
 * @authors  Elizabeth Housworth and Lalitha Viswanath
 * @affiliation Department of Mathematics Indiana University Bloomington Indiana 
 */
public class UI extends SwingWorker, JFrame { 
	// extends JFrame
	//implements ItemListener, MouseListener{

	static final long serialVersionUID = 0;    
    /** Creates a new instance of UI */
    public UI() {

        try {
			setMainFrame(new JFrame("Interference Parameter Estimation"));
			setContentPane(getContentPane());             
      final SwingWorker swingWorker = new SwingWorker() {
			    @Override
				public Object construct() {
			        getHelpPanel().createPanel((UI)this);
			        createSims();                
			        return new Integer(0);
			    }
			    
			    
			    
			};
			swingWorker.start();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
    }
    
    public Object construct() {
    	
		return (new Object());
    	
    }
    
    public void createHelpPanel()
   {
      try {
		setHelpPanel(new HelpPanel());
		  getHelpPanel().getUtility().createPanel();      
		  tabbedPane.addTab("About",getHelpPanel().getPanel());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
  
    private void createSims() {
        final SwingWorker swingWorker = new SwingWorker() {
            @Override
			public Object construct() {
                simPanel = new SimulationsPanel();
                simPanel.createPanel();
                tabbedPane.addTab("Run Simulations",simPanel.getPanel());
                return new Integer(0);
            }
            
            @Override
			public void finished() {
                createHelpPanel(); 
                // Action Listener for Load Distances
                simPanel.getFilePanel().getButton().addActionListener(simPanel.getFilePanel());
                
                // Action Listener for NullMComboBoxes
                simPanel.getNullMComboBoxes().getComboBoxClass().getComboBox().addItemListener(simPanel.getNullMComboBoxes().getComboBoxClass());
                
                // Action Listener for Number of Simulations in second tabbed pane
                simPanel.getpanelWithNumberOfSimulations().getComboBoxClass().getComboBox().addItemListener(simPanel.getpanelWithNumberOfSimulations().getComboBoxClass());
                
                simPanel.getpanelWithAnalyseButton().getButton().addActionListener(
                        new java.awt.event.ActionListener(){
                    @Override
					public void actionPerformed(ActionEvent actionEvent) {
                        // get mUnderNullModel, mUnderAltModel, AltP, NumberOfSims here
                        numberOfSimulations = simPanel.getpanelWithNumberOfSimulations().getComboBoxClass().getSelectedNumber();
                        if(simPanel.getSampleSizeTextBox().getText().length()==0)
                            sampleSize = 0;
                        else
                            sampleSize = new Integer(simPanel.getSampleSizeTextBox().getText()).intValue();
                          mUnderNullModel = simPanel.getNullMComboBoxes().getComboBoxClass().getSelectedNumber();
                          pUnderAltModel = new Double(simPanel.getAltP().getText()).doubleValue(); // we have to check whether this is a float or not...                            
                            if((pUnderAltModel < 0.0) || (pUnderAltModel > 1.0)) {
                                try {
                                    JOptionPane joptionpane = new JOptionPane("pUnderAltModel can be between 0 and 1 only");
                                    JOptionPane.showMessageDialog(mainPanel,"pUnderAltModel can be between 0 and 1 only");
                                    joptionpane.setFont(new Font("ArialBold",Font.BOLD, 15));
                                } catch (Exception exception) {
                                    System.out.println("Exception " + exception);
                                }
                            }
                            
                        fileInterMarker = simPanel.getFilePanel().getFile();
                      if( (checkForErrors(simPanel.getFilePanel(),"A file containing distances must be provided")==1) && (checkNumberOfSimulations("Number of simulations cannot be zero", numberOfSimulations)==1) && (checksampleSize("Sample Size must be greater than zero", sampleSize)==1)) {
                            backEndSimulations = new ExecuteBackEnd(fileInterMarker, simPanelNullModelSimulations, simPanelAltModelSimulations, mUnderNullModel, pUnderAltModel, numberOfSimulations, sampleSize);
                           if(backEndSimulations.isValid())
                           {                                
                            if(simPanel.getPanel().getComponentCount()==9)
                                simPanel.getPanel().remove(8);
                            final SwingWorker progressbarThread = new SwingWorker() {
                                @Override
								public Object construct() {
                                    createSimulationsProgressBar();
                                    simPanel.getPanel().add(simulationsProgressBar, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 8);
                                    contentPane.validate();
                                    return new Integer(0);
                                }                                
                            };
                            
                              final SwingWorker timerWorker = new SwingWorker() {
                                @Override
								public Object construct() {
                                    createAndUpdateSimulationsTimer();
                                    simulationsTimer.start();
                                    return new Integer(0);
                                }
                            };
       
                            final SwingWorker worker = new SwingWorker() {
                                @Override
								public Object construct() {
                                    backEndSimulations.runSimulations();
                                    scrollPaneDataPanel = backEndSimulations.getResultsPanel().getScrollPane();
                                    scrollPaneDataPanel.setPreferredSize(new Dimension((mainPanel.getWidth())*50/100, (mainPanel.getHeight())*20/100));
                                    simPanel.getPanel().remove(8);
                                    simPanel.add(scrollPaneDataPanel,0,8);
                                    backEndSimulations.getResultsPanel().getResults2Button().addActionListener(
                        new java.awt.event.ActionListener() {
                          @Override
						public void actionPerformed(ActionEvent actionEvent) {
                        final SwingWorker swingResults = new SwingWorker() {
                            @Override
							public Object construct() {
                                writeSimResultsToFile(true);
                                return new Integer(0);
                            }
                        };
                        swingResults.start();
                            }                                    
                        }                
                    );               
                                    contentPane.validate();
                                    return new Integer(0);
                                }
                            };
                            progressbarThread.start();
                            timerWorker.start();
                            worker.start();
                          }
                        }
                    }
                }                
                );
                
                // action listener for alt model checkbox (analyse using alt model checkbox)
                simPanel.getPanelWithCheckBoxesForAnalyseOptions().getAltModelCheckBox().addItemListener(
                        new java.awt.event.ItemListener() {
                    @Override
					public void itemStateChanged(ItemEvent itemEvent) {
                        if(itemEvent.getStateChange()==ItemEvent.SELECTED) {
                            simPanelAltModelSimulations = true;
                            simPanel.getpanelWithAnalyseButton().getButton().setEnabled(true);
                        } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED) {
                            simPanelAltModelSimulations = false;
                            if(!simPanel.getPanelWithCheckBoxesForAnalyseOptions().getNullCheckBox().isSelected())
                                simPanel.getpanelWithAnalyseButton().getButton().setEnabled(false);
                        }
                    }
                }
                );
                
                // checkbox item listener for null item
                simPanel.getPanelWithCheckBoxesForAnalyseOptions().getNullCheckBox().addItemListener(
                        new java.awt.event.ItemListener() {
                    @Override
					public void itemStateChanged(ItemEvent itemEvent) {
                        if(itemEvent.getStateChange()==ItemEvent.SELECTED) {
                            simPanelNullModelSimulations = true;
                            simPanel.getpanelWithAnalyseButton().getButton().setEnabled(true);
                        } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED) {
                            simPanelNullModelSimulations = false;
                          if(!simPanel.getPanelWithCheckBoxesForAnalyseOptions().getAltModelCheckBox().isSelected())
                                simPanel.getpanelWithAnalyseButton().getButton().setEnabled(false);
                        }
                    }
                }
                );
            }
        };
        swingWorker.start();
        // process window events
        getMainFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    @Override
	protected void processWindowEvent(WindowEvent e) {
        try {
			super.processWindowEvent(e);
			if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			    System.exit(0);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    public int checkForErrors(FileAndButtonPanel panelForLoadingFile, String message) {
        if(panelForLoadingFile.getFilename().length()==0) {
            try {
                JOptionPane joptionpane = new JOptionPane(message);
                JOptionPane.showMessageDialog(this,message);
                joptionpane.setFont(new Font("ArialBold",Font.BOLD, 15));
            } catch (Exception exception) {
                System.out.println("Exception " + exception);
            }
            return 0;
        }
        return 1;
    }
    
    public int checkNumberOfSimulations(String message, int NoSims) {
        // if its from the first panel
        if(NoSims == 0) {
            try {
                JOptionPane joptionpane = new JOptionPane(message);
                JOptionPane.showMessageDialog(this,message);
                joptionpane.setFont(new Font("ArialBold",Font.BOLD, 15));
            } catch (Exception exception) {
                System.out.println("Exception " + exception);
            }
            return 0;
        }
        return 1;
    }
    public int checksampleSize(String message, int sampleSize) {
        // if its from the first panel
        if(sampleSize == 0) {
            try {
                JOptionPane joptionpane = new JOptionPane(message);
                JOptionPane.showMessageDialog(this,message);
                joptionpane.setFont(new Font("ArialBold",Font.BOLD, 15));
            } catch (Exception exception) {
                System.out.println("Exception " + exception);
            }
            return 0;
        }
        return 1;
    }
    public void createProgressBar() {
        try {
			progressbar= new JProgressBar(0, backEnd.getLengthOfTask());
			progressbar.setValue(0);
			progressbar.setStringPainted(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createSimulationsProgressBar() {
        try {
			simulationsProgressBar= new JProgressBar(0, backEndSimulations.getLengthOfTask());
			simulationsProgressBar.setValue(0);
			simulationsProgressBar.setStringPainted(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createAndUpdateTimer() {
        try {
			timer = new javax.swing.Timer(500, new ActionListener() {
			    @Override
				public void actionPerformed(ActionEvent evt) {
			        progressbar.setValue(backEnd.getCurrent());
			        if(backEnd.isDone()) {
			            timer.stop();
			            progressbar.setValue(progressbar.getMaximum());
			        }
			    }
			}
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createAndUpdateSimulationsTimer() {
        try {
			simulationsTimer = new javax.swing.Timer(500, new ActionListener() {
			    @Override
				public void actionPerformed(ActionEvent evt) {
			        simulationsProgressBar.setValue(backEndSimulations.getCurrent());
			        if(backEndSimulations.isDone()) {
			            simulationsTimer.stop();
			            simulationsProgressBar.setValue(simulationsProgressBar.getMaximum());
			        }
			    }
			}
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public int analyseButtonPressed(MouseEvent e) {
        
        try {
			if(mainPanel.getComponentCount()==4)
			    mainPanel.remove(3);
			
			if(checkForErrors(panelForLoadingMainFile, "A file must be chosen for analysis")==0)
			    return 0;
			file = panelForLoadingMainFile.getFile();
			backEnd = new ExecuteBackEnd(file, panelWithCheckBoxes.getNullCheckBox().isSelected(), panelWithCheckBoxes.getAltModelCheckBox().isSelected());
			if(backEnd.isValid())
			{
			createProgressBar();
			mainPanel.add(progressbar, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 3);
			contentPane.validate();
      final SwingWorker timerWorker = new SwingWorker() {
			    @Override
				public Object construct() {
			        createAndUpdateTimer();
			        timer.start();
			        return new Integer(0);
			    }
			};
			
			
			// if results panel is displayed, remove it....
			final SwingWorker swingWorker = new SwingWorker() {
			    @Override
				public Object construct() {
			        
			        backEnd.run();
			        
			        scrollPaneDataPanel = backEnd.getResultsPanel().getScrollPane();
			        JTable table = backEnd.getResultsPanel().getTable();
			        backEnd.getResultsPanel().getResults1Button().addActionListener(
			                new java.awt.event.ActionListener() {
			            @Override
						public void actionPerformed(ActionEvent actionEvent) {
			                final SwingWorker swingResults = new SwingWorker() {
			                    @Override
								public Object construct() {
			                        writeResultsToFile(true);
			                        return new Integer(0);
			                    }
			                };
			            swingResults.start();
			            }                
			        }                
			        );
			        
			        backEnd.getResultsPanel().getDistancesButton().addActionListener(
			                new java.awt.event.ActionListener() {
			            @Override
						public void actionPerformed(ActionEvent actionEvent) {
			                final SwingWorker swingResults = new SwingWorker() {
			                    @Override
								public Object construct() {
			                        writeResultsToFile(false);
			                        return new Integer(0);
			                    }
			                };
			            swingResults.start();
			            }                
			        }                
			        );
			        mainPanel.remove(3);
			        mainPanel.add(scrollPaneDataPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), 3);
			        contentPane.validate();
			        return new Integer(1);
			    }
			};
			timerWorker.start();
			swingWorker.start();
			return 1;
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
    }
    @Override
	public void itemStateChanged(ItemEvent itemEvent) {
        try {
			JCheckBox alternateModelcheckbox = panelWithCheckBoxes.getAltModelCheckBox();
			JCheckBox nullModelCheckbox = panelWithCheckBoxes.getNullCheckBox();
			
			if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
			    // if both checkboxes are not selected
			    // if both null model and alternate model are deselected, then analyse button should be disabled
			    if( (!alternateModelcheckbox.isSelected()) && (!nullModelCheckbox.isSelected()))
			        panelWithAnalyseButton.getButton().setEnabled(false);
			} else if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			    if( (alternateModelcheckbox.isSelected()) || (nullModelCheckbox.isSelected()))
			        panelWithAnalyseButton.getButton().setEnabled(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void writeResultsToFile(boolean writingResults) {
        try {
			FileDialog fileDialog = new FileDialog(this,"Save",FileDialog.SAVE);
			fileDialog.setVisible(true);
			String FileNameToWrite = null, Resultsdirectory=null;
			FileNameToWrite = fileDialog.getFile();
			Resultsdirectory = fileDialog.getDirectory();
			if(FileNameToWrite!=null) {
			    fileDialog.setDirectory(Resultsdirectory);
			    JOptionPane joptionpane = backEnd.writeToFile(Resultsdirectory.toString(), FileNameToWrite.toString(), writingResults);
			    String message = "written to file " + FileNameToWrite.toString();
			    JOptionPane.showMessageDialog(this, message);
			    joptionpane.setFont(new Font("ArialBold",Font.BOLD, 15));
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void writeSimResultsToFile(boolean writingResults) {
        try {
			FileDialog fileDialog = new FileDialog(this,"Save",FileDialog.SAVE);
			fileDialog.setVisible(true);
			String FileNameToWrite = null, Resultsdirectory=null;
			FileNameToWrite = fileDialog.getFile();
			Resultsdirectory = fileDialog.getDirectory();
			if(FileNameToWrite!=null) {
			    fileDialog.setDirectory(Resultsdirectory);
			    JOptionPane joptionpane2 = backEndSimulations.writeToFile(Resultsdirectory.toString(), FileNameToWrite.toString(), writingResults);
			    String message = "written to file " + FileNameToWrite.toString();
			    JOptionPane.showMessageDialog(this, message);
			    joptionpane2.setFont(new Font("ArialBold",Font.BOLD, 15));
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
        try {
			UI uinew = new UI();
			uinew.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private JFrame mainFrame;
    private JPanel DataPanel;
    public FileAndButtonPanel panelForLoadingMainFile;
    public panelAnalyseButton panelWithAnalyseButton;
    public panelCheckBoxes panelWithCheckBoxes;
    private SimulationsPanel simPanel;
    public JPanel mainPanel;
    private JScrollPane scrollPaneDataPanel = new JScrollPane();
    private int mUnderNullModel=0, mUnderAltModel=0;
    private double pUnderAltModel=0.0;
    private int numberOfSimulations;
    private File file=null;
    private File fileInterMarker;
    private Container contentPane;
    private HelpPanel helpPanel;
    private ExecuteBackEnd backEnd, backEndSimulations;
    private JProgressBar progressbar, simulationsProgressBar;
    private boolean simPanelNullModelSimulations = true, simPanelAltModelSimulations = true;
    private int sampleSize;
    public JTabbedPane tabbedPane = new JTabbedPane();
    private javax.swing.Timer timer, simulationsTimer;
    @Override
	public void mouseClicked(MouseEvent mouseEvent) {
          int retVal = analyseButtonPressed(mouseEvent);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
    }
    
    @Override
	public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    @Override
	public void mouseExited(MouseEvent mouseEvent) {
    }
    
    @Override
	public void mousePressed(MouseEvent mouseEvent) {
    }
    
    @Override
	public void mouseReleased(MouseEvent mouseEvent) {
    }

	/**
	 * @return the helpPanel
	 */
	public HelpPanel getHelpPanel() {
		return helpPanel;
	}

	/**
	 * @param helpPanel the helpPanel to set
	 */
	public void setHelpPanel(HelpPanel helpPanel) {
		this.helpPanel = helpPanel;
	}

	/**
	 * @return the mainFrame
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @param mainFrame the mainFrame to set
	 */
	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}      
}


