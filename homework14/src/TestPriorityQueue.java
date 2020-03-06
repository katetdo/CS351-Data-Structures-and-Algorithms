import java.util.NoSuchElementException;

import edu.uwm.cs351.util.ProfileLink;
import junit.framework.TestCase;
import edu.uwm.cs351.PriorityQueue;
import snapshot.Snapshot;


public class TestPriorityQueue extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/PriorityQueue.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}

	private PriorityQueue pq;
	
	private ProfileLink l(int i) {
		return new ProfileLink(null, null, i);
	}
	
	protected void setUp() {
		try {
			assert pq.size() == 42;
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pq = new PriorityQueue();
	}
	
	//test0x: some tests on empty PriorityQueue
	
	public void test00() {
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
	}
	
	//test1x: adding some elements
	
	public void test10() {
		assertTrue(pq.add(l(0)));
		assertFalse(pq.isEmpty());
		assertEquals(1, pq.size());
		assertTrue(pq.add(l(2)));
		assertFalse(pq.isEmpty());
		assertEquals(2, pq.size());
	}
	
	public void test11() {
		assertTrue(pq.add(l(0)));
		//adding identical link
		assertTrue(pq.add(l(0)));
		assertFalse(pq.isEmpty());
		assertEquals(2, pq.size());
	}
	
	public void test12() {
		for(int i=0; i<1000; i++)
			pq.add(l(i));
		assertEquals(1000, pq.size());
	}
	
	public void test13() {
		for(int i=1000; i>0; i--)
			pq.add(l(i));
		assertEquals(1000, pq.size());
	}
	
	public void test14() {
		try {
			pq.add(null);
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	//test2x: removing
	
	public void test20() {
		pq.add(l(0));
		assertEquals(l(0), pq.remove());
		assertTrue(pq.isEmpty());
		assertEquals(0, pq.size());
	}
	
	public void test21() {
		pq.add(l(2));
		pq.remove();
		pq.add(l(5));
		pq.remove();
		pq.add(l(1));
		pq.remove();
		pq.add(l(4));
		pq.remove();
		assertTrue(pq.isEmpty());
		assertEquals(0, pq.size());
	}
	
	public void test22() {
		pq.add(l(0));
		pq.add(l(2));
		assertEquals(l(0), pq.remove());
		assertEquals(l(2), pq.remove());
		assertTrue(pq.isEmpty());
	}
	
	public void test23() {
		pq.add(l(2));
		pq.add(l(0));
		assertEquals(l(0), pq.remove());
		assertEquals(l(2), pq.remove());
		assertTrue(pq.isEmpty());
	}
	
	public void test24() {
		for(int i=0; i<1000; i++)
			pq.add(l(i));
		for(int i=0; i<1000; i++)
			assertEquals(l(i), pq.remove());
	}
	
	public void test25() {
		for(int i=999; i>=0; i--)
			pq.add(l(i));
		for(int i=0; i<1000; i++)
			assertEquals(l(i), pq.remove());
	}
	
	public void test26() {
		pq.add(l(8));
		pq.add(l(4));
		pq.add(l(12));
		pq.add(l(6));
		pq.add(l(10));
		pq.add(l(2));
		pq.add(l(14));
		assertEquals(l(2), pq.remove());
		assertEquals(l(4), pq.remove());
		assertEquals(l(6), pq.remove());
		assertEquals(l(8), pq.remove());
		assertEquals(l(10), pq.remove());
		assertEquals(l(12), pq.remove());
		assertEquals(l(14), pq.remove());
	}
	
	public void test27() {
		pq.add(l(5));
		pq.add(l(5));
		pq.add(l(13));
		pq.add(l(9));
		pq.add(l(11));
		assertEquals(l(5), pq.remove());
		pq.add(l(5));
		pq.add(l(7));
		assertEquals(l(5), pq.remove());
		pq.add(l(3));
		pq.add(l(1));
		assertEquals(7, pq.size());
		assertEquals(l(1), pq.remove());
		assertEquals(l(3), pq.remove());
		assertEquals(l(5), pq.remove());
		assertEquals(l(7), pq.remove());
		pq.add(l(3));
		pq.add(l(1));
		pq.add(l(3));
		pq.add(l(11));
		assertEquals(l(1), pq.remove());
		assertEquals(l(3), pq.remove());
		assertEquals(l(3), pq.remove());
		assertEquals(l(9), pq.remove());
		assertEquals(l(11), pq.remove());
		assertEquals(l(11), pq.remove());
		assertEquals(l(13), pq.remove());
	}
	
	public void test28() {
		try {
			pq.remove();
		}
		catch(NoSuchElementException ex) {
		}
	}
}
