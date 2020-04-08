import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import snapshot.Snapshot;
import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.ProfileGraph;


public class TestEfficiency extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileGraph.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}
	
	private static final int POWER = 16;
	private static final int MAX = 1<<POWER;
	private static final int SMALL_POWER = 12;
	private static final int SMALL_MAX = 1<<SMALL_POWER;
	private static final int LAYER_SIZE = 10;
	private static final int NODE_DEGREE = 10;
	private static final int TESTS = 2000000;
	private ProfileGraph pg;
	private Profile start;
	private Profile[] layer1;
	private Profile[] layer2;
	private Profile tailTop1;
	private Profile tailTop2;
	
	private Profile makeProfile(int i) {
		return new Profile(String.format("%d", i));
	}
	
	private boolean testPath(List<Profile> l, Profile... p) {
		if (l == null)
			return p.length == 0;
		int i;
		for(i=0; i<p.length; i++) {
			Profile cur = l.get(i);
			if(!cur.equals(p[i]))
				return false;
		}
		if(i != l.size())
			return false;
		return true;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			assert pg.numNodes() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pg = new ProfileGraph();
		for (int i=0; i < MAX; i++) {
			pg.addNode(makeProfile(i));
		}
	}
	
	private void makeCircle() {
		for (int i=0; i < MAX-1; i++) {
			pg.addEdge(makeProfile(i), makeProfile(i+1));
		}
		pg.addEdge(makeProfile(MAX-1), makeProfile(0));
	}
	
	private void makeSmallerStochastic() {
		pg = new ProfileGraph();
		for (int i=0; i < SMALL_MAX; i++) {
			pg.addNode(makeProfile(i));
		}
		Random rand = new Random();
		for (int i=0; i < SMALL_MAX; i++) {
			for (int j=0; j<NODE_DEGREE; j++) {
				pg.addEdge(makeProfile(i), makeProfile(rand.nextInt(SMALL_MAX)));
			}
		}
	}
	
	private void makeStochastic() {
		pg = new ProfileGraph();
		for (int i=0; i < MAX; i++) {
			pg.addNode(makeProfile(i));
		}
		Random rand = new Random();
		for (int i=0; i < MAX; i++) {
			for (int j=0; j<NODE_DEGREE; j++) {
				pg.addEdge(makeProfile(i), makeProfile(rand.nextInt(MAX)));
			}
		}
	}
	
	private void makeSearchBase() {
		start = new Profile("start");
		layer1 = new Profile[LAYER_SIZE];
		layer2 = new Profile[LAYER_SIZE];
		for(int i=0; i<LAYER_SIZE; i++) {
			layer1[i] = makeProfile(-1 - i);
			pg.addEdge(start, layer1[i]);
			
		}
		for(int i=0; i<LAYER_SIZE; i++) {
			layer2[i] = makeProfile(-1 - i - LAYER_SIZE);
			pg.addEdge(layer1[i], layer2[i]);
		}
		tailTop1 = makeProfile(0);
		for(int i=0; i<MAX/2; i++) {
			pg.addEdge(makeProfile(i), makeProfile(i+1));
		}
		tailTop2 = makeProfile(MAX/2 + 1);
		for(int i = MAX/2 + 1; i<MAX; i++) {
			pg.addEdge(makeProfile(i), makeProfile(i+1));
		}
	}

	public void testNumNodes() {
		for (int i=0; i < TESTS; i++) {
			assertEquals(MAX,pg.numNodes());
		}
	}

	public void testNumEdges() {
		makeCircle();
		for (int i=0; i < TESTS; i++) {
			assertEquals(MAX,pg.numEdges());
		}
	}
	
	public void testContainsNode() {
		for (int i=0; i < TESTS/MAX; i++) {
			for (int j=0; j < MAX; j++) {
				assertTrue(pg.containsNode(makeProfile(j)));
			}
		}
	}
	
	public void testContainsNodeFalse() {
		for (int i=1; i <= TESTS; i++) {
			assertFalse(pg.containsNode(makeProfile(-i)));
		}
	}
	
	public void testContainsEdge() {
		makeCircle();
		for (int i=0; i < TESTS/MAX; i++) {
			for (int j=0; j < MAX-1; j++) {
				assertTrue(pg.containsEdge(makeProfile(j), makeProfile(j+1)));
			}
			assertTrue(pg.containsEdge(makeProfile(MAX-1), makeProfile(0)));
		}
	}
	
	public void testContainsEdgeFalse() {
		makeCircle();
		for (int i=0; i < TESTS/MAX; i++) {
			for (int j=0; j < MAX; j++) {
				assertFalse(pg.containsEdge(makeProfile(j), makeProfile(j-1)));
			}
		}
	}
	
	public void testRemoveEdge() {
		for (int i=0; i < TESTS/MAX; i++) {
			makeCircle();
			for (int j=0; j < MAX-1; j++) {
				pg.removeEdge(makeProfile(j), makeProfile(j+1));
			}
			pg.removeEdge(makeProfile(MAX-1), makeProfile(0));
		}
	}
	
	public void testRemoveNode() {
		makeSmallerStochastic();
		for (int i=0; i < MAX; i++) {
			pg.removeNode(makeProfile(i));
		}
	}
	
	public void testNodeList() {
		for (int i=0; i < TESTS/MAX; i++) {
			assertEquals(MAX, pg.nodeList().size());
		}
	}
	
	public void testSearchStochastic() {
		makeStochastic();
		Profile begin;
		Profile end;
		do {
			begin = makeProfile(new Random().nextInt(MAX));
			end = makeProfile(new Random().nextInt(MAX));
		} while(!begin.equals(end));
		Profile easy = new Profile("easy path");
		pg.connectedTo(begin, end);
		pg.pathTo(begin, end);
		pg.addEdge(begin, easy);
		pg.addEdge(easy, end);
		pg.connectedToAvoiding(begin, end, easy);
		pg.pathToAvoiding(begin, end, easy);
	}
	
	public void testBFSDepth() {
		makeSearchBase();
		Profile target = layer2[layer2.length/2];
		pg.addEdge(layer2[0], tailTop1);
		pg.addEdge(layer2[layer2.length - 1], tailTop2);
		for (int i=0; i < TESTS; i++) {
			assertTrue(testPath(pg.pathTo(start, target), start, layer1[layer2.length/2], target));
		}
	}
	
	public void testDFS() {
		makeSearchBase();
		for (int i=0; i<layer2.length-1; i++)
			for (int j=1; j < MAX/(layer2.length-1); j++)
				pg.addEdge(layer2[i], makeProfile((layer2.length-1)*j+i));
		Profile target = new Profile("target");
		pg.addEdge(layer2[layer2.length-1], new Profile("link 1"));
		pg.addEdge(layer2[0], new Profile("link 1"));
		pg.addEdge(new Profile("link 1"), new Profile("link 2"));
		pg.addEdge(new Profile("link 2"), target);
		for (int i=0; i < TESTS; i++)
			assertTrue(pg.connectedTo(start, target));
	}
	
	public void testDFSOrder() {
		makeSearchBase();
		Profile target = layer2[layer2.length-1];
		pg.addEdge(layer2[0], tailTop1);
		for (int i=0; i < TESTS; i++) {
			assertTrue(pg.connectedTo(start, target));
		}
	}
}
