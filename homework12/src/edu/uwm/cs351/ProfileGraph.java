package edu.uwm.cs351;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.util.ProfileMap;
import junit.framework.TestCase;
import snapshot.Snapshot;

/******************************************************************************
 * This class is a homework assignment.
 * A ProfileGraph is a collection of Profiles and edges between Profiles.
 * The Profiles are treated as nodes of a directed graph. We do not allow
 * any edges from a node to itself. We use the solution for the ProfileMap
 * from a previous assignment as an implementation of an edge list graph
 * representation. Each key in the map is a node in the graph, and its
 * value in the map gives the set of edges for that node. Because the
 * ProfileMap class uses a hash table, we know we have fast access to the
 * nodes of the graph. The ProfileMap stores Profiles related to each
 * Profile in a linked list. This means accessing an edge for a given node
 * takes time linear on the number of outgoing edges from that node.
 *
 ******************************************************************************/

public class ProfileGraph {
	
	private ProfileMap map;
	private int manyEdges;
	
	private static boolean doReport = true;
	private boolean report(String message) {
		if (doReport) System.err.println("Invariant error: " + message);
		return false;
	}
	
	protected ProfileGraph(boolean ignored) {} // don't change: used by invariant checker
	
	private boolean wellFormed() {
		// TODO: Check some additional requirements
		// Luckily, we already did wellFormed for the map,
		// so no need to redo it here. Many checks are
		// handled in the ProfileMap class.
		// 1. map must not be null
		// 2. manyEdges must be correct
		// 3. every edge must connect to a node in the graph
		//    (which means Profiles in the values are also keys in the map;
		//		this was not required by the ProfileMap, so we must check it here)
		// 4. no self-edges are allowed
		//    (this was also allowed in the ProfileMap)
		// #(
		if (map == null)
			return report("map is null");
		int count = 0;
		for (Profile p : map.getAll()) {
 			count += map.get(p).size();
 			for (Profile d : map.get(p)) {
 				if(d.equals(p))
 					return report("Found an edge from a node to itself");
 				if(map.find(d) == null)
 					return report("Profile found which is not in the graph");
 			}
		}
		if (count != manyEdges)
			return report("manyEdges wrong");
		return true;
		// #)
	}
	
	public ProfileGraph() {
		// TODO: initialize the private fields
		// #(
		map = new ProfileMap();
		manyEdges = 0;
		assert wellFormed() : "Invariant broken at end of constructor";
		// #)
	}
	
	/**
	 * Get the number of nodes in the graph
	 * @return number of nodes
	 */
	public int numNodes() {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of numNodes";
		return map.size();
		// #)
	}
	
	/**
	 * Get the number of edges in the graph
	 * @return number of edges
	 */
	public int numEdges() {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of numEdges";
		return manyEdges;
		// #)
	}
	
	/**
	 * Add a Profile to the graph.
	 * The Profile added will be a copy,
	 * so changing the Profile object will
	 * not affect the graph.
	 * Don't add duplicate or null nodes.
	 * @param p Profile to be added
	 * @return whether a Profile was added
	 */
	public boolean addNode(Profile p) {
		//TODO: implement this to avoid throwing an exception
		// #(
		assert wellFormed() : "Invariant broken at start of addNode";
		if (p == null)
			return false;
		boolean result = map.add(p);
		assert wellFormed() : "Invariant broken at end of addNode";
		return result;
		// #)
	}
	
	/**
	 * Add an edge from p1 to p2 to the graph.
	 * If either node is not in the graph,
	 * add it to the graph first.
	 * Don't add duplicate edges,
	 * self-referential edges,
	 * or edges to or from null.
	 * Don't add nodes if p1 and p2 are equal.
	 * @param p1 source node
	 * @param p2 destination node
	 * @return whether an edge was added to the graph
	 */
	public boolean addEdge(Profile p1, Profile p2) {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of addEdge";
		if(p1 == null || p2 == null || p1.equals(p2))
			return false;
		boolean result = map.add(p1, p2.clone());
		if(result)
			manyEdges++;
		assert wellFormed() : "Invariant broken at end of addEdge";
		return result;
		// #)
	}
	
	/**
	 * Check whether the given Profile
	 * is in the graph as a node
	 * @param p node to check
	 * @return whether p is in the graph
	 */
	public boolean containsNode(Profile p) {
		//TODO: implement this to avoid throwing an exception
		// #(
		assert wellFormed() : "Invariant broken at start of containsNode";
		if (p== null)
			return false;
		return map.find(p) != null;
		// #)
	}
	
	/**
	 * Check whether there is an edge from
	 * p1 to p2 in the graph
	 * @param p1 source node
	 * @param p2 dest node
	 * @return whether there is an edge from p1 to p2
	 */
	public boolean containsEdge(Profile p1, Profile p2) {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of containsEdge";
		if (p1 == null || p2 == null || map.find(p1) == null)
			return false;
		return map.find(p1).contains(p2);
		// #)
	}
	
	/**
	 * Remove p from the graph, if present
	 * Remove all edges to p or from p
	 * @param p node to be removed
	 * @return whether a node was removed
	 */
	public boolean removeNode(Profile p) {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of removeNode";
		if (p == null || map.find(p) == null)
			return false;
		for(Profile d : map.getAll())
			if(map.find(d).remove(p))
				manyEdges--;
		manyEdges -= map.find(p).size();
		map.remove(p);
		assert wellFormed() : "Invariant broken at end of removeNode";
		return true;
		// #)
	}
	
	/**
	 * Remove the edge from p1 to p2, if present
	 * @param p1 source node
	 * @param p2 dest node
	 * @return whether an edge was removed
	 */
	public boolean removeEdge(Profile p1, Profile p2) {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of removeEdge";
		if (p1 == null || p2 == null || map.find(p1) == null)
			return false;
		boolean result = map.find(p1).remove(p2);
		if (result)
			manyEdges--;
		assert wellFormed() : "Invariant broken at end of removeEdge";
		return result;
		// #)
	}
	
	/**
	 * Get a list of the nodes in the graph
	 * They should be copies so that changing
	 * the Profile objects in the list
	 * will not change the graph
	 * @return List<Profile> containing the nodes
	 */
	public List<Profile> nodeList() {
		//TODO: implement this
		// #(
		assert wellFormed() : "Invariant broken at start of nodeList";
		return map.getAll();
		// #)
	}
	
	/**
	 * Determine whether p2 can be reached from p1,
	 * if both are profiles in the graph
	 * @param p1 start node
	 * @param p2 target node
	 * @return whether there is a path
	 */
	public boolean connectedTo(Profile p1, Profile p2) {
		assert wellFormed() : "Invariant broken at start of connectedTo";
		return(search(p1, p2, false) != null);
	}
	
	/**
	 * Determine whether p2 can be reached from p1,
	 * if both are profiles in the graph,
	 * if we avoid certain profiles
	 * @param p1 start node
	 * @param p2 target node
	 * @param profiles nodes to avoid
	 * @return whether there is a path
	 */
	public boolean connectedToAvoiding(Profile p1, Profile p2, Profile... profiles) {
		assert wellFormed() : "Invariant broken at start of connectedToAvoiding";
		return(search(p1, p2, false, profiles) != null);
	}

	/**
	 * Determine how to reach p2 from p1,
	 * if both are profiles in the graph,
	 * @param p1 start node
	 * @param p2 target node
	 * @return the path
	 */
	public List<Profile> pathTo(Profile p1, Profile p2) {
		assert wellFormed() : "Invariant broken at start of pathTo";
		return(search(p1, p2, true));
	}
	
	/**
	 * Determine how to reach p2 from p1,
	 * if both are profiles in the graph,
	 * if we avoid certain profiles
	 * @param p1 start node
	 * @param p2 target node
	 * @param profiles nodes to avoid
	 * @return the path
	 */
	public List<Profile> pathToAvoiding(Profile p1, Profile p2, Profile... profiles) {
		assert wellFormed() : "Invariant broken at start of pathToAvoiding";
		return(search(p1, p2, true, profiles));
	}
	
	// Private class used for searching
	// and to keep track of the path.
	private static class SearchLink {
		Profile node;
		Profile from;
		public SearchLink(Profile n, Profile f) {
			node = n;
			from = f;
		}
		public String toString() {
			return node + "<-" + from;
		}
	}
	
	/**
	 * Private helper method to implement BFS and DFS
	 * with an optional list of nodes to avoid searching over
	 * Returns a path from source to dest, or null if
	 * there is no path
	 * @param source node to start search from
	 * @param dest node searched for
	 * @param breadth true for BFS, false for DFS
	 * @param avoiding nodes to avoid during search
	 * @return path from source to dest, null if none
	 */
	private List<Profile> search(Profile source, Profile dest, boolean breadth, Profile... toAvoid) {
		//TODO: Read the strategy in the handout
		// Use the list's ordering of edges from the Profile lists, or you will fail tests
		LinkedList<SearchLink> worklist = new LinkedList<SearchLink>();
		Map<Profile, Profile> visited = new HashMap<Profile, Profile>();
		Set<Profile> avoiding = new HashSet<Profile>();
		// #(
		if(source == null || dest == null || map.find(source) == null || map.find(dest)==null)
			return null;
		
		for(Profile p : toAvoid)
			avoiding.add(p);
		
		if(!avoiding.contains(source))
			worklist.add(new SearchLink(source, null));
		while(!worklist.isEmpty()) {
			SearchLink l = worklist.removeFirst();
			if(visited.containsKey(l.node))
				continue;
			visited.put(l.node, l.from);
			if(l.node.equals(dest))
				return constructPath(l.node, visited);
			for(Profile d : map.find(l.node))
				if((!avoiding.contains(d))) {
					SearchLink later = new SearchLink(d, l.node);
					if(breadth)
						worklist.addLast(later);
					else
						worklist.addFirst(later);
				}

		}
		return null;
		// #)
	}
	
	/**
	 * Private helper method to construct a path
	 * from a final node, and a map of visited
	 * nodes to visited-from nodes (what node
	 * this node was visited from)
	 * @param last destination of the path
	 * @param visited map showing visiting edges
	 * @return reconstructed path
	 */
	private List<Profile> constructPath(Profile last, Map<Profile, Profile> visited) {
		//TODO: all you need is the final node and the visited map to get the path
		// #(
		LinkedList<Profile> path = new LinkedList<Profile>();
		while(last != null) {
			path.addFirst(last.clone());
			last = visited.get(last);
		}
		return path;
		// #)
	}

	public static class TestInvariant extends TestCase {
		private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileGraph.java"};
		private static boolean firstRun = true;	
		
		public void log() {
			System.out.println("running");
			Snapshot.capture(TO_LOG);
		}
		
		ProfileGraph self;
		ProfileMap m;
		Profile p1, p2, p3;

		@Override
		public void setUp() {
			if(firstRun) {
				log();
				firstRun = false;
			}
			self = new ProfileGraph(false);
			m = new ProfileMap();
			p1 = new Profile("carol");
			p2 = new Profile("kellen");
			p3 = new Profile("james");
		}

		public void testEmpty() {
			self.map = m;
			self.manyEdges = 0;
			assertTrue(self.wellFormed());
			self.map = null;
			assertFalse(self.wellFormed());
			self.map = m;
			self.manyEdges = -1;
			assertFalse(self.wellFormed());
			self.manyEdges = 1;
			assertFalse(self.wellFormed());
		}
		
		public void testEdges() {
			m.add(p1);
			m.add(p2);
			m.add(p3);
			self.map = m;
			self.manyEdges = 3;
			assertFalse(self.wellFormed());
			self.manyEdges = 0;
			assertTrue(self.wellFormed());
			m.add(p1, p2);
			assertFalse(self.wellFormed());
			self.manyEdges = 1;
			assertTrue(self.wellFormed());
			m.add(p2, p1);
			assertFalse(self.wellFormed());
			self.manyEdges = 2;
			assertTrue(self.wellFormed());
			m.add(p1, p3);
			assertFalse(self.wellFormed());
			self.manyEdges = 3;
			assertTrue(self.wellFormed());
		}
		
		//remember how the ProfileMap works
		public void testOutsideGraph() {
			m.add(p1);
			m.add(p1,p2);
			self.map = m;
			self.manyEdges = 1;
			assertTrue(self.wellFormed());
			m.find(p2).add(p3);
			assertFalse(self.wellFormed());
			self.manyEdges = 2;
			assertFalse(self.wellFormed());
			m.get(p3);
			assertTrue(self.wellFormed());
		}
		
		//we allow different but equal Profile objects to represent the same node
		public void testNodeIdentity() {
			m.add(p1);
			m.add(p2);
			self.map = m;
			self.manyEdges = 1;
			m.find(p1).add(new Profile(p2.getNickname()));
			assertTrue(self.wellFormed());
		}
		
		public void testSelfEdge() {
			m.add(p1);
			self.map = m;
			self.manyEdges = 1;
			m.find(p1).add(p1);
			assertFalse(self.wellFormed());
			self.manyEdges = 0;
			assertFalse(self.wellFormed());
		}
	}
}
