public class Token {

	String name;
	Tokentype type;
	
	public Token(Tokentype type) {
		this.type = type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Tokentype getTokenType() {
		return type;
	}

	public void setTokenType(Tokentype type) {
		this.type = type;
	}
}
