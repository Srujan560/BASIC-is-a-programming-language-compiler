/**
 * Here we hold an Node and Variable node 
 * @author Srujan Vepuri
 *
 */
public class AssignmentNode extends StatementNode{
	private Node node;//this is going to be an expression 
	private VariableNode variable;// this going name of the variable
	
	public AssignmentNode(Node n, VariableNode v) {
		node= n;
		variable =v;
	}
	
	public Node getNode() {
		return node;
	}
	
	public String getVariable() {
		return variable.getVariable();
	}
	public String toString() {
		return "AssignmentNode("+getVariable()+", "+node.toString()+")";
	}

}
