import java.io.File;
import java.util.Comparator;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.*;
import edu.uwm.cs351.util.Alphabetical_name;
import edu.uwm.cs351.util.DateDescending;
import edu.uwm.cs351.util.ValueDescending;

public class TestComparators extends LockedTestCase {
	
	private Comparator<Painting> c;
	private Painting p1, p2, p3, p4, p5, p6, p7, p8;
	
	@Override
	protected void setUp() {
		p1 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow","Mondrian", 1930, 50600000);
		p2 = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
		p3 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		p4 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
		p5 = new Painting(new File("./Paintings/rothko.jpg"), "Red on Red", "Rothko", 1969, 8237000);
		p6 = new Painting(new File("./Paintings/matisse.jpg"), "The Cat with Red Fish","Matisse", 1906, 33500000);
		p7 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow - Draft", "Mondrian", 1930, 150600000);
		p8 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);

	}
	
	private String asString(int c) {
		if (c == 0) return "=";
		else if (c < 0) return "<";
		else return ">";
	}
	
	public void test00(){
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <

		p1 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow","Mondrian", 1930, 50600000);
		p2 = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
		p3 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		p4 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		p5 = new Painting(new File("./Paintings/rothko.jpg"), "Red on Red", "Rothko", 1969, 8237000);
		
		c = Alphabetical_name.getInstance();
		assertEquals("p1 __ p2", Ts(976464052), asString(c.compare(p1, p2)));
		assertEquals("p3 __ p2", Ts(214343991), asString(c.compare(p3, p2)));
		assertEquals("p3 __ p4", Ts(2110890530), asString(c.compare(p3, p4)));
		assertEquals("p5 __ p4", Ts(375496229), asString(c.compare(p5, p4)));
	}
	
	public void test01(){
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <

		p1 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow","Mondrian", 1930, 50600000);
		p2 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
		p3 = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
		p4 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		p5 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		
		c = ValueDescending.getInstance();
		assertEquals("p1 __ p2", Ts(1768860844), asString(c.compare(p1, p2)));
		assertEquals("p2 __ p3", Ts(1629073831), asString(c.compare(p2, p3)));
		assertEquals("p4 __ p3", Ts(1088765837), asString(c.compare(p4, p3)));
		assertEquals("p5 __ p4", Ts(15629478), asString(c.compare(p5, p4)));
	}
	
	public void test02(){
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <

		p1 = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
		p2 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow", "Mondrian", 1930, 50600000);
		p3 = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
		p4 = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow - Draft", "Mondrian", 1930, 150600000);
		p5 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
		
		c = DateDescending.getInstance();
		assertEquals("p2 _ p1", Ts(572782944), asString(c.compare(p2, p1)));
		assertEquals("p3 _ p2", Ts(1152809541), asString(c.compare(p3, p2)));
		assertEquals("p3 _ p4", Ts(2112395742), asString(c.compare(p3, p4)));
		assertEquals("p4 _ p5", Ts(1546268744), asString(c.compare(p4, p5)));
	}
	
	protected void testComparator(Comparator<Painting> c, Painting[]... groups) {
		for (int i=0; i < groups.length; ++i) {
			for (Painting p1 : groups[i]) {
				for (int j=0; j < groups.length; ++j) {
					for (Painting p2 : groups[j]) {
						try {
							int expected = i-j;
							int result = c.compare(p1, p2);
							testComparison(c + "(" + p1 + "," + p2 + ")", expected, result);
						} catch (RuntimeException ex) {
							assertFalse(c + "(" + p1 + "," + p2 + ") threw exception " +ex, true);
						}
					}
				}
			}
		}
	}

	private void testComparison(String explain, int expected, int result) {
		if (result < 0 && expected < 0 || result > 0 && expected > 0 || result == 0 && expected == 0) {
			assertTrue(true);
		} else {
			assertFalse(explain + " = " + result + ", not " + asString(expected) + " 0",true);
		}
	}
	
	public void testAlphabetical() {
		testComparator(Alphabetical_name.getInstance(),
				new Painting[]{p1},
				new Painting[]{p7},
				new Painting[]{p2},
				new Painting[]{p3, p8},
				new Painting[]{p4},
				new Painting[]{p5},
				new Painting[]{p6});
	}
	
	public void testValueDescending() {
		testComparator(ValueDescending.getInstance(), 
				new Painting[]{p7},
				new Painting[]{p1},
				new Painting[]{p6},
				new Painting[]{p5},
				new Painting[]{p4},
				new Painting[]{p2},
				new Painting[]{p3, p8});
	}
	
	public void testDateDescending() {
		testComparator(DateDescending.getInstance(), 
				new Painting[]{p3, p8},
				new Painting[]{p5},
				new Painting[]{p2},
				new Painting[]{p1, p7},
				new Painting[]{p4},
				new Painting[]{p6});
	}
}
