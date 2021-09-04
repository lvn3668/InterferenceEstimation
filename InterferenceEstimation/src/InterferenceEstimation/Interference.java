package InterferenceEstimation;
/*
 * Interference.java
 * Conversion of VB Code for cross over interference in tetrads written by Dr. Housworth
 * Initially Created on September 27, 2004, 3:20 PM
 */
import java.io.*;
//import Jama.*;
import java.util.*;
import javax.swing.*;
//import org.apache.commons.math3.*;

import matrixUtilities.MatrixUtilities;

/**
 * Main class that performs backend processing for computing interference paramater
 * @authors Elizabeth Housworth (Indiana University Bloomington) and Lalitha Viswanath
 */
public class Interference {
    
    /**
     * Creates a new instance of Interference. It takes in a string containing the filename of the file containing the raw tetrad data as an input
     * @param fileName string containing the filename of the file containing tetrad data or intermarker distances
     */
    
    public Interference(String fileName) {
        try {
			setMarkers(new Vector<String>());
			setMarkersfile(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
    /** Default constructor for Interference class */
    public Interference() {
        try {
			setMarkers(new Vector<String>());
			setCurrent(0);
			setMatrixUtils(new MatrixUtilities());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Constructor that creates a  new instance of the Interference class. 
     * It takes in a file as an input. 
     * It initialises the variable 'current' controlling the progress bar. 
     * This constructor is called by ExecuteBackEnd to perform the analyses
     * @param fileName fileName is of type File
     */
    public Interference(File fileName) {
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
    public Interference(File fileName, int numSimulations, boolean NullModel, boolean AltModel, boolean Sims) {
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
     * 
     * @param tempm
     * @return
     */
    //GoldSection for Alternate Model
    private double goldSectionp(int tempm) {
        try {
			double pa, pb, px, py, fpx, fpy, fp, f0, Bestp,r;
			int IntI, IntJ;
			pa=0.0;
			pb=1.0;
			// (5 ^ 1/2 - 1)/2
			// downhill simplex (ref press et al 1988)
			// 
			double tmp = (Math.pow(5.0,0.5)-1)/(2);
			r=tmp;
			px=pa+(1-r)*(pb-pa);
			// Normalize as p = m+1
			// 2*p*(m+1)*(1-p)*intermarkerdistances
			for(int i=0;i<getNumberOfIntervals();i++){
			    getNormalizedIntermarkerDistances()[i]=2.0*(px+(tempm+1.0)*(1-px))*getInterMarkerDistances()[i];}
			computeNegLogLikeTetradData(px,tempm);
			fpx=getNegativeLogLikelihood();
			py=pb-(1-r)*(pb-pa);
			for(int i=0;i<getNumberOfIntervals();i++) {
			    getNormalizedIntermarkerDistances()[i]=2.0*(py+(tempm+1.0)*(1.0-py))*getInterMarkerDistances()[i];
			}
			computeNegLogLikeTetradData(py,tempm);
			fpy=getNegativeLogLikelihood();
			
			for(int j=1;j<=8;j++) {
			    if(fpx<fpy) {
			        pb=py;
			        fpy=fpx;
			        py=px;
			        px=pa+((1-r)*(pb-pa));
			        for(int i=0;i<getNumberOfIntervals();i++) {
			            getNormalizedIntermarkerDistances()[i]=2.0*(px+( tempm + 1.0 )*(1.0-px))*getInterMarkerDistances()[i];
			        }
			        computeNegLogLikeTetradData(px,tempm);
			        
			        fpx=getNegativeLogLikelihood();
			    }else {
			        pa=px;
			        px=py;
			        fpx=fpy;
			        py=pb-(1-r)*(pb-pa);
			        for(int i=0;i<getNumberOfIntervals();i++){
			            getNormalizedIntermarkerDistances()[i]=2.0*(py+(tempm + 1.0 )*(1.0-py))*getInterMarkerDistances()[i];}
			        computeNegLogLikeTetradData(py,tempm);
			        fpy=getNegativeLogLikelihood();
			    }
			}
			Bestp=(pa+pb)/2;
			if(Bestp < 0.03) {
			    for(int i=0;i<getNumberOfIntervals();i++){
			        getNormalizedIntermarkerDistances()[i]=2.0*(Bestp+(tempm+1.0)*(1.0-Bestp))*getInterMarkerDistances()[i];}
			    computeNegLogLikeTetradData(Bestp,tempm);
			    fp=getNegativeLogLikelihood();
			    for(int i=0;i<getNumberOfIntervals();i++){
			        getNormalizedIntermarkerDistances()[i]=2.0*(tempm+1.0)*getInterMarkerDistances()[i];}
			    computeNegLogLikeTetradData(0,tempm);
			    f0=getNegativeLogLikelihood();
			    if(f0<fp)
			        Bestp=0;
			}
			
			return Bestp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
    }
    // find MLE under Alternate Model
    /**
     * Finds Maximum Likelihood estimate using alternate model
     */
    public void findMaxLikelihoodEstimateSAlt() {
        try {
			setmUnderAltModel(0);
			setPvalueUnderAltModel(0.0);
			if(!isPerformSimulations()) {
			    setDone(false);
			    setCurrent(getmUnderAltModel());
			}
			double tempp;
			setNormalizedIntermarkerDistances(new double[getNumberOfIntervals()]);
			for(int i=0;i<getNumberOfIntervals();i++){
			    getNormalizedIntermarkerDistances()[i]=2.0*getInterMarkerDistances()[i];}
			
			// For pvalueUnderAltModel and mUnderAltModel values, find negative log likelihood under the alt model 
			computeNegLogLikeTetradData(getPvalueUnderAltModel(), getmUnderAltModel());
			setMinNegLogLikelihood(getNegativeLogLikelihood());
			// For values of interference parameter going from 1 through 20
			// get p value from GoldenSection algorithm
			// Get normalized values of intermarker distances
			// for each value of m, find neg log likelihood of tetrad data
			// find the p and m values with lowest log likelihood
			for(int m=1;m<=20;m++) {
			    if(!performSimulations)
			        setCurrent(m);
			    tempp=goldSectionp(m);
			    for(int counter=0;counter<getNumberOfIntervals();counter++)
			        getNormalizedIntermarkerDistances()[counter]=2.0*(tempp+(1-tempp)*(m+1))*getInterMarkerDistances()[counter];
			    computeNegLogLikeTetradData(tempp,m);
			    if(getNegativeLogLikelihood() < getMinNegLogLikelihood()) {
			        setPvalueUnderAltModel(tempp);
			        setmUnderAltModel(m);
			        setMinNegLogLikelihood(getNegativeLogLikelihood());
			    } else {
			        break;
			    }
			}
			setAltModelMinNegLogLikelihood(minNegLogLikelihood);
			if(!isPerformSimulations()){
			   setCurrent(20);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			    current = Sim;
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
			        findMaxLikelihoodEstimateSNull();
			        getSimulatedMValuesUnderNullModel()[Sim] = getmUnderNullModel();
			        getSimulatedNullMinNegLogLikeValues()[Sim] = getNullModelMinNegLogLikelihood();
			     }
			    if(altModel){    
			        findMaxLikelihoodEstimateSAlt();
			        getSimulatedMValuesUnderAltModel()[Sim]=mUnderAltModel;
			        getSimulatedPValuesUnderAltModel()[Sim]=pvalueUnderAltModel;
			        getSimulatedMinNegLogLikeValuesUnderAltModel()[Sim] = altModelMinNegLogLikelihood;
			     }
			    if(nullModel && altModel)
			        getSimulatedLikelihoodValues()[Sim] = 2.0*(simulatedNullMinNegLogLikeValues[Sim] - simulatedMinNegLogLikeValuesUnderAltModel[Sim]);
			} // for all simulations
			
			current = NumberOfSimulations;
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
    public void computeNegativeLogLikelihoodSingleSporeData(double prob, int tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted) 
    {
        try {
			setNegativeLogLikelihood(0);
			matrixUtils.setIdentityMatrix(new double[1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setIdentityMatrixTranspose(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			matrixUtils.setRunningMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setMatrixSum(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setTempMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			int test=0;
			double probability=0;
			int knumberofdoublestranddnabreaks=1;
			
			for(int i=0;i<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;i++) {
			    matrixUtils.getIdentityMatrix()[0][i]=1.0;
			    matrixUtils.getIdentityMatrixTranspose()[i][0]=1.0;
			}
			// Number of single spores = number of cols of intermarker distances minus one 
			for(int tetradcounter=0;tetradcounter<getNoTetrads();tetradcounter++) {
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
			                    matrixUtils.getRunningMatrix()[tmp1][tmp2]=1.00;
			                else
			                    matrixUtils.getRunningMatrix()[tmp1][tmp2]=0.00;
			            }
			        }
			        
			        for(int cols=0;cols<getNumberOfIntervals();cols++) {
			            double y = normalizedIntermarkerDistances[cols];
			            // if it is a tetratype
			            // compute tetrad type based on intermarker distances 
			            // for each tetrad type (non parental ditype, tetratype, parental ditype)
			            // compute probability based on Mathers formula (assumption: Non Chromatid Interference)
			            // Use a running matrix to compute posterior prob of a tetrad pattern
			            // MLE computed using simplex method fof find optimal value of m alone 
			            // Under null model
			            if(getTetradData()[tetradcounter][cols]==0) {
			                matrixUtils.setdMatrix(null);
			                matrixUtils.dMatrixFormation(prob,1,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        matrixUtils.getMatrixSum()[counter][innercounter]=matrixUtils.getdMatrix()[counter][innercounter];
			                    }
			                }
			                // k = number of double strand dna breaks over which negative log
			                // likelihood is calculated for different values of m 
			                // the number of ds dna breaks after which a successful cross is effected
			                
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                    matrixUtils.setdMatrix(null);
			                    matrixUtils.dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=(1.00/2.00)*matrixUtils.getdMatrix()[counter][innercounter];
			                        }
			                    }
			                }// for all K
			                

			                // check
			                if(getTetradData()[tetradcounter][cols]==0) {
			                    matrixUtils.setdMatrix(null);                 
			                    matrixUtils.dMatrixFormation(prob, 0,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=matrixUtils.getdMatrix()[counter][innercounter];
			                        }
			                    }
			                    
			                }
			            } 
			            else 
			            {
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        matrixUtils.getMatrixSum()[counter][innercounter]=0.0;
			                    }
			                }
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                    matrixUtils.setdMatrix(null);
			                    matrixUtils.dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=(1.00/2.00)*matrixUtils.getdMatrix()[counter][innercounter];
			                        }
			                    }
			                }
			                
			                
			            }// end of else
			            
			            matrixUtils.setMatrixProduct(null);
			            matrixUtils.findMatrixProduct(matrixUtils.getRunningMatrix(), 
			            		matrixUtils.getMatrixSum());
			            
			            for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                    matrixUtils.getRunningMatrix()[counter][innercounter] = matrixUtils.getMatrixProduct()[counter][innercounter];
			                }
			            }
			            
			        } // finishing loop over all columns
			        
			        matrixUtils.findMatrixProduct(matrixUtils.getRunningMatrix(), matrixUtils.getIdentityMatrixTranspose());            
			        for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++){
			            matrixUtils.getTempMatrix()[counter][0] = matrixUtils.getMatrixProduct()[counter][0];
			            }
			        
			        matrixUtils.setMatrixProduct(null);
			        // then take with one
			        matrixUtils.findMatrixProduct(matrixUtils.getIdentityMatrix(), matrixUtils.getTempMatrix());                
			        probability = matrixUtils.getMatrixProduct()[0][0]/(tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1);                
			    }// if test > 0	    
			    setNegativeLogLikelihood(getNegativeLogLikelihood() - Math.log(probability));
			}// for each row
			matrixUtils.setIdentityMatrix(null);
			matrixUtils.setIdentityMatrixTranspose(null);
			matrixUtils.setRunningMatrix(null);
			matrixUtils.setMatrixProduct(null);
			matrixUtils.setTempMatrix(null);
			matrixUtils.setMatrixSum(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
    
    // find MLE under Null Model
    /**
     * This method finds the maximum likelihood estimate 
     * under the counting model or the null model
     */
    public void findMaxLikelihoodEstimateSNull() {
        try {
			int m=0;        
			double p=0;
			normalizedIntermarkerDistances = new double[getNumberOfIntervals()];
			for(int i=0;i<getNumberOfIntervals();i++){
			    normalizedIntermarkerDistances[i]=2.0*(m+1)*getInterMarkerDistances()[i];}
			if(!performSimulations) {
			    done=false;
			    current = m;
			}
			computeNegLogLikeTetradData(p,m);
			minNegLogLikelihood = getNegativeLogLikelihood();
			for( m=1;m<=20;m++) {
			    if(!performSimulations)
			        current = m;
			    for(int i=0;i<getNumberOfIntervals();i++){
			        normalizedIntermarkerDistances[i]=2.0*(m+1)*getInterMarkerDistances()[i];}
			    computeNegLogLikeTetradData(p,m);
			    if(getNegativeLogLikelihood() < minNegLogLikelihood) {
			        mUnderNullModel = m;
			        minNegLogLikelihood = getNegativeLogLikelihood();
			    } else {
			        break;
			    }
			}        
      nullModelMinNegLogLikelihood  = minNegLogLikelihood;
			if(!performSimulations){
			   current = 20;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Compute Neg Log Likelihood of Tetrad Data
    /**
     * 
     * @param prob
     * @param tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted
     */
    private void computeNegLogLikeTetradData(double prob, int tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted) {
        
        try {
			setNegativeLogLikelihood(0);
			matrixUtils.setIdentityMatrix(new double[1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setIdentityMatrixTranspose(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			matrixUtils.setRunningMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setMatrixSum(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			matrixUtils.setTempMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			setMatrixUtils(new MatrixUtilities());
			int test=0;
			double probability=0;
			int knumberofdoublestranddnabreaks=1;
			
			for(int i=0;i<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;i++) {
			    matrixUtils.getIdentityMatrix()[0][i]=1.0;
			    matrixUtils.getIdentityMatrixTranspose()[i][0]=1.0;
			}
			// Number of tetrads = number of cols of intermarker distances minus one 
			for(int tetradcounter=0;tetradcounter<getNoTetrads();tetradcounter++) {
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
			                    matrixUtils.getRunningMatrix()[tmp1][tmp2]=1.00;
			                else
			                    matrixUtils.getRunningMatrix()[tmp1][tmp2]=0.00;
			            }
			        }
			        
			        for(int cols=0;cols<getNumberOfIntervals();cols++) {
			            double y = normalizedIntermarkerDistances[cols];
			            // if it is a tetratype
			            // compute tetrad type based on intermarker distances 
			            // for each tetrad type (non parental ditype, tetratype, parental ditype)
			            // compute probability based on Mathers formula (assumption: Non Chromatid Interference)
			            // Use a running matrix to compute posterior prob of a tetrad pattern
			            // MLE computed using simplex method fof find optimal value of m alone 
			            // Under null model
			            if(getTetradData()[tetradcounter][cols]==1) {
			            	matrixUtils.setdMatrix(null);
			                matrixUtils.dMatrixFormation(prob,1,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        matrixUtils.getMatrixSum()[counter][innercounter]=matrixUtils.getdMatrix()[counter][innercounter];
			                    }
			                }
			                // k = number of double strand dna breaks over which negative log
			                // likelihood is calculated for different values of m 
			                // the number of ds dna breaks after which a successful cross is effected
			                
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                	matrixUtils.setdMatrix(null);
			                    matrixUtils.dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=(2.00/3.00*(1-Math.pow((-0.5),knumberofdoublestranddnabreaks)))*(matrixUtils.getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                }// for all K
			            } // completes the TetraType if loop
			            else // if it is PD or NPD, add the 1/3 formula first
			                // then add Do if it is PD
			            {
			                for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                    for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                        matrixUtils.getMatrixSum()[counter][innercounter]=0.0;
			                    }
			                }
			                for(knumberofdoublestranddnabreaks=2;knumberofdoublestranddnabreaks<=5;knumberofdoublestranddnabreaks++) {
			                    matrixUtils.setdMatrix(null);
			                    
			                    matrixUtils.dMatrixFormation(prob, knumberofdoublestranddnabreaks,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=(1.00/3.00)*((1.00/2.00) +Math.pow((-0.5),knumberofdoublestranddnabreaks))*(matrixUtils.getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                }
			                
			                // check
			                if(getTetradData()[tetradcounter][cols]==0) {
			                    matrixUtils.setdMatrix(null);                 
			                    matrixUtils.dMatrixFormation(prob, 0,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            matrixUtils.getMatrixSum()[counter][innercounter]+=(matrixUtils.getdMatrix()[counter][innercounter]);
			                        }
			                    }
			                    
			                }
			            }// end of else
			            
			            matrixUtils.setMatrixProduct(null);
			            matrixUtils.findMatrixProduct(matrixUtils.getRunningMatrix(), matrixUtils.getMatrixSum());
			            
			            for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                    matrixUtils.getRunningMatrix()[counter][innercounter] = matrixUtils.getMatrixProduct()[counter][innercounter];
			                }
			            }
			            
			        } // finishing loop over all columns
			        
			        matrixUtils.findMatrixProduct(matrixUtils.getRunningMatrix(),matrixUtils.getIdentityMatrixTranspose());            
			        for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++){
			            matrixUtils.getTempMatrix()[counter][0] = matrixUtils.getMatrixProduct()[counter][0];}
			        
			        matrixUtils.setMatrixProduct(null);
			        // then take with one
			        matrixUtils.findMatrixProduct(matrixUtils.getIdentityMatrix(), matrixUtils.getTempMatrix());                
			        probability = matrixUtils.getMatrixProduct()[0][0]/(tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1);                
			    }// if test > 0
			    
			    setNegativeLogLikelihood(getNegativeLogLikelihood() - Math.log(probability));
			}// for each row
			matrixUtils.setIdentityMatrix(null);
			matrixUtils.setIdentityMatrixTranspose(null);
			matrixUtils.setRunningMatrix(null);
			matrixUtils.setMatrixProduct(null);
			matrixUtils.setTempMatrix(null);
			matrixUtils.setMatrixSum(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // compute factorial
    /**
     * @param number
     * @return calculate factorial 
     */
    private static double computefactorial(int number) {
        try {
			double answer=1.00;
			double factNum = number;
			if(number>1) {
			    for(int counter=1;counter<=number;counter++){
			        answer=answer*counter;}
			}
			return answer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
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
			return interMarkerDistances;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return interMarkerDistances;
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
			for(int x=2;x<rowsInRawData;x+=4) {
			    for(int xy=0;xy<columnsInRawData-1;xy++) {
			        
			        int sum = Math.abs(((Integer)rawData[x][xy]).intValue()-((Integer)rawData[x][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+1][xy]).intValue()-((Integer)rawData[x+1][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+2][xy]).intValue()-((Integer)rawData[x+2][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+3][xy]).intValue()-((Integer)rawData[x+3][xy+1]).intValue());
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
			setNumberOfMarkers(rawData[0].length+1);
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
			setNumberOfIntervals(rawData[0].length);
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
    @SuppressWarnings("deprecation")
	private void fillrawdata() {
        try {
			rawData=new Object[getNumberOfLinesInFile()][];
			Iterator<String> i = getMarkers().iterator();
			String tmp;
			String[] splitFields= new String[1];
			int counter=0;
			while(i.hasNext()) {
			    tmp=(String)i.next();
			    splitFields=tmp.split("\t");
			    rawData[counter]=new Object[splitFields.length];
			    for(int temp=0;temp<splitFields.length;temp++) {
			        rawData[counter][temp]= Integer.parseInt(splitFields[temp]);
			    }
			    counter++;
			}
			columnsInRawData = splitFields.length;
			rowsInRawData = counter;
			setNumberOfTetrads(rowsInRawData);
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
			for(int x=0;x<rowsInRawData;x++) {
			    for(int xy=0;xy<columnsInRawData;xy++) {
			        getTetradData()[Tetrad][xy]=Integer.parseInt(rawData[x][xy]+"");
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
			        String one = concatenate(getTetradData()[i]);
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

   /**
    * 
    * @param intArray
    * @return 
    */
    
    private static String concatenate(int[] intArray) {
        try {
			String tmp="";
			for(int i=0;i<intArray.length;i++)
			    tmp=tmp+intArray[i];
			return tmp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    private void printRawData() {
        try {
			int rows,cols;
			for(rows=0;rows<rowsInRawData;rows++) {
			    for(cols=0;cols<columnsInRawData;cols++)
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
            for(int i=0;i<rowsInRawData;i++) {
                for(int j=0;j<columnsInRawData;j++) {
                    
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
			setNumberOfMarkers(columnsInRawData);
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
        rawData[counter]=new Object[parentalData1.length];
        for(int temp=0;temp<parentalData1.length;temp++)
            rawData[counter][temp]= Integer.valueOf(0);
        counter++;
        
        // read second line
        tmp2=(String)i.next();
        parentalData2 = tmp2.split("\t");
        rawData[counter]=new Object[parentalData2.length];
        for(int temp=0;temp<parentalData2.length;temp++)
            rawData[counter][temp]= Integer.valueOf(1);
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
                rawData[counter]=new Object[splitFields1.length];
                for(int temp=0;temp<splitFields1.length;temp++) {
                    if(splitFields1[temp].equals(parentalData1[temp]))
                        rawData[counter][temp]= Integer.valueOf(0);
                    else if(splitFields1[temp].equals(parentalData2[temp]))
                        rawData[counter][temp]=Integer.valueOf(1);
                }
                counter++;
                rawData[counter]=new Object[splitFields2.length];
                for(int temp=0;temp<splitFields2.length;temp++) {
                    if(splitFields2[temp].equals(parentalData1[temp]))
                        rawData[counter][temp]= Integer.valueOf(0);
                    else if(splitFields2[temp].equals(parentalData2[temp]))
                        rawData[counter][temp]=Integer.valueOf(1);
                }
                counter++;
                rawData[counter]=new Object[splitFields3.length];
                for(int temp=0;temp<splitFields3.length;temp++) {
                    if(splitFields3[temp].equals(parentalData1[temp]))
                        rawData[counter][temp]= Integer.valueOf(0);
                    else if(splitFields3[temp].equals(parentalData2[temp]))
                        rawData[counter][temp]= Integer.valueOf(1);
                }
                counter++;
                rawData[counter]=new Object[splitFields4.length];
                for(int temp=0;temp<splitFields4.length;temp++) {
                    if(splitFields4[temp].equals(parentalData1[temp]))
                        rawData[counter][temp]= Integer.valueOf(0);
                    else if(splitFields4[temp].equals(parentalData2[temp]))
                        rawData[counter][temp]= Integer.valueOf(1);
                }
                counter++;
            }
        }
        columnsInRawData = splitFields1.length;
        rowsInRawData = counter;
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
            System.out.println("IOException " + ioexception + "in Interference.java!");
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
     * Sets the interference parameter under the null model
     * @param number Integer value of interference parameter under null model
     */
    public void setNullM(int number)
    {
        try {
			mUnderNullModel = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Sets the interference parameter under the alternate model
     * @param number Integer value of interference parameter under alternate model
     */
    public void setAltM(int number)
    {
        try {
			mUnderAltModel = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Method that sets proportion of non interfering crossovers
     * @param number double value of proportion of non-interfering crossovers under alternate model
     */
    public void setAltp(double number)
    {
        try {
			pvalueUnderAltModel = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Sets minimum log likelihood value under null model
     * @param number double specifying the log likelihood value under null model
     */
    public void setNullModelMinNegLogLike(double number) {
        try {
			nullModelMinNegLogLikelihood = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Sets minimum log likelihood value under alternate model
     * @param number double specifying the log likelihood value under alternate model
     */
    public void setAltModelMinNegLogLike(double number) {
        try {
			altModelMinNegLogLikelihood = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns loglikelihood under null model
     * @return double 
     */
    public double getNullModelMinNegLogLike() {
        try {
			return nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nullModelMinNegLogLikelihood;
        
    }
    
    /**
     * M value under null model
     * @return integer containing m value
     */
    public int getNullModelM() {
        try {
			return mUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mUnderNullModel;
    }
    /**
     * interference parameter under alternate model
     * @return integer 
     */
    public int getNoTetrads() {
        try {
			return getNumberOfTetrads();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getNumberOfTetrads();
    }

    public int getAltModelM() {
        try {
			return mUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return mUnderAltModel;
    }
    /**
     * p value under extended counting model
     * @return double
     */
    public double getAltp() {
        try {
			return pvalueUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pvalueUnderAltModel;
    }
    /**
     * returns null log likelihood
     * @return double
     */
    public double getNullLogLike() {
        try {
			return nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nullModelMinNegLogLikelihood;
    }
    /**
     * returns log likelihood under alternate model
     * @return double 
     */
    public double getAltModelLogLike() {
        try {
			return altModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return altModelMinNegLogLikelihood;
    }
    /**
     * returns vector of simulated null-m values
     * @return Vector of integers
     */
    public Double[] getSimulatedNullModelLogLike() {
        try {
			return simulatedNullMinNegLogLikeValues;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simulatedNullMinNegLogLikeValues;
    }
    /**
     * returns vector of simulated p values under alternate model
     * @return Vector of doubles
     */
    public Double[] getSimulatedAltPValues() {
        try {
			return simulatedPValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simulatedPValuesUnderAltModel;
    }
    /**
     * returns vector of likelihood values under alternate model
     * @return Vector of doubles
     */
    public Double[] getSimulatedAltModelLogLike() {
        try {
			return simulatedMinNegLogLikeValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simulatedMinNegLogLikeValuesUnderAltModel;
    }
    /**
     * 
     * @return Array of values generated from Simulated Model (Null Hypothesis Model)
     */
    public Integer[] getSimulatedNullModelMValues() {
        try {
			return simulatedMValuesUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simulatedMValuesUnderNullModel;
    }
    /**
     * return Array of values generated from Alt Model  
    */
    public Integer[] getSimulatedAltModelValues() {
        try {
			return simulatedMValuesUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simulatedMValuesUnderAltModel;
    }
    /**
     * computes loglikelihood ratio of which model fits the tetrad data better
     */
    public void computeLikeRatio() {
        try {
			likelihoodRatio = 2*(nullModelMinNegLogLikelihood - altModelMinNegLogLikelihood);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * returns loglikelihood ratio
     * @return double value containing loglikelihood ratio
     */
    public double getLikeRatio() {
        try {
			return likelihoodRatio;
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
		return markers;
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
		return markersfile;
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
		return numberOfSimulations;
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
		return numberOfLinesInFile;
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
		return numberOfIntervals;
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
    
    private double negativeLogLikelihood; // from VB Code
    private double minNegLogLikelihood; //from VB Code
    
    private double nullModelMinNegLogLikelihood, altModelMinNegLogLikelihood;
    private int mUnderNullModel; // from VB Code
    private int mUnderAltModel; // from VB Code
    private double pvalueUnderAltModel; // from VB Code
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
	private double[] getNormalizedIntermarkerDistances() {
		return this.normalizedIntermarkerDistances;
	}
	/**
	 * @param normalizedIntermarkerDistances the normalizedIntermarkerDistances to set
	 */
	private void setNormalizedIntermarkerDistances(double[] normalizedIntermarkerDistances) {
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
	private double getMinNegLogLikelihood() {
		return this.minNegLogLikelihood;
	}
	/**
	 * @param minNegLogLikelihood the minNegLogLikelihood to set
	 */
	private void setMinNegLogLikelihood(double minNegLogLikelihood) {
		try {
			this.minNegLogLikelihood = minNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the nullModelMinNegLogLikelihood
	 */
	private double getNullModelMinNegLogLikelihood() {
		return this.nullModelMinNegLogLikelihood;
	}
	/**
	 * @param nullModelMinNegLogLikelihood the nullModelMinNegLogLikelihood to set
	 */
	private void setNullModelMinNegLogLikelihood(double nullModelMinNegLogLikelihood) {
		try {
			this.nullModelMinNegLogLikelihood = nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the altModelMinNegLogLikelihood
	 */
	private double getAltModelMinNegLogLikelihood() {
		return this.altModelMinNegLogLikelihood;
	}
	/**
	 * @param altModelMinNegLogLikelihood the altModelMinNegLogLikelihood to set
	 */
	private void setAltModelMinNegLogLikelihood(double altModelMinNegLogLikelihood) {
		try {
			this.altModelMinNegLogLikelihood = altModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the mUnderNullModel
	 */
	private int getmUnderNullModel() {
		return this.mUnderNullModel;
	}
	/**
	 * @param mUnderNullModel the mUnderNullModel to set
	 */
	private void setmUnderNullModel(int mUnderNullModel) {
		try {
			this.mUnderNullModel = mUnderNullModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the mUnderAltModel
	 */
	private int getmUnderAltModel() {
		return this.mUnderAltModel;
	}
	/**
	 * @param mUnderAltModel the mUnderAltModel to set
	 */
	private void setmUnderAltModel(int mUnderAltModel) {
		try {
			this.mUnderAltModel = mUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the pvalueUnderAltModel
	 */
	private double getPvalueUnderAltModel() {
		return this.pvalueUnderAltModel;
	}
	/**
	 * @param pvalueUnderAltModel the pvalueUnderAltModel to set
	 */
	private void setPvalueUnderAltModel(double pvalueUnderAltModel) {
		try {
			this.pvalueUnderAltModel = pvalueUnderAltModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	private boolean isPerformSimulations() {
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
	private void setCurrent(int current) {
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
	private void setDone(boolean done) {
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
	 * @return the negativeLogLikelihood
	 */
	public double getNegativeLogLikelihood() {
		return negativeLogLikelihood;
	}
	/**
	 * @param negativeLogLikelihood the negativeLogLikelihood to set
	 */
	public void setNegativeLogLikelihood(double negativeLogLikelihood) {
		try {
			this.negativeLogLikelihood = negativeLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the numberOfTetrads
	 */
	public int getNumberOfTetrads() {
		return numberOfTetrads;
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
}


