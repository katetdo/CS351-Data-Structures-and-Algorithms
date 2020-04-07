import java.io.File;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Painting;
import edu.uwm.cs351.Painting.SortedCollection;
import edu.uwm.cs351.util.Alphabetical_name;
import edu.uwm.cs351.util.ValueDescending;


public class TestSortedCollection extends LockedTestCase {
	
	private SortedCollection group;
	private Iterator<Painting> it, it2;
	private Painting p1, p2, p3, p4, p5;
	
	@Override
	protected void setUp() {
		group = new SortedCollection(Alphabetical_name.getInstance());
		Painting p0 = null;
		p1 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
		p2 = new Painting(new File("./Paintings/matisse.jpg"), "The Cat with Red Fish","Matisse", 1906, 33500000);
		p3 = new Painting(new File("./Paintings/courbet.jpg"), "The Desperate Man", "Courbet", 1845, 12000000);
		p4 = new Painting(new File("./Paintings/hieronymus_bosch.jpg"), "The Harrowing of Hell", "Hieronymus Bosch", 1460, 137500);
		p5 = new Painting(new File("./Paintings/rothko.jpg"), "Untitled", "Rothko", 1969, 8237000);

		try {
			assertFalse (p0.getArtist() == "Rothko's painting is overrated");
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (NullPointerException ex) {
			assertTrue(true);
			return;
		}

	}
	
	private Comparator<Painting> nondiscrimination = new Comparator<Painting>() {

		@Override
		public int compare(Painting o1, Painting o2) {
			return 0;
		}
		
	};
	
	protected void testCollection(SortedCollection group, String name, Painting... paintings)
	{
		assertEquals(name + ".size()",paintings.length,group.size());
		Iterator<Painting> it = group.iterator();
		int i=0;
		while (it.hasNext() && i < paintings.length) {
			assertEquals(name + "[" + i + "]",paintings[i],it.next());
			++i;
		}
		assertFalse(name + " too long",it.hasNext());
		assertFalse(name + " too short",(i < paintings.length));
	}
	
	public void test00() {
		//Unlock Tests
		group = new Painting.SortedCollection(Alphabetical_name.getInstance());
		
		// Painting constructor takes (file, name, artist, year, value)
		group.add(new Painting(null, "Jingyuan", "unknown", 1991, 80));
		assertEquals(Ts(978402736), group.getFirst().getName());
		assertEquals(Ts(1866459795), group.getLast().getName());
		
		group.add(new Painting(null, "Omar", "unknown", 2012, 40));
		assertEquals(Ts(353461707), group.getFirst().getName());
		assertEquals(Ts(988370360), group.getLast().getName());
		
		group.add(new Painting(null, "Fitz", "unknown", 1844, 30));
		assertEquals(Ts(29737522), group.getFirst().getName());
		assertEquals(Ts(1180449268), group.getLast().getName());
	}
	
	public void test01() {
		group = new SortedCollection(ValueDescending.getInstance());
		
		// Painting constructor takes (file, name, artist, year, value)
		group.add(new Painting(null, "Jingyuan", "unknown", 1992, 70));
		group.add(new Painting(null, "Bobby", "unknown", 1950, 80));
		assertEquals(Ts(1300594462), group.getLast().getName());
		group.add(new Painting(null, "Arnold", "unknown", 1953, 80));
		assertEquals(Ts(948989387), group.getFirst().getName());
	}

	public void test02()
	{
		assertTrue(group.isEmpty());
		it = group.iterator();
		assertFalse(it.hasNext());
		group.add(p1);
		it = group.iterator();
		assertTrue(it.hasNext());
		assertEquals(p1, it.next());
		assertFalse(it.hasNext());
	
		group.add(p2);
		testCollection(group,"{p1,p2}",p1,p2);
		group.add(p3);
		testCollection(group,"{p1,p2,p3}",p1,p2,p3);
		group.add(p4);
		testCollection(group,"{p1,p2,p3,p4}",p1,p2,p3,p4);
		
		group.clear();
		testCollection(group,"after clear");
		it = group.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test03() {
		group.add(p1);
		it = group.iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testCollection(group,"{p1} after remove(p1)");
	}
	
	public void test04() {
		group.add(p2);
		group.add(p1);
		it = group.iterator();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p2, it.next());
		assertFalse(it.hasNext());
		testCollection(group,"{p1,p2} after remove(p1)",p2);
	}
	
	public void test05() {
		try {
			group.add(null);
			assertFalse("adding null Painting should throw exception",true);
		} catch (Exception ex) {
			assertTrue("add(Painting) threw wrong exception ",ex instanceof IllegalArgumentException);
		}
		
		group.add(p1);
		group.add(p2);
		
		SortedCollection group2 = new SortedCollection(Alphabetical_name.getInstance());
		try {
			group2.add(p1);
			assertFalse("adding Painting to two groups should throw exception",true);
		} catch (Exception ex) {
			assertTrue("add(Painting) threw wrong exception ",ex instanceof IllegalArgumentException);
		}
		
		try {
			group2.add(p2);
			assertFalse("adding Painting to two groups should throw exception",true);
		} catch (Exception ex) {
			assertTrue("add(Painting) threw wrong exception ",ex instanceof IllegalArgumentException);
		}
	}
	
	public void test06() {
		group.add(p1);
		group.add(p2);
		it = group.iterator();
		assertEquals(p1, it.next());
		group.clear();
		try {
			it.next();
			assertFalse("next() on stale iterator should throw exception",true);
		} catch (Exception ex) {
			assertTrue("next() threw wrong exception ",ex instanceof ConcurrentModificationException);
		}
		
		// p1 and p2 were removed from other group, so we can add them to group2
		SortedCollection group2 = new SortedCollection(Alphabetical_name.getInstance());
		group2.add(p1);
		group2.add(p2);
		it2 = group2.iterator();
		assertEquals(p1, it2.next());
		assertEquals(p2, it2.next());
		assertFalse(it2.hasNext());
	}
	
	public void test07() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		
		it = group.iterator();
		assertEquals(p1, it.next());
		assertEquals(p2, it.next());
		it.remove(); // Remove p2 from group
		
		SortedCollection group2 = new SortedCollection(Alphabetical_name.getInstance());
		group2.add(p2);
		it2 = group2.iterator();
		assertEquals(p2, it2.next());
		
		assertEquals(p3, it.next());
		assertFalse(it.hasNext());
		it.remove(); // Remove p3 from group
		
		group2.add(p3);
		try {
			it2.next();
			assertFalse("next() on stale iterator should throw exception",true);
		} catch (Exception ex) {
			assertTrue("next() threw wrong exception ",ex instanceof ConcurrentModificationException);
		}
		it2 = group2.iterator();
		assertEquals(p2, it2.next());
		assertEquals(p3, it2.next());
		assertFalse(it2.hasNext());
		
		it = group.iterator();
		assertEquals(p1, it.next());
		assertFalse(it.hasNext());
	}
	
	public void test08() {
		group.add(p1);
		group.add(p2);
		it = group.iterator();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testCollection(group,"{p1,p2} after remove(p2)",p1);
	}
	
	public void test09() {
		group = new SortedCollection(Alphabetical_name.getInstance());
		p2 = new Painting(new File("./Paintings/courbet.jpg"), "The Desperate Man", "Courbet", 1845, 12000000);
		p1 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
		p3 = new Painting(new File("./Paintings/hieronymus_bosch.jpg"), "The Harrowing of Hell", "Hieronymus Bosch", 1460, 137500);
		
		
		group.add(p2);
		group.add(p1);
		group.add(p3);
		it = group.iterator();
		assertTrue(it.hasNext());
		//unlock tests
		assertEquals(Ts(1076862205), it.next().getName());
		assertEquals(Ts(319044975), it.next().getName());
		it.remove();
		assertEquals(Tb(105019561), it.hasNext());
		assertEquals(Ts(27322186), it.next().getName());
		assertFalse(it.hasNext());
		testCollection(group,"{p1,p2,p3} after remove(p2)",p1,p3);
				
	}
	
	public void test10() {
		group.add(p3);
		group.add(p2);
		group.add(p1);
		it = group.iterator();
		assertEquals(p1, it.next());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p2, it.next());
		assertEquals(p3, it.next());
		assertFalse(it.hasNext());
		testCollection(group,"{p1,p2,p3} after remove(p1)",p2,p3);
	}

	public void test11() {
		group.add(p1);
		group.add(p3);
		group.add(p2);
		it = group.iterator();
		assertEquals(p1, it.next());
		assertEquals(p2, it.next());
		assertEquals(p3, it.next());
		it.remove();
		assertEquals(false, it.hasNext());
		it = group.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(p1, it.next());
		assertEquals(true, it.hasNext());
		assertEquals(p2, it.next());
		assertEquals(false, it.hasNext());
		testCollection(group,"{p1,p2,p3} after remove(p3)",p1,p2);	
	}

	public void test12() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		it = group.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		assertTrue(it.hasNext());
		assertEquals(p3,it.next());
		testCollection(group,"{p1,p2,p3} after remove(p1,p2)",p3);
	}

	public void test13() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		it = group.iterator();
		it.next(); it.remove();
		it.next();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		testCollection(group,"{p1,p2,p3} after remove(p1,p3)",p2);
	}

	public void test14() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		it = group.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		it = group.iterator();
		assertEquals(false, it.hasNext());
		testCollection(group,"{p1,p2,p3} after remove(p1,p2,p3)");
	}

	public void test15() 
	{
		group.add(p1);
		group.add(p2);
		group.add(p3);
		group.add(p4);
		testCollection(group,"{p1 p2 p3 p4}",p1,p2,p3,p4);
		
		it = group.iterator();
		it.next();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p3,it.next());
		assertTrue(it.hasNext());
		assertEquals(p4,it.next());
		assertFalse(it.hasNext());		
		testCollection(group,"{p1,p2,p3,p4} after remove(p2)",p1,p3,p4);
		
		group.clear();
		testCollection(group,"after clear");
	}
	
	public void test16() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		group.add(p4);
		
		it = group.iterator();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p2,it.next());
		testCollection(group,"{p1,p2,p3,p4} after remove(p1)",p2,p3,p4);
		assertTrue(it.hasNext());
		assertEquals(p3,it.next());
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(p4,it.next());
		testCollection(group,"{p1,p2,p3,p4} after remove(p1,p3)",p2,p4);
		it.remove();
		assertFalse(it.hasNext());
		testCollection(group,"{p1,p2,p3,p4} after remove(p1,p3,p4)",p2);
		
		it = group.iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testCollection(group,"all removed");
		
	}
	
	public void test17() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		group.add(p4);
		
		it= group.iterator();
		it.next();
		it.remove();
		testCollection(group,"{p1,p2,p3,p4} after remove(p1)",p2,p3,p4);
		it = group.iterator();
		it.next();
		it.remove();
		testCollection(group,"{p1,p2,p3,p4} after remove(p1,p2)",p3,p4);
		it = group.iterator();
		it.next();
		it.next();
		it.remove();
		testCollection(group,"{p1,p2,p3,p4} after remove(p1,p2,p4)",p3);
		it = group.iterator();
		it.next();
		it.remove();
		testCollection(group,"all removed again");
	}
	
	public void test18()
	{
		it = group.iterator();
		try {
			it.next();
			assertFalse("next() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.next() threw wrong exception ",ex instanceof NoSuchElementException);
		}
		assertFalse(it.hasNext());
		testCollection(group,"still empty");
	}
	
	public void test19()
	{
		it = group.iterator();
		try {
			it.remove();
			assertFalse("remove() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.remove() threw wrong exception ",ex instanceof IllegalStateException);
		}
		assertFalse(it.hasNext());
		testCollection(group,"still empty");

	}
	
	public void test20()
	{
		it = group.iterator();
		group.add(p3);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(group,"{p3}",p3);
	}
	
	public void test21()
	{
		group.add(p3);
		it = group.iterator();
		try {
			it.remove();
			assertTrue("remove() at start of iteration should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("just started remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertTrue(it.hasNext());
		assertEquals(p3, it.next());
		testCollection(group,"still {p3}",p3);
	}
	
	public void test22()
	{
		group.add(p4);
		it = group.iterator();
		it.next();
		it.remove();
		try {
			it.next();
			assertTrue("next() after removed only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after removal of only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertFalse("after removal of only element, hasNext() should still be false",it.hasNext());
		testCollection(group,"{p4} after remove (p4)");
	}
	
	public void test23()
	{
		group.add(p2);
		group.add(p4);
		it = group.iterator();
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
		testCollection(group,"{p2,p4} after remove (p2)",p4);
	}
	
	public void test24()
	{
		group.add(p3);
		it = group.iterator();
		it.next();
		it.remove();
		group.add(p4);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(group,"{p4}",p4);
	}
	
	public void test25()
	{
		group.add(p2);
		it = group.iterator();
		it.next();
		try {
			it.next();
			assertTrue("next() after iterated past only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after iteration past only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after iteration past only element, hasNext() should still be false",(!it.hasNext()));
		testCollection(group,"{p2}",p2);
	}
	
	public void test26()
	{
		group.add(p5);
		group.add(p1);
		it = group.iterator();
		it2 = group.iterator();
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
		testCollection(group,"{p1,p5} after remove (p1)",p5);
		
	}
	
	public void test27() {
		group.add(p1);
		group.add(p3);
		it = group.iterator();
		it.next();
		it.remove();
		
		it2 = group.iterator();
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
		testCollection(group,"{p1,p3} after remove (p1,p3)");
	}
	
	public void test28() {
		group.add(p1);
		it = group.iterator();
		group.clear();
		
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
	}
	
	public void test29() {
		group = new SortedCollection(nondiscrimination);
		group.add(p3);
		group.add(p2);
		assertSame(p3,group.getFirst());
		assertSame(p2,group.getLast());
		group.add(p5);
		assertSame(p5,group.getLast());
	}
	
	public void test30() {
		
		group.add(p3);
		group.add(p2);
		Painting p1a = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz(Fake)", "Kirchner", 0, 0);
		Painting p2a = new Painting(new File("./Paintings/matisse.jpg"), "The Cat with Red Fish(Fake)","Matisse", 0, 0);
		Painting p3a = new Painting(new File("./Paintings/courbet.jpg"), "The Desperate Man(Fake)", "Courbet", 0, 0);
		group.add(p1a);
		group.add(p2a);
		group.add(p3a);
		it = group.iterator();
		assertSame(p1a,it.next());
		assertSame(p2,it.next());
		assertSame(p2a,it.next());
		assertSame(p3,it.next());
		assertSame(p3a,it.next());
	}
	
	public void test31() {
		SortedCollection copy = group.clone();
		assertEquals(0, copy.size());
		assertNull(copy.getFirst());
		assertNull(copy.getLast());
		copy.add(p1);
		copy.add(p2);
		//unlock test
		assertEquals(Ti(2127135785), group.size());
		try {
			group.add(p1);
			assertTrue("adding p1 to original group should have throw exception",false);
		}
		catch (Exception e) {
			assertEquals("What type of exception?", "IllegalArgumentException", e.getClass().getSimpleName());
		}
	}
	
	public void test32() {
		SortedCollection copy = group.clone();
		group.add(p1);
		group.add(p2);
		copy.add(p1.clone());
		copy.add(p2.clone());
		
		it = group.iterator();
		it2 = copy.iterator();
		while (it.hasNext() && it2.hasNext()) {
			Painting cur1 = it.next();
			Painting cur2 = it2.next();
			assertEquals(cur1.getName(), cur2.getName());
			assertEquals(cur1.getValue(), cur2.getValue());
			assertEquals(cur1.getYear(), cur2.getYear());
		}
		assertFalse("shouldn't have a next", it.hasNext());
		assertFalse("shouldn't have a next", it2.hasNext());
	}
	
	public void test33() {
		group.add(p1);
		group.add(p2);
		SortedCollection copy = group.clone();
		
		it = group.iterator();
		it.next();
		it.remove();
		//unlock tests
		assertEquals(Ti(570352297), copy.size());
		
		it2 = copy.iterator();
		assertTrue(it2.hasNext());
		it2.next();
		assertEquals(Tb(840018027), it2.hasNext());
		it2.next();
		assertFalse(it2.hasNext());
	}
	
	public void test34() {
		group.add(p1);
		group.add(p2);
		group.add(p3);
		group.clear();
		group.add(p4);
		// If any of the following adds fail, it probably means
		// that the Painting overrode clear to do the wrong thing.
		group.add(p2);
		group.add(p3); 
		group.add(p1);
		assertEquals(4,group.size());
	}
}
