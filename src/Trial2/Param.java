package Trial2;
// output = continuous
public class Param {
	public static int numTrial = 240;
	public static int numStates = 3;
	public static int numParam = 2;
	public static double pi[] = {1.0, 0, 0};
	public static double a[][] = {
			{0.7, 0.2, 0.1},
			{0.1, 0.7, 0.2},
			{0.2, 0.1, 0.7}
	};	//Transition probabilities
	//Output probabilities
	//component is mean & variance of Gaussian
	public static double b[][] ={
			{0, 1.2}, 
			{3, 0.9}, 
			{-3, 1}
			};
}
