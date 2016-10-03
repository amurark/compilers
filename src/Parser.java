/**
 * Parser parses the input program for grammar and syntactical errors.
 * @author Ankit
 *
 */
public class Parser {
	/**
	 * Instance of Scanner
	 */
	public Scanner scanner;
	/**
	 * Stores the current token as returned by the GetNextToken method of Scanner
	 */
	public Token currentToken;
	/**
	 * Stores the current value of the token.
	 */
	public String word;
	/**
	 * Stores the current type of the token
	 */
	public Tokentype type;
	
	private int varCount = 0;
	private int funcCount = 0;
	private int statementCount = 0;
	
	/**
	 * Get the scanner object.
	 * @param scanner
	 */
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	
	/**
	 * Method to initialize the Parser and output the result.
	 */
	public void initialize() {
		/**
		 * If the program is empty, it's a pass case.
		 * Else call the program() method.
		 */
		if(!this.scanner.HasMoreTokens()) {
			System.out.println("Pass. variable "+varCount+" function "+funcCount+" statement "+statementCount);
		} else {
			/**
			 * Read the next word from scanner.
			 */
			nextWord();
			boolean x = this.program();
			/**
			 * If scanner has more tokens even when parser is done, it means that the input program is not following grammar.
			 * Else if parser returns false, there is some problem with the input program
			 */
			if(this.scanner.HasMoreTokens()) {
				System.out.println("Error in parsing the program.");
			} else {
				if(x) {
					System.out.println("Pass. variable "+varCount+" function "+funcCount+" statement "+statementCount);
				} else {
					System.out.println("Error in parsing the program.");
				}
			}
		}
	}
	
	/**
	 * Get the nextWord from the scanner
	 */
	private void nextWord() {
		/**
		 * Before getting the next token, check if scanner has more tokens.
		 */
		if(this.scanner.HasMoreTokens()) {
			/**
			 * Initialize currentToken
			 */
			this.currentToken = this.scanner.GetNextToken();
			/**
			 * Initialize currentToken's value
			 */
			this.word = this.currentToken.getName();
			/**
			 * Initialize currentToken's type
			 */
			this.type = this.currentToken.getTokenType();
			//System.out.println(this.type+" "+this.word);
			/**
			 * If the current token is a META_STATEMENT or a delimiter then skip it and read the next token.
			 */
			if(this.type == Tokentype.META_STATEMENT || this.type == Tokentype.DELIMITER) {
				nextWord();
			}
		}
	}
	
	/**
	 * <program>
	 * @return
	 */
	public boolean program() {
		if(type_name() == true) {
			boolean x = program_1();
			return x;
		} else {
			return true;
		}
	}
	
	/**
	 * <program>`
	 * @return
	 */
	private boolean program_1() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return program_11();
		} else {
			return false;
		}
	}
	
	/**
	 * <program>``
	 * @return
	 */
	private boolean program_11() {
		if(word.equals("(")) {
			nextWord();
			if(parameter_list() == false) {
				return false;
			} else {
				if(word.equals(")")) {
					nextWord();
					return program_111();
				} else {
					return false;
				}
			}
		} else if(word.equals(";")) {
			nextWord();
			varCount++;
			return program();
		} else if(word.equals(",")) {
			nextWord();
			varCount++;
			if(id() == false) {
				return false;
			} else {
				if(id_list_1() == false) {
					return false;
				} else {
					if(word.equals(";")) {
						nextWord();
						return program();
					} else {
						return false;
					}
				}
			}
		} else if(word.equals("[")) {
			nextWord();
			varCount++;
			if(expression() == false) {
				return false;
			} else {
				if(word.equals("]")) {
					nextWord();
					return program_1111();
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <program>```
	 * @return
	 */
	private boolean program_111() {
		if(word.equals(";")) {
			nextWord();
			return func_list();
		} else if(word.equals("{")) {
			nextWord();
			this.funcCount++;
			if(data_decls() == false) {
				return false;
			} else {
				if(statements() == false) {
					return false;
				} else {
					if(word.equals("}")) {
						nextWord();
						boolean a = func_list();
						return a;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * <program>````
	 * @return
	 */
	private boolean program_1111() {
		if(word.equals(";")) {
			nextWord();
			return program();
		} else if(word.equals(",")){
			nextWord();
			if(id() == false) {
				return false;
			} else {
				if(id_list_1() == false) {
					return false;
				} else {
					if(word.equals(";")) {
						nextWord();
						return program();
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	
	/**
	 * <func list>
	 * @return
	 */
	private boolean func_list() {
		if(func() == true) {
			return func_list();
		} else {
			return true;
		}
	}
	
	/**
	 * <func>
	 * @return
	 */
	private boolean func() {
		if(func_decl() == false){
			return false;
		} else {
			return func1();
		}
	}
	
	/**
	 * <func>`
	 * @return
	 */
	private boolean func1() {
		if(word.equals(";")) {
			nextWord();
			return true;
		} else {
			if(word.equals("{")) {
				nextWord();
				this.funcCount++;
				if(data_decls() == false) {
					return false;
				} else {
					if(statements() == false) {
						return false;
					} else {
						if(word.equals("}")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		}
	}
	
	/**
	 * <func decl>
	 * @return
	 */
	private boolean func_decl() {
		if(type_name() == false) {
			return false;
		} else {
			if(type == Tokentype.IDENTIFIER) {
				nextWord();
				if(word.equals("(")) {
					nextWord();
					if(parameter_list() == false) {
						return false;
					} else {
						if(word.equals(")")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	/**
	 * <type name>
	 * @return
	 */
	private boolean type_name() {
		if(word.equals("int") || word.equals("void") || word.equals("binary") || word.equals("decimal")) {
			nextWord();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <parameter list>
	 * @return
	 */
	private boolean parameter_list() {
		if(word.equals("int") || word.equals("binary") || word.equals("decimal")) {
			nextWord();
			if(type == Tokentype.IDENTIFIER) {
				nextWord();
				return non_empty_list_1();	
			} else {
				return false;
			}
		} else if(word.equals("void")) {
			nextWord();
			return parameter_list_1();
		} else {
			return true;
		}
	}

	/**
	 * <parameter list>`
	 * @return
	 */
	private boolean parameter_list_1() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return non_empty_list_1();
		} else {
			return true;
		}
	}
	
	/**
	 * <non empty list>
	 * This function is not used by the grammar as this was substituted and left factored to avoid backtracking. 
	 * @return
	 */
	private boolean non_empty_list() {
		if(word.equals("int") || word.equals("void") || word.equals("binary") || word.equals("decimal")) {
			nextWord();
			if(type == Tokentype.IDENTIFIER) {
				nextWord();
				if(non_empty_list_1() == false) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <non empty list>`
	 * @return
	 */
	private boolean non_empty_list_1() {
		if(word.equals(",")) {
			nextWord();
			if(type_name() == false) {
				return false;
			} else {
				if(type == Tokentype.IDENTIFIER) {
					nextWord();
					if(non_empty_list_1() == false) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}
	
	/**
	 * <data decls>
	 * @return
	 */
	private boolean data_decls() {
		if(word.equals("int") || word.equals("void") || word.equals("binary") || word.equals("decimal")) {
			nextWord();
			if(id_list() == false) {
				return false;
			} else {
				if(word.equals(";")) {
					nextWord();
					return data_decls();
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}

	/**
	 * <id list>
	 * @return
	 */
	private boolean id_list() {
		if(id() == false) {
			return false;
		} else {
			return id_list_1(); 	
		}
	}

	/**
	 * <id list>`
	 * @return
	 */
	private boolean id_list_1() {
		if(word.equals(",")) {
			nextWord();
			if(id() == false) {
				return false;
			} else {
				return id_list_1();
			}
		} else {
			return true;
		}
	}

	/**
	 * <id>
	 * @return
	 */
	private boolean id() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			this.varCount++;
			return id_1();
		} else {
			return false;
		}
	}
	
	/**
	 * <id>`
	 * @return
	 */
	private boolean id_1() {
		if(word.equals("[")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				if(word.equals("]")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}
	
	/**
	 * <block statements>
	 * @return
	 */
	private boolean block_statements() {
		if(word.equals("{")) {
			nextWord();
			if(statements() == false) {
				return false;
			} else {
				if(word.equals("}")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <statements>
	 * @return
	 */
	private boolean statements() {
		if(statement() == true) {
			return statements();
		} else {
			return true;
		}
	}
	
	/**
	 * <statement>
	 * @return
	 */
	private boolean statement() {
		if(type == Tokentype.IDENTIFIER) {
			this.statementCount++;
			nextWord();
			return statement_1();
		} else if(if_statement() == true) {
			this.statementCount++;
			return true;
		} else if(while_statement() == true) {
			this.statementCount++;
			return true;
		} else if(return_statement() == true) {
			this.statementCount++;
			return true;
		} else if(break_statement() == true) {
			this.statementCount++;
			return true;
		} else if(continue_statement() == true) {
			this.statementCount++;
			return true;
		} else if(word.equals("read")) {
			this.statementCount++;
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(type == Tokentype.IDENTIFIER) {
					nextWord();
					if(word.equals(")")) {
						nextWord();
						if(word.equals(";")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if(word.equals("write")) {
			this.statementCount++;
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(expression() == false) {
					return false;
				} else {
					if(word.equals(")")) {
						nextWord();
						if(word.equals(";")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else if(word.equals("print")) {
			this.statementCount++;
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(type == Tokentype.STRING) {
					nextWord();
					if(word.equals(")")) {
						nextWord();
						if(word.equals(";")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <statements>`
	 * @return
	 */
	private boolean statement_1() {
		if(assignment_1() == true) {
			return true;
		} else {
			if(word.equals("(")) {
				nextWord();
				if(expr_list() == true) {
					if(word.equals(")")) {
						nextWord();
						if(word.equals(";")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	/**
	 * <assignment>
	 * This function is not used by the grammar as this was substituted and left factored to avoid backtracking.
	 * @return
	 */
	private boolean assignment() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return assignment_1();
		} else {
			return false;
		}
	}
	
	/**
	 * <assignment>`
	 * @return
	 */
	private boolean assignment_1() {
		if(word.equals("=")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				if(word.equals(";")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		} else if(word.equals("[")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				if(word.equals("]")) {
					nextWord();
					if(word.equals("=")) {
						nextWord();
						if(expression() == false) {
							return false;
						} else {
							if(word.equals(";")) {
								nextWord();
								return true;
							} else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	/**
	 * <func call>
	 * This function is not used by the grammar as this was substituted and left factored to avoid backtracking.
	 * @return
	 */
	private boolean func_call() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(expr_list() == false) {
					return false;
				} else {
					if(word.equals(")")) {
						nextWord();
						if(word.equals(";")) {
							nextWord();
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <expr list>
	 * @return
	 */
	private boolean expr_list() {
		if(non_empty_expr_list() == false) {
			return true;
		} else {
			return true;
		}
	}
	
	/**
	 * <non-empty expr list>
	 * @return
	 */
	private boolean non_empty_expr_list() {
		if(expression() == false) {
			return false;
		} else {
			return non_empty_expr_list_1();
		}
	}

	/**
	 * <non-empty expr list>`
	 * @return
	 */
	private boolean non_empty_expr_list_1() {
		if(word.equals(",")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				return non_empty_expr_list_1();
			}
		} else {
			return true;
		}
	}
	
	/**
	 * <if statement>
	 * @return
	 */
	private boolean if_statement() {
		if(word.equals("if")) {
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(condition_expr() == false) {
					return false;
				} else {
					if(word.equals(")")) {
						nextWord();
						if(block_statements() == false) {
							return false;
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <condition expression>
	 * @return
	 */
	private boolean condition_expr() {
		if(condition() == false) {
			return false;
		} else {
			if(condtion_expr_1() == false) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * <condition expression>`
	 * @return
	 */
	private boolean condtion_expr_1() {
		if(condition_op() == true) {
			return condition();
		} else {
			return true;
		}
	}

	/**
	 * <condition op>
	 * @return
	 */
	private boolean condition_op() {
		if(word.equals("&")) {
			nextWord();
			if(word.equals("&")) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else if(word.equals("|")) {
			nextWord();
			if(word.equals("|")) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <condition>
	 * @return
	 */
	private boolean condition() {
		if(expression() == false) {
			return false;
		} else {
			if(comparison_op() == false) {
				return false;
			} else {
				if(expression() == false) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	/**
	 * <comparison op>
	 * @return
	 */
	private boolean comparison_op() {
		if(word.equals("=")) {
			nextWord();
			if(word.equals("=")) {
				nextWord();
				
				return true;
			} else {
				return false;
			}
		} else if(word.equals("!")) {
			nextWord();
			if(word.equals("=")) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else if(word.equals(">")) {
			nextWord();
			if(word.equals("=")) {
				nextWord();
				return true;
			} 
			return true; //CHECK
		} else if(word.equals("<")) {
			nextWord();
			if(word.equals("=")) {
				nextWord();
				return true;
			}
			return true; //CHECK
		} else {
			return false;
		}
	}
	
	/**
	 * <while statement>
	 * @return
	 */
	private boolean while_statement() {
		if(word.equals("while")) {
			nextWord();
			if(word.equals("(")) {
				nextWord();
				if(condition_expr() == false) {
					return false;
				} else {
					if(word.equals(")")) {
						nextWord();
						if(block_statements() == false) {
							return false;
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <return statement>
	 * @return
	 */
	private boolean return_statement() {
		if(word.equals("return")) {
			nextWord();
			return return_statement_1();
		} else {
			return false;
		}
	}

	/**
	 * <return statement>`
	 * @return
	 */
	private boolean return_statement_1() {
		if(word.equals(";")) {
			nextWord();
			return true;
		} else {
			if(expression() == false) {
				return false;
			} else {
				if(word.equals(";")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	/**
	 * <break statement>
	 * @return
	 */
	private boolean break_statement() {
		if(word.equals("break")) {
			nextWord();
			if(word.equals(";")) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * <continue statement>
	 * @return
	 */
	private boolean continue_statement() {
		if(word.equals("continue")) {
			nextWord();
			if(word.equals(";")) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * <expression>
	 * @return
	 */
	private boolean expression() {
		if(term() == false) {
			return false;
		} else {
			return expression_1();
		}
	}
	
	/**
	 * <expression>`
	 * @return
	 */
	private boolean expression_1() {
		if(addop() == true) {
			if(term() == false) {
				return false;
			} else {
				return expression_1();
			}
		} else {
			return true;
		}
	}
	
	/**
	 * <addop>
	 * @return
	 */
	private boolean addop() {
		if(word.equals("+")) {
			nextWord();
			return true;
		} else if(word.equals("-")) {
			nextWord();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <term>
	 * @return
	 */
	private boolean term() {
		if(factor() == false) {
			return false;
		} else {
			if(term_1() == false) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * <term>`
	 * @return
	 */
	private boolean term_1() {
		if(mulop() == true) {
			if(factor() == false) {
				return false;
			} else {
				return term_1();
			}
		} else {
			return true;
		}
	}
	
	/**
	 * <mulop>
	 * @return
	 */
	private boolean mulop() {
		if(word.equals("*")) {
			nextWord();
			return true;
		} else if(word.equals("/")) {
			nextWord();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <factor>
	 * @return
	 */
	private boolean factor() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return factor_1();
		} else if(type == Tokentype.NUMBER) {
			nextWord();
			return true;
		} else if(word.equals("-")) {
			nextWord();
			if(type == Tokentype.NUMBER) {
				nextWord();
				return true;
			} else {
				return false;
			}
		} else if(word.equals("(")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				if(word.equals(")")) {
					nextWord();
					return true;
				} else {
					return false;
				}
				
			}
		} else {
			return false;
		}
	}
	
	/**
	 * <factor>`
	 * @return
	 */
	private boolean factor_1() {
		if(word.equals("[")) {
			nextWord();
			if(expression() == false) {
				return false;
			} else {
				if(word.equals("]")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		} else if(word.equals("(")) {
			nextWord();
			if(expr_list() == false) {
				return false;
			} else {
				if(word.equals(")")) {
					nextWord();
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}
}
