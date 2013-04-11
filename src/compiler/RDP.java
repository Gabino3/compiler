package compiler;

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
	StringBuffer line;

	// ----------------------------------constructor

	public RDP(String line) {
		tree = new ParseTree<String>("");
		this.line = new StringBuffer(line);
	}

	public static ParseTree<String> rdparser(String line) {

		RDP rdp = new RDP(line);

		rdp.regex();

		return rdp.tree;
	}

	// --------------------------------- helper methods

	private void skipWhiteSpace() {
		char ch = line.charAt(0);
		while (ch != EOF && ch != '\n' && Character.isWhitespace(ch)) {
			line.deleteCharAt(0);
			ch = line.charAt(0);
		}
	}
	
	private String getNextToken(){
		String token = "";
		
		
		return token;
	}
	
	private boolean match(String token) {
		// TODO Auto-generated method stub
		return false;

	}

	// --------------------------recursive descent parser

	private boolean regex() {
		return rexp();

	}

	private boolean RE_CHAR() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean CLS_CHAR() {
		return false;
		// TODO

	}

	private boolean rexp() {
		if (rexp1())
			return rexp_();
		return false;

	}

	private boolean rexp_() {
		if (match("|")) {
			if (rexp1())
				return rexp_();
			return false;
		} else {
			return true;
		}
	}

	private boolean rexp1() {
		if (rexp2()) {
			return rexp1_();
		}
		return false;

	}

	private boolean rexp1_() {
		if (rexp2())
			return rexp1_();
		return true;
	}

	private boolean rexp2() {
		if (match("(")) {
			if (rexp())
				if (match(")"))
					return rexp2Tail();
			return false;
		} else if (RE_CHAR()) {
			return rexp2Tail();
		}
		return rexp3();
	}

	private boolean rexp2Tail() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean rexp3() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean charClass() {
		return false;
		// TODO
	}
	
	private boolean charClass1() {
		return false;
		// TODO
	}

	private boolean excludeSet() {
		return false;
		// TODO
	}

	private boolean charSet() {
		return false;
		// TODO
	}

	private boolean definedClass() {
		return false;
		// TODO
	}

	/*
	 * 
	 * 
	 * 
	 * <rexp2-tail> * | + | e <rexp3> <char-class> | e <char-class> . | [
	 * <char-class1> | <defined-class> <char-class1> <char-set-list> |
	 * <exclude-set> <char-set-list> <char-set> <char-set-list> | ] <char-set>
	 * CLS_CHAR <char-set-tail> <char-set-tail> – CLS_CHAR | e <exclude-set> ^
	 * <char-set>] IN <exclude-set-tail> <exclude-set-tail> [<char-set>] |
	 * <defined-class>
	 */

}
