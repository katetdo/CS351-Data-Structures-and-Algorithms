package edu.uwm.cs351.util;

import java.util.Comparator;

import edu.uwm.cs351.Painting;

/**
 * The class Alphabetical_artist.
 * 
 * Return p1 <= p2 if p1's artist comes before p2's artist, alphabetically (or, more accurately, lexicographically).
 */
public class Alphabetical_artist implements Comparator<Painting> {

	/*
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Painting p1, Painting p2) {
		// #(
		return p1.getArtist().compareTo(p2.getArtist());
		/* #)
		// TODO: Implement compare for two artists. compareTo() method helps.
		return 0;
		## */
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "Alphabetical_artist";}
	
	private static Comparator<Painting> instance = new Alphabetical_artist();
	
	/**
	 * Gets a single instance of Alphabetical comparator.
	 * @return a single instance of Alphabetical comparator
	 */
	public static Comparator<Painting> getInstance() { return instance; }
}
