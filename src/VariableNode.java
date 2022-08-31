/**
 * In this class we store Variable as string
 * @author Srujan Vepuri
 *
 */
public class VariableNode extends Node {
	private String variable;
	public VariableNode(String s) {
		variable=s;
		
	}
	
	public String getVariable() {
		return variable;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "VariableNode("+variable+")";
	}

}
