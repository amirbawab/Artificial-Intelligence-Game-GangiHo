package gui.panels;

import game.GangiHo;
import gui.listener.StartListener;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class StartPanel extends JPanel{
	
	private GridBagConstraints gc;
	private JPanel p1panel;
	private JPanel p2panel;
	private JLabel p1AIBtn;
	private JLabel p2AIBtn;
	private JLabel p1HumanBtn;
	private JLabel p2HumanBtn;
	private JLabel p1WhiteBtn;
	private JLabel p2WhiteBtn;
	private JLabel p1BlackBtn;
	private JLabel p2BlackBtn;
	private JTextField p1Name;
	private JTextField p2Name;
	private JButton p1Ready;
	private JButton p2Ready;
	private JLabel p1ReadyState;
	private JLabel p2ReadyState;
	private int p1Type, p2Type, p1Token, p2Token;
	private boolean p1IsReady, p2IsReady;
	private StartListener startListener;
	private JLabel upBtn, downBtn, sizeText;
	
	/**
	 * Constructor
	 */
	public StartPanel() {
		
		// Create JPanel
		p1panel = new JPanel();
		p2panel = new JPanel();
		
		// Set background
		setBackground(Color.DARK_GRAY);
		p1panel.setBackground(Color.GRAY);
		p2panel.setBackground(Color.GRAY);
		
		// Set layout
		setLayout(new GridBagLayout());
		
		// Set Layout
		p1panel.setLayout(new GridBagLayout());
		p2panel.setLayout(new GridBagLayout());
		
		// Create buttons
		p1AIBtn = new JLabel();
		p2AIBtn = new JLabel();
		p1HumanBtn = new JLabel();
		p2HumanBtn = new JLabel();
		p1WhiteBtn = new JLabel();
		p2WhiteBtn = new JLabel();
		p1BlackBtn = new JLabel();
		p2BlackBtn = new JLabel();
		p1Name = new JTextField("Player I", 10);
		p2Name = new JTextField("Player II", 10);
		p1Ready = new JButton("Player I Ready!");
		p2Ready = new JButton("Player II Ready!");
		p1ReadyState = new JLabel();
		p2ReadyState = new JLabel();
		
		// Set color
		p1ReadyState.setForeground(Color.WHITE);
		p2ReadyState.setForeground(Color.WHITE);
		
		// Human button style
		p1HumanBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/human.png")));
		p2HumanBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/human.png")));
		
		// AI button style
		p1AIBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/ai.png")));
		p2AIBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/ai.png")));
		
		// White token button style
		p1WhiteBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/white_token.png")));
		p2WhiteBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/white_token.png")));
		
		// Black token button style
		p1BlackBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/black_token.png")));
		p2BlackBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("gui/images/black_token.png")));
		
		// Default configuration
//		p1Type = GangiHo.HUMAN;
//		p2Type = GangiHo.HUMAN;
//		p1Token = GangiHo.WHITE;
//		p2Token = GangiHo.BLACK;
//		p1BlackBtn.setEnabled(false);
//		p2WhiteBtn.setEnabled(false);
//		p1AIBtn.setEnabled(false);
//		p2AIBtn.setEnabled(false);
		
		// Add p1 AI button listener
		p1HumanBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p1HumanBtn.setEnabled(true);
				p1AIBtn.setEnabled(false);
				p1Type = GangiHo.HUMAN;
			}
		});
		
		// Add p1 AI button listener
		p2HumanBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p2HumanBtn.setEnabled(true);
				p2AIBtn.setEnabled(false);
				p2Type = GangiHo.HUMAN;
			}
		});
		
		// Add p1 AI button listener
		p1AIBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p1AIBtn.setEnabled(true);
				p1HumanBtn.setEnabled(false);
				p1Type = GangiHo.AI;
			}
		});
		
		// Add p1 AI button listener
		p2AIBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p2AIBtn.setEnabled(true);
				p2HumanBtn.setEnabled(false);
				p2Type = GangiHo.AI;
			}
		});
		
		// Add p1 White token button listener
		p1WhiteBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p1WhiteBtn.setEnabled(true);
				p2WhiteBtn.setEnabled(false);
				p1BlackBtn.setEnabled(false);
				p2BlackBtn.setEnabled(true);
				p1Token = GangiHo.WHITE;
				p2Token = GangiHo.BLACK;
			}
		});
		
		// Add p2 White token button listener
		p2WhiteBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p2WhiteBtn.setEnabled(true);
				p1WhiteBtn.setEnabled(false);
				p2BlackBtn.setEnabled(false);
				p1BlackBtn.setEnabled(true);
				p1Token = GangiHo.BLACK;
				p2Token = GangiHo.WHITE;
			}
		});
		
		// Add p1 Black token button listener
		p1BlackBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p1BlackBtn.setEnabled(true);
				p1WhiteBtn.setEnabled(false);
				p2BlackBtn.setEnabled(false);
				p2WhiteBtn.setEnabled(true);
				p1Token = GangiHo.BLACK;
				p2Token = GangiHo.WHITE;
			}
		});
		
		// Add p2 Black token button listener
		p2BlackBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				p2BlackBtn.setEnabled(true);
				p2WhiteBtn.setEnabled(false);
				p1BlackBtn.setEnabled(false);
				p1WhiteBtn.setEnabled(true);
				p1Token = GangiHo.WHITE;
				p2Token = GangiHo.BLACK;
			}
		});
		
		// Add p1 ready
		p1Ready.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(p1Type == 0){
					p1ReadyState.setText("Type is required!");
				
				} else if(p1Token == 0){
					p1ReadyState.setText("Token is required!");
				
				} else if(p1Name.getText().isEmpty()){
					p1ReadyState.setText("Name is required!");
					
				} else {
					p1ReadyState.setText("Player I is ready!");
					p1Name.setEditable(false);
					p1IsReady = true;
					
					// Start game if both ready
					if(p1IsReady && p2IsReady)
						startListener.startAction(p1Name.getText(), p1Type, p1Token, p2Name.getText(), p2Type, p2Token, Integer.parseInt(sizeText.getText()));
				}
			}
		});
		
		// Add p2 ready
		p2Ready.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(p2Type == 0){
					p2ReadyState.setText("Type is required!");
				
				} else if(p2Token == 0){
					p2ReadyState.setText("Token is required!");
				
				} else if(p2Name.getText().isEmpty()){
					p2ReadyState.setText("Name is required!");
					
				} else {
					p2ReadyState.setText("Player II is ready!");
					p2Name.setEditable(false);
					p2IsReady = true;
					
					// Start game if both ready
					if(p1IsReady && p2IsReady)
						startListener.startAction(p1Name.getText(), p1Type, p1Token, p2Name.getText(), p2Type, p2Token, Integer.parseInt(sizeText.getText()));
				}
			}
		});
		
		// Set ready button
		p1Ready.setBackground(Color.WHITE);
		p2Ready.setBackground(Color.WHITE);
		
		// Default grid configuration
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = GridBagConstraints.NORTH;
		gc.weightx = 1;
		gc.weighty = 1;
		
		// Add titles
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		TitledBorder titledBorder = BorderFactory.createTitledBorder("Player I");
		titledBorder.setTitleColor(Color.WHITE);
		p1panel.setBorder(BorderFactory.createCompoundBorder(emptyBorder, titledBorder));
		titledBorder = BorderFactory.createTitledBorder("Player II");
		titledBorder.setTitleColor(Color.WHITE);
		p2panel.setBorder(BorderFactory.createCompoundBorder(emptyBorder, titledBorder));
		
		// Add names label
		gc.gridx = 0;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.EAST;
		p1panel.add(createWhiteLable("Name            "), gc);
		p2panel.add(createWhiteLable("Name            "), gc);
		
		// Add names
		gc.gridx = 1;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.WEST;
		p1panel.add(p1Name, gc);
		p2panel.add(p2Name, gc);
		
		// Add human buttons titles
		gc.gridx = 0;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.CENTER;
		p1panel.add(createWhiteLable("Human"), gc);
		p2panel.add(createWhiteLable("Human"), gc);
		
		// Add human buttons titles
		gc.gridx = 1;
		gc.gridy = 2;
		p1panel.add(createWhiteLable("AI"), gc);
		p2panel.add(createWhiteLable("AI"), gc);
		
		// Add human buttons
		gc.gridx = 0;
		gc.gridy = 3;
		p1panel.add(p1HumanBtn, gc);
		p2panel.add(p2HumanBtn, gc);
		
		// Add AI buttons
		gc.gridx = 1;
		gc.gridy = 3;
		p1panel.add(p1AIBtn, gc);
		p2panel.add(p2AIBtn, gc);
		
		// Add token label
		gc.anchor = GridBagConstraints.NORTH;
		gc.gridx = 0;
		gc.gridy = 4;
		p1panel.add(createWhiteLable("White"), gc);
		p2panel.add(createWhiteLable("White"), gc);		
		// Add token
		gc.gridx = 1;
		gc.gridy = 4;
		p1panel.add(createWhiteLable("Black"), gc);
		p2panel.add(createWhiteLable("Black"), gc);
		
		// Add token label
		gc.gridx = 0;
		gc.gridy = 5;
		p1panel.add(p1WhiteBtn, gc);
		p2panel.add(p2WhiteBtn, gc);
		
		// Add token
		gc.gridx = 1;
		gc.gridy = 5;
		p1panel.add(p1BlackBtn, gc);
		p2panel.add(p2BlackBtn, gc);
		
		// Add ready
		gc.gridx = 0;
		gc.gridy = 6;
		p1panel.add(p1Ready, gc);
		p2panel.add(p2Ready, gc);
		
		// Add ready
		gc.gridx = 1;
		gc.gridy = 6;
		p1panel.add(p1ReadyState, gc);
		p2panel.add(p2ReadyState, gc);
		
		// Add logo
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridwidth = 3;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/logo.png"))), gc);
		
		// Add p1 panel
		gc.anchor = GridBagConstraints.NORTHEAST;
		gc.gridwidth = 1;
		gc.weighty = 2;
		gc.weightx = 2;
		gc.gridx = 0;
		gc.gridy = 1;
		add(p1panel, gc);
		
		// Add board size
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new GridBagLayout());
		sizePanel.setOpaque(true);
		sizePanel.setBackground(null);
		
		gc.gridy = 1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.NORTH;
		upBtn = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/up.png")));
		sizePanel.add(upBtn,gc);
		
		gc.gridy = 2;
		sizeText = new JLabel("8");
		sizeText.setFont(new Font(sizeText.getFont().getName(), Font.BOLD, 25));
		sizeText.setForeground(Color.WHITE);
		sizePanel.add(sizeText, gc);
		
		gc.gridy = 3;
		downBtn = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/down.png")));
		sizePanel.add(downBtn,gc);
		
		gc.gridy = 4;
		sizePanel.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gui/images/vs.png"))),gc);
		
		gc.gridy = 1;
		gc.gridx = 1;
		add(sizePanel,gc);
		
		// Add board size listener
		upBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseClicked(MouseEvent e) {
				
				// Max 16
				if(Integer.parseInt(sizeText.getText()) == 16) 
					return;
				
				sizeText.setText(Integer.parseInt(sizeText.getText()) + 1 + "");
			}
		});
		
		downBtn.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseClicked(MouseEvent e) {
				
				// Min 6
				if(Integer.parseInt(sizeText.getText()) == 6) 
					return;
				
				sizeText.setText(Integer.parseInt(sizeText.getText()) - 1 + "");
			}
		});
		
		// Add p2 panel
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.weightx = 2;
		gc.gridx = 2;
		gc.gridy = 1;
		add(p2panel, gc);
	}
	
	/**
	 * Set Start Listener
	 * @param startListener
	 */
	public void setStartListener(StartListener startListener) {
		this.startListener = startListener;
	}
	
	/**
	 * Create a white text for JLabel
	 * @param str
	 * @return label
	 */
	public JLabel createWhiteLable(String str){
		JLabel tmp = new JLabel(str);
		tmp.setForeground(Color.WHITE);
		return tmp;
	}
}
