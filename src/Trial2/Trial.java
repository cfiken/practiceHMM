package Trial2;

//StatesとOutputsを管理
public class Trial {
	//Number of trial
	int numTrial;
	//Set the initials
	int numStates;	//Number of states
	int numOutputs;	//Number of outputs

	States states;
	int resultS[];
	double resultO[];

	public Trial(int numTrial, int numStates, int numParam){
		this.numTrial = numTrial;
		this.numStates = numStates;
		this.numOutputs = numParam;
		states = new States(numStates, numParam);
		states.setA(Param.a);
		states.setB(Param.b);
		states.setState(Param.pi);
		resultS = new int[numTrial];
		resultO = new double[numTrial];
	}

	public void doTrial(){
		for(int i = 0; i < numTrial; i++){
			resultS[i] = states.getState();
			resultO[i] = states.getOutput();;
			states.tranState();
		}
	}

	public int[] getResultS(){
		return resultS;
	}
	public double[] getResultO(){
		return resultO;
	}

}
