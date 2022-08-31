
public class IntegerNode extends Node {
	private int num ;
	public IntegerNode(int n) {
		num=n;
	}
	
	public int readNumber() {
		return num;
	}
	@Override
	 public String toString() {
		 return ""+num;
		 //"IntegerNode("+num+") "
	 }
	

}
