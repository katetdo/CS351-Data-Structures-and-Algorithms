package edu.uwm.cs351;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SocialApp extends JFrame {


	ProfileMap f = new ProfileMap();
	ArrayList<Profile> allProfiles;
	ProfilePanel profile;
	Profile p1 = new Profile("iron_man1010");
	Profile p2 = new Profile("wong_guy88");
	Profile p3 = new Profile("cool_surgeon");
	Profile p4 = new Profile("brucegoose");
	Profile p5 = new Profile("captain");

	//eclipse wants this
	private static final long serialVersionUID = 1L;

	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 600;


	public SocialApp() {
		
		createProfiles();

		allProfiles = getProfiles();
		//null parameter will use a list of all profiles
		//profile = new ProfilePanel(null);
		profile = new ProfilePanel(p3);

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		ImageIcon i = new ImageIcon("./ProfilePictures/social.jpg");
		setIconImage(i.getImage());
		add(profile);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Social"); 
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@SuppressWarnings("serial")
	public class ProfilePanel extends JPanel{

		public ProfilePanel(Profile p) {
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			if(p == null) {
				add(new ProfilePicturePanel(new File("./ProfilePictures/missing.jpg")));
				FriendListPanel friends = new FriendListPanel(null);
				add(friends);
				return;
			}
			add(new ProfilePicturePanel(p.getProfilePicture()));

			FriendListPanel friends = new FriendListPanel(p);
			friends.setLayout(new FlowLayout());

			add(friends);
		}
	}

	@SuppressWarnings("serial")
	public class FriendListPanel extends JPanel{

		public FriendListPanel(Profile p) {
			setLayout(new GridLayout());
			if(p == null) {
				JLabel name = new JLabel("All Users:");
				name.setFont(new Font("Helvetica", Font.BOLD, 100));
				add(name);
				for(Profile pr: f.getAll()) {
					add(new FriendPanel(pr));
				}
				return;
			}
			JLabel name = new JLabel(p.getNickname() + "'s Friends List");
			name.setFont(new Font("Helvetica", Font.BOLD, 25));
			add(name);
			if(f.find(p)!=null)
				for(Profile f: f.find(p))
					add(new FriendPanel(f));


		}
	}

	@SuppressWarnings("serial")
	public class FriendPanel extends JPanel{
		Profile p;

		public FriendPanel(Profile p) {
			this.p = p;
			JLabel profileName = new JLabel(p.toString());
			setLayout(new FlowLayout());
			JButton link = new JButton("View");
			link.addActionListener(new LinkToProfile());
			add(link);
			add(profileName);
		}
		class LinkToProfile implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				displayProfile(p);
			}
		}
	}

	/**
	 * Render a Profile Picture as a JPanel
	 */
	public class ProfilePicturePanel extends JPanel{
		File imageFile;

		/**
		 * Java wants this
		 */
		private static final long serialVersionUID = 1L;

		public ProfilePicturePanel(File file) {  

			this.imageFile = file;
			BufferedImage myPicture = null;
			try {
				myPicture = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageIcon i = new ImageIcon(myPicture);
			JLabel picLabel = new JLabel(i);
			picLabel.setLocation(0, 0);
			add(picLabel);
			setPreferredSize(new Dimension(300,0));
		}
	}

	public void displayProfile(Profile p) {

		this.remove(profile);
		profile = new ProfilePanel(p);
		this.add(profile);


		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Social"); 
		setLocationRelativeTo(null);
		setVisible(true);

	}
	private ArrayList<Profile> getProfiles(){
		ArrayList<Profile> allProfiles = new ArrayList<Profile>();
		for(Profile p: f.getAll()) {
			if(!(allProfiles.contains(p))) {
				allProfiles.add(p);
			}
		}
		return allProfiles;
	}
	private void createProfiles() {
		p1.setNickname("Dr. Strange");
		p1.setProfilePicture(new File("./ProfilePictures/strange.jpg"));

		p2.setNickname("Wong");
		p2.setProfilePicture(new File("./ProfilePictures/wong.jpg"));

		p3.setNickname("Iron Man");
		p3.setProfilePicture(new File("./ProfilePictures/ironman.jpg"));

		p4.setNickname("Bruce Banner");
		p4.setProfilePicture(new File("./ProfilePictures/bruce.jpg"));

		p5.setNickname("Captain America");
		p5.setProfilePicture(new File("./ProfilePictures/cap.jpg"));
		
		f.add(p1);
		f.add(p2);
		f.add(p3);
		f.add(p4);
		f.add(p5);
		f.add(p1,p2);
		f.add(p1,p5);
		f.add(p2,p1);
		f.add(p2,p3);
		f.add(p3,p4);
		f.add(p3,p5);
		f.add(p4,p3);
		f.add(p4,p5);
		f.add(p5,p3);
		f.add(p5,p4);
		f.add(p5,p1);
		f.add(p5,p2);
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SocialApp();
			}
		});
	}
}

