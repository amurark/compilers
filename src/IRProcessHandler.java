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
	
	int labelCount = 1;
	
	public IRProcessHandler() {}
	public void addToOutputList(Token currentToken) {
		this.outputTokens.add(currentToken);
	}
	public Integer getCurrentIndex() {
		return outputTokens.size()-1;
	}
	
	public void handleExpressions(List<Token> expressionValue) {
		ThreeAddressCode tac = new ThreeAddressCode(expressionValue,0);
		Token t = new Token(Tokentype.IDENTIFIER);
		t.setName(tac.valueStack.pop());
		outputTokens.add(t);
		//System.out.println("=======");
		for(int i = 0; i < tac.valueStack.size(); i++) {
			//System.out.println(tac.valueStack.pop());
		}
		for(int i = 0; i < tac.localVariables.size(); i++) {
			//System.out.println(tac.localVariables.get(i));
		}
	}
	
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
		
		List<String> varList = new ArrayList<String>();
		Map<String, Integer> varMap = new HashMap<String, Integer>();
		
		for(int i = 0; i < paramList.size(); i++) {
			varList.add(paramList.get(i));
			varMap.put(paramList.get(i), varList.size()-1);
		}
		
		for(int i = 0; i < lVarList.size(); i++) {
			varList.add(lVarList.get(i));
			varMap.put(lVarList.get(i), varList.size()-1);
		}
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
					ThreeAddressCode tac = new ThreeAddressCode(expTokens, varList.size());
					String express = getThreeAddressCodeExpression(tac, varList.size());
					if(express != null) {
						functionTokenList.get(i).getT().setName(tac.valueStack.peek());
						functionTokenList.get(i).getT().setTokenType(Tokentype.IDENTIFIER);
						Token et = new Token(Tokentype.IDENTIFIER);
						et.setName(express);
						FunctionTokens eft = new FunctionTokens(et);
						int k = i-1;
						while(!(functionTokenList.get(k).getT().getTokenType() == Tokentype.DELIMITER && functionTokenList.get(k).getT().getName().equals("\n"))) {
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
		Token closeBrace = new Token(Tokentype.SYMBOL);
		closeBrace.setName("}");
		functionTokenList.add(new FunctionTokens(closeBrace));
		processIfStatements();
		System.out.println("\nFunc after conversion is: ");
		for(int i = 0; i < functionTokenList.size(); i++) {
			if(functionTokenList.get(i).getT().getTokenType() != Tokentype.EXPRESSION) {
				System.out.print(functionTokenList.get(i).getT().getName());
			}
		}
		System.out.println("=================");
	}
	
	private void processIfStatements() {
		Stack<String> endingLabels = new Stack<String>();
		int ifFlag = 0;
		String startLabel = "", endLabel = "";
		for(int i = 0; i < functionTokenList.size(); i++) {
			Token t = functionTokenList.get(i).getT();
			if(t.getName().equals("if")) {
				startLabel = "C"+labelCount;
				labelCount++;
				endLabel = "C"+labelCount;
				labelCount++;
				endingLabels.push(endLabel);
				ifFlag++;
			}
			if(t.getName().equals("{") && ifFlag > 0) {
				t.setTokenType(Tokentype.IDENTIFIER);
				t.setName("goto ");
				
				i++;
				
				addSemicolon(i,functionTokenList);
				addColon(i,functionTokenList);
				
				Token l2 = new Token(Tokentype.IDENTIFIER);
				l2.setName(startLabel);
				FunctionTokens ft5 = new FunctionTokens(l2);
				functionTokenList.add(i,ft5);
				
				//For friggin indentation
				for(int j = 0; j < ifFlag*2; j++) {
					addSpace(i,functionTokenList);
				}
				
				addSpace(i,functionTokenList);
				addNewLine(i,functionTokenList);
				addSemicolon(i,functionTokenList);
				
				Token l1 = new Token(Tokentype.IDENTIFIER);
				l1.setName(endLabel);
				FunctionTokens ft4 = new FunctionTokens(l1);
				functionTokenList.add(i,ft4);
				
				
				Token i1 = new Token(Tokentype.IDENTIFIER);
				i1.setName("goto ");
				FunctionTokens ft3 = new FunctionTokens(i1);
				functionTokenList.add(i,ft3);
				
				//For friggin indentation
				for(int j = 0; j < ifFlag*2; j++) {
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
				i+= (ifFlag*4);
			}
			
			if(t.getName().equals("}") && ifFlag > 0) {
				ifFlag--;
				t.setTokenType(Tokentype.IDENTIFIER);
				String eLabel = endingLabels.pop();
				t.setName(eLabel);
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
	private String getThreeAddressCodeExpression(ThreeAddressCode tac, int count) {
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < tac.localVariables.size(); i++) {
			x.append("LOCAL["+(i+count)+"] = "+tac.localVariables.get(i)+";\n");
		}
		return x.toString();
	}
	public void addToFunctionTokenList(FunctionTokens ft) {
		this.functionTokenList.add(ft);
	}
}