
import edu.uwm.cs351.util.Queue;
import junit.framework.TestCase;


public class TestEfficiency extends TestCase {

	// These tests should take no more than a few seconds.
	private static final int BIG = 1 << 22; // 4 million
	private Queue<Integer> queue;
	
	protected void setUp() throws Exception {
		assert false : "Do not enable assertions for efficiency test!";
		super.setUp();
	}
	
	public void test00() {
		queue = new Queue<>();
		for (int i=0; i < BIG; ++i) {
			assertEquals(i,queue.size());
			queue.enqueue(i);
		}
		assertEquals(BIG,queue.clone().size());
	}
	
	public void test01() {
		queue = new Queue<>();
		for (int i=0; i < BIG; ++i)
			queue.enqueue(i);
		for (int i=0; i < BIG; ++i)
			queue.dequeue();
		assertTrue(queue.isEmpty());
	}
}
