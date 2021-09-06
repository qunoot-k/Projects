import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class BTree<Key extends Comparable<Key>, Value> {
// order of BTree is 4
	private static final int M = 4;
	private Node root; // root of the B-tree
	private int height; // height of the B-tree
	private int numOfKeyValuePairs; // number of key-value pairs in the B-tree
// helper B-tree node data type

	private static final class Node {
		private int numOfEntery; // number of children
		private Entry[] entryInEachNode = new Entry[M]; // the array of
		children

// create a node with k children
private Node(int k) {
numOfEntery = k;
}
	}

// internal nodes: only use key and next
// external nodes: only use key and value
	private static class Entry {
		private Comparable key;
		private final Object val;
		private Node next; // helper field to iterate over array entries

		public Entry(Comparable key, Object val, Node next) {
			this.key = key;
			this.val = val;
			this.next = next;
		}
	}

	/**
	 * Initializes an empty B-tree.
	 */
	public BTree() {
		root = new Node(0);
	}

	/**
	 * Returns the number of key-value pairs
	 */
	public int getNumOfKeyValuePairs() {
		return numOfKeyValuePairs;
	}

	/**
	 * Returns the height of this B-tree (for debugging).
	 */
	public int getHeight() {
		return height;
	}

//Returns the value associated with the given key.
public Value get(Key key) {
if (key == null)
throw new IllegalArgumentException("argument to get() is 
null");
return search(root, key, height);
}

	private Value search(Node root, Key key, int height) {
		Entry[] children = root.entryInEachNode;
// external node
		if (height == 0) {
			for (int j = 0; j < root.numOfEntery; j++) {
				if (eq(key, children[j].key))
					return (Value) children[j].val;
			}
		}
// internal node
		else {
			for (int j = 0; j < root.numOfEntery; j++) {
				if (j + 1 == root.numOfEntery || less(key, children[j + 1].key))
					return search(children[j].next, key, height - 1);
			}
		}
		return null;
	}

	/**
	 * Inserts the key-value pair , overwriting the old value with the new value if
	 * the key is already there.
	 */
	public void put(Key key, Value val) {
		if (key == null)
			throw new IllegalArgumentException("argument key to put() is null");
		Node u = insert(root, key, val, height);
		numOfKeyValuePairs++;
		if (u == null)
			return;
// need to split root
		Node t = new Node(2);
		t.entryInEachNode[0] = new Entry(root.entryInEachNode[0].key, null, root);
		t.entryInEachNode[1] = new Entry(u.entryInEachNode[0].key, null, u);
		root = t;
		height++;
	}

	private Node insert(Node root, Key key, Value val, int height) {
		int j;
		Entry t = new Entry(key, val, null);
// external node
		if (height == 0) {
			for (j = 0; j < root.numOfEntery; j++) {
				if (less(key, root.entryInEachNode[j].key))
					break;
			}
		}
// internal node
		else {
			for (j = 0; j < root.numOfEntery; j++) {
				if ((j + 1 == root.numOfEntery) || less(key, root.entryInEachNode[j + 1].key)) {
					Node u = insert(root.entryInEachNode[j++].next, key, val, height - 1);
					if (u == null)
						return null;
					t.key = u.entryInEachNode[0].key;
					t.next = u;
					break;
				}
			}
		}
		for (int i = root.numOfEntery; i > j; i--)
			root.entryInEachNode[i] = root.entryInEachNode[i - 1];
		root.entryInEachNode[j] = t;
		root.numOfEntery++;
		if (root.numOfEntery < M)
			return null;
		else
			return split(root);
	}

// split node in half
	private Node split(Node h) {
		Node t = new Node(M / 2);
		h.numOfEntery = M / 2;
		for (int j = 0; j < M / 2; j++)
			t.entryInEachNode[j] = h.entryInEachNode[M / 2 + j];
		return t;
	}

// comparison functions - make Comparable instead of Key to avoid casts
	private boolean less(Comparable key1, Comparable key2) {
		return key1.compareTo(key2) < 0;
	}

	private boolean eq(Comparable key1, Comparable key2) {
		return key1.compareTo(key2) == 0;
	}

	/**
	 * Unit tests the BTree
	 */
	public static void main(String[] args) {
		BTree<String, String> morseCode = new BTree<String, String>();
		morseCode.put(".-", "A");
		morseCode.put("-...", "B");
		morseCode.put("-.-.", "C");
		morseCode.put("-..", "D");
		morseCode.put(".", "E");
		morseCode.put("..-.", "F");
		morseCode.put("--.", "G");
		morseCode.put("....", "H");
		morseCode.put("..", "I");
		morseCode.put(".---", "J");
		morseCode.put("-.-", "K");
		morseCode.put(".-..", "L");
		morseCode.put("--", "M");
		morseCode.put("-.", "N");
		morseCode.put("---", "O");
		morseCode.put(".--.", "P");
		morseCode.put("--.-", "Q");
		morseCode.put(".-.", "R");
		morseCode.put("...", "S");
		morseCode.put("-", "T");
		morseCode.put("..-", "U");
		morseCode.put("...-", "V");
		morseCode.put(".--", "W");
		morseCode.put("-..-", "X");
		morseCode.put("-.--", "Y");
		morseCode.put("--..", "Z");
		Scanner input = new Scanner(System.in);
		char ans;
		do {
			System.out.println("Enter the code: ");
			String code = input.next();
			String currentLetter = "";
			String currentValue;
			List<String> message = new ArrayList<String>();
			int codeSize = code.length();
			for (int i = 0; i < codeSize; i = i + 2) {
				try {
					currentValue = ("" + (code.charAt(i)) + (code.charAt(i + 1))).toString();
					if (currentValue.equals("11")) {// insert space
						if (i + 2 < codeSize && i + 3 < codeSize) {
							if (code.charAt(i + 2) != '1' || code.charAt(i + 3) != '1') {
								message.add(" ");
							} else {
								// if '1111' found end of sentence
								message.add(" STOP ");
							}
						}
					} else if (currentValue.equals("00")) {
// '00' end of letter
// Search for the alphabet
						message.add(morseCode.get(currentLetter));
// reset the 'currentLetter' string
						currentLetter = "";
					} else if (currentValue.equals("01")) {
// "01" represents '.'
						currentLetter = currentLetter.concat(".");
					} else if (currentValue.equals("10")) {
// '"10" represents '-'
						currentLetter = currentLetter.concat("-");
					} else {
// Anything entered other than 0 and 1
						System.out.println("The code is Incorrect !");
						System.exit(0);
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Your code pattern is wrong !");
					System.exit(0);
				}
			}
			System.out.println("\n***Message***");
			for (String s : message) {
				System.out.print(s);
			}
			System.out.println();
			System.out.println("\nMore messages (Y/N) ?");
			ans = input.next().charAt(0);
			while ((ans != 'Y') && (ans != 'y') && (ans != 'N') && (ans != 'n')) {
				System.out.println("Wrong input !!!");
				System.out.println("Enter only Y or N ");
				System.out.println("More messages ?");
				ans = input.next().charAt(0);
			}
		} while ((ans == 'Y') || (ans == 'y'));
		input.close();
	}
}
