import java.util.ArrayList;
/**
 * In this class we add Inputs 
 * @author Srujan Vepuri
 *
 */
public class InputNode extends StatementNode {
	private ArrayList<Node> list;
	public InputNode(ArrayList<Node> list) {
		this.list=list;
	}
	public Node getNode(int index) {
		return list.get(index);
	}
	
	public ArrayList<Node> getList(){
		return list;
	}
	@Override
	public String toString() {
		String s ="InputNode (";
		for(Node n: list)
			s+=n.toString();
		s=s.substring(0,s.length()-1);
		s+=") End of InputNodeList\n";
		return  s;
	}

}
