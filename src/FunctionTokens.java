import java.util.ArrayList;
import java.util.List;

public class FunctionTokens {
	Token t;
	List<Token> l;
	
	
	public FunctionTokens(Token t){
		this.t = t;
	}
	
	
	
	public Token getT() {
		return t;
	}
	public List<Token> getL() {
		return l;
	}
	
	public void setL(List<Token> l) {
		this.l = l;
	}
	
	
}
