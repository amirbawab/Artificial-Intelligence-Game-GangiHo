package gui.lib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

import game.Board;
import game.GangiHo;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class BoardLabel extends JLabel{
	private int row,col;
	private int token = Board.EMPTY;
	private int size;
	
	/**
	 * Constructor
	 * @param row
	 * @param col
	 */
	public BoardLabel(int row, int col, int size) {
		super();
		this.row = row;
		this.col = col;
		this.size = size;
		setBackground(Color.GRAY);
		setOpaque(true);
	}

	/**
	 * Get row
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Get col
	 * @return col
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Mouse over and black token to be placed
	 */
	public void overBlackToken(){
		if(token == Board.EMPTY)
			setIcon(resizeImg( getClass().getResource("../images/black_token_over.png")));
		else if(token == GangiHo.WHITE)
			setIcon(resizeImg( getClass().getResource("../images/white_wrong.png")));
		else 
			setIcon(resizeImg( getClass().getResource("../images/black_wrong.png")));
	}
	
	/**
	 * Place black token
	 */
	public void placeBlackToken(){
		this.token = GangiHo.BLACK;
		setIcon(resizeImg( getClass().getResource("../images/black_token.png")));
	}
	
	/**
	 * Mouse over and white token to be placed
	 */
	public void overWhiteToken(){
		if(token == Board.EMPTY)
			setIcon(resizeImg( getClass().getResource("../images/white_token_over.png")));
		else if(token == GangiHo.WHITE)
			setIcon(resizeImg( getClass().getResource("../images/white_wrong.png")));
		else 
			setIcon(resizeImg( getClass().getResource("../images/black_wrong.png")));
	}
	
	/**
	 * Place white token
	 */
	public void placeWhiteToken(){
		this.token = GangiHo.WHITE;
		setIcon(resizeImg(getClass().getResource("../images/white_token.png")));
	}
	
	/**
	 * Resize image
	 * @param srcImg
	 * @param w
	 * @param h
	 * @return image
	 */
	public ImageIcon resizeImg(URL url){
	    BufferedImage resizedImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(new ImageIcon(url).getImage(), 0, 0, size, size, null);
	    g2.dispose();
	    return new ImageIcon(resizedImg);
	}
	
	/**
	 * Remove token from cell
	 */
	public void removeToken(){
		if(token == Board.EMPTY)
			setIcon(null);
		
		// Repaint
		else
			refresh();
	}
	
	/**
	 * Refresh token
	 */
	public void refresh(){
		if(token == GangiHo.WHITE)
			placeWhiteToken();
		else if(token == GangiHo.BLACK)
			placeBlackToken();
	}
	
	/**
	 * Reset label
	 */
	public void reset(){
		token = Board.EMPTY;
		setIcon(null);
	}
}
