import java.io.IOException;

/**
 * This is a RedBlackBST datastructure that has strings as its key and the ADT States as its values
 * Only functions that were necessary for our use were brought over from the algorithms textbook code
 * and they were modified for our needs.
 * 
 * @author Michael Barreiros, Ben Li
 * @version 3.0
 * Citation: This code is based off of the RedBlackBST found in the Algorithms textbook and is referencing
 * https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/RedBlackBST.java.html
 */
public class RedBlackTree<String extends Comparable<String>, States> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private Node root;
	
	/**
	 * This is a helper node data type for the BST
	 * 
	 * @author Michael Barreiros, Ben Li
	 *
	 */
	private class Node{
		private String key;
		private States state;
		private Node left, right;
		private boolean colour;
		private int size;
		
		/**
		 * The Node constructor
		 * 
		 * @param key A string for the key that identifies the node
		 * @param state The "value" of the node, which is a state ADT
		 * @param colour The colour of the Branch leading to this node, either BLACK or RED
		 * @param size The number of nodes that this node branches to
		 */
		public Node(String key,States state, boolean colour, int size) {
			this.key= key;
			this.state = state;
			this.colour = colour;
			this.size = size;
		}
	}
	/**
	 * Used To initialize an empty symbol table.
	 */
	public RedBlackTree() {}
	
	/******************************************************************************
	 * Node helper methods
	 ******************************************************************************/
	
	/**
	 * A function to return whether of node is Red
	 * 
	 * @param x Node being checked
	 * @return boolean True if x is red, false if x is either null or black
	 */
	private boolean isRed(Node x) {
		if (x == null) return false;
		return x.colour == RED;
	}
	
	/**
	 * A function to return the size of a node
	 * 
	 * @param x Node being checked
	 * @return x.size The size of the node rooted at x. 0 if x is null
	 */
	private int size(Node x) {
		if (x == null) return 0;
		return x.size;
	}
	
	/**
	 * A function to return the size of the whole tree. This uses the previous size but has root as its parameter
	 * 
	 * @return root.size The size of the whole tree. Also the number of key-state
	 * pairs.
	 */
	private int size() {
		return size(root);
	}
	
	/**
	 * A function to return whether the RedBlackBST is empty or not
	 * 
	 * @return boolean True if the symbol table is empty and False otherwise
	 */
	private boolean isEmpty() {
		return root == null;
	}
	
	/******************************************************************************
	 * Standard BST search
	 ******************************************************************************/
	
	/**
	 * A function to return a state when given a key
	 * 
	 * @param key the name of the node we are looking for 
	 * @return States the state that is associated with the key
	 */
	public States get(String key) {
		return get(root, key);
	}
	
	/**
	 * A function to return the state found when searching for a key. if not found return null
	 * 
	 * @param x the root of the subtree we are looking in
	 * @param key the key we are searching for
	 * @return States the state that is associated with the key, null if key not found
	 */
	private States get(Node x, String key) {
		while(x != null) {
			int cmp = key.compareTo(x.key);
			if (cmp < 0) x = x.left;
			else if (cmp > 0) x = x.right;
			else return x.state;
		}
		return null;
	}
	
	
	/**
	 * A function that returns whether or not the BST contains a key, returns true if in the tree. Uses the get function
	 * 
	 * @param key the key we are looking for
	 * @return True if the BST contains the key and False otherwise
	 */
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	/******************************************************************************
	 * Red-black tree insertion.
	 ******************************************************************************/
	
	/**
	 * A function to put the String, State pair onto the BST
	 * 
	 * @param key This is the string "identifier" for the node being placed on the tree
	 * @param state This is the States object that is paired with the string identifier to be added to the BST
	 */
	public void put(String key, States state) {
		
		root = put(root, key, state);
		root.colour = BLACK;
		
	}
	
	/**
	 * A function to put the <String, State> pair onto the BST in the proper position 
	 * 
	 * @param h The node being compared to when finding the correct position of the state being inserted
	 * @param key The key of the state being inserted
	 * @param state The States object of the state being inserted
	 * @return A node that has been built correctly
	 */
	private Node put(Node h, String key, States state) {
		//if node is null then just build a new node in that position
		if (h == null) return new Node(key, state, RED, 1);
		
		int cmp = key.compareTo(h.key);
		
		//find the correct position of the 
		if (cmp < 0) h.left = put(h.left, key, state);
		else if (cmp > 0) h.right = put(h.right, key, state);
		else h.state = state;
		
		//reformat the tree to be a left leaning red black BST
		if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right)) flipColours(h);
		//update the size of the tree
		h.size = size(h.left) + size(h.right) + 1; 
		
		return h;
	}
	
	/******************************************************************************
	 * Red-black tree helper functions
	 ******************************************************************************/
	
	/**
	 * A function to rotate the node given right, this is used to rebuild the tree to be a left leaning red black BST
	 * 
	 * @param h Node being rotated
	 * @return Node after rotations
	 */
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.colour = x.right.colour;
		x.right.colour = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;  
		return x;
	}
	
	/**
	 *  A function to rotate the node given left, this is used to rebuild the tree to be a left leaning red black BST
	 * 
	 * @param h Node being rotated
	 * @return Node after rotations
	 */
	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.colour = x.left.colour;
		x.left.colour = RED;
		x.size = h.size;
		h.size = size(h.left)+ size(h.right) + 1;  
		return x;
	}
	
	/**
	 * A function to flip the colours of a node, this is used to maintain the tree as a left leaning red black BST
	 * 
	 * @param h Node who's colours are to be flipped
	 */
	private void flipColours(Node h) {
		h.colour = !h.colour;
		h.left.colour = !h.left.colour;
		h.right.colour = !h.right.colour;
	}
	
}
