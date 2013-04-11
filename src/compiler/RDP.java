package compiler;

import javax.swing.text.StyledEditorKit.ForegroundAction;

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
	String line;
	int linePointer;
	public enum Terminal {CLS_CHAR, RE_CHAR, DEFINED_CLASS, UNION, STAR, PLUS, DASH, ANY, LPAREN, RPAREN, LBRACKET, RBRACKET, CARROT};
	// ----------------------------------constructor

	public RDP(String line) {
		tree = new ParseTree<String>("");
		this.line = line;
		linePointer = 0;
	}

	public static ParseTree<String> rdparser(String line) throws parseError {

		RDP rdp = new RDP(line);

		rdp.regex();

		return rdp.tree;
	}

	// --------------------------------- helper methods

	private void skipWhiteSpace() {
		char ch = line.charAt(linePointer);
		while (ch != EOF && ch != '\n' && Character.isWhitespace(ch)) {
			linePointer++;
			ch = line.charAt(linePointer);
		}
	}
	
	private boolean peek(Terminal t){
		
		//TODO
		switch (t) {
		
		case CLS_CHAR:
			
			break;
		case RE_CHAR:
			
			break;
		case DEFINED_CLASS:
			
			break;
		case UNION:
			
			break;
		case STAR:
			
			break;
		case PLUS:
			
			break;
		case DASH:
			
			break;
		case ANY:
			
			break;
		case LPAREN:
			
			break;
		case RPAREN:
			
			break;
		case LBRACKET:
			
			break;
		case RBRACKET:
			
			break;
		case CARROT:
			
			break;
		default:
			
		}
		
		return false;
	}
	
	private boolean match(Terminal t) {
		// TODO Auto-generated method stub
		switch (t) {
		
		case CLS_CHAR:
			
			break;
		case RE_CHAR:
			
			break;
		case DEFINED_CLASS:
			
			break;
		case UNION:
			
			break;
		case STAR:
			
			break;
		case PLUS:
			
			break;
		case DASH:
			
			break;
		case ANY:
			
			break;
		case LPAREN:
			
			break;
		case RPAREN:
			
			break;
		case LBRACKET:
			
			break;
		case RBRACKET:
			
			break;
		case CARROT:
			
			break;
		default:
			
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
		if(peek(Terminal.LPAREN) || peek(Terminal.RE_CHAR) || peek(Terminal.ANY) || peek(Terminal.LBRACKET) || peek(Terminal.DEFINED_CLASS)){
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

		} else if (peek(Terminal.RE_CHAR)) {
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
	
	

	private void rexp3() {
		if (peek(Terminal.ANY) || peek(Terminal.LBRACKET) || peek(Terminal.DEFINED_CLASS))
			charClass();
		
	}

	private void charClass() {
		if (peek(Terminal.ANY)){
			match(Terminal.ANY);
		}else if (peek(Terminal.LBRACKET)){
			match(Terminal.LBRACKET);
			charClass1();
		}else if (peek(Terminal.DEFINED_CLASS)){
			match(Terminal.DEFINED_CLASS);
		}
		
	}
	
	private void charClass1() {
		
		// TODO
	}
	
	private void charSetList() {
		
		// TODO
	}

	private void charSet() {
		
		// TODO
	}
	
	private void charSetTail() {
		
		// TODO
	}
	
	private void excludeSet() {
		
		// TODO
	}
	
	private void excludeSetTail() {
		
		// TODO
	}


	private void definedClass() {
		
		// TODO
	}



}
