
public class StringNode extends Node{
	private String str;
	
	public StringNode(String s) {
		str=s;
	}
	public String getString() {
		return str;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "StringNode("+str+") ";
	}

}
