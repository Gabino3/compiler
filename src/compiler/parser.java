/**
 * 
 */
package compiler;

import java.io.*;
import java.util.ArrayList;


/**
 * @author Nino
 * 
 */
public class parser {

	public ArrayList<NFA> NFAs = new ArrayList<NFA>();

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		FileReader fr = new FileReader("input\\SampleSpec");
		BufferedReader br = new BufferedReader(fr);
		String s;
		parser parse = new parser();
		while ((s = br.readLine()) != null && !s.equals("")) {

			parse.NFAs.add(parse.charClass(s.trim()));
			
		}
		System.out.println(parse.NFAs);
		
		while ((s = br.readLine()) != null) {

			//System.out.println(s);
		}

		fr.close();

		/*
		 * 
		 * parse.charClass(); parse.regex();
		 */
	}

	private NFA charClass(String line) throws InterruptedException {
		String id; // the id of the character class
		ArrayList<Character> accept = new ArrayList<Character>(); // everything accepted
		ArrayList<Character> notAccept = new ArrayList<Character>(); // everything not accepted (only used with ^)

		int ipointer = line.indexOf(" ");
		id = line.substring(0, ipointer++);
		if (line.charAt(ipointer) == '[') {
			ipointer++;
			if (line.charAt(ipointer) == '^') { //// adding to not accept
				notAccept = getBrackets(line.substring(ipointer+1, ipointer = line.indexOf("]")));
				ipointer += 5;
				if (line.charAt(ipointer) == '$'){
					String ccname = line.substring(ipointer);
					if (NFAs.contains(new NFA(ccname, null, 0, null))){
						accept = NFAs.get(NFAs.indexOf(new NFA(ccname, null, 0, null))).getTable().getInputs();
					}
				}else if (line.charAt(ipointer) == '['){
					accept = getBrackets(line.substring(ipointer, ipointer = line.indexOf(']', ipointer)));
				}else {
					System.out.println("error need \" IN \" statement");
				}

			} else { //////////////////////////// adding to accept
				accept = getBrackets(line.substring(ipointer, ipointer = line.indexOf("]")));
			}
		} else if (line.charAt(ipointer) == '.') {
			for (int i = 32; i<=126;i++)
				accept.add((char)i);
		} else {
			System.out.println("error: need [] or .");
		}
		

		accept.removeAll(notAccept);
		accept.remove((Character)(char)238);	
		Table newTable = new Table(2, accept);
		for (int i=0; i<accept.size();i++){
			newTable.addnStates(0, accept.get(i), 1);
		}
		ArrayList<Integer> acceptState = new ArrayList<Integer>();
		acceptState.add(1);
	//	System.out.println(newTable);
	//	System.out.println(accept);
	//	Thread.sleep(100000);
		return new NFA(id, newTable, 0, acceptState);

	}

	// evaluates values between brackets
	private ArrayList<Character> getBrackets(String s) {
		ArrayList<Character> chars = new ArrayList<Character>();
		int ipointer = 0;
		while (ipointer + 1 < s.length()) {
			if (s.charAt(ipointer) == '\\') { // takes care of escaped characters
				ipointer++;
				switch (s.charAt(ipointer)) {
				case '-':
					if (!chars.contains('-'))
						chars.add('-');
					break;
				case '\\':
					if (!chars.contains('\\'))
						chars.add('\\');
					break;
				case '^':
					if (!chars.contains('^'))
						chars.add('^');
					break;
				case '[':
					if (!chars.contains('['))
						chars.add('[');
					break;
				case ']':
					if (!chars.contains(']'))
						chars.add(']');
					break;
				default:
					System.out.println("error: bad escaped character");
				}
			}
			if (s.charAt(ipointer + 1) == '-') { // takes care of ranges
				int start = s.charAt(ipointer);
				int end = s.charAt(ipointer + 2);
				if (start > end) { // invalid ranges i.e. (1-0)
					System.out.println("error: invalid range");
					return null;
				}
				for (; start <= end; start++) {
					if (!chars.contains((char) start))
						chars.add((char) start);
				}
			} else { // takes care of single characters
				if (!chars.contains(s.charAt(ipointer)) && s.charAt(ipointer) != '\\' && s.charAt(ipointer) != '^' && s.charAt(ipointer) != '-' && s.charAt(ipointer) != '[' && s.charAt(ipointer) != ']')
					chars.add(s.charAt(ipointer));
			}
			ipointer++;

		}
		if (ipointer < s.length()) { // if the last char is not an escaped char
										// this makes sure it is added to the
										// list
			if (!chars.contains(s.charAt(ipointer)))
				chars.add(s.charAt(ipointer));
		}

		return chars;
	}

	// //////////////recursive decent parser section /////////////////////

	private boolean match(String token) {
		// TODO Auto-generated method stub
		return false;

	}

	private boolean RE_CHAR() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unused")
	private boolean regex() {
		return rexp();

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
