import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ThreeAddressCode {
	
	List<String> localVariables = new ArrayList<String>();
	Stack<String> valueStack = new Stack<String>();
	Stack<Token> operatorStack = new Stack<Token>();
	private List<Token> inputArray = new ArrayList<Token>();
	private int varCount;
	private Map<String, Integer> varMap;
	private Map<String, Integer> funcCalls;
	private Map<String, Integer> globalVars;
	
	public ThreeAddressCode(List<Token> list, List<String> varList, Map<String, Integer> varMap, Map<String, Integer> funcCalls, Map<String, Integer> globalVars) {
		this.inputArray = list;
		this.initializeExpression();
		this.varCount = varList.size();
		this.varMap = varMap;
		this.funcCalls = funcCalls;
		this.globalVars = globalVars;
		iterateString();
	}
	
	/**
	 * Remove enclosing parenthesis, if any. Also handle unary operations
	 */
	private void initializeExpression() {
//		if(this.inputArray.get(0).getName().equals("(") && this.inputArray.get(this.inputArray.size()-1).getName().equals("(")) {
//			this.inputArray.remove(0);
//			this.inputArray.remove(this.inputArray.size()-1);
//		}
		boolean unaryFlag = false;
		for(int i = 0; i < this.inputArray.size(); i++){
			if(this.inputArray.get(i).getName().equals("-")) {
				//Unary operator ALERT
				if(i == 0) {
					unaryFlag = true;
					Token t = new Token(Tokentype.SYMBOL);
					t.setName(")");
					this.inputArray.add(i+2, t);
					Token t1 = new Token(Tokentype.NUMBER);
					t1.setName("0");
					this.inputArray.add(i, t1);
					Token t2 = new Token(Tokentype.SYMBOL);
					t2.setName("(");
					this.inputArray.add(i, t2);
					i += 2;
				} else {
					String operator = this.inputArray.get(i-1).getName();
					//Unary operator ALERT
					if(operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")) {
						unaryFlag = true;
						Token t = new Token(Tokentype.SYMBOL);
						t.setName(")");
						this.inputArray.add(i+2, t);
						Token t1 = new Token(Tokentype.NUMBER);
						t1.setName("0");
						this.inputArray.add(i, t1);
						Token t2 = new Token(Tokentype.SYMBOL);
						t2.setName("(");
						this.inputArray.add(i, t2);
						i += 2;
					}
				}
			}
		}
	}
	
	/**
	 * Iterate through the string and get the value of the expression by creating variables and converting all the expressions into 3 address code.
	 */
	private void iterateString() {
		for(int i = 0; i < inputArray.size(); i++) {
			if(i < inputArray.size()-2) {
				evaluateToken(inputArray.get(i), inputArray.get(i+1), inputArray.get(i+2));
			} else {
				evaluateToken(inputArray.get(i), null, null);
			}
		}
		while(!operatorStack.isEmpty()) {
			evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
		}
		for(int i = 0; i < valueStack.size(); i++) {
			//System.out.println(valueStack.pop());
		}
		for(int i = 0; i < localVariables.size(); i++) {
			//System.out.println(localVariables.get(i));
		}
		//System.out.println("wass");
	}

	/**
	 * Take actions as per the current token type.
	 * @param currentToken
	 */
	private void evaluateToken(Token currentToken, Token nextToken, Token nnextToken) {
		String cToken = currentToken.getName();
		if(cToken.equals("+") || cToken.equals("-") || cToken.equals("*") || cToken.equals("/")) {
			while(!operatorStack.isEmpty() && hasPrecedence(cToken.charAt(0),operatorStack.peek().getName().charAt(0))) {
				evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
			}
			operatorStack.push(currentToken);
		} else if(cToken.equals("(") || cToken.equals("[")) {
			operatorStack.push(currentToken);
		} else if(cToken.equals(")") || cToken.equals("]")) {
			while(!operatorStack.isEmpty() && (!operatorStack.peek().getName().equals("(") && !operatorStack.peek().getName().equals("["))) {
				evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
			}
			if(!operatorStack.isEmpty()) {
				operatorStack.pop();
			}
			/**
			 * Create the function call in the expression
			 * If the operator stack has identifier(function name)
			 */
			if(!operatorStack.isEmpty() && operatorStack.peek().getTokenType() == Tokentype.IDENTIFIER) {
				int index = localVariables.size()+varCount;
				if(!valueStack.isEmpty()) {
					if(operatorStack.peek().getName().charAt(operatorStack.peek().getName().length()-1) == '|') {
						String x = operatorStack.peek().getName().substring(0, operatorStack.peek().getName().length()-1);
						localVariables.add(x+"()");
						operatorStack.pop();
					} else {
						//If its for a function call
						if(funcCalls.get(operatorStack.peek().getName()) != null) {
							int pCount = funcCalls.get(operatorStack.peek().getName());
							StringBuilder sb = new StringBuilder();
							for(int l = 0; l < pCount; l++) {
								sb.insert(0, valueStack.pop());
								sb.insert(0, ",");
							}
							if(sb.length() > 0) {
								localVariables.add(operatorStack.pop().getName()+"("+sb.subSequence(1, sb.length()).toString()+")");
							} else {
								localVariables.add(operatorStack.pop().getName()+"()");
							}
						}
						//If its for an array
						if(cToken.equals("]")) {
							if(globalVars.get(operatorStack.peek().getName()) != null) {
								int cnt = globalVars.get(operatorStack.pop().getName());
								if(cnt == 0)
									localVariables.add("GLOBAL"+"["+valueStack.pop()+"]");
								else
									localVariables.add("GLOBAL"+cnt+"["+valueStack.pop()+"]");
							} else {
								localVariables.add(operatorStack.pop().getName()+"["+valueStack.pop()+"]");
							}
							
						}
						//localVariables.add(operatorStack.pop().getName()+"("+valueStack.pop()+")");
					}
				}
				else {
					String x = operatorStack.peek().getName().substring(0, operatorStack.peek().getName().length()-1);
					localVariables.add(x+"()");
					operatorStack.pop();
				}
				valueStack.push("LOCAL["+index+"]");
			}
		} else {
			if(currentToken.getTokenType() == Tokentype.NUMBER) {
				valueStack.push(cToken);
			} else if(currentToken.getTokenType() == Tokentype.IDENTIFIER) {
				/**
				 * If nextToken is a '('. Then this is a function name.
				 */
				if(nextToken != null && (nextToken.getName().equals("(") || nextToken.getName().equals("[")) && varMap.get(cToken) == null) {
					if(nnextToken != null && (nnextToken.getName().equals(")") || nnextToken.getName().equals("]"))) {
						currentToken.setName(currentToken.getName()+"|");
						operatorStack.push(currentToken);
					} else {
						operatorStack.push(currentToken);
					}
					
				} else {
					valueStack.push("LOCAL["+varMap.get(cToken)+"]");
				}
				//valueStack.push(cToken);
			}
		}
//		System.out.println(".........");
//		for(int i = 0; i < valueStack.size(); i++) {
//			System.out.print(valueStack.pop()+" ");
//		}
//		for(int i = 0; i < operatorStack.size(); i++) {
//			System.out.print(operatorStack.pop().getName()+" ");
//		}
	}
	
	/**
	 * Take action as per operator type.
	 * @param operator
	 * @param val2
	 * @param val1
	 */
	private void evaluateExpression(Token operator, String val2, String val1) {
		if(operator.getName().equals("+") || operator.getName().equals("-") || operator.getName().equals("*") ) {
			int index = localVariables.size()+varCount;
			localVariables.add(val1+operator.getName()+val2);
			valueStack.push("LOCAL["+index+"]");
		} else if(operator.getName().equals("/")) {
			if(val2.equals("0")) {
				System.out.println("error");
				System.exit(0);
			} else {
				int index = localVariables.size()+varCount;
				localVariables.add(val1+operator.getName()+val2);
				valueStack.push("LOCAL["+index+"]");
			}
		}
	}
	
	// Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
	public boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if (op2 != '+' && op2 != '-' && op2 != '*' && op2 != '/')
        	return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
}

