import java.util.ArrayList;
import java.util.List;

/**
 * Function tokens are all the tokens in a function.
 * @author Ankit
 *
 */
public class FunctionTokens {
	Token t;
	List<Token> l;
	
	/**
	 * Capture token type in the constructor
	 * @param t
	 */
	public FunctionTokens(Token t){
		this.t = t;
	}
	
	
	/**
	 * Get token
	 * @return
	 */
	public Token getT() {
		return t;
	}
	
	/**
	 * Get the list of expression tokens.
	 * @return
	 */
	public List<Token> getL() {
		return l;
	}
	
	/**
	 * Set the list of expression tokens.
	 * @param l
	 */
	public void setL(List<Token> l) {
		this.l = l;
	}
	
	
}
