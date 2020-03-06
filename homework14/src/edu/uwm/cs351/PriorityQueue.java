package edu.uwm.cs351;
//Looked at tips from geeks for geeks website
import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.util.ProfileLink;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import snapshot.Snapshot;

public class PriorityQueue {
	/**
	 * An priority queue using an array-based ternary tree heap
	 */
	private final int INITIAL_CAPACITY = 7;
	private ProfileLink[] data;
	private int manyItems;
	private final Comparator<ProfileLink> COMP = new Comparator<ProfileLink>() {
		public int compare(ProfileLink a, ProfileLink b) {return a.cost - b.cost;}
	};


	private static boolean doReport = true;
	private boolean report(String message) {
		if (doReport) System.err.println("Invariant error: " + message);
		return false;
	}

	protected PriorityQueue(boolean ignored) {} // don't change: used by invariant checker
	
	
	private boolean wellFormed() {
		// TODO: Check invariant
		// 1. data array can't be null
		// 2. manyItems must be between 0 and data's length
		// 3. no null elements
		// 4. all children are larger than their parents
		
		if (data == null) {
			return report("data is null");
		} else if (manyItems<0 || manyItems>data.length) {
			return report("manyItems is incorrect");
		} 

		for (int i = 0; i < manyItems; i++) {
			if (data[i] == null) {
				return report("element is null");
			}
			if (!child(i)) {
				return report("children are not smaller than node");
			}
		}
		
		return true;
	}
	
	//TODO: optional helper method to check the children of a given node

	private boolean child(int i) {
		if ((left(i)) < data.length  && data[left(i)] !=null && 
				COMP.compare(data[i], data[left(i)]) > 0)
			return false;
		else if ((mid(i)) < data.length && data[mid(i)] !=null && 
				COMP.compare(data[i], data[mid(i)]) > 0)
			return false;
		else if ((right(i)) < data.length && data[right(i)] !=null && 
				COMP.compare(data[i], data[right(i)]) > 0)
			return false;
		return true;
	}

	/**
	 * Create a new PriorityQueue
	 */
	public PriorityQueue() {
		data = new ProfileLink[INITIAL_CAPACITY];
		assert wellFormed() : "invariant broken at end of constructor";
	}
	
	/**
	 * Add a ProfileLink to the PriorityQueue
	 * @param l the ProfileLink to be added
	 * @return true
	 * @throws IllegalArgumentException for null ProfileLink
	 */
	public boolean add(ProfileLink l) {
		// TODO: add a ProfileLink to the end of the array, and then bubble it up
		assert wellFormed(): "invariant failed at the start of add";
		if (l == null)
			throw new IllegalArgumentException("Profile link is null");
		
		if (manyItems+1>=data.length) ensureCapacity(manyItems+1);
		data[manyItems] = l;
		int i = manyItems;
		while (COMP.compare(data[i],data[parent(i)])<0) {
			swap(i, parent(i));
			i = parent(i);
		}
		manyItems++;
		
		assert wellFormed(): "invariant failed at the end of add";
		return true;
	}
	
	/**
	 * Remove and return the smallest ProfileLink from the PriorityQueue
	 * @return the smallest ProfileLink, now removed
	 * @throws NoSuchElementException if empty
	 */
	public ProfileLink remove() {
		//TODO: remove and return the first Profile Link
		//    replace it with the last ProfileLink
		//    and then bubble it down
		if (isEmpty()) throw new NoSuchElementException("Nothing to remove");
		assert wellFormed(): "invariant failed at the start of remove";
		ProfileLink returning = data[0];
		data[0]=data[manyItems-1];
		data[manyItems-1]=null;
		bubbleDown(0);
		manyItems--;
		assert wellFormed(): "invariant failed at the end of remove";
		return returning;
	}
	
	/**
	 * Return the size of the PriorityQueue
	 * @return the number of elements
	 */
	public int size() {
		return manyItems;
	}
	
	/**
	 * Check if the PriorityQueue is empty
	 * @return whether the PriorityQueue is empty
	 */
	public boolean isEmpty() {
		if (manyItems==0) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		ProfileLink [] newArray;
		if(data.length < minimumCapacity)
		{
			int newCapacity = data.length *2;
			if(newCapacity < minimumCapacity)
				newCapacity = minimumCapacity;
			newArray = new ProfileLink[newCapacity];
			for(int i=0; i<manyItems; i++)
				newArray[i] = data[i];
			data = newArray;
		}
	}
	
	// TODO: optional helper methods
	private void bubbleDown(int i) {
		if (i >= data.length || data[i] == null) {
			return;
		}
		if (COMP.compare(data[i], data[parent(i)]) < 0) {
			swap(i, parent(i));
		}
		bubbleDown(left(i));
		bubbleDown(mid(i));
		bubbleDown(right(i));

	}
	private void swap(int x, int y) 
    { 
       ProfileLink tmp; 
        tmp = data[x]; 
        data[x] = data[y]; 
        data[y] = tmp; 
    } 	
	private int parent(int i) { return (i-1)/3;}
	private int left(int i) { return i*3+1;}
	private int mid(int i) { return i*3+2;}
	private int right(int i) { return i*3+3;}
	
	public static class TestInvariant extends TestCase {
		private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/PriorityQueue.java"};
		private static boolean firstRun = true;
		
		public void log() {
			System.out.println("running");
			Snapshot.capture(TO_LOG);
		}
		
		PriorityQueue self;
		Profile a = new Profile("a");
		Profile b = new Profile("b");
		Profile c = new Profile("c");
		
		private ProfileLink l(int i) {
			return new ProfileLink(null, null, i);
		}

		@Override
		public void setUp() {
			if(firstRun) {
				log();
				firstRun = false;
			}
			self = new PriorityQueue(false);
		}
		
		public void testNull() {
			assertFalse(self.wellFormed());
			self.manyItems = 0;
			assertFalse(self.wellFormed());
		}

		public void testEmpty() {
			self.data = new ProfileLink[0];
			self.manyItems = 0;
			assertTrue(self.wellFormed());
			self.manyItems = -1;
			assertFalse(self.wellFormed());
			self.manyItems = 1;
			assertFalse(self.wellFormed());
		}
		
		public void testNullElements() {
			self.data = new ProfileLink[1];
			self.manyItems = 1;
			assertFalse(self.wellFormed());
			self.data = new ProfileLink[3];
			self.data[0] = new ProfileLink(a, b);
			self.data[2] = new ProfileLink(b, c);
			self.manyItems = 2;
			assertFalse(self.wellFormed());
			self.manyItems = 3;
			assertFalse(self.wellFormed());
			self.manyItems = 1;
			assertTrue(self.wellFormed());
		}
		
		public void testSmall() {
			self.data = new ProfileLink[2];
			self.data[0] = l(1);
			self.data[1] = l(1);
			self.manyItems = 2;
			assertTrue(self.wellFormed());
			self.data[1] = l(2);
			assertTrue(self.wellFormed());
			self.data[1] = l(0);
			assertFalse(self.wellFormed());
		}
		
		public void testMedium() {
			self.data = new ProfileLink[4];
			self.data[0] = l(3);
			self.data[1] = l(8);
			self.data[2] = l(5);
			self.data[3] = l(12);
			self.manyItems = 4;
			assertTrue(self.wellFormed());
			self.data[1] = l(2);
			assertFalse(self.wellFormed());
			self.data[1] = l(12);
			self.data[2] = l(1);
			assertFalse(self.wellFormed());
			self.data[2] = l(11);
			self.data[3] = l(1);
			assertFalse(self.wellFormed());
			self.data[3] = l(4);
			assertTrue(self.wellFormed());
		}
		
		public void testLarge() {
			self.data = new ProfileLink[21];
			
			self.data[0] = l(5);
			
			self.data[1] = l(11);
			self.data[2] = l(88);
			self.data[3] = l(14);

			self.data[4] = l(12);
			self.data[5] = l(36);
			self.data[6] = l(22);
			self.data[7] = l(89);
			self.data[8] = l(100);
			self.data[9] = l(99);
			self.data[10] = l(53);
			self.data[11] = l(15);
			self.data[12] = l(15);

			self.data[13] = l(13);
			self.data[14] = l(13);
			self.data[15] = l(14);
			self.data[16] = l(65);
			self.data[17] = l(39);
			self.data[18] = l(44);
			
			self.manyItems = 19;
			assertTrue(self.wellFormed());
			self.data[7] = l(86);
			assertFalse(self.wellFormed());
			self.data[7] = l(89);
			self.data[8] = l(86);
			assertFalse(self.wellFormed());
			self.data[8] = l(100);
			self.data[9] = l(86);
			assertFalse(self.wellFormed());
			self.data[9] = l(99);
			

			self.data[16] = l(35);
			assertFalse(self.wellFormed());
			self.data[16] = l(65);
			self.data[17] = l(35);
			assertFalse(self.wellFormed());
			self.data[17] = l(39);
			self.data[18] = l(35);
			assertFalse(self.wellFormed());
			self.data[18] = l(44);
			
			self.data[4] = l(14);
			assertFalse(self.wellFormed());
			self.data[4] = l(12);
			self.data[5] = l(40);
			assertFalse(self.wellFormed());
			self.data[5] = l(36);
			
			self.data[0] = l(11);
			assertTrue(self.wellFormed());
		}
	}

}
