package edu.uwm.cs351;

import java.util.ArrayList;

import junit.framework.TestCase;
import edu.uwm.cs351.util.Primes;
import snapshot.Snapshot;

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
		// TODO: Check
		// All the issues in the DESIGN
		// 1. Check that table size is the larger of two twin primes
		// 2. That every entry with a non-null key can be found.
		// (To test this, you must NOT call a public member function.
		//   but you may rely on private helper methods and also
		//   public methods of Primes)
		// 3. the only entry with a null key is PLACE_HOLDER.
		// 4. used is correct
		// 5. numEntries is correct
		// 6. the table is not too crowded
		// #(
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

		// #)
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
		//use Profile's hashCode function
		// #(
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
		/* #)
		return 0;
		## */
		// TODO: write this helper method.
		// This will be useful for any other method that needs to find something
		// in the table, and also for the invariant and the rehash method.
	}

	/**
	 * Create a new array without any place holders that holds all the existing
	 * true entries (added in the order they appear in the current table).
	 * The size of the new array is the larger of smallest twin primes that is
	 * larger than four times the number of true entries, except that the
	 * size is never less than the {@link #INITIAL_CAPACITY}.
	 */
	private void rehash() {
		// Don't assert the invariant before the rehash.
		// Often the array is crowded/full.
		// #(
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
		// #)
		// TODO rehash the table (always)
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
		// #(
		return numEntries;
		/* #)
		return 0; // TODO: very little work
		## */
	}
	
	/**
	 * Add a Profile to the map.
	 * If it is not in the map, create an association
	 * with an empty list.
	 * The Profile added should be a clone
	 * so that the key can't be modified by the client.
	 * @param p the Profile to be added
	 * @return whether a Profile was added
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
		// #(
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
		
		// #)
		//TODO: implement this
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
		// #(
		int h = getEntryIndex(key,false);
		return table[h];
		/* #)
		return null; // TODO
		## */
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
		// #(
		if (result == null) {
			add(key, null);
			result = find(key);
		}
		// #)
		// TODO: add if needed
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
		// #(
		for(int i=0; i<table.length; ++i) {
			if(table[i] != null && table[i].key != null) {
				allProfiles.add(table[i].key.clone());
			}
		}
		// #)
		// TODO: implement this method
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
		// #(
		int h = getEntryIndex(key,false);
		result = table[h];
		if (result == null) return null;
		table[h] = PLACE_HOLDER;
		--numEntries;
		// #)
		// TODO: remove if there
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

	public static class TestInvariant extends TestCase {
		private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileMap.java"};
		private static boolean firstRun = true;	
		
		public void log() {
			System.out.println("running");
			Snapshot.capture(TO_LOG);
		}
		
		ProfileMap self;

		private Entry e0,e1,e2,e3,e4,e5,e6,e7,e8;

		@Override
		public void setUp() {
			if(firstRun) {
				log();
				firstRun = false;
			}
			self = new ProfileMap();
			e0 = new Entry(null);
			e1 = new Entry(new Profile("carol"));
			e2 = new Entry(new Profile("kellen"));
			e3 = new Entry(new Profile("james"));
			e4 = new Entry(new Profile("ariana"));
			e5 = new Entry(new Profile("joel"));
			e6 = new Entry(new Profile("noel"));
			e7 = new Entry(new Profile("kate"));
			e8 = new Entry(new Profile("penny"));
		}
		// #(
		// Some code to help us figure out where these keys hash to
		private void printPlaces(int n) {
			for (Entry e : new Entry[]{e1,e2,e3,e4,e5,e6,e7,e8}) {
				self.table = new Entry[n];
				System.out.print(e.key.getNickname() + ":");
				for (int i=0; i < n; ++i) {
					int h = self.getEntryIndex(e.key, false);
					System.out.print(" " + h);
					self.table[h] = PLACE_HOLDER;
				}
				System.out.println();
				for (int i=0; i < self.table.length; ++i) {
					self.table[i] = null;
				}
			}
		} 

		public void testPrintPlaces() {
			printPlaces(7);
			printPlaces(13);
			printPlaces(19);
		}
		// #)

		private Entry[] makeArray(int n) {
			return new Entry[n];
		}

		private void checkInvariantFails(String s) {
			// we don't want to print messages that are for intentional errors
			doReport = false;
			assertFalse(s,self.wellFormed());
			doReport = true;
		}

		public void testSize() {
			// testing that the size is the larger of twin primes
			self.table = makeArray(9);
			checkInvariantFails("not prime");
			self.table = makeArray(37);
			checkInvariantFails("not twin prime");
			self.table = makeArray(17);
			checkInvariantFails("not larger twin prime");
			self.table = makeArray(13);
			assertTrue(self.wellFormed());
		}

		public void testCount() {
			// testing that entries and place-holders are correctly counted.
			self.table[0] = PLACE_HOLDER;
			checkInvariantFails("used wrong");
			self.used = 1;
			assertTrue(self.wellFormed());
			self.table[0] = e2;
			checkInvariantFails("num entries wrong");
			self.numEntries = 1;
			assertTrue(self.wellFormed());
			self.table[5] = PLACE_HOLDER;
			checkInvariantFails("used wrong");
			self.used = 2;
			assertTrue(self.wellFormed());
			self.table[4] = e3;
			checkInvariantFails("used & num entries wrong");
			self.numEntries = 2;
			checkInvariantFails("used wrong");
			self.used = 3;
			assertTrue(self.wellFormed());
			self.table[0] = PLACE_HOLDER;
			checkInvariantFails("num entries wrong");
			self.numEntries = 1;
			assertTrue(self.wellFormed());
			self.table[4] = PLACE_HOLDER;
			checkInvariantFails("num entries wrong");
			self.numEntries = 0;
			assertTrue(self.wellFormed());
		}

		public void testBad() {
			self.table[0] = e0;
			checkInvariantFails("used wrong");
			self.used = 1;
			checkInvariantFails("bad entry");
			self.numEntries = 1;
			checkInvariantFails("bad entry");
			self.table[0] = e2;
			assertTrue(self.wellFormed());
			self.table[6] = e0;
			checkInvariantFails("used wrong");
			self.used = 2;
			checkInvariantFails("bad entry");
			self.numEntries = 2;
			checkInvariantFails("bad entry");
			self.table[6] = e5;
			assertTrue(self.wellFormed());
			self.table[6] = null;
			self.table[3] = new Entry(new Profile("kellen"));
			checkInvariantFails("duplicate key");
		}

		public void testCrowded07() {
			self.table[0] = PLACE_HOLDER;
			self.table[1] = PLACE_HOLDER;
			self.table[4] = PLACE_HOLDER;
			self.table[5] = PLACE_HOLDER;
			self.used = 4;
			checkInvariantFails("too many 4 PH");
			self.table[0] = e2;
			self.numEntries = 1;
			checkInvariantFails("too many 3 PH, 1 E");
			self.table[1] = e4;
			self.numEntries = 2;
			checkInvariantFails("too many 2 PH, 2 E");
			self.table[4] = e3;
			self.numEntries = 3;
			checkInvariantFails("too many 1 PH, 3 E");
			self.table[5] = e1;
			self.numEntries = 4;
			checkInvariantFails("too many 0 PH, 4 E");
			self.table[4] = null;
			self.used = 3;
			self.numEntries = 3;
			assertTrue(self.wellFormed());
		}

		public void testCrowded13() {
			self.table = new Entry[13];
			self.table[1] = PLACE_HOLDER;
			self.table[7] = PLACE_HOLDER;
			self.table[4] = PLACE_HOLDER;
			self.table[5] = PLACE_HOLDER;
			self.table[0] = PLACE_HOLDER;
			self.table[2] = PLACE_HOLDER;
			self.table[6] = PLACE_HOLDER;
			self.used = 7;
			checkInvariantFails("too many 7 PH");
			self.table[1] = e1;
			self.numEntries = 1;
			checkInvariantFails("too many 6 PH + 1 E");
			self.table[7] = e2;
			self.numEntries = 2;
			checkInvariantFails("too many 5 PH + 2 E");
			self.table[4] = e3;
			self.numEntries = 3;
			checkInvariantFails("too many 4 PH + 3 E");
			self.table[5] = e4;
			self.numEntries = 4;
			checkInvariantFails("too many 3 PH + 4 E");
			self.table[0] = e8;
			self.numEntries = 5;
			checkInvariantFails("too many 2 PH + 5 E");
			self.table[2] = null;
			self.used = 6;
			assertTrue(self.wellFormed());
		}

		public void testCollision07() {
			self.table[6] = e5;
			self.used = 1;
			self.numEntries = 1;
			assertTrue("no collision",self.wellFormed());
			self.table[6] = PLACE_HOLDER;
			self.used = 2;
			for (int i=0; i < 7; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e5;
				if (i == 3) {
					assertTrue("e5 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e5 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}
			self.table[6] = null;
			self.table[4] = e3;
			self.numEntries = 2;
			for (int i=0; i < 7; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e6;
				if (i == 5) {
					assertTrue("e6 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e6 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}
			self.table[5] = PLACE_HOLDER;
			self.used = 3;
			for (int i=0; i < 7; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e8;
				if (i == 6) {
					assertTrue("e8 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e8 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}
		}

		public void testCollisions13() {
			self.table = new Entry[13];
			self.table[1] = e1;
			self.numEntries = 1;
			self.used = 1;
			assertTrue(self.wellFormed());

			self.table[1] = PLACE_HOLDER;
			self.used = 2;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e1;
				if (i == 6) {
					assertTrue("e1 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e1 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}

			self.table[1] = e6;
			self.numEntries = 2;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e5;
				if (i == 7) {
					assertTrue("e5 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e5 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}

			self.table[6] = PLACE_HOLDER;
			self.used = 3;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e7;
				if (i == 11) {
					assertTrue("e7 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e7 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}

			self.table[11] = e7;
			self.numEntries = 3;
			self.used = 4;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e1;
				if (i == 3) {
					assertTrue("e1 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e1 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}

			self.table[2] = PLACE_HOLDER;
			self.table[3] = PLACE_HOLDER;
			self.used = 6;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e6;
				if (i == 3) {
					assertTrue("e6 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e6 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}

			self.table[4] = e3;
			self.table[1] = PLACE_HOLDER;
			self.table[11] = null;
			self.numEntries = 2;
			for (int i=0; i < 13; ++i) {
				if (self.table[i] != null) continue; // occupied
				self.table[i] = e6;
				if (i == 5) {
					assertTrue("e6 is correctly in slot " + i, self.wellFormed());
				} else {
					checkInvariantFails("e6 is incorrectly in slot " + i);
				}
				self.table[i] = null;
			}
		}

		public void testCollisions19() {
			self.table = new Entry[19];
			self.table[11] = e4;
			self.table[8] = PLACE_HOLDER;
			self.table[1] = e2;
			self.table[10] = PLACE_HOLDER;
			self.table[0] = e6;
			self.table[9] = e5;
			self.table[18] = e3;
			self.used = 7;
			self.numEntries = 5;
			assertTrue(self.wellFormed());
		}
		
		//the following tests require implementing some public methods
		
		public void testRehash() {
			self.table = new Entry[7];
			self.table[5] = e1;
			self.table[0] = e2;
			self.table[4] = e3;
			self.used = 3;
			self.numEntries = 3;
			assertTrue(self.wellFormed());
			self.get(e5.key);
			assertEquals(19, self.table.length);
			assertEquals(self.table[8], e2);
			assertEquals(self.table[11], e3);
			assertEquals(self.table[4], e1);
			assertEquals(self.table[9], e5);
			assertTrue(self.wellFormed());
		}
		
		public void testOverwritePlaceholder() {
			self.table = new Entry[7];
			self.table[4] = PLACE_HOLDER;
			self.table[5] = PLACE_HOLDER;
			self.used = 2;
			self.get(e3.key);
			assertEquals(self.table[4], e3);
			self.get(e6.key);
			assertEquals(self.table[5], e6);
			self.get(e8.key);
			assertEquals(self.table[6], e8);
			assertEquals(3, self.used);
			assertEquals(7, self.table.length);
		}
	}

}
