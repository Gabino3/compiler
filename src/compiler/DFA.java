package compiler;

import java.util.ArrayList;

public class DFA {
	static final char EPSILON = (char)238;
	private String id;
	private Digraph dfa;
	private ArrayList<ArrayList<Integer>> states;
	private ArrayList<Integer>  currentState;
	private ArrayList<Integer>  startState;
	private ArrayList<Character> alphabet;
	private ArrayList<Integer> marked; //0 - unmarked, 1 - marked
	private ArrayList<ArrayList<Character>> edges;
	 
	

	public ArrayList<ArrayList<Character>> getEdges() {
		return edges;
	}


	public void setEdges(ArrayList<ArrayList<Character>> edges) {
		this.edges = edges;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Digraph getDfa() {
		return dfa;
	}


	public void setDfa(Digraph dfa) {
		this.dfa = dfa;
	}


	public ArrayList<ArrayList<Integer>> getStates() {
		return states;
	}


	public void setStates(ArrayList<ArrayList<Integer>> states) {
		this.states = states;
	}


	public ArrayList<Integer> getCurrentState() {
		return currentState;
	}


	public void setCurrentState(ArrayList<Integer> currentState) {
		this.currentState = currentState;
	}


	public ArrayList<Integer> getStartState() {
		return startState;
	}


	public void setStartState(ArrayList<Integer> startState) {
		this.startState = startState;
	}


	public ArrayList<Integer> getMarked() {
		return marked;
	}


	public void setMarked(ArrayList<Integer> marked) {
		this.marked = marked;
	}


	public ArrayList<Character> getAlphabet() {
		return alphabet;
	}


	public void setAlphabet(ArrayList<Character> alphabet) {
		this.alphabet = alphabet;
	}
	
	public DFA(String id, ArrayList<Integer> startState, Digraph stateSpace, ArrayList<Character> alphabet) {
		super();
		this.id = id;
		this.startState = startState;
		this.currentState = startState;
		dfa = stateSpace;
		this.alphabet = alphabet;
		this.states = new ArrayList<ArrayList<Integer>>();
		//populate states with startState
		states.add(startState);
		this.marked = new ArrayList<Integer>();
		//set state 0 to unmarked
		marked.add(0);
		this.edges = new ArrayList<ArrayList<Character>>();
	}


	public DFA(String id, ArrayList<Integer>  startState) {
		super();
		this.id = id;
		this.startState = startState;
		this.currentState = startState;
		this.dfa = new Digraph(1);
		this.alphabet = new ArrayList<Character>();
		this.alphabet.add(NFA.EPSILON);
		this.states = new ArrayList<ArrayList<Integer>>();
		//populate states with startState
		states.add(startState);
		this.marked = new ArrayList<Integer>();
		//set state 0 to unmarked
		marked.add(0);
		this.edges = new ArrayList<ArrayList<Character>>();
	}


	public boolean hasUnmarked(){
		return getMarked().contains(0);
	}
	
	public int unmarkedIndex(){
		return getMarked().indexOf(0);
	}
	
	//add all unique Integer objects from b into a  
	public void addUnique(ArrayList<Integer> a, ArrayList<Integer> b){
		for (int index =0; index< b.size() ; index++){
			if ( !(a.contains(b.get(index))) ){
				a.add(b.get(index));
			}
		}
	}


	public void addUniqueState(ArrayList<Integer> newState) {
		if (!(getStates().contains(newState))) {
			getStates().add(newState);
			getMarked().add(0);
		}
	}

	//adds edge to edges. With inialVertex as index of edges, destinationVertex as index of ArrayList<Characters> 
	//and the character a, ie. (State initialVertex, action a) -> State destinationVertex
	public void addEdge(Integer initialVertex, Character a, Integer destinationVertex ) {
		ArrayList<Character> action = new ArrayList<Character>() ;
		action.add(destinationVertex, a);
		getEdges().add(initialVertex,action);
		
	}
}








