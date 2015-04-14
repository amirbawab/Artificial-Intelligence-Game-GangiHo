package gui.helper;

import game.Board;

public class BoardHelper {
	
	// Corners constants
	public final static int TOP_LEFT = 1;
	public final static int TOP_RIGHT = 2;
	public final static int BOTTOM_LEFT = 3;
	public final static int BOTTOM_RIGHT = 4;
	
	// Borders constants
	public final static int TOP = 5;
	public final static int RIGHT = 6;
	public final static int BOTTOM = 7;
	public final static int LEFT = 8;
	
	// Not valid constant
	public final static int INVALID = -1;
	
	/**
	 * Checks if a cell is a border
	 * @param row
	 * @param col
	 * @return position
	 */
	public static int isCorner(int row, int col){
		if(row == 0 && col == 0)
			return TOP_LEFT;
		
		if(row == 0 && col == Board.MAX_SIZE-1)
			return TOP_RIGHT;
		
		if(row == Board.MAX_SIZE-1 && col == 0)
			return BOTTOM_LEFT;
		
		if(row == Board.MAX_SIZE-1 && col == Board.MAX_SIZE-1)
			return BOTTOM_RIGHT;
		
		return INVALID;
	}
	
	/**
	 * Checks if a cell is a border
	 * @param row
	 * @param col
	 * @return position
	 */
	public static int isBorder(int row, int col){
		if(isCorner(row, col) != INVALID)
			return isCorner(row, col);
		
		if(row == 0)
			return TOP;
		
		if(row == Board.MAX_SIZE-1)
			return BOTTOM;
		
		if(col == 0)
			return LEFT;
		
		if(col == Board.MAX_SIZE-1)
			return RIGHT;
		
		return INVALID;
	}
	
	/**
	 * Checks if a cell is inside the board and not at its border
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public static boolean isInner(int row, int col){
		return isBorder(row, col) == INVALID;
	}
	
	/**
	 * Checks if a cell is inside, top or bottom of the board. But not left or right
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public static boolean isTopBottomOrInner(int row, int col){
		return isInner(row, col) || isBorder(row, col) == TOP || isBorder(row, col) == BOTTOM;
	}
	
	/**
	 * Checks if a cell is inside, left or right of the board. But not top or bottom
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public static boolean isLeftRightOrInner(int row, int col){
		return isInner(row, col) || isBorder(row, col) == LEFT || isBorder(row, col) == RIGHT;
	}
	
	/**
	 * Checks if an horizontal wall exists
	 * @param row
	 * @param col
	 * @param count
	 * @param boardp
	 * @return boolean
	 */
	public static boolean isHorizontalWall(int row, int col, int count, int[][] boardp){
		while(col < Board.MAX_SIZE - 1 && count-- != 0)
			if(boardp[row][col++] == Board.EMPTY)
				return false;
		return true;
	}
	
	/**
	 * Checks if an vertical wall exists
	 * @param row
	 * @param col
	 * @param count
	 * @param boardp
	 * @return boolean
	 */
	public static boolean isVerticalWall(int row, int col, int count, int[][] boardp){
		while(row < Board.MAX_SIZE - 1 && count-- != 0)
			if(boardp[row++][col] == Board.EMPTY)
				return false;
		return true;
	}
	
	/**
	 * Top-Right L shape
	 * @param row
	 * @param col
	 * @param top
	 * @param center
	 * @param right
	 * @param boardp
	 * @return boolean
	 */
	public static boolean LShapeTopRight(int row, int col, int top, int center, int right, int[][] boardp){
		return boardp[row-1][col] == top && boardp[row][col] == center && boardp[row][col+1] == right && boardp[row-1][col+1] != Board.EMPTY;
	}
	
	/**
	 * Right-Bottom L shape
	 * @param row
	 * @param col
	 * @param right
	 * @param center
	 * @param bottom
	 * @param boardp
	 * @return boolean
	 */
	public static boolean LShapeRightBottom(int row, int col, int right, int center, int bottom, int[][] boardp){
		return boardp[row][col+1] == right && boardp[row][col] == center && boardp[row+1][col] == bottom && boardp[row+1][col+1] != Board.EMPTY;
	}
	
	/**
	 * Bottom-Left L shape
	 * @param row
	 * @param col
	 * @param bottom
	 * @param center
	 * @param left
	 * @param boardp
	 * @return boolean 
	 */
	public static boolean LShapeBottomLeft(int row, int col, int bottom, int center, int left, int[][] boardp){
		return boardp[row+1][col] == bottom && boardp[row][col] == center && boardp[row][col-1] == left && boardp[row+1][col-1] != Board.EMPTY;
	}
	
	/**
	 * Left-Top L shape
	 * @param row
	 * @param col
	 * @param left
	 * @param center
	 * @param top
	 * @param boardp
	 * @return boolean
	 */
	public static boolean LShapeLeftTop(int row, int col, int left, int center, int top, int[][] boardp){
		return boardp[row][col-1] == left && boardp[row][col] == center && boardp[row-1][col] == top && boardp[row-1][col-1] != Board.EMPTY;
	}
}
