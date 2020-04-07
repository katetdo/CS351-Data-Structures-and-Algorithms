import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import junit.framework.TestCase;
import edu.uwm.cs351.Painting;


public class TestEfficiency extends TestCase {

	// These tests should take a few seconds only.
	
	private static final int BIG = 1 << 20; // a million
	
	private static final Comparator<Painting> value = new Comparator<Painting>() {

		@Override
		public int compare(Painting o1, Painting o2) {
			return (int)Math.signum(o1.getValue() - o2.getValue());
		}
		
	};
	
	private static final Comparator<Painting> nondiscrimination = new Comparator<Painting>() {
		@Override
		public int compare(Painting o1, Painting o2) {
			return 0;
		}
	};
	
	private Random r = new Random();
	
	protected void setUp() throws Exception {
		assert false : "Do not enable assertions for efficiency test!";
		super.setUp();
	}

	public void test1() {
		Painting.SortedCollection g = new Painting.SortedCollection(value);
		for (int i=0; i < BIG; ++i) {
			if (i > 0 && i < 100000 && (i % 10000) == 0 ||
				i >= 100000 && (i % 100000) == 0) System.out.println("... " + i + " created");
			Painting s = new Painting(new File("./Paintings/missing.jpg"), "Painting" + i, "Henry the Artist", 2019,(i*100)/BIG);
			g.add(s);
		}
		System.out.println("Painted a big random art gallery");
		// now cloning:
		assertEquals(BIG,g.clone().size());
	}
	
	public void test2() {
		Painting.SortedCollection g = new Painting.SortedCollection(nondiscrimination);
		for (int i=0; i < BIG; ++i) {
			if (i > 0 && i < 100000 && (i % 10000) == 0 ||
				i >= 100000 && (i % 100000) == 0) System.out.println("... " + i + " created");
			// Painting sorted by name!
			Painting s = new Painting(new File("./Paintings/missing.jpg"), "Painting " + i, "Henry the Artist", r.nextInt()*100, 100);
			g.add(s);
		}
		System.out.println("Painted a big random art gallery");
		for (Iterator<Painting> it = g.iterator(); it.hasNext();) {
			Painting s = it.next();
			String name = s.getName();
			if (name.endsWith("0") || name.endsWith("2") || name.endsWith("4") ||
					name.endsWith("6") || name.endsWith("8")) it.remove();
		}
		assertEquals(BIG/2,g.size());
	}
}
