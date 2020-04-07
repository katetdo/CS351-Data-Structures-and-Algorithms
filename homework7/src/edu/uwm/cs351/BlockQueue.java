package edu.uwm.cs351;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.uwm.cs351.util.Queue;

public class BlockQueue {
	private final JFrame frame;
	private final MyPanel panel = new MyPanel();
	private static Queue<Block> pending;
	private static Queue<Block> random;
	private static Queue<Block> fixed;
	private int supplyType;
	
	private static Block getNextBlock(Queue<Block> q) {
		Block k= q.dequeue();
		q.enqueue(k);
		return k;
	}

	public static void main(String[] args) {
		random = new Queue<Block>();
		for(Block b : Block.RBG_TABLE)
			random.enqueue(b);
		
		long seed = System.currentTimeMillis() % random.size();

		for (int i=0; i<seed; i++){
			getNextBlock(random);
		}
		

		fixed = new Queue<Block>();
		for (Block b : Block.values()){
			fixed.enqueue(b);
		}
		
		
		//initialize the pending queue with two blocks from the random queue
		//this is for the initial display
		pending = new Queue<Block>();
		
		//For Supply 7
		
		//For Supply 14
		
		//For Random
		pending.enqueue(random.dequeue());
		pending.enqueue(random.dequeue());
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				new BlockQueue();
			}
		});
	}
	
	private class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Block current = pending.dequeue();
		Block next = pending.front();
		JLabel print = new JLabel("Pending: " + pending.toString());
		
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			current.draw(g, current, 40, 20, 40);
			next.draw(g, next, 20, 30, 160);
			print.setText("Pending: " + pending.toString());
		}
	}
/**
 * Renders a Frame which shows a current and
 * upcoming Block within a queue
 * 
 * Supply types:
 * 
 * 0- We see blocks in the same repeating sequence: Z,S,L,J,O,I,T.
 * 
 * 1- Random piece generation. We supply pieces drawn uniformly at random from the set of blocks.
 * Any amount of time may pass before seeing the same piece
 * (this is standard in most classic Tetris games)
 * 
 * 2- 7 Seq randomizer. Blocks are supplied as a series of permutations of the 7 blocks.
 * This means that we see one of each block in some order before we get a new set
 * of the same blocks in a different order.
 * No more than a 12 pieces will pass before the same piece is seen again.
 * This would happen if that block is the first block in one permutation,
 * and then the last block in the next permutation.
 * (this is standard in most modern Tetris games)
 * 
 * 3- 14 Seq randomizer. We instead see two of each block before a new set begins.
 * This means we are supplying permutations of a collection of 14 blocks (2 of each).
 * Longer times between repeat pieces is possible.
 */
	public BlockQueue() {
		supplyType = 1; // fully random by default
		frame = new JFrame("Block Queue");		
		frame.setSize(500, 340);

		JButton advance = new JButton("Advance");
		advance.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(pending.size() <= 1) {  //if there is only one left in the pending queue
											// we need to supply more blocks to the pending queue
					switch(supplyType) {
					case 0: // set supply, giving one of each block in a fixed order
						pending=fixed.clone();
						
					break;
					
					
					case 1:// fully random
						pending.enqueue(getNextBlock(random));
					break;
					
					case 2:// 7-supply
						
						//TODO Get blocks from the random queue to add to pending
						// Each of the 7 unique blocks should be added once
						// If you get a block that has already been added, skip it
						
						//set up a boolean array?
						boolean [] arrBoo = new boolean [7];
						Queue<Block> setUp = new Queue<Block>();
						int trues=0;
						
						while (trues!=7){
							Block a = getNextBlock(random);
							if (arrBoo[a.ordinal()]==false){
								trues++;
								arrBoo[a.ordinal()]=true;
								setUp.enqueue(a);
							}
						}

						pending=setUp.clone();
						
						break;
						
					case 3:// 14-supply
						//TODO Get blocks from the random queue to add to pending
						// Each of the 7 unique blocks should be added twice
						// If you get a block that has already been added twice, skip it
						
						// set up an int array 
						int [] arrInts = new int [14];
						Queue<Block> setUp14 = new Queue<Block>();
						int t=0;
						
						while (t!=14){
							Block a = getNextBlock(random);
							if (arrInts[a.ordinal()]<2){
								t++;
								arrInts[a.ordinal()]++;
								setUp14.enqueue(a);
							}
						}

						pending=setUp14.clone();
						
						
					break;
					}
				}
				//TODO Remove a block from pending and store it in panel.current,
				// and panel.next should become the front-most block in pending.
				panel.current=pending.dequeue();
				panel.next=pending.front();
				panel.repaint();
			}
		});
		JButton inOrder = new JButton("Ordered");
		inOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 0;
			}
		});
		JButton random = new JButton("Random");
		random.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 1;
			}
		});
		JButton sup_7 = new JButton("7 Sup");
		sup_7.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 2;
			}
		});
		JButton sup_14 = new JButton("14 Sup");
		sup_14.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supplyType = 3;
			}
		});
		
		JLabel current = new JLabel("Current");
		JLabel next = new JLabel("Next");
		panel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(random);
		buttonPanel.add(inOrder);
		buttonPanel.add(sup_7);
		buttonPanel.add(sup_14);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(panel.print, BorderLayout.NORTH);
		southPanel.add(advance, BorderLayout.SOUTH);
		
		panel.add(buttonPanel, BorderLayout.EAST);
		panel.add(southPanel, BorderLayout.SOUTH);
		panel.add(current, BorderLayout.NORTH);
		panel.add(next, BorderLayout.CENTER);

		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}




}



