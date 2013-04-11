package compiler;

import java.util.ArrayList;
import java.util.Collections;
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
		print(Boolean.toString(notaccept.contains('+')));
		
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
		ArrayList<ArrayList<Integer>> t = new ArrayList<ArrayList<Integer>>(10);
		//System.out.println(t);
		Table table = new Table(6, notaccept);
		
		Digraph d = new Digraph(3);
		d.addEdge(0, 1, NFA.EPSILON);
		d.addEdge(0, 2, 'h');
		d.addEdge(1, 2, '1');
		
		Digraph b = d.copy();
		
		b.addEdge(2, 1, 'h');
		
		//print(d.toString());
		System.out.println(d.toString());
		Table table2 = new Table(table.getStates(), table.getInputs());
		table2.addState();
		table2.addState();
		table2.addState();
		table2.addState();
		table2.addInput('h');
		
		
		//System.out.println(table);
		//System.out.println(table2);
	}
	
	public static void print(String s){
		System.out.println(s);
	}

}
