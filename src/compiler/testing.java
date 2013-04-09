package compiler;

import java.util.ArrayList;
import java.util.Collections;
@SuppressWarnings("unused")
public class testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		ArrayList<ArrayList<Integer>> bh = new ArrayList<ArrayList<Integer>>(20);
		ArrayList<Integer> g = new ArrayList<Integer>();
		g.add(1);
		bh.add(new ArrayList<Integer>());
		bh.get(0).addAll(g);
		
		Table table = new Table(6, notaccept);
		
		System.out.println(table.getAllnStates());
	}

}
