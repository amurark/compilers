
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
			this.currentToken = this.scanner.GetNextToken();
			this.word = this.currentToken.getName();
			this.type = this.currentToken.getTokenType();
		}
		return true;
	}
	
	private void nextWord() {
		this.currentToken = this.scanner.GetNextToken();
		this.word = this.currentToken.getName();
		this.type = this.currentToken.getTokenType();
	}
	
	public boolean program() {
		if(data_decls() == false) {
			return false;
		} else {
			return func_list();
		}
	}

	private boolean func_list() {
		if(word.equals(null)) {
			return true;
		} else {
			if(func() == false) {
				return false;
			} else {
				return func_list();
			}
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
					if(statement() == false) {
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
			if(type != Tokentype.IDENTIFIER) {
				nextWord();
				return false;
			} else {
				nextWord();
				if(word.equals("(")) {
					nextWord();
					if(parameterList() == false) {
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

	private boolean parameterList() {
		if(word.equals(null)) {
			return true;
		} else {
			if(word.equals("void")) {
				nextWord();
				return true;
			} else {
				if(non_empty_list() == false) {
					return false;
				} else {
					return true;
				}
			}
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
		if(word.equals(null)) {
			return true;
		} else {
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
				return false;
			}
		}
	}
	
	private boolean data_decls() {
		if(word.equals(null)) {
			return true;
		} else {
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
				return false;
			}
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
		if(word.equals(null)) {
			return true;
		} else {
			if(word.equals(",")) {
				nextWord();
				if(id() == false) {
					return false;
				} else {
					return id_list_1();
				}
			} else {
				return false;
			}
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
			return false;
		}
	}

	private boolean expression() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean statement() {
		// TODO Auto-generated method stub
		return false;
	}
}
