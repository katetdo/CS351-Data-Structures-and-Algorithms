package edu.uwm.cs351;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.util.ProfileLink;

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

public class WeightedProfileGraph {
	
	private Profile[] profiles;
	private int[][] edges;
	private int manyNodes;
	private int manyEdges;
	
	private static final int INITIAL_CAPACITY = 7;
	
	private static boolean doReport = true;
	private boolean report(String message) {
		if (doReport) System.err.println("Invariant error: " + message);
		return false;
	}
	
	protected WeightedProfileGraph(boolean ignored) {} // don't change: used by invariant checker
	

	
	public WeightedProfileGraph() {
		profiles = new Profile[INITIAL_CAPACITY];
		edges = new int[INITIAL_CAPACITY][INITIAL_CAPACITY];
		manyNodes = 0;
		manyEdges = 0;
	}
	
	/**
	 * Get the number of nodes in the graph
	 * @return number of nodes
	 */
	public int numNodes() {
		return manyNodes;
	}
	
	/**
	 * Get the number of edges in the graph
	 * @return number of edges
	 */
	public int numEdges() {
		return manyEdges;
	}
	
	/**
	 * Add a Profile to the graph.
	 * The Profile added will be a copy,
	 * so changing the Profile object will
	 * not affect the graph.
	 * Don't add duplicate or null nodes.
	 * @param p Profile to be added
	 * @return whether a Profile was added
	 * @throws IllegalArgumentException for null profile
	 */
	public boolean addNode(Profile p) {
		if (p == null)
			throw new IllegalArgumentException();
		for(int i=0; i<manyNodes; i++)
			if(profiles[i].equals(p))
				return false;
		ensureCapacity(manyNodes+1);
		profiles[manyNodes] = p;
		for(int i=0; i<manyNodes+1; i++) {
			edges[manyNodes][i] = -1;
			edges[i][manyNodes] = -1;
		}
		manyNodes++;
		return true;
	}
	
	/**
	 * Add an edge from p1 to p2 to the graph with given weight.
	 * If an edge exists, replace the weight
	 * If either node is not in the graph,
	 * add it to the graph first.
	 * Don't add duplicate edges,
	 * self-referential edges,
	 * or edges to or from null.
	 * Don't add nodes if p1 and p2 are equal.
	 * @param p1 source node
	 * @param p2 destination node
	 * @param weight for the edge
	 * @return the previous weight, or -1 if none
	 * @throws IllegalArgumentException for null profile,
	 *     self edge, or negative weight
	 */
	public int addEdge(Profile p1, Profile p2, int weight) {
		if(p1 == null || p2 == null || p1.equals(p2) || weight < 0)
			throw new IllegalArgumentException();
		addNode(p1);
		addNode(p2);
		int result = edges[indexOf(p1)][indexOf(p2)];
		edges[indexOf(p1)][indexOf(p2)] = weight;
		if(result == -1)
			manyEdges++;
		return result;
	}
	
	/**
	 * Check whether the given Profile
	 * is in the graph as a node
	 * @param p node to check
	 * @return whether p is in the graph
	 */
	public boolean containsNode(Profile p) {
		if (p == null)
			return false;
		return indexOf(p) != -1;
	}
	
	/**
	 * Check the weight of the edge from
	 * p1 to p2 in the graph
	 * @param p1 source node
	 * @param p2 dest node
	 * @return the weight of the edge from p1 to p2
	 *     or -1 if none
	 */
	public int getEdge(Profile p1, Profile p2) {
		if (p1 == null || p2 == null)
			return -1;
		if (indexOf(p1) == -1 || indexOf(p2) == -1)
			return -1;
		return edges[indexOf(p1)][indexOf(p2)];
	}
	
	/**
	 * Get a list of the nodes in the graph
	 * They should be copies so that changing
	 * the Profile objects in the list
	 * will not change the graph
	 * @return List<Profile> containing the nodes
	 */
	public List<Profile> nodeList() {
		List<Profile> result = new LinkedList<Profile>();
		for (int i=0; i<manyNodes; i++)
			result.add(profiles[i]);
		return result;
	}
	
	private int indexOf(Profile p) {
		for(int i=0; i<manyNodes; i++)
			if(profiles[i].equals(p))
				return i;
		return -1;
	}
	
	private void ensureCapacity(int minimumCapacity) {
		Profile [] newProfiles;
		int[][] newEdges;
		if(profiles.length < minimumCapacity)
		{
			int newCapacity = profiles.length * profiles.length;
			if(newCapacity < minimumCapacity)
				newCapacity = minimumCapacity;
			newProfiles = new Profile[newCapacity];
			newEdges = new int[newCapacity][newCapacity];
			for(int i=0; i<manyNodes; i++)
			{
				newProfiles[i] = profiles[i];
				for(int j=0; j<manyNodes; j++)
					newEdges[i][j] = edges[i][j];
			}
			profiles = newProfiles;
			edges = newEdges;
		}
	}
	
	/**
	 * Determine whether p2 can be reached from p1,
	 * if both are profiles in the graph
	 * @param p1 start node
	 * @param p2 target node
	 * @return whether there is a path
	 */
	public boolean connectedTo(Profile p1, Profile p2) {
		return(search(p1, p2) != null);
	}

	/**
	 * Determine how to reach p2 from p1,
	 * if both are profiles in the graph,
	 * @param p1 start node
	 * @param p2 target node
	 * @return the path
	 */
	public List<Profile> pathTo(Profile p1, Profile p2) {
		return(search(p1, p2));
	}
	
	/**
	 * Private helper method to implement BFS and DFS
	 * with an optional list of nodes to avoid searching over
	 * Returns a path from source to dest, or null if
	 * there is no path
	 * @param source node to start search from
	 * @param dest node searched for
	 * @return path from source to dest, null if none
	 */
	private List<Profile> search(Profile source, Profile dest) {
		PriorityQueue worklist = new PriorityQueue();
		Map<Profile, Profile> visited = new HashMap<Profile, Profile>();
		if(source == null || dest == null || indexOf(source) == -1 || indexOf(dest) == -1)
			return null;
		worklist.add(new ProfileLink(null, source, 0));
		while(!worklist.isEmpty()) {
			ProfileLink l = worklist.remove();
			if(visited.containsKey(l.dest))
				continue;
			visited.put(l.dest, l.source);
			if(l.dest.equals(dest))
				return constructPath(l.dest, visited);
			int p = indexOf(l.dest);
			for(int i=0; i<manyNodes; i++) {
				if(edges[p][i] == -1)
					continue;
				worklist.add(new ProfileLink(l.dest, profiles[i], l.cost + edges[p][i]));
			}

		}
		return null;
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
		LinkedList<Profile> path = new LinkedList<Profile>();
		while(last != null) {
			path.addFirst(last.clone());
			last = visited.get(last);
		}
		return path;
	}

}
