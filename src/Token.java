/**
 * The token class defines the different types of tokens.
 * @author Ankit
 *
 */
public class Token {
	
	/**
	 * The name of the token
	 */
	String name;
	/**
	 * The type of the token for example: IDENTIFIER< META_STATEMENT, RESERVED_WORD etc...
	 */
	Tokentype type;
	
	/**
	 * Constructor to initialize the token
	 * @param type
	 */
	public Token(Tokentype type) {
		this.type = type;
	}
	
	/**
	 * Setter for the name property
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the name property
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for the token-type property
	 * @return
	 */
	public Tokentype getTokenType() {
		return type;
	}
	
	/**
	 * Setter for the token-type property.
	 * @param type
	 */
	public void setTokenType(Tokentype type) {
		this.type = type;
	}
}
