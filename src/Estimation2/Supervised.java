package Estimation2;

public class Supervised {

	//試行回数
	int numTrial;
	//aの回数
	int ca[][];
	//bの回数
	int cb[][];

	double a[][];
	double b[][];

	public Supervised(int numTrial, int numStates, int numOutputs){
		this.numTrial = numTrial;
		ca = new int[numStates][numStates];
		cb = new int[numStates][numOutputs];
		a = new double[numStates][numStates];
		b = new double[numStates][numOutputs];
	}

	public void countA(int[] observedStates){
		int s1 = observedStates[0];

		for(int i = 1; i < observedStates.length; i++){
			int s = observedStates[i];
			ca[s1][s]++;
			s1 = s;
		}
	}
	public void countB(int[] observedStates, double[] observedOutputs){
		for(int i = 0; i < observedOutputs.length; i++){
			int s = observedStates[i];
			double o = observedOutputs[i];
			//cb[s][o]++;
		}
	}
	public void count(int[] observedStates, double[] observedOutputs){
		countA(observedStates);
		countB(observedStates, observedOutputs);
	}

	private void estimateA(){
		int ni;
		for(int i = 0; i < a.length; i++){
			ni = 0;
			for(int j = 0; j < a[i].length; j++){
				ni += ca[i][j];
			}
			for(int j = 0; j < a[i].length; j++){
				a[i][j] = ca[i][j]/(double)ni;
			}
		}
	}
	private void estimateB(){
		int ni;
		for(int i = 0; i < b.length; i++){
			ni = 0;
			for(int j = 0; j < b[i].length; j++){
				ni += cb[i][j];
			}
			for(int j = 0; j < b[i].length; j++){
				b[i][j] = cb[i][j]/(double)ni;
				//SSystem.out.println("b:"+b[i][j]+", cb:"+cb[i][j]+", ni:"+ni+", cb/n:"+cb[i][j]/ni);
			}
		}
	}
	public void estimate(){
		estimateA();
		estimateB();
	}

	public double[][] getA(){
		return a;
	}
	public double[][] getB(){
		return b;
	}
}






