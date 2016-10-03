import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * The scanner class scans the input file, tokenizes it and stores the tokens in a data structure. 
 * It also checks and gets token using methods.
 * @author Ankit
 *
 */
public class Scanner {
	
	/**
	 * Data structure to store all the tokens
	 */
	private Queue<Token> tokens = new LinkedList<Token>();
	/**
	 * List of all symbols
	 */
	private char[] symbols = {'(', ')', '{', '}','[', ']', ',', ';', '+', '-', '*', '/', '=', '!', '<', '>', '&', '|'};
	/**
	 * List of all keywords
	 */
	private String[] raw_keywords = {"int", "void", "if", "while", "return", "read", "write", "print", "continue", "break", "binary", "decimal"};
	/**
	 * Special data_structure to store keywords to compare while tokenizing.
	 */
	private Map<Character, List<String>> reserved_words = new HashMap<Character, List<String>>();
	/**
	 * Variable declared to read the input file.
	 */
	private String inputText = "";
	/**
	 * Constructor is used to initialize the inputText String variable with the input file text.
	 * @param filename
	 */
	public Scanner(String filename) {
		/**
		 * Creating a bufferedReader to read the input file.
		 */
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			/**
			 * Variable to read a line at a time
			 */
			String thisLine;
			
			while ((thisLine = br.readLine()) != null) {
				/**
				 * Concatenate each line along with a newLine character to the inputText variable.
				 */
				inputText += thisLine;
				inputText += "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method tokenizes the input program and stores the tokens in a data structure.
	 */
	public int tokenize() {
		/**
		 * Variable to store the start index for a recognized token
		 */
		int startIndex = 0;
		/**
		 * Flag to acknowledge that a token has been identified.
		 */
		boolean started = false;
		/**
		 * Variable to store the current token.
		 */
		Token currToken = null;
		/**
		 * Variable to store the number of new lines to identify an error(if any) and the line number.
		 */
		int count = 0;
		int error = 1;
		/**
		 * Iterate through the input string character by character and tokenize based on the language rules.
		 */
		for(int i = 0; i < inputText.length(); i++) {
			/**
			 * Get the current character
			 */
			char currChar = inputText.charAt(i);
			/**
			 * If its a newline, increase the line counter.
			 */
			if(currChar == '\n') {
				count++;
			}
			
			/**
			 * If a token has not yet been identified.
			 */
			if(!started) {
				/**
				 * If the current character is a '#', then it can be a META_STATEMENT
				 * Provided that either its the first character in the input file or its the first character in a new line.
				 * Check in the else block, if this ends with a new line to ensure the token validity
				 */
				if(currChar == '#'){
					/**
					 * Check if its either the first character of the program or otherwise it has a newLine as previous character.
					 */
					if(i-1 < 0 || i-1 >= 0 && inputText.charAt(i-1) == '\n') {
						/**
						 * If its the start of a META_STATEMENT 
						 * Store the index
						 */
						startIndex = i;
						/**
						 * Initialize a new token object 
						 */
						currToken = new Token(Tokentype.META_STATEMENT);
						/**
						 * Set the started flag.
						 */
						started = true;
					}
				}
				/**
				 * If the character is a forward slash(/), and then the next character is also a forward slash, then
				 * then it is a comment(META_STATEMENT)
				 * Check in the else block, if this ends with a new line to ensure the token validity
				 */
				else if(currChar == '/' && ((i+1 < inputText.length()) && inputText.charAt(i+1) == '/')) {
					/**
					 * Store the start index
					 */
					startIndex = i;
					/**
					 * Initialize a new token
					 */
					currToken = new Token(Tokentype.META_STATEMENT);
					/**
					 * Set the started flag
					 */
					started = true;
				}
				/**
				 * If the current character is a double quotes("), it can be the start of a STRING.
				 */
				else if(currChar == '"') {
					/**
					 * Store the start index
					 */
					startIndex = i;
					/**
					 * Initialize a new token
					 */
					currToken = new Token(Tokentype.STRING);
					/**
					 * Set the started flag
					 */
					started = true;
				}
				/**
				 * If the current character is an underscore(_) or a letter, then it can be an identifier.
				 * If its a letter, it can also be a keyword
				 */
				else if(currChar == '_' || isLetter(currChar)) {
					/**
					 * Store the start index
					 */
					startIndex = i;
					/**
					 * Initialize a new token
					 */
					currToken = new Token(Tokentype.IDENTIFIER);
					/**
					 * Set the started flag
					 */
					started = true;
				}
				/**
				 * If it starts with a digit, the token is a digit
				 */
				else if(isDigit(currChar)) {
					/**
					 * Store the start index
					 */
					startIndex = i;
					/**
					 * Initialize a new token
					 */
					currToken = new Token(Tokentype.NUMBER);
					/**
					 * Set the started flag
					 */
					started = true;
				}
				/**
				 * If it starts with a symbol, its a symbol token
				 */
				else if(isSymbol(currChar)) {
					/**
					 * Initialize a new token.
					 */
					currToken = new Token(Tokentype.SYMBOL);
					/**
					 * Set the name of the token
					 */
					currToken.setName(Character.toString(currChar));
					/**
					 * Add the token to the queue.
					 */
					tokens.add(currToken);
				}
				/**
				 * If it starts with a delimiter, the token is a delimiter
				 */
				else if(isDelimiter(currChar)) {
					/**
					 * Initialize a new token.
					 */
					currToken = new Token(Tokentype.DELIMITER);
					/**
					 * Set the name of the token
					 */
					currToken.setName(Character.toString(currChar));
					/**
					 * Add the token to the queue.
					 */
					tokens.add(currToken);
				}
				/**
				 * If its none of the above, then it does not follow the language specs and the scanner will throw an error.
				 */
				else {
					//System.out.println("The program has some errors in line "+count);
					//System.out.println("Unidentified character '"+currChar+"'");
					System.out.println("Error in Scanner: Invalid token in source file %");
					error = 0;
					break;
				}
			} else {
				/**
				 * If currToken is a String and we encounter the closing quotes, then set the name of the string token.
				 */
				if(currToken != null && currToken.getTokenType() == Tokentype.STRING) {
					if(currChar == '"') {
						currToken.setName(inputText.substring(startIndex, i+1));
						/**
						 * Add the token to the queue.
						 */
						tokens.add(currToken);
						/**
						 * Reinitialize the currToken object to null
						 */
						currToken = null;
						/**
						 * Reinitialize the startIndex to 0.
						 */
						startIndex = 0;
						/**
						 * Reset the started flag to false.
						 */
						started = false;
					}
				}
				/**
				 * If the currToken is a META_STATEMENT and the current charater is a newline character,
				 * Then set the value of the token. 
				 */
				else if(currToken != null && currToken.getTokenType() == Tokentype.META_STATEMENT) {
					if(currChar == '\n') {
						currToken.setName(inputText.substring(startIndex, i));
						/**
						 * Add the token to the queue.
						 */
						tokens.add(currToken);
						/**
						 * Reinitialize the currToken object to null
						 */
						currToken = null;
						/**
						 * Reinitialize the startIndex to 0.
						 */
						startIndex = 0;
						/**
						 * Reset the started flag to false.
						 */
						started = false;
						/**
						 * In this case, add the newLine character as a symbol token to the queue.
						 */
						currToken = new Token(Tokentype.DELIMITER); //check
						currToken.setName(Character.toString(currChar));
						tokens.add(currToken);
					}
				}
				/**
				 * If the current token is an IDENTIFIER
				 */
				else if(currToken != null && currToken.getTokenType() == Tokentype.IDENTIFIER) {
					/**
					 * If the current character satisfies the criteria of an IDENTIFIER, then continue.
					 */
					if(isLetter(currChar) || isDigit(currChar) || currChar == '_') {
						continue;
					} else {
						/**
						 * Get the token value out of the string once the character does not satisfy the criteria of an identifier.
						 */
						String tokenName = inputText.substring(startIndex, i);
						/**
						 * Check if its a reserved_word
						 */
						boolean isReservedWord = false;
						String rWord = "";
						if(reserved_words.containsKey(tokenName.charAt(0))) {
							/**
							 * By iterating through the map of reserved_words, we check if the identifier is in fact a reserved word. 
							 */
							List<String> kList = reserved_words.get(tokenName.charAt(0));
							for(String word : kList) {
						    	if(tokenName.equals(word)) {
						    		isReservedWord = true;
						    		rWord = word;
						    		break;
						    	}
						    }
						}
						/**
						 * If it is a reserved_word then add the RESERVED_WORD token to the queue.
						 */
						if(isReservedWord) {
							currToken.setTokenType(Tokentype.RESERVED_WORD);
							currToken.setName(tokenName);
						}
						/**
						 * Else add it as an IDENTIFIER token to the queue.
						 */
						else {
							currToken.setName(tokenName);
						}
						/**
						 * Add the token to the queue.
						 */
						tokens.add(currToken);
						/**
						 * Reinitialize the currToken object to null
						 */
						currToken = null;
						/**
						 * Reinitialize the startIndex to 0.
						 */
						startIndex = 0;
						/**
						 * Reset the started flag to false.
						 */
						started = false;
						i--;//Because it has reached the next character.
					}
				}
				/**
				 * If the current token is an IDENTIFIER
				 */
				else if(currToken != null && currToken.getTokenType() == Tokentype.NUMBER) {
					/**
					 * If the current character satisfies the criteria of a NUMBER, then continue.
					 */
					if(isDigit(currChar)) {
						continue;
					} else {
						/**
						 * Get the token value out of the string once the character does not satisfy the criteria of a number.
						 */
						String tokenName = inputText.substring(startIndex, i);

						/**
						 * Else add it as a NUMBER token to the queue.
						 */
						currToken.setName(tokenName);
						
						/**
						 * Add the token to the queue.
						 */
						tokens.add(currToken);
						/**
						 * Reinitialize the currToken object to null
						 */
						currToken = null;
						/**
						 * Reinitialize the startIndex to 0.
						 */
						startIndex = 0;
						/**
						 * Reset the started flag to false.
						 */
						started = false;
						i--;//Because it has reached the next character.
					}
				}
				/**
				 * If its none of the above, then it does not follow the language specs and the scanner will throw an error.
				 */
				else {
					//System.out.println("The program has some errors in line "+count);
					//System.out.println("Unidentified character '"+currChar+"'");
					System.out.println("Error in Scanner: Invalid token in source file %");
					error = 0;
				}
			}
		}
		return error;
	}
	
	/**
	 * Method to unescape new line and tab characters.
	 * @param s
	 * @return
	 */
	public String unEscapeString(String inp){
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < inp.length(); i++)
	        switch (inp.charAt(i)){
	            case '\n': sb.append("\\n"); break;
	            case '\t': sb.append("\\t"); break;
	            default: sb.append(inp.charAt(i));
	        }
	    return sb.toString();
	}
	
	
	
	
	/**
	 * Check if the input character is one of the symbols as per the specifications.
	 * @param currChar
	 * @return
	 */
	public boolean isSymbol(char currChar) {
		boolean found = false;
		for(char s : symbols) {
			if(s == currChar) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * Check if the input is a character(A...Z|a...z)
	 * @param currChar
	 * @return
	 */
	public boolean isLetter(char currChar) {
		if(Character.isLetter(currChar))
			return true;
		else
			return false;
	}
	
	/**
	 * Check if the input character is a delimiter like \t, \n or a space.
	 * @param currChar
	 * @return
	 */
	public boolean isDelimiter(char currChar) {
		if(currChar == ' ' || currChar == '\t' || currChar == '\n')
			return true;
		else
			return false;
	}
	
	/**
	 * Check if the input is a digit.
	 * @param currChar
	 * @return
	 */
	public boolean isDigit(char currChar) {
		if(Character.isDigit(currChar))
			return true;
		else
			return false;
	}
	
	/**
	 * Builds a hashmap for the reserved words so that its easier to identify a token as a reserved_word.
	 * Structure {'i':["int"], 'r':["return", "read"]...}
	 */
	public void buildReservedWordsMap() {
		for(String keyword : raw_keywords) {
			if(reserved_words.containsKey(keyword.charAt(0))) {
				List<String> keywordList = reserved_words.get(keyword.charAt(0));
				keywordList.add(keyword);
			} else {
				List<String> keywordList = new ArrayList<String>();
				keywordList.add(keyword);
				reserved_words.put(keyword.charAt(0), keywordList);
			}
		}
	}
	
	/**
	 * Function to check the hashmap for reservedWords by iterating through it.
	 */
	public void checkReservedWordsMap() {
		for (Map.Entry<Character, List<String>> entry : reserved_words.entrySet()) {
			Character key = entry.getKey();
		    List<String> l = entry.getValue();
		    System.out.print(key+ ": ");
		    for(String s : l) {
		    	System.out.print(s+", ");
		    }
		    System.out.println();
		}
	}
	
	/**
	 * Method to check for more tokens in the queue.
	 * @return
	 */
	public boolean HasMoreTokens() {
		if(tokens.peek() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method to get the next token in the queue.
	 * @return
	 */
	public Token GetNextToken() {
		return tokens.poll();
	}
}
