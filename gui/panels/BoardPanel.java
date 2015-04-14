package gui.panels;

import game.Board;
import game.GangiHo;
import gui.GameFrame;
import gui.lib.BoardLabel;
import gui.listener.BoardListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BoardPanel extends JPanel{

	private GridBagConstraints gc;
	private BoardLabel[][] board;
	private JPanel gridPanel;
	private BoardListener boardListner;
	private boolean lock;
	
	public BoardPanel() {
		
		// Create panel
		gridPanel = new JPanel();

		// Set background
		setBackground(Color.DARK_GRAY);
		gridPanel.setBackground(Color.DARK_GRAY);
		
		// Set layout
		setLayout(new GridBagLayout());
		gridPanel.setLayout(new GridBagLayout());
	}
	
	/**
	 * Build board
	 */
	public void buildBoard(){
		
		// Decide the cells size
		int variable_size;
		if(Board.MAX_SIZE <= 6)
			variable_size = 100;
		
		else if(Board.MAX_SIZE <= 8)
			variable_size = 80;
		
		else if(Board.MAX_SIZE <= 10)
			variable_size = 65;
		
		else if(Board.MAX_SIZE <= 12)
			variable_size = 55;
		
		else if(Board.MAX_SIZE <= 14)
			variable_size = 45;
		
		else
			variable_size = 40;
		
		// Fixed dimension
		final Dimension DIM = new Dimension(variable_size,variable_size);
		final Dimension DIM_HEADERS_TOP = new Dimension(variable_size,50);
		final Dimension DIM_HEADERS_LEFT = new Dimension(50,variable_size);
		
		// Create JLabel board
		board = new  BoardLabel[Board.MAX_SIZE][Board.MAX_SIZE];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				
				// Create Label
				board[row][col] = new BoardLabel(row,col, variable_size);
				
				// Add border
				if(col == board.length-1 && row == board.length-1)
					board[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
				else if(col == board.length-1)
					board[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.LIGHT_GRAY));
				else if(row == board.length-1)
					board[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY));
				else 
					board[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.LIGHT_GRAY));
				
				// Add mouse listener
				final int rowF = row, colF = col;
				board[row][col].addMouseListener(new MouseListener() {
					
					public void mouseReleased(MouseEvent arg0) {}
					public void mouseClicked(MouseEvent arg0) {}
					
					/**
					 * Mouse pressed
					 */
					public void mousePressed(MouseEvent arg0) {
						
						// If locked board, return
						if(lock) return;
						
						// If board listener is not set, return
						if(boardListner == null) return;
						
						int turn = boardListner.pressAction(rowF,colF);
						
						// If black token
						if(turn == GangiHo.BLACK){
							if(colF == board.length-1)
								board[rowF][colF-1].placeBlackToken();
							else
								board[rowF][colF+1].placeBlackToken();
							board[rowF][colF].placeBlackToken();
						
						// If white token
						} else if(turn == GangiHo.WHITE) {
							if(rowF == board.length-1)
								board[rowF-1][colF].placeWhiteToken();
							else
								board[rowF+1][colF].placeWhiteToken();
							board[rowF][colF].placeWhiteToken();
						
						// If cannot be placed
						} else if(turn == GameFrame.INVALID_MOVE) {
							// Do something
						}
					}
					
					/**
					 * Mouse quit cell
					 */
					public void mouseExited(MouseEvent arg0) {
						
						// If locked board, return
						if(lock) return;
						
						// If board listener is not set, return
						if(boardListner == null) return;
						
						int turn = boardListner.overAction();
						
						// If black token
						if(turn == GangiHo.BLACK){
							if(colF == board.length-1)
								board[rowF][colF-1].removeToken();
							else
								board[rowF][colF+1].removeToken();
							
						// If white token
						} else {
							if(rowF == board.length-1)
								board[rowF-1][colF].removeToken();
							else 
								board[rowF+1][colF].removeToken();
						}
						board[rowF][colF].removeToken();
					}
					
					/**
					 * Mouse entered cell
					 */
					public void mouseEntered(MouseEvent arg0) {
						
						// If locked board, return
						if(lock) return;
						
						// If board listener is not set, return
						if(boardListner == null) return;
						
						int turn = boardListner.overAction();
						
						// If black token
						if(turn == GangiHo.BLACK){
							if(colF == board.length-1)
								board[rowF][colF-1].overBlackToken();
							else
								board[rowF][colF+1].overBlackToken();
							board[rowF][colF].overBlackToken();
						
						// If white token
						} else {
							if(rowF == board.length-1)
								board[rowF-1][colF].overWhiteToken();
							else
								board[rowF+1][colF].overWhiteToken();
							board[rowF][colF].overWhiteToken();
						}
					}
				});
			}
		}
		
		// Default grid configuration
		gc = new GridBagConstraints();
		gc.weightx = 1;
		gc.weighty = 1;
		
		// Add headers to the board
		gridPanel.add(new JLabel());
		for(int i=0; i<board.length; i++){
			JLabel label;
			gc.gridx = i+1;
			gc.gridy = 0;
			label = new JLabel( i+1 + "", SwingConstants.CENTER); 
			label.setPreferredSize(DIM_HEADERS_TOP);
			label.setOpaque(false);
			label.setForeground(Color.WHITE);
			gridPanel.add(label,gc);
			
			gc.gridx = 0;
			gc.gridy = i+1;
			label = new JLabel((char)('A' + i) + "", SwingConstants.CENTER); 
			label.setPreferredSize(DIM_HEADERS_LEFT);
			label.setOpaque(false);
			label.setForeground(Color.WHITE);
			gridPanel.add(label,gc);
		}
		
		// Add components to grid panel
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				gc.gridx = col+1;
				gc.gridy = row+1;
				gridPanel.add(board[row][col],gc);
				board[row][col].setPreferredSize(DIM);
			}
		}
		
		// Add grid panel to wrapper panel
		add(gridPanel);
	}
	
	/**
	 * Set board listener
	 * @param boardListener
	 */
	public void setBoardListener(BoardListener boardListener){
		this.boardListner = boardListener;
	}
	
	/**
	 * Lock/Unlock board
	 * @param lock
	 */
	public void lock(boolean lock){
		this.lock = lock;
	}
	
	/**
	 * Reset board
	 */
	public void reset(){
		// Add components to grid panel
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				board[row][col].reset();
			}
		}
	}
	
	/**
	 * Manual place
	 * @param row
	 * @param col
	 */
	public void manualPlace(int row, int col){
		
		// If locked board, return
		if(lock) return;
		
		// If board listener is not set, return
		if(boardListner == null) return;
		
		int turn = boardListner.overAction();
		
		// If black token
		if(turn == GangiHo.BLACK){
			if(col == board.length-1)
				board[row][col-1].placeBlackToken();
			else
				board[row][col+1].placeBlackToken();
			board[row][col].placeBlackToken();
		
		// If white token
		} else {
			if(row == board.length-1)
				board[row-1][col].placeWhiteToken();
			else
				board[row+1][col].placeWhiteToken();
			board[row][col].placeWhiteToken();
		}
	}
	
	/**
	 * Rebuild board based on AI moves
	 * @param newBoard
	 */
	public void rebuild(Board newBoard){
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				if(newBoard.board[row][col] == GangiHo.BLACK){
					board[row][col].placeBlackToken();
				}else if(newBoard.board[row][col] == GangiHo.WHITE){
					board[row][col].placeWhiteToken();
				}
			}
		}
	}
}
