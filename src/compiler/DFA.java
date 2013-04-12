package compiler;

import java.util.ArrayList;

public class DFA {
	private String id;
	private Digraph dfa;
	int E;
	private ArrayList<ArrayList<Integer>> states;
	private ArrayList<Integer>  currentState;
	private ArrayList<ArrayList<Integer>> acceptStates;
	private ArrayList<Integer>  startState;
	private ArrayList<Character> alphabet;
	private ArrayList<Integer> marked; //0 - unmarked, 1 - marked
	private ArrayList<ArrayList<Edge>> edges;
	 
	@SuppressWarnings("serial")
	static class Edge implements java.io.Serializable{
	    public final ArrayList<Integer> to;
	    public final char action;
	    
	    
	    public Edge(ArrayList<Integer> to, char action) {
	      this.to = to;
	      this.action = action;
	    }
	    @Override
	    public boolean equals(Object obj) {
	    	if (obj instanceof Edge){
	    		Edge e = (Edge)obj;
	    		return e.to == to && e.action == action;
	    	}
	    	else if(obj instanceof Character){
	    		return (int)action == (int)((Character)obj);
	    	}
	    	return false;
	    }
	    
	    public String toString(){
	    	String s = "";
	    	
	    	s = "{\'" + action + "\' : " + to.toString() + "}";
	    	
	    	return s;
	    }
	    
	  }//-----------------------------

	
	public ArrayList<Integer> move(char action){
		for (Edge edge : getEdges().get(states.indexOf(currentState))){
			if (edge.action == action){
				currentState = edge.to;
				return edge.to;
			}
		}
		return null;
	}
	
	public boolean canMove(char action){
		for (Edge edge : getEdges().get(states.indexOf(currentState))){
			if (edge.action == action){
				return !edge.to.equals(new ArrayList<Integer>());
			}
		}
		return false;
	}
	
	public boolean isAccept(){
		return acceptStates.contains(currentState);
	}
	
	public ArrayList<ArrayList<Edge>> getEdges() {
		return edges;
	}


	public void setEdges(ArrayList<ArrayList<Edge>> edges) {
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


	public ArrayList<ArrayList<Integer>> getAcceptStates() {
		return acceptStates;
	}


	public void setAcceptStates(ArrayList<ArrayList<Integer>> acceptStates) {
		this.acceptStates = acceptStates;
	}
	
	public void addAcceptStates(ArrayList<Integer> acceptState) {
		if (!(getAcceptStates().contains(acceptState))) {
			getAcceptStates().add(acceptState);
		}
		
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
		this.edges = new ArrayList<ArrayList<Edge>>();
		this.edges.add(new ArrayList<Edge>());
		this.acceptStates = new ArrayList<ArrayList<Integer>>();
		E = 0;
	}


	public DFA(String id, ArrayList<Integer>  startState) {
		super();
		this.id = id;
		this.startState = startState;
		this.currentState = startState;
		this.dfa = new Digraph(1);
		this.alphabet = new ArrayList<Character>();
		this.states = new ArrayList<ArrayList<Integer>>();
		//populate states with startState
		states.add(startState);
		this.marked = new ArrayList<Integer>();
		//set state 0 to unmarked
		marked.add(0);
		this.edges = new ArrayList<ArrayList<Edge>>();
		this.edges.add(new ArrayList<Edge>());
		this.acceptStates = new ArrayList<ArrayList<Integer>>();
		E = 0;
	}


	public boolean hasUnmarked(){
		return getMarked().contains(0);
	}
	
	public int unmarkedIndex(){
		return getMarked().indexOf(0);
	}
	
	//add all unique Integer objects from b into a  
	public static void addUnique(ArrayList<Integer> a, ArrayList<Integer> b){
		for (int index =0; index < b.size() ; index++){
			if ( !(a.contains(b.get(index))) ){
				a.add(b.get(index));
			}
		}
	}


	public void addUniqueState(ArrayList<Integer> newState) {
		if (!(getStates().contains(newState))) {
			getStates().add(newState);
			getMarked().add(0);
			getEdges().add(new ArrayList<Edge>());
		}
	}

	//adds edge to edges. With inialVertex as index of edges, destinationVertex as index of ArrayList<Characters> 
	//and the character a, ie. (State initialVertex, action a) -> State destinationVertex
	public void addEdge(int initialVertex, Character a, ArrayList<Integer> destinationVertex ) {
		
		getEdges().get(initialVertex).add(new Edge(destinationVertex, a));
		E++;
		
	}
	
	public String toString(){
		return id +"\nStates " +Integer.toString(getStates().size()) + "\nEdges " + Integer.toString(E) + 
				"\nAlpha Size " + Integer.toString(this.alphabet.size()) +"\n" + Integer.toString(this.alphabet.size()*getStates().size()) +
						" \n acceptStates" + acceptStates.size();
	}
	
	
}








