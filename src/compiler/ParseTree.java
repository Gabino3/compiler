package compiler;

import java.util.ArrayList;


public class ParseTree<T> {
    private Node<T> root;
    private Node<T> current;

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private ArrayList<Node<T>> children;
        private RDP.Terminal type;
        
        public Node(T data, RDP.Terminal type, Node<T> parent) {
			super();
			this.data = data;
			this.type = type;
			this.parent = parent;
			this.children = new ArrayList<Node<T>>();
		}

		public T getData(){
        	return data;
        }
		
		public RDP.Terminal getType(){
        	return type;
        }
		
		public boolean isType(RDP.Terminal type){
        	return this.type == type;
        }
		
		public void setData(T data){
			this.data = data;
		}
        
		public void setType(RDP.Terminal type){
			this.type = type;
		}
		
    	public void addChild(Node<T> child){
    		this.children.add(child);
    	}
    	
    	public void newParent(Node<T> parent){
    		if (this.parent == null){
    			this.parent = parent;
    			this.parent.addChild(this);
    		}
    		else{
    			Node<T> tempParent = this.parent;
    			this.parent = parent; //makes parent the new parent
    			this.parent.addChild(this); // makes this a child of parent
    			this.parent.parent = tempParent; //makes parent a child of the old parent
    			this.parent.parent.children.remove(this); // makes this NOT a child of the old parent
    			this.parent.parent.addChild(this.parent); // makes new parent a child of old parent
    			
    			System.out.print("");
    		}
    	}
    	
    	public ArrayList<Node<T>> getChildren(){
    		return this.children;
    	}
    	
    	public boolean hasChildren(){
    		return this.children.size() > 0;
    	}
    	
    	public int getNumOfChildren(){
    		return this.children.size();
    	}
    	
    	public Node<T> getParent() {
    		return parent;
    	}
        
    	public String toString(){
    		String s;
    		if(type == RDP.Terminal.ROOT)
    			s = data + " [";
    		else
    			s= type.toString() + " [";
    		
    		for (Node<T> n : children){
    			s+=n.toString();
    		}
    		 s+= "]";
    		
    		return s;
    	}
        
    }  //----------------------------------node
    
    public ParseTree(T rootData) {
        root = new Node<T>(rootData, null, null);
        current = root;
    }

	public Node<T> getRoot() {
		return root;
	}

	public Node<T> getCurrent() {
		return current;
	}
	
	public void setCurrent(Node<T> current) {
		this.current = current;
	}
	
	public boolean rootIsCurrent() {
		return root.equals(current);
	}
	
	public String toString(){
		return root.toString();
	}


}
