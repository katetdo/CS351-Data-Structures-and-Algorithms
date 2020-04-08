package edu.uwm.cs351;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import edu.uwm.cs.junit.LockedTestCase;


public class BSTSortedSet<E extends Comparable<E>> extends AbstractSet<E> {

	//TODO: class fields
	// We need root and version
	// DO NOT use any field to count the number of nodes
	//#(
	private Node<E> root;
	private int version = 0;
	//#)
	
	private static boolean doReport = true;

	private static boolean report(String s) {
		if (doReport)
			System.out.println("Invariant error: " + s);
		return false;
	}
	private static int reportNeg(String s) {
		report(s);
		return -1;
	}
	//TODO: static node class
	// Fields must be named data, left, right, parent, treeSize
	// A helpful toString might help with debugging
	//#(
	private static class Node<T> {
		T data;
		int treeSize;
		Node<T> left, right, parent;
		Node (T e) { data = e; }
		@Override
		public String toString() {
			return data.toString() + " with " + treeSize + " nodes";
		}
	}
	//#)

	//Helper method to make sure an Object o can be used as type E
	//Only works when tree is not empty (we have an example of type E to compare it against)
	@SuppressWarnings("unchecked")
	private E asElement(Object o) {
		if (root == null)
			return null;
		try {
			E t = (E) o;
			root.data.compareTo(t);
			return t;
		} catch (ClassCastException | NullPointerException e) {
			return null;
		}
	}

	/**
	 * Check the invariant.  
	 */
	private boolean wellFormed() {
		//#(
		// Now we need to check the tree (as before), but also check parent pointers and size field
		int check = checkTree(root, null, null, null);
		if (check < 0)
			return false;
		//#)
		//TODO: Implement invariant similar to last homework
		//For each subtree rooted at n:
		// 1. n's data must not be null.
		// 2. n's data must fall within the appropriate bounds.
		// 3. n's parent must be the node from which you found n (new parameter to check this?)
		// 4. check the two subtrees (with updated bounds)
		// 5. treeSize must be appropriate for the size of the subtrees
		// 6. return the size of the subtree rooted at n,
		//     or -1 if there is a problem (either here, or already detected further down the tree).
		//     Use reportNeg where appropriate.
		return true;

	}
	//You may wish to use a recursive helper method to check wellFormed
	//Feel free to use homework 8 as a starting point
	//#(
	/**
	 * Check if a subtree is well formed. None of the data may be null and all of
	 * the keys must be in the given range (lo,hi) exclusive, except that a null
	 * bound means there is no bound in that direction, and finally all subtrees
	 * must also be well formed.
	 * 
	 * @param r  root of the subtree, may be null
	 * @param lo exclusive lower bound. If null, then no lower bound.
	 * @param hi exclusive upper bound. If null, then no upper bound.
	 * @param p  parent of the node in r (check against stored)
	 * @return numbers of nodes in tree, if well formed, -1 otherwise.
	 */
	private int checkTree(Node<E> r, E lo, E hi, Node<E> p) {
		if (r == null)
			return 0;
		if (r.parent != p)
			return reportNeg("Parent of " + r + "(" + r.data + ") is wrong");
		if (r.data == null)
			return reportNeg("null data found");
		if ((lo != null && lo.compareTo(r.data) >= 0 ) || (hi != null && hi.compareTo(r.data) <= 0)) {
			return reportNeg("data out of place: " + r.data + " in (" + lo + "," + hi + ")");
		}
		int n1 = checkTree(r.left, lo, r.data, r);
		int n2 = checkTree(r.right, r.data, hi, r);
		if (n1 < 0 || n2 < 0)
			return -1;
		if(n1 + n2 + 1 != r.treeSize) return reportNeg("r size should be " + (n1+n2+1) + " but was " + r.treeSize);
		return n1 + n2 + 1;
	}
	//#)

	
	/**
	 * Construct empty tree-based set
	 */
	public BSTSortedSet() {
		//TODO: implement constructor
		//#(
		root = null;
		//#)
		//assert invariant after constructor
		assert wellFormed() : "invariant fails after constructor";
	}
	
	protected BSTSortedSet(boolean ignored) {} // don't change: used by invariant checker
	
	@Override
	public int size() {
		assert wellFormed() : "Set not well-formed at start of size()";
		//TODO: implement size
		//#(
		if(root == null) return 0;
		return root.treeSize;
		//#)
	}
	
	@Override
	public boolean add(E t) {
		assert wellFormed() : "Set not well-formed at start of add()";
		//TODO: implement add
		//#(
		if (t == null)
			throw new IllegalArgumentException("null cannot be added");
		if (contains(t))
			return false;

		root = doAdd(root, null, t);

		++version;
		//#)
		assert wellFormed() : "Set not well-formed at end of add()";
		return true;
	}
	//You may wish to use a recursive helper method for add()
	//#(
	private Node<E> doAdd(Node<E> n, Node<E> par, E value) {
		if (n == null) {
			n = new Node<E>(value);
			n.parent = par;
			//update size field of all parents and n
			n.treeSize = 1;
			Node<E> p = n.parent;
			while (p != null) {
				++p.treeSize;
				p = p.parent;
			}
		} else {
			int c = value.compareTo(n.data);
			if (c < 0) {
				n.left = doAdd(n.left, n, value);
			} else if (c > 0) {
				n.right = doAdd(n.right, n, value);
			}
		}
		return n;
	}
	//#)

	@Override
	public void clear() {
		//TODO: Implement clear(). Do not update version if data structure state isn't changed
		//#(
		assert wellFormed();
		if (isEmpty())
			return;
		root = null;
		++version;
		assert wellFormed();
		//#)
	}

	@Override
	public boolean contains(Object o) {
		//TODO: implement contains
		//  Use asElement
		//#(
		assert wellFormed() : "Set not well-formed at start of contains()";
		E arg = asElement(o);
		if (arg == null)
			return false;
		Node<E> p = doFind(root, arg);
		return p != null;
		/*
		//#)
		return false;
		##*/
	}
	// You may wish to use a helper method for a recursion
	//#(
	private Node<E> doFind(Node<E> n, E val) {
		if (n == null)
			return null;
		int c = val.compareTo(n.data);
		if (c == 0)
			return n;
		else if (c < 0)
			return doFind(n.left, val);
		else
			return doFind(n.right, val);
	}
	//#)

	@Override
	public boolean isEmpty() {
		//TODO: very little work
		//#(
		assert wellFormed();
		return size() == 0;
		/*
		//#)
		return false;
		##*/
	}

	@Override
	public boolean remove(Object o) {
		assert wellFormed() : "Set not well-formed at start of remove()";
		//TODO: implement remove
		//#(
		if (!contains(o))
			return false;
		E val = asElement(o);
		Node<E> n = doFind(root, val);
		doRemove(n);
		++version;
		//#)
		assert wellFormed() : "Set not well-formed at end of remove()";
		return true;
	}
	// You may wish to use a helper method for remove
	// You may also wish to have your iterator use this helper method
	// There are multiple removal cases to consider
	//#(
	private void doRemove(Node<E> n) {
		Node<E> par = n.parent;
		boolean isRoot = par == null;
		boolean isLeft = !isRoot && n == par.left;
		if (n.left == null) { //n has no left child
			if (isRoot) //has no parent
				root = n.right;
			else if (isLeft) { //if n is a left child
				par.left = n.right;
				doUpdateParentSize(par);}
			else { //n is a right child
				par.right = n.right;
				doUpdateParentSize(par);}
			if (n.right != null)
				n.right.parent = n.parent;
		} else if (n.right == null) { //there is no right child
			if (isRoot)
				root = n.left;
			else if (isLeft) { //n is a left child
				par.left = n.left;
				doUpdateParentSize(par);}
			else { // n is a right child
				par.right = n.left;
				doUpdateParentSize(par);}
			n.left.parent = n.parent;  //we already know there is a left child
		} else { // left and right children
			Node<E> s = n.left;
			Node<E> sp = n;
			while (s.right != null) {//find closest smaller child and parent of that node
				sp = s;
				s = s.right;
			}
			n.data = s.data;
			if (sp == n) 
				n.left = s.left;
			else
				sp.right = s.left;
			if (s.left != null) {
				s.left.parent = sp;
			}
			doUpdateParentSize(sp);
		}
	}
	//#)
	//#(
	private void doUpdateParentSize(Node<E> parent) {
		while (parent != null) {
			--parent.treeSize;
			parent = parent.parent;
		}
	}
	//#)

	/**
	 * Method to get the nth element in sorted order (according to compareTo)
	 * @param n which ordinality of element to return
	 * @return nth element, 0-indexed
	 */
	public E getNth(int n) {
		//TODO: implement get Nth
		// You may use a recursive helper method
		// Our solution "micromanages" just a little
		//#(
		assert wellFormed();
		if (n < 0 || isEmpty() || n >= root.treeSize) throw new IllegalArgumentException("Invalid value for getNth");
		return doGetNth(root, n);
		//#)
	}
	
	//#(
	private E doGetNth(Node<E> r, int n) {
		int leftSize = r.left == null? 0 : r.left.treeSize;
		if (n == leftSize) return r.data;
		else if (n < leftSize) return doGetNth(r.left, n);
		else return doGetNth(r.right, n - leftSize - 1);
	}
	//#)

	@Override
	public String toString() {
		// TODO optional: make this more useful
		assert wellFormed();
		return "Set of size " + size();
	}

	@Override
	public Iterator<E> iterator() {
		//TODO: very little
		//#(
		assert wellFormed();
		return new MyIterator();
		//#)
	}
	

	private class MyIterator implements Iterator<E> {
		Node<E> nextNode;
		boolean hasCurrent;
		int myVersion = version;
		
		/*
		 * Iterator design:
		 * nextNode points to the next node to be iterated over
		 * nextNode null means there are no more elements to iterate over
		 * hasCurrent determines whether there is something that can be removed
		 * the thing to be removed is the node that came before nextNode
		 * this can be found by following the appropriate references
		 * when nextNode is null and hasCurrent is true, the current is the final node
		 */
		
		private boolean wellFormed() {
			// #(
			if (!BSTSortedSet.this.wellFormed()) return false;
			if (myVersion != version) return true;
			if (nextNode != null) {
				for (Node<E> p = nextNode; p != root; p = p.parent) {
					if (p.parent == null) return report("Not connected to root: " + nextNode.data);
					if (p.parent.left != p && p.parent.right != p) {
						return report("Not connected to root: " + nextNode.data);
					}
				}
			}
			Node<E> first = root;
			if (first != null)
				while(first.left != null)
					first = first.left;
			if(nextNode == first && hasCurrent)
				return report("hasCurrent true but nothing to remove");
			// #)
			// TODO: check outer invariant
			// TODO: if versions don't match, accept
			// TODO: check that nextNode is in the tree, or is null
			// TODO: if nextNode is the first node (or tree is empty), hasCurrent must be false
			return true;
		}
		
		protected MyIterator(boolean ignored) {} // don't change: used by invariant checker
		
		public MyIterator() {
			// #(
			if(root == null) return;
			nextNode = root;
			while(nextNode.left != null) {
				nextNode = nextNode.left;
			}
			hasCurrent = false;
			// #)
			// TODO: implement constructor
			assert wellFormed() : "invariant broken at end of constructor";
		}
		// #(
		private void checkVersion() {
			if (version != myVersion) throw new ConcurrentModificationException("stale");
		}
		// #)
		
		@Override
		public boolean hasNext() {
			assert wellFormed() : "invariant broken in hasNext()";
			//TODO: implement hasNext
			// #(
			checkVersion();
			return nextNode != null;
			/* #)
			return false;
			## */
		}

		@Override
		public E next() {
			assert wellFormed() : "invariant broken at start of next()";
			//TODO: implement next
			// #(
			if (!hasNext()) {
				throw new NoSuchElementException("no more");
			}
			E result = nextNode.data;
			if (nextNode.right != null) {
				nextNode = nextNode.right;
				while (nextNode.left != null) nextNode = nextNode.left;
			} else {
				while (nextNode.parent != null && nextNode.parent.right == nextNode) {
					nextNode = nextNode.parent;
				}
				nextNode = nextNode.parent;
			}
			hasCurrent = true;
			assert wellFormed(): "Invariant broken at end of next()";
			return result;
			/* #)
			assert _wellFormed(): "Invariant broken at end of next()";
			return null;
			## */
		}
		
		// #(
		@Override
		public void remove() {
			assert wellFormed() : "invariant broken at start of remove";
			checkVersion();
			if (!hasCurrent) throw new IllegalStateException();
			Node<E> n = nextNode;
			if (n != null && n.left == null) {
				Node<E> p = n.parent;
				Node<E> former = n;
				while (p.left == former) {
					former = p;
					p = p.parent;
				}
				doRemove(p);
			} else {
				Node<E> p = n == null ? (Node<E>) root : n.left;
				while (p.right != null) {
					p = p.right;
				}
				doRemove(p);
			}
			hasCurrent = false;
			myVersion = ++version;
			assert wellFormed() : "invariant broken at end of remove";
		}
		// #)
		// TODO: override remove
		//   You may be able to reuse a helper method used by remove(Object o)
	}

	public static class TestInternals extends LockedTestCase {
		private BSTSortedSet<Integer> self;
		private BSTSortedSet<Integer>.MyIterator iterator;
		Integer i1, i1a, i2, i3, i4, i5;
		Node<Integer> n0, n1, n1a, n2, n3, n4, n5;
		static {
			System.out.println("In the following tests, you should see 'invariant error' messages");
			System.out.println("If you do not see any, you are failing the tests!");
		}

		public void assertOK(String comment, boolean condition) {
			doReport = true;
			super.assertTrue(comment, condition);
		}

		public void assertNotOK(String message, boolean condition) {
			doReport = false;
			super.assertFalse(message, condition);
		}

		protected void setUp() {
			i1 = 1;
			i1a = 1;
			i2 = 4;
			i3 = 9;
			i4 = 16;
			i5 = 25;
			n0 = new Node<Integer>(null);
			n1 = new Node<Integer>(i1);
			n1a = new Node<Integer>(i1a);
			n2 = new Node<Integer>(i2);
			n3 = new Node<Integer>(i3);
			n4 = new Node<Integer>(i4);
			n5 = new Node<Integer>(i5);
			
			self = new BSTSortedSet<Integer>(false);
			iterator = self.new MyIterator(false);
		}

		public void testEmpty() {
			self.root = null;
			assertOK("empty set is just fine", self.wellFormed());
		}

		public void testNullElement() {
			self.root = n0;
			n0.treeSize = 1;
			assertNotOK("null element not OK", self.wellFormed());
			self.root = n1;
			n1.treeSize = 1;
			assertOK("One element should be OK", self.wellFormed());
			n1.left = n0;
			n0.parent = n1;
			n1.treeSize = 2;
			assertNotOK("null element not OK", self.wellFormed());
		}

		public void testEquivalent1() {
			self.root = n1;
			n1.left = n1a;
			n1a.parent = n1;
			n1.treeSize = 2;
			n1a.treeSize = 1;
			assertNotOK("equivalent values should be disallowed", self.wellFormed());
			n1.left = null;
			n1.right = n1a;
			assertNotOK("equivalent values should be disallowed", self.wellFormed());
		}

		public void testIdentical() {
			self.root = n1;
			n1.left = n1a;
			n1a.data = i1;
			n1a.parent = n1;
			n1.treeSize = 2;
			n1a.treeSize = 1;
			assertNotOK("identical values not OK", self.wellFormed());
			n1.left = null;
			n1.right = n1a;
			assertNotOK("identical values not OK", self.wellFormed());
		}

		public void testEquivalent2() {
			self.root = n2.parent = n1;
			n1.right = n2;
			n2.left = n1a;
			n1a.parent = n2;
			n1.treeSize = 3;
			n2.treeSize = 2;
			n1a.treeSize = 1;
			assertNotOK("equivalent values not OK", self.wellFormed());
		}
		
		public void testTreeSize() {
			self.root = null;
			assertOK("OK so far", self.wellFormed());
			self.root = n3;
			n3.treeSize = 1;
			assertOK("OK so far", self.wellFormed());
			n3.treeSize = 0;
			assertNotOK("treeSize should be 1", self.wellFormed());
			n3.left = n2;
			n3.treeSize = 2;
			n2.parent = n3;
			n2.treeSize = 2;
			assertNotOK("n2 treeSize should be 1", self.wellFormed());
			n2.treeSize = 0;
			assertNotOK("n2 treeSize should be 1", self.wellFormed());
			n2.treeSize = 1;
			assertOK("Parent", self.wellFormed());
		}
		
		public void testTreeSize2() {
			self.root = n3;
			n3.left = n2;
			n2.parent = n3;
			n3.treeSize = 2;
			n2.treeSize = 1;
			assertOK("OK so far", self.wellFormed());
			
			n3.right = n4;
			n4.parent = n3;
			n4.treeSize = 1;
			n3.treeSize = 3;
			n2.left = n1;
			n1.parent = n2;
			n1.treeSize = 1;
			n2.treeSize = 2;
			assertNotOK("root treeSize incorrect", self.wellFormed());
			n3.treeSize = 4;
			n2.treeSize = 1;
			assertNotOK("n2 treeSize incorrect", self.wellFormed());
			n2.treeSize = 2;
			assertOK("OK", self.wellFormed());
		}
		


		public void testParent1() {
			self.root = n3;
			n3.treeSize = 1;
			assertOK("OK so far", self.wellFormed());
			n3.left = n2;
			n3.treeSize++;
			n2.treeSize = 1;
			assertNotOK("No parent", self.wellFormed());
			n2.parent = n3;
			assertOK("Parent", self.wellFormed());
			n3.parent = n3;
			assertNotOK("Cyclic 1", self.wellFormed());
			n3.parent = n2;
			assertNotOK("Cyclic 2", self.wellFormed());
			n2.parent = null;
			assertNotOK("Out of order", self.wellFormed());
		}

		public void testParent2() {
			self.root = n4;
			n4.treeSize = 1;
			assertOK("OK so far", self.wellFormed());
			n4.left = n2;
			n4.treeSize++;
			n2.treeSize = 1;
			assertNotOK("No parent", self.wellFormed());
			n2.parent = n4;
			assertOK("Parent", self.wellFormed());
			n3.treeSize = 1;
			n2.treeSize++;
			n4.treeSize++;
			n2.right = n3;
			assertNotOK("No parent", self.wellFormed());
			n3.parent = n4;
			assertNotOK("skip Parent", self.wellFormed());
			n3.parent = n2;
			assertOK("Parent", self.wellFormed());
			n2.left = n1;
			n1.treeSize = 1;
			n2.treeSize++;
			n4.treeSize++;
			
			assertNotOK("No Parent", self.wellFormed());
			n1.parent = n4;
			assertNotOK("Bad Parent", self.wellFormed());
			n1.parent = n3;
			assertNotOK("Bad Parent", self.wellFormed());
			n1.parent = n2;
			assertOK("Parents OK", self.wellFormed());
			n4.parent = n1;
			assertNotOK("Cyclic 1", self.wellFormed());
			n4.parent = n2;
			assertNotOK("Cyclic 2", self.wellFormed());
			n4.parent = n3;
			assertNotOK("Cyclic 3", self.wellFormed());
		}

		public void testParent3() {
			self.root = n2;
			n2.right = n4;
			n2.treeSize = 2;
			n4.treeSize = 1;
			assertNotOK("Not linked (n2)", self.wellFormed());
			n4.parent = n2;
			assertOK("Linked", self.wellFormed());
			n2.left = n1;
			n2.treeSize++;
			n1.treeSize = 1;
			assertNotOK("Not linked (n1)", self.wellFormed());
			n1.parent = n2;
			assertOK("Linked", self.wellFormed());
			n4.left = n3;
			n3.treeSize = 1;
			n4.treeSize++;
			n2.treeSize++;
			self.root.treeSize = 4;
			assertNotOK("Not linked (n3)", self.wellFormed());
			n3.parent = n2;
			assertNotOK("Badly linked (n3)", self.wellFormed());
			n3.parent = n4;
			n2.parent = n3;
			assertNotOK("Cyclic 1", self.wellFormed());
			n2.parent = null;
			assertOK("OK", self.wellFormed());
			n1a.data = 20;
			n2.parent = n1a;
			assertNotOK("Not in tree", self.wellFormed());
			n1a.left = n2;
			assertNotOK("Not in tree", self.wellFormed());
			self.root = n1a;
			self.root.treeSize = 5;
			assertOK("OK", self.wellFormed());
		}
		
		public void testParent4() {
			self.root = n1;
			n1.right = n2;
			n1.treeSize = 2;
			n2.treeSize = 1;
			n2.parent = n1a;
			n1a.treeSize = 2;
			assertNotOK("Not in tree", self.wellFormed());
			n1a.data = i1;
			assertNotOK("Not in tree", self.wellFormed());
		}

		public void testOutOfOrder1() {
			self.root = n2;
			n2.treeSize = 1;
			assertOK("OK 1", self.wellFormed());
			n2.right = n4;
			n2.treeSize++;
			n4.treeSize = 1;
			n4.parent = n2;
			assertOK("OK 2", self.wellFormed());
			n4.left = n3;
			n3.treeSize = 1;
			n4.treeSize++;
			n2.treeSize++;
			n3.parent = n4;
			assertOK("OK 3", self.wellFormed());
			n3.left = n1;
			n1.parent = n3;
			n1.treeSize = 1;
			n2.treeSize++;
			n3.treeSize++;
			n4.treeSize++;
			assertNotOK("out of order", self.wellFormed());
			n2.left = n1;
			n1.parent = n2;
			n3.left = null;
			n3.treeSize--;
			n4.treeSize--;
			assertOK("OK 4", self.wellFormed());
		}

		public void testOutOfOrder2() {
			self.root = n1;
			n1.treeSize = 1;
			assertOK("OK 1", self.wellFormed());
			n1.right = n3;
			n3.parent = n1;
			n3.treeSize = 1;
			n1.treeSize++;
			assertOK("OK 2", self.wellFormed());
			n3.left = n2;
			n2.treeSize = 1;
			n2.parent = n3;
			n1.treeSize++;
			n3.treeSize++;
			assertOK("OK 3", self.wellFormed());
			n3.right = n4;
			n4.parent = n3;
			++n3.treeSize;
			n4.treeSize = 1;
			++n1.treeSize;;
			assertOK("OK 4", self.wellFormed());
			n4.left = n1a;
			n1a.parent = n4;
			n1a.data = i2;
			++n1.treeSize;
			++n3.treeSize;
			++n4.treeSize;
			n1a.treeSize = 1;
			assertNotOK("out of order", self.wellFormed());
			n4.left = null;
			n4.right = n5;
			n5.parent = n4;
			n5.treeSize = 1;
			assertOK("OK 5", self.wellFormed());
		}

		public void testEmptyIterator() {
			self.root = null;
			assertOK("null cursor should be OK", iterator.wellFormed());
			++self.version;
			assertOK("version bad", iterator.wellFormed());
			++iterator.myVersion;
			assertOK("cursor OK", iterator.wellFormed());
			iterator.hasCurrent = true;
			assertNotOK("iterator on empty set shouldn't have current", iterator.wellFormed());
			iterator.hasCurrent = false;
			iterator.nextNode = n1;
			assertNotOK("cursor lost", iterator.wellFormed());
			self.root = n1;
			n1.treeSize = 1;
			assertOK("initial iterator", iterator.wellFormed());
		}

		public void testIterator() {
			self.version = 456;
			self.root = n2;
			n1.parent = n2;
			n3.parent = n2;
			n2.right = n3;
			n2.left = n1;
			n2.treeSize = 3;
			n1.treeSize = 1;
			n3.treeSize = 1;
			assertOK("", self.wellFormed());
			assertOK("", iterator.wellFormed());
			iterator.nextNode = n1;
			iterator.myVersion = 456;
			assertOK("", iterator.wellFormed());
			iterator.nextNode = n2;
			assertOK("", iterator.wellFormed());
			iterator.nextNode = n4;
			assertNotOK("cursor lost", iterator.wellFormed());
			++iterator.myVersion;
			assertOK("", iterator.wellFormed());
		}

		public void testThroughIterator() {
			self.root = n2;
			n2.left = n1;
			n1.parent = n2;
			n1.treeSize = 1;
			n2.treeSize = 2;
			iterator.nextNode = n1;
			assertEquals("initial state", Tb(1610585648), iterator.wellFormed());
			iterator.hasCurrent = true;
			assertEquals("hasCurrent but no node before nextNode", Tb(1945878069), iterator.wellFormed());
			iterator.nextNode = n2;
			assertEquals("iterated over n1", Tb(323097581), iterator.wellFormed());
			iterator.hasCurrent = false;
			assertEquals("removed something before n2", Tb(1378428561), iterator.wellFormed());
			iterator.nextNode = null;
			iterator.hasCurrent = true;
			assertEquals("iterated over n2 (final element)", Tb(583541388), iterator.wellFormed());
			iterator.hasCurrent = false;
			assertEquals("removed final element", Tb(1014855961), iterator.wellFormed());
		}
	}
}
