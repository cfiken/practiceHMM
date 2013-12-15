package Trial;

//Diceの管理
import java.util.ArrayList;

public class States {
	public int now; 		//state now
	public int numStates;	//Number of states
	public int numOutputs;	//Number of outputs
	public double pi[];		//Initial state probabilities
	public double a[][];	//Transition probabilities
	public double b[][];	//Output probabilities
	
	public ArrayList<Dice> stateList = new ArrayList<Dice>();
	
	public States(int numStates, int numOutputs){
		this.numStates = numStates;
		this.numOutputs = numOutputs;
		pi = new double[numStates];
		a = new double[numStates][numStates];
		b = new double[numStates][numOutputs];
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
			for(int j = 0; j < b[i].length; j++){
				this.b[i][j] = b[i][j];
			}
		}
	}
	public void setState(double[] pi){
		for(int i = 0; i < numStates; i++){
			stateList.add(new Dice(b[i]));
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
	//��Ԃ�J�ڂ��� now:���݂̏��, a[now]:now����e��Ԃւ̑J�ڊm��
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
	//return 現在のStatesのOutput
	public int getOutput(){
		return stateList.get(now).getOutput();
	}
	//return the state now
	public int getState(){
		return now;
	}
}
