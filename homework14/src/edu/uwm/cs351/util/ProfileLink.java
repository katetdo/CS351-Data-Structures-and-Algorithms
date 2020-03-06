package edu.uwm.cs351.util;

public class ProfileLink {
	/**
	 * Represents a link between two profiles and an associated cost
	 * For uniform-cost search, we use 
	 */

	public final Profile source;
	public final Profile dest;
	public final int cost;
	
	public ProfileLink(Profile s, Profile d) {
		this(s, d, 0);
	}
	
	public ProfileLink(Profile s, Profile d, int c) {
		this.source = s;
		this.dest = d;
		this.cost = c;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProfileLink) {
			ProfileLink o = (ProfileLink)obj;
			return (o.source == source && o.dest == dest && o.cost == cost);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "" + cost;
	}
	
	

}
