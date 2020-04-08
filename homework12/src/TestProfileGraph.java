import java.util.List;

import edu.uwm.cs351.util.Profile;
import junit.framework.TestCase;
import edu.uwm.cs351.ProfileGraph;
import snapshot.Snapshot;


public class TestProfileGraph extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileGraph.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}

	private ProfileGraph pg;
	
	private Profile p0,p1,p2,p3,p4,p5,p6,p7,p8;
	
	protected void setUp() {
		try {
			assert pg.numNodes() == 42;
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pg = new ProfileGraph();
		
		p0 = new Profile(null);
		p1 = new Profile("carol");
		p2 = new Profile("kellen");
		p3 = new Profile("james");
		p4 = new Profile("ariana");
		p5 = new Profile("joel");
		p6 = new Profile("noel");
		p7 = new Profile("kate");
		p8 = new Profile("penny");
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
	
	//test0x: some tests on empty Profile graph
	
	public void test00() {
		assertEquals(0, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test01() {
		assertFalse(pg.containsNode(p1));
		assertFalse(pg.containsEdge(p1, p2));
	}
	
	//test1x: adding some nodes
	
	public void test10() {
		assertTrue(pg.addNode(p1));
		assertFalse(pg.addNode(null));
		assertTrue(pg.addNode(p0));
		assertEquals(2, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test11() {
		assertTrue(pg.addNode(p1));
		assertFalse(pg.addNode(p1));
		assertEquals(1, pg.numNodes());
	}
	
	public void test12() {
		pg.addNode(p4);
		pg.addNode(p5);
		assertFalse(pg.containsNode(p1));
		assertTrue(pg.containsNode(p4));
		assertTrue(pg.containsNode(p5));
		pg.addNode(null);
		pg.addNode(p5);
		assertTrue(pg.containsNode(p5));
		assertFalse(pg.containsNode(null));
		assertEquals(2, pg.numNodes());
		assertFalse(pg.containsNode(p1));
	}
	
	//test2x: adding some edges
	
	public void test20() {
		pg.addNode(p1);
		pg.addNode(p2);
		assertTrue(pg.addEdge(p1, p2));
		assertEquals(2, pg.numNodes());
		assertEquals(1, pg.numEdges());
		assertTrue(pg.addEdge(p2, p1));
		assertEquals(2, pg.numEdges());
	}
	
	public void test21() {
		pg.addNode(p1);
		pg.addNode(p2);
		pg.addNode(p3);
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p3, p1);
		assertTrue(pg.containsEdge(p1, p2));
		assertTrue(pg.containsEdge(p1, p3));
		assertFalse(pg.containsEdge(p2, p1));
		assertFalse(pg.containsEdge(p2, p3));
		assertTrue(pg.containsEdge(p3, p1));
		assertFalse(pg.containsEdge(p3, p2));
	}
	
	public void test22() {
		assertTrue(pg.addEdge(p1, p2));
		assertTrue(pg.addEdge(p2, p3));
		assertTrue(pg.addEdge(p3, p1));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertTrue(pg.containsNode(p3));
		assertTrue(pg.containsEdge(p1, p2));
		assertTrue(pg.containsEdge(p2, p3));
		assertTrue(pg.containsEdge(p3, p1));
		assertEquals(3, pg.numNodes());
		assertEquals(3, pg.numEdges());
	}
	
	public void test23() {
		assertTrue(pg.addEdge(p1, p2));
		assertTrue(pg.addEdge(p1, p3));
		assertTrue(pg.addEdge(p1, p4));
		assertEquals(4, pg.numNodes());
		assertEquals(3, pg.numEdges());
	}
	
	public void test24() {
		pg.addEdge(p4, p6);
		assertFalse(pg.addEdge(p4, p6));
		assertTrue(pg.containsEdge(p4, p6));
		assertEquals(1, pg.numEdges());
		assertTrue(pg.addEdge(p6, p4));
		assertEquals(2, pg.numEdges());
		assertFalse(pg.addEdge(p4, new Profile(p6.getNickname())));
		assertFalse(pg.addEdge(new Profile(p4.getNickname()), p6));
		assertFalse(pg.addEdge(new Profile(p4.getNickname()), new Profile(p6.getNickname())));
		assertTrue(pg.containsEdge(p4, p6));
		assertEquals(2, pg.numEdges());
	}
	
	public void test25() {
		assertFalse(pg.addEdge(p1, null));
		assertFalse(pg.addEdge(null, p2));
		assertFalse(pg.addEdge(null, null));
		assertFalse(pg.containsNode(p1));
		assertFalse(pg.containsNode(p2));
		assertFalse(pg.containsNode(null));
		assertFalse(pg.containsEdge(p1, null));
		assertFalse(pg.containsEdge(null, p2));
		assertFalse(pg.containsEdge(null, null));
		assertEquals(0, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test26() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p2, p3);
		assertTrue(pg.containsNode(p2));
		assertTrue(pg.containsEdge(p2, p3));
		assertTrue(pg.containsEdge(p1, p2));
		Profile p2c = p2.clone();
		p2.setNickname("changed");
		assertFalse(pg.containsNode(p2));
		assertFalse(pg.containsEdge(p2, p3));
		assertFalse(pg.containsEdge(p1, p2));
		assertTrue(pg.containsNode(p2c));
		assertTrue(pg.containsEdge(p2c, p3));
		assertTrue(pg.containsEdge(p1, p2c));
	}
	
	public void test27() {
		assertFalse(pg.addEdge(p1, p1));
		assertFalse(pg.containsNode(p1));
		pg.addNode(p1);
		assertFalse(pg.addEdge(p1, p1));
		assertFalse(pg.containsEdge(p1, p1));
	}
	
	//test3x: removing edges
	
	public void test30() {
		assertFalse(pg.removeEdge(p1, p2));
	}
	
	public void test31() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p1);
		pg.removeEdge(p1, p2);
		assertFalse(pg.containsEdge(p1, p2));
		assertTrue(pg.containsEdge(p2, p1));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertEquals(2, pg.numNodes());
		assertEquals(1, pg.numEdges());
	}
	
	public void test32() {
		pg.addEdge(p1, p2);
		pg.removeEdge(p1, p2);
		assertFalse(pg.containsEdge(p1, p2));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertEquals(2, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test33() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p4, p1);
		pg.addEdge(p1, p5);
		pg.addNode(p7);
		assertEquals(4, pg.numEdges());
		assertEquals(6, pg.numNodes());
		assertFalse(pg.removeEdge(p1, p4));
		assertFalse(pg.removeEdge(p7, p1));
		assertFalse(pg.removeEdge(p1, p8));
		assertFalse(pg.removeEdge(p8, p2));
		assertFalse(pg.removeEdge(p6, p8));
		assertEquals(4, pg.numEdges());
		assertEquals(6, pg.numNodes());
		assertTrue(pg.removeEdge(p1, p2));
		assertFalse(pg.removeEdge(p1, p2));
		assertEquals(3, pg.numEdges());
		assertEquals(6, pg.numNodes());
	}
	
	public void test34() {
		pg.addEdge(p1, p2);
		assertFalse(pg.removeEdge(p1, null));
		assertFalse(pg.removeEdge(null, p2));
		assertFalse(pg.removeEdge(null, null));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertTrue(pg.containsEdge(p1, p2));
		assertEquals(1, pg.numEdges());
	}
	
	public void test35() {
		pg.addEdge(p1, p2);
		pg.addEdge(new Profile(p2.getNickname()), new Profile(p3.getNickname()));
		assertTrue(pg.containsNode(p3));
		assertTrue(pg.containsEdge(p2, p3));
		assertTrue(pg.removeEdge(p2.clone(), p3.clone()));
		assertFalse(pg.containsEdge(p2, p3));
	}
	
	//test4x: removing nodes
	
	public void test40() {
		assertFalse(pg.removeNode(p1));
	}
	
	public void test41() {
		pg.addNode(p1);
		assertTrue(pg.removeNode(p1));
		assertFalse(pg.containsNode(p1));
		assertEquals(0, pg.numNodes());
	}
	
	public void test42() {
		pg.addEdge(p1, p2);
		assertTrue(pg.removeNode(p1));
		assertFalse(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertFalse(pg.containsEdge(p1, p2));
		assertEquals(0, pg.numEdges());
	}
	
	public void test43() {
		pg.addEdge(p1, p2);
		assertTrue(pg.removeNode(p2));
		assertFalse(pg.containsNode(p2));
		assertTrue(pg.containsNode(p1));
		assertFalse(pg.containsEdge(p1, p2));
		assertEquals(0, pg.numEdges());
	}
	
	public void test44() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertTrue(pg.removeNode(p2));
		assertFalse(pg.containsNode(p2));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p3));
		assertFalse(pg.containsEdge(p1, p2));
		assertFalse(pg.containsEdge(p2, p3));
		assertEquals(0, pg.numEdges());
	}
	
	public void test45() {
		pg.addEdge(p1, p5);
		pg.addEdge(p2, p5);
		pg.addEdge(p3, p5);
		pg.addEdge(p4, p5);
		pg.addEdge(p5, p6);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p8);
		pg.removeNode(p5);
		assertFalse(pg.containsNode(p5));
		assertEquals(7, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test46() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p4);
		pg.addEdge(p4, p1);
		pg.addEdge(p6, p1);
		pg.addEdge(p5, p4);
		pg.addEdge(p2, p4);
		pg.addEdge(p4, p6);
		pg.removeNode(p1);
		assertEquals(4, pg.numNodes());
		assertEquals(3, pg.numEdges());
		assertFalse(pg.containsEdge(p1, p2));
		assertFalse(pg.containsEdge(p1, p4));
		assertFalse(pg.containsEdge(p4, p1));
		assertFalse(pg.containsEdge(p6, p1));
		assertTrue(pg.containsEdge(p5, p4));
		assertTrue(pg.containsEdge(p2, p4));
		assertTrue(pg.containsEdge(p4, p6));
	}
	
	public void test47() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertFalse(pg.removeNode(p4));
		assertFalse(pg.removeNode(null));
		assertEquals(3, pg.numNodes());
		assertEquals(2, pg.numEdges());
		assertTrue(pg.removeNode(new Profile(p2.getNickname())));
		assertFalse(pg.containsEdge(p2, p3));
		assertFalse(pg.containsEdge(p1, p2));
		assertFalse(pg.containsNode(p2));
	}
	
	//test5x: testing nodeList
	
	public void test50() {
		assertTrue(pg.nodeList().isEmpty());
	}
	
	public void test51() {
		pg.addNode(p1);
		pg.addNode(p2);
		pg.addNode(p3);
		assertEquals(3, pg.nodeList().size());
		assertTrue(pg.nodeList().contains(p1));
		assertTrue(pg.nodeList().contains(p2));
		assertTrue(pg.nodeList().contains(p3));
	}
	
	public void test52() {
		pg.addNode(p1);
		Profile p1c = p1.clone();
		p1.setNickname("changed");
		assertFalse(pg.nodeList().contains(p1));
		assertTrue(pg.nodeList().contains(p1c));
	}
	
	public void test53() {
		pg.addNode(p1);
		List<Profile> l = pg.nodeList();
		Profile change = l.get(0);
		change.setNickname("changed");
		l.add(p2);
		assertFalse(pg.containsNode(change));
		assertFalse(pg.containsNode(p2));
		assertTrue(pg.containsNode(p1));
		assertFalse(pg.nodeList().contains(change));
		assertFalse(pg.nodeList().contains(p2));
		assertTrue(pg.nodeList().contains(p1));
	}
	
	//test6x: testing connectedTo, connectedToAvoiding
	
	public void test61() {
		pg.addEdge(p1, p2);
		pg.addNode(p3);
		assertFalse(pg.connectedTo(p1, p3));
		assertFalse(pg.connectedTo(p2, p1));
		assertFalse(pg.connectedTo(p2, p3));
		assertFalse(pg.connectedTo(p3, p1));
		assertFalse(pg.connectedTo(p3, p2));
		assertTrue(pg.connectedTo(p1, p2));
	}
	
	public void test62() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		pg.addEdge(p3, p4);
		assertTrue(pg.connectedTo(p1, p2));
		assertTrue(pg.connectedTo(p1, p3));
		assertTrue(pg.connectedTo(p1, p4));
		assertTrue(pg.connectedTo(p2, p3));
		assertTrue(pg.connectedTo(p2, p4));
		assertTrue(pg.connectedTo(p3, p4));
		assertFalse(pg.connectedTo(p2, p1));
		assertFalse(pg.connectedTo(p3, p2));
		assertFalse(pg.connectedTo(p3, p1));
		assertFalse(pg.connectedTo(p4, p1));
		assertFalse(pg.connectedTo(p4, p2));
		assertFalse(pg.connectedTo(p4, p3));
	}
	
	public void test63() {
		pg.addNode(p1);
		pg.addEdge(p2, p3);
		assertTrue(pg.connectedTo(p1, p1));
		assertTrue(pg.connectedTo(p2, p2));
		assertTrue(pg.connectedTo(p3, p3));
	}
	
	public void test64() {
		pg.addEdge(p1, p2);
		assertFalse(pg.connectedTo(null, null));
		assertFalse(pg.connectedTo(null, p2));
		assertFalse(pg.connectedTo(p1, null));
		assertFalse(pg.connectedTo(p1, p3));
		assertFalse(pg.connectedTo(p3, p2));
		assertFalse(pg.connectedTo(p3, p3));
		assertFalse(pg.connectedTo(null, p3));
		assertFalse(pg.connectedTo(p3, null));
	}
	
	public void test65() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p1, p4);
		pg.addEdge(p2, p1);
		pg.addEdge(p3, p5);
		pg.addEdge(p4, p1);
		pg.addEdge(p5, p4);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p8);
		pg.addEdge(p6, p1);
		pg.addEdge(p8, p1);
		pg.addEdge(new Profile("start"), p1);
		assertTrue(pg.connectedTo(new Profile("start"), p7));
		assertFalse(pg.connectedTo(p5, new Profile("start")));
	}
	
	public void test66() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertFalse(pg.connectedToAvoiding(p1, p3, p2));
		assertTrue(pg.connectedToAvoiding(p1, p3));
	}
	
	public void test67() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertFalse(pg.connectedToAvoiding(p1, p3, p1));
		assertFalse(pg.connectedToAvoiding(p1, p3, p3));
	}
	
	public void test68() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p1, p4);
		pg.addEdge(p2, p1);
		pg.addEdge(p3, p5);
		pg.addEdge(p4, p1);
		pg.addEdge(p5, p4);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p8);
		pg.addEdge(p6, p1);
		pg.addEdge(p8, p1);
		pg.addEdge(new Profile("start"), p1);
		assertTrue(pg.connectedToAvoiding(new Profile("start"), p7, p2, p4));
		assertTrue(pg.connectedToAvoiding(p5, p3, p4, p2));
		assertFalse(pg.connectedToAvoiding(p5, p3, p4, p8));
		assertFalse(pg.connectedToAvoiding(p1, p7, p3));
		assertFalse(pg.connectedToAvoiding(p1, p7, p5));
		assertTrue(pg.connectedToAvoiding(p1, p7, p2, p4, p6, p8));
		assertFalse(pg.connectedToAvoiding(p1, p7, p2, p6, p8, p5));
	}
	
	public void test69() {
		pg.addEdge(p1, p2);
		assertFalse(pg.connectedToAvoiding(null, null));
		assertFalse(pg.connectedToAvoiding(null, p2));
		assertFalse(pg.connectedToAvoiding(p1, null));
		assertFalse(pg.connectedToAvoiding(p1, p3));
		assertFalse(pg.connectedToAvoiding(p3, p2));
		assertFalse(pg.connectedToAvoiding(p3, p3));
		assertFalse(pg.connectedToAvoiding(null, p3));
		assertFalse(pg.connectedToAvoiding(p3, null));
	}
	
	//test7x: testing pathTo, pathToAvoiding
	
	public void test70() {
		pg.addNode(p1);
		assertTrue(testPath(pg.pathTo(p1, p1), p1));
		pg.addEdge(p1, p2);
		assertTrue(testPath(pg.pathTo(p1, p2), p1, p2));
		assertTrue(testPath(pg.pathTo(p2, p1)));
		pg.addEdge(p2, p1);
		assertTrue(testPath(pg.pathTo(p2, p1), p2, p1));
	}
	
	public void test71() {
		pg.addEdge(p1, p2);
		pg.addNode(p3);
		assertTrue(testPath(pg.pathTo(p1, p3)));
		assertTrue(testPath(pg.pathTo(p2, p1)));
		assertTrue(testPath(pg.pathTo(p2, p3)));
		assertTrue(testPath(pg.pathTo(p3, p1)));
		assertTrue(testPath(pg.pathTo(p3, p2)));
		assertTrue(testPath(pg.pathTo(p1, p2), p1, p2));
	}
	
	public void test72() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		pg.addEdge(p3, p4);
		assertTrue(testPath(pg.pathTo(p1, p2), p1, p2));
		assertTrue(testPath(pg.pathTo(p1, p3), p1, p2, p3));
		assertTrue(testPath(pg.pathTo(p1, p4), p1, p2, p3, p4));
		assertTrue(testPath(pg.pathTo(p2, p3), p2, p3));
		assertTrue(testPath(pg.pathTo(p2, p4), p2, p3, p4));
		assertTrue(testPath(pg.pathTo(p3, p4), p3, p4));
		assertTrue(testPath(pg.pathTo(p2, p1)));
		assertTrue(testPath(pg.pathTo(p3, p2)));
		assertTrue(testPath(pg.pathTo(p3, p1)));
		assertTrue(testPath(pg.pathTo(p4, p1)));
		assertTrue(testPath(pg.pathTo(p4, p2)));
		assertTrue(testPath(pg.pathTo(p4, p3)));
	}
	
	public void test73() {
		pg.addNode(p1);
		pg.addEdge(p2, p3);
		assertTrue(testPath(pg.pathTo(p1, p1), p1));
		assertTrue(testPath(pg.pathTo(p2, p2), p2));
		assertTrue(testPath(pg.pathTo(p3, p3), p3));
	}
	
	public void test74() {
		pg.addEdge(p1, p2);
		assertTrue(testPath(pg.pathTo(null, null)));
		assertTrue(testPath(pg.pathTo(null, p2)));
		assertTrue(testPath(pg.pathTo(p1, null)));
		assertTrue(testPath(pg.pathTo(p1, p3)));
		assertTrue(testPath(pg.pathTo(p3, p2)));
		assertTrue(testPath(pg.pathTo(p3, p3)));
		assertTrue(testPath(pg.pathTo(null, p3)));
		assertTrue(testPath(pg.pathTo(p3, null)));
	}
	
	public void test75() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p1, p4);
		pg.addEdge(p2, p1);
		pg.addEdge(p3, p4);
		pg.addEdge(p3, p5);
		pg.addEdge(p4, p1);
		pg.addEdge(p5, p4);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p8);
		pg.addEdge(p6, p1);
		pg.addEdge(p8, p1);
		assertTrue(testPath(pg.pathTo(p6, p7), p6, p1, p3, p5, p7));
		assertTrue(testPath(pg.pathTo(p5, p6)));
		//While all three of the following paths are valid, BFS chooses the shortest
		assertTrue(testPath(pg.pathTo(p3, p1), p3, p4, p1));
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p8, p1));
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p4, p1));
		//BFS will choose the FIRST path because of the order the edges are added
		assertTrue(testPath(pg.pathTo(p5, p1), p5, p4, p1));
		assertFalse(testPath(pg.pathTo(p5, p1), p5, p8, p1));
	}
	
	//same as last test, but edges added in reverse order
	public void test76() {
		pg.addEdge(p8, p1);
		pg.addEdge(p6, p1);
		pg.addEdge(p5, p8);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p4);
		pg.addEdge(p4, p1);
		pg.addEdge(p3, p5);
		pg.addEdge(p3, p4);
		pg.addEdge(p2, p1);
		pg.addEdge(p1, p4);
		pg.addEdge(p1, p3);
		pg.addEdge(p1, p2);
		assertTrue(testPath(pg.pathTo(p6, p7), p6, p1, p3, p5, p7));
		assertTrue(testPath(pg.pathTo(p5, p6)));
		//While all three of the following paths are valid, BFS chooses the shortest
		assertTrue(testPath(pg.pathTo(p3, p1), p3, p4, p1));
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p8, p1));
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p4, p1));
		//BFS will choose the SECOND path because of the order the edges are added
		assertFalse(testPath(pg.pathTo(p5, p1), p5, p4, p1));
		assertTrue(testPath(pg.pathTo(p5, p1), p5, p8, p1));
	}
	
	public void test77() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertTrue(testPath(pg.pathToAvoiding(p1, p3, p2)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p3), p1, p2, p3));
	}
	
	public void test78() {
		pg.addEdge(p1, p2);
		pg.addEdge(p2, p3);
		assertTrue(testPath(pg.pathToAvoiding(p1, p3, p1)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p3, p3)));
	}
	
	public void test79() {
		pg.addEdge(p1, p2);
		pg.addEdge(p1, p3);
		pg.addEdge(p1, p4);
		pg.addEdge(p2, p1);
		pg.addEdge(p3, p4);
		pg.addEdge(p3, p5);
		pg.addEdge(p4, p1);
		pg.addEdge(p5, p4);
		pg.addEdge(p5, p7);
		pg.addEdge(p5, p8);
		pg.addEdge(p6, p1);
		pg.addEdge(p8, p1);
		assertTrue(testPath(pg.pathToAvoiding(p6, p7, p2, p4), p6, p1, p3, p5, p7));
		assertTrue(testPath(pg.pathToAvoiding(p5, p3, p4, p2), p5, p8, p1, p3));
		assertTrue(testPath(pg.pathToAvoiding(p5, p3, p4, p8)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p7, p3)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p7, p5)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p7, p2, p4, p6, p8), p1, p3, p5, p7));
		assertTrue(testPath(pg.pathToAvoiding(p1, p7, p2, p6, p8, p5)));
		assertTrue(testPath(pg.pathToAvoiding(p3, p1, p4), p3, p5, p8, p1));
		assertTrue(testPath(pg.pathToAvoiding(p3, p1, p5), p3, p4, p1));
		assertTrue(testPath(pg.pathToAvoiding(p3, p1, p2), p3, p4, p1));
		assertTrue(testPath(pg.pathToAvoiding(p5, p1, p7), p5, p4, p1));
		pg.addEdge(p7, p6);
		assertTrue(testPath(pg.pathToAvoiding(p3, p1), p3, p4, p1));
		assertTrue(testPath(pg.pathToAvoiding(p3, p1, p4), p3, p5, p8, p1));
		assertTrue(testPath(pg.pathToAvoiding(p3, p1, p4, p8), p3, p5, p7, p6, p1));
	}
	
	public void test80() {
		pg.addEdge(p1, p2);
		assertTrue(testPath(pg.pathToAvoiding(null, null)));
		assertTrue(testPath(pg.pathToAvoiding(null, p2)));
		assertTrue(testPath(pg.pathToAvoiding(p1, null)));
		assertTrue(testPath(pg.pathToAvoiding(p1, p3)));
		assertTrue(testPath(pg.pathToAvoiding(p3, p2)));
		assertTrue(testPath(pg.pathToAvoiding(p3, p3)));
		assertTrue(testPath(pg.pathToAvoiding(null, p3)));
		assertTrue(testPath(pg.pathToAvoiding(p3, null)));
	}
	
	public void test81() {
		pg.addEdge(p1, p2);
		Profile changed = pg.pathTo(p1, p2).get(0);
		assertTrue(pg.containsNode(changed));
		changed.setNickname("changed");
		assertFalse(pg.containsNode(changed));
		assertFalse(pg.containsEdge(p1, changed));
		assertTrue(pg.containsEdge(p1, p2));
	}
}
