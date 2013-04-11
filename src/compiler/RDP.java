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

	public enum Terminal {
		CLS_CHAR, RE_CHAR, DEFINED_CLASS, UNION, STAR, PLUS, DASH, DOT, LPAREN, RPAREN, LBRACKET, RBRACKET, CARROT, IN
	};

	// ----------------------------------constructor

	public RDP(String line, ArrayList<String> definedClasses) {
		tree = new ParseTree<String>("");
		this.line = line;
		linePointer = 0;
		this.definedClasses = definedClasses;
	}

	public static ParseTree<String> rdparser(String line, ArrayList<String> definedClasses) throws parseError {

		RDP rdp = new RDP(line, definedClasses);
		String id = rdp.line.substring(0, rdp.line.indexOf(' '));
		rdp.tree.getRoot().setData(id);
		//System.out.print(id);
		rdp.linePointer = rdp.line.indexOf(' ');
		rdp.regex();
		System.out.println();
		System.out.print(rdp.tree);
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
		char ch = line.charAt(linePointer);
		while (ch != EOF && ch != '\n' && Character.isWhitespace(ch)) {
			linePointer++;
			ch = line.charAt(linePointer);
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
			if (line.charAt(linePointer) == '[')
				return true;
			break;
		case RBRACKET:
			if (line.charAt(linePointer) == ']')
				return true;
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
						tree.getCurrent().addChild(new Node<String>(line.substring(linePointer-2, linePointer), Terminal.CLS_CHAR, tree.getCurrent()));
						tree.setCurrent(tree.getCurrent().getChildren().get(0));
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
					linePointer+=2;
					print("\\RE_CHAR");
					return true;
				}
			}else if ((int)line.charAt(linePointer) >=32 && (int)line.charAt(linePointer) <=126 && 
					line.charAt(linePointer) != '\\' && line.charAt(linePointer) != '*' && line.charAt(linePointer) != '+' && 
					line.charAt(linePointer) != '[' && line.charAt(linePointer) != ']' && line.charAt(linePointer) != '?' && 
					line.charAt(linePointer) != '|' && line.charAt(linePointer) != '(' && line.charAt(linePointer) != ')' && 
					line.charAt(linePointer) != '.' && line.charAt(linePointer) != '\'' && line.charAt(linePointer) != '\"' && line.charAt(linePointer) != ' '){ //any ascii char other than SPACE, \, *, +, ?, |, [, ], (, ), ., ' and " 
				linePointer++;
				print("RE_CHAR");
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
					print("Defined_Class");
					linePointer+=i;
					
					return true;
				}
			}
			
			break;
		case UNION:
			if(line.charAt(linePointer) == '|'){
				linePointer++;
				print('|');
				return true;
			}
			break;
		case STAR:
			if(line.charAt(linePointer) == '*'){
				linePointer++;
				print('*');
				return true;
			}
			break;
		case PLUS:
			if(line.charAt(linePointer) == '+'){
				linePointer++;
				print('+');
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
				print('(');
				return true;
			}
			break;
		case RPAREN:
			if(line.charAt(linePointer) == ')'){
				linePointer++;
				print(')');
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

}
