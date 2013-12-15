package Trial;
// output = discrete
public class Param {
	public static int numTrial = 450;
	public static int numStates = 3;
	public static int numOutputs = 6;
	public static double pi[] = {1.0, 0, 0};
	public static double a[][] = {
			{0.7, 0.2, 0.1},
			{0.1, 0.7, 0.2},
			{0.2, 0.1, 0.7}
	};	//Transition probabilities
	public static double b[][] ={
			{0.75, 0.05, 0.05, 0.05, 0.05, 0.05},
			{0.1, 0.2, 0.2, 0.2, 0.2, 0.1},
			{0.04, 0.04, 0.04, 0.04, 0.04, 0.8}
	};	//Output probabilities

}
