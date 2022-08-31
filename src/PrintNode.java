import java.util.ArrayList;
/**
 * In this class we store list of nodes
 * @author Srujan Vepuri
 *
 */
public class PrintNode extends StatementNode{

	private ArrayList<Node> list;
	public PrintNode(ArrayList<Node> l) {
		list=l;
	}
	/**
	 * We get node of certain Index
	 * @param index
	 * @return a node 
	 */
	public Node getNode(int index) {

		try {
			return list.get(index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e+": "+index+" is out of bound");
			//e.printStackTrace();
		}
		return null;
	}
	/*
	 * We just return string
	 */
	public String getNodeToString(int index) {
		try {
			return list.get(index).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e+": "+index+" is out of range");
		}
		return null;
	}
	public ArrayList<Node> getList(){
		return list;
	}
	
	@Override
	public String toString() {
		// if the size 0 or not define yet than we know our list is empty
		if(list.size()==0||list==null)
			return "It look like an empty list";
		String s="From PrintNode and The List Is \n";
		for(Node n: list) {
			if(n==null)// if current node is empty
				s+="This node is empty";
			else
				s+=n.toString()+"\n";
		}s+="End of PrintNodeList. ";
		return s;
	}

}
