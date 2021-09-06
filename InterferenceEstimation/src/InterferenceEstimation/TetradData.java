package InterferenceEstimation;
/*
 * TetradData.java
 * Conversion of VB Code for cross over interference in tetrads written by Dr. Housworth
 * Initially Created on September 27, 2004, 3:20 PM
 */
import java.io.*;
//import Jama.*;
import java.util.*;
import javax.swing.*;
//import org.apache.commons.math3.*;

import Utilities.Utilities;
import matrixUtilities.MatrixUtilities;
import mleAltModel.MLEAltModel;
import mleNullModel.MLENullModel;

/**
 * Main class that performs backend processing for computing interference paramater
 * @authors Elizabeth Housworth (Indiana University Bloomington) and Lalitha Viswanath
 */
public class TetradData {
    
    /**
     * Creates a new instance of TetradData. It takes in a string containing the filename of the file containing the raw tetrad data as an input
     * @param fileName string containing the filename of the file containing tetrad data or intermarker distances
     */
    
    public TetradData(String fileName) {
        try {
			setMarkers(new Vector<String>());
			setMarkersfile(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
    /** Default constructor for TetradData class */
    public TetradData() {
        try {
			setMarkers(new Vector<String>());
			setCurrent(0);
			setMatrixUtils(new MatrixUtilities());
			setUtilities(new Utilities());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Constructor that creates a  new instance of the TetradData class. 
     * It takes in a file as an input. 
     * It initialises the variable 'current' controlling the progress bar. 
     * This constructor is called by ExecuteBackEnd to perform the analyses
     * @param fileName fileName is of type File
     */
    public TetradData(File fileName) {
        try {
			setMarkers(new Vector<String>());
			setMarkersfile(fileName.getAbsolutePath());
			setCurrent(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Constructor that takes in a number of parameters
     * @param fileName File containing data
     * @param numSimulations integer indicating number of simulations
     * @param NullModel boolean variable indicating whether null model is to be applied
     * @param AltModel boolean variable that declares whether alternate model is to be used or not
     * @param Sims Simulations
     */
    public TetradData(File fileName, int numSimulations, boolean NullModel, boolean AltModel, boolean Sims) {
        try {
			setMarkers(new Vector<String>());
			setMarkersfile(fileName.getAbsolutePath());
			setNumberOfSimulations(numSimulations);
			setEstimateInterferenceParameterUnderNullModel(NullModel);
			setEstimateInterferenceParameterUnderAlternateModel(AltModel);
			setPerformSimulations(Sims);
			setCurrent(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
    /**
     * reads Intermarker distances from the file.
     * @return returns vector containing the intermarker distances
     */
    public boolean readInterMarkerDistances() {
        try {
			setInterMarkerDistances(new double[getNumberOfLinesInFile()]);
			Iterator<String> iter = getMarkers().iterator();
			int counter=0;
			try {
				// Loop through lines in file and read in intermarker distances
			    while(iter.hasNext()) {
			        getInterMarkerDistances()[counter]=Double.parseDouble((String)iter.next());
			        counter++;
			    }
			} catch(Exception e) {
			    JOptionPane joptionPane = new JOptionPane("File is in invalid format",JOptionPane.ERROR_MESSAGE);
			    JOptionPane.showMessageDialog(null,"File is in invalid format");
			    return false;
			}
			// 
			setNumberOfIntervals(getNumberOfLinesInFile());
			setNumberOfMarkers(getNumberOfIntervals()+1);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }
    /**
     * method that tells whether the simulations or computation is done or is ongoing
     * @return boolean value indicating status of computation
     */
    public boolean isDone() {
        try {
			return this.done;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }
    // make a copy of original tetrad data before overwriting tetrdata during simulations
    private void copyTetradData() {
    	
        try {
			setCopiedTetradData(new int[getNumberOfTetrads()][getNumberOfIntervals()]);
			for(int rows=0;rows<getNumberOfTetrads();rows++) {
			    for(int cols=0;cols<getNumberOfIntervals();cols++){
			        getCopiedTetradData()[rows][cols] = getTetradData()[rows][cols];}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * this method provides the current value of the interference parameter or the simulation which is used to dynamically update the progress bar.
     * @return integer that provides current status of simulation or interference parameter.
     */
    public int getCurrent() {
        try {
			return this.current;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return this.current;
    }
    // perform simulations
    /**
     * This method performs the simulations of the data and analyses the simulated data under the null or the alternate models. The data is simulated under the null model or the alternate model using the parameters specified as input
     * @param numberOfSimulations Number providing number of simulations
     * @param Fixedm M value under the null or the alternate model 
     * @param Fixedp Proportion of non interfering crossovers under the null model (0) or alternate model
     * @param sampleSize Number of rows in the tetrad data that need to be generated
     * @param nullModel Boolean variable indicating whether the simulated data is to be tested under the null model or not
     * @param altModel Boolean variable indicating whether the simulated data is to be tested under the alternate model or not
     */
    public void simulations(int NumberOfSimulations, int Fixedm, double Fixedp, int sampleSize, boolean nullModel, boolean altModel) {
        try {
        	MLENullModel mlenullmodel = new MLENullModel();
        	MLEAltModel mlealtmodel = new MLEAltModel();
			setPerformSimulations(true);
			setCurrent(0);
			double[] ActualDistances = new double[getNumberOfIntervals()+1];
			setInterMarkerDistancesInActualData(new double[getNumberOfIntervals()]);
			double Rate, ProbI;
			int LeftMarker;
			int counter=0;
			setNumberOfTetrads(sampleSize);
			if(nullModel) {
			    setSimulatedMValuesUnderNullModel(new Integer[NumberOfSimulations]);
			    setSimulatedNullMinNegLogLikeValues(new Double[NumberOfSimulations]);
			}
			if(altModel) {
			    setSimulatedMValuesUnderAltModel(new Integer[NumberOfSimulations]);
			    setSimulatedPValuesUnderAltModel(new Double[NumberOfSimulations]);
			    setSimulatedMinNegLogLikeValuesUnderAltModel(new Double[NumberOfSimulations]);
			}
			if(nullModel && altModel){
			    setSimulatedLikelihoodValues(new Double[NumberOfSimulations]);
			}
			setTetradData(new int[getNumberOfTetrads()][getNumberOfIntervals()]);
			// it needs intermarker distances
			// nullm, fixedm, fixedp
			for(int i=0;i<getNumberOfIntervals();i++){
			    getInterMarkerDistancesInActualData()[i] = getInterMarkerDistances()[i];}
			for(int i=0;i<getNumberOfIntervals()+1;i++){
			    ActualDistances[i] = 0.0;}
			for(int i =1;i<getNumberOfIntervals()+1;i++){
			    ActualDistances[i] = getInterMarkerDistancesInActualData()[i-1] + ActualDistances[i-1];}
			
			Rate = 2.0 *(Fixedp + (Fixedm + 1.0)*(1.0 - Fixedp));
			ProbI = 2.0 * Fixedp / Rate;
			int[] CrossoversBetweenMarkers =  new int[getNumberOfIntervals()];
			int NCoBeforeCxII;
			double Total, NextEvent,MyRand;
			MyRand = Math.random();
			int i,j;
			for(int Sim=0;Sim < NumberOfSimulations; Sim++) {
			    setCurrent(Sim);
			    for(i=0;i<getNumberOfTetrads();i++) {
			        for(j=0;j<getNumberOfIntervals();j++){
			            CrossoversBetweenMarkers[j] = 0;}
			        
			        MyRand = Math.random();
			        j=1;
			        NCoBeforeCxII = 0;
			        while(MyRand > ((double)j/(double)(Fixedm+1))) {
			            j++;
			            NCoBeforeCxII++;
			        }
			        Total = ActualDistances[0];
			        LeftMarker = 0;
			        NextEvent = -Math.log(Math.random()) / Rate;
			        Total += NextEvent;
			        
			        while(Total < ActualDistances[getNumberOfIntervals()]) 
			        {
			            counter++;
			            while(Total > ActualDistances[LeftMarker+1]){
			                LeftMarker++;}
			            
			            MyRand = Math.random();
			            if(MyRand < ProbI) {
			                CrossoversBetweenMarkers[LeftMarker]++;
			            } else if(NCoBeforeCxII == 0) {
			                CrossoversBetweenMarkers[LeftMarker]++;
			                NCoBeforeCxII = Fixedm;
			            } else {
			                NCoBeforeCxII--;
			            }
			            NextEvent = -Math.log(Math.random())/Rate;
			            Total = Total + NextEvent;
			        }
			        
			        for(j=0;j<getNumberOfIntervals();j++) {
			            if(CrossoversBetweenMarkers[j]==0)
			                getTetradData()[i][j] = 0;
			            else {
			                MyRand = Math.random();
			                if(MyRand  < (1.0/3.0)*(0.5 + Math.pow(-0.5, CrossoversBetweenMarkers[j])))
			                    getTetradData()[i][j] = 0;
			                else if(MyRand < (2.0/3.0)*(0.5 + Math.pow(-0.5, CrossoversBetweenMarkers[j])))
			                    getTetradData()[i][j] = 2;
			                else
			                    getTetradData()[i][j] = 1;
			            }
			        }
			    } // for all tetrads
			    // Sort the data
			   sortTetradData();
			    // this finds intermarker distances for tetradData
			    //
			    findInterMarkerDistances();
			    if(nullModel) {
			        mlenullmodel.findMaximumLikelihoodEstimatesNullModel(this);
			        getSimulatedMValuesUnderNullModel()[Sim] = mlenullmodel.getmUnderNullModel();
			        getSimulatedNullMinNegLogLikeValues()[Sim] = mlenullmodel.getNullModelMinNegLogLikelihood();
			     }
			    if(altModel){    
			        mlealtmodel.findMaximumLikelihoodEstimatesAlternateModel(this);
			        getSimulatedMValuesUnderAltModel()[Sim]=mlealtmodel.getAltModelM();
			        getSimulatedPValuesUnderAltModel()[Sim]=mlealtmodel.getAltp();
			        getSimulatedMinNegLogLikeValuesUnderAltModel()[Sim] = mlealtmodel.getAltModelMinNegLogLikelihood();
			     }
			    if(nullModel && altModel)
			        getSimulatedLikelihoodValues()[Sim] = 2.0*(simulatedNullMinNegLogLikeValues[Sim] - simulatedMinNegLogLikeValuesUnderAltModel[Sim]);
			} // for all simulations
			
			setCurrent(NumberOfSimulations);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    
    public void findMLEForSingleSporeData()
    {
        try {
			int m=0;        
			double p=0;
			setNormalizedIntermarkerDistances(new double[getNumberOfIntervals()]);
			for(int i=0;i<getNumberOfIntervals();i++){
			    getNormalizedIntermarkerDistances()[i]=2.0*(m+1)*getInterMarkerDistances()[i];}
			if(!isPerformSimulations()) {
			    setDone(false);
			    setCurrent(m);
			}
			
        } catch (Exception e)
        {
        	e.printStackTrace();
        }
    	
    }
    // Compute Neg Log Likelihood of Tetrad Data
    /**
     * 
     * @param prob
     * @param tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted
     */
    public void computeNegativeLogLikelihoodTetradData(double prob, int tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted) {
        
        try {
        	MLENullModel mlenullmodel = new MLENullModel();
			mlenullmodel.setNegativeLogLikelihood(0);
			getMatrixUtils().setIdentityMatrix(new double[1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setIdentityMatrixTranspose(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			getMatrixUtils().setRunningMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setMatrixSum(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setTempMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			setMatrixUtils(new MatrixUtilities());
			int test=0;
			double probability=0;
			int knumberofdoublestranddnabreaks=1;
			
			for(int i=0;i<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;i++) {
			    getMatrixUtils().getIdentityMatrix()[0][i]=1.0;
			    getMatrixUtils().getIdentityMatrixTranspose()[i][0]=1.0;
			}
			// Number of tetrads = number of cols of intermarker distances minus one 
			for(int tetradcounter=0;tetradcounter<getNumberOfTetrads();tetradcounter++) {
			    if(tetradcounter==0) {
			        test=1;
			    } else {
			        test=0;
			        for(int j=0;j<getNumberOfIntervals();j++){
			            test = test + (getTetradData()[tetradcounter][j]-getTetradData()[tetradcounter-1][j])*
			            		(getTetradData()[tetradcounter][j]-getTetradData()[tetradcounter-1][j]);
			            }
			    }
			    // Compute tetrad sum over each row	
			    if(test > 1) {
			        // create an identity matrix
			        for(int tmp1=0;tmp1<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;tmp1++) {
			            for(int tmp2=0;tmp2<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;tmp2++) {
			                if(tmp1==tmp2)
			                    getMatrixUtils().getRunningMatrix()[tmp1][tmp2]=1.00;
			                else
			                    getMatrixUtils().getRunningMatrix()[tmp1][tmp2]=0.00;
			            }
			        }
			        
			        for(int cols=0;cols<getNumberOfIntervals();cols++) {
			            double y = getNormalizedIntermarkerDistances()[cols];
			            // if it is a tetratype
			            // compute tetrad type based on intermarker distances 
			            // for each tetrad type (non parental ditype, tetratype, parental ditype)
			            // compute probability based on Mathers formula (assumption: Non Chromatid TetradData)
			            // Use a running matrix to compute posterior prob of a tetrad pattern
			            // MLE computed using simplex method fof find optimal value of m alone 
			            // Under null model
			            if(getTetradData()[tetradcounter][cols]==1) {
			            	getMatrixUtils().setdMatrix(null);
			                getMatrixUtils().dMatrixFormation(prob,1,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        getMatrixUtils().getMatrixSum()[counter][innercounter]=getMatrixUtils().getdMatrix()[counter][innercounter];
			                    }
			                }
			                // k = number of double strand dna breaks over which negative log
			                // likelihood is calculated for different values of m 
			                // the number of ds dna breaks after which a successful cross is effected
			                
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                	getMatrixUtils().setdMatrix(null);
			                    getMatrixUtils().dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=(2.00/3.00*(1-Math.pow((-0.5),knumberofdoublestranddnabreaks)))*(matrixUtils.getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                }// for all K
			            } // completes the TetraType if loop
			            else // if it is PD or NPD, add the 1/3 formula first
			                // then add Do if it is PD
			            {
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        getMatrixUtils().getMatrixSum()[counter][innercounter]=0.0;
			                    }
			                }
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                    getMatrixUtils().setdMatrix(null);
			                    
			                    getMatrixUtils().dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=(1.00/3.00)*((1.00/2.00) +Math.pow((-0.5),knumberofdoublestranddnabreaks))*(matrixUtils.getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                }
			                
			                // check
			                if(getTetradData()[tetradcounter][cols]==0) {
			                    getMatrixUtils().setdMatrix(null);                 
			                    getMatrixUtils().dMatrixFormation(prob, 0,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=(getMatrixUtils().getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                    
			                }
			            }// end of else
			            
			            getMatrixUtils().setMatrixProduct(null);
			            getMatrixUtils().findMatrixProduct(getMatrixUtils().getRunningMatrix(), getMatrixUtils().getMatrixSum());
			            
			            for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                    getMatrixUtils().getRunningMatrix()[counter][innercounter] = getMatrixUtils().getMatrixProduct()[counter][innercounter];
			                }
			            }
			            
			        } // finishing loop over all columns
			        
			        getMatrixUtils().findMatrixProduct(getMatrixUtils().getRunningMatrix(),getMatrixUtils().getIdentityMatrixTranspose());            
			        for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++){
			            getMatrixUtils().getTempMatrix()[counter][0] = getMatrixUtils().getMatrixProduct()[counter][0];}
			        
			        getMatrixUtils().setMatrixProduct(null);
			        // then take with one
			        getMatrixUtils().findMatrixProduct(getMatrixUtils().getIdentityMatrix(), getMatrixUtils().getTempMatrix());                
			        probability = getMatrixUtils().getMatrixProduct()[0][0]/(tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1);                
			    }// if test > 0
			    
			    mlenullmodel.setNegativeLogLikelihood(mlenullmodel.getNegativeLogLikelihood() - Math.log(probability));
			}// for each row
			getMatrixUtils().setIdentityMatrix(null);
			getMatrixUtils().setIdentityMatrixTranspose(null);
			getMatrixUtils().setRunningMatrix(null);
			getMatrixUtils().setMatrixProduct(null);
			getMatrixUtils().setTempMatrix(null);
			getMatrixUtils().setMatrixSum(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * This method finds the intermarker distances using the raw tetrad data
     */
    public void findInterMarkerDistances() {
        try {
			setInterMarkerDistances(new double[getNumberOfIntervals()]);
			for(int i=0;i<getNumberOfIntervals();i++) {
			    getInterMarkerDistances()[i]=0.0;
			    for(int a=0;a<getNumberOfTetrads();a++) {
			        if(getTetradData()[a][i]==2)
			            getInterMarkerDistances()[i] +=3.0;
			        else
			            getInterMarkerDistances()[i] += (getTetradData()[a][i])/(2.00);
			    }
			    getInterMarkerDistances()[i] = (getInterMarkerDistances()[i])/(getNumberOfTetrads());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    /**
     * This method returns a vector containing the intermarker distances
     * @return Vector containing intermarker distances
     */
    public double[] getInterMarkerDistances() {
        try {
			return this.interMarkerDistances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return this.interMarkerDistances;
    }
    /**
     * Converts the raw data into tetrad-type data
     */
    public void convertRawToTetraType() {
        try {
			findNumberOfMarkers();
			findNumberOfIntervals();
			findNumberOfTetrads();
			int Tetrad=0;
			setTetradData(new int[getNumberOfTetrads()][getNumberOfIntervals()]);
			for(int x=2;x<getRowsInRawData();x+=4) {
			    for(int xy=0;xy<getColumnsInRawData()-1;xy++) {
			        
			        int sum = Math.abs(((Integer)getRawData()[x][xy]).intValue()-((Integer)getRawData()[x][xy+1]).intValue()) +
			                Math.abs(((Integer)getRawData()[x+1][xy]).intValue()-((Integer)getRawData()[x+1][xy+1]).intValue()) +
			                Math.abs(((Integer)getRawData()[x+2][xy]).intValue()-((Integer)getRawData()[x+2][xy+1]).intValue()) +
			                Math.abs(((Integer)getRawData()[x+3][xy]).intValue()-((Integer)getRawData()[x+3][xy+1]).intValue());
			        sum = sum / 2;
			        getTetradData()[Tetrad][xy]=sum;
			    }
			    Tetrad++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // Tetrad data ordered as number of tetrads x number of intervals 
    // Number of markers = number of intervals + 1
    /**
     * Number of markers in tetrad data = number of intervals plus one 
     */
    private void findNumberOfMarkersFromTetradData() {
        try {
			setNumberOfMarkers(getRawData()[0].length+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Number of intervals is the number of columns in raw tetrad data 
     */
    private void findNumberOfIntervalsfromTetradData() {
        try {
			setNumberOfIntervals(getRawData()[0].length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Setter for number of tetrads (number of rows in raw data file)
     */
    private void findNumberOfTetradsfromTetradData() {
        try {
			setNumberOfTetrads(rawData.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Fill in raw data 
     * Read intermarker distances from file 
     */
    /**
     * 
     */
	private void fillrawdata() {
        try {
			setRawData(new Object[getNumberOfLinesInFile()][]);
			Iterator<String> i = getMarkers().iterator();
			String tmp;
			String[] splitFields= new String[1];
			int counter=0;
			while(i.hasNext()) {
			    tmp=(String)i.next();
			    splitFields=tmp.split("\t");
			    getRawData()[counter]=new Object[splitFields.length];
			    for(int temp=0;temp<splitFields.length;temp++) {
			        getRawData()[counter][temp]= Integer.parseInt(splitFields[temp]);
			    }
			    counter++;
			}
			setColumnsInRawData(splitFields.length);
			setRowsInRawData(counter);
			setNumberOfTetrads(getRowsInRawData());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     */
    private void readTetraTypeDirectly() {

        try {
			findNumberOfMarkersFromTetradData();
			findNumberOfIntervalsfromTetradData();
			findNumberOfTetradsfromTetradData();
			int Tetrad=0;
			setTetradData(new int[getNumberOfTetrads()][getNumberOfIntervals()]);
			for(int x=0;x<getRowsInRawData();x++) {
			    for(int xy=0;xy<getColumnsInRawData();xy++) {
			        getTetradData()[Tetrad][xy]=Integer.parseInt(getRawData()[x][xy]+"");
			    }
			    Tetrad++;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
 
   //  Sorts the tetrad data
   
/**
 * 
 */
   public void sortTetradData() {
        try {
			String[] tempTetradData = new String[getNumberOfTetrads()];
			for(int i=0;i<getNumberOfTetrads();i++) {
			        getUtilities();
					String one = Utilities.concatenate(getTetradData()[i]);
			        tempTetradData[i] = one;
			}
			// Sort in ascending order 
			Arrays.sort(tempTetradData);
			for(int i=0;i<getNumberOfTetrads();i++) {
			    for(int j=0;j<getNumberOfMarkers()-1;j++) {
			        getTetradData()[i][j]= Integer.parseInt(tempTetradData[i].substring(j,j+1));                
			    }
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

   private void printRawData() {
        try {
			int rows,cols;
			for(rows=0;rows<getRowsInRawData();rows++) {
			    for(cols=0;cols<getColumnsInRawData();cols++)
			        System.out.print(rawData[rows][cols]+"\t");
			    System.out.print("\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @param filename write out tetrad data to file 
     */
    private void writeTetradData(String filename) {
        try {
            File f = new File(filename);
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<getNumberOfTetrads();i++) {
                for(int j=0;j<getNumberOfIntervals();j++) {
                    
                    out.write(getTetradData()[i][j]+"\t");
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException fio) {
            System.out.println("Error Trapping IO Exception\nClass:writeTetradData\n" + fio.toString());
        }
    }
    /**
     * @param filename writes raw data (intermarker distances) to file
     */
    private void writeRawData(String filename) {
        try {
            File f = new File(filename);
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<getRowsInRawData();i++) {
                for(int j=0;j<getColumnsInRawData();j++) {
                    
                    out.write(rawData[i][j]+"\t");
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException fio) {
            System.out.println("Error Trapping IO Exception\nClass:writeTetradData\n" + fio.toString());
        }
    }
    private void writeInterMarkerDistances(String filename) {

        try{
            for(int i=0;i<getNumberOfIntervals();i++)
                System.out.print(getInterMarkerDistances()[i]+"\t");
            File f = new File(filename);
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<getNumberOfIntervals();i++)
                out.write(getInterMarkerDistances()[i]+"\t");
            fw.close();
        }catch(IOException io) {
            System.out.println("Error Trapping IO Exception\nClass:writeIntermarkerdistances\n" + io.toString());
        }
    }
    
    private void printTetradData() {
        try {
			for(int i=0;i<getNumberOfTetrads();i++) {
			    for(int j=0;j<getNumberOfIntervals();j++) {
			        System.out.print(getTetradData()[i][j]+"\t");
			    }
			    System.out.println("\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void findNumberOfMarkers() {
        try {
			setNumberOfMarkers(getColumnsInRawData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void findNumberOfIntervals() {
        try {
			setNumberOfIntervals(getNumberOfMarkers()-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void findNumberOfTetrads() {
        try {
			setNumberOfTetrads((getNumberOfTetrads()-2)/4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Processes raw data by converting it into numerical representation. 
     * This data is subsequently used to get tetrad-type data which is sorted and analysed
     * @return returns the raw data represented in tetrad-type format
     */
    @SuppressWarnings("deprecation")
	public boolean processRawData() {
        try {
        rawData=new Object[getNumberOfLinesInFile()][];
        Iterator<String> i = getMarkers().iterator();
        int test;
        String tmp1;
        String tmp2;
        String tmp3;
        String tmp4;
        String[] splitFields1= new String[1];
        String[] splitFields2= new String[1];
        String[] splitFields3= new String[1];
        String[] splitFields4= new String[1];
        int counter=0;
        String[] parentalData1, parentalData2;
        // read first line
        tmp1=(String)i.next();
        parentalData1 = tmp1.split("\t");
        getRawData()[counter]=new Object[parentalData1.length];
        for(int temp=0;temp<parentalData1.length;temp++)
            getRawData()[counter][temp]= Integer.valueOf(0);
        counter++;
        
        // read second line
        tmp2=(String)i.next();
        parentalData2 = tmp2.split("\t");
        getRawData()[counter]=new Object[parentalData2.length];
        for(int temp=0;temp<parentalData2.length;temp++)
            getRawData()[counter][temp]= Integer.valueOf(1);
        counter++;
        
        boolean skip;
        while(i.hasNext()) {
            
            skip=false;
            tmp1=(String)i.next();
            tmp2=(String)i.next();
            tmp3=(String)i.next();
            tmp4=(String)i.next();
            
            splitFields1=tmp1.split("\t");
            splitFields2=tmp2.split("\t");
            splitFields3=tmp3.split("\t");
            splitFields4=tmp4.split("\t");
            
            for(int temp=0;temp<splitFields1.length;temp++) {
                 test = 0;
                // if line contains invalid recombination  information, skip it
                if((splitFields1[temp].equals(parentalData1[temp]))){
               test = test + 0;
                 }
             else if ((splitFields1[temp].equals(parentalData2[temp]))) {
                    test = test + 1;
                }
             else {skip=true;}

                if((splitFields2[temp].equals(parentalData1[temp]))){
                test = test + 0;
                 }
             else if ((splitFields2[temp].equals(parentalData2[temp]))) {
                    test = test + 1;
                }
             else {skip=true;}

                if((splitFields3[temp].equals(parentalData1[temp]))){
                test = test + 0;
                 }
             else if ((splitFields3[temp].equals(parentalData2[temp]))) {
                    test = test + 1;
                }
             else {skip=true;}

                if((splitFields4[temp].equals(parentalData1[temp]))){
                test = test + 0;
                 }
             else if ((splitFields4[temp].equals(parentalData2[temp]))) {
                    test = test + 1;
                }
             else {skip=true;}

               if(!skip){
               if(!(test==2)){skip=true;}
                         }

            }
            
            if(!skip) {
                getRawData()[counter]=new Object[splitFields1.length];
                for(int temp=0;temp<splitFields1.length;temp++) {
                    if(splitFields1[temp].equals(parentalData1[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(0);
                    else if(splitFields1[temp].equals(parentalData2[temp]))
                        getRawData()[counter][temp]=Integer.valueOf(1);
                }
                counter++;
                getRawData()[counter]=new Object[splitFields2.length];
                for(int temp=0;temp<splitFields2.length;temp++) {
                    if(splitFields2[temp].equals(parentalData1[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(0);
                    else if(splitFields2[temp].equals(parentalData2[temp]))
                        rawData[counter][temp]=Integer.valueOf(1);
                }
                counter++;
                getRawData()[counter]=new Object[splitFields3.length];
                for(int temp=0;temp<splitFields3.length;temp++) {
                    if(splitFields3[temp].equals(parentalData1[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(0);
                    else if(splitFields3[temp].equals(parentalData2[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(1);
                }
                counter++;
                getRawData()[counter]=new Object[splitFields4.length];
                for(int temp=0;temp<splitFields4.length;temp++) {
                    if(splitFields4[temp].equals(parentalData1[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(0);
                    else if(splitFields4[temp].equals(parentalData2[temp]))
                        getRawData()[counter][temp]= Integer.valueOf(1);
                }
                counter++;
            }
        }
        setColumnsInRawData(splitFields1.length);
        setRowsInRawData(counter);
        setNumberOfTetrads(rowsInRawData);
        } catch (Exception e)
        {
         JOptionPane joptionPane = new JOptionPane("File is in invalid format",JOptionPane.ERROR_MESSAGE);
         JOptionPane.showMessageDialog(null,"File is in invalid format");   
         return false;
        }
        return true;
        }
    
    /**
     * method that reads raw data
     */
    public void readInputFile() {
        try{
            BufferedReader in = new BufferedReader(new FileReader(getMarkersfile()));            
            String str="";
            while((str=in.readLine())!=null) {                
                getMarkers().add(str);
            }
            in.close();
            setNumberOfLinesInFile(getMarkers().size());    
        }catch (IOException ioexception) {
            System.out.println("IOException " + ioexception + "in TetradData.java!");
        }
            
    }
    
    /**
     * prints results of simulations
     */
    public void printSimulationResults() {
        try {
			for(int x=0;x<getNumberOfSimulations();x++) {
			    System.out.println("Simulation Number " + x + " m value under Null Model " + simulatedMValuesUnderNullModel[x]+ " Neg Log Likelihood Values under Null Model " + simulatedNullMinNegLogLikeValues[x]  + " m under alternate model " + simulatedMValuesUnderAltModel[x] + " p values under Alternate Model " + simulatedPValuesUnderAltModel[x] + " Negative Log Likelihood Values under Alternate Model " + simulatedMinNegLogLikeValuesUnderAltModel[x]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * interference parameter under alternate model
     * @return integer 
     */
    public int getNumberOfTetrads() {
        try {
			return this.numberOfTetrads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.numberOfTetrads;
    }

    /**
     * returns vector of simulated null-m values
     * @return Vector of integers
     */
    public Double[] getSimulatedNullModelLogLike() {
        try {
			return this.simulatedNullMinNegLogLikeValues;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.simulatedNullMinNegLogLikeValues;
    }
    /**
     * returns vector of simulated p values under alternate model
     * @return Vector of doubles
     */
    public Double[] getSimulatedAltPValues() {
        try {
			return this.simulatedPValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.simulatedPValuesUnderAltModel;
    }
    /**
     * returns vector of likelihood values under alternate model
     * @return Vector of doubles
     */
    public Double[] getSimulatedAltModelLogLike() {
        try {
			return this.simulatedMinNegLogLikeValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.simulatedMinNegLogLikeValuesUnderAltModel;
    }
    /**
     * 
     * @return Array of values generated from Simulated Model (Null Hypothesis Model)
     */
    public Integer[] getSimulatedNullModelMValues() {
        try {
			return this.simulatedMValuesUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.simulatedMValuesUnderNullModel;
    }
    /**
     * return Array of values generated from Alt Model  
    */
    public Integer[] getSimulatedAltModelValues() {
        try {
			return this.simulatedMValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.simulatedMValuesUnderAltModel;
    }
    /**
     * returns loglikelihood ratio
     * @return double value containing loglikelihood ratio
     */
    public double getLikeRatio() {
        try {
			return this.likelihoodRatio;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return likelihoodRatio;
    }
  
    /**
	 * @return the markers
	 */
	public Vector<String> getMarkers() {
		return this.markers;
	}
	/**
	 * @param markers the markers to set
	 */
	public void setMarkers(Vector<String> markers) {
		try {
			this.markers = markers;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the markersfile
	 */
	public String getMarkersfile() {
		return this.markersfile;
	}
	/**
	 * @param markersfile the markersfile to set
	 */
	public void setMarkersfile(String markersfile) {
		try {
			this.markersfile = markersfile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the numberOfSimulations
	 */
	public int getNumberOfSimulations() {
		return this.numberOfSimulations;
	}
	/**
	 * @param numberOfSimulations the numberOfSimulations to set
	 */
	public void setNumberOfSimulations(int numberOfSimulations) {
		try {
			this.numberOfSimulations = numberOfSimulations;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param interMarkerDistances the interMarkerDistances to set
	 */
	public void setInterMarkerDistances(double[] interMarkerDistances) {
		try {
			this.interMarkerDistances = interMarkerDistances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the numberOfLinesInFile
	 */
	public int getNumberOfLinesInFile() {
		return this.numberOfLinesInFile;
	}
	/**
	 * @param numberOfLinesInFile the numberOfLinesInFile to set
	 */
	public void setNumberOfLinesInFile(int numberOfLinesInFile) {
		try {
			this.numberOfLinesInFile = numberOfLinesInFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the numberOfIntervals
	 */
	public int getNumberOfIntervals() {
		return this.numberOfIntervals;
	}
	/**
	 * @param numberOfIntervals the numberOfIntervals to set
	 */
	public void setNumberOfIntervals(int numberOfIntervals) {
		try {
			this.numberOfIntervals = numberOfIntervals;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Vector<String> markers;
    private Utilities utilities;
    private int numberOfLinesInFile;
    private int columnsInRawData, rowsInRawData;
    private Object[][] rawData;
    private int numberOfTetrads; // from VB Code
    private int numberOfIntervals; // from VB Code
    private int numberOfMarkers; // from VB Code
    private int[][] tetradData; // from VB Code
    
    private double[] interMarkerDistancesInActualData; // from VB Code
    private double[] interMarkerDistances;
    private double[] normalizedIntermarkerDistances;
    
    private boolean estimateInterferenceParameterUnderNullModel, estimateInterferenceParameterUnderAlternateModel;
    private int numberOfSimulations; // from VB Code 
    private int copiedTetradData[][];
    
    private Integer simulatedMValuesUnderNullModel[];
    private Double simulatedNullMinNegLogLikeValues[];
    private Double simulatedMinNegLogLikeValuesUnderAltModel[];
    private Integer simulatedMValuesUnderAltModel[];
    private Double simulatedPValuesUnderAltModel[];
    private Double simulatedLikelihoodValues[];
    private Double likelihoodRatio; 
    private String markersfile="";
    private boolean  performSimulations=false;
    private int current;
    private boolean done=false;
    private MatrixUtilities matrixUtils;
	private double minNegLogLikelihood; //from VB Code 
	private MLENullModel mlenullmodel;
	private MLEAltModel mlealtmodel;
	/**
	 * @return the columnsInRawData
	 */
	private int getColumnsInRawData() {
		return this.columnsInRawData;
	}
	/**
	 * @param columnsInRawData the columnsInRawData to set
	 */
	private void setColumnsInRawData(int columnsInRawData) {
		try {
			this.columnsInRawData = columnsInRawData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the rowsInRawData
	 */
	private int getRowsInRawData() {
		return this.rowsInRawData;
	}
	/**
	 * @param rowsInRawData the rowsInRawData to set
	 */
	private void setRowsInRawData(int rowsInRawData) {
		try {
			this.rowsInRawData = rowsInRawData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the rawData
	 */
	private Object[][] getRawData() {
		return this.rawData;
	}
	/**
	 * @param rawData the rawData to set
	 */
	private void setRawData(Object[][] rawData) {
		try {
			this.rawData = rawData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the interMarkerDistancesInActualData
	 */
	private double[] getInterMarkerDistancesInActualData() {
		return this.interMarkerDistancesInActualData;
	}
	/**
	 * @param interMarkerDistancesInActualData the interMarkerDistancesInActualData to set
	 */
	private void setInterMarkerDistancesInActualData(double[] interMarkerDistancesInActualData) {
		try {
			this.interMarkerDistancesInActualData = interMarkerDistancesInActualData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the normalizedIntermarkerDistances
	 */
	public double[] getNormalizedIntermarkerDistances() {
		return this.normalizedIntermarkerDistances;
	}
	/**
	 * @param normalizedIntermarkerDistances the normalizedIntermarkerDistances to set
	 */
	public void setNormalizedIntermarkerDistances(double[] normalizedIntermarkerDistances) {
		try {
			this.normalizedIntermarkerDistances = normalizedIntermarkerDistances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the minNegLogLikelihood
	 */
	public double getMinNegLogLikelihood() {
		return this.minNegLogLikelihood;
	}
	/**
	 * @return the estimateInterferenceParameterUnderNullModel
	 */
	private boolean isEstimateInterferenceParameterUnderNullModel() {
		return this.estimateInterferenceParameterUnderNullModel;
	}
	/**
	 * @param estimateInterferenceParameterUnderNullModel the estimateInterferenceParameterUnderNullModel to set
	 */
	private void setEstimateInterferenceParameterUnderNullModel(boolean estimateInterferenceParameterUnderNullModel) {
		try {
			this.estimateInterferenceParameterUnderNullModel = estimateInterferenceParameterUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the estimateInterferenceParameterUnderAlternateModel
	 */
	private boolean isEstimateInterferenceParameterUnderAlternateModel() {
		return this.estimateInterferenceParameterUnderAlternateModel;
	}
	/**
	 * @param estimateInterferenceParameterUnderAlternateModel the estimateInterferenceParameterUnderAlternateModel to set
	 */
	private void setEstimateInterferenceParameterUnderAlternateModel(
			boolean estimateInterferenceParameterUnderAlternateModel) {
		try {
			this.estimateInterferenceParameterUnderAlternateModel = estimateInterferenceParameterUnderAlternateModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedMValuesUnderNullModel
	 */
	private Integer[] getSimulatedMValuesUnderNullModel() {
		return this.simulatedMValuesUnderNullModel;
	}
	/**
	 * @param simulatedMValuesUnderNullModel the simulatedMValuesUnderNullModel to set
	 */
	private void setSimulatedMValuesUnderNullModel(Integer[] simulatedMValuesUnderNullModel) {
		try {
			this.simulatedMValuesUnderNullModel = simulatedMValuesUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedNullMinNegLogLikeValues
	 */
	private Double[] getSimulatedNullMinNegLogLikeValues() {
		return this.simulatedNullMinNegLogLikeValues;
	}
	/**
	 * @param simulatedNullMinNegLogLikeValues the simulatedNullMinNegLogLikeValues to set
	 */
	private void setSimulatedNullMinNegLogLikeValues(Double[] simulatedNullMinNegLogLikeValues) {
		try {
			this.simulatedNullMinNegLogLikeValues = simulatedNullMinNegLogLikeValues;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedMinNegLogLikeValuesUnderAltModel
	 */
	private Double[] getSimulatedMinNegLogLikeValuesUnderAltModel() {
		return this.simulatedMinNegLogLikeValuesUnderAltModel;
	}
	/**
	 * @param simulatedMinNegLogLikeValuesUnderAltModel the simulatedMinNegLogLikeValuesUnderAltModel to set
	 */
	private void setSimulatedMinNegLogLikeValuesUnderAltModel(Double[] simulatedMinNegLogLikeValuesUnderAltModel) {
		try {
			this.simulatedMinNegLogLikeValuesUnderAltModel = simulatedMinNegLogLikeValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedMValuesUnderAltModel
	 */
	private Integer[] getSimulatedMValuesUnderAltModel() {
		return this.simulatedMValuesUnderAltModel;
	}
	/**
	 * @param simulatedMValuesUnderAltModel the simulatedMValuesUnderAltModel to set
	 */
	private void setSimulatedMValuesUnderAltModel(Integer[] simulatedMValuesUnderAltModel) {
		try {
			this.simulatedMValuesUnderAltModel = simulatedMValuesUnderAltModel;
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
	private void setSimulatedPValuesUnderAltModel(Double[] simulatedPValuesUnderAltModel) {
		try {
			this.simulatedPValuesUnderAltModel = simulatedPValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the simulatedLikelihoodValues
	 */
	private Double[] getSimulatedLikelihoodValues() {
		return this.simulatedLikelihoodValues;
	}
	/**
	 * @param simulatedLikelihoodValues the simulatedLikelihoodValues to set
	 */
	private void setSimulatedLikelihoodValues(Double[] simulatedLikelihoodValues) {
		try {
			this.simulatedLikelihoodValues = simulatedLikelihoodValues;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the likelihoodRatio
	 */
	private Double getLikelihoodRatio() {
		return this.likelihoodRatio;
	}
	/**
	 * @param likelihoodRatio the likelihoodRatio to set
	 */
	private void setLikelihoodRatio(Double likelihoodRatio) {
		try {
			this.likelihoodRatio = likelihoodRatio;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the performSimulations
	 */
	public boolean isPerformSimulations() {
		return this.performSimulations;
	}
	/**
	 * @param performSimulations the performSimulations to set
	 */
	private void setPerformSimulations(boolean performSimulations) {
		try {
			this.performSimulations = performSimulations;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the matrixUtils
	 */
	private MatrixUtilities getMatrixUtils() {
		return this.matrixUtils;
	}
	/**
	 * @param matrixUtils the matrixUtils to set
	 */
	private void setMatrixUtils(MatrixUtilities matrixUtils) {
		try {
			this.matrixUtils = matrixUtils;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(int current) {
		try {
			this.current = current;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		try {
			this.done = done;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the numberOfMarkers
	 */
	public int getNumberOfMarkers() {
		return numberOfMarkers;
	}
	/**
	 * @param numberOfMarkers the numberOfMarkers to set
	 */
	public void setNumberOfMarkers(int numberOfMarkers) {
		try {
			this.numberOfMarkers = numberOfMarkers;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param numberOfTetrads the numberOfTetrads to set
	 */
	public void setNumberOfTetrads(int numberOfTetrads) {
		try {
			this.numberOfTetrads = numberOfTetrads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the copiedTetradData
	 */
	public int[][] getCopiedTetradData() {
		return copiedTetradData;
	}
	/**
	 * @param copiedTetradData the copiedTetradData to set
	 */
	public void setCopiedTetradData(int copiedTetradData[][]) {
		try {
			this.copiedTetradData = copiedTetradData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the tetradData
	 */
	public int[][] getTetradData() {
		return tetradData;
	}
	/**
	 * @param tetradData the tetradData to set
	 */
	public void setTetradData(int[][] tetradData) {
		try {
			this.tetradData = tetradData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the utilities
	 */
	public Utilities getUtilities() {
		return utilities;
	}
	/**
	 * @param utilities the utilities to set
	 */
	public void setUtilities(Utilities utilities) {
		this.utilities = utilities;
	}
	/**
	 * computes loglikelihood ratio of which model fits the tetrad data better
	 */
	public void computeLikelihoodRatio(MLENullModel mlenullmodel, MLEAltModel mlealtmodel) {
	    try {
			likelihoodRatio = 2*(mlenullmodel.getNullModelMinNegLogLikelihood() - mlealtmodel.getAltModelMinNegLogLikelihood());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param minNegLogLikelihood the minNegLogLikelihood to set
	 */
	public void setMinNegLogLikelihood(double minNegLogLikelihood) {
		this.minNegLogLikelihood = minNegLogLikelihood;
	}
	/**
	 * @return the mlenullmodel
	 */
	public MLENullModel getMlenullmodel() {
		return mlenullmodel;
	}
	/**
	 * @param mlenullmodel the mlenullmodel to set
	 */
	public void setMlenullmodel(MLENullModel mlenullmodel) {
		this.mlenullmodel = mlenullmodel;
	}
	/**
	 * @return the mlealtmodel
	 */
	public MLEAltModel getMlealtmodel() {
		return mlealtmodel;
	}
	/**
	 * @param mlealtmodel the mlealtmodel to set
	 */
	public void setMlealtmodel(MLEAltModel mlealtmodel) {
		this.mlealtmodel = mlealtmodel;
	}
	
}


