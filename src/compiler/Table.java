package compiler;

import java.util.ArrayList;

public class Table {

	private ArrayList<Character> inputs;
	private int states;
	private ArrayList<ArrayList<ArrayList<Integer>>> nStates; //state, input, nStates
	
	@SuppressWarnings("unchecked")
	public Table( int states, ArrayList<Character> inputs, ArrayList<ArrayList<ArrayList<Integer>>> nStates) {
		super();
		this.inputs = (ArrayList<Character>)inputs.clone();
		this.states = states;
		this.nStates = nStates;
		if(!this.inputs.contains((char)238)){ //adds the empty string
			this.inputs.add((char)238);
			for(int i=0;i<this.states;i++){
				this.nStates.get(i).add(new ArrayList<Integer>());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Table( int states, ArrayList<Character> inputs) {
		super();
		this.inputs = (ArrayList<Character>)inputs.clone();
		if(!this.inputs.contains((char)238)) //adds the empty string
			this.inputs.add((char)238);
		this.states = states;
		nStates = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for (int i = 0; i<states;i++){
			nStates.add(new ArrayList<ArrayList<Integer>>());
			for(int j=0;j<this.inputs.size();j++)
				nStates.get(i).add(new ArrayList<Integer>());
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getnStates(int state, char input){
		if (state > states || state < 0 || inputs.indexOf(input) == -1)
			return null;
		return (ArrayList<Integer>) nStates.get(state).get(inputs.indexOf(input)).clone();
		
	}
	
	
	public void setnStates(int state, char input,  ArrayList<Integer> nStates){ //replaces
		if (state > states || state < 0 || inputs.indexOf(input) == -1){
			
		}else{
			 this.nStates.get(state).get(inputs.indexOf(input)).clear();
			 this.nStates.get(state).get(inputs.indexOf(input)).addAll(nStates);
		}
		
	}
	
	public void addnStates(int state, char input,  ArrayList<Integer> nStates){ //adds on a state
		if (state > states || state < 0 || inputs.indexOf(input) == -1){
			
		}else{
			 this.nStates.get(state).get(inputs.indexOf(input)).addAll(nStates);
		}
		
	}
	
	public void addnStates(int state, char input,  int nStates){ //adds on a state
		if (state > states || state < 0 || inputs.indexOf(input) == -1){
			
		}else{
			 this.nStates.get(state).get(inputs.indexOf(input)).add(nStates);
		}
		
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Character> getInputs() {
		return (ArrayList<Character>)inputs.clone();
	}

	public int getStates() {
		int temp = states;
		return temp;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<ArrayList<Integer>>> getAllnStates() {
		return (ArrayList<ArrayList<ArrayList<Integer>>>) nStates.clone();
	}
	
	public void addInput(char input){
		if (!inputs.contains(input))
			inputs.add(input);
		for (int i = 0; i<states;i++){
			nStates.get(i).add(new ArrayList<Integer>());
		}
	}
	
	public void addState(){
		states++;
		nStates.add(new ArrayList<ArrayList<Integer>>());
		for(int j=0;j<inputs.size();j++)
			nStates.get(states-1).add(new ArrayList<Integer>());
	}
	
	public String toString(){
		String s = "";
		s += "   "+this.getInputs().toString() + "\n";
		for(int i=0; i<this.getStates();i++){
			if(i<10)
				s += "  "+Integer.toString(i) +this.getAllnStates().get(i).toString() + "\n";
			else if (i<100)
				s += " "+Integer.toString(i) +this.getAllnStates().get(i).toString() + "\n";
			else
				s += Integer.toString(i) +this.getAllnStates().get(i).toString() + "\n";
		}
		
		return s;
	}
	
}
