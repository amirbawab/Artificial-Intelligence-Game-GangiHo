package gui;

import game.Board;
import game.GangiHo;
import gui.listener.BoardListener;
import gui.listener.StartListener;
import gui.panels.BoardPanel;
import gui.panels.PathPanel;
import gui.panels.ProfilesPanel;
import gui.panels.StartPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class GameFrame extends JFrame{
	
	private BoardPanel bpanel;
	private StartPanel spanel;
	private ProfilesPanel ppanel;
	private PathPanel papanel;
	private GangiHo gangiHo;
	private final String NOW_PLAYING = "Now playing...";
	private long time;
	private final int PATH_VISIBLE_ON = 1200;
	private boolean debugMode;
	public static int INVALID_MOVE = -1;
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem restartMenu;
	private JMenuItem exitMenu;
	private JMenu debugMenu;
	private JCheckBoxMenuItem chosenPathMenu;
	private JMenuItem memoMenu;
	
	public GameFrame() {
		
		// Set title
		super("Gangi-Ho");
		
		// Set layout
		setLayout(new BorderLayout());
		
		// Create panels
		bpanel = new BoardPanel();
		spanel = new StartPanel();
		ppanel = new ProfilesPanel();
		papanel = new PathPanel();
		
		// By default ppanel and papanel are not visible
		ppanel.setVisible(false);
		papanel.setVisible(false);
		
		// By default lock board
		bpanel.lock(true);
		
		// By default debug mode is off
		debugMode = false;
		
		// Set start listener
		spanel.setStartListener(new StartListener() {
			public void startAction(String p1Name, int p1Type, int p1Token, String p2Name, int p2Type, int p2Token, int sizeBoard) {
				
				// Configure window
				spanel.setVisible(false);
				GameFrame.this.add(bpanel, BorderLayout.CENTER);
				ppanel.setProfilesInfo(p1Name, p1Type, p1Token, p2Name, p2Type, p2Token);
				ppanel.setVisible(true);
				
				// Re validate and repaint
				GameFrame.this.validate();
				GameFrame.this.repaint();
				
				// Create game
				Board.MAX_SIZE = sizeBoard;
				gangiHo = new GangiHo(p1Type, p2Type, p1Token);
				
				// Build board
				bpanel.buildBoard();
				
				// Start time
				time = System.currentTimeMillis();
				
				// If both players are human
				if(p1Type == GangiHo.HUMAN && p2Type == GangiHo.HUMAN){
					chosenPathMenu.setEnabled(false);
					chosenPathMenu.setSelected(false);
					debugMode = false;
				
				// If at least one player is an AI
				} else {
					if(debugMode && GameFrame.this.getWidth() > PATH_VISIBLE_ON){
						papanel.setVisible(true);
					}
				}
				
				// If both player are AI
				if(p1Type == GangiHo.AI && p2Type == GangiHo.AI){
					
					// Lock the board
					bpanel.lock(true);
					
					// Play the game in another thread so that the GUI doesn't freezes
					new Thread(){
						public void run() {
							
							// Keep playing until an AI wins
							while(!gangiHo.gameOver()){
								
								// AI plays
								AIMove();
								
								// Switch turn
								String msg = "e(n)= " + gangiHo.getHeuristic();
								if(gangiHo.getHeuristic() == Integer.MAX_VALUE)
									msg = "Guaranteed to win";
								else if(gangiHo.getHeuristic() == Integer.MIN_VALUE)
									msg = "High risk of loss";
								else if(gangiHo.getHeuristic() == Integer.MAX_VALUE-1)
									msg = "Winner or Tie";
								
								msg += "\n" + getTimeDiff();
								
								if(gangiHo.getTurn() == gangiHo.getP1Token()){
									ppanel.p1Turn(NOW_PLAYING, msg);
								} else {
									ppanel.p2Turn(msg, NOW_PLAYING);
								}
							}
							
							if(gangiHo.isTie())
								ppanel.tie();
							else if(gangiHo.getTurn() == gangiHo.getP1Token())
								ppanel.p2Wins();
							else
								ppanel.p1Wins();
						};
					}.start();
					
				// If p1 is AI
				} else if(p1Type == GangiHo.AI){
					
					// Lock board
					bpanel.lock(true);
					
					// Play on another thread
					new Thread(){
						public void run() {
							
							// AI plays
							AIMove();
							
							// Switch turn to p2
							String msg = "e(n)= " + gangiHo.getHeuristic();
							msg += "\n" + getTimeDiff();
							ppanel.p2Turn(msg, NOW_PLAYING);
							
							// Unlock board
							bpanel.lock(false);
						}
					}.start();
					
				} else {
					
					// Unlock board if p1 is human
					bpanel.lock(false);
				}
			}
		});
		
		// Set board listener
		bpanel.setBoardListener(new BoardListener() {
			
			/**
			 * When placing tokens using the mouse
			 */
			public int pressAction(int row, int col) {
				
				// Get turn
				int turn = gangiHo.getTurn();
				
				// Checks if a point at the end of the board is pressed
				if(turn == GangiHo.WHITE){
					if(row == Board.MAX_SIZE-1)
						row--;
				} else if(turn == GangiHo.BLACK) {
					if(col == Board.MAX_SIZE-1)
						col--;
				}
				
				// Checks if can be placed
				if(gangiHo.canPlace(row, col)){
					gangiHo.place(row, col);
					
					// Switch turn
					if(gangiHo.getTurn() == gangiHo.getP1Token()){
						ppanel.p1Turn(NOW_PLAYING,getTimeDiff());
					} else {
						ppanel.p2Turn(getTimeDiff(),NOW_PLAYING);
					}
					
					// Check if game is over
					if(gangiHo.gameOver()){
						
						// Lock board
						bpanel.lock(true);
						
						if(gangiHo.isTie())
							ppanel.tie();
						else if(gangiHo.getTurn() == gangiHo.getP1Token()){
							ppanel.p2Wins();
						} else {
							ppanel.p1Wins();
						}
						return turn;
					}
					
					// If any of the other players is an AI
					if(gangiHo.getP1Type() == GangiHo.AI || gangiHo.getP2Type() == GangiHo.AI){
						
						// Lock board
						bpanel.lock(true);
						
						// Let AI play on another thread
						new Thread(){
							public void run() {
								
								// AI move
								AIMove();
								
								// Switch turn
								String msg = "e(n)= " + gangiHo.getHeuristic();
								if(gangiHo.getHeuristic() == Integer.MAX_VALUE)
									msg = "Guaranteed to win";
								else if(gangiHo.getHeuristic() == Integer.MIN_VALUE)
									msg = "High risk of loss";
								else if(gangiHo.getHeuristic() == Integer.MAX_VALUE-1)
									msg = "Winner or Tie";
								
								msg+= "\n" + getTimeDiff();
								
								if(gangiHo.getTurn() == gangiHo.getP1Token()){
									ppanel.p1Turn(NOW_PLAYING, msg);
								} else {
									ppanel.p2Turn(msg,NOW_PLAYING);
								}
								
								// Unlock board
								bpanel.lock(false);
								
								// Check if game is over
								if(gangiHo.gameOver()){
									
									// Lock board
									bpanel.lock(true);
									
									if(gangiHo.isTie())
										ppanel.tie();
									else if(gangiHo.getTurn() == gangiHo.getP1Token()){
										ppanel.p2Wins();
									} else {
										ppanel.p1Wins();
									}
								}
							};
						}.start();;
					}
					return turn;
				}
				
				return INVALID_MOVE;
			}
			
			public int overAction() {
				return gangiHo.getTurn();
			}
		});
		
		// Set Window listener
		addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				
				// If game created && is debug mode && at least of the players is AI
				if(gangiHo != null && debugMode && (gangiHo.getP1Type() == GangiHo.AI || gangiHo.getP2Type() == GangiHo.AI)){
					
					// If window is big enough
					if(getWidth() > PATH_VISIBLE_ON){
						papanel.setVisible(true);
					}else{
						papanel.setVisible(false);
					}
				}
			}
		});
		
		// Add menu bar
		menuBar = new JMenuBar();
		
		// File
		fileMenu = new JMenu("File");
		exitMenu = new JMenuItem("Exit");
		restartMenu = new JMenuItem("Restart");
		
		// Add exit listener
		exitMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		// Add restart listener
		restartMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				gangiHo = null;
				bpanel = null;
				ppanel = null;
				papanel = null;
				System.gc();
				new GameFrame();
			}
		});
		
		// Debug
		debugMenu = new JMenu("Debug");
		chosenPathMenu = new JCheckBoxMenuItem("Chosen path (Slower performance)");
		memoMenu = new JMenuItem("Memory information");
		
		// Add chosen path listener
		chosenPathMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chosenPathMenu.isSelected()){
					debugMode = true;
					
					// If game created && at least of the players is AI && Window size is big enough
					if(gangiHo != null && (gangiHo.getP1Type() == GangiHo.AI || gangiHo.getP2Type() == GangiHo.AI) && getWidth() > PATH_VISIBLE_ON){
						papanel.setVisible(true);
					}
					
				} else {
					debugMode = false;
					papanel.setVisible(false);
				}
			}
		});
		
		// Add memory menu listener
		memoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Set to MB
				int mb = 1024*1024;
		        
		        // Getting the runtime reference from system
		        Runtime runtime = Runtime.getRuntime();
		        
		        String output = "";
		        output += "Heap utilization";
		        output += "\n\nUsed Memory:\t" + (runtime.totalMemory() - runtime.freeMemory()) / mb + " MB";
		        output += "\nFree Memory:\t" + runtime.freeMemory() / mb + " MB";
		        output += "\nTotal Memory:\t" + runtime.totalMemory() / mb + " MB";
		        output += "\nMax Memory:\t" + runtime.maxMemory() / mb + " MB";
		        
		        // Create textarea and its style
		        JTextArea textArea = new JTextArea(output);
		        textArea.setBackground(null);
		        textArea.setEditable(false);
		        textArea.setFocusable(false);
		        
		        // Show option panel
		        JOptionPane.showMessageDialog(GameFrame.this, textArea, "Memory information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// Add sub items
		fileMenu.add(restartMenu);
		fileMenu.add(exitMenu);
		debugMenu.add(chosenPathMenu);
		debugMenu.add(memoMenu);
		
		// Add menu
		menuBar.add(fileMenu);
		menuBar.add(debugMenu);
		
		// Add menu bar
		setJMenuBar(menuBar);
		
		// Add panels
		add(spanel, BorderLayout.CENTER);
		add(ppanel, BorderLayout.WEST);
		add(papanel, BorderLayout.EAST);
		
		// Frame preference
		setVisible(true);
		Dimension dim = new Dimension(980,820);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
	}
	
	/**
	 * Make AI Move and repaint board
	 */
	public void AIMove(){
		gangiHo.getNextMove();
		Board newBoard = gangiHo.getGraph().games[gangiHo.getChosenChild()].getBoard();
		gangiHo.setBoard(newBoard);
		bpanel.rebuild(newBoard);
		
		// If debug mode
		if(debugMode){
			
			// Get full path
			String output = "Total games generated: " + gangiHo.getGraph().size();
			int child = gangiHo.getChosenChild();
			while(child != -1){
				output += "\n\nChosen childID: " + child;
				output += "\ne(n)=" + gangiHo.getGraph().games[child].getHeuristic();
				output += "\nIs leaf: " + (gangiHo.getGraph().degree[child] == 0);
				output += "\nMAX token: " + (3 - gangiHo.getTurn() == GangiHo.WHITE ? "WHITE" : "BLACK");
				output += "\nNext turn: " + (gangiHo.getGraph().games[child].getTurn() == GangiHo.WHITE ? "WHITE" : "BLACK");
				output += "\n"+ gangiHo.getGraph().games[child];
				
				// Update child
				child = gangiHo.getGraph().games[child].getChosenChild();
				
				if(child != -1)
					output += "\n---------------------";
			}
			
			// Update path panel
			papanel.setPahtText(output);
		}	
	}
	
	/**
	 * Get time difference
	 * @return time difference
	 */
	public String getTimeDiff(){
		String timeDiff =  (float)(System.currentTimeMillis() - time)/1000 + " sec";
		time = System.currentTimeMillis();
		return timeDiff;
	}
}
