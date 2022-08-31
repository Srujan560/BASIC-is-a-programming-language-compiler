import java.util.ArrayList;

/**
 * Was asked to make this class 
 * @author Srujan Vepuri
 *
 */
public class StatementsNode extends StatementNode{
	private ArrayList<Node>list;
	public StatementsNode(ArrayList<Node> l) {
		this.list=l;
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
		// TODO Auto-generated method stub
		
		if(list.size()==0||list.get(0)==null)
			return "It Look Like your List is empty";
		String s = "From StatementsNode Class( \n";
		for(Node n: this.list) {
			s+=n.toString();
		}
		s+=") End of the List";
		return s;
	}
	
}
