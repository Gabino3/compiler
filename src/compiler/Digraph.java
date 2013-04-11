package compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Digraph implements java.io.Serializable{

	  static class Edge implements java.io.Serializable{
	    public final int to;
	    public final char action;
	    
	    public Edge(int to, char action) {
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
	    	
	    	s = "{\'" + action + "\' : " + Integer.toString(to) + "}";
	    	
	    	return s;
	    }
	    
	  }//-----------------------------
	  
	  
	  private int V;
	  private int E;
	  private ArrayList<ArrayList<Edge>> stateSpace; //vert, edge
	  private ArrayList<Integer> acceptStates;
	  



	public Digraph(int v) {
		super();
		V = v;
		this.stateSpace = new ArrayList<ArrayList<Edge>>();
		for(int i=0;i<v;i++)
			this.stateSpace.add(new ArrayList<Edge>());
		acceptStates = new ArrayList<Integer>();
	}


	public ArrayList<Integer> getAcceptStates() {
		return acceptStates;
	}


	public void addAcceptStates(int acceptState) {
		this.acceptStates.add(acceptState);
		
	}

	public void addVertex(){
		V++;
		this.stateSpace.add(new ArrayList<Edge>());
	}

	public int getV() {
		return V;
	}


	public int getE() {
		return E;
	}

	public ArrayList<Edge> getEdges(int from){
		return this.stateSpace.get(from);
	}
	
	public ArrayList<Edge> getEdge(int from, char action){
		ArrayList<Edge> temp = new ArrayList<Edge>();
		for (Edge e : this.stateSpace.get(from))
			if (e.equals((Character)action))
				temp.add(e);
		return temp;
	}

	public boolean addEdge(int from, int to, char action){
		if(from < 0 || from >= V || to < 0 || to >= V){
			System.out.println("Error: addEdge out of bounds");
			return false;
		}
		else{
			stateSpace.get(from).add(new Edge(to, action));
			E++;
			return true;
		}
	  }
	  
	  
	public Digraph copy() throws Exception{
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
	         return (Digraph) ois.readObject();
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
		String s = "";
        s += V + " vertices, " + E + " edges " + "\n";
        s += "Accept States: " + acceptStates.toString() + "\n";
        for (int v = 0; v < V; v++) {
           s += Integer.toString(v) + " : " + stateSpace.get(v).toString();
            s += "\n";
        }
        return s;
	}
	  

}