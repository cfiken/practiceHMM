package Trial2;

import java.util.Random;

//one state
public class Output {

	double b[];		//出力確率

	
	public Output(double b[]){
		this.b = new double[Param.numParam];
		for(int i = 0; i < Param.numParam; i++){
			this.b[i] = b[i];
		}
		//確率のセット
	}

	
	//サイコロを投げて出力を取得
	public double getOutput(){
		double output = 0;
		Random random = new Random();
		output = (random.nextGaussian()+b[0])*b[1];
		return output;
	}
}
