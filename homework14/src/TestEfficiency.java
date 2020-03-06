import junit.framework.TestCase;
import snapshot.Snapshot;
import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.util.ProfileLink;
import edu.uwm.cs351.PriorityQueue;


public class TestEfficiency extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/PriorityQueue.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}
	
	private static final int POWER = 18;
	private static final int MAX = 1<<POWER;
	private static final int TESTS = 2000000;
	private PriorityQueue pq;
	
	private ProfileLink makeProfileLink(int i) {
		return new ProfileLink(new Profile(String.format("%d", i)),
				new Profile(String.format("%d", i)), i);
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			assert pq.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pq = new PriorityQueue();
		for (int i=0; i < MAX; i++) {
			pq.add(makeProfileLink(i));
		}
	}
	
	public void testSetup() {
		
	}
	
	public void testAdd() {
		for (int i=0; i < MAX; i++) {
			pq.add(makeProfileLink(i));
		}
	}

	public void testRemove() {
		while(!pq.isEmpty())
			pq.remove();
	}
	
	public void testSize() {
		for(int i=0; i<TESTS; i++)
			assertEquals(MAX, pq.size());
	}
	
	public void testIsEmpty() {
		for(int i=0; i<TESTS; i++)
			assertFalse(pq.isEmpty());
	}
}
