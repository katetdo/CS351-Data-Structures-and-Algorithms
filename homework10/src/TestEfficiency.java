import junit.framework.TestCase;
import snapshot.Snapshot;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

import edu.uwm.cs351.Grade;
import edu.uwm.cs351.Student;
import edu.uwm.cs351.StudentReportMap;
import edu.uwm.cs351.StudentReportMap.AtRiskSet;

public class TestEfficiency extends TestCase {
	
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/StudentReportMap.java"};
	private static boolean firstRun = true;	
	
	StudentReportMap bal;
	Set<Entry<Student,Grade>> es;
	AtRiskSet ar;
    
    private static final int POWER = 20;
    private static final int TESTS = 2000000;
    
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}
    
    
	protected void setUp() throws Exception {
		super.setUp();

		try {assert 1/0 == 42 : "OK";}
		catch (ArithmeticException ex) {
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);}
		
		if(firstRun) {
			log();
			firstRun = false;
		}
		
		bal = new StudentReportMap();
		int max = (1 << (POWER)); // 2^(POWER) = one million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				bal.put(new Student(Integer.toString(i, 10)), new Grade(50.0));
			}
		}
		
		es = bal.entrySet();
		ar = bal.atRiskSet();
	}
	
    @Override
    protected void tearDown() {
    	bal = null;
    }

    public void testMapSize() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals((1<<(POWER-1))-1,bal.size());
    	}
    }
    
    public void testMapIsEmpty() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,bal.isEmpty());
    	}
    }
    
    public void testMapContainsKey() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(bal.containsKey(new Student(Integer.toString(i, 10))));
			}
		}
    }
    
    public void testMapGet() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertEquals(new Grade(50.0), bal.get(new Student(Integer.toString(i, 10))));
			}
		}
    }
    
    public void testMapRemove() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				bal.remove(new Student(Integer.toString(i, 10)));
			}
		}
    }
    
    public void testEntrySize() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals((1<<(POWER-1))-1,es.size());
    	}
    }
    
    public void testEntryIsEmpty() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,es.isEmpty());
    	}
    }
    
    public void testEntryContains() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(es.contains(new SimpleEntry<Student,Grade>(new Student(Integer.toString(i, 10)), new Grade(50.0))));
			}
		}
    }
    
    public void testEntryRemove() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				es.remove(new SimpleEntry<Student,Grade>(new Student(Integer.toString(i, 10)), new Grade(50.0)));
			}
		}
    }

    public void testAtRiskSize() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals((1<<(POWER-1))-1,ar.size());
    	}
    }
    
    public void testAtRiskIsEmpty() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,ar.isEmpty());
    	}
    }
    
    public void testAtRiskContainsTrue() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(ar.contains(new Student(Integer.toString(i, 10))));
			}
		}
    }
    
    public void testAtRiskContainsFalse() {
    	bal.put(new Student("Madam"), new Grade(100.0));
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,ar.contains(new Student("Madam")));
    	}
    }
    
    public void testAtRiskGetGrade() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertEquals(new Grade(50.0), ar.getGrade(new Student(Integer.toString(i, 10))));
			}
		}
    }
    
    public void testAtRiskRemove() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				ar.remove(new Student(Integer.toString(i, 10)));
			}
		}
    }
    
    public void testAtRiskAdd() {
    	ar.clear();
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				ar.add(new Student(Integer.toString(i, 10)));
			}
		}
    }

}
