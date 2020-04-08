import junit.framework.TestCase;

import java.util.Iterator;

import edu.uwm.cs351.BSTSortedSet;

public class TestEfficiency extends TestCase {
	BSTSortedSet<Integer> bal;
	BSTSortedSet<Integer> unbal;
    
    private static final int POWER = 20;
    private static final int TESTS = 2000000;
    private static final int UNBAL_SIZE = 20000;
    
    
	protected void setUp() throws Exception {
		super.setUp();

		try {assert 1/0 == 42 : "OK";}
		catch (ArithmeticException ex) {
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);}
		
		bal = new BSTSortedSet<Integer>();
		unbal = new BSTSortedSet<Integer>();
		int max = (1 << (POWER)); // 2^(POWER) = one million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				bal.add(new Integer(i));
			}
		}
		
		for(int i=UNBAL_SIZE/2; i<UNBAL_SIZE; i++)
			unbal.add(new Integer(i));
		for(int i=UNBAL_SIZE/2 - 1; i>=0; i--)
			unbal.add(new Integer(i));
	}
	
    @Override
    protected void tearDown() {
    	bal = null;
    }

    public void testSize() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals((1<<(POWER-1))-1,bal.size());
    	}
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(UNBAL_SIZE,unbal.size());
    	}
    }
    
    public void testIsEmpty() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,bal.isEmpty());
    	}
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,unbal.isEmpty());
    	}
    }
    
    public void testContains() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(bal.contains(i));
			}
		}
    	
    	//check root node repeatedly in unbalanced
    	for (int j=0; j < TESTS; j++)
			assertTrue(unbal.contains(UNBAL_SIZE/2));
    }
    
    public void testRemove() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(bal.remove(i));
			}
		}
    	
    	//remove in a favorable order from unbalanced
    	int mid = UNBAL_SIZE/2;
    	assertTrue(unbal.remove(mid));
    	for(int i=1; i < mid; i++) {
    		assertTrue(unbal.remove(mid+i));
    		assertTrue(unbal.remove(mid-i));
    	}
    }
    
    public void testGetNth() {
    	for (int i=0; i<bal.size(); i++)
    		bal.getNth(i).intValue();
    	
    	//check near root repeatedly in unbalanced
    	for (int i=0; i<TESTS; i++)
    		unbal.getNth(UNBAL_SIZE/2).intValue();
    }
    
    public void testIterate() {
    	for(Iterator<Integer> it = bal.iterator(); it.hasNext(); )
    		it.next();
    	
    	for(Iterator<Integer> it2 = unbal.iterator(); it2.hasNext(); )
    		it2.next();
    }
    
    public void testItRemove() {
    	boolean rem = false;
    	for(Iterator<Integer> it = bal.iterator(); it.hasNext(); ) {
    		it.next();
    		rem = !rem;
    		if (rem) it.remove();
    	}
    }

}
