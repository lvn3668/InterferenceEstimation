package mleNullModel;

import InterferenceEstimation.TetradData;
import matrixUtilities.MatrixUtilities;

public class MLENullModel {

	private Double likelihoodRatio;
	private int mUnderNullModel; // from VB Code
	private double negativeLogLikelihood; // from VB Code
	private MatrixUtilities MatrixUtils;
	private double nullModelMinNegLogLikelihood;

	/**
	 * @return the matrixUtils
	 */
	private MatrixUtilities getMatrixUtils() {
		return this.MatrixUtils;
	}

	/**
	 * @param matrixUtils the matrixUtils to set
	 */
	private void setMatrixUtils(MatrixUtilities matrixUtils) {
		try {
			this.MatrixUtils = matrixUtils;
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

	public void computeNegativeLogLikelihoodSingleSporeData(TetradData tetraddata, double prob, int tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted) 
	{
	    try {
	    	
			setNegativeLogLikelihood(0);
			getMatrixUtils().setIdentityMatrix(new double[1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setIdentityMatrixTranspose(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			getMatrixUtils().setRunningMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setMatrixSum(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1]);
			getMatrixUtils().setTempMatrix(new double[tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1][1]);
			int test=0;
			double probability=0;
			int knumberofdoublestranddnabreaks=1;
			
			for(int i=0;i<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;i++) {
			    getMatrixUtils().getIdentityMatrix()[0][i]=1.0;
			    getMatrixUtils().getIdentityMatrixTranspose()[i][0]=1.0;
			}
			// Number of single spores = number of cols of intermarker distances minus one 
			for(int tetradcounter=0;tetradcounter<tetraddata.getNumberOfTetrads();tetradcounter++) {
			    if(tetradcounter==0) {
			        test=1;
			    } else {
			        test=0;
			        for(int j=0;j<tetraddata.getNumberOfIntervals();j++){
			            test = test + (tetraddata.getTetradData()[tetradcounter][j]-tetraddata.getTetradData()[tetradcounter-1][j])*
			            		(tetraddata.getTetradData()[tetradcounter][j]-tetraddata.getTetradData()[tetradcounter-1][j]);
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
			        
			        for(int cols=0;cols<tetraddata.getNumberOfIntervals();cols++) {
			            double y = tetraddata.getNormalizedIntermarkerDistances()[cols];
			            // if it is a tetratype
			            // compute tetrad type based on intermarker distances 
			            // for each tetrad type (non parental ditype, tetratype, parental ditype)
			            // compute probability based on Mathers formula (assumption: Non Chromatid TetradData)
			            // Use a running matrix to compute posterior prob of a tetrad pattern
			            // MLE computed using simplex method fof find optimal value of m alone 
			            // Under null model
			            if(tetraddata.getTetradData()[tetradcounter][cols]==0) {
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
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=(1.00/2.00)*getMatrixUtils().getdMatrix()[counter][innercounter];
			                        }
			                    }
			                }// for all K
			                
	
			                // check
			                if(tetraddata.getTetradData()[tetradcounter][cols]==0) {
			                    getMatrixUtils().setdMatrix(null);                 
			                    getMatrixUtils().dMatrixFormation(prob, 0,tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted,y);
			                    for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                        for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=getMatrixUtils().getdMatrix()[counter][innercounter];
			                        }
			                    }
			                    
			                }
			            } 
			            else 
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
			                            getMatrixUtils().getMatrixSum()[counter][innercounter]+=(1.00/2.00)*getMatrixUtils().getdMatrix()[counter][innercounter];
			                        }
			                    }
			                }
			                
			                
			            }// end of else
			            
			            getMatrixUtils().setMatrixProduct(null);
			            getMatrixUtils().findMatrixProduct(getMatrixUtils().getRunningMatrix(), 
			            		getMatrixUtils().getMatrixSum());
			            
			            for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++) {
			                for(int innercounter=0;innercounter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;innercounter++) {
			                    getMatrixUtils().getRunningMatrix()[counter][innercounter] = getMatrixUtils().getMatrixProduct()[counter][innercounter];
			                }
			            }
			            
			        } // finishing loop over all columns
			        
			        getMatrixUtils().findMatrixProduct(getMatrixUtils().getRunningMatrix(), getMatrixUtils().getIdentityMatrixTranspose());            
			        for(int counter=0;counter<tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1;counter++){
			            getMatrixUtils().getTempMatrix()[counter][0] = getMatrixUtils().getMatrixProduct()[counter][0];
			            }
			        
			        getMatrixUtils().setMatrixProduct(null);
			        // then take with one
			        getMatrixUtils().findMatrixProduct(getMatrixUtils().getIdentityMatrix(), getMatrixUtils().getTempMatrix());                
			        probability = getMatrixUtils().getMatrixProduct()[0][0]/(tempvalueofm_numberofdsdnabreaksafterhwichasuccessfulcrossisexecuted+1);                
			    }// if test > 0	    
			    setNegativeLogLikelihood(getNegativeLogLikelihood() - Math.log(probability));
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

	// find MLE under Null Model
	/**
	 * This method finds the maximum likelihood estimate 
	 * under the counting model or the null model
	 */
	public void findMaximumLikelihoodEstimatesNullModel(TetradData tetraddata) {
	    try {
			int m=0;        
			double p=0;
			tetraddata.setNormalizedIntermarkerDistances(new double[tetraddata.getNumberOfIntervals()]);
			for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			    tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(m+1)*tetraddata.getInterMarkerDistances()[i];}
			if(!tetraddata.isPerformSimulations()) {
			    tetraddata.setDone(false);
			    tetraddata.setCurrent(m);
			}
			tetraddata.computeNegativeLogLikelihoodTetradData(p,m);
			tetraddata.setMinNegLogLikelihood(getNegativeLogLikelihood());
			for( m=1;m<=20;m++) {
			    if(!tetraddata.isPerformSimulations())
			        tetraddata.setCurrent(m);
			    for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			        tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(m+1)*tetraddata.getInterMarkerDistances()[i];}
			    tetraddata.computeNegativeLogLikelihoodTetradData(p,m);
			    if(getNegativeLogLikelihood() < tetraddata.getMinNegLogLikelihood()) {
			        setmUnderNullModel(m);
			        tetraddata.setMinNegLogLikelihood(getNegativeLogLikelihood());
			    } else {
			        break;
			    }
			}        
			setNullModelMinNegLogLikelihood(tetraddata.getMinNegLogLikelihood());
			if(!tetraddata.isPerformSimulations()){
			   tetraddata.setCurrent(20);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the mUnderNullModel
	 */
	public int getmUnderNullModel() {
		return this.mUnderNullModel;
	}

	/**
	 * @param mUnderNullModel the mUnderNullModel to set
	 */
	private void setmUnderNullModel(int mUnderNullModel) {
		this.mUnderNullModel = mUnderNullModel;
	}

	/**
	 * @return the negativeLogLikelihood
	 */
	public double getNegativeLogLikelihood() {
		return this.negativeLogLikelihood;
	}

	/**
	 * @param negativeLogLikelihood the negativeLogLikelihood to set
	 */
	public void setNegativeLogLikelihood(double negativeLogLikelihood) {
		this.negativeLogLikelihood = negativeLogLikelihood;
	}

	/**
	 * Sets the interference parameter under the null model
	 * @param number Integer value of interference parameter under null model
	 */
	public void setNullM(int number)
	{
	    try {
			this.mUnderNullModel = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * returns null log likelihood
	 * @return double
	 */
	public double getNullLogLike() {
	    try {
			return this.nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.nullModelMinNegLogLikelihood;
	}

	/**
	 * returns loglikelihood under null model
	 * @return double 
	 */
	public double getNullModelMinNegLogLike() {
	    try {
			return this.nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.nullModelMinNegLogLikelihood;
	    
	}

	/**
	 * @return the nullModelMinNegLogLikelihood
	 */
	public double getNullModelMinNegLogLikelihood() {
		return this.nullModelMinNegLogLikelihood;
	}

	/**
	 * Sets minimum log likelihood value under null model
	 * @param number double specifying the log likelihood value under null model
	 */
	public void setNullModelMinNegLogLike(double number) {
	    try {
			this.nullModelMinNegLogLikelihood = number;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param nullModelMinNegLogLikelihood the nullModelMinNegLogLikelihood to set
	 */
	public void setNullModelMinNegLogLikelihood(double nullModelMinNegLogLikelihood) {
		try {
			this.nullModelMinNegLogLikelihood = nullModelMinNegLogLikelihood;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
