/**
 * 
 */
package compiler;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author Nino
 *
 */
public class NFA {
	private String id;
	Table table; //state, input, nextStates
	int startState;
	int currentState;
	ArrayList<Integer> acceptStates;
	
	
	
	public NFA(String id, Table table, int startState, ArrayList<Integer> acceptStates) {
		super();
		this.id = id;
		this.table = table;
		this.startState = startState;
		this.currentState = startState;
		this.acceptStates = acceptStates;
			
	}
	
	public NFA union(String id, NFA nfa){
		return union(id, nfa.getTable(), nfa.getStartState(), nfa.getAcceptStates());
	}
	
	public NFA union(String id, Table table, int startState, ArrayList<Integer> acceptStates){
		
		ArrayList<Integer> newAcceptStates = new ArrayList<Integer>(); //gets new accept states (this then other)
		newAcceptStates.addAll(this.getAcceptStates());
		for (int i=0;i<acceptStates.size();i++){
			newAcceptStates.add(acceptStates.get(i)+this.getTable().getStates());
		}
		
		int newStateSpace = this.getTable().getStates() + table.getStates() + 1; //gets the number of states needed
		
		ArrayList<Character> inputs = new ArrayList<Character>();
		inputs.addAll(this.table.getInputs());//gets the alphabet from both tables and removes duplicates
		inputs.addAll(table.getInputs());
		Collections.sort(inputs);
		for (int i=0;i+1<inputs.size();i++)
			if((int)inputs.get(i) == (int)inputs.get(i+1))
				inputs.remove(i--);
			
	
		Table newTable = new Table(newStateSpace, inputs);
		
		for(int i=0;i<this.getTable().getStates();i++){
			for(int j=0;j<this.getTable().getInputs().size();j++){
				if (!this.getTable().getnStates(i, this.getTable().getInputs().get(j)).isEmpty()){
					newTable.addnStates(i, this.getTable().getInputs().get(j), this.getTable().getnStates(i, this.getTable().getInputs().get(j)));//add table this
				}
			}
			
		}
		
		for(int i=0;i<table.getStates();i++){
			for(int j=0;j<table.getInputs().size();j++){
				if (!table.getnStates(i, table.getInputs().get(j)).isEmpty()){
					newTable.addnStates(i+this.getTable().getStates(), table.getInputs().get(j), addToAllInList(this.getTable().getStates(), table.getnStates(i, table.getInputs().get(j))) );//add table other
				}
			}
			
		}
		ArrayList<Integer> newStartState = new ArrayList<Integer>();
		newStartState.add(startState+this.getTable().getStates());
		newStartState.add(this.getStartState());
		newTable.addnStates(newTable.getStates()-1, (char)238, newStartState); //new start state
		
		
		NFA unioned = new NFA(id, newTable, newTable.getStates()-1, newAcceptStates);
		return unioned;
	}
	
	//TODO star()
	public NFA star(){
		return null;
		
	}
	//TODO plus()
	//TODO concat(other)
	//TODO toDFA()
	
	private ArrayList<Integer> addToAllInList(int adder, ArrayList<Integer> addon){
			ArrayList<Integer> summed= new ArrayList<Integer>();
			for(int i=0;i<addon.size();i++){
				summed.add(addon.get(i)+adder);
			}
		return summed;
	}

	public int getStartState() {
		return startState;
	}

	public void setStartState(int startState) {
		this.startState = startState;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public ArrayList<Integer> getAcceptStates() {
		return acceptStates;
	}

	public void setAcceptStates(ArrayList<Integer> acceptStates) {
		this.acceptStates = acceptStates;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	@Override
	public boolean equals(Object other){
		if( other instanceof NFA)
			return this.getId().equals(((NFA) other).getId());
		return false;
	}
	
	public String toString(){
		return id +" \n "+ table.toString();
		
	}
	

}
