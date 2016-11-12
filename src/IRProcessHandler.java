import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class IRProcessHandler {
	List<Token> outputTokens = new ArrayList<Token>();
	List<FunctionTokens> functionTokenList;
	
	List<String> paramList;
	List<String> lVarList;
	int paramCount;
	int localVarCount;
	String funcName;
	
	int globalCount = 0;
	Map<String, Integer> globalVars = new HashMap<String, Integer>();
	
	int labelCount = 1;
	
	public IRProcessHandler() {}
	public void addToOutputList(Token currentToken) {
		if(currentToken.getName().equals("["))
		{
			Token lt = outputTokens.get(outputTokens.size()-1);
			if(this.globalCount == 0) {
				lt.setName("GLOBAL");
			} else {
				lt.setName("GLOBAL"+this.globalCount);
			}
			globalVars.put(lt.getName(), globalCount);
			this.globalCount++;
			
		}
		this.outputTokens.add(currentToken);
	}
	public Integer getCurrentIndex() {
		return outputTokens.size()-1;
	}
	
//	public void handleExpressions(List<Token> expressionValue) {
//		ThreeAddressCode tac = new ThreeAddressCode(expressionValue,0);
//		Token t = new Token(Tokentype.IDENTIFIER);
//		t.setName(tac.valueStack.pop());
//		outputTokens.add(t);
//		//System.out.println("=======");
//		for(int i = 0; i < tac.valueStack.size(); i++) {
//			//System.out.println(tac.valueStack.pop());
//		}
//		for(int i = 0; i < tac.localVariables.size(); i++) {
//			//System.out.println(tac.localVariables.get(i));
//		}
//	}
	
	public void functionHandler(List<String> paramList, int paramCount, String funcName) {
		this.paramCount = paramCount;
		this.paramList = paramList;
		this.funcName = funcName;
		this.functionTokenList = new ArrayList<FunctionTokens>();
	}
	
	//Called when a function ends
	public void processFunction(int localVarCount, List<String> lVarList) {
//		System.out.println("\nFunction Name: "+this.funcName);
//		System.out.println("Param Count: "+this.paramCount);
//		System.out.println("Local Variable Count: "+localVarCount);
//		System.out.println("Params are:");
		for(int i = 0; i < paramList.size(); i++) {
			//System.out.print(paramList.get(i));
		}
		//System.out.println("\nLocal Variables are:");
		for(int i = 0; i < lVarList.size(); i++) {
			//System.out.print(lVarList.get(i));
		}
		//System.out.println("\nFunc is: ");
		for(int i = 0; i < functionTokenList.size(); i++) {
			//System.out.print(functionTokenList.get(i).getT().getName());
			if(functionTokenList.get(i).getT().getTokenType() == Tokentype.EXPRESSION) {
				//System.out.print("(expr)");
			}
		}
		//System.out.println("done");
		
		/**
		 * List of variables in the function, including the parameters and also the 3 address expressions.
		 */
		List<String> varList = new ArrayList<String>();
		/**
		 * This is also used for the same reason, in parallel with varList
		 */
		Map<String, Integer> varMap = new HashMap<String, Integer>();
		
		/**
		 * Add all the parameters to the VARLIST
		 */
		for(int i = 0; i < paramList.size(); i++) {
			varList.add(paramList.get(i));
			varMap.put(paramList.get(i), varList.size()-1);
		}
		
		/**
		 * Add all the localVariables(we do have the count(passed as params to this function)).
		 */
		for(int i = 0; i < lVarList.size(); i++) {
			varList.add(lVarList.get(i));
			varMap.put(lVarList.get(i), varList.size()-1);
		}
		/**
		 * Count of total variables including intermediate 3AC expressions
		 */
		int varCount = varList.size();
		//functionTokenList is of type functionToken
		for(int i = 0; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getTokenType() == Tokentype.IDENTIFIER) {
				if(varMap.get(t.getName()) != null) {
					t.setName("LOCAL["+varMap.get(t.getName())+"]");
				}
			} else if(t.getTokenType() == Tokentype.EXPRESSION) {
				List<Token> expTokens = functionTokenList.get(i).getL();
				int count = 0;
				/**
				 * To check if the expression is a single identifier.
				 */
				for(int j = 0; j < expTokens.size(); j++) {
					if(expTokens.get(j).getTokenType() != Tokentype.DELIMITER) {
						count++;
					}
				}
				/**
				 * If the expression is not a single identifier and is actually an arithmetic expression
				 */
				if(count > 1) {
					//System.out.println("qwertt");
					for(int m = 0; m < expTokens.size(); m++) {
						//System.out.print(expTokens.get(m).getName());
					}
					
					Map<String, Integer> funcCalls = evaluateFuncCalls(expTokens);
					ThreeAddressCode tac = new ThreeAddressCode(expTokens, varList, varMap, funcCalls);
					String express = getThreeAddressCodeExpression(tac, varList, varMap);
					if(express != null) {
						functionTokenList.get(i).getT().setName(tac.valueStack.peek());
						functionTokenList.get(i).getT().setTokenType(Tokentype.IDENTIFIER);
						Token et = new Token(Tokentype.IDENTIFIER);
						et.setName(express);
						FunctionTokens eft = new FunctionTokens(et);
						int k = i-1;
						while(k >= 0 && !(functionTokenList.get(k).getT().getTokenType() == Tokentype.DELIMITER && functionTokenList.get(k).getT().getName().equals("\n"))) {
							k -= 1;
						}
						++k;
						//This friggin while is to indent the expression. 
						int k1 = k;
						while(functionTokenList.get(k1).getT().getTokenType() == Tokentype.DELIMITER && functionTokenList.get(k1).getT().getName().equals(" ")) {
							eft.getT().setName(" "+eft.getT().getName());
							k1++;
						}
						
						functionTokenList.add(k, eft);
						i = i+1;
					}
				} else {
					for(int j = 0; j < expTokens.size(); j++) {
						Token t1 = expTokens.get(j);
						if(t1.getTokenType() == Tokentype.IDENTIFIER) {
							if(varMap.get(t1.getName()) != null) {
								t1.setName("LOCAL["+varMap.get(t1.getName())+"]");
							}
						}
						FunctionTokens ft = new FunctionTokens(t1);
						functionTokenList.add(i+1, ft);
					}
				}
			}
		}
		//THIS WAS GIVING DOUBLE CLOSING BRACE FOR FUNCTIONS
//		Token closeBrace = new Token(Tokentype.SYMBOL);
//		closeBrace.setName("}");
//		functionTokenList.add(new FunctionTokens(closeBrace));
		processIfStatements();
		processDataDeclarations(varList.size(), lVarList);
		processBinaryAndDecimalDataDeclarations(varList.size(), lVarList);
		for(int i = 0; i < functionTokenList.size(); i++) {
			if(functionTokenList.get(i).getT().getTokenType() != Tokentype.EXPRESSION) {
				outputTokens.add(functionTokenList.get(i).getT());
			}
		}
		
		System.out.println("\nFunc after conversion is: ");
		
		//System.out.println("*****************");
		for(int i = 0; i < functionTokenList.size(); i++) {
			if(functionTokenList.get(i).getT().getTokenType() != Tokentype.EXPRESSION) {
				//System.out.print(functionTokenList.get(i).getT().getName());
			}
		}
		//System.out.println("=================");
	}
	
	
	private Map<String, Integer> evaluateFuncCalls(List<Token> expTokens) {
//		System.out.println("hello there ");
//		for(int i = 0; i <  expTokens.size(); i++) {
//			System.out.print(expTokens.get(i).getName());
//		}
		System.out.println();
		Map<String, Integer> funcCalls = new HashMap<String, Integer>();
		Stack<Token> tokenStack = new Stack<Token>();
		int commaCount = 0;
		for(int j = 0; j < expTokens.size(); j ++) {
			Token t = expTokens.get(j);
			//System.out.println("why1 "+expTokens.get(j).getName());
			if(t.getTokenType() == Tokentype.IDENTIFIER && j <= expTokens.size()-2 && expTokens.get(j+1).getName().equals("(")) {//CHECK j<=
				funcCalls.put(t.getName(), 0);
				tokenStack.push(t);
				tokenStack.push(expTokens.get(j+1));
				j++;
			} else {
				//System.out.println("why "+expTokens.get(j).getName());
				if(!tokenStack.isEmpty()) {
					if(t.getName().equals(")")) {
						Token t1 = tokenStack.pop();
						if(t1.getName().equals("(")) {
							Token id = tokenStack.pop();
							if(id.getTokenType() == Tokentype.IDENTIFIER) {
								if(funcCalls.get(id.getName()) != null ) {
									funcCalls.put(id.getName(), 0);
								}
								commaCount = 0;
							}
						} else {
							/**
							 * Otherwise get the comma count,
							 */
							while(!t1.getName().equals("(")) {
								if(t1.getName().equals(",")) {
									commaCount++;
								}
								t1 = tokenStack.pop();
							}
							Token id = tokenStack.pop();
							if(id.getTokenType() == Tokentype.IDENTIFIER) {
								if(funcCalls.get(id.getName()) != null ) {
									if(commaCount == 0)
										funcCalls.put(id.getName(), 1);
									else
										funcCalls.put(id.getName(), commaCount+1);
									commaCount = 0;
								}
							}
						}
						
					} else {
						tokenStack.push(t);
					}
				}
			}
		}
		return funcCalls;
	}
	public void displayOutputTokens() {
		for(int i = 0; i < outputTokens.size(); i++) {
			System.out.print(outputTokens.get(i).getName());
		}
	}
	
	/**
	 * Handle declarations for binary and decimal
	 * @param size
	 * @param lVarList2
	 */
	private void processBinaryAndDecimalDataDeclarations(int size, List<String> lVarList2) {
		boolean declFlag = false;
		List<FunctionTokens> ftl = new ArrayList<FunctionTokens>();
		/**
		 * Get the index for the 'binary' keyword and the endIndex where the declaration statement ends.
		 */
		for(int i = 0; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getName().equals("binary") || t.getName().equals("decimal")) {
				declFlag = true;
			}
			if(declFlag) {
				if(t.getName().equals("\n")) {
					declFlag = false;
				}
			} else {
				ftl.add(functionTokenList.get(i));
			}
		}
		functionTokenList = ftl;
	}
	
	private void processDataDeclarations(int varCount, List<String> lVarList) {
		boolean declFlag = false;
		int startIndex = 0, endIndex = 0;
		/**
		 * Get the index for the 'int' keyword and the endIndex where the declaration statement ends.
		 */
		for(int i = 0; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getName().equals("int")) {
				declFlag = true;
				startIndex = i;
			}
			if(declFlag) {
				if(t.getName().equals(";")) {
					endIndex = i;
					declFlag = false;
					break;
				}
			}
		}
		/**
		 * Remove all the variables declared between 'int' and ';'
		 */
		for(int i = startIndex+2; i < endIndex; i++) {
			functionTokenList.remove(startIndex+2);
		}
		
		/**
		 * To remove all declarations other than the first one, which is already modified.
		 */
		for(int i = startIndex+4; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getName().equals("int")) {
				while(!t.getName().equals("\n")) {
					functionTokenList.remove(i);
					t = functionTokenList.get(i).getT();
				}
				functionTokenList.remove(i);
			}
		}
		
		/**
		 * IF: 
		 * There is a declaration in the function.
		 * Add the LOCAL array declaration along with the size.
		 * ELSE:
		 * If there is no declaration in the function, but there are expressions formed later in the functions.
		 * Then declare a 'LOCAL' array.
		 */
		int place = 0;
		if(startIndex != 0) {
			Token l = new Token(Tokentype.IDENTIFIER);
			l.setName("LOCAL["+varCount+"]");
			FunctionTokens ft = new FunctionTokens(l);
			functionTokenList.add(startIndex+2, ft);
			place = 5;
		} else {
			if(varCount > 0) {
				Token l = new Token(Tokentype.IDENTIFIER);
				l.setName("int LOCAL["+varCount+"];\n");
				FunctionTokens ft = new FunctionTokens(l);
				functionTokenList.add(startIndex+2, ft);
				place = 3;
			}
		}
		
		/**
		 * For all the parameters in the function, add initializations. e.g
		 * func abc(int a){
		 * LOCAL[0] = a;
		 * }
		 */
		for(int i = this.paramCount-1; i >= 0; i--) {
			
			addNewLine(startIndex+place, functionTokenList);
			
			addSemicolon(startIndex+place, functionTokenList);
			
			Token l3 = new Token(Tokentype.IDENTIFIER);
			l3.setName(paramList.get(i));
			FunctionTokens ft3 = new FunctionTokens(l3);
			functionTokenList.add(startIndex+place, ft3);
			
			Token l2 = new Token(Tokentype.SYMBOL);
			l2.setName("=");
			FunctionTokens ft2 = new FunctionTokens(l2);
			functionTokenList.add(startIndex+place, ft2);
			
			Token l1 = new Token(Tokentype.IDENTIFIER);
			l1.setName("LOCAL["+i+"]");
			FunctionTokens ft1 = new FunctionTokens(l1);
			functionTokenList.add(startIndex+place, ft1);
		}
	}
	private void processIfStatements() {
		Stack<Integer> endingLabels = new Stack<Integer>();
		Stack<String> openingBlocks = new Stack<String>();
		String startLabel = "", endLabel = "";
		for(int i = 0; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getName().equals("if")) {
				startLabel = "C"+labelCount;
				labelCount++;
				endLabel = "C"+labelCount;
				endingLabels.push(labelCount);
				labelCount++;
				openingBlocks.add("if");
			}
			if(t.getName().equals("while")) {
				int loopLabel = labelCount;
				labelCount++;
				startLabel = "C"+labelCount;
				labelCount++;
				endLabel = "C"+labelCount;
				endingLabels.push(labelCount);
				labelCount++;
				openingBlocks.add("while");
				
				t.setName("if");
				
				addNewLine(i, functionTokenList);
				addSemicolon(i, functionTokenList);
				addColon(i, functionTokenList);
				
				Token l11 = new Token(Tokentype.IDENTIFIER);
				l11.setName("C"+loopLabel);
				FunctionTokens ft11 = new FunctionTokens(l11);
				functionTokenList.add(i,ft11);
				i += 4;
			}
			if(t.getName().equals("{") && openingBlocks.size() > 0) {
				t.setTokenType(Tokentype.RESERVED_WORD);
				t.setName("goto ");
				
				i++;
				
				addSemicolon(i,functionTokenList);
				addColon(i,functionTokenList);
				
				Token l2 = new Token(Tokentype.IDENTIFIER);
				l2.setName(startLabel);
				FunctionTokens ft5 = new FunctionTokens(l2);
				functionTokenList.add(i,ft5);
				
				//For friggin indentation
				for(int j = 0; j < openingBlocks.size()*2; j++) {
					addSpace(i,functionTokenList);
				}
				
				addSpace(i,functionTokenList);
				addNewLine(i,functionTokenList);
				addSemicolon(i,functionTokenList);
				
				Token l1 = new Token(Tokentype.IDENTIFIER);
				l1.setName(endLabel);
				FunctionTokens ft4 = new FunctionTokens(l1);
				functionTokenList.add(i,ft4);
				
				
				Token i1 = new Token(Tokentype.RESERVED_WORD);
				i1.setName("goto ");
				FunctionTokens ft3 = new FunctionTokens(i1);
				functionTokenList.add(i,ft3);
				
				//For friggin indentation
				for(int j = 0; j < openingBlocks.size()*2; j++) {
					addSpace(i,functionTokenList);
				}
				
				
				addNewLine(i,functionTokenList);
				addSemicolon(i,functionTokenList);
				
				Token l = new Token(Tokentype.IDENTIFIER);
				l.setName(startLabel);
				FunctionTokens ft1 = new FunctionTokens(l);
				functionTokenList.add(i,ft1);
				
				i+=10;
				//For friggin indentation
				i+= (openingBlocks.size()*4);
			}
			
			if(t.getName().equals("break") && openingBlocks.size() > 0) {
				String blockType = openingBlocks.peek();
				t.setTokenType(Tokentype.RESERVED_WORD);
				if(blockType.equals("while")) {
					int eLabel = endingLabels.peek();
					t.setName("goto C"+eLabel);
				} else {
					int eLabel = endingLabels.peek();
					int eLabel1 = endingLabels.get(endingLabels.size()-2);
					t.setName("goto C"+eLabel1);
				}
				
			}
			
			if(t.getName().equals("}") && openingBlocks.size() > 0) {
				String blockType = openingBlocks.pop();
				
				t.setTokenType(Tokentype.IDENTIFIER);
				int eLabel = endingLabels.pop();
				t.setName("C"+eLabel);
				
				if(blockType.equals("while")) {
					addNewLine(i, functionTokenList);
					addSemicolon(i, functionTokenList);
					
					Token l1 = new Token(Tokentype.IDENTIFIER);
					l1.setName("C"+(eLabel-2));
					FunctionTokens ft = new FunctionTokens(l1);
					functionTokenList.add(i,ft);
					
					Token i1 = new Token(Tokentype.RESERVED_WORD);
					i1.setName("goto ");
					FunctionTokens ft3 = new FunctionTokens(i1);
					functionTokenList.add(i,ft3);
					i += 4;
				}
				i++;
				addSemicolon(i,functionTokenList);
				addColon(i,functionTokenList);
				i+=1;
			}
		}	
	}
	
	private void addColon(int i, List<FunctionTokens> functionTokenList) {
		Token t = new Token(Tokentype.SYMBOL);
		t.setName(":");
		FunctionTokens ft = new FunctionTokens(t);
		functionTokenList.add(i, ft);
	}
	
	private void addSemicolon(int i, List<FunctionTokens> functionTokenList) {
		Token t = new Token(Tokentype.SYMBOL);
		t.setName(";");
		FunctionTokens ft = new FunctionTokens(t);
		functionTokenList.add(i, ft);
	}
	
	private void addSpace(int i, List<FunctionTokens> functionTokenList) {
		Token t = new Token(Tokentype.DELIMITER);
		t.setName(" ");
		FunctionTokens ft = new FunctionTokens(t);
		functionTokenList.add(i, ft);
	}
	private void addNewLine(int i, List<FunctionTokens> functionTokenList) {
		Token t = new Token(Tokentype.DELIMITER);
		t.setName("\n");
		FunctionTokens ft = new FunctionTokens(t);
		functionTokenList.add(i, ft);
	}
	private String getThreeAddressCodeExpression(ThreeAddressCode tac, List<String> varList, Map<String, Integer> varMap) {
		int count = varList.size();
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < tac.localVariables.size(); i++) {
			x.append("LOCAL["+(i+count)+"] = "+tac.localVariables.get(i)+";\n");
			varList.add(tac.localVariables.get(i));
			varMap.put(tac.localVariables.get(i), varList.size());
		}
		return x.toString();
	}
	public void addToFunctionTokenList(FunctionTokens ft) {
		this.functionTokenList.add(ft);
	}
}