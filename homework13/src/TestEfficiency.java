import java.util.Comparator;
import junit.framework.TestCase;
import snapshot.Snapshot;
import edu.uwm.cs351.BasicTree;


public class TestEfficiency extends TestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/BasicTree.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}
	private static final int POWER = 18;
	
	BasicTree<Integer> bal;
	Comparator<Integer> descending = new Comparator<Integer>() {
		public int compare(Integer a, Integer b) {return b-a;}
	};
	Comparator<Integer> ascending = new Comparator<Integer>() {
		public int compare(Integer a, Integer b) {return a-b;}
	};

	protected void setUp() throws Exception {
		super.setUp();
		
		try {assert 1/0 == 42 : "OK";}
		catch (ArithmeticException ex) {
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);}
		
		if(firstRun) {
			log();
			firstRun = false;
		}
		
		bal = new BasicTree<Integer>(new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		});
		int max = (1 << (POWER)); // 2^(POWER) = one million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				bal.add(i);
			}
		}
	}
	
	public void testMergeSort() {
		bal.mergeSort(descending);
		bal.mergeSort(ascending);
	}
	
	public void testInsertionSort() {
		bal.insertionSort(descending);
		bal.insertionSort(ascending);
	}
}
