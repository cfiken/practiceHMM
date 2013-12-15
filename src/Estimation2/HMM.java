package Estimation2;

import java.util.Arrays;

public class HMM {
	//試行回数
	int numTrial;
	int numStates;
	int numParam;

	//bの数
	int cb[][];
	//Parameters
	double pi[];
	double a[][];
	double b[][];
	//States
	int s[];
	//Observation
	double[] obs;
	public HMM(int numTrial, int numStates, int numParam, double[] observedOutputs){
		this.numTrial = numTrial;
		this.numStates = numStates;
		this.numParam = numParam;
		//cb = new int[numStates][numParam];
		pi = new double[numStates];
		a = new double[numStates][numStates];
		b = new double[numStates][numParam];
		s = new int[numTrial];
		obs = new double[numTrial];
		for(int i = 0; i < numTrial; i++){
			obs[i] = observedOutputs[i];
		}
	}
	//get Probability of output for continuous
	public double getB(double output, int state, double[][] bb){
		double pb = 0;
		pb = 1/Math.sqrt(2*Math.PI*bb[state][1]) * Math.exp(-1/(2*bb[state][1])*Math.pow((output-bb[state][0]), 2));
		return pb;
	}
	
	//Forward Algorithm
	public double[][] getAlpha(double[][] aa, double[][] bb, double[] ppi){
		double pAlpha = 0.0;
		double[][] alpha = new double[numTrial][numStates];
		//Initial of Alpha
		for(int i = 0; i < numStates; i++){
			alpha[0][i] = ppi[i]*getB(obs[0], i, bb);
		}

		for(int t = 1; t < numTrial; t++){
			for(int j = 0; j < numStates; j++){
				pAlpha = 0.0;
				for(int i = 0; i < numStates; i++){
					pAlpha += alpha[t-1][i]*aa[i][j];
				}
				alpha[t][j] = pAlpha*getB(obs[t], j, bb);
			}
		}
		return alpha;
	}
	public double forward(){
		double px = 0.0;
		double[][] alpha = new double[numTrial][numStates];
		alpha = getAlpha(a, b, pi);
		for(int i = 0; i < numStates; i++){
			px += alpha[numTrial-1][i];
		}
		return px;
	}
	//Backward Algorithm
	public double[][] getBeta(double[][] aa, double[][] bb){
		double[][] beta = new double[numTrial][numStates];
		//Initial of Beta
		for(int i = 0; i < numStates; i++){
			beta[numTrial-1][i] = 1;
		}
		for(int t = numTrial-2; t >= 0; t--){
			for(int i = 0; i < numStates; i++){
				beta[t][i] = 0;
				for(int j = 0; j < numStates; j++){
					beta[t][i] += aa[i][j]*getB(obs[t+1], j, bb)*beta[t+1][j];
				}
			}
		}
		return beta;
	}
	public double backward(){
		double px = 0.0;
		double[][] beta = new double[numTrial][numStates];
		//Initial of Beta
		beta = getBeta(a, b);
		for(int i = 0; i < numStates; i++){
			px += pi[i]*beta[0][i]*getB(obs[0], i, b);
		}
		return px;
	}

	//Restoring by Viterbi Algorithm & return restored result
	public int[] viterbi(){
		int ss[][] = new int[numTrial][numStates];
		double phi[][] = new double[numTrial][numStates];
		//First trial
		for(int i = 0; i < numStates; i++){
			phi[0][i] = pi[i]*getB(obs[0], i, b);
			ss[0][i] = 0;
		}
		//recursive calculation
		int maxPsi = 0;
		for(int t = 1; t < numTrial; t++){
			for(int j = 0; j < numStates; j++){
				//get (t-1) index of PsiMax & value of PhiMax
				maxPsi = 0;
				for(int i = 1; i < numStates; i++){
					if((phi[t-1][maxPsi]*a[maxPsi][j]) < (phi[t-1][i]*a[i][j])){
						maxPsi = i;
					}
				}
				phi[t][j] = phi[t-1][maxPsi]*a[maxPsi][j]*getB(obs[t], j, b);
				ss[t][j] = maxPsi;
			}
		}
		//Last Trial
		int lastPsi = 0;
		for(int i = 1; i < numStates; i++){
			if(phi[numTrial-1][lastPsi] < phi[numTrial-1][i]){
				lastPsi = i;
			}
		}
		//Restore
		s[numTrial-1] = lastPsi;
		for(int t = 0; t < numTrial-1; t++){
			s[numTrial-t-2] = ss[numTrial-t-1][s[numTrial-t-1]];
		}
		return s;
	}
	
	//Baum-Welch Algorithm
	public double[][] baumWelch(double infA[][], double infB[][], double infPi[]){
		double[][] infLog = new double[2001][numParam];
		//for(int i = 0; i < numParam; i++){
		//	infLog[0][i] = infB[0][i];
		//}
		for(int t = 0; t < 1000; t++){
			infA = estimateA(infA, infB, infPi);
			//System.out.println("infA : " + Arrays.deepToString(infA));
			infB = estimateB(infA, infB, infPi);
			infPi = estimatePi(infA, infB, infPi);
		}
		System.out.println();
		System.out.println("infA : " + Arrays.deepToString(infA));
		System.out.println("infB : " + Arrays.deepToString(infB));
		System.out.println("infPi: " + Arrays.toString(infPi));
		return infLog;
	}
	private double[][] estimateA(double infA[][], double infB[][], double infPi[]){
		double[][] alpha = getAlpha(infA, infB, infPi);
		double[][] beta = getBeta(infA, infB);
		for(int i = 0; i < numStates; i++){
			for(int j = 0; j < numStates; j++){
				double numerator = 0.0;
				double denominator = 0.0;
				for(int tt = 0; tt < numTrial-2; tt++){
					numerator += alpha[tt][i]*infA[i][j]*getB(obs[tt+1], j, infB)*beta[tt+1][j];
					denominator += alpha[tt][i]*beta[tt][i];
					//System.out.println(alpha[tt][i]+"*"+beta[tt][i] + "=" + alpha[tt][i]*beta[tt][i]);
				}
				infA[i][j] = numerator/denominator;	//exception
			}
		}
		return infA;
	}
	private double[][] estimateB(double infA[][], double infB[][], double infPi[]){
		double[][] alpha = getAlpha(infA, infB, infPi);
		double[][] beta = getBeta(infA, infB);
		for(int i = 0; i < numStates; i++){
			double numerator = 0.0;
			double denominator = 0.0;
			for(int tt = 0; tt < numTrial; tt++){
				denominator += alpha[tt][i]*beta[tt][i];
				numerator += alpha[tt][i]*beta[tt][i]*obs[tt];
			}
			infB[i][0] = numerator/denominator;
			numerator = 0.0;
			for(int tt = 0; tt < numTrial; tt++){
				numerator += alpha[tt][i]*beta[tt][i]*Math.pow((obs[tt]-infB[i][0]), 2);
			}
			infB[i][1] = numerator/denominator;
		}
		return infB;
	}
	private double[] estimatePi(double infA[][], double infB[][], double infPi[]){
		double[][] alpha = getAlpha(infA, infB, infPi);
		double[][] beta = getBeta(infA, infB);
		for(int i = 0; i < numStates; i++){
			double denominator = 0.0;
			for(int j = 0; j < numStates; j++){
				denominator += alpha[numTrial-1][j];
			}
			infPi[i] = alpha[0][i]*beta[0][i]/denominator;	//exception
		}
		return infPi;
	}

	

	//Setter, Getter
	public void setPi(double[] pi){
		for(int i = 0; i < pi.length; i++){
			this.pi[i] = pi[i];
		}
	}
	public void setA(double[][] a){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				this.a[i][j] = a[i][j];
			}
		}
	}
	public void setB(double[][] b){
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[i].length; j++){
				this.b[i][j] = b[i][j];
			}
		}
	}
	public double[][] getA(){
		return a;
	}
	public double[][] getB(){
		return b;
	}
}
