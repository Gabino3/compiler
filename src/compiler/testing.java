package compiler;

import java.util.ArrayList;
import java.util.Collections;

import compiler.ParseTree.Node;
@SuppressWarnings("unused")
public class testing {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	//	String s = "] IN $DIGIT ";
	//	int ipointer = 0;
		ArrayList<Character> accept = new ArrayList<Character>();
		accept.add(null);
		accept.add('h');
		//accept.add(9, 'a');
		ArrayList<Character> notaccept = new ArrayList<Character>();
		for (int i = 40; i<=50;i++)
			notaccept.add((char)i);
		notaccept.add(NFA.EPSILON);
	//	print(Boolean.toString(notaccept.contains('+')));
		
		String ss = "1234567890";
		int point = 0;
		String sss = "123";
		point += sss.length();
		//print(Character.toString(ss.charAt(point)));
		
		ArrayList<ArrayList<Integer>> bh = new ArrayList<ArrayList<Integer>>(20);
		ArrayList<Integer> g = new ArrayList<Integer>();
		/*
		NFAState one = new NFAState(false);
		NFAState two = new NFAState(false);
		one.addNextStates(NFA.EPSILON, two);
		NFAState three = one.copy();
		two.setAccept(true);
		
		
		System.out.println(one.getNextStates(NFA.EPSILON));
		System.out.println(two);
		System.out.println(three.getNextStates(NFA.EPSILON));
		
		
		g.add(1);
		bh.add(new ArrayList<Integer>());
		bh.get(0).addAll(g);
		
		*/
		ArrayList<Integer> t = new ArrayList<Integer>();
		t.add(0);
		NFA nfa = new NFA("v", 0);
		
		for (int i = 0; i < 10; i++) {
			nfa.getNfa().addVertex();
		}
		
		nfa.addEdge(0, 7, NFA.EPSILON);
		nfa.addEdge(0, 1, NFA.EPSILON);
		nfa.addEdge(1, 2, NFA.EPSILON);
		nfa.addEdge(1, 4, NFA.EPSILON);
		nfa.addEdge(3, 6, NFA.EPSILON);
		nfa.addEdge(5, 6, NFA.EPSILON);
		nfa.addEdge(6, 1, NFA.EPSILON);
		nfa.addEdge(6, 7, NFA.EPSILON);
		
		nfa.addEdge(2, 3, 'a');
		nfa.addEdge(4, 5, 'b');
		nfa.addEdge(7, 8, 'a');
		nfa.addEdge(8, 9, 'b');
		nfa.addEdge(9, 10,'b');
		
		//print(nfa.getNfa().epClosure(t).toString());
		/*
		g = nfa.getNfa().epsilonClosure(0);
		Collections.sort(g);
		print(g.toString());
		g = nfa.getNfa().epsilonClosure(nfa.getNfa().moveNfa(g, 'a'));
		Collections.sort(g);
		print(g.toString());
		*/
		print(NFA.toDFA(nfa).toString());
		
	}
	
	public static void print(String s){
		System.out.println(s);
	}

}
