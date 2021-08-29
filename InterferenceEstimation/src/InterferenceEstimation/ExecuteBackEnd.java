package InterferenceEstimation;
/*
 * ExecuteBackEnd.java
 *
 * Initially Created on January 18, 2005, 8:12 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import resultsPanel.ResultsPanel;

import java.io.*;
import java.util.*;
/**
 * class that interfaces between the front end and the back end class
 * @authors Elizabeth Housworth and Lalitha Viswanath
 * Department of Mathematics Indiana University Bloomington
 */
final public class ExecuteBackEnd {
    
    /** Creates a new instance of ExecuteBackEnd */
    public ExecuteBackEnd() {
        try {
			this.interference = new Interference();
			this.numberOfSimulations = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * overloaded constructor
     * @param interMarkerDistances file containing intermarker distances
     * @param nullModelSims boolean specifying whether data is to be simulated under the null model
     * @param altModelSims boolean specifying whether the data is to be simulated under the alternate model
     * @param analyseNullModel boolean specifying whether the simulated data is to be analysed under the null model
     * @param analyseAltModel boolean specifying whether the simulated data is to be analysed under the alternate model
     * @param interferenceParameterUnderNullModel interference parameter under null model
     * @param pvalueunderAlternateModel p value under the alternate model
     * @param numberofsimulationstobeperformed desired number of simulations
     * @param tetradsamplesize size of simulated data that is to be generated
     */
    public ExecuteBackEnd(File interMarkerDistances,  boolean analyseNullModel, boolean analyseAltModel, int interferenceParameterUnderNullModel, double pvalueunderAlternateModel, int numberofsimulationstobeperformed, int tetradsamplesize) {
        try {
			this.done = false;
			this.setIntermarkerdistancesfilepointer(interMarkerDistances);
			this.setAnalyseNullModelForSimulations(analyseNullModel);
			this.setAnalyseAltModelForSimulations(analyseAltModel);
			this.setInterferenceParameterUnderNullModel(interferenceParameterUnderNullModel);
			this.setpValueUnderAltModel(pvalueunderAlternateModel);
			this.setNumberOfTetradsToBeSimulated(tetradsamplesize);
			this.setNumberOfSimulations(numberofsimulationstobeperformed);
			this.setInterference(new Interference(this.getIntermarkerdistancesfilepointer()));
			this.getInterference().readInputFile();
			this.setFlagForCheckingValidityOfIntermarkerDistances(this.getInterference().readInterMarkerDistances());
			if(this.isFlagForCheckingValidityOfIntermarkerDistances() == true)
			{
			this.setResultsPanel(new ResultsPanel(this.numberOfSimulations+2, true));
			this.getResultsPanel().initialiseTable("Simulation"); //$NON-NLS-1$
			this.lengthOfTask =  this.numberOfSimulations;         
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This is used to generate an appropriate error message in the front end
     * @return returns whether the input file is valid or not. 
     */
    final public boolean isValid()
    {
        return this.flagForCheckingValidityOfIntermarkerDistances;
    }
    /**
     * returns current state of execution from Interference which is used to update the progressbar
     * @return integer 
     */
    final public int getCurrent() {
        return this.getInterference().getCurrent();
    }
    /**
     * states whether backend processing is completed or not
     * @return boolean
     */
    final public boolean isDone() {
        return this.done;
    }
    /**
     * returns total length of task to be performed
     * @return returns total length of task (total m values to be tested OR number of simulations as the case maybe)
     */
    final public int getLengthOfTask() {
        return this.lengthOfTask;
    }
    /**
     * overloaded constructor
     * @param fileToAnalyse tetrad data to be analysed
     * @param flagtoperformsimulationsundernullmodel boolean specifying whether interference is to be estimated under null model
     * @param altModel boolean specifying whether interference is to be estimated under alternate model
     */
    public ExecuteBackEnd(File fileToAnalyse, boolean flagtoperformsimulationsundernullmodel, boolean flagtoperformsimulationsunderaltmodel) {
        try {
			this.executeNullModel = flagtoperformsimulationsundernullmodel;
			this.executeAlternateModel = flagtoperformsimulationsunderaltmodel;
			if(this.executeNullModel)
			    this.lengthOfTask += 20;
			if(this.executeAlternateModel)
			    this.lengthOfTask += 20;
			this.setResultsPanel(new ResultsPanel(this.numberOfSimulations+3, false));
			this.getResultsPanel().initialiseTable("Sample_Size"); //$NON-NLS-1$
			this.setInterference(new Interference(fileToAnalyse));
			this.getInterference().readInputFile();
			this.setFlagForCheckingValidityOfIntermarkerDistances( this.getInterference().processRawData());
			if(this.isFlagForCheckingValidityOfIntermarkerDistances())
			{
			this.getInterference().convertRawToTetraType();
			this.getInterference().sortTetradData();
			this.getInterference().findInterMarkerDistances();
			this.setNumberOfTetradsToBeSimulated(this.getInterference().getNoTetrads());
			this.getResultsPanel().addSampleSize(this.getNumberOfTetradsToBeSimulated());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * run method calls the appropriate methods in Interference and obtaining the results to display in tabular format on screen
     */
    final public void run() {
        try {
			this.done = false;
			if(this.executeNullModel) {
			    executeNullModel();
			    this.getResultsPanel().addResultsFromNullModelSimulations(this.getInterferenceParameterUnderNullModel(), this.getNegativeLogLikelihoodUnderNullModel());
			}
			if(this.executeAlternateModel) {
			    executeAlternateModel();
			    this.getResultsPanel().addAltData(this.getInterferenceParameterUnderAltModel(), this.getpValueUnderAltModel(), this.getNegativeLogLikelihoodUnderAltModel(), 0);
			}
			this.getResultsPanel().addTableToPanel();        
			this.done = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * computes parameter under null model by calling appropriate methods from Interference
     */
    final public void executeNullModel() {
        try {
			this.getInterference().findMaxLikelihoodEstimateSNull();
			this.setNegativeLogLikelihoosUnderNullModel(this.getInterference().getNullLogLike());
			this.setInterferenceParameterUnderNullModel( this.getInterference().getNullModelM());
			this.getInterference().setNullModelMinNegLogLike(this.getNegativeLogLikelihoodUnderNullModel());
			this.getInterference().setNullM(this.getInterferenceParameterUnderNullModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * returns results Pane for displaying on screen
     * @return ResultsPane containing a table of specified number of rows and columns for displaying data
     */
    final public ResultsPanel getResultsPanel() {
        return this.resultsPanel;
    }
    /**
     * estimates interference parameter under alternate model
     */
    final public void executeAlternateModel() {
        try {
			this.getInterference().findMaxLikelihoodEstimateSAlt();
			this.setNegativeLogLikelihoodUnderAltModel(this.getInterference().getAltModelLogLike());
			this.getInterference().setAltModelMinNegLogLike(this.getNegativeLogLikelihoodUnderAltModel());
			this.setInterferenceParameterUnderAltModel(this.getInterference().getAltModelM());
			this.setpValueUnderAltModel(this.getInterference().getAltp());
			this.getInterference().setAltM(this.getInterferenceParameterUnderAltModel());
			this.getInterference().setAltp(this.getpValueUnderAltModel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * run method calls the appropriate methods in Interference for performing simulations and obtaining the results to display in tabular format on screen
     */
    final public void runSimulations() {

            try {
				this.getInterference().simulations(this.numberOfSimulations, this.nullMSimsOnly, this.simulatedPValueUnderAltModel, this.numberoftetradstobesimulated, this.analyseNullModelForSimulations, this.analyseAltModelForSimulations);
				
				if(this.analyseAltModelForSimulations)
				{
				this.simulatedInterferenceParameterUnderAltModel = new Integer[this.getNumberOfSimulations()];
				this.simulatedInterferenceParameterUnderNullModel = this.getInterference().getSimulatedAltModelValues();
				
				this.simulatedNegativeLogLikelihoodValuesUnderNullModel = new Double[this.getNumberOfSimulations()];
				this.simulatedNegativeLogLikelihoodValuesUnderAltModel =  this.getInterference().getSimulatedAltModelLogLike();
				
				this.simulatedPValuesUnderAltModel = new Double[this.numberOfSimulations];
				this.simulatedPValuesUnderAltModel = this.getInterference().getSimulatedAltPValues();
				
				this.getResultsPanel().addSimulationsData(this.simulatedInterferenceParameterUnderNullModel, this.simulatedPValuesUnderAltModel, this.simulatedNegativeLogLikelihoodValuesUnderAltModel);
				}
      
				if(this.analyseNullModelForSimulations)
				{
				this.simulatedInterferenceParameterUnderNullModel = new Integer[this.numberOfSimulations];
				this.simulatedInterferenceParameterUnderNullModel = this.getInterference().getSimulatedNullModelMValues();
				this.simulatedNegativeLogLikelihoodValuesUnderNullModel =  new Double[this.numberOfSimulations];
				this.simulatedNegativeLogLikelihoodValuesUnderNullModel =  this.getInterference().getSimulatedNullModelLogLike();
				this.getResultsPanel().addSimulationsData(this.simulatedInterferenceParameterUnderNullModel, this.getSimulatedNegativeLogLikelihoodValuesUnderNullModel());                
				}
      this.getResultsPanel().addTableToPanel();
      this.done = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    /**
     * writes results of simulations or analyses to specified file and directory
     * @param directory directory containing file
     * @param filename file in which results are to be written
     * @param writingResults boolean specifying results or distances are to be written
     * @return boolean specifying whether the process was successful
     */
    final public JOptionPane writeToFile(String directory, String filename, boolean writingResults) {
        JOptionPane joptionpane=null;
        try {
            File f = new File(directory,filename);
            FileWriter filewriter = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(filewriter);
            if(writingResults) {
                Iterator<String> i = this.getResultsPanel().getArray().iterator();
                while(i.hasNext()) {
                    out.write(i.next().toString());
                    out.newLine();
                }
            } else // writing Intermarker Distance
            {
                double[] distances = this.getInterference().getInterMarkerDistances();
                for(int counter=0;counter<distances.length;counter++) {
                    out.write(String.valueOf(distances[counter]));
                    out.newLine();
                }
            }
            out.close();
            joptionpane = new JOptionPane("A file must be provided"); //$NON-NLS-1$
        } catch (IOException fio) {
            System.out.println("Error Trapping IO Exception\nClass:ExecuteBackEnd\n" + fio.toString()); //$NON-NLS-1$
        }
        return joptionpane;
    }
    
    private boolean executeNullModel, executeAlternateModel;
    private boolean analyseNullModelForSimulations, analyseAltModelForSimulations;
    private int interferenceParameterUnderNullModel, interferenceParameterUnderAltModel, numberOfTetradsToBeSimulated;
    private double pValueUnderAltModel;
    private Integer nullMSimsOnly;
    private double simulatedPValueUnderAltModel;
    private double negativeLogLikelihoodUnderAltModel;
    private double negativeLogLikelihoodUnderNullModel;
    private Double[] simulatedNegativeLogLikelihoodValuesUnderNullModel, simulatedNegativeLogLikelihoodValuesUnderAltModel, simulatedPValuesUnderAltModel;
    private Integer[] simulatedInterferenceParameterUnderNullModel, simulatedInterferenceParameterUnderAltModel;
    private Interference interference;
    private File intermarkerdistancesfilepointer;
    private int numberOfSimulations;
    private int numberoftetradstobesimulated;
    private ResultsPanel resultsPanel;
    private int lengthOfTask=0;
    private boolean done=false;
    private boolean flagForCheckingValidityOfIntermarkerDistances = true;
	/**
	 * @return the executeNullModel
	 */
	private boolean isExecuteNullModel() {
		return this.executeNullModel;
	}
	/**
	 * @param executeNullModel the executeNullModel to set
	 */
	private void setExecuteNullModel(boolean executeNullModel) {
		try {
			this.executeNullModel = executeNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the executeAlternateModel
	 */
	private boolean isExecuteAlternateModel() {
		return this.executeAlternateModel;
	}
	/**
	 * @param executeAlternateModel the executeAlternateModel to set
	 */
	private void setExecuteAlternateModel(boolean executeAlternateModel) {
		try {
			this.executeAlternateModel = executeAlternateModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the analyseNullModelForSimulations
	 */
	private boolean isAnalyseNullModelForSimulations() {
		return this.analyseNullModelForSimulations;
	}
	/**
	 * @param analyseNullModelForSimulations the analyseNullModelForSimulations to set
	 */
	private void setAnalyseNullModelForSimulations(boolean analyseNullModelForSims) {
		try {
			this.analyseNullModelForSimulations = analyseNullModelForSims;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the analyseAltModelForSimulations
	 */
	private boolean isAnalyseAltModelForSimulations() {
		return this.analyseAltModelForSimulations;
	}
	/**
	 * @param analyseAltModelForSimulations the analyseAltModelForSimulations to set
	 */
	private void setAnalyseAltModelForSimulations(boolean analyseAltModelForSims) {
		try {
			this.analyseAltModelForSimulations = analyseAltModelForSims;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the nullMValue
	 */
	private int getInterferenceParameterUnderNullModel() {
		return this.interferenceParameterUnderNullModel;
	}
	/**
	 * @param nullMValue the nullMValue to set
	 */
	private void setInterferenceParameterUnderNullModel(int nullMValue) {
		try {
			this.interferenceParameterUnderNullModel = nullMValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the interferenceParameterUnderAltModel
	 */
	private int getInterferenceParameterUnderAltModel() {
		return this.interferenceParameterUnderAltModel;
	}
	/**
	 * @param interferenceParameterUnderAltModel the interferenceParameterUnderAltModel to set
	 */
	private void setInterferenceParameterUnderAltModel(int altMValue) {
		try {
			this.interferenceParameterUnderAltModel = altMValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the noTetrads
	 */
	private int getNumberOfTetradsToBeSimulated() {
		return this.numberOfTetradsToBeSimulated;
	}
	/**
	 * @param noTetrads the noTetrads to set
	 */
	private void setNumberOfTetradsToBeSimulated(int noTetrads) {
		try {
			this.numberOfTetradsToBeSimulated = noTetrads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the pValueUnderAltModel
	 */
	private double getpValueUnderAltModel() {
		return this.pValueUnderAltModel;
	}
	/**
	 * @param pValueUnderAltModel the pValueUnderAltModel to set
	 */
	private void setpValueUnderAltModel(double altPValue) {
		try {
			this.pValueUnderAltModel = altPValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the nullMSimsOnly
	 */
	private int getSimulatedPValueUnderNullModel() {
		return this.nullMSimsOnly;
	}
	/**
	 * @param integers the nullMSimsOnly to set
	 */
	private void setSimulatedInterferenceParameterUnderNullModel(Integer integers) {
		try {
			this.nullMSimsOnly = integers;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the altPSimsOnly
	 */
	private double getSimulatedPValueUnderAltModel() {
		return this.simulatedPValueUnderAltModel;
	}
	/**
	 * @param simulatedPValueUnderAltModel the altPSimsOnly to set
	 */
	private void setSimulatedPValueUnderAltModel(double simulatedPValueUnderAltModel) {
		try {
			this.simulatedPValueUnderAltModel = simulatedPValueUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the negativeLogLikelihoodUnderAltModel
	 */
	private double getNegativeLogLikelihoodUnderAltModel() {
		return this.negativeLogLikelihoodUnderAltModel;
	}
	/**
	 * @param negativeLogLikelihoodUnderAltModel the negativeLogLikelihoodUnderAltModel to set
	 */
	private void setNegativeLogLikelihoodUnderAltModel(double altMNegLog) {
		try {
			this.negativeLogLikelihoodUnderAltModel = altMNegLog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the nullMNegLog
	 */
	private double getNegativeLogLikelihoodUnderNullModel() {
		return this.negativeLogLikelihoodUnderNullModel;
	}
	/**
	 * @param nullMNegLog the nullMNegLog to set
	 */
	private void setNegativeLogLikelihoosUnderNullModel(double nullMNegLog) {
		try {
			this.negativeLogLikelihoodUnderNullModel = nullMNegLog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedNegLogLikeVals
	 */
	private Double[] getSimulatedNegativeLogLikelihoodValuesUnderNullModel() {
		return this.simulatedNegativeLogLikelihoodValuesUnderNullModel;
	}
	/**
	 * @param simulatedNegLogLikeVals the simulatedNegLogLikeVals to set
	 */
	private void setSimulatedNegativeLogLikelihoodValues(Double[] simulatedNegLogLikeVals) {
		try {
			this.simulatedNegativeLogLikelihoodValuesUnderNullModel = simulatedNegLogLikeVals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedAltNegLogLikeVals
	 */
	private Double[] getSimulatedAltNegLogLikeVals() {
		return this.simulatedNegativeLogLikelihoodValuesUnderAltModel;
	}
	/**
	 * @param simulatedAltNegLogLikeVals the simulatedAltNegLogLikeVals to set
	 */
	private void setSimulatedAltNegLogLikeVals(Double[] simulatedAltNegLogLikeVals) {
		try {
			this.simulatedNegativeLogLikelihoodValuesUnderAltModel = simulatedAltNegLogLikeVals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedPValuesUnderAltModel
	 */
	private Double[] getSimulatedPValuesUnderAltModel() {
		return this.simulatedPValuesUnderAltModel;
	}
	/**
	 * @param simulatedPValuesUnderAltModel the simulatedPValuesUnderAltModel to set
	 */
	private void setSimulatedPValuesUnderAltModel(Double[] simulatedAltPVals) {
		try {
			this.simulatedPValuesUnderAltModel = simulatedAltPVals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedInterferenceParameterUnderNullModel
	 */
	private Integer[] getSimulatedInterferenceParameterUnderNullModel() {
		return this.simulatedInterferenceParameterUnderNullModel;
	}
	/**
	 * @param simulatedInterferenceParameterUnderNullModel the simulatedInterferenceParameterUnderNullModel to set
	 */
	/** private void setSimulatedInterferenceParameterUnderNullModel(Integer[] simulatedAltMVals) {
		try {
			this.simulatedInterferenceParameterUnderNullModel = simulatedAltMVals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} **/
	/**
	 * @return the simulatedNullMVals
	 */
	//private int[] getSimulatedInterferenceParameterUnderNullModel() {
	//	return this.simulatedInterferenceParameterUnderNullModel;
//	}
	/**
	 * @param simulatedNullMVals the simulatedNullMVals to set
	 */
	private void setSimulatedNullMVals(Integer[] simulatedNullMVals) {
		try {
			this.simulatedInterferenceParameterUnderNullModel = simulatedNullMVals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the interference
	 */
	private Interference getInterference() {
		return this.getInterference();
	}
	/**
	 * @param interference the interference to set
	 */
	private void setInterference(Interference interference) {
		try {
			this.interference = interference;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the intermarkerdistancesfilepointer
	 */
	private File getIntermarkerdistancesfilepointer() {
		return this.intermarkerdistancesfilepointer;
	}
	/**
	 * @param intermarkerdistancesfilepointer the intermarkerdistancesfilepointer to set
	 */
	private void setIntermarkerdistancesfilepointer(File fileIntermarker) {
		try {
			this.intermarkerdistancesfilepointer = fileIntermarker;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the numberOfSimulations
	 */
	private int getNumberOfSimulations() {
		return this.numberOfSimulations;
	}
	/**
	 * @param numberOfSimulations the numberOfSimulations to set
	 */
	private void setNumberOfSimulations(int numberOfSimulations) {
		try {
			this.numberOfSimulations = numberOfSimulations;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the sampleSize
	 */
	private int getSampleSize() {
		return this.numberoftetradstobesimulated;
	}
	/**
	 * @param sampleSize the sampleSize to set
	 */
	private void setSampleSize(int sampleSize) {
		try {
			this.numberoftetradstobesimulated = sampleSize;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the flagForCheckingValidityOfIntermarkerDistances
	 */
	private boolean isFlagForCheckingValidityOfIntermarkerDistances() {
		return this.flagForCheckingValidityOfIntermarkerDistances;
	}
	/**
	 * @param flagForCheckingValidityOfIntermarkerDistances the flagForCheckingValidityOfIntermarkerDistances to set
	 */
	private void setFlagForCheckingValidityOfIntermarkerDistances(boolean validInput) {
		try {
			this.flagForCheckingValidityOfIntermarkerDistances = validInput;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param resultsPanel the resultsPanel to set
	 */
	private void setResultsPanel(ResultsPanel resultsPanel) {
		try {
			this.resultsPanel = resultsPanel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param lengthOfTask the lengthOfTask to set
	 */
	private void setLengthOfTask(int lengthOfTask) {
		try {
			this.lengthOfTask = lengthOfTask;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param done the done to set
	 */
	private void setDone(boolean done) {
		try {
			this.done = done;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedInterferenceParameterUnderAltModel
	 */
	public Integer[] getSimulatedInterferenceParameterUnderAltModel() {
		return this.simulatedInterferenceParameterUnderAltModel;
	}
	/**
	 * @param simulatedInterferenceParameterUnderAltModel the simulatedInterferenceParameterUnderAltModel to set
	 */
	private void setSimulatedInterferenceParameterUnderAltModel(Integer[] simulatedInterferenceParameterUnderAltModel) {
		try {
			this.simulatedInterferenceParameterUnderAltModel = simulatedInterferenceParameterUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
