
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uwm.cs351.Painting;
import edu.uwm.cs351.util.Alphabetical_artist;
import edu.uwm.cs351.util.ValueDescending;
import edu.uwm.cs351.util.DateDescending;
import edu.uwm.cs351.util.Alphabetical_name;


/**
 * Render some pixels on the screen.
 */
public class Demo extends JFrame {
	/**
	 * Eclipse wants this
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Demo x = new Demo();
				x.setSize(1700, 700);
				x.setVisible(true);
				x.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
	}
	/**
	 * Render a group of paintings as a JPanel
	 */
	public class ImagePanel extends JPanel{
		int x_offset;
		int y_offset;
		int width;
		int height;
		LinkedList<Painting> paintings = new LinkedList<Painting>();
		
		public void setx(int x_offset){this.x_offset = x_offset;}
		public void sety(int y_offset){this.y_offset = y_offset;}
		public int getx_offset(int x_offset){return x_offset;}
		public int gety_offset(int y_offset){return y_offset;}
		/**
		 * Java wants this
		 */
		private static final long serialVersionUID = 1L;
		private BufferedImage image;

		public ImagePanel(LinkedList<Painting> list) {    
			paintings = list;
			x_offset = 25;
			y_offset = 25;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int maxY = 0;
			for(Painting p: paintings)
			{
				
				File file = p.getFile();
				try {
					image = ImageIO.read(file);
					
				} catch (IOException e) {
					System.out.println("Invalid image file given");
				}
				g.drawImage(image, x_offset, y_offset, this);
				x_offset = x_offset + image.getWidth() + 25;
				if(image.getHeight() > maxY){
					maxY = image.getHeight();
				}
				if (x_offset > 1800 - image.getWidth()){
					maxY = maxY + y_offset +25;
					y_offset = maxY;
					x_offset = 25;
				}
			}
		}

	}
	@SuppressWarnings("serial")
	public Demo() {
		this.setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				//**Many of these values are estimated, don't try to use this for your Art History homework
				Painting mondrian = new Painting(new File("./Paintings/mondrian.jpg"), "Composition with Red Blue and Yellow","Mondrian", 1930, 50600000);
				Painting monkey = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
				Painting missing = new Painting(new File("./Paintings/missing.jpg"), "Missing", "Steve", 2009, 0);
				Painting kirchner = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
				Painting rothko = new Painting(new File("./Paintings/rothko.jpg"), "Red on Red", "Rothko", 1969, 8237000);
				Painting matisse = new Painting(new File("./Paintings/matisse.jpg"), "The Cat with Red Fish","Matisse", 1906, 33500000);
				Painting courbet = new Painting(new File("./Paintings/courbet.jpg"), "The Desperate Man", "Courbet", 1845, 12000000);
				Painting hieronymus_bosch = new Painting(new File("./Paintings/hieronymus_bosch.jpg"), "The Harrowing of Hell", "Hieronymus Bosch", 1460, 137500);

				//try changing the comparator here
				//you should get different orderings when you run it!
				Painting.SortedCollection paintings = new Painting.SortedCollection(DateDescending.getInstance());
				paintings.add(rothko);
				paintings.add(courbet);
				paintings.add(hieronymus_bosch);
				paintings.add(kirchner);
				paintings.add(matisse);
				paintings.add(mondrian);
				paintings.add(monkey);
				paintings.add(missing);
				//what is this line doing?
				ImagePanel p = new ImagePanel(new LinkedList<Painting>(paintings));
				p.paintComponent(g);

				
			}
		});
	}
}
