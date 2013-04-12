/**
 * 
 */
package compiler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import compiler.Digraph.Edge;
@SuppressWarnings("serial")
public class NFA  implements java.io.Serializable{
	private String id;
	private Digraph nfa;
	private ArrayList<Integer> currentState;
	private int startState;
	static final char EPSILON = (char)238;
	private ArrayList<Character> alphabet;
	
	public ArrayList<Character> getAlphabet() {
		return alphabet;
	}


	public void setAlphabet(ArrayList<Character> alphabet) {
		this.alphabet = alphabet;
	}


	public NFA(String id, int startState, Digraph stateSpace, ArrayList<Character> alphabet) {
		super();
		this.id = id;
		this.startState = startState;
		this.currentState = new ArrayList<Integer>();
		this.currentState.add(startState);
		nfa = stateSpace;
		this.alphabet = alphabet;
		
	}


	public NFA(String id, int startState) {
		super();
		this.id = id;
		this.startState = startState;
		this.currentState = new ArrayList<Integer>();
		this.currentState.add(startState);
		this.nfa = new Digraph(1);
		this.alphabet = new ArrayList<Character>();
		this.alphabet.add(NFA.EPSILON);
		
	}
	
	public static NFA union(String id, NFA nfa1, NFA nfa2) throws Exception{
		
		if(nfa1 == null)
			return nfa2.copy();
		if(nfa2 == null)
			return nfa1.copy();
		
		Digraph newD = new Digraph(nfa1.nfa.getV() + nfa2.nfa.getV() + 1);
		//----------------------------get edges
		for (int v=0;v<nfa1.nfa.getV();v++){
			for (Edge e : nfa1.nfa.getEdges(v))
				newD.addEdge(v, e.to, e.action);
		}
		
		for (int v=0;v<nfa2.nfa.getV();v++){
			for (Edge e : nfa2.nfa.getEdges(v))
				newD.addEdge(v+nfa1.nfa.getV(), e.to+nfa1.nfa.getV(), e.action);
		}
		//-----------------------------get accept states
		for (int i : nfa1.nfa.getAcceptStates()){
			newD.addAcceptStates(i, nfa1.nfa.getAcceptStateIds().get(nfa1.nfa.getAcceptStates().indexOf((Integer)i)));
		}
		
		for (int i : nfa2.nfa.getAcceptStates()){
			newD.addAcceptStates(i+nfa1.nfa.getV(), nfa2.nfa.getAcceptStateIds().get(nfa2.nfa.getAcceptStates().indexOf((Integer)i)));
		}
		//-----------------------------new edges for old start states from new start state
		newD.addEdge(newD.getV()-1, nfa1.startState, NFA.EPSILON);
		newD.addEdge(newD.getV()-1, nfa2.startState + nfa1.nfa.getV(), NFA.EPSILON);
		
		//-----------------------------get alphabet
		ArrayList<Character> newAlpha = new ArrayList<Character>();
		for(char c : nfa1.alphabet){
			newAlpha.add(c);
		}
		
		for(char c : nfa2.alphabet){
			if(!newAlpha.contains(c))
				newAlpha.add(c);
		}
		
		return new NFA(id, newD.getV()-1, newD, newAlpha);
	}
	
	public static NFA concat(String id, NFA nfa1, NFA nfa2){
		
		Digraph newD = new Digraph(nfa1.nfa.getV() + nfa2.nfa.getV());
		//----------------------------get edges
		for (int v=0;v<nfa1.nfa.getV();v++){
			for (Edge e : nfa1.nfa.getEdges(v))
				newD.addEdge(v, e.to, e.action);
		}
		
		for (int v=0;v<nfa2.nfa.getV();v++){
			for (Edge e : nfa2.nfa.getEdges(v))
				newD.addEdge(v+nfa1.nfa.getV(), e.to+nfa1.nfa.getV(), e.action);
		}
		//-----------------------------get accept states		
		for (int i : nfa2.nfa.getAcceptStates()){
			newD.addAcceptStates(i+nfa1.nfa.getV(), id);
		}
		//-----------------------------new edges from nfa1 accept states to nfa2 start state
		for (int i : nfa1.nfa.getAcceptStates()){
			newD.addEdge(i, nfa2.startState+nfa1.nfa.getV(), NFA.EPSILON);
		}
		
		//-----------------------------get alphabet
				ArrayList<Character> newAlpha = new ArrayList<Character>();
				for(char c : nfa1.alphabet){
					newAlpha.add(c);
				}
				
				for(char c : nfa2.alphabet){
					if(!newAlpha.contains(c))
						newAlpha.add(c);
				}
		
		
		return new NFA(id, nfa1.startState, newD, newAlpha);
	}
	
	public static NFA star(String id, NFA nfa){
		
		Digraph newD = new Digraph(nfa.nfa.getV()+1);
		//----------------------------get edges
		for (int v=0;v<nfa.nfa.getV();v++){
			for (Edge e : nfa.nfa.getEdges(v))
				newD.addEdge(v, e.to, e.action);
		}
		
		//-----------------------------get accept states and make edges from accept to old start		
		for (int i : nfa.nfa.getAcceptStates()){
			newD.addAcceptStates(i, id);
			newD.addEdge(i, nfa.startState, NFA.EPSILON);
		}
		
		//-----------------------------make new start state that is accept state the epsilons to old start state
		newD.addAcceptStates(newD.getV()-1, id);
		newD.addEdge(newD.getV()-1, nfa.startState, NFA.EPSILON);
		
		
		return new NFA(id, newD.getV()-1, newD, nfa.alphabet);
	}

	
	public static NFA plus(String id, NFA nfa){
		
		Digraph newD = new Digraph(nfa.nfa.getV());
		//----------------------------get edges
		for (int v=0;v<nfa.nfa.getV();v++){
			for (Edge e : nfa.nfa.getEdges(v))
				newD.addEdge(v, e.to, e.action);
		}
		
		//-----------------------------get accept states and make edges from accept to start		
		for (int i : nfa.nfa.getAcceptStates()){
			newD.addAcceptStates(i, id);
			newD.addEdge(i, nfa.startState, NFA.EPSILON);
		}
		

		
		return new NFA(id, nfa.startState, newD, nfa.alphabet);
	}

	public void addEdge(int from, int to, char action){
		if(nfa.addEdge(from, to, action))
			if(!this.alphabet.contains(action))
				this.alphabet.add(action);
		
	}

	public ArrayList<Integer> getCurrentState() {
		return currentState;
	}


	public void setCurrentState(ArrayList<Integer> currentState) {
		this.currentState = currentState;
	}


	public String getId() {
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public Digraph getNfa() {
		return nfa;
	}


	public int getStartState() {
		return startState;
	}
	
	@Override
	public boolean equals(Object other){
		if( other instanceof NFA)
			return this.getId().equals(((NFA) other).getId());
		else if(other instanceof String)
			return this.getId().equals(other);
		return false;
	}
	
	public String toString(){
		return id + "\nAlphabet: "+ alphabet.toString() + "\nStart: "+Integer.toString(startState)+ " \n "+ nfa.toString();
		
	}
	
	public NFA copy() throws Exception{
	      ObjectOutputStream oos = null;
	      ObjectInputStream ois = null;
	      try
	      {
	         ByteArrayOutputStream bos = new ByteArrayOutputStream();
	         oos = new ObjectOutputStream(bos); 
	         // serialize and pass the object
	         oos.writeObject(this);   
	         oos.flush();               
	         ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray()); 
	         ois = new ObjectInputStream(bin);         
	         // return the new object
	         return (NFA) ois.readObject();
	      }
	      catch(Exception e)
	      {
	         System.out.println("Exception in ObjectCloner = " + e);
	         throw(e);
	      }
	      finally
	      {
	         oos.close();
	         ois.close();
	      }
	   }
		
	//takes NFA and returns corresponding DFA
	public static DFA toDFA(NFA nfa){
		
		ArrayList<Integer> dfaStartState = new ArrayList<Integer>(); //getting start state
		dfaStartState = nfa.nfa.epsilonClosure(nfa.getStartState());
		Collections.sort(dfaStartState);
		
		DFA dfa = new DFA(nfa.id, dfaStartState, nfa.nfa, nfa.alphabet);
		dfa.getAlphabet().remove((Character)NFA.EPSILON);
		
		while (dfa.hasUnmarked()){
			//get next unmarked
			int stateIndex = dfa.getMarked().indexOf(0);
			//mark it
			dfa.getMarked().set( stateIndex, 1);
			
			//for each a in alphabet do eClosure(move(T,a))
			for (Character a : dfa.getAlphabet() ){
				//get MOVEdfa(state, a)
				ArrayList<Integer> newState =  new ArrayList<Integer>();
				newState = nfa.nfa.moveDfa(dfa.getStates().get(stateIndex), a);
				Collections.sort(newState);
				
				//if S is not in dfa.states add it and have it as unmarked
				dfa.addUniqueState(newState);
				
				//create edge between T and S ie.move(T,a) to S
				dfa.addEdge(stateIndex,a,newState);
				
				
				/*
				ArrayList<Integer> newState =  new ArrayList<Integer>();
				
				//S = eClosure(move(T,a))
				for (Integer vertex : dfa.getStates().get(stateIndex)){
					DFA.addUnique(newState,(dfa.getDfa().epsilonClosure(vertex)));
				}
								
				//if S is not in dfa.states add it and have it as unmarked
				dfa.addUniqueState(newState);
				
				//create edge between T and S ie.move(T,a) to S
				//dfa.addEdge(,a,);
				*/
				

			}
			
		}
		
		for (ArrayList<Integer> state : dfa.getStates()){
			
			for (Integer accept : nfa.nfa.getAcceptStates()){
				if (state.contains(accept)){
					dfa.addAcceptStates(state);
				}
			}
			
		}
		
		
		return dfa;
		
	}
	
	
	
}

