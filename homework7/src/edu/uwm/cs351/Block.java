package edu.uwm.cs351;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 *An enum class 7 different block shapes
 *
 */

public enum Block {
	Z,
	S,
	L,
	J,
	O,
	I,
	T;
	
	private static Block b(String s) {
		return Block.valueOf(s);
	}
	
	public final static Block[] RBG_TABLE = {b("J"), b("T"), b("Z"), b("I"), b("J"), b("J"),
			b("I"), b("O"), b("Z"), b("S"), b("L"), b("O"), b("T"), b("Z"), b("T"),
			b("I"), b("J"), b("L"), b("O"), b("T"), b("Z"), b("Z"), b("I"), b("O"),
			b("Z"), b("O"), b("I"), b("O"), b("T"), b("L"), b("S"), b("S"), b("S"),
			b("O"), b("Z"), b("T"), b("S"), b("I"), b("J"), b("S"), b("O"), b("Z"),
			b("Z"), b("O"), b("J"), b("I"), b("S"), b("L"), b("L"), b("S"), b("Z"),
			b("S"), b("L"), b("L"), b("O"), b("Z"), b("T"), b("L"), b("Z"), b("L"),
			b("I"), b("S"), b("J"), b("O"), b("T"), b("Z"), b("S"), b("S"), b("I"),
			b("O"), b("T"), b("I"), b("Z"), b("I"), b("J"), b("Z"), b("I"), b("T"),
			b("I"), b("Z"), b("J"), b("J"), b("Z"), b("J"), b("L"), b("J"), b("J"),
			b("L"), b("O"), b("L"), b("L"), b("S"), b("S"), b("I"), b("S"), b("S"),
			b("O"), b("T"), b("T"), b("T"), b("O"), b("T"), b("Z"), b("J"), b("S"),
			b("L"), b("S"), b("Z"), b("J"), b("S"), b("O"), b("O"), b("O"), b("T"),
			b("I"), b("O"), b("J"), b("L"), b("T"), b("Z"), b("Z"), b("S"), b("S"),
			b("L"), b("L"), b("J"), b("J"), b("L"), b("S"), b("J"), b("T"), b("L"),
			b("O"), b("O"), b("T"), b("I"), b("T"), b("I"), b("L"), b("T"), b("T"),
			b("I"), b("S"), b("I"), b("Z"), b("J"), b("L"), b("T"), b("L"), b("I"),
			b("Z"), b("I"), b("I"), b("O"), b("I"), b("L"), b("J"), b("J"), b("O"),
			b("T"), b("J")};
	
	//draws polygon blocks
	public void draw(Graphics g, Block b, int size, int x, int y) {
		Polygon p;
		switch (b) {
	    case Z : g.setColor(Color.RED);
	    p = new Polygon();
	    p.addPoint(x+(0*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(2*size));
	    p.addPoint(x+(1*size), y+(2*size));
	    p.addPoint(x+(1*size), y+(1*size));
	    p.addPoint(x+(0*size), y+(1*size));
	    p.addPoint(x+(0*size), y+(0*size));

	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case S : g.setColor(Color.GREEN);
	    p = new Polygon();
	    
	    p.addPoint(x+(1*size), y+(0*size));
	    p.addPoint(x+(3*size), y+(0*size));
	    p.addPoint(x+(3*size), y+(1*size));
	    p.addPoint(x+(2*size), y+(1*size));
	    p.addPoint(x+(2*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(0*size));
	   
	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case L : g.setColor(Color.ORANGE);
	    p = new Polygon();
	    p.addPoint(x+(0*size), y+(0*size));
	    p.addPoint(x+(3*size), y+(0*size));
	    p.addPoint(x+(3*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(0*size));
	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case J : g.setColor(Color.BLUE);
	    p = new Polygon();
	    p.addPoint(x+(0*size), y+(0*size));
	    p.addPoint(x+(1*size), y+(0*size));
	    p.addPoint(x+(1*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(0*size));

	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case O : g.setColor(Color.YELLOW);
	    p = new Polygon();
	    p.addPoint(x+(0*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(0*size));
	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case I : g.setColor(Color.CYAN);
	    p = new Polygon();
	    p.addPoint(x+(0*size), y+(0*size));
	    p.addPoint(x+(4*size), y+(0*size));
	    p.addPoint(x+(4*size), y+(1*size));
	    p.addPoint(x+(0*size), y+(1*size));
	    p.addPoint(x+(0*size), y+(0*size));
	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
	    case T : g.setColor(new Color(192, 0, 255));//Purple
	    p = new Polygon();
	    p.addPoint(x+(1*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(0*size));
	    p.addPoint(x+(2*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(1*size));
	    p.addPoint(x+(3*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(2*size));
	    p.addPoint(x+(0*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(1*size));
	    p.addPoint(x+(1*size), y+(0*size));
	    
	    g.fillPolygon(p);
	    g.drawPolygon(p);
	    break;
		}
		
	}
}
	

