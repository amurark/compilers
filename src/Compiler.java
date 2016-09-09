public class Compiler {
	public static void main(String[] args) {
		System.out.println("Compilers Project 1: Lets go!!\n");
		
		Scanner scanner = new Scanner("foo.c");  // Initialize the scanner. 
		scanner.buildReservedWordsMap();
		//scanner.checkReservedWordsMap();
		/**
		 * The grand function.
		 */
		scanner.tokenize();

		
		while(scanner.HasMoreTokens()) { 
	        Token t = scanner.GetNextToken();  
	        if (t.getTokenType() == Tokentype.IDENTIFIER && !t.getName().equals("main")) {
	        	// print token with cs512 attached
	        	System.out.print("cs512"+t.getName());
	        }
	        else {
	        	System.out.print(t.getName());
	        } 
	    }
	}
}
