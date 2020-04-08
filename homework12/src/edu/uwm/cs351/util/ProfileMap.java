package edu.uwm.cs351.util;

import java.util.ArrayList;

//DO NOT CHANGE THIS CLASS

import edu.uwm.cs351.util.Primes;

public class ProfileMap {

	private static class Entry extends ArrayList<Profile> {
		/**
		 * Keep Eclipse Happy
		 */
		private static final long serialVersionUID = 1L;
		Profile key;
		Entry(Profile key) {
			super();
			if (key != null) {
				this.key = key.clone();
			}
		}

		@Override
		public String toString() {
			return "Entry(" + key + ")";
		}
	}

	public static final int INITIAL_CAPACITY = 7; // must be the larger of twin primes
	private static final double CROWDED = 0.50;
	private static final Entry PLACE_HOLDER = new Entry((Profile)null);

	private Entry[] table;
	private int numEntries; // number of true entries
	private int used; // number of slots in contents with non-null values

	/// DESIGN
	// The table's capacity and (capacity - 2) must be prime.
	// If the table has null at some position, this means there was never 
	// an entry here since the last rehashing. [Cannot be checked.]
	// "_used" is the number of non-null entries.
	// If an entry is removed, we leave a PLACE_HOLDER
	// (so that successive finds know that the entry had been occupied).
	// "_numEntries" is the number of non-null, non PLACE_HODLER entries.
	// We rehash the table if the number of used / capacity > CROWDED,
	// so the table should never have used/capacity >= CROWDED.

	private static boolean doReport = true;
	private boolean report(String message) {
		if (doReport) System.err.println("Invariant error: " + message);
		return false;
	}

	private boolean wellFormed() {
		if (!Primes.isPrime(table.length)) return report("table size is not prime");
		if (!Primes.isPrime(table.length-2)) return report("table size - 2 is not primes");
		int nonNull = 0;
		int nonNullKey = 0;
		for (int i=0; i < table.length; ++i) {
			if (table[i] != null) {
				++nonNull;
				if (table[i].key == null) {
					if (table[i] != PLACE_HOLDER) return report("Entry with null key found, not a place holder");
				} else {
					++nonNullKey;
					int j = getEntryIndex(table[i].key,false);
					if (j != i)
						return report("entry " + table[i] + " lost! (expected at " + j + ")");
				}
			}
		}
		if (used != nonNull) return report("used is  " + used + ", expected " + nonNull);
		if (numEntries != nonNullKey) return report("numEntries is  " 
				+ numEntries + ", expected " + nonNullKey);
		if ((double)nonNull/table.length > CROWDED ) return report("table is too crowded");
		return true;

	}

	/**
	 * Find the index to use for an entry in the hash table.
	 * (Compare with findIndex in textbook, page 581.)
	 * @param x Profile, to be hashed by {@link Profile#hashCode()}. 
	 * @param nullKeyOK stop if an entry with null key is found 
	 *   (useful for {@link #get} after determining the entry is not present).
	 * @return index to look for entry (if nullKeyOK is false)
	 * or to put entry (if nullKeyOK is true).
	 */
	private int getEntryIndex(Profile x, boolean nullKeyOK) {
		int h = x.hashCode();
		if (h < 0) h = -h;
		int h1 = h % table.length;
		int h2 = h % (table.length - 2) + 1;
		while (table[h1] != null) {
			if (table[h1].key == null) {
				if (nullKeyOK) break;
			}
			else if (table[h1].key.equals(x)) break;
			h1 += h2;
			h1 %= table.length;
		}
		return h1;
	}

	/**
	 * Create a new array without any place holders that holds all the existing
	 * true entries (added in the order they appear in the current table).
	 * The size of the new array is the larger of smallest twin primes that is
	 * larger than four times the number of true entries, except that the
	 * size is never less than the {@link #INITIAL_CAPACITY}.
	 */
	private void rehash() {
		Entry[] oldTable = table;
		int newCapacity = Primes.nextTwinPrime(numEntries*4);
		if (newCapacity < INITIAL_CAPACITY) {
			newCapacity = INITIAL_CAPACITY;
		}
		table = new Entry[newCapacity];
		numEntries = 0;
		used = 0;
		for (Entry e : oldTable) {
			if (e != null && e.key != null) {
				int i = getEntryIndex(e.key,false); // actually there are no empty entries!
				table[i] = e;
				++numEntries;
				++used;
			}
		}
	}

	/// Public methods

	public ProfileMap() {
		table = new Entry[INITIAL_CAPACITY];
		numEntries = 0;
		used = 0;
		assert wellFormed() : "invariant flse at end of constructor";
	}

	/**
	 * Return the number of entries in this table,
	 * mappings from Profiles to lists of Profiles.
	 * @return
	 */
	public int size() {
		assert wellFormed() : "invariant broken at start of size()";
		return numEntries;
	}
	
	/**
	 * Add a Profile to the map.
	 * If it is not in the map, create an association
	 * with an empty list.
	 * The Profile added should be a clone
	 * so that the key can't be modified by the client.
	 * @param p the Profile to be added
	 * @return whether a Profile was added
	 * @throws IllegalArgumentException if p is null
	 */
	public boolean add(Profile p) {
		return add(p, null);
	}

	/**
	 * Add p2 to p1's list of Profiles, adding p1 and/or p2 to the map if necessary
	 * If p1's list already contains p2, don't add anything
	 * If p2 is null, don't add anything to p1's list, and just add p1 to the map
	 * Newly added Profiles should be clones, and should have associations
	 * with an empty list.
	 * @param p1, the Profile being added to
	 * @param p2, the Profile being added, null if merely adding p1 to the map
	 * @return true if something changed in the map
	 * @throws IllegalArgumentException if p1 is null
	 */
	public boolean add(Profile p1, Profile p2) {
		assert wellFormed() : "invariant broken at start of add()";
		if(p1 == null) throw new IllegalArgumentException();
		boolean result = false;
		int h1 = getEntryIndex(p1, false);
		Entry e = table[h1];
		if (e == null) {
			e = new Entry(p1);
			int h = getEntryIndex(p1,true);
			if (table[h] == null) ++used;
			table[h] = e;
			result = true;
			++numEntries;
			if (used >= CROWDED*table.length) rehash();
		}
		if(p2 != null) {
			if(table[getEntryIndex(p2, false)] == null) {
				add(p2, null);
				result = true;
			}
			if (!e.contains(p2)) {
				e.add(p2);
				result = true;
			}
		}
		assert wellFormed() : "invariant broken at end of add()";
		return result;
	}

	/**
	 * Return the array list for the associated key.  If there
	 * is no entry for the key, return null.
	 * @param key Profile used as a key, must not be null
	 * @return array list of Profiles associated with the key, may be null
	 */
	public ArrayList<Profile> find(Profile key) {
		assert wellFormed(): "invariant broken at start of find(" + key + ")";
		int h = getEntryIndex(key,false);
		return table[h];
	}

	/** 
	 * Return the array list for the associated key.  If there is none,
	 * create a new entry in the table and return it.
	 * @param key Profile used as a key, must not be null
	 * @return array list of Profiles associated with the key, never null.
	 */
	public ArrayList<Profile> get(Profile key) {
		assert wellFormed(): "invariant broken at start of get(" + key + ")";
		ArrayList<Profile> result = find(key);
		if (result == null) {
			add(key, null);
			result = find(key);
		}
		assert wellFormed() : "invariant broken after get(" + key + ")";
		return result;
	}
	
	/** 
	 * Return all keys 
	 * Should return clones so that the client can't
	 * modify keys that are in the table.
	 * @return array list of all usernames in the profileMap
	 */
	public ArrayList<Profile> getAll() {
		assert wellFormed(): "invariant broken at start of getAll()";
		ArrayList<Profile> allProfiles = new ArrayList<Profile>();
		for(int i=0; i<table.length; ++i) {
			if(table[i] != null && table[i].key != null) {
				allProfiles.add(table[i].key.clone());
			}
		}
		return allProfiles;
	}


	/**
	 * Remove the entry for the given key, and return the old list, if any,
	 * associated with it.
	 * @param key Profile used as a key, must not be null
	 * @return array list of Profiles formerly associated with the key, may be null.
	 */
	public ArrayList<Profile> remove(Profile key) {
		assert wellFormed(): "invariant broken at start of remove(" + key + ")";
		ArrayList<Profile> result = null;
		int h = getEntryIndex(key,false);
		result = table[h];
		if (result == null) return null;
		table[h] = PLACE_HOLDER;
		--numEntries;
		assert wellFormed() : "invariant broken after remove(" + key + ")";
		return result;		
	}


	/**
	 * Return a string that summarizes the state of the internal hashtable.
	 * The array is show in the form [e0,e2,...,en]
	 * where e<i>i</i> is determined by the entry there:
	 * <ul>
	 * <li> nothing if the entry is null
	 * <li> An X if the entry is a placeholder
	 * <li> Otherwise a string representation of the key.
	 * </ul>
	 * Don't change this code.
	 * @return string summary of the internal array.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (Entry e : table) {
			if (first) first = false;
			else sb.append(",");
			if (e == null) continue;
			if (e == PLACE_HOLDER) sb.append('X');
			else if (e.key == null) sb.append('?'); // don't crash, show problem
			else sb.append(e.key);
		}
		sb.append("]");
		return sb.toString();
	}


}
