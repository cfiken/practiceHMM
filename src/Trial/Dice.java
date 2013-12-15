package Trial;

//サイコロ(状態)1個分
public class Dice {

	double b[];		//各目の出力確率
	
	public Dice(double[] b){
		this.b = new double[b.length];
		//確率のセット
		for(int i = 0; i < b.length; i++){
			this.b[i] = b[i];
		}
	}
	
	//サイコロを投げて出力を取得
	public int getOutput(){
		int output = 0;
		double p = Math.random();
		for(int i = 0; i < b.length; i++){
			if(b[i] > p){
				output = i;
				break;
			} else {
				p = p - b[i];
			}
		}
		return output;
	}
}
