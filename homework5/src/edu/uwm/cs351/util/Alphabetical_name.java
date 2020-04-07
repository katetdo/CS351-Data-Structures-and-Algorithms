package edu.uwm.cs351.util;

import java.util.Comparator;

import edu.uwm.cs351.Painting;

/**
 * The class Alphabetical_name.
 * 
 * Return p1 <= p2 if p1's name comes before p2's name, alphabetically (or, more accurately, lexicographically).
 */
public class Alphabetical_name implements Comparator<Painting> {

	/*
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Painting p1, Painting p2) {
		// #(
		return p1.getName().compareTo(p2.getName());
		/* #)
		// TODO: Implement compare for two names. compareTo() method helps.
		return 0;
		## */
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "Alphabetical_name";}
	
	private static Comparator<Painting> instance = new Alphabetical_name();
	
	/**
	 * Gets a single instance of Alphabetical comparator.
	 * @return a single instance of Alphabetical comparator
	 */
	public static Comparator<Painting> getInstance() { return instance; }
}
