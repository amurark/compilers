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
	
	public ThreeAddressCode(List<Token> list, int varCount) {
		this.inputArray = list;
		this.initializeExpression();
		this.varCount = varCount;
		iterateString();
	}
	
	/**
	 * Remove enclosing parenthesis, if any. Also handle unary operations
	 */
	private void initializeExpression() {
		if(this.inputArray.get(0).getName().equals("(") && this.inputArray.get(this.inputArray.size()-1).getName().equals("(")) {
			this.inputArray.remove(0);
			this.inputArray.remove(this.inputArray.size()-1);
		}
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
			evaluateToken(inputArray.get(i));
		}

		while(!operatorStack.isEmpty()) {
			evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
		}
		//System.out.println("=======");
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
	private void evaluateToken(Token currentToken) {
		String cToken = currentToken.getName();
		if(cToken.equals("+") || cToken.equals("-") || cToken.equals("*") || cToken.equals("/")) {
			while(!operatorStack.isEmpty() && hasPrecedence(cToken.charAt(0),operatorStack.peek().getName().charAt(0))) {
				evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
			}
			operatorStack.push(currentToken);
		} else if(cToken.equals("(")) {
			operatorStack.push(currentToken);
		} else if(cToken.equals(")")) {
			while(!operatorStack.peek().getName().equals("(")) {
				evaluateExpression(operatorStack.pop(), valueStack.pop(), valueStack.pop());
			}
			operatorStack.pop();
		} else {
			if(currentToken.getTokenType() == Tokentype.NUMBER) {
				valueStack.push(cToken);
			} else if(currentToken.getTokenType() == Tokentype.IDENTIFIER) {
				valueStack.push(cToken);
			}
		}
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
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
}
