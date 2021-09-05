package mleAltModel;

import InterferenceEstimation.Interference;

public class MLEAltModel {

	private int mUnderAltModel; // from VB Code
	private double pvalueUnderAltModel; // from VB Code
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
			computeNegativeLogLikelihoodTetradData(getPvalueUnderAltModel(), getmUnderAltModel());
			setMinNegLogLikelihood(getNegativeLogLikelihood());
			// For values of interference parameter going from 1 through 20
			// get p value from GoldenSection algorithm
			// Get normalized values of intermarker distances
			// for each value of m, find neg log likelihood of tetrad data
			// find the p and m values with lowest log likelihood
			for(int m=1;m<=20;m++) {
			    if(!isPerformSimulations())
			        setCurrent(m);
			    tempp=goldSectionp(m);
			    for(int counter=0;counter<getNumberOfIntervals();counter++)
			        getNormalizedIntermarkerDistances()[counter]=2.0*(tempp+(1-tempp)*(m+1))*getInterMarkerDistances()[counter];
			    computeNegativeLogLikelihoodTetradData(tempp,m);
			    if(getNegativeLogLikelihood() < getMinNegLogLikelihood()) {
			        setPvalueUnderAltModel(tempp);
			        setmUnderAltModel(m);
			        setMinNegLogLikelihood(getNegativeLogLikelihood());
			    } else {
			        break;
			    }
			}
			setAltModelMinNegLogLikelihood(getMinNegLogLikelihood());
			if(!isPerformSimulations()){
			   setCurrent(20);
			}
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
	 * @return the altModelMinNegLogLikelihood
	 */
	public double getAltModelMinNegLogLikelihood() {
		return this.altModelMinNegLogLikelihood;
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
	// find MLE under Alternate Model
	/**
	 * Finds Maximum Likelihood estimate using alternate model
	 */
	public void findMaximumLikelihoodEstimatesAlternateModel(Interference tetraddata) {
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
			computeNegativeLogLikelihoodTetradData(getPvalueUnderAltModel(), getmUnderAltModel());
			setMinNegLogLikelihood(getNegativeLogLikelihood());
			// For values of interference parameter going from 1 through 20
			// get p value from GoldenSection algorithm
			// Get normalized values of intermarker distances
			// for each value of m, find neg log likelihood of tetrad data
			// find the p and m values with lowest log likelihood
			for(int m=1;m<=20;m++) {
			    if(!isPerformSimulations())
			        setCurrent(m);
			    tempp=goldSectionp(m);
			    for(int counter=0;counter<getNumberOfIntervals();counter++)
			        getNormalizedIntermarkerDistances()[counter]=2.0*(tempp+(1-tempp)*(m+1))*getInterMarkerDistances()[counter];
			    computeNegativeLogLikelihoodTetradData(tempp,m);
			    if(getNegativeLogLikelihood() < getMinNegLogLikelihood()) {
			        setPvalueUnderAltModel(tempp);
			        setmUnderAltModel(m);
			        setMinNegLogLikelihood(getNegativeLogLikelihood());
			    } else {
			        break;
			    }
			}
			setAltModelMinNegLogLikelihood(getMinNegLogLikelihood());
			if(!isPerformSimulations()){
			   setCurrent(20);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
