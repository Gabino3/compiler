package compiler;

import java.util.ArrayList;


public class ParseTree<T> {
    private Node<T> root;

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private ArrayList<Node<T>> children;
        
        
        
        public Node(T data, Node<T> parent) {
			super();
			this.data = data;
			this.parent = parent;
			this.children = new ArrayList<Node<T>>();
		}

		public T getData(){
        	return data;
        }
		
		public void setData(T data){
			this.data = data;
		}
        
    	public void addNode(Node<T> child){
    		this.children.add(child);
    	}
    	
    	public ArrayList<Node<T>> getChildren(){
    		return this.children;
    	}
    	
    	public Node<T> getParent() {
    		return parent;
    	}
        
        
    }  
    
    public ParseTree(T rootData) {
        root = new Node<T>(rootData, null);
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

	public Node<T> getRoot() {
		return root;
	}

	public void addNode(Node<T> parent, Node<T> child){
		parent.children.add(child);
	}
	
	public ArrayList<Node<T>> getChildren(Node<T> node){
		return node.children;
	}
	
	public Node<T> getParent(Node<T> child) {
		return child.parent;
	}


}
