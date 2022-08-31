/**
 * In this class we will get expression symbol expression and Label 
 * @author Srujan Vepuri
 *
 */
public class IfNode extends StatementNode{
	private Node label, booleanStatement;
	
	public IfNode(Node l, Node b) {
		label=l;
		booleanStatement=b;
	}
	public BooleanOperationNode getBooleanStatement() {
		return (BooleanOperationNode)booleanStatement;
	}
	public Node getLabel() {
		return label;
	}
	@Override
	public String toString() {
		return "IfNode("+booleanStatement.toString()+"\n"+label.toString()+")";
	}
}
