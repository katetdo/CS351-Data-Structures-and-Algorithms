import java.util.List;

import edu.uwm.cs351.util.Profile;
import junit.framework.TestCase;
import edu.uwm.cs351.WeightedProfileGraph;
import snapshot.Snapshot;


public class TestProfileGraph extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/PriorityQueue.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}

	private WeightedProfileGraph pg;
	
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
		pg = new WeightedProfileGraph();
		
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
		assertEquals(-1, pg.getEdge(p1, p2));
	}
	
	//test1x: adding some nodes
	
	public void test10() {
		assertTrue(pg.addNode(p1));
		try {
			pg.addNode(null);
		} catch (IllegalArgumentException ex) {
		}
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
		try {
			pg.addNode(null);
		} catch (IllegalArgumentException ex) {
		}
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
		assertEquals(-1, pg.addEdge(p1, p2, 0));
		assertEquals(2, pg.numNodes());
		assertEquals(1, pg.numEdges());
		assertEquals(-1, pg.addEdge(p2, p1, 0));
		assertEquals(2, pg.numEdges());
	}
	
	public void test21() {
		pg.addNode(p1);
		pg.addNode(p2);
		pg.addNode(p3);
		pg.addEdge(p1, p2, 0);
		pg.addEdge(p1, p3, 0);
		pg.addEdge(p3, p1, 0);
		assertEquals(0, pg.getEdge(p1, p2));
		assertEquals(0, pg.getEdge(p1, p3));
		assertEquals(-1, pg.getEdge(p2, p1));
		assertEquals(-1, pg.getEdge(p2, p3));
		assertEquals(0, pg.getEdge(p3, p1));
		assertEquals(-1, pg.getEdge(p3, p2));
	}
	
	public void test22() {
		assertEquals(-1, pg.addEdge(p1, p2, 0));
		assertEquals(-1, pg.addEdge(p2, p3, 0));
		assertEquals(-1, pg.addEdge(p3, p1, 0));
		assertTrue(pg.containsNode(p1));
		assertTrue(pg.containsNode(p2));
		assertTrue(pg.containsNode(p3));
		assertEquals(0, pg.getEdge(p1, p2));
		assertEquals(0, pg.getEdge(p2, p3));
		assertEquals(0, pg.getEdge(p3, p1));
		assertEquals(3, pg.numNodes());
		assertEquals(3, pg.numEdges());
	}
	
	public void test23() {
		assertEquals(-1, pg.addEdge(p1, p2, 0));
		assertEquals(-1, pg.addEdge(p1, p3, 0));
		assertEquals(-1, pg.addEdge(p1, p4, 0));
		assertEquals(4, pg.numNodes());
		assertEquals(3, pg.numEdges());
	}
	
	public void test24() {
		pg.addEdge(p4, p6, 0);
		assertEquals(0, pg.addEdge(p4, p6, 1));
		assertEquals(1, pg.addEdge(p4, p6, 2));
		assertEquals(2, pg.getEdge(p4, p6));
		assertEquals(1, pg.numEdges());
		assertEquals(-1, pg.addEdge(p6, p4, 0));
		assertEquals(2, pg.numEdges());
		assertEquals(2, pg.getEdge(p4, p6));
		assertEquals(0, pg.getEdge(p6, p4));
	}
	
	public void test25() {
		try {pg.addEdge(p1, null, 0);}
		catch (IllegalArgumentException ex) {}
		try {pg.addEdge(null, p2, 0);}
		catch (IllegalArgumentException ex) {}
		try {pg.addEdge(null, null, 0);}
		catch (IllegalArgumentException ex) {}
		assertFalse(pg.containsNode(p1));
		assertFalse(pg.containsNode(p2));
		assertFalse(pg.containsNode(null));
		assertEquals(-1, pg.getEdge(p1, null));
		assertEquals(-1, pg.getEdge(null, p2));
		assertEquals(-1, pg.getEdge(null, null));
		assertEquals(0, pg.numNodes());
		assertEquals(0, pg.numEdges());
	}
	
	public void test26() {
		try {pg.addEdge(p1, p1, 0);}
		catch (IllegalArgumentException ex) {}
		assertFalse(pg.containsNode(p1));
		pg.addNode(p1);
		try {pg.addEdge(p1, p1, 0);}
		catch (IllegalArgumentException ex) {}
		assertEquals(-1, pg.getEdge(p1, p1));
	}
	
	public void test27() {
		pg.addNode(p1);
		assertEquals(-1, pg.getEdge(p1, p2));
		assertEquals(-1, pg.getEdge(p2, p1));
		assertEquals(-1, pg.getEdge(p2, p3));
	}
	

	
	//test3x: testing nodeList
	
	public void test30() {
		assertTrue(pg.nodeList().isEmpty());
	}
	
	public void test31() {
		pg.addNode(p1);
		pg.addNode(p2);
		pg.addNode(p3);
		assertEquals(3, pg.nodeList().size());
		assertTrue(pg.nodeList().contains(p1));
		assertTrue(pg.nodeList().contains(p2));
		assertTrue(pg.nodeList().contains(p3));
	}
	
	//test4x: testing connectedTo
	
	public void test41() {
		pg.addEdge(p1, p2, 0);
		pg.addNode(p3);
		assertFalse(pg.connectedTo(p1, p3));
		assertFalse(pg.connectedTo(p2, p1));
		assertFalse(pg.connectedTo(p2, p3));
		assertFalse(pg.connectedTo(p3, p1));
		assertFalse(pg.connectedTo(p3, p2));
		assertTrue(pg.connectedTo(p1, p2));
	}
	
	public void test42() {
		pg.addEdge(p1, p2, 0);
		pg.addEdge(p2, p3, 0);
		pg.addEdge(p3, p4, 0);
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
	
	public void test43() {
		pg.addNode(p1);
		pg.addEdge(p2, p3, 0);
		assertTrue(pg.connectedTo(p1, p1));
		assertTrue(pg.connectedTo(p2, p2));
		assertTrue(pg.connectedTo(p3, p3));
	}
	
	public void test44() {
		pg.addEdge(p1, p2, 0);
		assertFalse(pg.connectedTo(null, null));
		assertFalse(pg.connectedTo(null, p2));
		assertFalse(pg.connectedTo(p1, null));
		assertFalse(pg.connectedTo(p1, p3));
		assertFalse(pg.connectedTo(p3, p2));
		assertFalse(pg.connectedTo(p3, p3));
		assertFalse(pg.connectedTo(null, p3));
		assertFalse(pg.connectedTo(p3, null));
	}
	
	public void test45() {
		pg.addEdge(p1, p2, 0);
		pg.addEdge(p1, p3, 0);
		pg.addEdge(p1, p4, 0);
		pg.addEdge(p2, p1, 0);
		pg.addEdge(p3, p5, 0);
		pg.addEdge(p4, p1, 0);
		pg.addEdge(p5, p4, 0);
		pg.addEdge(p5, p7, 0);
		pg.addEdge(p5, p8, 0);
		pg.addEdge(p6, p1, 0);
		pg.addEdge(p8, p1, 0);
		pg.addEdge(new Profile("start"), p1, 0);
		assertTrue(pg.connectedTo(new Profile("start"), p7));
		assertFalse(pg.connectedTo(p5, new Profile("start")));
	}
	
	//test5x: testing pathTo
	
	public void test50() {
		pg.addNode(p1);
		assertTrue(testPath(pg.pathTo(p1, p1), p1));
		pg.addEdge(p1, p2, 2);
		assertTrue(testPath(pg.pathTo(p1, p2), p1, p2));
		assertTrue(testPath(pg.pathTo(p2, p1)));
		pg.addEdge(p2, p1, 6);
		assertTrue(testPath(pg.pathTo(p2, p1), p2, p1));
	}
	
	public void test51() {
		pg.addEdge(p1, p2, 5);
		pg.addNode(p3);
		assertTrue(testPath(pg.pathTo(p1, p3)));
		assertTrue(testPath(pg.pathTo(p2, p1)));
		assertTrue(testPath(pg.pathTo(p2, p3)));
		assertTrue(testPath(pg.pathTo(p3, p1)));
		assertTrue(testPath(pg.pathTo(p3, p2)));
		assertTrue(testPath(pg.pathTo(p1, p2), p1, p2));
	}
	
	public void test52() {
		pg.addEdge(p1, p2, 2);
		pg.addEdge(p2, p3, 2);
		pg.addEdge(p3, p4, 2);
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
	
	public void test53() {
		pg.addNode(p1);
		pg.addEdge(p2, p3, 0);
		assertTrue(testPath(pg.pathTo(p1, p1), p1));
		assertTrue(testPath(pg.pathTo(p2, p2), p2));
		assertTrue(testPath(pg.pathTo(p3, p3), p3));
	}
	
	public void test54() {
		pg.addEdge(p1, p2, 0);
		assertTrue(testPath(pg.pathTo(null, null)));
		assertTrue(testPath(pg.pathTo(null, p2)));
		assertTrue(testPath(pg.pathTo(p1, null)));
		assertTrue(testPath(pg.pathTo(p1, p3)));
		assertTrue(testPath(pg.pathTo(p3, p2)));
		assertTrue(testPath(pg.pathTo(p3, p3)));
		assertTrue(testPath(pg.pathTo(null, p3)));
		assertTrue(testPath(pg.pathTo(p3, null)));
	}
	
	public void test55() {
		pg.addEdge(p1, p2, 1);
		pg.addEdge(p1, p3, 1);
		pg.addEdge(p1, p4, 10);
		pg.addEdge(p2, p1, 1);
		pg.addEdge(p3, p4, 8);
		pg.addEdge(p3, p5, 6);
		pg.addEdge(p4, p1, 6);
		pg.addEdge(p5, p4, 1);
		pg.addEdge(p5, p7, 3);
		pg.addEdge(p5, p8, 4);
		pg.addEdge(p6, p1, 2);
		pg.addEdge(p8, p1, 5);
		assertTrue(testPath(pg.pathTo(p6, p7), p6, p1, p3, p5, p7));
		assertTrue(testPath(pg.pathTo(p5, p6)));
		//While all three of the following paths are valid, UCS chooses the lowest cost
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p4, p1));  //cost 14
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p8, p1));  //cost 15
		assertTrue(testPath(pg.pathTo(p3, p1), p3, p5, p4, p1));  //cost 13
		
		assertTrue(testPath(pg.pathTo(p5, p1), p5, p4, p1));  //cost 7
		assertFalse(testPath(pg.pathTo(p5, p1), p5, p8, p1));  //cost 9
		
		assertFalse(testPath(pg.pathTo(p1, p4), p1, p3, p4));  //cost 9
		assertFalse(testPath(pg.pathTo(p1, p4), p1, p4));  //cost 10
		assertTrue(testPath(pg.pathTo(p1, p4), p1, p3, p5, p4));  //cost 8
	}
	
	//same as last test, but edges added in reverse order
	public void test56() {
		pg.addEdge(p8, p1, 5);
		pg.addEdge(p6, p1, 2);
		pg.addEdge(p5, p8, 4);
		pg.addEdge(p5, p7, 3);
		pg.addEdge(p5, p4, 1);
		pg.addEdge(p4, p1, 6);
		pg.addEdge(p3, p5, 6);
		pg.addEdge(p3, p4, 8);
		pg.addEdge(p2, p1, 1);
		pg.addEdge(p1, p4, 10);
		pg.addEdge(p1, p3, 1);
		pg.addEdge(p1, p2, 1);
		
		assertTrue(testPath(pg.pathTo(p6, p7), p6, p1, p3, p5, p7));
		assertTrue(testPath(pg.pathTo(p5, p6)));
		//While all three of the following paths are valid, UCS chooses the lowest cost
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p4, p1));  //cost 14
		assertFalse(testPath(pg.pathTo(p3, p1), p3, p5, p8, p1));  //cost 15
		assertTrue(testPath(pg.pathTo(p3, p1), p3, p5, p4, p1));  //cost 13
		
		assertTrue(testPath(pg.pathTo(p5, p1), p5, p4, p1));  //cost 7
		assertFalse(testPath(pg.pathTo(p5, p1), p5, p8, p1));  //cost 9

		assertFalse(testPath(pg.pathTo(p1, p4), p1, p3, p4));  //cost 9
		assertFalse(testPath(pg.pathTo(p1, p4), p1, p4));  //cost 10
		assertTrue(testPath(pg.pathTo(p1, p4), p1, p3, p5, p4));  //cost 8
	}
}
