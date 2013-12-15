package Trial;

//StatesとOutputsを管理
public class Trial {
	//Number of trial
	int numTrial;
	//Set the initials
	int numStates;	//Number of states
	int numOutputs;	//Number of outputs

	States states;
	int resultS[];
	int resultO[];

	public Trial(int numTrial, int numStates, int numOutputs){
		this.numTrial = numTrial;
		this.numStates = numStates;
		this.numOutputs = numOutputs;
		states = new States(numStates, numOutputs);
		states.setA(Param.a);
		states.setB(Param.b);
		states.setState(Param.pi);
		resultS = new int[numTrial];
		resultO = new int[numTrial];
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
	public int[] getResultO(){
		return resultO;
	}

}
