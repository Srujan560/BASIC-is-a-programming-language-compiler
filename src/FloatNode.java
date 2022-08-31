
public class FloatNode extends Node{
	private float floatNum;
	
	public FloatNode(float f) {
		floatNum=f;
	}

	public float getFloatNum() {
		return floatNum;
	}
	
	@Override
	public String toString() {
		return""+floatNum+"f";
		// "FloatNode("+floatNum+") "
	}
	

}
