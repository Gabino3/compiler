package compiler;

import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.ForegroundAction;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import compiler.ParseTree.Node;

@SuppressWarnings("unused")
public class RDP {
	// -----------------------------------other classes
	@SuppressWarnings("serial")
	private static class parseError extends Exception {
		parseError(String message) {
			super(message);
		}
	}

	private static final char EOF = (char) 0xFFFF;
	// ---------------------------------variables
	ParseTree<String> tree;
	ArrayList<String> definedClasses;
	String line;
	int linePointer;
	boolean insideBracket;
	public enum Terminal {
		CLS_CHAR, RE_CHAR, DEFINED_CLASS, UNION, STAR, PLUS, DASH, DOT, LPAREN, RPAREN, LBRACKET, RBRACKET, CARROT, IN, CONCAT, ROOT
	};

	// ----------------------------------constructor

	public RDP(String line, ArrayList<String> definedClasses) {
		tree = new ParseTree<String>("");
		this.line = line;
		linePointer = 0;
		insideBracket = false;
		this.definedClasses = definedClasses;
	}

	public static ParseTree<String> rdparser(String line, ArrayList<String> definedClasses) throws parseError {

		RDP rdp = new RDP(line, definedClasses);
		String id = rdp.line.substring(0, rdp.line.indexOf(' '));
		rdp.tree.getRoot().setData(id);
		rdp.tree.getRoot().setType(Terminal.ROOT);
		//System.out.print(id);
		rdp.linePointer = rdp.line.indexOf(' ');
		rdp.regex();
		//System.out.println();
		//System.out.print(rdp.tree);
		return rdp.tree;
	}

	// --------------------------------- helper methods

	public void print(String s) {
		//System.out.print(" " + s);
	}

	public void print(char c) {
		print(Character.toString(c));
	}

	private void skipWhiteSpace() {
		if(!insideBracket){
			char ch = line.charAt(linePointer);
			while (ch != EOF && ch != '\n' && Character.isWhitespace(ch)) {
				linePointer++;
				ch = line.charAt(linePointer);
			}
		}
	}

	private boolean peek(Terminal t) {

		// TODO
		if (linePointer >= line.length())
			return false;

		skipWhiteSpace();
		switch (t) {

		case CLS_CHAR:
			if (line.charAt(linePointer) == '\\') {
				if (line.charAt(linePointer + 1) == '\\' || line.charAt(linePointer + 1) == '^' || line.charAt(linePointer + 1) == '-' || line.charAt(linePointer + 1) == '[' || line.charAt(linePointer + 1) == ']') {
					return true;
				}
			} else if (((int) line.charAt(linePointer) >= 32 && (int) line.charAt(linePointer) <= 90 && (int) line.charAt(linePointer) != 45) || ((int) line.charAt(linePointer) >= 95 && (int) line.charAt(linePointer) <= 126)) { // any ascii char other than \^-[]
																																																								
				return true;
			}
			break;
		case RE_CHAR:
			if (line.charAt(linePointer) == '\\') {
				if (line.charAt(linePointer + 1) == '\\' || line.charAt(linePointer + 1) == '*' || line.charAt(linePointer + 1) == '+' || line.charAt(linePointer + 1) == '[' || line.charAt(linePointer + 1) == ']' || line.charAt(linePointer + 1) == '?' || line.charAt(linePointer + 1) == '|' || line.charAt(linePointer + 1) == '(' || line.charAt(linePointer + 1) == ')' || line.charAt(linePointer + 1) == '.' || line.charAt(linePointer + 1) == '\'' || line.charAt(linePointer + 1) == '\"' || line.charAt(linePointer + 1) == ' ') {

					return true;

				}
			} else if ((int) line.charAt(linePointer) >= 32 && (int) line.charAt(linePointer) <= 126 && line.charAt(linePointer) != '\\' && line.charAt(linePointer) != '*' && line.charAt(linePointer) != '+' && line.charAt(linePointer) != '[' && line.charAt(linePointer) != ']' && line.charAt(linePointer) != '?' && line.charAt(linePointer) != '|' && line.charAt(linePointer) != '(' && line.charAt(linePointer) != ')' && line.charAt(linePointer) != '.' && line.charAt(linePointer) != '\'' && line.charAt(linePointer) != '\"' && line.charAt(linePointer) != ' ') { // any ascii char other than SPACE \ * + ? | [ ] ( ) . ' "
																																																																																																																																											

				return true;

			}
			break;
		case DEFINED_CLASS:
			if (line.charAt(linePointer) == '$') {
				int i = 1;
				while (linePointer + i < line.length() && (((int) line.charAt(linePointer + i) >= 65 && (int) line.charAt(linePointer + i) <= 90) || // look for a word made up of upper and lower case letters
																															
						((int) line.charAt(linePointer + i) >= 97 && (int) line.charAt(linePointer + i) <= 122))) {
					i++;
				}
				if (definedClasses.contains(line.substring(linePointer, linePointer + i))) {
					return true;
				}
			}

			break;
		case UNION:
			if (line.charAt(linePointer) == '|')
				return true;
			break;
		case STAR:
			if (line.charAt(linePointer) == '*')
				return true;
			break;
		case PLUS:
			if (line.charAt(linePointer) == '+')
				return true;
			break;
		case DASH:
			if (line.charAt(linePointer) == '-')
				return true;
			break;
		case DOT:
			if (line.charAt(linePointer) == '.')
				return true;
			break;
		case LPAREN:
			if (line.charAt(linePointer) == '(')
				return true;
			break;
		case RPAREN:
			if (line.charAt(linePointer) == ')')
				return true;
			break;
		case LBRACKET:
			if (line.charAt(linePointer) == '['){
				insideBracket = true;
				return true;
			}
			break;
		case RBRACKET:
			if (line.charAt(linePointer) == ']'){
				insideBracket = false;
				return true;
			}
			break;
		case CARROT:
			if (line.charAt(linePointer) == '^')
				return true;
			break;
		case IN:
			if (line.charAt(linePointer) == 'I' && line.charAt(linePointer + 1) == 'N')
				return true;
			break;
		}

		return false;
	}

	private boolean match(Terminal t) {

		if(linePointer>=line.length())
			return false;
		
		skipWhiteSpace();
		switch (t) {
		
		case CLS_CHAR:
			if(line.charAt(linePointer) == '\\'){
				if(line.charAt(linePointer+1) == '\\' || line.charAt(linePointer+1) == '^' || line.charAt(linePointer+1) == '-' || 
						line.charAt(linePointer+1) == '[' ||line.charAt(linePointer+1) == ']'){
					linePointer+=2; //take care of string management
					print("\\CLS_CHAR"); //debugging
					//tree management-----------------------------------
					if(tree.rootIsCurrent() || tree.getCurrent().isType(Terminal.CARROT)){//if this CLS is the first thing or is right after a negation carrot
						Node<String> temp = new Node<String>(line.substring(linePointer-2, linePointer), Terminal.CLS_CHAR, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}else if(tree.getCurrent().isType(Terminal.DASH) && tree.getCurrent().getNumOfChildren()<2){//if there is a range (CLS - CLS)
						tree.getCurrent().addChild(new Node<String>(line.substring(linePointer-2, linePointer), Terminal.CLS_CHAR, tree.getCurrent()));
					}else if(tree.getCurrent().isType(Terminal.CLS_CHAR) || (tree.getCurrent().isType(Terminal.DASH) && tree.getCurrent().getNumOfChildren()>=2)){ //if there needs to be a union (CLS CLS --OR-- CLS-CLS CLS )
						tree.getCurrent().newParent(new Node<String>(null, Terminal.UNION, tree.getCurrent().getParent()));
						tree.setCurrent(tree.getCurrent().getParent());
						Node<String> temp = new Node<String>(line.substring(linePointer-2, linePointer), Terminal.CLS_CHAR, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}
					
					//---------
					return true;
				}
			}else if (((int)line.charAt(linePointer) >=32 && (int)line.charAt(linePointer) <=90 && (int)line.charAt(linePointer) != 45) ||
					((int)line.charAt(linePointer) >=95 && (int)line.charAt(linePointer) <=126)){ //any ascii char other than \^-[]
				linePointer++; //string management
				print("CLS_CHAR");//debugging
				//tree management-----------------------------------
				if(tree.rootIsCurrent() || tree.getCurrent().isType(Terminal.CARROT)){//if this CLS is the first thing or is right after a negation carrot
					Node<String> temp = new Node<String>(Character.toString(line.charAt(linePointer-1)), Terminal.CLS_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}else if(tree.getCurrent().isType(Terminal.DASH) && tree.getCurrent().getNumOfChildren()<2){//if there is a range (CLS - CLS)
					tree.getCurrent().addChild(new Node<String>(Character.toString(line.charAt(linePointer-1)), Terminal.CLS_CHAR, tree.getCurrent()));
				}else if(tree.getCurrent().isType(Terminal.CLS_CHAR) || (tree.getCurrent().isType(Terminal.DASH) && tree.getCurrent().getNumOfChildren()>=2)){ //if there needs to be a union (CLS CLS --OR-- CLS-CLS CLS )
					tree.getCurrent().newParent(new Node<String>(null, Terminal.UNION, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
					Node<String> temp = new Node<String>(Character.toString(line.charAt(linePointer-1)), Terminal.CLS_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}
				
				//---------
				return true;
			}
			break;
		case RE_CHAR:
			if(line.charAt(linePointer) == '\\'){
				if(line.charAt(linePointer+1) == '\\' || line.charAt(linePointer+1) == '*' || line.charAt(linePointer+1) == '+' || 
						line.charAt(linePointer+1) == '[' || line.charAt(linePointer+1) == ']' || line.charAt(linePointer+1) == '?' || 
						line.charAt(linePointer+1) == '|' || line.charAt(linePointer+1) == '(' || line.charAt(linePointer+1) == ')' || 
						line.charAt(linePointer+1) == '.' || line.charAt(linePointer+1) == '\'' || line.charAt(linePointer+1) == '\"' || line.charAt(linePointer+1) == ' '){
					linePointer+=2;//string management
					print("\\RE_CHAR");//debugging
					//tree management-----------------------------------
					if(tree.rootIsCurrent() ){
						Node<String> temp = new Node<String>(line.substring(linePointer-2, linePointer), Terminal.RE_CHAR, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
						
					}else if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) || tree.getCurrent().isType(Terminal.CONCAT) || tree.getCurrent().isType(Terminal.PLUS) || tree.getCurrent().isType(Terminal.STAR)){ //if there needs to be a union (CLS CLS --OR-- CLS-CLS CLS )
						
						tree.getCurrent().newParent(new Node<String>(null, Terminal.CONCAT, tree.getCurrent().getParent()));
						tree.setCurrent(tree.getCurrent().getParent());
						Node<String> temp = new Node<String>(line.substring(linePointer-2, linePointer), Terminal.RE_CHAR, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}else if(tree.getCurrent().isType(Terminal.UNION)){
						Node<String> temp = new Node<String>(line.substring(linePointer-2, linePointer), Terminal.RE_CHAR, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}
					
					//---------
					return true;
				}
			}else if ((int)line.charAt(linePointer) >=32 && (int)line.charAt(linePointer) <=126 && 
					line.charAt(linePointer) != '\\' && line.charAt(linePointer) != '*' && line.charAt(linePointer) != '+' && 
					line.charAt(linePointer) != '[' && line.charAt(linePointer) != ']' && line.charAt(linePointer) != '?' && 
					line.charAt(linePointer) != '|' && line.charAt(linePointer) != '(' && line.charAt(linePointer) != ')' && 
					line.charAt(linePointer) != '.' && line.charAt(linePointer) != '\'' && line.charAt(linePointer) != '\"' && line.charAt(linePointer) != ' '){ //any ascii char other than SPACE, \, *, +, ?, |, [, ], (, ), ., ' and " 
				linePointer++;
				print("RE_CHAR");//debugging
				//tree management-----------------------------------
				if(tree.rootIsCurrent() ){
					Node<String> temp = new Node<String>(line.substring(linePointer-1, linePointer), Terminal.RE_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
					
				}else if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) || tree.getCurrent().isType(Terminal.CONCAT) || tree.getCurrent().isType(Terminal.PLUS) || tree.getCurrent().isType(Terminal.STAR)){ //if there needs to be a union (CLS CLS --OR-- CLS-CLS CLS )
					
					tree.getCurrent().newParent(new Node<String>(null, Terminal.CONCAT, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
					Node<String> temp = new Node<String>(line.substring(linePointer-1, linePointer), Terminal.RE_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}else if(tree.getCurrent().isType(Terminal.UNION)){
					Node<String> temp = new Node<String>(line.substring(linePointer-1, linePointer), Terminal.RE_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}
				
				//---------
				return true;
			}
			break;
		case DEFINED_CLASS:
			if(line.charAt(linePointer) == '$'){
				int i = 1;
				while (   linePointer+i < line.length() && (((int)line.charAt(linePointer+i) >=65 && (int)line.charAt(linePointer+i) <=90) || //look for a word made up of upper and lower case letters
						((int)line.charAt(linePointer+i) >=97 && (int)line.charAt(linePointer+i) <=122))){
					i++;
				}
				if( definedClasses.contains(line.substring(linePointer, linePointer+i)) ){
					print("Defined_Class");//debugging
					linePointer+=i;//string management
					//-----------tree management
					//RE_CHAR, DEFINED_CLASS, UNION, STAR, PLUS, DASH, DOT, LPAREN, RPAREN, LBRACKET, RBRACKET, CARROT, IN, CONCAT
					
					if(tree.getCurrent().isType(Terminal.IN) || tree.rootIsCurrent() || tree.getCurrent().isType(Terminal.LPAREN)){
						Node<String> temp = new Node<String>(line.substring(linePointer-i, linePointer), Terminal.DEFINED_CLASS, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}else if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) || tree.getCurrent().isType(Terminal.CONCAT) || tree.getCurrent().isType(Terminal.PLUS) || tree.getCurrent().isType(Terminal.STAR)){ 
						
						tree.getCurrent().newParent(new Node<String>(null, Terminal.CONCAT, tree.getCurrent().getParent()));
						tree.setCurrent(tree.getCurrent().getParent());
						Node<String> temp = new Node<String>(line.substring(linePointer-i, linePointer), Terminal.DEFINED_CLASS, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}else if(tree.getCurrent().isType(Terminal.UNION)){
						Node<String> temp = new Node<String>(line.substring(linePointer-i, linePointer), Terminal.DEFINED_CLASS, tree.getCurrent());
						tree.getCurrent().addChild(temp);
						tree.setCurrent(temp);
					}
					
					//---------------
					return true;
				}
			}
			
			break;
		case UNION:
			if(line.charAt(linePointer) == '|'){
				linePointer++;
				print('|');//string management
				//-----------tree management
				
				if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) ){ 
					tree.getCurrent().newParent(new Node<String>(null, Terminal.UNION, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
				}
				
				//--------------------------
				return true;
			}
			break;
		case STAR:
			if(line.charAt(linePointer) == '*'){
				linePointer++;
				print('*');//string management
				//-----------tree management
				
				if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) ){ 
					tree.getCurrent().newParent(new Node<String>(null, Terminal.STAR, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
				}
				
				//--------------------------
				return true;
			}
			break;
		case PLUS:
			if(line.charAt(linePointer) == '+'){
				linePointer++;
				print('+');//string management
				//-----------tree management
				
				if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN) ){ 
					tree.getCurrent().newParent(new Node<String>(null, Terminal.PLUS, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
				}
				
				//--------------------------
				return true;
			}
			break;
		case DASH:
			if(line.charAt(linePointer) == '-'){
				linePointer++;//string management
				print('-');//debugging
				//-----------tree management
				
				if(tree.getCurrent().isType(Terminal.CLS_CHAR)){
					tree.getCurrent().newParent(new Node<String>(null, Terminal.DASH, tree.getCurrent().getParent()) );
					tree.setCurrent(tree.getCurrent().getParent());
				}
				
				//--------------
				return true;
			}
			break;
		case DOT:
			if(line.charAt(linePointer) == '.'){
				linePointer++;
				print('.');
				return true;
			}
			break;
		case LPAREN:
			if(line.charAt(linePointer) == '('){
				linePointer++;
				print('(');//string management
				//-----------tree management
				
				if(tree.rootIsCurrent()){ 
					
					Node<String> temp = new Node<String>(null, Terminal.LPAREN, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}else if(tree.getCurrent().isType(Terminal.RE_CHAR) || tree.getCurrent().isType(Terminal.DEFINED_CLASS) || tree.getCurrent().isType(Terminal.RPAREN)){
					tree.getCurrent().newParent(new Node<String>(null, Terminal.CONCAT, tree.getCurrent().getParent()));
					tree.setCurrent(tree.getCurrent().getParent());
					Node<String> temp = new Node<String>(null, Terminal.LPAREN, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}
				
				//--------------------------
				return true;
			}
			break;
		case RPAREN:
			if(line.charAt(linePointer) == ')'){
				linePointer++;
				print(')');//string management
				//-----------tree management
				
				if(tree.getCurrent().isType(Terminal.UNION)){
					Node<String> temp = new Node<String>(Character.toString(NFA.EPSILON), Terminal.RE_CHAR, tree.getCurrent());
					tree.getCurrent().addChild(temp);
					tree.setCurrent(temp);
				}
				
				while (!tree.getCurrent().isType(Terminal.LPAREN)){ //go to the carrot level
					tree.setCurrent(tree.getCurrent().getParent());
				}
				tree.getCurrent().setType(Terminal.RPAREN);
				//--------------------------
				return true;
			}
			break;
		case LBRACKET:
			if(line.charAt(linePointer) == '['){
				linePointer++;
				print('[');
				return true;
			}
			break;
		case RBRACKET:
			if(line.charAt(linePointer) == ']'){
				linePointer++;
				print(']');
				insideBracket = false;
				return true;
			}
			break;
		case CARROT:
			if(line.charAt(linePointer) == '^'){
				linePointer++;//string management
				print('^');//debugging
				//-----------tree management
				if (tree.rootIsCurrent()){
					Node<String> temp = new Node<String>(null, Terminal.CARROT, tree.getCurrent());
					tree.getCurrent().addChild(temp );
					tree.setCurrent(temp);
				}
				//---------------
				return true;
			}
			break;
			
		case IN:
			if(line.charAt(linePointer) == 'I' && line.charAt(linePointer+1) == 'N'){
				linePointer+=2;//string management
				print("IN");//debugging
				//-----------tree management
				
				while (!tree.getCurrent().isType(Terminal.CARROT)){ //go to the carrot level
					tree.setCurrent(tree.getCurrent().getParent());
				}
				Node<String> temp = new Node<String>(null, Terminal.IN, tree.getCurrent().getParent());
				tree.getCurrent().newParent(temp);
				tree.setCurrent(temp);
				
				
				//------------------
				return true;
			}
			break;
		}
		
		
		return false;

	}

	// --------------------------recursive descent parser

	private void regex() throws parseError {
		rexp();

	}

	private void rexp() throws parseError {
		rexp1();
		rexp_();

	}

	private void rexp_() throws parseError {
		if (peek(Terminal.UNION)) {
			match(Terminal.UNION);
			rexp1();
			rexp_();

		}

	}

	private void rexp1() throws parseError {
		rexp2();
		rexp1_();

	}

	private void rexp1_() throws parseError {
		if (peek(Terminal.LPAREN) || peek(Terminal.RE_CHAR) || peek(Terminal.DOT) || peek(Terminal.LBRACKET) || peek(Terminal.DEFINED_CLASS)) {
			rexp2();
			rexp1_();
		}

	}

	private void rexp2() throws parseError {
		if (peek(Terminal.LPAREN)) {
			match(Terminal.LPAREN);
			rexp();
			if (!match(Terminal.RPAREN))
				throw new parseError("Missing Right Paren");
			rexp2Tail();

		} else if (peek(Terminal.RE_CHAR) && !peek(Terminal.DEFINED_CLASS)) {
			match(Terminal.RE_CHAR);
			rexp2Tail();
		} else {
			rexp3();
		}
	}

	private void rexp2Tail() {
		if (peek(Terminal.STAR))
			match(Terminal.STAR);
		else if (peek(Terminal.PLUS))
			match(Terminal.PLUS);

	}

	private void rexp3() throws parseError {
		if (peek(Terminal.DOT) || peek(Terminal.LBRACKET) || peek(Terminal.DEFINED_CLASS))
			charClass();

	}

	private void charClass() throws parseError {
		if (peek(Terminal.DOT)) {
			match(Terminal.DOT);
		} else if (peek(Terminal.LBRACKET)) {
			match(Terminal.LBRACKET);
			charClass1();
		} else if (peek(Terminal.DEFINED_CLASS)) {
			match(Terminal.DEFINED_CLASS);
		}

	}

	private void charClass1() throws parseError {
		if (peek(Terminal.RBRACKET) || peek(Terminal.CLS_CHAR)) {
			charSetList();
		} else if (peek(Terminal.CARROT)) {
			excludeSet();
		} else {
			throw new parseError("Missing Right Bracket OR character");
		}

	}

	private void charSetList() throws parseError {
		if (peek(Terminal.CLS_CHAR)) {
			charSet();
			charSetList();
		} else if (peek(Terminal.RBRACKET)) {
			match(Terminal.RBRACKET);
		} else {
			throw new parseError("Missing Right Bracket");
		}

	}

	private void charSet() throws parseError {
		if (peek(Terminal.CLS_CHAR)) {
			match(Terminal.CLS_CHAR);
			charSetTail();
		} else {
			throw new parseError("Missing char");
		}

	}

	private void charSetTail() throws parseError {
		if (peek(Terminal.DASH)) {
			match(Terminal.DASH);
			if (!match(Terminal.CLS_CHAR))
				throw new parseError("Missing char");
		}

	}

	private void excludeSet() throws parseError {
		if (peek(Terminal.CARROT)) {
			match(Terminal.CARROT);
			charSet();
			if (!match(Terminal.RBRACKET) || !match(Terminal.IN))
				throw new parseError("Missing right bracket or IN statement");
			excludeSetTail();
		}

	}

	private void excludeSetTail() throws parseError {
		if (peek(Terminal.LBRACKET)) {
			match(Terminal.LBRACKET);
			charSet();
			if (!match(Terminal.RBRACKET))
				throw new parseError("Missing right bracket or IN statement");

		} else if (peek(Terminal.DEFINED_CLASS)) {
			match(Terminal.DEFINED_CLASS);
		} else {
			throw new parseError("Missing right bracket or IN statement");
		}

	}

	
	//------------------------------------------------- Interpret tree
	
	
public static NFA treeParser(ParseTree<String> t, ArrayList<NFA> nfas) throws Exception{
		
		NFA temp= treeParserHelper(t.getRoot(), nfas, t.getRoot().getData());
		temp.setId(t.getRoot().getData());
		return temp;
	}
	
	
	private static NFA treeParserHelper(Node<String> n, ArrayList<NFA> nfas, String id) throws Exception{
		
		NFA nfa0 = null;
		NFA nfa1 = null;
	
		if(n.hasChildren()){ //----------------------------------has children
			
			if(n.getChildren().size() == 1){ // -----------------1 child
				
				if (n.getChildren().get(0).hasChildren()){ // if child has children
				
					nfa0 = treeParserHelper(n.getChildren().get(0), nfas, id);
					
					switch (n.getType()){
					
					case ROOT : case CARROT : case RPAREN:
						return nfa0;
					case PLUS :
						return NFA.plus(id, nfa0);
					case STAR :
						return NFA.star(id, nfa0);
					
					}
			
				} else { // ------------------------------------if child has no children

					switch (n.getType()){
					
					case ROOT : case CARROT : case RPAREN :
						if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR)){
							return quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1));
						}else if (n.getChildren().get(0).isType(Terminal.DEFINED_CLASS)){
							return nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))).copy();
						}
						break;
					case PLUS :
						if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR)){
							return NFA.plus(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)));
						}else if (n.getChildren().get(0).isType(Terminal.DEFINED_CLASS)){
							return NFA.plus( id,(nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0)))));
						}
						break;
					case STAR :
						if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR)){
							return NFA.star(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)));
						}else if (n.getChildren().get(0).isType(Terminal.DEFINED_CLASS)){
							return NFA.star( id,(nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0)))));
						}
						break;
						
					}
					
					
				}
				
			}else{  //-------------------------------------------two children
				
				if (!n.getChildren().get(0).hasChildren() && !n.getChildren().get(1).hasChildren()){ // if children have no children
					
					
					switch (n.getType()){
					
						case UNION :
							if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR)){
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.union(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
											quickNFA(id, n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)) );
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.union(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
							}else if (n.getChildren().get(0).isType(Terminal.DEFINED_CLASS)){
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.union(id, nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
											quickNFA(n.getData(), n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)));
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.union(id, nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
							}
							break;
						case DASH :
							return quickNFA(id, n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1), 
									n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1));
							
						case CONCAT :
							if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR)){
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.concat(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
											quickNFA(id, n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)) );
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.concat(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
							}else if (n.getChildren().get(0).isType(Terminal.DEFINED_CLASS)){
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.concat(id, nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
											quickNFA(n.getData(), n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)));
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.concat(id, nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
							}
							break;
						case IN :
							@SuppressWarnings("unchecked")
							ArrayList<Character> temp = (ArrayList<Character>) (nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0)))).getAlphabet().clone();
							temp.remove(n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1));
							return quickNFA(id, temp);
							
					}
					
					
					
				}else { //---------------------------------------a child has a child (still in 2 children area)
					if(n.getChildren().get(0).hasChildren()){ //first has child
						
						nfa0 = treeParserHelper(n.getChildren().get(0), nfas, id);
						
						if(n.getChildren().get(1).hasChildren()){ //both have children
							
							nfa1 = treeParserHelper(n.getChildren().get(1), nfas, id);
							switch (n.getType()){
							
								case UNION :
									return NFA.union(id, nfa0, nfa1);
									
								case CONCAT :
									return NFA.concat(id, nfa0, nfa1);
								
							}
							
						} else { //first is only one with child
							switch (n.getType()){
							
							case UNION :
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.union(id, nfa0, 
											quickNFA(n.getData(), n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)));
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.union(id, nfa0, 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
								
							case CONCAT :
								if (n.getChildren().get(1).isType(Terminal.CLS_CHAR) || n.getChildren().get(1).isType(Terminal.RE_CHAR))
									return NFA.concat(id, nfa0, 
											quickNFA(n.getData(), n.getChildren().get(1).getData().charAt(n.getChildren().get(1).getData().length()-1)));
								else if(n.getChildren().get(1).isType(Terminal.DEFINED_CLASS))
									return NFA.concat(id, nfa0, 
											nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0))));
							
							case IN :
								@SuppressWarnings("unchecked")
								ArrayList<Character> temp = (ArrayList<Character>) (nfas.get(nfas.indexOf(new NFA(n.getChildren().get(1).getData(),0)))).getAlphabet().clone();
								temp.removeAll(nfa0.getAlphabet());
								//temp.add(NFA.EPSILON);
								return quickNFA(id, temp);
							
							}
							
						}
						
						
					} else if(n.getChildren().get(1).hasChildren()){
						
						nfa1 = treeParserHelper(n.getChildren().get(1), nfas, id);
						
						switch (n.getType()){
						
						case UNION :
							if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR))
								return NFA.union(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
										nfa1 );
							else if(n.getChildren().get(0).isType(Terminal.DEFINED_CLASS))
								return NFA.union(id,  nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
										nfa1);
							
						case CONCAT :
							if (n.getChildren().get(0).isType(Terminal.CLS_CHAR) || n.getChildren().get(0).isType(Terminal.RE_CHAR))
								return NFA.concat(id, quickNFA(n.getData(), n.getChildren().get(0).getData().charAt(n.getChildren().get(0).getData().length()-1)), 
										nfa1 );
							else if(n.getChildren().get(0).isType(Terminal.DEFINED_CLASS))
								return NFA.concat(id,  nfas.get(nfas.indexOf(new NFA(n.getChildren().get(0).getData(),0))), 
										nfa1);
						
						}
						
						
					}
					
					
				}
				
			}
			
		} else { //----------------------------------------------no children
			
			System.out.println("SHOULD NOT BE HERE");
			
		}
		
		return null;
	}
	
	
	private static NFA quickNFA(String id, char action){
		
		NFA temp;
			temp = new NFA(id, 0);
			temp.getNfa().addVertex();
			temp.getNfa().addAcceptStates(1, id);
			temp.addEdge(0, 1, action);
			return temp;
		
	}
	
	private static NFA quickNFA(String id, char start, char end){
		
		NFA temp;
			temp = new NFA(id, 0);
			temp.getNfa().addVertex();
			temp.getNfa().addAcceptStates(1, id);
			for(int i=(int)start; i<=end;i++)
				temp.addEdge(0, 1, (char)i);
			return temp;
		
	}
	
	private static NFA quickNFA(String id, ArrayList<Character> actions){
		
		NFA temp;
			temp = new NFA(id, 0);
			temp.getNfa().addVertex();
			temp.getNfa().addAcceptStates(1, id);
			for(char action : actions)
				temp.addEdge(0, 1, action);
			return temp;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
