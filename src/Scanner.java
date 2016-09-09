import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Scanner {
	
	private Queue<Token> tokens = new LinkedList<Token>();
	private char[] symbols = {'(', ')', '{', '}', ',', ';', '+', '-', '*', '/', '=', '!', '<', '>', '&', '|'};
	private String[] raw_keywords = {"int", "void", "if", "while", "return", "read", "write", "print", "continue", "break", "binary", "decimal"};
	private Map<Character, List<String>> reserved_words = new HashMap<Character, List<String>>();
	private String inputText = "";
	public Scanner(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			String thisLine;

			while ((thisLine = br.readLine()) != null) {
				inputText += thisLine;
				inputText += "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(inputText);
		System.out.println("**************************************");
	}
	
	public void tokenize() {
		//input text
		//tokens is the list
		
		
		//inputText = "scanf(\"%d\n\", &x)";
		
		int startIndex = 0;
		boolean started = false;
		Token currToken = null;

		for(int i = 0; i < inputText.length(); i++) {
			char currChar = inputText.charAt(i);
			
//			if(currChar == '\n') {
//				System.out.println("Line Break");
//			}

			if(!started) {
				if(currChar == '#'){
					/**
					 * Check if its either the first character of the program or otherwise it has a newLine as previous character.
					 */
					if(i-1 < 0 || i-1 >= 0 && inputText.charAt(i-1) == '\n') {
						startIndex = i;
						currToken = new Token(Tokentype.META_STATEMENT);
						started = true;
					}
				} else if(currChar == '/') {
					if((i+1 < inputText.length()) && inputText.charAt(i+1) == '/') {
						startIndex = i;
						currToken = new Token(Tokentype.META_STATEMENT);
						started = true;
					}
				} else if(currChar == '"') {
					startIndex = i;
					currToken = new Token(Tokentype.STRING);
					started = true;
				} else if(currChar == '_' ) { //Identifier
					startIndex = i;
					currToken = new Token(Tokentype.IDENTIFIER);
					started = true;
				} else if(isLetter(currChar)) { //Reserved_words or Identifier
					/**
					 * Check if its a reserved_word
					 */
					boolean isReservedWord = false;
					String rWord = "";
					if(reserved_words.containsKey(currChar)) {
						List<String> kList = reserved_words.get(currChar);
						for(String word : kList) {
					    	if(inputText.substring(i, i+word.length()).equals(word)) {
					    		//System.out.println("Yo, I found a word, "+word+".");
					    		isReservedWord = true;
					    		rWord = word;
					    		break;
					    	}
					    }
					}
					if(isReservedWord) {
						currToken = new Token(Tokentype.RESERVED_WORD);
						currToken.setName(rWord);
						tokens.add(currToken);
						i += (rWord.length()-1);
					} else { //identifier
						startIndex = i;
						currToken = new Token(Tokentype.IDENTIFIER);
						started = true;
					}
				} else if(isDigit(currChar)) {
					currToken = new Token(Tokentype.NUMBER);
					currToken.setName(Character.toString(currChar));
					tokens.add(currToken);
				} else if(isSymbol(currChar)) {
					currToken = new Token(Tokentype.SYMBOL);
					currToken.setName(Character.toString(currChar));
					tokens.add(currToken);
				} else if(isDelimiter(currChar)) {
					currToken = new Token(Tokentype.DELIMITER);
					currToken.setName(Character.toString(currChar));
					tokens.add(currToken);
				}
			} else {
				if(currToken != null && currToken.getTokenType() == Tokentype.STRING) {
					if(currChar == '"') {
						currToken.setName(inputText.substring(startIndex, i+1));
						tokens.add(currToken);
						currToken = null;
						startIndex = 0;
						started = false;
					}
				} else if(currToken != null && currToken.getTokenType() == Tokentype.META_STATEMENT) {
					if(currChar == '\n') {
						currToken.setName(inputText.substring(startIndex, i));
						tokens.add(currToken);
						currToken = null;
						startIndex = 0;
						started = false;
						currToken = new Token(Tokentype.SYMBOL);
						currToken.setName(Character.toString(currChar));
						tokens.add(currToken);
					}
				} else if(currToken != null && currToken.getTokenType() == Tokentype.IDENTIFIER) {
					if(isLetter(currChar) || isDigit(currChar)) {
						continue;
					} else {
						currToken.setName(inputText.substring(startIndex, i));
						tokens.add(currToken);
						currToken = null;
						startIndex = 0;
						started = false;
						i--;//Because it has reached the next character.
					}
				}
				
			}
			
//			if(isSymbol(currChar)) {
//				System.out.println(currChar);
//			}
			
			
		}
		
//		System.out.println("ouu");
//		System.out.println(tokens.size());
//		while(tokens.peek() != null) {
//			Token t = tokens.poll();
//			System.out.println(t.getTokenType()+" **"+unEscapeString(t.getName())+"**");
//		}
		
	}
	
	public String unEscapeString(String s){
	    StringBuilder sb = new StringBuilder();
	    for (int i=0; i<s.length(); i++)
	        switch (s.charAt(i)){
	            case '\n': sb.append("\\n"); break;
	            case '\t': sb.append("\\t"); break;
	            // ... rest of escape characters
	            default: sb.append(s.charAt(i));
	        }
	    return sb.toString();
	}
	
	
	
	
	
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
	
	public boolean isLetter(char currChar) {
		if(Character.isLetter(currChar))
			return true;
		else
			return false;
	}
	
	public boolean isDelimiter(char currChar) {
		if(currChar == ' ' || currChar == '\t' || currChar == '\n')
			return true;
		else
			return false;
	}
	
	public boolean isDigit(char currChar) {
		if(Character.isDigit(currChar))
			return true;
		else
			return false;
	}
	
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
	
	public boolean HasMoreTokens() {
		if(tokens.peek() != null) {
			return true;
		}
		return false;
	}
	
	public Token GetNextToken() {
		return tokens.poll();
	}
}
