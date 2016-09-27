
public class Parser {
	public Scanner scanner;
	public Token currentToken;
	public String word;
	public Tokentype type;
	
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public boolean initialize() {
		if(!this.scanner.HasMoreTokens()) {
			return false;
		} else {
			nextWord();
			boolean x = this.program();
			if(this.scanner.HasMoreTokens()) {
				return false;
			} else {
				return x;
			}
			
		}
	}
	
	private void nextWord() {
		if(this.scanner.HasMoreTokens()) {
			this.currentToken = this.scanner.GetNextToken();
			this.word = this.currentToken.getName();
			this.type = this.currentToken.getTokenType();
			System.out.println(this.type+" "+this.word);
			if(this.type == Tokentype.META_STATEMENT || this.type == Tokentype.DELIMITER) {
				nextWord();
			}
		}
	}
	
	public boolean program() {
		if(type_name() == true) {
			boolean x = program_1();
			return x;
		} else {
			return true;
		}
	}

	private boolean program_1() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return program_11();
		} else {
			return false;
		}
	}

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
			return program();
		} else if(word.equals(",")) {
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
		} else if(word.equals("[")) {
			nextWord();
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
	
	private boolean program_111() {
		if(word.equals(";")) {
			nextWord();
			return func_list();
		} else if(word.equals("{")) {
			nextWord();
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

	

	private boolean func_list() {
		if(func() == true) {
			return func_list();
		} else {
			return true;
		}
	}

	private boolean func() {
		if(func_decl() == false){
			return false;
		} else {
			return func1();
		}
	}

	private boolean func1() {
		if(word.equals(";")) {
			nextWord();
			return true;
		} else {
			if(word.equals("{")) {
				nextWord();
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
	
	private boolean type_name() {
		if(word.equals("int") || word.equals("void") || word.equals("binary") || word.equals("decimal")) {
			nextWord();
			return true;
		} else {
			return false;
		}
	}

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

	private boolean parameter_list_1() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return non_empty_list_1();
		} else {
			return true;
		}
	}

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

	private boolean id_list() {
		if(id() == false) {
			return false;
		} else {
			return id_list_1(); 	
		}
	}

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

	private boolean id() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return id_1();
		} else {
			return false;
		}
	}

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

	private boolean statements() {
		if(statement() == true) {
			return statements();
		} else {
			return true;
		}
	}
	
	private boolean statement() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return statement_1();
		} else if(if_statement() == true) {
			return true;
		} else if(while_statement() == true) {
			return true;
		} else if(return_statement() == true) {
			return true;
		} else if(break_statement() == true) {
			return true;
		} else if(continue_statement() == true) {
			return true;
		} else if(word.equals("read")) {
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

	private boolean assignment() {
		if(type == Tokentype.IDENTIFIER) {
			nextWord();
			return assignment_1();
		} else {
			return false;
		}
	}

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

	private boolean expr_list() {
		if(non_empty_expr_list() == false) {
			return true;
		} else {
			return true;
		}
	}

	private boolean non_empty_expr_list() {
		if(expression() == false) {
			return false;
		} else {
			return non_empty_expr_list_1();
		}
	}

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

	private boolean condtion_expr_1() {
		if(condition_op() == true) {
			return condition();
		} else {
			return true;
		}
	}

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
	
	private boolean return_statement() {
		if(word.equals("return")) {
			nextWord();
			return return_statement_1();
		} else {
			return false;
		}
	}

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


	private boolean expression() {
		if(term() == false) {
			return false;
		} else {
			return expression_1();
		}
	}

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
