package game;
import java.awt.Point;

public class Board {
	
	// Constants
	public static int MAX_SIZE = 8;
	public final static int EMPTY = 0;
	
	// Variables
	public int board[][];
	
	// Constructor
	protected Board() {
		board = new int[MAX_SIZE][MAX_SIZE];
	}
	
	/**
	 * Checks if 2 tokens are placeable
	 * @param token1 (y: row & x: col)
	 * @param player GangiHo.WHITE | GangiHo.BLACK
	 * @return boolean
	 */
	protected boolean isPlaceable(Point token1, int player){
		Point token2 = null;
		if(player == GangiHo.WHITE){
			if(		token1.x < 0 || token1.x >= MAX_SIZE  || 
					token1.y < 0 || token1.y >= MAX_SIZE - 1)
				return false;
			token2 = new Point(token1.x, token1.y + 1);
			
		} else if(player == GangiHo.BLACK){
			if(		token1.x < 0 || token1.x >= MAX_SIZE - 1 || 
					token1.y < 0 || token1.y >= MAX_SIZE)
				return false;
			token2 = new Point(token1.x + 1, token1.y);
		}
		return board[token1.y][token1.x] == EMPTY && board[token2.y][token2.x] == EMPTY;
	}
	
	/**
	 * Place tokens
	 * @param token1
	 * @param player GangiHo.WHITE | GangiHo.BLACK
	 */
	protected void placeTokens(Point token1, int player){
		if(player == GangiHo.WHITE){
			board[token1.y][token1.x] = GangiHo.WHITE;
			board[token1.y+1][token1.x] = GangiHo.WHITE;
		
		} else if(player == GangiHo.BLACK){
			board[token1.y][token1.x] = GangiHo.BLACK;
			board[token1.y][token1.x+1] = GangiHo.BLACK;
		}
	}
	
	/**
	 * Remove tokens
	 * @param token1
	 * @param player GangiHo.WHITE | GangiHo.BLACK
	 */
	protected void removeTokens(Point token1, int player){
		if(player == GangiHo.WHITE){
			board[token1.y][token1.x] = Board.EMPTY;
			board[token1.y+1][token1.x] = Board.EMPTY;
		
		} else if(player == GangiHo.BLACK){
			board[token1.y][token1.x] = Board.EMPTY;
			board[token1.y][token1.x+1] = Board.EMPTY;
		}
	}
	
	/**
	 * Reset board
	 */
	public void reset(){
		for(int row = 0; row < Board.MAX_SIZE; row++)
			for(int col = 0; col < Board.MAX_SIZE; col++)
				board[row][col] = EMPTY;
	}
	
	/**
	 * Clone board
	 */
	public Board clone(){
		Board clone = new Board();
		for(int row = 0; row < MAX_SIZE; row++){
			for(int col = 0; col < MAX_SIZE; col++){
				clone.board[row][col] = board[row][col];
			}
		}
		return clone;
	}
	
	/**
	 * To string
	 */
	public String toString(){
		String output = "   [1][2][3][4][5][6]\n";
		for(int row = 0; row < MAX_SIZE; row++){
			output += "["+(char)('A' + row) + "]";
			for(int col = 0; col < MAX_SIZE; col++){
				if(board[row][col] != 0)
					output += "[" + (board[row][col] == GangiHo.WHITE ? 'o' : '\u2022') + "]";
				else
					output += "[ ]";
			}
			output += "\n";
		}
		return output;
	}
}
