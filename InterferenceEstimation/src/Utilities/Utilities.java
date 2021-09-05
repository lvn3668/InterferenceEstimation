package Utilities;

public class Utilities {

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
	
	public static String concatenate(int[] intArray) {
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

}
