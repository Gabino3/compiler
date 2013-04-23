package parserLL1;

import java.util.ArrayList;

public class Set {

	ArrayList<ArrayList<String>> terminals;
	ArrayList<String> nonterminals;

	public Set(ArrayList<ArrayList<String>> terminals, ArrayList<String> nonterminals) {
		super();
		this.terminals = terminals;
		this.nonterminals = nonterminals;
	}
	
	public Set() {
		super();
		this.terminals = new ArrayList<ArrayList<String>>();
		this.nonterminals = new ArrayList<String>();
	}
	
	public ArrayList<String> getSetFor(String nonterminal){
		if(nonterminals.contains(nonterminal))
			return terminals.get(nonterminals.indexOf(nonterminal));
		return null;
	}
	
	public void addToSet(String nonterminal, String terminal){
		if(nonterminals.contains(nonterminal)){
			terminals.get(nonterminals.indexOf(nonterminal)).add(terminal);
		}else{
			System.out.println("ERROR: addToSet Failed with adding \""+terminal+"\" to \""+nonterminal+"\"");
		}
	}

	public static Set getFirstSet(String grammer){
		int changed = 1;
		Set firstSet = new Set();
		
		/*
		for each nonterminals A
			First(A):={} 
		while there are changes to any First(A)
			for each production rule X -> X1, X2, ...Xn
				k:=1; 
				while k<=n
					First(A) = First(A) U (First(Xk)-{e}) 
					if e is not in First(Xk)
						break; 
					k := k+1; 
				if ( k>n)
					First(A) = First(A) U {e}
		*/
		return null;	
	}
	
	public static Set getFollowSet(String grammer){
		
		/*
		 
	  	for each nonterminals A
			Follow(A):={$} for start symbol or {} for others
			
		while there are changes to any Follow(A) 
			for each production rule X -> X1 X2	...Xn 
				for each Xi that is a nonterminal 
					Follow(Xi) = Follow(Xi) U First(Xi+1...Xn)-{e} 
					if e is in First(Xi+1...Xn)
						Follow(Xi) = Follow(Xi) U Follow(X) 
		  
		  
		 
		 */
		return null;	
	}
	
	
}
