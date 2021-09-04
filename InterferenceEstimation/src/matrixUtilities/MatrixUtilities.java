package matrixUtilities;

public class MatrixUtilities {
    private double[][] dMatrix;
	private double[][] identityMatrix; // from VB Code
	private double[][] identityMatrixTranspose; // from VB Code
	private double[][] matrixProduct; // from VB Code
	private double[][] matrixSum;
	private double[][] runningMatrix;
	private double[][] TempMatrix;
	/**
	 * @return the dMatrix
	 */
	public double[][] getdMatrix() {
		return this.dMatrix;
	}

	/**
	 * @param dMatrix the dMatrix to set
	 */
	public void setdMatrix(double[][] dMatrix) {
		try {
			this.dMatrix = dMatrix;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// dMatrix Formation used in computing Neg Log Likelihood
	public void dMatrixFormation(double prob, int tempk, int mval, double normDistance) {
	    try {
			this.dMatrix = new double[mval+1][mval+1];
			// Each entry in DMatrix =  exp ^ -y (where y = intermarker distance = 2 px and p = m+1) /
			// (intermarkdistance*valueofnum_Crossovers_forwhich_neglgolikelihood is being computed + j - i)
			// 
			if(prob==0) {
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            int n = innercounter - counter + tempk + mval*tempk;
			            if((tempk == 0) &&(innercounter < counter))
			                this.dMatrix[counter][innercounter]=0.00;
			            else if((normDistance==0) &&(n==0))
			                this.dMatrix[counter][innercounter]=1.00;
			            else {
			                this.dMatrix[counter][innercounter]=( (Math.exp(-normDistance) * Math.pow(normDistance,n)))/computefactorial(n);                        
			            }                    
			        }
			    }
			} else {            
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            this.dMatrix[counter][innercounter]=0.0;
			        }
			    }
			    // for a set of markers, compute intermarker distance 
			    //  compute tetrad pattern  across every pair (row wise) between any two intermarker distances (col wise)
			    // for any tetrad pattern, compute probability as
			    // 1/ (temp m) -> assuming interference 
			    // times 
			    // identity and transpose needed to reduce to single number 
			    // between identity and transpose  , matrix of different k values 
			    // where k is number of crossovers going from 2 to 5 
			    // 2 is minimum number of crossovers needed to produce parental ditype (0 or 2) 
			    // for each k , the dmatrix size is temp m byb temp m -> value of interference parameter 
			    // each entry in D(k) matrix is value of seeing the observed tetrad pattern (NPD, PD, TT) 
			    // for given value of k and m (number of crossovers (k) that occurs after (m): unsuccessful ds dna breaks that does not result in a crossover)
			    // TT can occur with minimum 2 crossovers 
			    // PD can occur with minimum of 1 crossover
			    // Each entry in DMatrix is chi square estimation of seeing the observed tetrad pattern
			    // for given value of k and varying values of m  with m going from 0 to <parameter to function>
			    // So each matrix give probability of observing given ttrtrad pattern for a specified value of k and values of m going from 0 to <input parameter>
			    // interference for each value of k
			    // a full mle is performed over all possible values of k and m 
			   
			    for(int counter=0;counter<mval+1;counter++) {
			        for(int innercounter=0;innercounter<mval+1;innercounter++) {
			            for(int innercounter2=0;innercounter2<tempk+1;innercounter2++) {
			                int n=innercounter-counter+tempk+mval*(tempk-innercounter2);
			                if((innercounter2==tempk) && (innercounter < counter))
			                    this.dMatrix[counter][innercounter]+=0.00;
			                else if( (normDistance==0.00) &&(n==0))
			                    this.dMatrix[counter][innercounter]=1.00;
			                else
			                    this.dMatrix[counter][innercounter]=this.dMatrix[counter][innercounter]+
			                            (
			                            		// e^-y * y *(pk+j-i) / (pk+j-i)! * 
			                            		// (p / (p + (m+1)(1-p))^(numberof ds dna breaks for which MLE is computed)  *
			                            		// (p / (p + (m+1)(1-p)^ (k temp) *
			                            		// ((1-p)*(m+1))/(p+(m+1)*(1-p)) ^n - tempk
			                            		// p is intermarker distance 
			                            		// m is number of unsuccessful ds dna breaks after which a successful cross occurs
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

	/**
	 * @return the identityMatrix
	 */
	public double[][] getIdentityMatrix() {
		return this.identityMatrix;
	}

	/**
	 * @param identityMatrix the identityMatrix to set
	 */
	public void setIdentityMatrix(double[][] identityMatrix) {
		try {
			this.identityMatrix = identityMatrix;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the identityMatrixTranspose
	 */
	public double[][] getIdentityMatrixTranspose() {
		return this.identityMatrixTranspose;
	}

	/**
	 * @param identityMatrixTranspose the identityMatrixTranspose to set
	 */
	public void setIdentityMatrixTranspose(double[][] identityMatrixTranspose) {
		try {
			this.identityMatrixTranspose = identityMatrixTranspose;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the matrixProduct
	 */
	public double[][] getMatrixProduct() {
		return this.matrixProduct;
	}

	/**
	 * @param matrixProduct the matrixProduct to set
	 */
	public void setMatrixProduct(double[][] matrixProduct) {
		try {
			this.matrixProduct = matrixProduct;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the matrixSum
	 */
	public double[][] getMatrixSum() {
		return this.matrixSum;
	}

	/**
	 * @param matrixSum the matrixSum to set
	 */
	public void setMatrixSum(double[][] matrixSum) {
		try {
			this.matrixSum = matrixSum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the runningMatrix
	 */
	public double[][] getRunningMatrix() {
		return this.runningMatrix;
	}

	/**
	 * @param runningMatrix the runningMatrix to set
	 */
	public void setRunningMatrix(double[][] runningMatrix) {
		try {
			this.runningMatrix = runningMatrix;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the tempMatrix
	 */
	public double[][] getTempMatrix() {
		return this.TempMatrix;
	}

	/**
	 * @param tempMatrix the tempMatrix to set
	 */
	public void setTempMatrix(double[][] tempMatrix) {
		try {
			this.TempMatrix = tempMatrix;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Find Matrix Product
	/**
	 * @param Matrix1
	 * @param Matrix2
	 * Performs multiplication of two matrices
	 */
	public void findMatrixProduct(double[][] Matrix1, double[][] Matrix2) {
	    try {
	        
	        setMatrixProduct(new double[Matrix1.length][Matrix2[0].length]);
	        for(int counter1=0;counter1<Matrix1.length;counter1++) {
	            for(int counter2=0;counter2<Matrix2[0].length;counter2++) {
	                getMatrixProduct()[counter1][counter2] = 0.0;
	                for(int innercounter=0;innercounter<Matrix1[0].length;innercounter++) {
	                    getMatrixProduct()[counter1][counter2]+=
	                            Matrix1[counter1][innercounter]*Matrix2[innercounter][counter2];
	                }
	            }
	        }
	        
	    }catch(Exception e) {
	        System.out.println("Illegal operation in Matrix Multiplication ");
	    }
	}
	
}
