package uk.ac.reading.tom.xw009807;

import java.util.Random;

import uk.ac.reading.tom.xw009807.GenoKey.KEYS;


public class Genotype implements Cloneable {
	
	public Genotype leftNode = null;
	public Genotype rightNode = null;
	public GenoKey key = null;
	
	public Genotype(GenoKey gKey) {
		key = gKey;
	}
	
	@Override
	public Genotype clone() {
		Genotype returnValue = new Genotype(key.clone());
		returnValue.leftNode = leftNode == null ? null : leftNode.clone(); 
		returnValue.rightNode = rightNode == null ? null : rightNode.clone(); 

		return returnValue;
	}
	/**
	 * Constructs Genotype tree of parameter size
	 * @param size of tree
	 */
	public Genotype(int size) {
		key = new GenoKey(true);
		// create all nodes
		for(int i=0;i<size;i++) {
			if(leftNode == null) {
				place(leftNode, this);
			}
			else if(rightNode == null) {
				place(rightNode, this);
			}
			else {
				int left_count = count(leftNode);
				int right_count = count(rightNode);
				if(left_count <= right_count) 
					place(leftNode, this);
				else
					place(rightNode, this);			
			}					
		}
		correctTree(this);
	}
	
	/**
	 * add missing keys to current node if key is an operator
	 * @param genotype
	 */
	public void correctTree(Genotype g) {
		if(g.key.getKey() == KEYS.HEALTH) {
			if(g.leftNode == null) {
			 g.leftNode = new Genotype(new GenoKey(false));
			}
			if(g.rightNode == null) {
				g.rightNode = new Genotype(new GenoKey(false));
			}
		}
		if(g.leftNode != null) {
			correctTree(g.leftNode);
		}
		if(g.rightNode != null) {
			correctTree(g.rightNode);
		}
	}
	
	/**
	 * Attempts to place a Genotype node into the tree
	 * @param genotype
	 * @param previous node
	 */
	private void place(Genotype g, Genotype previous) {
		if(g == null) {
			g = new Genotype(new GenoKey(true));
			if(previous.leftNode == null) {
				previous.leftNode = g;
			}
			else
				previous.rightNode = g;
			return;
		}
		if(g.leftNode == null)
			place(g.leftNode, g);
		else if(g.rightNode == null)
			place(g.rightNode, g);
		else {
			int left_count = count(g.leftNode);
			int right_count = count(g.rightNode);
			if(left_count <= right_count)
				place(g.leftNode, g);
			else
				place(g.rightNode, g);
		}
	}
	
	/**
	 * Recursive count of nodes in tree
	 * @param g
	 * @return length of tree
	 */
	private int count(Genotype g) {
		int length = 1;
		if(g.leftNode == null) 
			length += count(g.leftNode);
		if(g.rightNode == null)
			length += count(g.rightNode);
		return length;
	}
	
	/**
	 * get length
	 * @return length
	 */
	public int length() { 
 		return count(this); 
 	} 

	
	/**
	 * Swap 2 given nodes
	 * @param g
	 * @param p1
	 * @param p2
	 */
	public void swap(Genotype g, int p1, int p2) {
		// get node positions of intended swap
		Genotype[] position1 = getPosition(this, new int[] {1, p1});
		Genotype[] position2 = getPosition(g, new int[] {1, p2});
		Genotype temp = position1[0].clone();
		
		// swap parameter nodes
		position1[0].leftNode = position2[0].leftNode;
		position1[0].rightNode = position2[0].rightNode;
		position1[0].key = position2[0].key;
		
		//set second node parameter with temporary stored data
		position2[0].leftNode = temp.leftNode;
		position2[0].rightNode = temp.rightNode;	
	}
	
	/**
	 * Gets Genotype at given position 
	 * @param g
	 * @param currentPosition
	 * @return Genotype
	 */
	public Genotype[] getPosition(Genotype g, int[] currentPosition) {
		if(currentPosition[0] == currentPosition[1])
			return new Genotype[] {g};
		if(g.leftNode != null) {
			currentPosition[0] += 1;
			Genotype[] temp = getPosition(g.leftNode, currentPosition);
			return temp;
		}
		if(g.rightNode != null) {
			currentPosition[0] += 1;
			Genotype[] temp = getPosition(g.rightNode, currentPosition); 
			return temp;
		}
		return null;
	}
	
	public Genotype(String s) { 
 		decodeString(this, new String[] { s }); 
 	} 

	/**
	 * Decode Genotype string
	 * @param g
	 * @param s
	 * @return Genotype
	 */
	public Genotype decodeString(Genotype g, String[] s) {
		char in = s[0].charAt(0);
		if(in == ';') {
			s[0] = s[0].substring(1);
			return null;
		}
		int key = Integer.parseInt(s[0].substring(0, 2));
		int amount = Integer.parseInt(s[0].substring(2, 4));
		if(g != null)
			g.key = new GenoKey(GenoMap.get(key), amount);
		else
			g = new Genotype(new GenoKey(GenoMap.get(key), amount));
		s[0] = s[0].substring(4);
		g.leftNode = decodeString(g.leftNode, s);
		g.rightNode = decodeString(g.rightNode,s);
		
		return g;
	}
	
	public String getStorageString() { 
 		StringBuilder sb = new StringBuilder(); 
 		buildStorageString(this, sb); 
 		return sb.toString();
 	} 
	
	private void buildStorageString(Genotype g, StringBuilder sb) { 
 		if (g == null) { 
 			sb.append(";"); 
 			return; 
 		} 
 		sb.append(String.format("%02d", GenoMap.get(g.key.getKey())) 
 				+ String.format("%02d", g.key.getPower())); 
 		buildStorageString(g.leftNode, sb); 
 		buildStorageString(g.rightNode, sb); 
 	} 

	public void mutate() {
		tryMutate(this);
	}
	
	private void tryMutate(Genotype g) {
		if(randomNum(1, 100) <= TB_Robot.mutationWeight)
			g.key = new GenoKey(true);
		if(g.leftNode != null)
			tryMutate(g.leftNode);
		if(g.rightNode != null)
			tryMutate(g.rightNode);
	}
	
	private int randomNum(int low, int high) {
		Random r = new Random();
		int result = r.nextInt(high-low) + low;
		return result;
	}
}
