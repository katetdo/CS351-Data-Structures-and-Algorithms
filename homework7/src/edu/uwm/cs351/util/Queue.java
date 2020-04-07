package edu.uwm.cs351.util;

import java.util.NoSuchElementException;
import edu.uwm.cs.junit.LockedTestCase;

/*
 * This class implements a generic Queue, but does not use Java's Queue interface.
 * It uses a circular array data structure.
 * It does not allow null elements.
 */
public class Queue<E> implements Cloneable {

	/** Constants */
	private static final int DEFAULT_CAPACITY = 1; // force early resizing

	/** Fields */
	private E[] data;
	private int front;
	private int manyItems;

	private boolean report(String s) {
		System.out.println("invariant error: " + s);
		return false;
	}

	/** Invariant */
	private boolean wellFormed() {
		// The invariant:
		// 0. the data array cannot be null
		if (data==null) return report("data is null");
		// 1. front must be a valid index within the bounds of the array
		if (front<0 || front >=data.length) return report("front is incorrect");
		// 2. manyItems must not be negative or more than the length of the array
		if(manyItems<0 || manyItems>data.length) return report("manyItems is incorrect");
		// 3. If manyItems is not equal to 0, there are no null values in
		//		the range that holds elements
		//			NB: This range *may* wrap around the array.
		if (manyItems!=0){
			for (int i = front, j=0; j < manyItems; i--, j++){
				if (data[i]==null){
					return report("null value");
				}
				if (i==0){
					i=data.length;
				}
			}
	
		}
		return true;
	}

	/**
	 * a private helper method to compute where the back of the queue is
	 * AKA where enqueue should put the element
	 * NB: Do not use / or % operations.
	 * @return the index after the last element
	 */
	private int getBack(){		
		return Math.floorMod(front-manyItems, data.length);
	}

	/**
	 * Helper function to advance through the circular array.
	 * Keep in mind the direction the queue goes in the array.
	 * NB: Do not use / or % operations.
	 * @param i the index
	 * @return the next index after i
	 */
	private int nextIndex(int i) {
		if (i==0){
			return data.length-1;
		}
		return i-1;
	}

	private Queue(boolean ignored) {} // do not change: used by invariant checker.

	/** Create an empty Queue with capacity DEFAULT_CAPACITY. */
	public Queue() {
		data=makeArray(DEFAULT_CAPACITY);
		assert wellFormed() : "invariant failed at the end of constructor";
	}

	@SuppressWarnings("unchecked")
	private E[] makeArray(int s) {
		manyItems=0;
		front=0;
		return ((E[]) new Object[s]);
	}

	/**
	 * Determine whether the queue is empty.
	 * @return true if queue is empty
	 */
	public boolean isEmpty() {
		if (manyItems==0) return true;
		return false;
	}

	/**
	 * Compute how many elements are in the queue.
	 * @return how many elements are in this queue
	 */
	public int size() {
		return manyItems;
	}

	/**
	 * Add an element to the queue,
	 * @param x the element to add, must not be null
	 * @exception IllegalArgumentException if x is null
	 */
	public void enqueue(E x) {
		assert wellFormed() : "invariant violated at start of enqueue()";
		if (x==null) throw new IllegalArgumentException ("x cannot be null");

		ensureCapacity(manyItems+1);	
		data[getBack()]=x;
		manyItems++;

		assert wellFormed() : "invariant violated at end of enqueue()";
	}

	/**
	 * Return (but do not remove) the front element of this queue.
	 * @return element at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E front() {
		if (manyItems == 0) throw new NoSuchElementException("Queue underflow.");
		assert wellFormed() : "invariant violated at start of front()";
		return data[front];
	}

	/**
	 * Remove and return the front element from the queue.
	 * @return element formerly at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E dequeue() {
		if (manyItems == 0) throw new NoSuchElementException("Queue underflow.");
		assert wellFormed() : "invariant violated at start of dequeue()";
		
		E result = data[front];
		if (front==0){
			front=data.length-1;
		} else {
			front = Math.floorMod(front - 1 , data.length);
		}
		manyItems--;
		
		assert wellFormed() : "invariant violated at end of dequeue()";
		return result;
	}
	
	@Override
	public String toString() {
		String s="";
		for (int i=front, j=0; j<manyItems; i--, j++){
			s+=data[i].toString();
			if (i==0) i = data.length-1;
		}
		return s;
	}


	@Override
	@SuppressWarnings("unchecked")
	public Queue<E> clone()
	{
		Queue<E> result = null;
		try {
			result = (Queue<E>) super.clone( );
		}
		catch (CloneNotSupportedException e) {  
			// Shouldn't happen
		}
		result.data = data.clone();
		return result;
	}

	/**
	 * Ensure that the capacity of the array is such that
	 * at least minCap elements can be in queue.  If necessary,
	 * the capacity is doubled and the elements are arranged
	 * in the queue correctly. There is generally more
	 * than one valid arrangement for your data in the array.
	 * @param minCap the minimum capacity
	 */
	private void ensureCapacity(int minCap) {
		if (data.length >= minCap)
			return;

		int newCapacity = Math.max(data.length * 2, minCap);
		int x= manyItems;
		int a= front;
		
		E[] newData = makeArray(newCapacity);
		front=a;
		
		for (int i = front, j=0, k=0; k < x; i--, j--, k++){
			newData[j]=data[i];
			if (i==0) {
				i=data.length; 
			}
			if (j==0){
				j=newData.length;
			}
			
			manyItems++;
		}
		front=0;
		data = newData;
		assert wellFormed(): "invariant failed at the end of ensureCapacity";
	}


	public static class TestInvariant extends LockedTestCase {
		private Queue<Object> self;

		protected void setUp() {
			self = new Queue<Object>(false);
		}

		public void test00() {
			//data array is null
			assertFalse(self.wellFormed());
		}

		public void test01() {
			self.data = new Object[0];
			//Think about why this isn't well formed. Which field gives us a problem?
			assertFalse(self.wellFormed());
			self.data = new Object[DEFAULT_CAPACITY];
			assertTrue(self.wellFormed());
		}

		public void test02() {
			self.data = new Object[DEFAULT_CAPACITY];

			self.front = -1;
			assertFalse(self.wellFormed());
			
			self.front = DEFAULT_CAPACITY;
			assertFalse(self.wellFormed());
			
			self.front = 0;
			assertTrue(self.wellFormed());
			
		}

		public void test03() {
			self.data = new Object[] { null, null, null, null };
			//manyItems is 0

			self.front = 1;
			assertEquals(Tb(696236041), self.wellFormed());

			self.front = 2;
			assertEquals(true, self.wellFormed());
			
			self.front = 3;
			assertEquals(true, self.wellFormed());
			
			self.front = 0;
			assertEquals(true, self.wellFormed());
		}
		
		public void test04() {
			self.data = new Object[] { null, null, null, null };
			//front is 0
			
			self.manyItems = -1;
			assertEquals(false, self.wellFormed());
			
			self.manyItems = 5;
			assertEquals(false, self.wellFormed());

			self.manyItems = 1;
			assertEquals(Tb(1983819536), self.wellFormed());
		}
		
		public void test05() {
			self.data = new Object[] { new Integer(5), new Integer(3), new Integer(2), new Integer(4) };
			self.front = 3;
			self.manyItems = 4;
			assertEquals(Tb(1024095790), self.wellFormed());
			self.front = 1;
			assertEquals(true, self.wellFormed());
			self.manyItems = 0;
			assertEquals(true, self.wellFormed());
		}

		public void test06() {
			self.data = new Object[] { null, null, 6, null };
			self.manyItems = 1;
			//we do not care about array data outside of the queue range
			
			self.front = 1;
			assertEquals(Tb(269466524), self.wellFormed());

			self.front = 3;
			assertEquals(Tb(1201608649), self.wellFormed());

			self.front = 2;
			assertEquals(Tb(946336364), self.wellFormed());
		}

		public void test07() {
			self.data = new Object[] { 2, null, 6, 0 };
			self.manyItems = 2;
			
			//remember the queue goes right-to-left from the front
			self.front = 0;
			assertEquals(true, self.wellFormed());
			
			self.front = 1;
			assertEquals(Tb(1599463611), self.wellFormed());
			
			self.front = 2;
			assertEquals(Tb(1852259318), self.wellFormed());
			
			self.front = 3;
			assertEquals(Tb(1681530588), self.wellFormed());
		}

		public void test08() {
			self.data = new Object[] { 2, null, 6, 0 };

			self.front = 0;
			self.manyItems = 3;
			assertEquals(true, self.wellFormed());
			
		}

		public void test09() {
			self.data = new Object[] { 2, null, null, 0 };

			self.front = 2;
			self.manyItems = 2;
			assertEquals(false, self.wellFormed());

			self.front = 3;
			self.manyItems = 1;
			assertEquals(true, self.wellFormed());

			self.front = 0;
			self.manyItems = 2;
			assertEquals(true, self.wellFormed());
		}

		public void test10() {
			self.data = new Object[] { null };
			self.front = 0;
			self.ensureCapacity(1);
			assertEquals(1, self.data.length);
		}

		public void test11() {
			self.data = new Object[] { 1, null };
			self.front = 0;
			self.manyItems = 1;
			self.ensureCapacity(1);
			assertEquals(2, self.data.length);
			self.ensureCapacity(2);
			assertEquals(2, self.data.length);
			self.ensureCapacity(3);
			assertEquals(4, self.data.length);
		}

		public void test12() {
			self.data = new Object[] { 1, 2, null };
			self.front = 1;
			self.manyItems = 2;
			self.ensureCapacity(3);
			assertEquals(3, self.data.length);
			self.ensureCapacity(6);
			assertEquals(6, self.data.length);
		}

		public void test13() {
			self.data = new Object[4];
			self.front = 0;
			self.ensureCapacity(300);
			assertEquals(300, self.data.length);
		}

		public void test14() {
			self.data = new Object[100];
			self.front = 0;
			self.ensureCapacity(101);
			assertEquals(200, self.data.length);
		}

		public void test15() {
			self.data = new Object[8];
			assertEquals(Ti(1882901848), self.nextIndex(7));
			assertEquals(Ti(466756724), self.nextIndex(1));
			assertEquals(Ti(1207052122), self.nextIndex(0));
		}
		
		
		public void test16() {
			self.data = new Object[] { null, null, 8, 3, 0 };
			self.front = 4;
			self.manyItems = 3;
			assertEquals(Ti(455994013), self.getBack());
			
			self.data = new Object[] { 3, 0, null, null, 8 };
			self.front = 1;
			self.manyItems = 3;
			assertEquals(Ti(52298907), self.getBack());
			
			self.data = new Object[] { 0, null, null, 8, 3 };
			self.front = 0;
			self.manyItems = 3;
			assertEquals(Ti(1968034367), self.getBack());
			
		}
		
		public void test17() {
			self.data = new Object[] {8, 3, 0 };
			self.front = 2;
			self.manyItems = 0;
			assertEquals(2, self.getBack());
			self.manyItems = 1;
			assertEquals(1, self.getBack());
			self.manyItems = 2;
			assertEquals(0, self.getBack());
			self.manyItems = 3;
			assertEquals(2, self.getBack());
		}
		
		public void test18() {
			self.data = new Object[] {4};
			self.front = 0;
			self.manyItems = 0;
			assertEquals(0, self.getBack());
			self.manyItems = 1;
			assertEquals(0, self.getBack());
		}
	}
}
