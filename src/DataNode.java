import java.util.ArrayList;
/**
 * 
 * @author Srujan Vepuri
 *
 */
public class DataNode extends StatementNode {
	private ArrayList<Node> list;// here we node like StringNode, IntegerNode, FloatNode

	public DataNode(ArrayList<Node>l) {
		list=l;
	}
	public Node getNode(int index) {
		return list.get(index);
	}

	public ArrayList<Node> getList(){
		return list;
	}
	/**
	 * In this class we return what type of data We just read 
	 * @param node - only node type is VariableNode
	 * @return - the Node that was found in Data
	 */
	public Node getData(Node node) {
		VariableNode tempVar= (VariableNode)node;// type cast 
		String s= tempVar.getVariable();// we get the String Variable 
		if(s.contains("$")) {// if $ has this we know that this type String Variable 
			for(Node n:list) {
				if(n.getClass().equals(StringNode.class)) {
					System.out.println("This from DataNode Class and Variable from ReadNode "+s+" = (DataNode)"+n.toString());
					return n;
				}		
			}
			throw new IllegalArgumentException("There was no StringNode in DATA list, So your READ is wrong");
		}
		if(s.contains("%")) {// if % it is than we know it type Float 
			for(Node n:list) {
				if(n.getClass().equals(FloatNode.class)) {
					System.out.println("This from DataNode Class and Variable from ReadNode "+s+" = (DataNode)"+n.toString());
					return n;
				}
					
			}
			throw new IllegalArgumentException("There was no FloatNode in DATA list, So your READ is worng");
			
		}
		if(s.matches("[a-zA-Z]+")) {// if it only has letters than we it type is Integer 
			for(Node n: list) {
				if(n.getClass().equals(IntegerNode.class)) {
					System.out.println("This from DataNode Class and Variable from ReadNode "+s+" = (DataNode)"+n.toString());
					return n;
				}
					
			}
			throw new IllegalArgumentException("There Was no IntegerNode in DATA list, So your READ is wrong");
		}
		System.out.println("Waning: Read is not DATA Type");// a little Waring Message 
		return null;
	}

	@Override
	public String toString() {
		String s="DataNode (";
		for(Node n:list)
			s+=n.toString()+", ";
		s=s.substring(0, s.length()-2);
		s+=") End Of DataNodeList\n ";
		return s;
	}

}
