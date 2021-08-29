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
        interference = new Interference();
        NumberOfSimulations = 0;
    }
    /**
     * overloaded constructor
     * @param interMarkerDistances file containing intermarker distances
     * @param nullModelSims boolean specifying whether data is to be simulated under the null model
     * @param altModelSims boolean specifying whether the data is to be simulated under the alternate model
     * @param analyseNullModel boolean specifying whether the simulated data is to be analysed under the null model
     * @param analyseAltModel boolean specifying whether the simulated data is to be analysed under the alternate model
     * @param nullM interference parameter under null model
     * @param altPSims p value under the alternate model
     * @param NumberOfSims desired number of simulations
     * @param sampSize size of simulated data that is to be generated
     */
    public ExecuteBackEnd(File interMarkerDistances,  boolean analyseNullModel, boolean analyseAltModel, int nullM, double altPSims, int NumberOfSims, int sampSize) {
        try {
			this.done = false;
			this.fileIntermarker = interMarkerDistances;
			this.analyseNullModelForSims = analyseNullModel;
			this.analyseAltModelForSims = analyseAltModel;
			this.nullMSimsOnly = nullM;
			this.altPSimsOnly = altPSims;
			this.sampleSize = sampSize;
			this.NumberOfSimulations = NumberOfSims;
			this.interference = new Interference(fileIntermarker);
			this.interference.readInputFile();
			this.validInput = interference.readInterMarkerDistances();
			if(validInput)
			{
			resultsPanel = new ResultsPanel(NumberOfSimulations+2, true);
			resultsPanel.initialiseTable("Simulation");
			lengthOfTask =  NumberOfSimulations;         
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
        return validInput;
    }
    /**
     * returns current state of execution from Interference which is used to update the progressbar
     * @return integer 
     */
    final public int getCurrent() {
        return interference.getCurrent();
    }
    /**
     * states whether backend processing is completed or not
     * @return boolean
     */
    final public boolean isDone() {
        return done;
    }
    /**
     * returns total length of task to be performed
     * @return returns total length of task (total m values to be tested OR number of simulations as the case maybe)
     */
    final public int getLengthOfTask() {
        return lengthOfTask;
    }
    /**
     * overloaded constructor
     * @param fileToAnalyse tetrad data to be analysed
     * @param nullModel boolean specifying whether interference is to be estimated under null model
     * @param altModel boolean specifying whether interference is to be estimated under alternate model
     */
    public ExecuteBackEnd(File fileToAnalyse, boolean nullModel, boolean altModel) {
        executeNullModel = nullModel;
        executeAlternateModel = altModel;
        if(executeNullModel)
            lengthOfTask += 20;
        if(executeAlternateModel)
            lengthOfTask += 20;
        resultsPanel = new ResultsPanel(NumberOfSimulations+3, false);
        resultsPanel.initialiseTable("Sample_Size");
        interference = new Interference(fileToAnalyse);
        interference.readInputFile();
        validInput = interference.processRawData();
        if(validInput)
        {
        interference.convertRawToTetraType();
        interference.sortTetradData();
        interference.findInterMarkerDistances();
        NoTetrads = interference.getNoTetrads();
        resultsPanel.addSampleSize(NoTetrads);
        }
        //System.out.println("null " + executeNullModel + " alt model " + executeAlternateModel + "run sims " );
        
    }
    /**
     * run method calls the appropriate methods in Interference and obtaining the results to display in tabular format on screen
     */
    final public void run() {
        done = false;
        if(executeNullModel) {
            executeNullModel();
            resultsPanel.addNullData(nullMValue, nullMNegLog);
        }
        if(executeAlternateModel) {
            executeAlternateModel();
            resultsPanel.addAltData(altMValue, altPValue, altMNegLog, 0);
        }
        resultsPanel.addTableToPanel();        
        done = true;
    }
    /**
     * computes parameter under null model by calling appropriate methods from Interference
     */
    final public void executeNullModel() {
       //System.out.println("Before executing null model ");
        interference.findMaxLikelihoodEstimateSNull();
        nullMNegLog = interference.getNullLogLike();
        nullMValue = interference.getNullModelM();
        interference.setNullModelMinNegLogLike(nullMNegLog);
        interference.setNullM(nullMValue);
       // System.out.println("After creating new instance of interference class " + nullMNegLog + " m value " + nullMValue);
    }
    
    /**
     * returns results Pane for displaying on screen
     * @return ResultsPane containing a table of specified number of rows and columns for displaying data
     */
    final public ResultsPanel getResultsPanel() {
        return resultsPanel;
    }
    /**
     * estimates interference parameter under alternate model
     */
    final public void executeAlternateModel() {
        interference.findMaxLikelihoodEstimateSAlt();
        altMNegLog = interference.getAltModelLogLike();
        interference.setAltModelMinNegLogLike(altMNegLog);
        altMValue =  interference.getAltModelM();
        altPValue = interference.getAltp();
        interference.setAltM(altMValue);
        interference.setAltp(altPValue);
    }
    
    /**
     * run method calls the appropriate methods in Interference for performing simulations and obtaining the results to display in tabular format on screen
     */
    final public void runSimulations() {

            interference.simulations(NumberOfSimulations, nullMSimsOnly, altPSimsOnly, sampleSize, analyseNullModelForSims, analyseAltModelForSims);
            
            if(analyseAltModelForSims)
            {
            simulatedAltMVals = new int[NumberOfSimulations];
            simulatedAltMVals = interference.getSimulatedAltModelValues();
            
            simulatedAltNegLogLikeVals = new double[NumberOfSimulations];
            simulatedAltNegLogLikeVals =  interference.getSimulatedAltModelLogLike();
            
            simulatedAltPVals = new double[NumberOfSimulations];
            simulatedAltPVals = interference.getSimulatedAltPValues();
            
            resultsPanel.addSimulationsData(simulatedAltMVals, simulatedAltPVals, simulatedAltNegLogLikeVals);
            }
        
            if(analyseNullModelForSims)
            {
            simulatedNullMVals = new int[NumberOfSimulations];
            simulatedNullMVals = interference.getSimulatedNullModelMValues();
            simulatedNegLogLikeVals =  new double[NumberOfSimulations];
            simulatedNegLogLikeVals =  interference.getSimulatedNullModelLogLike();
            resultsPanel.addSimulationsData(simulatedNullMVals, simulatedNegLogLikeVals);                
            }

      
        resultsPanel.addTableToPanel();
        done = true;
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
          //  System.out.println("Checking whether the file exists?\n" + f.getAbsolutePath() + " " + f.getAbsoluteFile());
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(fw);
            if(writingResults) {
                Iterator<String> i = resultsPanel.getArray().iterator();
                while(i.hasNext()) {
                    out.write(i.next().toString());
                    out.newLine();
                }
            } else // writing Intermarker Distance
            {
                double[] distances = interference.getInterMarkerDistances();
                for(int counter=0;counter<distances.length;counter++) {
                    out.write(new String(distances[counter]+""));
                    out.newLine();
                }
            }
            out.close();
            joptionpane = new JOptionPane("A file must be provided");
        } catch (IOException fio) {
            System.out.println("Error Trapping IO Exception\nClass:ExecuteBackEnd\n" + fio.toString());
        }
        return joptionpane;
    }
    
    private boolean executeNullModel, executeAlternateModel;
    private boolean analyseNullModelForSims, analyseAltModelForSims;
    private int nullMValue, altMValue, NoTetrads;
    private double altPValue;
    private int nullMSimsOnly;
    private double altPSimsOnly;
    private double altMNegLog;
    private double nullMNegLog;
    private double[] simulatedNegLogLikeVals, simulatedAltNegLogLikeVals, simulatedAltPVals;
    private int[] simulatedAltMVals, simulatedNullMVals;
    private Interference interference;
    private File fileIntermarker;
    private int NumberOfSimulations;
    private int sampleSize;
    private ResultsPanel resultsPanel;
    private int lengthOfTask=0;
    private boolean done=false;
    private boolean validInput = true;
}
