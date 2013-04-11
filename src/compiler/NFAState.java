package compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//DOES NOT WORK
@SuppressWarnings("serial")
public class NFAState implements java.io.Serializable{
	
	
	private ArrayList<ArrayList<NFAState>> nextStates;
	private ArrayList<Character> actions;
	@SuppressWarnings("unused")
	private NFA nfa;
	boolean accept;
	
	public NFAState(NFA nfa, boolean accept) {
		super();
		this.accept = accept;
		this.nextStates = new ArrayList<ArrayList<NFAState>>();
		this.actions = new ArrayList<Character>();
		this.nfa = nfa;
	}

	public NFAState(NFA nfa, ArrayList<Character> actions, boolean accept) {
		super();
		this.nextStates = new ArrayList<ArrayList<NFAState>>();
		this.actions = actions;
		this.accept = accept;
		this.nfa = nfa;
	}
	
	public NFAState(NFA nfa, ArrayList<ArrayList<NFAState>> nextStates, ArrayList<Character> actions, boolean accept) {
		super();
		this.nextStates = nextStates;
		this.actions = actions;
		this.accept = accept;
		this.nfa = nfa;
	}

	public ArrayList<NFAState> getNextStates(char action) {
		if(actions.contains(action))
			return nextStates.get(actions.indexOf(action));
		return null;
	}

	public void addNextStates(char action, NFAState state) {
		if(actions.contains(action)){
			nextStates.get(actions.indexOf(action)).add(state);
	//		if (!nfa.getStateSpace().contains(state)){
		//		nfa.getStateSpace().add(state);
	//		}
		}
		else{
			actions.add(action);
			nextStates.add(new ArrayList<NFAState>());
			nextStates.get(actions.indexOf(action)).add(state);
	//		if (!nfa.getStateSpace().contains(state)){
	//			nfa.getStateSpace().add(state);
	//		}
		}
	}

	public boolean isAccept() {
		return accept;
	}

	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	
	public NFAState copy() throws Exception{
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
	         return (NFAState) ois.readObject();
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
	
	public String toString(){
		return Boolean.toString(accept);
	}
	
}
