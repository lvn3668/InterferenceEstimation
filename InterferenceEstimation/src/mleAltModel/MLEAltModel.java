package mleAltModel;

import InterferenceEstimation.TetradData;

public class MLEAltModel {

	private int mUnderAltModel; // from VB Code
	private double pvalueUnderAltModel; // from VB Code
	private Double altModelMinNegLogLikelihood;
	private static double negativeLogLikelihood; // from VB Code
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
	public void findMaximumLikelihoodEstimatesAlternateModel(TetradData tetraddata) {
	    try {
			setmUnderAltModel(0);
			setPvalueUnderAltModel(0.0);
			if(!tetraddata.isPerformSimulations()) {
			    tetraddata.setDone(false);
			    tetraddata.setCurrent(getmUnderAltModel());
			}
			double tempp;
			tetraddata.setNormalizedIntermarkerDistances(new double[tetraddata.getNumberOfIntervals()]);
			for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			    tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*tetraddata.getInterMarkerDistances()[i];}
			
			// For pvalueUnderAltModel and mUnderAltModel values, find negative log likelihood under the alt model 
			tetraddata.computeNegativeLogLikelihoodTetradData(getPvalueUnderAltModel(), getmUnderAltModel());
			tetraddata.setMinNegLogLikelihood(getNegativeLogLikelihood());
			// For values of interference parameter going from 1 through 20
			// get p value from GoldenSection algorithm
			// Get normalized values of intermarker distances
			// for each value of m, find neg log likelihood of tetrad data
			// find the p and m values with lowest log likelihood
			for(int m=1;m<=20;m++) {
			    if(!tetraddata.isPerformSimulations())
			        tetraddata.setCurrent(m);
			    tempp=goldSectionp(tetraddata, m);
			    for(int counter=0;counter<tetraddata.getNumberOfIntervals();counter++)
			        tetraddata.getNormalizedIntermarkerDistances()[counter]=2.0*(tempp+(1-tempp)*(m+1))*tetraddata.getInterMarkerDistances()[counter];
			    tetraddata.computeNegativeLogLikelihoodTetradData(tempp,m);
			    if(getNegativeLogLikelihood() < tetraddata.getMinNegLogLikelihood()) {
			        setPvalueUnderAltModel(tempp);
			        setmUnderAltModel(m);
			        tetraddata.setMinNegLogLikelihood(getNegativeLogLikelihood());
			    } else {
			        break;
			    }
			}
			setAltModelMinNegLogLikelihood(tetraddata.getMinNegLogLikelihood());
			if(!tetraddata.isPerformSimulations()){
			   tetraddata.setCurrent(20);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param mUnderNullModel the mUnderNullModel to set
	 */
	private void setmUnderAltModel(int mUnderAltModel) {
		this.mUnderAltModel = mUnderAltModel;
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
		this.pvalueUnderAltModel = pvalueUnderAltModel;
	}
	/**
	 * @return the mUnderAltModel
	 */
	private int getmUnderAltModel() {
		return this.mUnderAltModel;
	}
	/**
	 * @param altModelMinNegLogLikelihood the altModelMinNegLogLikelihood to set
	 */
	private void setAltModelMinNegLogLikelihood(Double altModelMinNegLogLikelihood) {
		this.altModelMinNegLogLikelihood = altModelMinNegLogLikelihood;
	}
	/**
	 * @return the negativeLogLikelihood
	 */
	public static double getNegativeLogLikelihood() {
		return negativeLogLikelihood;
	}
	/**
	 * 
	 * @param tempm
	 * @return
	 */
	//GoldSection for Alternate Model
	private static double goldSectionp(TetradData tetraddata, int tempm) {
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
			for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			    tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(px+(tempm+1.0)*(1-px))*tetraddata.getInterMarkerDistances()[i];}
			tetraddata.computeNegativeLogLikelihoodTetradData(px,tempm);
			fpx=getNegativeLogLikelihood();
			py=pb-(1-r)*(pb-pa);
			for(int i=0;i<tetraddata.getNumberOfIntervals();i++) {
			    tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(py+(tempm+1.0)*(1.0-py))*tetraddata.getInterMarkerDistances()[i];
			}
			tetraddata.computeNegativeLogLikelihoodTetradData(py,tempm);
			fpy=getNegativeLogLikelihood();
			
			for(int j=1;j<=8;j++) {
			    if(fpx<fpy) {
			        pb=py;
			        fpy=fpx;
			        py=px;
			        px=pa+((1-r)*(pb-pa));
			        for(int i=0;i<tetraddata.getNumberOfIntervals();i++) {
			            tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(px+( tempm + 1.0 )*(1.0-px))*tetraddata.getInterMarkerDistances()[i];
			        }
			        tetraddata.computeNegativeLogLikelihoodTetradData(px,tempm);	        
			        fpx=getNegativeLogLikelihood();
			    }else {
			        pa=px;
			        px=py;
			        fpx=fpy;
			        py=pb-(1-r)*(pb-pa);
			        for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			            tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(py+(tempm + 1.0 )*(1.0-py))*tetraddata.getInterMarkerDistances()[i];}
			        tetraddata.computeNegativeLogLikelihoodTetradData(py,tempm);
			        fpy=getNegativeLogLikelihood();
			    }
			}
			Bestp=(pa+pb)/2;
			if(Bestp < 0.03) {
			    for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			        tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(Bestp+(tempm+1.0)*(1.0-Bestp))*tetraddata.getInterMarkerDistances()[i];}
			    tetraddata.computeNegativeLogLikelihoodTetradData(Bestp,tempm);
			    fp=getNegativeLogLikelihood();
			    for(int i=0;i<tetraddata.getNumberOfIntervals();i++){
			        tetraddata.getNormalizedIntermarkerDistances()[i]=2.0*(tempm+1.0)*tetraddata.getInterMarkerDistances()[i];}
			    tetraddata.computeNegativeLogLikelihoodTetradData(0,tempm);
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

}
