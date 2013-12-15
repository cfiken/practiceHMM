package Trial2;

//Diceの管理
import java.util.ArrayList;

public class States {
	public int now; 		//state now
	public int numStates;	//Number of states
	public int numParam;	//Number of outputs
	public double pi[];		//Initial state probabilities
	public double a[][];	//Transition probabilities
	public double b[][];	//Output probabilities
	
	public ArrayList<Output> stateList = new ArrayList<Output>();
	
	public States(int numStates, int numParam){
		this.numStates = numStates;
		this.numParam = numParam;
		pi = new double[numStates];
		a = new double[numStates][numStates];
		b = new double[numStates][2];
	}
	public void setA(double a[][]){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				this.a[i][j] = a[i][j];
			}
		}
	}
	public void setB(double b[][]){
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < numParam; j++){
				this.b[i][j] = b[i][j];
			}
		}
	}
	public void setState(double[] pi){
		for(int i = 0; i < numStates; i++){
			stateList.add(new Output(b[i]));
		}
		setInitialState(pi);
	}
	//ここからメソッド
	//初期状態
	public void setInitialState(double[] pi){
		double p = Math.random();
		for(int i = 0; i < pi.length; i++){
			if(pi[i] > p){
				now = i;
				break;
			} else {
				p = p - pi[i];
			}
		}
	}
	//transition
	public int tranState(){
		double p = Math.random();
		for(int i = 0; i < a[now].length; i++){
			if(a[now][i] > p){
				now = i;
				break;
			} else {
				p = p - a[now][i];
			}
		}
		return now;
	}
	//return nowStates Output
	public double getOutput(){
		return stateList.get(now).getOutput();
	}
	//return the state now
	public int getState(){
		return now;
	}
}
