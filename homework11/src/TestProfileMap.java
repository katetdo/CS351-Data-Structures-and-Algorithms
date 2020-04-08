import java.util.List;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Profile;
import edu.uwm.cs351.ProfileMap;
import snapshot.Snapshot;


public class TestProfileMap extends LockedTestCase {
	private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/ProfileMap.java"};
	private static boolean firstRun = true;	
	
	public void log() {
		System.out.println("running");
		Snapshot.capture(TO_LOG);
	}

	private ProfileMap pm;
	
	private Profile p0,p1,p2,p3,p4,p5,p6,p7,p8;
	
	protected void setUp() {
		try {
			assert pm.size() == 42;
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		if(firstRun) {
			log();
			firstRun = false;
		}
		pm = new ProfileMap();
		
		p0 = new Profile(null);
		p1 = new Profile("carol");
		p2 = new Profile("kellen");
		p3 = new Profile("james");
		p4 = new Profile("ariana");
		p5 = new Profile("joel");
		p6 = new Profile("noel");
		p7 = new Profile("kate");
		p8 = new Profile("penny");
	}
	
	public void test00() {
		assertEquals(0,pm.size());
	}
	
	public void test01() {
		List<Profile> l = pm.find(p0);
		assertEquals(true, l == null);
	}
	
	public void test02() {
		List<Profile> l = pm.get(p2);
		assertEquals(false, l == null);
		assertEquals(0,l.size());
		assertEquals(1,pm.size());
		l.add(p3);
		l = pm.find(p2);
		assertEquals(false, l == null);
		assertEquals(1,l.size());
		l = pm.find(p3);
		assertEquals(true, l == null);
	}
	
	public void test03() {
		List<Profile> l = pm.get(p3);
		l.add(p4);
		Profile p3c = new Profile(p3.getUsername());
		l = pm.find(p3c);
		assertEquals(false,l == null);
		assertEquals(1,l.size());
		assertEquals(p4,l.get(0));
	}
	
	public void test04() {
		Profile p4c = p4.clone();
		List<Profile> l = pm.get(p4c);
		l.add(p5);
		String nickname = p4c.getNickname();
		p4c.setNickname("dunno");
		l = pm.find(p4c); // using mutated profile
		assertEquals(true, l == null);
		l = pm.find(p4); // using original profile
		assertEquals(false, l == null);
		assertEquals(1,l.size());
		assertEquals(p5,l.get(0));
		p4c.setNickname(nickname);
		assertFalse(pm.find(p4c) == null);
	}
	
	public void test05() {
		List<Profile> l = pm.get(p5);
		l.add(p6);
		l = pm.find(p3);
		assertEquals(true, l == null);
		l = pm.get(p3); 
		assertEquals(0,l.size());
		assertEquals(2,pm.size());
	}
	
	public void test06() {
		List<Profile> l = pm.get(p3);
		l.add(p3);
		l = pm.remove(p3);
		assertTrue(l.get(0).equals(p3));
		assertEquals(0,pm.size());
		l = pm.find(p3);
		assertEquals(true, l == null);
		l = pm.get(p5);
		l.add(p3);
		l = pm.find(p3);
		assertEquals(true, l == null);
	}
	
	public void test07() {
		List<Profile> l = pm.get(p3);
		l.add(p4);
		l = pm.get(p6);
		l.add(p4);
		l = pm.remove(p3);
		assertEquals(p4,l.get(0));
		l = pm.find(p3);
		assertEquals(true, l == null);
		l = pm.find(p6);
		assertEquals(false, l == null);
		assertEquals(p4,l.get(0));
	}
	
	public void test08() {
		List<Profile> l = pm.get(p3);
		l.add(p8);
		l = pm.get(p6);
		l.add(p3);
		l = pm.get(p8);
		l.add(p6);
		pm.remove(p3);
		assertEquals(2,pm.size());
		l = pm.get(p6);
		assertEquals(p3,l.get(0));
		l = pm.get(p5);
		assertEquals(0,l.size());
		assertEquals(3,pm.size());
		pm.remove(p8);
		l = pm.get(p5);
		assertEquals(0,l.size());
	}
	
	public void test09() {
		List<Profile> l = pm.get(p2);
		l.add(p2);
		l = pm.get(p7);
		l.add(p7);
		pm.get(p3);
		pm.remove(p2);
		l = pm.get(p7);
		assertEquals(p7,l.get(0));
		pm.remove(p7);
		pm.remove(p3);
		l = pm.get(p8);
	}
	
	public void test10() {
		List<Profile> l = pm.get(p1);
		l.add(p2);
		assertEquals(l,pm.get(p1));
		l = pm.get(p3);
		l.add(p4);
		assertEquals(2,pm.size());
	}
	
	public void test11() {
		List<Profile> l = pm.get(p8);
		l.add(p6);
		l = pm.remove(p1);
		assertEquals(true,l == null);
		assertEquals(1,pm.size());
	}
	
	public void test12() {
		List<Profile> l = pm.get(p3);
		l.add(p3);
		l = pm.get(p6);
		l.add(p3);
		l.add(p6);
		l = pm.get(p8);
		l.add(p3);
		l = pm.get(p1);
		l.add(p3);
		assertEquals(4,pm.size());
		assertEquals(p3,pm.find(p3).get(0));
		assertEquals(p6,pm.find(p6).get(1));
		assertEquals(p3,pm.find(p8).get(0));
		assertEquals(p3,pm.get(p1).get(0));
	}

	public void test13() {
		pm.get(p2);
		List<Profile> l = pm.get(p3);
		l.add(p2);
		l.add(p3);
		l = pm.get(p4);
		l.add(p2);
		pm.remove(p2);
		l = pm.get(p5);
		l.add(p2);
		assertEquals(p2,pm.find(p5).get(0));
		assertEquals(p3,pm.find(p3).get(1));
		assertEquals(p2,pm.find(p4).get(0));
	}
	
	public void test14() {
		pm.get(p1).add(p1);
		pm.get(p2);
		pm.get(p3);
		pm.get(p4);
		pm.get(p5);
		pm.get(p6);
		pm.get(p7);
		pm.get(p8).add(p1);
		pm.remove(p1);
		pm.remove(p2);
		assertEquals(0, pm.get(p1).size());
		assertEquals(1, pm.get(p8).size());
		assertEquals(7, pm.size());
		assertEquals(7, pm.getAll().size());
		assertTrue(pm.getAll().contains(p1));
		assertFalse(pm.getAll().contains(p2));
		assertTrue(pm.getAll().contains(p3));
		assertTrue(pm.getAll().contains(p4));
		assertTrue(pm.getAll().contains(p5));
		assertTrue(pm.getAll().contains(p6));
		assertTrue(pm.getAll().contains(p7));
		assertTrue(pm.getAll().contains(p8));
	}
	
	//testing map add
	
	public void test15() {
		assertTrue(pm.add(p1));
		List<Profile> l = pm.get(p1);
		assertFalse(l == null);
		assertEquals(0, l.size());
	}
	
	public void test16() {
		pm.get(p1);
		assertFalse(pm.add(p1));
		assertEquals(0, pm.get(p1).size());
	}
	
	public void test17() {
		assertTrue(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test18() {
		pm.get(p1);
		assertTrue(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test19() {
		pm.get(p2);
		assertTrue(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test20() {
		pm.get(p1);
		pm.get(p2);
		assertTrue(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test21() {
		pm.get(p1).add(p2);
		assertTrue(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test22() {
		pm.get(p1).add(p2);
		pm.get(p2);
		assertFalse(pm.add(p1, p2));
		assertEquals(1, pm.get(p1).size());
		assertEquals(2, pm.size());
		assertEquals(0, pm.get(p2).size());
	}
	
	public void test23() {
		pm.add(p1, p2);
		pm.add(p1, p4);
		pm.add(p1, p6);
		pm.add(p2, p1);
		pm.add(p2, p2);
		pm.add(p3, p5);
		pm.add(p6, p6);
		pm.add(p8, p1);
		pm.add(p8, p6);
		pm.add(p8, p8);
		pm.remove(p3);
		pm.remove(p2);
		pm.get(p2).add(p7);
		assertTrue(pm.getAll().contains(p1));
		assertTrue(pm.getAll().contains(p2));
		assertFalse(pm.getAll().contains(p3));
		assertTrue(pm.getAll().contains(p4));
		assertTrue(pm.getAll().contains(p5));
		assertTrue(pm.getAll().contains(p6));
		assertFalse(pm.getAll().contains(p7));
		assertTrue(pm.getAll().contains(p8));
		assertEquals(3, pm.find(p1).size());
		assertEquals(1, pm.find(p2).size());
		assertEquals(p7, pm.find(p2).get(0));
		assertNull(pm.find(p3));
		assertEquals(0, pm.find(p4).size());
		assertEquals(0, pm.find(p5).size());
		assertEquals(1, pm.find(p6).size());
		assertNull(pm.find(p7));
		assertEquals(3, pm.find(p8).size());
	}
	
	//testing immutability of stored keys
	
	public void test24() {
		pm.add(p1);
		List<Profile> l = pm.getAll();
		Profile pg = l.get(0);
		assertEquals(p1, pg);
		pg.setNickname("testing");
		assertFalse(p1.equals(pg));
		assertFalse(pm.find(p1) == null);
		assertNull(pm.find(pg));
		p1.setNickname("testing");
		assertEquals(p1,pg);
		assertNull(pm.find(p1));
	}
	
	public void test25() {
		Profile pt = new Profile("initial");
		pm.add(p1, pt);
		assertFalse(pm.find(pt) == null);
		Profile pc = pm.find(p1).get(0);
		pc.setNickname("testing");
		//previous line might have changed pt
		pt = new Profile("initial");
		assertFalse(pm.find(p1).contains(pt));
		assertFalse(pm.find(pt) == null);
		assertNull(pm.find(pc));
	}
	
	//locked tests
	
	public void test26() {
		assertEquals(Tb(1912561601), pm.find(p1) == null);
		assertEquals(Tb(136433601), pm.get(p1) == null);
		assertEquals(Tb(1184504961), pm.find(p1) == null);
		pm.get(p2).add(p3);
		assertEquals(Tb(988243974), pm.get(p2).contains(p3));
		assertEquals(Tb(1102117612), pm.add(p2, p1));
		assertEquals(Tb(1109259919), pm.get(p2).contains(p1));
		assertEquals(Tb(1744818208), pm.add(p2, p1));
		assertEquals(Tb(2076759050), pm.add(p2, p4));
		assertEquals(Tb(1493664962), pm.find(p4) == null);
	}
}
