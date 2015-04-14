package gui.panels;

import game.GangiHo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ProfilesPanel extends JPanel{
	
	private GridBagConstraints gc;
	private JPanel p1panel;
	private JPanel p2panel;
	private JLabel p1Name;
	private JLabel p2Name;
	private JLabel p1TypePic, p2TypePic, p1TokenPic, p2TokenPic, p1TrophyPic, p2TrophyPic, totalMoves;
	private JTextPane p1Status, p2Status;
	private Color GREEN = Color.decode("#4cab4e");
	private Color RED = Color.decode("#ab4c4c");
	private Color ORANGE = Color.decode("#FFA500");
	
	/**
	 * Constructor
	 */
	public ProfilesPanel() {
		
		// Create JPanel
		p1panel = new JPanel();
		p2panel = new JPanel();
		
		// Set background
		setBackground(Color.DARK_GRAY);
		p1panel.setBackground(Color.GRAY);
		p2panel.setBackground(Color.GRAY);
		
		// Set layout
		setLayout(new GridBagLayout());
		
		// Set empty border
		setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
				
		// Set Layout
		p1panel.setLayout(new GridBagLayout());
		p2panel.setLayout(new GridBagLayout());
		
		// Set size
		Dimension dim = new Dimension(200,200);
		p1panel.setPreferredSize(dim);
		p2panel.setPreferredSize(dim);
		
		// Create labels
		p1Name = new JLabel();
		p2Name = new JLabel();
		p1TypePic = new JLabel();
		p2TypePic = new JLabel();
		p1TokenPic = new JLabel();
		p2TokenPic = new JLabel();;
		p1TrophyPic = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/trophy.png")));
		p2TrophyPic = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/trophy.png")));
		p1Status = new JTextPane();
		p2Status = new JTextPane();
		totalMoves = new JLabel("0");
		
		// Configure status style
		Dimension statusDim = new Dimension((int) dim.getWidth(), 40);
		p1Status.setBackground(GREEN);
		p1Status.setPreferredSize(statusDim);
		p1Status.setOpaque(true);
		p2Status.setBackground(RED);
		p2Status.setPreferredSize(statusDim);
		p2Status.setOpaque(true);
		p1Status.setForeground(Color.WHITE);
		p2Status.setForeground(Color.WHITE);
		p1Status.setText("Now playing...");
		p1Status.setEditable(false);
		p2Status.setEditable(false);
		Font boldFont = new Font(p1Status.getFont().getName(), Font.BOLD, p1Status.getFont().getSize());
		p1Status.setFont(boldFont);
		p2Status.setFont(boldFont);
		p1Status.setFocusable(false);
		p2Status.setFocusable(false);
		
		// Center text in status
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyledDocument doc = p1Status.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		doc = p2Status.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		// Configure total moves style
		totalMoves.setForeground(Color.white);
		totalMoves.setFont(new Font(totalMoves.getFont().getName(), Font.BOLD, 40));;
		
		// No winners by default
		p1TrophyPic.setVisible(false);
		p2TrophyPic.setVisible(false);
		
		// Set token size
		Dimension tokenDim = new Dimension(50,50);
		p1TokenPic.setPreferredSize(tokenDim);
		p2TokenPic.setPreferredSize(tokenDim);
		
		// Default grid configuration
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.NORTH;
		gc.weightx = 1;
		gc.weighty = 1;
		
		// Add titles
		TitledBorder titledBorder = BorderFactory.createTitledBorder("Player I");
		titledBorder.setTitleColor(Color.WHITE);
		p1panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), titledBorder));
		titledBorder = BorderFactory.createTitledBorder("Player II");
		titledBorder.setTitleColor(Color.WHITE);
		p2panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), titledBorder));
		
		// Add tokens pics
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.SOUTHEAST;
		p1panel.add(p1TokenPic, gc);
		p2panel.add(p2TokenPic, gc);
		
		// Add tokens pics
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.SOUTHWEST;
		p1panel.add(p1TrophyPic, gc);
		p2panel.add(p2TrophyPic, gc);
		
		// Add type pics
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.CENTER;
		p1panel.add(p1TypePic, gc);
		p2panel.add(p2TypePic, gc);
		
		// Add names
		gc.gridx = 0;
		gc.gridy = 1;
		p1panel.add(p1Name, gc);
		p2panel.add(p2Name, gc);
		
		// Add p1 panel
		gc.anchor = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 0;
		add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/logo_small.png"))), gc);
		
		// Add p1 panel
		gc.anchor = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 1;
		add(p1panel, gc);
		
		// Add p1 status
		gc.anchor = GridBagConstraints.NORTH;
		gc.gridx = 0;
		gc.gridy = 2;
		add(p1Status, gc);
		
		// Add total moves
		gc.anchor = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 3;
		JLabel totalMovesLabel = new JLabel("Total moves");
		totalMovesLabel.setForeground(Color.white);
		add(totalMovesLabel, gc);
		
		// Add total moves
		gc.anchor = GridBagConstraints.NORTH;
		gc.gridx = 0;
		gc.gridy = 4;
		add(totalMoves, gc);
		
		// Add p1 status
		gc.anchor = GridBagConstraints.SOUTH;
		gc.gridx = 0;
		gc.gridy = 5;
		add(p2Status, gc);
		
		// Add p2 panel
		gc.anchor = GridBagConstraints.NORTH;
		gc.gridx = 0;
		gc.gridy = 6;
		add(p2panel, gc);
		
	}
	
	/**
	 * Set Player I & II profiles
	 * @param p1Name
	 * @param p1Type
	 * @param p1Token
	 * @param p2Name
	 * @param p2Type
	 * @param p2Token
	 */
	public void setProfilesInfo(String p1Name, int p1Type, int p1Token, String p2Name, int p2Type, int p2Token){
		this.p1Name.setText(p1Name);
		this.p2Name.setText(p2Name);
		this.p1Name.setForeground(Color.WHITE);
		this.p2Name.setForeground(Color.WHITE);
		
		if(p1Token == GangiHo.BLACK)
			p1TokenPic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/black_token_small.png")));
		else
			p1TokenPic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/white_token_small.png")));
		
		if(p2Token == GangiHo.BLACK)
			p2TokenPic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/black_token_small.png")));
		else
			p2TokenPic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/white_token_small.png")));
		
		if(p1Type == GangiHo.AI)
			p1TypePic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/ai.png")));
		else
			p1TypePic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/human.png")));
		
		if(p2Type == GangiHo.AI)
			p2TypePic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/ai.png")));
		else
			p2TypePic.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/human.png")));
	}
	
	/**
	 * Get p1 name
	 * @return p1 name
	 */
	public String getP1Name(){
		return p1Name.getText();
	}
	
	/**
	 * Get p2 name
	 * @return p2 name
	 */
	public String getP2Name(){
		return p2Name.getText();
	}
	
	/**
	 * Set p1 as winner
	 */
	public void p1Wins(){
		p1TrophyPic.setVisible(true);
		p1Turn("Winner", "Looser");
		
		// Adjust moves
		int moves = Integer.parseInt(totalMoves.getText());
		totalMoves.setText(moves - 1 + "");
	}
	
	/**
	 * Set p2 as winner
	 */
	public void p2Wins(){
		p2TrophyPic.setVisible(true);
		p2Turn("Looser", "Winner");
		
		// Adjust moves
		int moves = Integer.parseInt(totalMoves.getText());
		totalMoves.setText(moves - 1 + "");
	}
	
	/**
	 * Set turn for p1
	 * @param str
	 */
	public void p1Turn(String str1, String str2){
		p1Status.setBackground(GREEN);
		p2Status.setBackground(RED);
		p1Status.setText(str1);
		p2Status.setText(str2);
		
		// Update total moves
		int moves = Integer.parseInt(totalMoves.getText());
		totalMoves.setText(moves + 1 + "");
	}
	
	/**
	 * Set turn for p2
	 * @param str
	 */
	public void p2Turn(String str1, String str2){
		p2Status.setBackground(GREEN);
		p1Status.setBackground(RED);
		p2Status.setText(str2);
		p1Status.setText(str1);
		
		// Update total moves
		int moves = Integer.parseInt(totalMoves.getText());
		totalMoves.setText(moves + 1 + "");
	}
	
	/**
	 * Tie
	 */
	public void tie(){
		p1Status.setBackground(ORANGE);
		p2Status.setBackground(ORANGE);
		p2Status.setText("Tie - No winner");
		p1Status.setText("Tie - No winner");
	}
}
