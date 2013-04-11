/**
 * 
 */
package compiler;

import java.util.ArrayList;




/**
 * @author Nino
 * 
 */
public class PrimitiveParser {
	
	@SuppressWarnings({ "serial" })
	private static class primitiveError extends Exception {
		primitiveError(String message) {
			super(message);
		}
	}
	

	public ArrayList<NFA> NFAs = new ArrayList<NFA>();

	@SuppressWarnings({ "unchecked" })
	public NFA charClass(String line) throws InterruptedException, primitiveError {
		String id; // the id of the character class
		ArrayList<Character> accept = new ArrayList<Character>(); // everything accepted
		ArrayList<Character> notAccept = new ArrayList<Character>(); // everything not accepted (only used with ^)

		int ipointer = line.indexOf(" ");
		id = line.substring(0, ipointer);
		while (Character.isWhitespace(line.charAt(ipointer)))//skip whitespace
			ipointer++;
		if (line.charAt(ipointer) == '[') {
			ipointer++;
			if (line.charAt(ipointer) == '^') { //// adding to not accept
				notAccept = getBrackets(line.substring(ipointer+1, ipointer = line.lastIndexOf("]")));
				ipointer++;
				while (Character.isWhitespace(line.charAt(ipointer))) //get past the IN
					ipointer++;
				if(line.charAt(ipointer) == 'I' && line.charAt(ipointer+1) == 'N' && Character.isWhitespace(line.charAt(ipointer+2)))
					ipointer += 2;
				else{
					throw new primitiveError("Need IN after a negation");
				}
					
				while (Character.isWhitespace(line.charAt(ipointer)))
					ipointer++;
				
				if (line.charAt(ipointer) == '$'){
					String ccname = line.substring(ipointer);
					if (NFAs.contains(new NFA(ccname, 0))){
						accept = (ArrayList<Character>) NFAs.get(NFAs.indexOf(new NFA(ccname, 0))).getAlphabet().clone();
					}
				}else if (line.charAt(ipointer) == '['){
					accept = getBrackets(line.substring(ipointer, ipointer = line.lastIndexOf(']', ipointer)));
				}

			} else { //////////////////////////// adding to accept
				accept = getBrackets(line.substring(ipointer, ipointer = line.lastIndexOf("]")));
			}
		} else if (line.charAt(ipointer) == '.') {
			for (int i = 32; i<=126;i++)
				accept.add((char)i);
		} else {
			throw new primitiveError("Need [] expression or .");
		}
		

		accept.removeAll(notAccept);
		accept.remove((Character)(char)238);
		NFA newnfa = new NFA(id, 0);
		newnfa.getNfa().addVertex();
		newnfa.getNfa().addAcceptStates(1);
		for (char c : accept){
			newnfa.addEdge(0, 1, c);
		}
		ArrayList<Integer> acceptState = new ArrayList<Integer>();
		acceptState.add(1);
	//	System.out.println(newnfa);
	//	System.out.println(accept);
	//	Thread.sleep(100000);
		return newnfa;

	}

	// evaluates values between brackets
	private ArrayList<Character> getBrackets(String s) throws primitiveError {
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
					throw new primitiveError("bad escaped character");
				}
			}
			if (s.charAt(ipointer + 1) == '-') { // takes care of ranges
				int start = s.charAt(ipointer);
				int end = s.charAt(ipointer + 2);
				if (start > end) { // invalid ranges i.e. (1-0)
					throw new primitiveError("Invalid Range");
					
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

}
