
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.BSTSortedSet;

public class TestBSTSortedSet extends LockedTestCase {

	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown", true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	protected BSTSortedSet<Integer> c;
	protected BSTSortedSet<String> cs;
	private Iterator<Integer> it;
	private Integer p1, p2, p3, p4, p5;

	
	protected <T extends Comparable<T>> void testCollection(BSTSortedSet<T> l, String name, T... parts) {
		assertEquals(name + ".size()", parts.length, l.size());
		Iterator<T> it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			assertEquals(name + " element #" + i + " wrong", parts[i], it.next());
			++i;
		}
	}


	@Override
	protected void setUp() {
		try {
			assert c.size() == cs.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration", false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		initCollections();
	}
	/**
	 * Initialize c and cs.
	 */
	protected void initCollections() {
		c = new BSTSortedSet<Integer>();
		cs = new BSTSortedSet<String>();
		p1 = 1;
		p2 = 2;
		p3 = 3;
		p4 = 4;
		p5 = 5;
	}
	
	private void makeBigTree() {
		c.add(13);
		c.add(6);
		c.add(16);
		c.add(5);
		c.add(3);
		c.add(1);
		c.add(2);
		c.add(4);
		c.add(8);
		c.add(7);
		c.add(12);
		c.add(9);
		c.add(11);
		c.add(10);
		c.add(19);
		c.add(17);
		c.add(18);
		c.add(20);
		c.add(14);
		c.add(15);
	}
	
	/// Test Suites start here!
	
	public void test01() {
		assertTrue(c.isEmpty());
		assertEquals(0, c.size());
		assertTrue(cs.isEmpty());
		assertEquals(0, cs.size());
	}
	
	public void test02() {
		assertFalse(c.contains(0));
		assertFalse(c.contains(null));
		assertFalse(c.contains("hello"));
		assertFalse(cs.contains(0));
		assertFalse(cs.contains(null));
		assertFalse(cs.contains("hello"));
	}
	
	public void test03() {
		c.add(16);
		assertFalse(c.contains(new Object() {
			public String toString() {
				return "16";
			}
		}));
	}
	
	public void test04() {
		c.add(13);
		assertFalse(c.isEmpty());
		assertEquals(1, c.size());
		assertFalse(c.contains(null));
		assertFalse(c.contains(4));
		assertTrue(c.contains(13));
	}
	
	public void test05() {
		cs.add("apples");
		assertTrue(cs.contains("apples"));
		assertTrue(cs.contains(new String("apples")));
	}

	
	public void test06() {
		c.add(20);
		assertTrue(c.add(10));
		assertFalse(c.add(10));
		assertFalse(c.add(20));
		assertTrue(c.contains(10));
		assertEquals(2, c.size());
	}
	
	public void test07() {
		c.add(15);
		assertEquals(1, c.size());
		c.add(16);
		assertEquals(2, c.size());
		c.add(17);
		assertEquals(3, c.size());
		c.add(12);
		assertEquals(4, c.size());
		c.add(14);
		assertEquals(5, c.size());
		c.add(13);
		assertEquals(6, c.size());
		assertTrue(c.contains(12));
		assertTrue(c.contains(13));
		assertTrue(c.contains(14));
		assertTrue(c.contains(15));
		assertTrue(c.contains(16));
		assertTrue(c.contains(17));
	}
	
	public void test08() {
		cs.add("apples");
		assertTrue(cs.contains(new String("apples")));

		cs.add("18");
		assertFalse(cs.contains(18));
		
		try {
			c.add(null);
			assertFalse("adding null should throw exception",true);
		} catch (Exception ex) {
			assertTrue("add threw wrong exception ",ex instanceof IllegalArgumentException);
		}
	}
	
	public void test09() {
		cs.add("apples");
		cs.add("10");
		assertTrue(cs.contains("apples"));
		assertTrue(cs.contains("10"));
		assertFalse(cs.contains("sprouts"));
		assertFalse(cs.contains("candy"));
		assertFalse(cs.contains("0"));
		assertFalse(cs.contains(10));
		assertFalse(cs.contains(-10));
		assertFalse(cs.contains(null));
		assertEquals(2, cs.size());
	}
	
	public void test10() {
		c.add(10);
		c.remove(10);
		assertFalse(c.contains(10));
		assertEquals(0,c.size());
		c.add(44);
		assertTrue(c.contains(44));
	}
	
	public void test11() {
		c.add(10);
		c.add(5);
		c.remove(10);
		assertEquals(1, c.size());
	}
	
	public void test12() {
		c.add(10);
		c.add(15);
		c.remove(10);
		assertEquals(1, c.size());
	}
	
	public void test13() {
		c.add(10);
		c.add(5);
		c.add(15);
		c.remove(10);
		assertEquals(2, c.size());
		assertFalse(c.contains(10));
		assertTrue(c.contains(5));
		assertTrue(c.contains(15));
	}
	
	public void test14() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.remove(10);
		assertFalse(c.contains(10));
		assertEquals(2,c.size());
	}
	
	public void test15() {
		c.add(40);
		c.add(5);
		c.add(10);
		c.remove(10);
		assertFalse(c.contains(10));
		assertEquals(2,c.size());
		c.add(20);
		assertTrue(c.contains(5));
		assertTrue(c.contains(20));
	}
	
	public void test16() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(5);
		c.remove(10);
		c.add(3);
		assertTrue(c.contains(40));
		assertTrue(c.contains(3));
	}
	
	public void test17() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(5);
		c.add(3);
		c.add(7);
		c.remove(10);
		c.remove(5);
		c.remove(20);
		c.remove(3);
		assertTrue(c.contains(40));
		assertTrue(c.contains(7));
		assertEquals(2, c.size());
	}
	
	public void test18() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(15);
		c.remove(10);
		c.remove(15);
		c.add(2);
		c.add(6);
		assertFalse(c.contains(10));
		assertTrue(c.contains(6));
	}
	
	public void test19() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(15);
		c.add(13);
		c.add(18);
		c.remove(10);
		c.remove(15);
		c.add(2);
		c.add(6);
		assertFalse(c.contains(10));
		assertTrue(c.contains(6));
	}
	
	public void test20() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(5);
		c.add(15);
		c.remove(10);
		assertTrue(c.contains(5));
		c.remove(5);
		c.remove(15);
		assertEquals(2,c.size());
	}
	
	public void test21() {
		c.add(40);
		c.add(20);
		c.add(10);
		c.add(5);
		c.add(15);
		c.add(3);
		c.add(7);
		c.add(4);
		c.add(2);
		c.add(8);
		c.add(9);
		c.add(13);
		c.add(17);
		c.add(14);
		c.add(12);
		c.add(18);
		c.add(19);
		c.remove(10);
		c.add(11);
		c.add(16);
		c.remove(13);
		c.remove(18);
		c.remove(12);
		c.remove(9);
		c.add(12);
		assertEquals(15, c.size());
	}
	
	public void test22() {
		cs.add("hello");
		assertEquals(1, cs.size());
		cs.add(new String("hello"));
		assertEquals(1, cs.size());
		cs.add("goodbye");
		assertEquals(2, cs.size());
		cs.add("goodbye");
		assertEquals(2, cs.size());
		cs.add(new String("goodbye"));
		assertEquals(2, cs.size());
		cs.add("world");
		assertEquals(3, cs.size());
		cs.add(new String("hello"));
		cs.add(new String("world"));
		assertEquals(3, cs.size());
		assertTrue(cs.remove("world"));
		assertFalse(cs.remove("world"));
		assertFalse(cs.remove(null));
		assertFalse(cs.remove(17));
	}
	
	public void test23() {
		c.clear();
		c.add(10);
		c.add(15);
		c.add(12);
		c.add(3);
		c.clear();
		assertTrue(c.isEmpty());
		c.clear();
		assertTrue(c.isEmpty());
	}
	
	//Iterator
	public void test30() {
		it = c.iterator();
		assertFalse(it.hasNext());
	}
	public void test31() {
		c.add(51);
		it = c.iterator();
		assertTrue(it.hasNext());
	}
	public void test32() {
		c.add(52);
		it = c.iterator();
		assertEquals(52, it.next().intValue());
	}
	public void test33() {
		c.add(53);
		it = c.iterator();
		it.next();
		assertFalse(it.hasNext());
	}
	public void test34() {
		c.add(58);
		c.add(100);
		it = c.iterator();
		assertTrue(it.hasNext());
		assertEquals(58, it.next().intValue());
	}
	public void test35() {
		c.add(63);
		c.add(0);
		c.add(300);
		it = c.iterator();
		assertTrue(it.hasNext());
		assertEquals(0, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(63, it.next().intValue());
		assertTrue(it.hasNext());
		assertEquals(300, it.next().intValue());
		assertFalse(it.hasNext());
	}
	public void test36() {
		cs.add("hello");
		cs.add("world");
		cs.add("new");
		Iterator<String> it = cs.iterator();
		assertTrue(it.hasNext());
		assertEquals("hello", it.next());
		assertTrue(it.hasNext());
		assertEquals("new", it.next());
		assertTrue(it.hasNext());
		assertEquals("world", it.next());
		assertFalse(it.hasNext());
	}

	public void test37()
	{
		assertTrue(c.isEmpty());
		it = c.iterator();
		assertFalse(it.hasNext());
		c.add(1);
		it = c.iterator();
		assertTrue(it.hasNext());
		assertEquals(p1, it.next());
		assertFalse(it.hasNext());
	
		c.add(p2);
		testCollection(c,"{p1,p2}",p1,p2);
		c.add(p3);
		testCollection(c,"{p1,p2,p3}",p1,p2,p3);
		c.add(p4);
		testCollection(c,"{p1,p2,p3,p4}",p1,p2,p3,p4);
		
		c.clear();
		testCollection(c,"after clear");
		it = c.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test38() {
		c.add(10);
		c.add(5);
		c.add(16);
		c.add(2);
		c.add(8);
		c.add(20);
		c.add(6);
		c.add(9);
		// Suppose we have a tree
		//  10
		//  / \
		//  5 16
		// / \ \
		// 2 8 20
		//  / \
		// 6   9
		it = c.iterator(); // now we do a traversal:
		assertEquals(Ti(1329996532), it.next().intValue());
		assertEquals(Ti(1207684064), it.next().intValue());
		assertEquals(Ti(963639281), it.next().intValue());
		assertEquals(Ti(1842892443), it.next().intValue());
		assertEquals(Ti(953503697), it.next().intValue());
		assertEquals(Ti(1806839422), it.next().intValue());
		assertEquals(Ti(2038154801), it.next().intValue());
		assertEquals(Ti(990522457), it.next().intValue());
	}
	
	public void test41() {
		c.add(p1);
		it = c.iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testCollection(c,"{p1} after remove(p1)");
	}
	
	public void test42() {
		c.add(p2);
		c.add(p1);
		it = c.iterator();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p2, it.next());
		assertFalse(it.hasNext());
		testCollection(c,"{p1,p2} after remove(p1)",p2);
	}
	
	public void test43() {
		c.add(p1);
		c.add(p2);
		it = c.iterator();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testCollection(c,"{p1,p2} after remove(p2)",p1);
	}
	
	public void test44() {
		c.add(p3);
		c.add(p2);
		c.add(p1);
		it = c.iterator();
		assertEquals(p1, it.next());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p2, it.next());
		assertEquals(p3, it.next());
		assertFalse(it.hasNext());
		testCollection(c,"{p1,p2,p3} after remove(p1)",p2,p3);
	}

	public void test45() {
		c.add(p1);
		c.add(p3);
		c.add(p2);
		it = c.iterator();
		assertEquals(p1, it.next());
		assertEquals(p2, it.next());
		assertEquals(p3, it.next());
		it.remove();
		assertEquals(false, it.hasNext());
		it = c.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(p1, it.next());
		assertEquals(true, it.hasNext());
		assertEquals(p2, it.next());
		assertEquals(false, it.hasNext());
		testCollection(c,"{p1,p2,p3} after remove(p3)",p1,p2);	
	}

	public void test46() {
		c.add(p1);
		c.add(p2);
		c.add(p3);
		it = c.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		assertTrue(it.hasNext());
		assertEquals(p3,it.next());
		testCollection(c,"{p1,p2,p3} after remove(p1,p2)",p3);
	}

	public void test47() {
		c.add(p1);
		c.add(p2);
		c.add(p3);
		it = c.iterator();
		it.next(); it.remove();
		it.next();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		testCollection(c,"{p1,p2,p3} after remove(p1,p3)",p2);
	}

	public void test48() {
		c.add(p1);
		c.add(p2);
		c.add(p3);
		it = c.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		it = c.iterator();
		assertEquals(false, it.hasNext());
		testCollection(c,"{p1,p2,p3} after remove(p1,p2,p3)");
	}

	public void test49() 
	{
		c.add(p3);
		c.add(p2);
		c.add(p1);
		c.add(p4);
		testCollection(c,"{p1 p2 p3 p4}",p1,p2,p3,p4);
		
		it = c.iterator();
		it.next();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p3,it.next());
		assertTrue(it.hasNext());
		assertEquals(p4,it.next());
		assertFalse(it.hasNext());		
		testCollection(c,"{p1,p2,p3,p4} after remove(p2)",p1,p3,p4);
		
		c.clear();
		testCollection(c,"after clear");
	}
	
	public void test50() {
		makeBigTree();
		testCollection(c,"",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		
		it = c.iterator();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(8,it.next().intValue());
		testCollection(c,"",2,3,4,5,6,8,9,10,11,12,13,14,15,16,17,18,19,20);
		assertTrue(it.hasNext());
		assertEquals(9,it.next().intValue());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(10,it.next().intValue());
		testCollection(c,"",2,3,4,5,6,8,10,11,12,13,14,15,16,17,18,19,20);
		it.remove();
		assertTrue(it.hasNext());
		testCollection(c,"",2,3,4,5,6,8,11,12,13,14,15,16,17,18,19,20);
		
		it = c.iterator();
		it.next();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		testCollection(c,"",2,3,4,6,8,11,12,13,14,15,16,17,18,19,20);
	}
	
	public void test51() {
		makeBigTree();
		it = c.iterator();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.next();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		it.next();
		it.remove();
		testCollection(c,"",1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,18);
		
	}
	
	public void test61() {
		c.add(p1);
		c.add(p2);
		it = c.iterator();
		assertEquals(p1, it.next());
		c.clear();
		try {
			it.next();
			assertFalse("next() on stale iterator should throw exception",true);
		} catch (Exception ex) {
			assertTrue("next() threw wrong exception ",ex instanceof ConcurrentModificationException);
		}
	}
	
	public void test62() {
		c.add(p1);
		c.add(p2);
		c.add(p3);
		
		it = c.iterator();
		assertEquals(p1, it.next());
		assertEquals(p2, it.next());
		it.remove();
		assertEquals(p3, it.next());
		assertFalse(it.hasNext());
		Iterator<Integer> it2 = c.iterator();
		it.remove();
		try {
			it2.next();
			assertFalse("next() on stale iterator should throw exception",true);
		} catch (Exception ex) {
			assertTrue("next() threw wrong exception ",ex instanceof ConcurrentModificationException);
		}		
		it = c.iterator();
		assertEquals(p1, it.next());
		assertFalse(it.hasNext());
	}
	
	public void test63()
	{
		it = c.iterator();
		try {
			it.next();
			assertFalse("next() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.next() threw wrong exception ",ex instanceof NoSuchElementException);
		}
		assertFalse(it.hasNext());
		testCollection(c,"still empty");
	}
	
	public void test64()
	{
		it = c.iterator();
		try {
			it.remove();
			assertFalse("remove() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.remove() threw wrong exception ",ex instanceof IllegalStateException);
		}
		assertFalse(it.hasNext());
		testCollection(c,"still empty");

	}
	
	public void test65()
	{
		it = c.iterator();
		c.add(p3);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(c,"{p3}",p3);
	}
	
	public void test66()
	{
		c.add(p3);
		it = c.iterator();
		try {
			it.remove();
			assertTrue("remove() at start of iteration should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("just started remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertTrue(it.hasNext());
		assertEquals(p3, it.next());
		testCollection(c,"still {p3}",p3);
	}
	
	public void test67()
	{
		c.add(p4);
		it = c.iterator();
		it.next();
		it.remove();
		try {
			it.next();
			assertTrue("next() after removed only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after removal of only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertFalse("after removal of only element, hasNext() should still be false",it.hasNext());
		testCollection(c,"{p4} after remove (p4)");
	}
	
	public void test68()
	{
		c.add(p2);
		c.add(p4);
		it = c.iterator();
		it.next();
		it.remove();
		try {
			it.remove();
			assertTrue("remove() after remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertTrue(it.hasNext());
		assertEquals(p4, it.next());
		testCollection(c,"{p2,p4} after remove (p2)",p4);
	}
	
	public void test69()
	{
		c.add(p3);
		it = c.iterator();
		it.next();
		it.remove();
		c.add(p4);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(c,"{p4}",p4);
	}
	
	public void test70()
	{
		c.add(p2);
		it = c.iterator();
		it.next();
		try {
			it.next();
			assertTrue("next() after iterated past only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after iteration past only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after iteration past only element, hasNext() should still be false",(!it.hasNext()));
		testCollection(c,"{p2}",p2);
	}
	
	public void test71()
	{
		c.add(p5);
		c.add(p1);
		it = c.iterator();
		Iterator<Integer> it2 = c.iterator();
		it.next();
		it2.next();
		it.remove();
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}	
		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}		
		try {
			it.remove();
			assertTrue("remove() after first remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after first remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertTrue(it.hasNext());
		testCollection(c,"{p1,p5} after remove (p1)",p5);
		
	}
	
	public void test72() {
		c.add(p1);
		c.add(p3);
		it = c.iterator();
		it.next();
		it.remove();
		
		Iterator<Integer> it2 = c.iterator();
		it.next();
		it.remove();
		
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it2.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it.remove();
			assertTrue("remove() after second remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after second remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertTrue("after remove() after second remove(), hasNext() should still be false",(!it.hasNext()));
		testCollection(c,"{p1,p3} after remove (p1,p3)");
	}
	
	public void test73() {
		c.add(p1);
		it = c.iterator();
		c.clear();
		
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		it = c.iterator();
		c.clear();
		assertFalse(it.hasNext());
	}
	
	public void test74() {
		makeBigTree();
		it = c.iterator();
		c.remove(30); //not in the tree
		it.hasNext();
		c.remove(1);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		it = c.iterator();
		c.remove(20);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		it = c.iterator();
		c.remove(13);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
	}
	
	public void test80() {
		
		try {
			c.getNth(0);
			assertTrue("getNth on empty should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("getNth on empty threw wrong exception " + ex),(ex instanceof IllegalArgumentException));
		}
	}
	
	public void test81() {
		c.add(5);
		assertEquals(5, c.getNth(0).intValue());
	}
	
	public void test82() {
		c.add(5);
		
		try {
			c.getNth(-1);
			assertTrue("negative n should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("getNth() on negative n threw wrong exception " + ex),(ex instanceof IllegalArgumentException));
		}
		
		try {
			c.getNth(1);
			assertTrue("too high n should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("getNth() on too high n threw wrong exception " + ex),(ex instanceof IllegalArgumentException));
		}
		
	}
	
	public void test83() {
		makeBigTree();
		for(int i=0; i < c.size(); i++)
			assertEquals(i+1, c.getNth(i).intValue());
		
		try {
			c.getNth(-5);
			assertTrue("negative n should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("getNth() on negative n threw wrong exception " + ex),(ex instanceof IllegalArgumentException));
		}
		
		try {
			c.getNth(20);
			assertTrue("too high n should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("getNth() on too high n threw wrong exception " + ex),(ex instanceof IllegalArgumentException));
		}
	}
	
	public void test84() {
		makeBigTree();
		it = c.iterator();
		assertEquals(1, c.getNth(0).intValue());
		assertEquals(1, it.next().intValue());
		it.remove();
		assertEquals(2, c.getNth(0).intValue());
	}
	
	public void test90() {
		makeBigTree();
		c.add(22);
		c.remove(20);
		int i;
		for(it = c.iterator(); it.hasNext();) {
			i = it.next();
			if (i == 11)
				it.remove();
		}
		assertFalse(c.contains(11));
		assertEquals(15, c.getNth(13).intValue());
		c.add(-3);
		assertEquals(1, c.getNth(1).intValue());
		for(it = c.iterator(); it.hasNext();) {
			i = it.next();
			if (i % 2 == 0)
				it.remove();
		}
		c.add(6);
		testCollection(c,"",-3,1,3,5,6,7,9,13,15,17,19);
	}



}
