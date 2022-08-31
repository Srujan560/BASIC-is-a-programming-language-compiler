/**
 * Here we just Save GOSub Node
 * @author Srujan Vepuri
 *
 */
public class GOsubNode extends StatementNode{
	private Node variableNode;
	public GOsubNode(Node n) {
		variableNode= n;
	}
	public Node getVar() {
		return variableNode;
	}
	@Override
	public String toString() {
		return "GosubNode("+variableNode.toString()+") ";
	}
	

}
