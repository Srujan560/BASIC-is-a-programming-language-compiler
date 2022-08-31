import java.util.ArrayList;
/**
 * 
 * @author Srujan Vepuri
 *
 */
public class ReadNode extends StatementNode{
	private ArrayList<Node> list;// this where can store list of variable Node
	public ReadNode(ArrayList<Node>l) {
		list=l;
	}
	public Node getNode(int index) {// get the index  
		return list.get(index);
	}
	public ArrayList<Node> getList(){
		return list;
	}
	
	@Override
	public String toString() {
		String s="ReadNode (";
		for(Node n: list)
			s+=n.toString()+" ";
		s=s.substring(0,s.length()-1);
		s+=") End of ReadNodeList \n";
		return s;
	}

}
