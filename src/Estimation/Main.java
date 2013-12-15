package Estimation;
// output = discrete
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;

import Trial.Param;
import Trial.Trial;

public class Main {

	public static void main(String[] args) {
		
		int numTrial = Param.numTrial;
		int numStates = Param.numStates;
		int numOutputs = Param.numOutputs;
		Trial trial = new Trial(numTrial, numStates, numOutputs);
		trial.doTrial();

		int observedStates[] = new int[numTrial];
		int observedOutputs[] = new int[numTrial];
		for(int i = 0; i < numTrial; i++){
			observedOutputs[i] = trial.getResultO()[i];
			observedStates[i] = trial.getResultS()[i];
		}
		System.out.println("Observed X : " + Arrays.toString(observedOutputs));
		System.out.println("Observed S : " + Arrays.toString(observedStates));

		//Parameter Estimation in Supervised Learning

		/*Supervised ep = new Supervised(numTrial, numStates, numOutputs);
		ep.count(observedStates, observedOutputs);
		ep.estimate();

		for(int i = 0; i < numStates; i++){
			System.out.println("Transition from ["+i+"]");
			System.out.println(Arrays.toString(ep.getA()[i]));
			System.out.println("Outputs from ["+i+"]");
			System.out.println(Arrays.toString(ep.getB()[i]));
		}
		*/

		//Unsupervised Learning
		HMM hmm = new HMM(numTrial, numStates, numOutputs, observedOutputs);
		hmm.setPi(Param.pi);
		hmm.setA(Param.a);
		hmm.setB(Param.b);
		//(1) given parameters, consider p(x)
		//Forward Algorithm
		double pxf = hmm.forward();
		System.out.println("Probability of X by Forward Algorithm  : " + pxf);
		//Backword Algorithm
		double pxb = hmm.backward();
		System.out.println("Probability of X by Backward Algorithm : " + pxb);

		//(2) given parameters, consider si(i=1,..,n)
		int s[] = new int[numTrial];
		s = hmm.viterbi();
		System.out.println("Restored S : " + Arrays.toString(s));
		int precision = 0;
		for(int i = 0; i < numTrial; i++){
			if(s[i] == observedStates[i]){
				precision++;
			}
		}
		System.out.println("Correct Restored Rate : " + (double)precision/numTrial);

		//(3) Estimate unknown parameters
		//initial parameters
		double infA[][] = {
				{0.4, 0.3, 0.3},
				{0.3, 0.4, 0.3},
				{0.3, 0.3, 0.4}
				};
		double infB[][] = {
				{0.5, 0.1, 0.1, 0.1, 0.1, 0.1},
				{1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0, 1.0/6.0},
				{0.1, 0.1, 0.1, 0.1, 0.1, 0.5}
				};
		double infPi[] = {1.0/3.0, 1.0/3.0, 1.0/3.0};
		
		double[][] infLog = hmm.baumWelch(infA, infB, infPi);

		/*	write file
		for(int j = 0; j < numOutputs; j++){
			File file = new File("b0" + j + ".txt");
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				for(int i = 0; i < 2001; i ++){
					pw.println(infLog[i][j]);
				}
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 */
	}


}


