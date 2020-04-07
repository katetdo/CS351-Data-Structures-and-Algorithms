package edu.uwm.cs351;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.uwm.cs.junit.LockedTestCase;
import snapshot.Snapshot;

/*
 * StudentReportMap implements a map of Students to Grades
 * It does not allow null Students or null Grades
 */

public class StudentReportMap extends AbstractMap<Student,Grade> {
	
	// The tree structure for this assignment is based on homework9.
	// We remove treeSize and bring back manyNodes, as this is
	// not a sorted collection.
	// Some of the structure is given,
	// but we will need to add fields to support the AtRisk list

	private RecordNode root;
	private int manyNodes;
	private int version = 0;

	//TODO: add the new fields firstAtRisk and manyAtRisk
	//#(
	private RecordNode firstAtRisk;
	private int manyAtRisk;
	//#)
	
	private static double AT_RISK_GRADE = 70.0;
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

	protected StudentReportMap(boolean ignored) {} // don't change: used by invariant checker

	private static class RecordNode {
		Entry<Student,Grade> entry;
		RecordNode left, right, parent;
		RecordNode(Entry<Student, Grade> e) { entry = e; }

		public String toString() {
			return "(" + this.entry.getKey().toString() + ", "  + entry.getValue().toString() + ")";
		}
		
		//TODO: add the new fields nextAtRisk and prevAtRisk
		//#(
		RecordNode nextAtRisk, prevAtRisk;
		//#)
	}

	/**
	 * Check the invariant.  
	 */
	private boolean wellFormed() {
		// Now we need to check the tree (as before),
		// but also check the AtRisk list
		//TODO: Implement invariant for the AtRisk list
		// 1. Each node in AtRisk must be correctly doubly linked (and the first's previous is null)
		// 2. Each node in AtRisk must be in the BST
		// 3. The number of nodes in AtRisk must match manyAtRisk
		// 4. Every node not in AtRisk has prevAtRisk and nextAtRisk null
		//     (easier to check in checkTree)
		//#(
		int count = 0;
		RecordNode cursor = firstAtRisk;
		if(cursor != null && cursor.prevAtRisk != null)
			return report(cursor.toString() + " not linked correctly");
		while(cursor != null) {
			count++;
			if (cursor != getNode(cursor.entry.getKey()))
				return report(cursor.toString() + " not in tree");
			if(cursor.nextAtRisk != null)
				if (cursor.nextAtRisk.prevAtRisk != cursor)
					return report(cursor.toString() + " not linked correctly");
			cursor = cursor.nextAtRisk;
		}
		if (count != manyAtRisk) return report("manyAtRisk wrong");
		//#)
		if (manyNodes < 0) return report("invalid manyNodes");
		int check = checkTree(root, null, null, null);
		if (check < 0) return false;
		if (check != manyNodes) return report("Size should be " + check + " but was " + manyNodes);
		return true;

	}
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
	private int checkTree(RecordNode r, Student lo, Student hi, RecordNode p) {
		if (r == null)
			return 0;
		if (r.entry == null)
			return reportNeg("null entry found");
		if (r.entry.getKey() == null)
			return reportNeg("null student found");
		if (r.entry.getValue() == null)
			return reportNeg("null grade found");
		if (r.parent != p)
			return reportNeg("Parent of " + r + "(" + r.entry + ") is wrong");
		if (r.entry == null)
			return reportNeg("null data found");
		if ((lo != null && lo.compareTo(r.entry.getKey()) >= 0 ) || (hi != null && hi.compareTo(r.entry.getKey()) <= 0)) {
			return reportNeg("data out of place: " + r.entry + " in (" + lo + "," + hi + ")");
		}
		//#(
		if (r.nextAtRisk != null || r.prevAtRisk != null) {
			RecordNode cur = firstAtRisk;
			while (cur != null && cur != r)
				cur = cur.nextAtRisk;
			if (cur != r)
				return reportNeg("non-at-risk node with a prev or next");
		}
		//#)
		int n1 = checkTree(r.left, lo, r.entry.getKey(), r);
		int n2 = checkTree(r.right, r.entry.getKey(), hi, r);
		if (n1 < 0 || n2 < 0)
			return -1;
		return n1 + n2 + 1;
	}
	
	public StudentReportMap() {
		root = null;
		manyNodes = 0;
		version = 0;
		//TODO initialize new fields
		//#(
		firstAtRisk = null;
		manyAtRisk = 0;
		//#)
		assert wellFormed() : "invariant broken after constructor";
	}
	
	/**
	 * Get the RecordNode corresponding to a key(student).
	 * 
	 * @param s the [potential] key to find the node of
	 * @return The node containing key s. Or null if no such node exists in tree.
	 */
	private RecordNode getNode(Student s) {
		if (s == null) return null;
		RecordNode r = root;
		while (r != null) {
			int c = s.compareTo(r.entry.getKey());
			if (c == 0) return r;
			if (c < 0) r = r.left;
			else r = r.right;
		}
		return null;
	}
	
	//this removes a node from the BST
	private void doRemove(RecordNode n) {
		//remove from the atRisk set first
		atRiskSet().removeNode(n);
		RecordNode par = n.parent;
		boolean isRoot = par == null;
		boolean isLeft = !isRoot && n == par.left;
		if (n.left == null) { //n has no left child
			if (isRoot) //has no parent
				root = n.right;
			else if (isLeft) { //if n is a left child
				par.left = n.right;}
			else { //n is a right child
				par.right = n.right;}
			if (n.right != null)
				n.right.parent = n.parent;
		} else if (n.right == null) { //there is no right child
			if (isRoot)
				root = n.left;
			else if (isLeft) { //n is a left child
				par.left = n.left;}
			else { // n is a right child
				par.right = n.left;
				}
			n.left.parent = n.parent;  //we already know there is a left child
		} else { // left and right children
			RecordNode s = n.left;
			RecordNode sp = n;
			while (s.right != null) {//find closest smaller child and parent of that node
				sp = s;
				s = s.right;
			}
			n.entry = s.entry;
			if (sp == n) 
				n.left = s.left;
			else
				sp.right = s.left;
			if (s.left != null) {
				s.left.parent = sp;
			}
		}
	}
	
	//to add something to tree, use root = doAdd(root, null, ...
	//this adds something assuming the key is not already in the tree
	private RecordNode doAdd(RecordNode n, RecordNode par, Entry<Student, Grade> e) {
		//TODO: modify this to replace an entry with matching key
		if (n == null) {
			n = new RecordNode(e);
			n.parent = par;
		} else {
			int c = e.getKey().compareTo(n.entry.getKey());
			if (c < 0)
				n.left = doAdd(n.left, n, e);
			else if (c > 0)
				n.right = doAdd(n.right, n, e);
			//#(
			else
				n.entry = e;
			//#)
		}
		return n;
	}

	@Override
	public void clear() {
		//TODO modify for AtRisk fields
		assert wellFormed() : "invariant broken at start of clear";
		if (isEmpty())
			return;
		root = null;
		manyNodes = 0;
		//#(
		firstAtRisk = null;
		manyAtRisk = 0;
		//#)
		++version;
		assert wellFormed() : "invariant broken at end of clear";
	}

	@Override
	public int size() {
		assert wellFormed() : "invariant broken at start of size";
		return manyNodes;
	}

	@Override
	public boolean isEmpty() {
		assert wellFormed() : "invariant broken at start of isEmpty";
		return manyNodes == 0;
	}

	@Override
	public boolean containsKey(Object arg0) {
		//TODO: Implement containsKey
		//#(
		assert wellFormed() : "invariant broken at start of containsKey";
		if (arg0 instanceof Student){
			Student key = (Student)arg0;
			return getNode(key) != null;
		}
		else return false;
		//#)
	}

	@Override
	public Grade get(Object arg0) {
		//TODO: Implement get
		//#(
		assert wellFormed() : "invariant broken at start of get";
		if (arg0 instanceof Student){
			Student key = (Student)arg0;
			RecordNode n = getNode(key);
			if (n == null) return null;
			return n.entry.getValue();
		}
		else return null;
		//#)
	}



	@Override
	public Grade put(Student s, Grade g) {
		//TODO: Implement put
		//Increment version if something changed
		//Increment manyNodes if a new student was added
		//If it's a new student, add to atRisk depending on grade
		//Use doAdd
		//You can use get or containsKey if you like
		//#(
		assert wellFormed() : "invariant false at start of put()";
		if (s == null) throw new IllegalArgumentException("Cannot have null values");
		if (g == null) throw new IllegalArgumentException("Cannot have null values");
		Grade oldValue = get(s);
		root = doAdd(root, null, new SimpleEntry<Student,Grade>(s,g));
		if(oldValue == null)
			manyNodes++;
		if(oldValue == null || !oldValue.equals(g))
			version++;
		if(oldValue == null && g.getGrade() < AT_RISK_GRADE)
			atRiskSet().add(s);
		assert wellFormed() : "invariant false at end of add()";
		return oldValue;
		//#)
	}


	@Override
	public Grade remove(Object arg0) {
		//TODO: Implement remove
		//Remember that you may also have to remove from the atRisk set.
		//Check what doRemove does.
		//#(
		assert wellFormed() : "invariant false at start of remove()";
		Grade oldValue = null;
		if(arg0 instanceof Student) {
			Student s = (Student)arg0;
			RecordNode n = getNode(s);
			if (n == null) return null; //argument in was not in the map
			oldValue = n.entry.getValue();
			doRemove(n);
			--manyNodes;
			++version;
		}
		assert wellFormed() : "invariant false at end of remove()";
		return oldValue;
		//#)
	}
	

	@Override
	public String toString() {
		assert wellFormed() : "invariant broken at start of toString";
		return ("A map with " + manyNodes + " students");
	}

	@Override
	public Set<Entry<Student, Grade>> entrySet() {
		assert wellFormed() : "invariant broken at start of entrySet";
		return new EntrySet();
	}
	@Override
	public Set<Student> keySet() {
		assert wellFormed() : "invariant broken at start of keySet";
		return new KeySet();
	}
	public AtRiskSet atRiskSet() {
		assert wellFormed() : "invariant broken at start of atRiskSet";
		return new AtRiskSet();
	}

	/**
	 * Iterator over all entries within the StudentReportMap
	 * This is provided for you.
	 *
	 */
	private class MyIterator implements Iterator<Entry<Student,Grade>>{
		
		RecordNode nextNode;
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
			if (!StudentReportMap.this.wellFormed()) return false;
			if (myVersion != version) return true;
			if (nextNode != null) {
				for (RecordNode p = nextNode; p != root; p = p.parent) {
					if (p.parent == null) return report("Not connected to root: " + nextNode.entry);
					if (p.parent.left != p && p.parent.right != p) {
						return report("Not connected to root: " + nextNode.entry);
					}
				}
			}
			RecordNode first = root;
			if (first != null)
				while(first.left != null)
					first = first.left;
			if(nextNode == first && hasCurrent)
				return report("hasCurrent true but nothing to remove");
			return true;
		}
		
		public MyIterator() {
			if(root == null) return;
			nextNode = root;
			while(nextNode.left != null) {
				nextNode = nextNode.left;
			}
			hasCurrent = false;
		}
		
		private void checkVersion() {
			if (version != myVersion) throw new ConcurrentModificationException("stale");
		}

		@Override
		public boolean hasNext() {
			assert wellFormed() : "invariant broken in hasNext()";
			checkVersion();
			return nextNode != null;
		}

		@Override
		public Entry<Student, Grade> next() {
			assert wellFormed() : "invariant broken at start of next()";
			if (!hasNext()) {
				throw new NoSuchElementException("no more");
			}
			Entry<Student,Grade> result = nextNode.entry;
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

		}

		@Override
		public void remove() {
			assert wellFormed() : "invariant broken at start of remove";
			checkVersion();
			if (!hasCurrent) throw new IllegalStateException();
			RecordNode n = nextNode;
			if (n != null && n.left == null) {
				RecordNode p = n.parent;
				RecordNode former = n;
				while (p.left == former) {
					former = p;
					p = p.parent;
				}
				doRemove(p);
			} else {
				RecordNode p = n == null ? root : n.left;
				while (p.right != null) {
					p = p.right;
				}
				doRemove(p);
			}
			hasCurrent = false;
			myVersion = ++version;
			manyNodes--;
			assert wellFormed() : "invariant broken at end of remove";
		}
		
		
	}
	/**
	 * The Entry set for this map.
	 * This set doesn't have its own data structure:
	 * it uses the data structure of the map.
	 */
	private class EntrySet extends AbstractSet<Entry<Student,Grade>> {

		//required
		@Override
		public Iterator<Entry<Student, Grade>> iterator() {
			assert wellFormed();
			return new MyIterator();
		}

		//required
		@Override
		public int size() {
			//TODO: don't duplicate code
			//#(
			return StudentReportMap.this.size();
			//#)
		}

		//for efficiency
		@Override
		public void clear() {
			//TODO: don't duplicate code
			//#(
			StudentReportMap.this.clear();
			//#)
		}
		
		//for efficiency
		@Override
		public boolean contains(Object arg0) {
			//TODO: return false if
			// 1. arg0 is not an Entry
			// 2. arg0's key is not a Student
			// 3. the map does not contain arg0's key
			// 4. the map has a different value for the key
			//
			// You may have to cast to Entry<?,?>
			//#(
			assert wellFormed();
			if(!(arg0 instanceof Entry<?,?>)) return false;
			Entry<?,?> e = (Entry<?,?>) arg0;
			Object s = e.getKey();
			if(!(s instanceof Student)) return false;
			RecordNode n = getNode((Student)s);
			if(n==null) return false;
			return n.entry.getValue().equals(e.getValue());
			//#)
		}

		//for efficiency
		@Override
		public boolean remove(Object arg0) {
			//TODO remove only if this association is in the map
			//#(
			if(!contains(arg0))
				return false;
			StudentReportMap.this.remove(((Entry<?,?>)arg0).getKey());
			return true;
			//#)
		}

	}
	/**
	 * The AtRiskSet for this map.
	 * This set doesn't have its own data structure:
	 * it uses the data structure of the map.
	 */
	public class AtRiskSet extends AbstractSet<Student> {

		@Override
		public Iterator<Student> iterator() {
			assert wellFormed();
			return new AtRiskIterator();
		}

		@Override
		/**
		 * This method should only add the student to the AtRisk set
		 * if that student is in the map.
		 * If that student is not in the map, or is already in
		 * the atRisk set, return false.
		 * @return whether a student was added
		 */
		public boolean add(Student e) {
			//TODO: Implement add
			//#(
			assert wellFormed();
			RecordNode n = getNode(e);
			if (n == null) { //then add an entry in the tree with a null grade
				return false;
			}
			if (n.nextAtRisk != null || n.prevAtRisk != null || n == firstAtRisk) {
				return false; // Student is already at risk
			}
			//otherwise add it at the head
			n.nextAtRisk = firstAtRisk;
			if(firstAtRisk != null)
				firstAtRisk.prevAtRisk = n;
			firstAtRisk = n;
			manyAtRisk++;
			version++;
			assert wellFormed();
			return true;
			//#)
		}


		@Override
		public boolean contains(Object arg0) {
			//TODO: implement contains efficiently
			//#(
			assert wellFormed();
			if(arg0 instanceof Student) {
				Student s = (Student)arg0;
				RecordNode n = getNode(s);
				if (n==null)
					return false;
				if (n.nextAtRisk != null || n.prevAtRisk != null || n == firstAtRisk) {
					return true;
				}
			}
			return false;
			//#)
		}

		@Override
		public boolean isEmpty() {
			//TODO
			//#(
			assert wellFormed();
			return manyAtRisk == 0;
			//#)
		}

		@Override
		public int size() {
			//TODO
			//#(
			assert wellFormed();
			return manyAtRisk;
			//#)
		}
		
		@Override
		public boolean remove(Object arg0) {
			//TODO: remove a student from the AtRisk set
			// (not from the map)
			// Return false if student is not in AtRisk.
			//#(
			assert wellFormed();
			if(!(arg0 instanceof Student))
				return false;
			Student s = (Student)arg0;
			RecordNode n = getNode(s);
			if(n==null) return false;
			boolean removed = removeNode(n);
			assert wellFormed();
			return removed;
			//#)
		}
		
		//this method should check if the node is in the atRisk set
		//then remove it from AtRisk (not the map) if so
		//and return whether something was removed
		//you can call this helper method from other methods
		private boolean removeNode(RecordNode n) {
			//TODO: remove node n from the linked list
			//#(
			if (n.nextAtRisk != null || n.prevAtRisk != null || n == firstAtRisk) {
				if(n.prevAtRisk != null)
					n.prevAtRisk.nextAtRisk = n.nextAtRisk;
				if(n.nextAtRisk != null)
					n.nextAtRisk.prevAtRisk = n.prevAtRisk;
				if(n==firstAtRisk)
					firstAtRisk = n.nextAtRisk;
				n.nextAtRisk = n.prevAtRisk = null;
				version++;
				manyAtRisk--;
				return true;
			}
			return false;
			//#)
		}

		/**
		 * Returns the grade of an at risk student
		 * @return the grade of the atRisk Student
		 *   or null of the student is not in the AtRisk set
		 */
		public Grade getGrade(Student s) {
			//TODO: implement this efficiently
			//#(
			assert wellFormed();
			if(!contains(s))
				return null;
			return get(s);
			//#)
		}
		
		//We provide you an iterator for the AtRisk set
		//We omit the invariant checker
		private class AtRiskIterator implements Iterator<Student>{
			
			RecordNode current;
			RecordNode next;
			int myVersion = version;
			
			public AtRiskIterator() {
				next = firstAtRisk;
			}
			
			private void checkVersion() {
				if (version != myVersion) throw new ConcurrentModificationException("stale");
			}

			@Override
			public boolean hasNext() {
				assert wellFormed();
				checkVersion();
				return next != null;
			}

			@Override
			public Student next() {
				assert wellFormed();
				checkVersion();
				if(next == null) throw new NoSuchElementException();
				current = next;
				next = next.nextAtRisk;
				return current.entry.getKey();
			}

			@Override
			public void remove() {
				assert wellFormed();
				checkVersion();
				if (current == null) throw new IllegalStateException("No Current");
				removeNode(current);
				current = null;
				//it's OK if removeNode already incremented version
				//incrementing twice won't hurt anything
				myVersion = ++version;
				assert wellFormed();
			}
			
		}

	}
	
	//KeySet is given to you.
	//It mostly uses your implementation of the EntrySet
	//Pay attention to how cleanly it uses the backing ADT
	private class KeySet extends AbstractSet<Student> {

		//required
		@Override
		public Iterator<Student> iterator() {
			assert wellFormed();
			//iterator is exactly the same as MyIterator, but returns Students instead of Entries
			return new KeySetIterator();
		}

		//required
		@Override
		public int size() {
			return StudentReportMap.this.size();
		}

		//for efficiency
		@Override
		public void clear() {
			StudentReportMap.this.clear();
		}

		//for efficiency
		@Override
		public boolean contains(Object o) {
			return StudentReportMap.this.containsKey(o);
		}

		//for efficiency
		@Override
		public boolean remove(Object o) {
			Grade g = StudentReportMap.this.remove(o);
			return g != null;
		}
		
		//KeySet's iterator uses EntrySet's iterator
		//It just returns the keys instead of the entries
		//All exceptions are thrown by the other iterator's methods,
		//   so we have no need to throw them here
		private class KeySetIterator implements Iterator<Student>{
			Iterator<Entry<Student,Grade>> it = entrySet().iterator();
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Student next() {
				Entry<Student,Grade> e = it.next();
				return e.getKey();
			}
			
			@Override
			public void remove() {
				it.remove();
			}
			
		}



	}
	public static class TestInternals extends LockedTestCase {
		private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/StudentReportMap.java"};
		private static boolean firstRun = true;	
		
		public void log() {
			System.out.println("running");
			Snapshot.capture(TO_LOG);
		}
		
		private StudentReportMap self;
		private StudentReportMap.EntrySet selfEntrySet;
		SimpleEntry<Student, Grade> i1, i1a, i2, i3, i4, i5;
		RecordNode n0, n1, n1a, n2, n3, n4, n5;
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
			if(firstRun) {
				log();
				firstRun = false;
			}
			
			Student s1 = new Student("adamw");
			Student s1a = new Student("adamw");
			Student s2 = new Student("boyland");
			Student s3 = new Student("christine");
			Student s4 = new Student("dobby");
			Student s5 = new Student("erin");

			Grade g = new Grade(95.0);
			SimpleEntry<Student,Grade> i1 = new SimpleEntry<Student, Grade>(s1, g);
			SimpleEntry<Student,Grade> i1a = new SimpleEntry<Student, Grade>(s1a, g);
			SimpleEntry<Student,Grade> i2 = new SimpleEntry<Student, Grade>(s2, g);
			SimpleEntry<Student,Grade> i3 = new SimpleEntry<Student, Grade>(s3, g);
			SimpleEntry<Student,Grade> i4 = new SimpleEntry<Student, Grade>(s4, g);
			SimpleEntry<Student,Grade> i5 = new SimpleEntry<Student, Grade>(s5, g);

			n0 = new RecordNode(null);
			n1 = new RecordNode(i1);
			n1a = new RecordNode(i1a);
			n2 = new RecordNode(i2);
			n3 = new RecordNode(i3);
			n4 = new RecordNode(i4);
			n5 = new RecordNode(i5);

			self = new StudentReportMap(false);
			//iterator = self.new MyIterator(false);
		}

		public void testEmpty() {
			self.root = null;
			assertOK("empty set is just fine", self.wellFormed());
		}

		public void testNullElement() {
			self.root = n0;
			self.manyNodes = 1;
			assertNotOK("null element not OK", self.wellFormed());
			self.root = n1;
			assertOK("One element should be OK", self.wellFormed());
			n1.left = n0;
			self.manyNodes = 2;
			n0.parent = n1;
			assertNotOK("null element not OK", self.wellFormed());
		}

		public void testEquivalent1() {
			self.root = n1;
			n1.left = n1a;
			self.manyNodes = 2;
			n1a.parent = n1;
			assertNotOK("equivalent values should be disallowed", self.wellFormed());
			n1.left = null;
			n1.right = n1a;
			assertNotOK("equivalent values should be disallowed", self.wellFormed());
		}

		public void testSize1() {
			assertOK("empty tree", self.wellFormed());
			self.root = n1;
			assertNotOK("Size should be 1", self.wellFormed());
			self.manyNodes = 1;
			assertOK("size 1 tree", self.wellFormed());
			n1.right = n2;
			n2.parent = n1;
			assertNotOK("Size should be 2", self.wellFormed());
			self.manyNodes = 2;
			assertOK("size 2 tree", self.wellFormed());
		}

		public void testIdentical() {
			self.root = n1;
			n1.left = n1a;
			n1a.entry = i1;
			n1a.parent = n1;
			self.manyNodes = 2;
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
			self.manyNodes = 3;
			assertNotOK("equivalent values not OK", self.wellFormed());
			n2.left = null;
			self.manyNodes = 2;
			assertOK("OK", self.wellFormed());
		}				

		public void testParent1() {
			self.root = n3;
			self.manyNodes = 1;
			assertOK("OK so far", self.wellFormed());
			n3.left = n2;
			self.manyNodes = 2;
			assertNotOK("No parent", self.wellFormed());
			n2.parent = n3;

			int x = n2.entry.getKey().compareTo(n3.entry.getKey());
			assertEquals(-1,x);

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
			self.manyNodes = 1;
			assertOK("OK so far", self.wellFormed());
			n4.left = n2;
			self.manyNodes = 2;
			assertNotOK("No parent", self.wellFormed());
			n2.parent = n4;
			assertOK("Parent", self.wellFormed());
			n2.right = n3;
			self.manyNodes = 3;
			assertNotOK("No parent", self.wellFormed());
			n3.parent = n4;
			assertNotOK("skip Parent", self.wellFormed());
			n3.parent = n2;
			assertOK("Parent", self.wellFormed());
			n2.left = n1;
			self.manyNodes = 4;

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
			self.manyNodes = 2;
			assertNotOK("Not linked (n2)", self.wellFormed());
			n4.parent = n2;
			assertOK("Linked", self.wellFormed());
			n2.left = n1;
			self.manyNodes = 3;
			assertNotOK("Not linked (n1)", self.wellFormed());
			n1.parent = n2;
			assertOK("Linked", self.wellFormed());
			n4.left = n3;
			self.manyNodes = 4;
			assertNotOK("Not linked (n3)", self.wellFormed());
			n3.parent = n2;
			assertNotOK("Badly linked (n3)", self.wellFormed());
			n3.parent = n4;
			n2.parent = n3;
			assertNotOK("Cyclic 1", self.wellFormed());
			n2.parent = null;
			assertOK("OK", self.wellFormed());
			n1a.entry = new SimpleEntry<Student, Grade>(new Student("newNate"), new Grade(60.0));
			n2.parent = n1a;
			assertNotOK("Not in tree", self.wellFormed());
			n1a.left = n2;
			assertNotOK("Not in tree", self.wellFormed());
			self.root = n1a;
			self.manyNodes = 5;
			assertOK("OK", self.wellFormed());
		}

		public void testParent4() {
			self.root = n1;
			n1.right = n2;
			n2.parent = n1a;
			self.manyNodes = 2;
			assertNotOK("Not in tree", self.wellFormed());
			n1a.entry = i1;
			assertNotOK("Not in tree", self.wellFormed());
		}

		public void testOutOfOrder1() {
			self.root = n2;
			self.manyNodes = 1;
			assertOK("OK 1", self.wellFormed());
			n2.right = n4;
			n4.parent = n2;
			self.manyNodes = 2;
			assertOK("OK 2", self.wellFormed());
			n4.left = n3;
			n3.parent = n4;
			self.manyNodes = 3;
			assertOK("OK 3", self.wellFormed());
			n3.left = n1;
			n1.parent = n3;
			self.manyNodes = 4;
			assertNotOK("out of order", self.wellFormed());
			n2.left = n1;
			n1.parent = n2;
			n3.left = null;
			assertOK("OK 4", self.wellFormed());
		}

		public void testOutOfOrder2() {
			self.root = n1;
			self.manyNodes = 1;
			assertOK("OK 1", self.wellFormed());
			n1.right = n3;
			n3.parent = n1;
			self.manyNodes = 2;
			assertOK("OK 2", self.wellFormed());
			n3.left = n2;
			n2.parent = n3;
			self.manyNodes = 3;
			assertOK("OK 3", self.wellFormed());
			n3.right = n4;
			n4.parent = n3;
			self.manyNodes = 4;
			assertOK("OK 4", self.wellFormed());
			n4.left = n1a;
			n1a.parent = n4;
			n1a.entry = i2;
			self.manyNodes = 5;
			assertNotOK("out of order", self.wellFormed());
			n4.left = null;
			n4.right = n5;
			n5.parent = n4;
			assertOK("OK 5", self.wellFormed());
		}

		public void testAR1() {
			self.root = n2;
			n2.left = n1;
			n1.parent = n2;
			n2.right = n3;
			n3.parent = n2;
			self.manyNodes = 3;
			assertTrue(self.wellFormed());
			self.manyAtRisk = 1;
			assertFalse(self.wellFormed());
			self.firstAtRisk = n1;
			assertTrue(self.wellFormed());
			self.firstAtRisk = n2;
			assertTrue(self.wellFormed());
			n2.prevAtRisk = n1;
			assertFalse(self.wellFormed());
			n1.nextAtRisk = n2;
			assertFalse(self.wellFormed());
			self.firstAtRisk = n1;
			assertFalse(self.wellFormed());
			self.manyAtRisk = 2;
			assertTrue(self.wellFormed());
			n2.prevAtRisk = null;
			assertFalse(self.wellFormed());
			n2.prevAtRisk = n1a;
			assertFalse(self.wellFormed());
		}

		public void testAR2() {
			self.root = n2;
			n2.left = n1;
			n1.parent = n2;
			n2.right = n3;
			n3.parent = n2;
			self.manyNodes = 3;
			assertTrue(self.wellFormed());
			n2.prevAtRisk = n1;
			assertFalse(self.wellFormed());
			n1.nextAtRisk = n2;
			self.manyAtRisk = 2;
			self.firstAtRisk = n1;
			assertTrue(self.wellFormed());
			n3.nextAtRisk = n2;
			assertFalse(self.wellFormed());
			n3.nextAtRisk = null;
			n2.nextAtRisk = n4;
			n4.prevAtRisk = n2;
			self.manyAtRisk = 3;
			assertFalse(self.wellFormed());
			n3.right = n4;
			n4.parent = n3;
			self.manyNodes = 4;
			assertTrue(self.wellFormed());
		}
	}
}
