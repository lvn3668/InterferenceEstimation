package InterferenceEstimation;
import java.awt.HeadlessException;
/*
 * Interference.java
 * Conversion of VB Code for cross over interference in tetrads written by Dr. Housworth
 * Initially Created on September 27, 2004, 3:20 PM
 */
import java.io.*;
//import Jama.*;
import java.util.*;
import java.math.*;
import javax.swing.*;
import java.lang.Integer.*;

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
			dataInFile = new Vector<String>();
			fname = fileName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
    /** Default constructor for Interference class */
    public Interference() {
        try {
			dataInFile = new Vector<String>();
			current = 0;
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
			dataInFile = new Vector<String>();
			fname = fileName.getAbsolutePath();
			current = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * Constructor that takes in a number of parameters
     * @param fileName File containing data
     * @param NoSimulations integer indicating number of simulations
     * @param NullModel boolean variable indicating whether null model is to be applied
     * @param AltModel boolean variable that declares whether alternate model is to be used or not
     * @param Sims Simulations
     */
    public Interference(File fileName, int NoSimulations, boolean NullModel, boolean AltModel, boolean Sims) {
        try {
			dataInFile = new Vector<String>();
			fname = fileName.getAbsolutePath();
			numberOfSimulations = NoSimulations;
			doNullModel = NullModel;
			doAltModel = AltModel;
			doSims = Sims;
			current = 0;
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
			interMarkerDistances = new double[noLinesInFile];
			Iterator<String> iter = dataInFile.iterator();
			int counter=0;
			try {
				// Loop through lines in file and read in intermarker distances
			    while(iter.hasNext()) {
			        interMarkerDistances[counter]=Double.parseDouble((String)iter.next());
			        counter++;
			    }
			} catch(Exception e) {
			    JOptionPane joptionPane = new JOptionPane("File is in invalid format",JOptionPane.ERROR_MESSAGE);
			    JOptionPane.showMessageDialog(null,"File is in invalid format");
			    return false;
			}
			// 
			numberOfIntervals = noLinesInFile;
			numberOfMarkers = numberOfIntervals+1;
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
			double tmp = (Math.pow(5.0,0.5)-1)/(2);
			r=tmp;
			px=pa+(1-r)*(pb-pa);
			for(int i=0;i<numberOfIntervals;i++){
			    normalizedIntermarkerDistances[i]=2.0*(px+(tempm+1.0)*(1-px))*interMarkerDistances[i];}
			computeNegLogLikeTetradData(px,tempm);
			fpx=negativeLogLikelihood;
			py=pb-(1-r)*(pb-pa);
			for(int i=0;i<numberOfIntervals;i++) {
			    normalizedIntermarkerDistances[i]=2.0*(py+(tempm+1.0)*(1.0-py))*interMarkerDistances[i];
			}
			computeNegLogLikeTetradData(py,tempm);
			fpy=negativeLogLikelihood;
			
			for(int j=1;j<=8;j++) {
			    if(fpx<fpy) {
			        pb=py;
			        fpy=fpx;
			        py=px;
			        px=pa+((1-r)*(pb-pa));
			        for(int i=0;i<numberOfIntervals;i++) {
			            normalizedIntermarkerDistances[i]=2.0*(px+( tempm + 1.0 )*(1.0-px))*interMarkerDistances[i];
			        }
			        computeNegLogLikeTetradData(px,tempm);
			        
			        fpx=negativeLogLikelihood;
			    }else {
			        pa=px;
			        px=py;
			        fpx=fpy;
			        py=pb-(1-r)*(pb-pa);
			        for(int i=0;i<numberOfIntervals;i++){
			            normalizedIntermarkerDistances[i]=2.0*(py+(tempm + 1.0 )*(1.0-py))*interMarkerDistances[i];}
			        computeNegLogLikeTetradData(py,tempm);
			        fpy=negativeLogLikelihood;
			    }
			}
			Bestp=(pa+pb)/2;
			if(Bestp < 0.03) {
			    for(int i=0;i<numberOfIntervals;i++){
			        normalizedIntermarkerDistances[i]=2.0*(Bestp+(tempm+1.0)*(1.0-Bestp))*interMarkerDistances[i];}
			    computeNegLogLikeTetradData(Bestp,tempm);
			    fp=negativeLogLikelihood;
			    for(int i=0;i<numberOfIntervals;i++){
			        normalizedIntermarkerDistances[i]=2.0*(tempm+1.0)*interMarkerDistances[i];}
			    computeNegLogLikeTetradData(0,tempm);
			    f0=negativeLogLikelihood;
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
			mUnderAltModel=0;
			pvalueUnderAltModel=0.0;
			if(!doSims) {
			    done=false;
			    current = mUnderAltModel;
			}
			double tempp;
			normalizedIntermarkerDistances = new double[numberOfIntervals];
			for(int i=0;i<numberOfIntervals;i++){
			    normalizedIntermarkerDistances[i]=2.0*interMarkerDistances[i];}
			
			// For pvalueUnderAltModel and mUnderAltModel values, find negative log likelihood under the alt model 
			computeNegLogLikeTetradData(pvalueUnderAltModel, mUnderAltModel);
			minNegLogLikelihood = negativeLogLikelihood;
			// For values of interference parameter going from 1 through 20
			// get p value from GoldenSection algorithm
			// Get normalized values of intermarker distances
			// for each value of m, find neg log likelihood of tetrad data
			// find the p and m values with lowest log likelihood
			for(int m=1;m<=20;m++) {
			    if(!doSims)
			        current = m;
			    tempp=goldSectionp(m);
			    for(int counter=0;counter<numberOfIntervals;counter++)
			        normalizedIntermarkerDistances[counter]=2.0*(tempp+(1-tempp)*(m+1))*interMarkerDistances[counter];
			    computeNegLogLikeTetradData(tempp,m);
			    if(negativeLogLikelihood < minNegLogLikelihood) {
			        pvalueUnderAltModel=tempp;
			        mUnderAltModel=m;
			        minNegLogLikelihood = negativeLogLikelihood;
			    } else {
			        break;
			    }
			}
			altModelMinNegLogLikelihood = minNegLogLikelihood;
			if(!doSims){
			   current =20;
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
			return done;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }
    // make a copy of original tetrad data before overwriting tetrdata during simulations
    private void copyTetradData() {
    	
        try {
			copiedTetradData = new int[numberOfTetrads][numberOfIntervals];
			for(int rows=0;rows<numberOfTetrads;rows++) {
			    for(int cols=0;cols<numberOfIntervals;cols++){
			        copiedTetradData[rows][cols] = tetradData[rows][cols];}
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
			return current;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return current;
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
			doSims = true;
			current = 0;
			double[] ActualDistances = new double[numberOfIntervals+1];
			interMarkerDistancesInActualData = new double[numberOfIntervals];
			double Rate, ProbI;
			int LeftMarker;
			int counter=0;
			numberOfTetrads = sampleSize;
			if(nullModel) {
			    simulatedMValuesUnderNullModel = new Integer[NumberOfSimulations];
			    simulatedNullMinNegLogLikeValues = new Double[NumberOfSimulations];
			}
			if(altModel) {
			    simulatedMValuesUnderAltModel = new Integer[NumberOfSimulations];
			    simulatedPValuesUnderAltModel = new Double[NumberOfSimulations];
			    simulatedMinNegLogLikeValuesUnderAltModel = new Double[NumberOfSimulations];
			}
			if(nullModel && altModel){
			    simulatedLikelihoodValues = new Double[NumberOfSimulations];
			}
			tetradData = new int[numberOfTetrads][numberOfIntervals];
			// it needs intermarker distances
			// nullm, fixedm, fixedp
			for(int i=0;i<numberOfIntervals;i++){
			    interMarkerDistancesInActualData[i] = interMarkerDistances[i];}
			for(int i=0;i<numberOfIntervals+1;i++){
			    ActualDistances[i] = 0.0;}
			for(int i =1;i<numberOfIntervals+1;i++){
			    ActualDistances[i] = interMarkerDistancesInActualData[i-1] + ActualDistances[i-1];}
			
			Rate = 2.0 *(Fixedp + (Fixedm + 1.0)*(1.0 - Fixedp));
			ProbI = 2.0 * Fixedp / Rate;
			int[] CrossoversBetweenMarkers =  new int[numberOfIntervals];
			int NCoBeforeCxII;
			double Total, NextEvent,MyRand;
			MyRand = Math.random();
			int i,j;
			for(int Sim=0;Sim < NumberOfSimulations; Sim++) {
			    current = Sim;
			    for(i=0;i<numberOfTetrads;i++) {
			        for(j=0;j<numberOfIntervals;j++){
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
			        
			        while(Total < ActualDistances[numberOfIntervals]) 
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
			        
			        for(j=0;j<numberOfIntervals;j++) {
			            if(CrossoversBetweenMarkers[j]==0)
			                tetradData[i][j] = 0;
			            else {
			                MyRand = Math.random();
			                if(MyRand  < (1.0/3.0)*(0.5 + Math.pow(-0.5, CrossoversBetweenMarkers[j])))
			                    tetradData[i][j] = 0;
			                else if(MyRand < (2.0/3.0)*(0.5 + Math.pow(-0.5, CrossoversBetweenMarkers[j])))
			                    tetradData[i][j] = 2;
			                else
			                    tetradData[i][j] = 1;
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
			        simulatedMValuesUnderNullModel[Sim] = mUnderNullModel;
			        simulatedNullMinNegLogLikeValues[Sim] = nullModelMinNegLogLikelihood;
			     }
			    if(altModel){
			        findMaxLikelihoodEstimateSAlt();
			        simulatedMValuesUnderAltModel[Sim]=mUnderAltModel;
			        simulatedPValuesUnderAltModel[Sim]=pvalueUnderAltModel;
			        simulatedMinNegLogLikeValuesUnderAltModel[Sim] = altModelMinNegLogLikelihood;
			     }
			    if(nullModel && altModel)
			        simulatedLikelihoodValues[Sim] = 2.0*(simulatedNullMinNegLogLikeValues[Sim] - simulatedMinNegLogLikeValuesUnderAltModel[Sim]);
			} // for all simulations
			
			current = NumberOfSimulations;
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
			normalizedIntermarkerDistances = new double[numberOfIntervals];
			for(int i=0;i<numberOfIntervals;i++){
			    normalizedIntermarkerDistances[i]=2.0*(m+1)*interMarkerDistances[i];}
			if(!doSims) {
			    done=false;
			    current = m;
			}
			computeNegLogLikeTetradData(p,m);
			minNegLogLikelihood = negativeLogLikelihood;
			for( m=1;m<=20;m++) {
			    if(!doSims)
			        current = m;
			    for(int i=0;i<numberOfIntervals;i++){
			        normalizedIntermarkerDistances[i]=2.0*(m+1)*interMarkerDistances[i];}
			    computeNegLogLikeTetradData(p,m);
			    if(negativeLogLikelihood < minNegLogLikelihood) {
			        mUnderNullModel = m;
			        minNegLogLikelihood = negativeLogLikelihood;
			    } else {
			        break;
			    }
			}        
      nullModelMinNegLogLikelihood  = minNegLogLikelihood;
			if(!doSims){
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
     * @param tempm
     */
    private void computeNegLogLikeTetradData(double prob, int tempm) {
        
        try {
			negativeLogLikelihood = 0;
			identitymatrix = new double[1][tempm+1];
			identityTranspose = new double[tempm+1][1];
			runningMatrix = new double[tempm+1][tempm+1];
			matrixSum = new double[tempm+1][tempm+1];
			Temp1 = new double[tempm+1][1];
			int test=0;
			double probability=0;
			int k=1;
			
			for(int i=0;i<tempm+1;i++) {
			    identitymatrix[0][i]=1.0;
			    identityTranspose[i][0]=1.0;
			}
			
			for(int i=0;i<numberOfTetrads;i++) {
			    if(i==0) {
			        test=1;
			    } else {
			        test=0;
			        for(int j=0;j<numberOfIntervals;j++){
			            test = test + (tetradData[i][j]-tetradData[i-1][j])*
			            		(tetradData[i][j]-tetradData[i-1][j]);
			            }
			    }
			    
			    if(test > 0) {
			        // create an identity matrix
			        for(int tmp1=0;tmp1<tempm+1;tmp1++) {
			            for(int tmp2=0;tmp2<tempm+1;tmp2++) {
			                if(tmp1==tmp2)
			                    runningMatrix[tmp1][tmp2]=1.00;
			                else
			                    runningMatrix[tmp1][tmp2]=0.00;
			            }
			        }
			        
			        for(int cols=0;cols<numberOfIntervals;cols++) {
			            double y = normalizedIntermarkerDistances[cols];
			            // if it is a tetratype
			            if(tetradData[i][cols]==1) {
			                dMatrix=null;
			                dMatrixFormation(prob,1,tempm,y);
			                for(int counter=0;counter<tempm+1;counter++) {
			                    for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                        matrixSum[counter][innercounter]=dMatrix[counter][innercounter];
			                    }
			                }
			                for(k=2;k<=5;k++) {
			                    dMatrix=null;
			                    dMatrixFormation(prob, k,tempm,y);
			                    for(int counter=0;counter<tempm+1;counter++) {
			                        for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                            matrixSum[counter][innercounter]+=(2.00/3.00*(1-Math.pow((-0.5),k)))*(dMatrix[counter][innercounter]);
			                        }
			                    }
			                }// for all K
			            } // completes the TetraType if loop
			            else // if it is PD or NPD, add the 1/3 formula first
			                // then add Do if it is PD
			            {
			                for(int counter=0;counter<tempm+1;counter++) {
			                    for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                        matrixSum[counter][innercounter]=0.0;
			                    }
			                }
			                for(k=2;k<=5;k++) {
			                    dMatrix=null;
			                    
			                    dMatrixFormation(prob, k,tempm,y);
			                    for(int counter=0;counter<tempm+1;counter++) {
			                        for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                            matrixSum[counter][innercounter]+=(1.00/3.00)*((1.00/2.00) +Math.pow((-0.5),k))*(dMatrix[counter][innercounter]);
			                        }
			                    }
			                }
			                
			                // check
			                if(tetradData[i][cols]==0) {
			                    dMatrix=null;                 
			                    dMatrixFormation(prob, 0,tempm,y);
			                    for(int counter=0;counter<tempm+1;counter++) {
			                        for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                            matrixSum[counter][innercounter]+=(dMatrix[counter][innercounter]);
			                        }
			                    }
			                    
			                }
			            }// end of else
			            
			            matrixProduct=null;
			            findMatrixProduct(runningMatrix, matrixSum);
			            
			            for(int counter=0;counter<tempm+1;counter++) {
			                for(int innercounter=0;innercounter<tempm+1;innercounter++) {
			                    runningMatrix[counter][innercounter] = matrixProduct[counter][innercounter];
			                }
			            }
			            
			        } // finishing loop over all columns
			        
			        findMatrixProduct(runningMatrix,identityTranspose);            
			        for(int counter=0;counter<tempm+1;counter++){
			            Temp1[counter][0] = matrixProduct[counter][0];}
			        
			        matrixProduct=null;
			        // then take with one
			        findMatrixProduct(identitymatrix, Temp1);                
			        probability = matrixProduct[0][0]/(tempm+1);                
			    }// if test > 0
			    
			    negativeLogLikelihood = negativeLogLikelihood - Math.log(probability);
			}// for each row
			identitymatrix=null;
			identityTranspose=null;
			runningMatrix = null;
			matrixProduct=null;
			Temp1 = null;
			matrixSum=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // dMatrix Formation used in computing Neg Log Likelihood
    private void dMatrixFormation(double prob, int tempk, int mval, double normDistance) {
        try {
			dMatrix = new double[mval+1][mval+1];
			if(prob==0) {
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            int n = innercounter - counter + tempk + mval*tempk;
			            if((tempk == 0) &&(innercounter < counter))
			                dMatrix[counter][innercounter]=0.00;
			            else if((normDistance==0) &&(n==0))
			                dMatrix[counter][innercounter]=1.00;
			            else {
			                dMatrix[counter][innercounter]=( (Math.exp(-normDistance) * Math.pow(normDistance,n)))/computefactorial(n);                        
			            }                    
			        }
			    }
			} else {            
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            dMatrix[counter][innercounter]=0.0;
			        }
			    }
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            for(int innercounter2=0;innercounter2<tempk+1;innercounter2++) {
			                int n=innercounter-counter+tempk+mval*(tempk-innercounter2);
			                if((innercounter2==tempk) && (innercounter < counter))
			                    dMatrix[counter][innercounter]+=0.00;
			                else if( (normDistance==0.00) &&(n==0))
			                    dMatrix[counter][innercounter]=1.00;
			                else
			                    dMatrix[counter][innercounter]=dMatrix[counter][innercounter]+
			                            (
			                            (Math.exp(-normDistance)* Math.pow(normDistance,n))
			                            /
			                            (computefactorial(n-innercounter2)*computefactorial(innercounter2))
			                            ) *
			                            (
			                            Math.pow(prob/(prob+ ((mval+1)*(1.0-prob)) ), innercounter2)
			                            ) *
			                            ( Math.pow( ((1-prob)*(mval+1))/(prob+(mval+1)*(1.0-prob)),n-innercounter2)
			                            );
			            }
			        }
			    }
			}
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
    // Find Matrix Product
    /**
     * @param Matrix1
     * @param Matrix2
     * Performs multiplication of two matrices
     */
    private void findMatrixProduct(double[][] Matrix1, double[][] Matrix2) {
        try {
            
            matrixProduct = new double[Matrix1.length][Matrix2[0].length];
            for(int counter1=0;counter1<Matrix1.length;counter1++) {
                for(int counter2=0;counter2<Matrix2[0].length;counter2++) {
                    matrixProduct[counter1][counter2] = 0.0;
                    for(int innercounter=0;innercounter<Matrix1[0].length;innercounter++) {
                        matrixProduct[counter1][counter2]+=
                                Matrix1[counter1][innercounter]*Matrix2[innercounter][counter2];
                    }
                }
            }
            
        }catch(Exception e) {
            System.out.println("Illegal operation in Matrix Multiplication ");
        }
    }
    /**
     * This method finds the intermarker distances using the raw tetrad data
     */
    public void findInterMarkerDistances() {
        try {
			interMarkerDistances = new double[numberOfIntervals];
			for(int i=0;i<numberOfIntervals;i++) {
			    interMarkerDistances[i]=0.0;
			    for(int a=0;a<numberOfTetrads;a++) {
			        if(tetradData[a][i]==2)
			            interMarkerDistances[i] +=3.0;
			        else
			            interMarkerDistances[i] += (tetradData[a][i])/(2.00);
			    }
			    interMarkerDistances[i] = (interMarkerDistances[i])/(numberOfTetrads);
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
			tetradData = new int[numberOfTetrads][numberOfIntervals];
			for(int x=2;x<rowsInRawData;x+=4) {
			    for(int xy=0;xy<columnsInRawData-1;xy++) {
			        
			        int sum = Math.abs(((Integer)rawData[x][xy]).intValue()-((Integer)rawData[x][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+1][xy]).intValue()-((Integer)rawData[x+1][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+2][xy]).intValue()-((Integer)rawData[x+2][xy+1]).intValue()) +
			                Math.abs(((Integer)rawData[x+3][xy]).intValue()-((Integer)rawData[x+3][xy+1]).intValue());
			        sum = sum / 2;
			        tetradData[Tetrad][xy]=sum;
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
			numberOfMarkers = rawData[0].length+1;
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
			numberOfIntervals = rawData[0].length;
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
			numberOfTetrads = rawData.length;
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
			rawData=new Object[noLinesInFile][];
			Iterator<String> i = dataInFile.iterator();
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
			numberOfTetrads = rowsInRawData;
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
			tetradData = new int[numberOfTetrads][numberOfIntervals];
			for(int x=0;x<rowsInRawData;x++) {
			    for(int xy=0;xy<columnsInRawData;xy++) {
			        tetradData[Tetrad][xy]=Integer.parseInt(rawData[x][xy]+"");
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
			String[] tempTetradData = new String[numberOfTetrads];
			for(int i=0;i<numberOfTetrads;i++) {
			        String one = concatenate(tetradData[i]);
			        tempTetradData[i] = one;
			}
			// Sort in ascending order 
			Arrays.sort(tempTetradData);
			for(int i=0;i<numberOfTetrads;i++) {
			    for(int j=0;j<numberOfMarkers-1;j++) {
			        tetradData[i][j]= Integer.parseInt(tempTetradData[i].substring(j,j+1));                
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
            for(int i=0;i<numberOfTetrads;i++) {
                for(int j=0;j<numberOfIntervals;j++) {
                    
                    out.write(tetradData[i][j]+"\t");
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
            for(int i=0;i<numberOfIntervals;i++)
                System.out.print(interMarkerDistances[i]+"\t");
            File f = new File(filename);
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<numberOfIntervals;i++)
                out.write(interMarkerDistances[i]+"\t");
            fw.close();
        }catch(IOException io) {
            System.out.println("Error Trapping IO Exception\nClass:writeIntermarkerdistances\n" + io.toString());
        }
    }
    
    private void printTetradData() {
        try {
			for(int i=0;i<numberOfTetrads;i++) {
			    for(int j=0;j<numberOfIntervals;j++) {
			        System.out.print(tetradData[i][j]+"\t");
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
			numberOfMarkers=columnsInRawData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void findNumberOfIntervals() {
        try {
			numberOfIntervals = numberOfMarkers-1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void findNumberOfTetrads() {
        try {
			numberOfTetrads=(numberOfTetrads-2)/4;
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
        rawData=new Object[noLinesInFile][];
        Iterator<String> i = dataInFile.iterator();
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
        numberOfTetrads = rowsInRawData;
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
            BufferedReader in = new BufferedReader(new FileReader(fname));            
            String str="";
            while((str=in.readLine())!=null) {                
                dataInFile.add(str);
            }
            in.close();
            noLinesInFile=dataInFile.size();    
        }catch (IOException ioexception) {
            System.out.println("IOException " + ioexception + "in Interference.java!");
        }
            
    }
    
    /**
     * prints results of simulations
     */
    public void printSimulationResults() {
        try {
			for(int x=0;x<numberOfSimulations;x++) {
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
			return numberOfTetrads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberOfTetrads;
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
  
    private Vector<String> dataInFile;
    
    private int noLinesInFile;
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
    private boolean doNullModel, doAltModel;
    private double[][] identitymatrix; // from VB Code
    private double[][] identityTranspose; // from VB Code
    private double[][] matrixProduct; // from VB Code
    private double[][] runningMatrix;
    private double[][] matrixSum;
    private double[][] dMatrix;
    
    private double[][] Temp1;
    
    private int numberOfSimulations; // from VB Code 
    private int copiedTetradData[][];
    
    private Integer simulatedMValuesUnderNullModel[];
    private Double simulatedNullMinNegLogLikeValues[];
    private Double simulatedMinNegLogLikeValuesUnderAltModel[];
    private Integer simulatedMValuesUnderAltModel[];
    private Double simulatedPValuesUnderAltModel[];
    private Double simulatedLikelihoodValues[];
    private Double likelihoodRatio; 
    private String fname="";
    private boolean  doSims=false;
    private int current;
    private boolean done=false;
}


