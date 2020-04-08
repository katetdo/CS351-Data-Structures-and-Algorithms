import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import snapshot.Snapshot;
import edu.uwm.cs351.Profile;
import edu.uwm.cs351.ProfileMap;


public class TestEfficiency extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileMap.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}
	
	private static final int POWER = 16;
	private static final int MAX = 1<<POWER;
	private static final int TESTS = 2000000;
	private ProfileMap pm;
	
	private Profile makeProfile(int i) {
		return new Profile(String.format("%d", i));
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			assert pm.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pm = new ProfileMap();for (int i=0; i < MAX; ++i) {
			pm.add(makeProfile(i));
		}
		for (int i=0; i < MAX; ++i) {
			pm.add(makeProfile(i), new Profile("data"));
		}
	}

	public void testSize() {
		for (int i=0; i < TESTS; ++i) {
			assertEquals(MAX+1,pm.size());
		}
	}
	
	public void testGet() {
		for (int i=0; i < TESTS/MAX; i++) {
			for (int j=0; j < MAX; ++j) {
				assertEquals(1,pm.get(makeProfile(j)).size());
			}
		}
	}
	
	public void testFindStochastic() {
		Random r = new Random();
		for (int i=0; i < TESTS; ++i) {
			List<Profile> l = pm.find(makeProfile(r.nextInt(MAX)));
			assertEquals(1,l.size());
		}
	}
	
	public void testFindAbsent() {
		for (int i=1; i < TESTS; ++i) {
			List<Profile> l = pm.find(makeProfile(-i));
			assertNull(l);
		}
	}
	
	public void testRemoveStochastic() {
		Random r = new Random();
		Set<Integer> removed = new TreeSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int h = r.nextInt(MAX);
			List<Profile> l = pm.remove(makeProfile(h));
			if (removed.add(h)) {
				assertEquals(1,l.size());
			} else {
				assertNull(l);
			}
		}
	}
	public void testGetAll() {
		assertEquals(MAX+1, pm.getAll().size());
	}
}
