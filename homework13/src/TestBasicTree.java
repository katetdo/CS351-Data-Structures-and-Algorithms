import edu.uwm.cs351.BasicTree;
import junit.framework.TestCase;
import snapshot.Snapshot;
import java.util.Comparator;

public class TestBasicTree extends TestCase{
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/BasicTree.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}



	protected BasicTree<Integer> c;
	protected BasicTree<String> cs;
	Comparator<Integer> ascending = new Comparator<Integer>() {
		public int compare(Integer a, Integer b) {return a-b;}
	};
	Comparator<Integer> descending = new Comparator<Integer>() {
		public int compare(Integer a, Integer b) {return b-a;}
	};
	Comparator<String> lengthAscending = new Comparator<String>() {
		public int compare(String a, String b) {return a.length()-b.length();}
	};
	Comparator<String> firstChar = new Comparator<String>() {
		public int compare(String a, String b) {
			Character aChar = a.length() == 0 ? null : new Character(a.charAt(0));
			Character bChar = b.length() == 0 ? null : new Character(b.charAt(0));
			if (aChar==null && bChar == null) return 0;
			else if (aChar==null && bChar != null) return -1;
			else if (bChar==null && aChar != null) return 1;
			else return aChar.compareTo(bChar);
		}
	};

	@Override
	protected void setUp() {
		try {
			assert c.size() == cs.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration", false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		initCollections();
	}
	/**
	 * Initialize c and cs.
	 */
	protected void initCollections() {
		c = new BasicTree<Integer>(new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		});
		cs = new BasicTree<String>(new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
	}
	
	private boolean arrayTest(Object[] a1, Object[] a2) {
		if(a1.length != a2.length)
			return false;
		for(int i=0; i<a1.length; i++)
			if(!(a1[i] == a2[i]))
				return false;
		return true;
	}
	
	//test0x: insertionSort
	
	public void test00() {
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[0]));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[0]));
	}
	
	public void test01() {
		c.add(3);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {3}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {3}));
	}
	
	public void test02() {
		c.add(3);
		c.add(4);
		c.add(5);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {3, 4, 5}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {5, 4, 3}));
	}
	
	public void test03() {
		c.add(3);
		c.add(5);
		c.add(4);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {3, 4, 5}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {5, 4, 3}));
	}
	
	public void test04() {
		c.add(2);
		c.add(5);
		c.add(4);
		c.add(8);
		c.add(4);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {2, 4, 4, 5, 8}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {8, 5, 4, 4, 2}));
	}
	
	public void test05() {
		c.add(9);
		c.add(5);
		c.add(2);
		c.add(8);
		c.add(0);
		c.add(0);
		c.add(4);
		c.add(4);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {0, 0, 2, 4, 4, 5, 8, 9}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {9, 8, 5, 4, 4, 2, 0, 0}));
	}
	
	public void test06() {
		c.add(4);
		c.add(5);
		c.add(8);
		c.add(2);
		c.add(0);
		c.add(0);
		c.add(4);
		c.add(9);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {0, 0, 2, 4, 4, 5, 8, 9}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {9, 8, 5, 4, 4, 2, 0, 0}));
	}
	
	public void test07() {
		c.add(7);
		c.add(5);
		c.add(8);
		c.add(1);
		c.add(2);
		c.add(6);
		c.add(0);
		c.add(4);
		c.add(9);
		c.add(3);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}));
	}
	public void test08() {
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(2);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		assertTrue(arrayTest(c.insertionSort(ascending), new Integer[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 2}));
		assertTrue(arrayTest(c.insertionSort(descending), new Integer[] {2, 1, 1, 1, 1, 1, 1, 1, 1, 1}));
	}
	
	public void test09() {
		cs.add("braeburn");
		cs.add("evercrisp");
		cs.add("cox's orange pippin");
		assertTrue(arrayTest(cs.insertionSort(lengthAscending), new String[] {"braeburn", "evercrisp", "cox's orange pippin"}));
		cs.add("ambrosia");
		assertTrue(arrayTest(cs.insertionSort(firstChar), new String[] {"ambrosia", "braeburn", "cox's orange pippin", "evercrisp"}));
	}
	
	//test1x: mergeSort
	
	public void test10() {
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[0]));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[0]));
	}
	
	public void test11() {
		c.add(3);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {3}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {3}));
	}
	
	public void test12() {
		c.add(3);
		c.add(4);
		c.add(5);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {3, 4, 5}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {5, 4, 3}));
	}
	
	public void test13() {
		c.add(3);
		c.add(5);
		c.add(4);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {3, 4, 5}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {5, 4, 3}));
	}
	
	public void test14() {
		c.add(2);
		c.add(5);
		c.add(4);
		c.add(8);
		c.add(4);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {2, 4, 4, 5, 8}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {8, 5, 4, 4, 2}));
	}
	
	public void test15() {
		c.add(9);
		c.add(5);
		c.add(2);
		c.add(8);
		c.add(0);
		c.add(0);
		c.add(4);
		c.add(4);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {0, 0, 2, 4, 4, 5, 8, 9}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {9, 8, 5, 4, 4, 2, 0, 0}));
	}
	
	public void test16() {
		c.add(4);
		c.add(5);
		c.add(8);
		c.add(2);
		c.add(0);
		c.add(0);
		c.add(4);
		c.add(9);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {0, 0, 2, 4, 4, 5, 8, 9}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {9, 8, 5, 4, 4, 2, 0, 0}));
	}
	
	public void test17() {
		c.add(7);
		c.add(5);
		c.add(8);
		c.add(1);
		c.add(2);
		c.add(6);
		c.add(0);
		c.add(4);
		c.add(9);
		c.add(3);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}));
	}
	public void test18() {
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(2);
		c.add(1);
		c.add(1);
		c.add(1);
		c.add(1);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 2}));
		assertTrue(arrayTest(c.mergeSort(descending), new Integer[] {2, 1, 1, 1, 1, 1, 1, 1, 1, 1}));
	}
	
	public void test19() {
		cs.add("braeburn");
		cs.add("evercrisp");
		cs.add("cox's orange pippin");
		assertTrue(arrayTest(cs.mergeSort(lengthAscending), new String[] {"braeburn", "evercrisp", "cox's orange pippin"}));
		cs.add("ambrosia");
		assertTrue(arrayTest(cs.mergeSort(firstChar), new String[] {"ambrosia", "braeburn", "cox's orange pippin", "evercrisp"}));
	}
	
	//test2x: mergeSort stability
	
	public void test20() {
		Integer first = new Integer(5);
		Integer second = new Integer(5);
		Integer third = new Integer(5);
		c.add(2);
		c.add(first);
		c.add(8);
		c.add(second);
		c.add(third);
		assertTrue(arrayTest(c.mergeSort(ascending), new Integer[] {2, first, second, third, 8}));
		assertFalse(arrayTest(c.mergeSort(ascending), new Integer[] {2, second, first, third, 8}));
	}
	
	public void test21() {
		cs = new BasicTree<String>(lengthAscending);
		cs.add("aa");
		cs.add("b");
		cs.add("bbb");
		cs.add("bb");
		cs.add("cc");
		cs.add("c");
		cs.add("ccc");
		cs.add("a");
		assertTrue(arrayTest(cs.mergeSort(firstChar), new String[] {"a", "aa", "b", "bb", "bbb", "c", "cc", "ccc"}));
	}
}
