package Estimation;

import java.util.Arrays;

public class HMM {
	//試行回数
	int numTrial;
	int numStates;
	int numOutputs;

	//bの数
	int cb[][];
	//Parameters
	double pi[];
	double a[][];
	double b[][];
	//States
	int s[];
	//Observation
	int[] obs;
	public HMM(int numTrial, int numStates, int numOutputs, int[] observedOutputs){
		this.numTrial = numTrial;
		this.numStates = numStates;
		this.numOutputs = numOutputs;
		cb = new int[numStates][numOutputs];
		pi = new double[numStates];
		a = new double[numStates][numStates];
		b = new double[numStates][numOutputs];
		s = new int[numTrial];
		obs = new int[numTrial];
		for(int i = 0; i < numTrial; i++){
			obs[i] = observedOutputs[i];
		}
	}
	//Forward Algorithm
	public double[][] getAlpha(double[][] aa, double[][] bb, double[] ppi){
		double pAlpha = 0.0;
		double[][] alpha = new double[numTrial][numStates];
		//Initial of Alpha
		for(int i = 0; i < numStates; i++){
			alpha[0][i] = ppi[i]*bb[i][obs[0]];
		}

		for(int t = 1; t < numTrial; t++){
			for(int j = 0; j < numStates; j++){
				pAlpha = 0.0;
				for(int i = 0; i < numStates; i++){
					pAlpha += alpha[t-1][i]*aa[i][j];
				}
				alpha[t][j] = pAlpha*bb[j][obs[t]];
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
					beta[t][i] += aa[i][j]*bb[j][obs[t+1]]*beta[t+1][j];
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
			px += pi[i]*beta[0][i]*b[i][obs[0]];
		}
		return px;
	}

	//Restoring by Viterbi Algorithm & return restored result
	public int[] viterbi(){
		int ss[][] = new int[numTrial][numStates];
		double phi[][] = new double[numTrial][numStates];
		//First trial
		for(int i = 0; i < numStates; i++){
			phi[0][i] = pi[i]*b[i][obs[0]];
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
				phi[t][j] = phi[t-1][maxPsi]*a[maxPsi][j]*b[j][obs[t]];
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
		double[][] infLog = new double[2001][numOutputs];
		for(int i = 0; i < numOutputs; i++){
			infLog[0][i] = infB[0][i];
		}
		for(int t = 0; t < 1000; t++){
			infA = estimateA(infA, infB, infPi);
			//System.out.println("infA : " + Arrays.deepToString(infA));
			infB = estimateB(infA, infB, infPi);
			for(int i = 0; i < numOutputs; i++){
				infLog[t+1][i] = infB[0][i];
			}
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
					numerator += alpha[tt][i]*infA[i][j]*infB[i][obs[tt+1]]*beta[tt+1][j];
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
		for(int j = 0; j < numStates; j++){
			for(int k = 0; k < numOutputs; k++){
				double numerator = 0.0;
				double denominator = 0.0;
				for(int tt = 0; tt < numTrial-1; tt++){
					if(obs[tt] == k){
						numerator += alpha[tt][j]*beta[tt][j];
					}
					denominator += alpha[tt][j]*beta[tt][j];
				}
				infB[j][k] = numerator/denominator;	//exception
			}
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
			//System.out.println(alpha[0][i] + "*" + beta[0][i] + "/" + denominator);
			infPi[i] = alpha[0][i]*beta[0][i]/denominator;	//exception
			//System.out.println("=" + infPi[i]);
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
