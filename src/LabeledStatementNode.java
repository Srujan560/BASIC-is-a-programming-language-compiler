
public class LabeledStatementNode extends StatementNode{
	private Node statement;
	private String label; 
	public StatementNode nextStatement;
	public LabeledStatementNode(Node n, String s) {
		statement=n;
		label =s;
		//label= label.substring(0, label.length()-1);
	}
	public Node getStatement() {
		return statement;
	}
	public String getLabel() {
		return label;
	}
	@Override
	public String toString() {
		if(statement==null)
			return "LabeledNodeStatementNode("+label+", no statement was give)";
		return "LabeledStatementNode("+label+","+ statement.toString()+") ";
	}

}
