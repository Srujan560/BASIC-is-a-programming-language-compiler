
public class NextNode extends StatementNode{
	private VariableNode var;
	private ForNode ref;
	public StatementNode nextStatement;
	public ForNode getRef() {
		return ref;
	}
	public void setRef(ForNode ref) {
		this.ref = ref;
	}
	public NextNode(VariableNode v) {
		var=v;
	}
	public Node getVar() {
		return var;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "NextNode( "+ var.toString()+")";
	}

}
