
import java.util.AbstractMap.SimpleEntry;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Grade;
import edu.uwm.cs351.Student;
import edu.uwm.cs351.StudentReportMap;
import edu.uwm.cs351.StudentReportMap.AtRiskSet;
import snapshot.Snapshot;


	
	

public class TestStudentReportMap extends LockedTestCase{
	
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/StudentReportMap.java"};
	private static boolean firstRun = true;
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}

	protected StudentReportMap m0, m1;
	protected Set<Entry<Student,Grade>> es0, es1;
	protected AtRiskSet ar0, ar1;
	Student s0, s1, s2, s3, s4;
	Grade g0, g1, g2, g3, g4;
	Entry<Student, Grade> e0, e1, e2, e3, e4;
	Iterator<Entry<Student, Grade>> eIt;
	Iterator<Student> sIt;
	


	@Override
	protected void setUp() {
		try {
			assert m0.size() == 31415;
			throw new IllegalStateException("assertions must be enabled to run this test");
		} catch (NullPointerException ex) {
			// OK!
			if(firstRun) {
				log();
				firstRun = false;
			}
			
			m0 = new StudentReportMap();
			m1 = new StudentReportMap();
			
			es0 = m0.entrySet();
			es1 = m1.entrySet();
			
			ar0 = m0.atRiskSet();
			ar1 = m1.atRiskSet();

			s0 = new Student("Adam");
			s1 = new Student("Badam");
			s2 = new Student("Cadam");
			s3 = new Student("Dadam");
			s4 = new Student("Eadam");

			g0 = new Grade(100.0);
			g1 = new Grade(95.0);
			g2 = new Grade(80.0);
			g3 = new Grade(65.0);
			g4 = new Grade(60.0);
			
			e0 = new SimpleEntry<Student,Grade>(s0,g0);
			e1 = new SimpleEntry<Student,Grade>(s1,g1);
			e2 = new SimpleEntry<Student,Grade>(s2,g2);
			e3 = new SimpleEntry<Student,Grade>(s3,g3);
			e4 = new SimpleEntry<Student,Grade>(s4,g4);
			
			m1.put(s2, g2);
			m1.put(s1, g1);
			m1.put(s0, g0);
			m1.put(s4, g4);
			m1.put(s3, g3);
		}
	}
	
	
	//test0x: just Map methods
		

	public void test00() {
		assertTrue(m0.isEmpty());
		assertEquals(0,m0.size());
	}

	public void test01() {
		assertNull(m0.get(new Student("James")));
		assertNull(m0.get(s1));
		assertNull(m0.get(s2));
		assertNull(m0.get(33));
		assertNull(m0.get("adam"));
		assertNull(m0.get(null));

		assertFalse(m0.containsKey(new Student("James")));
		assertFalse(m0.containsKey(s1));
		assertFalse(m0.containsKey(s2));
		assertFalse(m0.containsKey(33));
		assertFalse(m0.containsKey("adam"));
		assertFalse(m0.containsKey(null));
	}

	public void test02(){
		assertNull(m0.put(s0, g0));
		assertNull(m0.put(s1, g1));
		assertNull(m0.put(s2, g2));
		assertNull(m0.put(s3, g3));
		assertNull(m0.put(s4, g4));

		assertEquals(5, m0.size());
		assertEquals(new Grade(100), m0.put(s0, new Grade(97)));
		assertEquals(5, m0.size());
		assertEquals(new Grade(95), m0.put(s1, new Grade(98)));
		assertEquals(5, m0.size());
		assertEquals(new Grade(80), m0.put(s2, new Grade(99)));
		assertEquals(5, m0.size());
		assertEquals(new Grade(65), m0.put(s3, new Grade(59)));
		assertEquals(5, m0.size());
		assertEquals(new Grade(60), m0.put(s4, new Grade(66)));
		assertEquals(5, m0.size());
	}

	public void test03() {
		try {
			m0.put(null, g0);
			assertFalse("null argument should have thrown an exception",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
		try {
			m0.put(s0, null);
			assertFalse("null argument should have thrown an exception",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof IllegalArgumentException);
		}
	}

	public void test04() {
		Student james = new Student("James");
		Student kate = new Student("Kate");
		m0.put(james, new Grade(65));
		m0.put(kate, new Grade(95));
		assertEquals(new Grade(65), m0.put(james,new Grade(80)));
		assertFalse(m0.isEmpty());
		assertEquals(2,m0.size());
		assertNull(m0.get(new Student("Lukas")));
		assertEquals(new Grade(95),m0.get(kate));
		assertNull(m0.get(null));
	}


	public void test05() {
		assertNull(m0.put(s3, g3));
		assertEquals(new Grade(65), m0.put(s3, new Grade(50)));
		assertFalse(m0.isEmpty());
		assertEquals(1, m0.size());
		assertEquals(new Grade(50), m0.get(s3));
		assertNull(m0.get(null));
		assertTrue(m0.containsKey(s3));
		assertFalse(m0.containsKey(s4));
	}
	
	public void test06() {
		m0.put(s1, g1);
		assertNull(m0.remove(s0));
		assertNull(m0.remove(null));
		assertNull(m0.remove(33));
		assertNull(m0.remove("Badam"));
		assertEquals(1, m0.size());
	}
	
	public void test07() {
		assertEquals(g1, m1.remove(s1));
		assertEquals(4, m1.size());
		assertFalse(m1.containsKey(s1));
		assertEquals(null, m1.remove(s1));
		assertFalse(m1.containsKey(s1));
		assertEquals(4, m1.size());
		assertEquals(g2, m1.remove(s2));
		assertEquals(g3, m1.remove(s3));
		assertEquals(g4, m1.remove(s4));
		assertEquals(g0, m1.remove(s0));
		assertEquals(0, m1.size());
		assertFalse(m1.containsKey(s0));
		assertFalse(m1.containsKey(s2));
		assertFalse(m1.containsKey(s3));
		assertFalse(m1.containsKey(s4));
	}
	
	public void test08() {
		assertEquals(g3, m1.remove(s3));
		assertNull(m1.put(s3, g3));
		assertEquals(g4, m1.put(s4, g2));
		assertEquals(g2, m1.remove(s4));
		assertNull(m1.remove(s4));
		assertNull(m1.put(s4, g1));
		assertTrue(m1.containsKey(s4));
		assertEquals(g1, m1.remove(s1));
		assertEquals(g1, m1.get(s4));
		assertEquals(null, m1.get(s1));
		assertEquals(4, m1.size());
		m1.clear();
		assertEquals(0, m1.size());
		assertTrue(m1.isEmpty());
	}
	
	//test1x: Map and EntrySet
	
	public void test10() {
		assertEquals(0, es0.size());
		assertTrue(es0.isEmpty());
	}
	
	public void test11() {
		assertEquals(5, es1.size());
		assertFalse(es1.isEmpty());
	}
	
	public void test12() {
		assertFalse(es1.contains(null));
		assertFalse(es1.contains(new SimpleEntry<Student,Grade>(s1, null)));
		assertFalse(es1.contains(new SimpleEntry<Student,Grade>(null, g1)));
	}
	
	public void test13() {
		assertFalse(es1.contains(33));
		assertFalse(es1.contains(new SimpleEntry<String,Grade>("Adam", g0)));
		assertFalse(es1.contains(new SimpleEntry<Student,Double>(s0, 100.0)));
		//should work for raw types
		assertFalse(es1.contains(new SimpleEntry("Adam", g0)));
		assertFalse(es1.contains(new SimpleEntry(s0, 100.0)));
	}
	
	public void test14() {
		assertTrue(es1.contains(e0));
		assertTrue(es1.contains(e1));
		assertTrue(es1.contains(e2));
		assertTrue(es1.contains(e3));
		assertTrue(es1.contains(e4));
	}
	
	public void test15() {
		assertTrue(es1.contains(e1));
		//should work for raw types
		assertTrue(es1.contains(new SimpleEntry(s1, g1)));
		assertTrue(es1.contains(new SimpleEntry<Student,Grade>(new Student("Badam"), new Grade(95.0))));
		assertFalse(es1.contains(new SimpleEntry<Student,Grade>(s1, g2)));
		assertFalse(es1.contains(new SimpleEntry<Student,Grade>(new Student("Badam"), new Grade(100.0))));
	}
	
	public void test16() {
		m0.put(s2,g4);
		assertFalse(es0.contains(e2));
		assertTrue(es0.contains(new SimpleEntry<Student,Grade>(s2,g4)));
		es0 = m0.entrySet();
		m0.put(s2,g3);
		assertFalse(es0.contains(new SimpleEntry<Student,Grade>(s2,g4)));
		assertTrue(es0.contains(new SimpleEntry<Student,Grade>(s2,g3)));
		m1.remove(s4);
		assertFalse(es1.contains(e4));
		m1.clear();
		assertFalse(es1.contains(e0));
	}
	
	public void test17() {
		assertFalse(es1.remove(null));
		assertFalse(es1.remove(new SimpleEntry<Student,Grade>(null,g4)));
		assertFalse(es1.remove(new SimpleEntry<Student,Grade>(s2,null)));
		assertFalse(es1.remove(new SimpleEntry("Adam",g0)));
		assertFalse(es1.remove(new SimpleEntry(s0,100.0)));
		assertFalse(es1.remove(new SimpleEntry<Student,Grade>(s2,g4)));
	}
	
	public void test18() {
		assertTrue(es1.remove(e2));
		assertEquals(4, es1.size());
		assertEquals(4, m1.size());
		assertNull(m1.get(s2));
		assertNull(m1.put(s2,g1));
		assertTrue(es1.contains(new SimpleEntry<Student,Grade>(s2,g1)));
		assertEquals(5, es1.size());
		assertEquals(5, m1.size());
		es1.clear();
		assertEquals(0, es1.size());
		assertEquals(0, m1.size());
	}
	
	public void test19() {
		try {
			es0.add(new SimpleEntry<Student,Grade>(s0,g0));
			assertFalse("add should throw exception",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof UnsupportedOperationException);
		}
	}


	//test2x: tests for AtRiskSet
	public void test20() {
		assertTrue(ar0.isEmpty());
		assertEquals(0, ar0.size());
	}
	
	public void test21() {
		assertFalse(ar1.isEmpty());
		assertEquals(2, ar1.size());
	}
	
	public void test22() {
		assertFalse(ar1.contains(null));
		assertFalse(ar1.contains("Dadam"));
		assertFalse(ar1.contains(s0));
		assertFalse(ar1.contains(s1));
		assertFalse(ar1.contains(s2));
		assertTrue(ar1.contains(s3));
		assertTrue(ar1.contains(s4));
		assertTrue(ar1.contains(new Student("Dadam")));
	}
	
	public void test23() {
		assertFalse(ar1.add(s3));
		assertFalse(ar1.add(s4));
		assertFalse(ar1.add(new Student("Madam")));
		assertEquals(2, ar1.size());
		assertEquals(5, m1.size());
		assertEquals(5, es1.size());
		assertFalse(m1.containsKey(new Student("Madam")));
	}
	
	public void test24() {
		assertTrue(ar1.add(s0));
		assertTrue(ar1.add(s1));
		assertTrue(ar1.add(s2));
		assertEquals(5, ar1.size());
		assertTrue(ar1.contains(s0));
		assertTrue(ar1.contains(s1));
		assertTrue(ar1.contains(s2));
		assertTrue(ar1.contains(s3));
		assertTrue(ar1.contains(s4));
	}
	
	public void test25() {
		assertFalse(ar0.add(s0));
		m0.put(s0, g0);
		m0.put(s1, g1);
		m0.put(s2, g2);
		assertFalse(ar0.contains(s3));
		assertTrue(ar0.isEmpty());
		assertTrue(ar0.add(s1));
		assertEquals(1, ar0.size());
		//old student, new grade
		m0.put(s1, g4);
		//doesn't add, even though low grade
		assertEquals(1, ar0.size());
		//new student, new grade
		m0.put(s4, g4);
		//adds because low
		assertTrue(ar0.contains(s4));
		assertEquals(2, ar0.size());
		m0.put(s4, g0);
		assertTrue(ar0.contains(s4));
		assertEquals(2, ar0.size());
	}

	public void test26() {
		assertFalse(ar1.remove(s0));
		assertFalse(ar1.remove(s1));
		assertFalse(ar1.remove(s2));
		assertFalse(ar1.remove(new Student("Madam")));
		assertTrue(m1.containsKey(s0));
		assertEquals(5, m1.size());
	}
	
	public void test27() {
		assertTrue(ar1.remove(s3));
		assertTrue(ar1.remove(s4));
		assertEquals(0, ar1.size());
		assertEquals(5, m1.size());
		assertTrue(m1.containsKey(s3));
		assertTrue(m1.containsKey(s4));
	}
	
	public void test28() {
		ar1.clear();
		assertTrue(ar1.isEmpty());
		assertEquals(5, m1.size());
		assertEquals(5, es1.size());
		ar1.clear();
		assertTrue(ar1.isEmpty());
		assertEquals(5, m1.size());
		assertEquals(5, es1.size());
	}
	
	public void test29() {
		assertNull(ar0.getGrade(s1));
		assertNull(ar0.getGrade(null));
		assertNull(ar0.getGrade(new Student("Madam")));
		assertEquals(new Grade(65.0), ar1.getGrade(s3));
		assertEquals(new Grade(60.0), ar1.getGrade(s4));
	}
	
	//test3x: combinations of views
	
	public void test30() {
		es1.remove(e4);
		assertEquals(4, m1.size());
		assertEquals(1, ar1.size());
		assertFalse(ar1.contains(s4));
		assertTrue(ar1.contains(s3));
	}
	
	public void test31() {
		ar1.remove(s4);
		assertTrue(es1.contains(e4));
	}
	
	public void test32() {
		m1.clear();
		assertTrue(ar1.isEmpty());
		assertFalse(ar1.iterator().hasNext());
	}
	
	public void test33() {
		es1.clear();
		assertTrue(ar1.isEmpty());
		assertFalse(ar1.iterator().hasNext());
	}
	
	public void test34() {
		assertEquals(new Grade(60.0), ar1.getGrade(s4));
		m1.put(s4, new Grade(72.0));
		assertEquals(new Grade(72.0), ar1.getGrade(s4));
		assertTrue(ar1.contains(s4));
		m1.remove(s4);
		assertFalse(ar1.contains(s4));
		m1.put(s4, new Grade(72.0));
		assertFalse(ar1.contains(s4));
		assertNull(ar1.getGrade(s4));
	}
	
	//test4x: testing fail-fast on iterators
	
	public void test40() {
		sIt = ar0.iterator();
		eIt = es0.iterator();
		assertFalse(sIt.hasNext());
		m0.clear();
		assertTrue(m0.isEmpty());
		assertFalse(sIt.hasNext());
		m0.remove(null);
		assertFalse(sIt.hasNext());
		m0.remove("nothing");
		assertFalse(sIt.hasNext());
		m0.remove(s2);
		assertFalse(sIt.hasNext());
		m0.containsKey(s2);
		assertFalse(sIt.hasNext());
		assertFalse(eIt.hasNext());
	}
	
	public void test41() {
		sIt = ar1.iterator();
		eIt = es1.iterator();
		m1.put(s0, g0);
		assertTrue(sIt.hasNext());
		m1.get(s0);
		m1.get(s3);
		assertNull(m1.get(new Student("Madam")));
		assertTrue(sIt.hasNext());
		m1.remove(new Student("Madam"));
		assertEquals(5, m1.size());
		assertTrue(sIt.hasNext());
		assertTrue(eIt.hasNext());
	}
	
	public void test42() {
		sIt = ar0.iterator();
		eIt = es0.iterator();
		m0.put(s1,g1);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		
		sIt = ar0.iterator();
		eIt = es0.iterator();
		m0.put(s1,g2);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		
		sIt = ar0.iterator();
		eIt = es0.iterator();
		m0.remove(s1);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		
		sIt = ar1.iterator();
		eIt = es1.iterator();
		m1.clear();
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
	}
	
	public void test43() {
		sIt = ar0.iterator();
		eIt = es0.iterator();
		assertFalse(sIt.hasNext());
		es0.clear();
		assertTrue(es0.isEmpty());
		assertFalse(sIt.hasNext());
		es0.remove(null);
		assertFalse(sIt.hasNext());
		es0.remove("nothing");
		assertFalse(sIt.hasNext());
		es0.remove(new SimpleEntry("Adam", 100.0));
		assertFalse(sIt.hasNext());
		es0.remove(e2);
		assertFalse(sIt.hasNext());
		es0.contains(s3);
		assertFalse(sIt.hasNext());
		assertFalse(eIt.hasNext());
	}
	
	public void test44() {
		sIt = ar1.iterator();
		eIt = es1.iterator();
		es1.contains(e0);
		es1.contains(e3);
		assertFalse(es1.contains(new Student("Adam")));
		assertTrue(sIt.hasNext());
		es1.remove(new SimpleEntry<Student,Grade>(s2,g4));
		assertEquals(5, es1.size());
		assertTrue(sIt.hasNext());
		assertTrue(eIt.hasNext());
	}
	
	public void test45() {
		sIt = ar1.iterator();
		eIt = es1.iterator();
		es1.remove(e3);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		sIt = ar1.iterator();
		eIt = es1.iterator();
		es1.clear();
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
	}
	
	public void test46() {
		sIt = ar0.iterator();
		eIt = es0.iterator();
		assertFalse(sIt.hasNext());
		ar0.clear();
		assertTrue(ar0.isEmpty());
		assertFalse(sIt.hasNext());
		ar0.remove(null);
		assertFalse(sIt.hasNext());
		ar0.remove("nothing");
		assertFalse(sIt.hasNext());
		ar0.remove(new Student("Adam"));
		assertFalse(sIt.hasNext());
		ar0.contains(s3);
		assertFalse(sIt.hasNext());
		assertNull(ar0.getGrade(s2));
		assertFalse(sIt.hasNext());
		assertFalse(ar0.add(s3));
		assertFalse(sIt.hasNext());
		assertFalse(eIt.hasNext());
	}
	
	public void test47() {
		sIt = ar1.iterator();
		eIt = es1.iterator();
		assertFalse(ar1.contains(s0));
		assertTrue(ar1.contains(s3));
		assertFalse(ar1.contains(new Student("Adam")));
		assertTrue(sIt.hasNext());
		assertFalse(ar1.remove(s2));
		assertEquals(2, ar1.size());
		assertTrue(sIt.hasNext());
		assertEquals(g3, ar1.getGrade(s3));
		assertTrue(sIt.hasNext());
		assertFalse(ar1.add(s3));
		assertTrue(sIt.hasNext());
		assertTrue(eIt.hasNext());
	}
	
	public void test48() {
		sIt = ar1.iterator();
		eIt = es1.iterator();
		ar1.remove(s3);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		sIt = ar1.iterator();
		eIt = es1.iterator();
		ar1.clear();
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		sIt = ar1.iterator();
		eIt = es1.iterator();
		ar1.add(s1);
		try {
			sIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
		try {
			eIt.hasNext();
			assertFalse("should be stale",true);
		} catch (RuntimeException ex) {
			assertTrue(ex instanceof ConcurrentModificationException);
		}
	}
}
