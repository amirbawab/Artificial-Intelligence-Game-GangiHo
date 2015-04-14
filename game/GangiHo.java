package game;

import graph.Graph;
import gui.helper.BoardHelper;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class GangiHo {
	
	// Constants
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	public static final int AI = 3;
	public static final int HUMAN = 4;
	
	// Variable
	private int turn, p1Type, p2Type, p1Token;
	private Board board;
	private int totalMoves;
	private int chosenChild;
	private int heuristic;
	private int parent;
	
	// Graph
	private Graph aiGraph;
	
	/**
	 * Constructor
	 * @param turn WHITE | BLACK
	 */
	public GangiHo(int p1Type, int p2Type, int p1Token) {
		setTurn(p1Token);
		board = new Board();
		totalMoves = 0;
		this.p1Type = p1Type;
		this.p2Type = p2Type;
		this.p1Token = p1Token;
	}
	
	/**
	 * Copy constructor
	 * @param gh
	 */
	public GangiHo(GangiHo gh){
		turn = gh.turn;
		totalMoves = gh.totalMoves;
		board = gh.getBoard().clone();
		p1Type = gh.p1Type;
		p2Type = gh.p2Type;
		p1Token = gh.p1Token;
	}
	
	/**
	 * Play by placing 2 tokens
	 * @param point
	 */
	public void place(int row, int col){
		board.placeTokens(new Point(col, row), turn);
		turn = getOppositeTurn(this);
		totalMoves++;
	}
	
	/**
	 * Play by placing 2 tokens
	 * @param str
	 */
	public void place(String str){
		place(str.charAt(0)-'A', Integer.parseInt(str.charAt(1) + "")-1);
	}
	
	/**
	 * Checks if can place at point `point`
	 * @param point
	 * @return boolean
	 */
	public boolean canPlace(int row, int col){
		return board.isPlaceable(new Point(col, row), turn);
	}
	
	/**
	 * Get board
	 * @return board
	 */
	public Board getBoard(){
		return board;
	}
	
	/**
	 * Get turn
	 * @return WHITE | BLACK
	 */
	public int getTurn(){
		return turn;
	}
	
	/**
	 * Set turn
	 * @param turn
	 */
	public void setTurn(int turn){
		this.turn = turn;
	}
	
	/**
	 * Get if the game is over
	 * @param player
	 * @return boolean
	 */
	public boolean gameOver(){
		for(int row = 0; row < Board.MAX_SIZE; row++)
			for(int col = 0; col < Board.MAX_SIZE; col++)
				if(canPlace(row, col))
					return false;
		return true;
	}
	
	/**
	 * Check if there's a tie
	 * @return boolean
	 */
	public boolean isTie(){
		for(int row = 0; row < Board.MAX_SIZE; row++)
			for(int col = 0; col < Board.MAX_SIZE; col++)
				if(board.board[row][col] == 0)
					return false;
		return true;
	}
	
	/**
	 * Set board
	 * @param board
	 */
	public void setBoard(Board board){
		this.board = board;
	}
	
	/**
	 * Get board of the next move
	 * @return board
	 */
	public void getNextMove(){
		
		// Build graph
		buildGraph();
		
		// Debug
//		System.out.println("Used only nodes: " + aiGraph.size());
		
		// Perform minimax
		miniMax();
		
		// Toggle turn and increment total moves
		turn = getOppositeTurn(this);
		totalMoves++;
	}
	
	/**
	 * Build graph
	 * @return first minimax node
	 */
	public void buildGraph(){
		// Calculate best depth
		int MAX_NODES = 300000;
		int DEPTH = 1;
		int allPoss = Board.MAX_SIZE * (Board.MAX_SIZE-1) - (int)(1.5 * totalMoves); // Estimate Worst case
		int dynamicPoss = allPoss;
		int prevDynamicPoss = 0;
		while(dynamicPoss < MAX_NODES){
			
			// Cache previous value of dynamic poss
			prevDynamicPoss = dynamicPoss;
			
			// Calculate next move possibilities
			int nextPoss = allPoss - DEPTH++ * 2;
			
			// If poss is less than 1 break
			if(nextPoss < 1)
				break;
			
			// Update poss
			dynamicPoss *= nextPoss;
		}

		// Adjust values
		DEPTH--;
		
		// Debug
//		System.out.println(String.format("Depth: %d ; All nodes: %d ; All poss: %d", DEPTH, prevDynamicPoss, allPoss));
				
//		prevDynamicPoss = Math.max(prevDynamicPoss, 10000);
//		allPoss = Math.max(allPoss,10);
		
		aiGraph = new Graph(prevDynamicPoss, allPoss); // Dynamic possibilities but not less than 50 and 5
		Queue<Integer> gameQ = new LinkedList<Integer>();
		
		int depthCounter = 0;
		int toggleTurn = turn;

		// Add root & reset config
		gameQ.add(0);
		aiGraph.addGame(this);
		this.parent = -1;
		this.chosenChild = -1;
		int gameID = 0;
		
		// While states are not empty
		while(!gameQ.isEmpty() && DEPTH != 0){
			gameID = gameQ.poll();
			GangiHo currentRootGame = aiGraph.games[gameID];
			
			// Limit depth
			if(toggleTurn != currentRootGame.getTurn()){
				toggleTurn = currentRootGame.getTurn();
				depthCounter++;
				if(depthCounter >= DEPTH){
					break;
				}
			}
			
			// Create all possibilities
			for(int row = 0; row < Board.MAX_SIZE; row++){
				for(int col = 0; col < Board.MAX_SIZE; col++){
					if(currentRootGame.canPlace(row, col)){
						GangiHo clonedGame = new GangiHo(currentRootGame);
						clonedGame.place(row, col);
						int newGameID = aiGraph.addGame(clonedGame);
						gameQ.offer(newGameID);
						aiGraph.addEdge(gameID, newGameID);
						aiGraph.games[newGameID].parent = gameID;
					}
				}
			}
		}
	}
	
	/**
	 * Minimax, then return chosen game
	 * @param minimax
	 * @return
	 */
	public int miniMax(){
		int min = 0, max = 0;
		
		for(int gameID = aiGraph.size()-1; gameID >= 0; gameID--){
			
			// If leaf node
			if(aiGraph.degree[gameID] == 0){
				calcHeuristic(gameID);
				aiGraph.games[gameID].chosenChild = -1;
				
			// If not leaf
			} else {
				
				// If MIN level
				if(! isMaxTurn(aiGraph.games[gameID])){
					
					min = aiGraph.graph[gameID][0];
					for(int i=1; i<aiGraph.degree[gameID]; i++){
						int childGameID = aiGraph.graph[gameID][i];
						if(aiGraph.games[min].heuristic > aiGraph.games[childGameID].heuristic)
							min = childGameID;
					}
					aiGraph.games[gameID].heuristic = aiGraph.games[min].heuristic;
					aiGraph.games[gameID].chosenChild = min;
				
				// If MAX level
				}else{
					
					max = aiGraph.graph[gameID][0];
					for(int i=1; i<aiGraph.degree[gameID]; i++){
						int childGameID = aiGraph.graph[gameID][i];
						if(aiGraph.games[max].heuristic < aiGraph.games[childGameID].heuristic)
							max = childGameID;
					}
					aiGraph.games[gameID].heuristic = aiGraph.games[max].heuristic;
					aiGraph.games[gameID].chosenChild = max;
				}
			}
		}
		return max;
	}
	
	/**
	 * Get graph
	 * @return graph
	 */
	public Graph getGraph(){
		return aiGraph;
	}
	
	/**
	 * Reset moves
	 */
	public void resetMoves(){
		totalMoves = 0;
	}
	
	/**
	 * Get p1 type
	 * @return p1 type
	 */
	public int getP1Type() {
		return p1Type;
	}

	/**
	 * Get p2 type
	 * @return p2 type
	 */
	public int getP2Type() {
		return p2Type;
	}

	/**
	 * Get p1 token color
	 * @return p1 token color
	 */
	public int getP1Token() {
		return p1Token;
	}

	/**
	 * Checks if is my turn or MIN's turn
	 * @param game
	 * @return
	 */
	public boolean isMaxTurn(GangiHo game){
		return turn == game.turn;
	}
	
	/**
	 * Get opposite turn
	 * @param game
	 * @return opposite turn
	 */
	public int getOppositeTurn(GangiHo game){
		return 3 - game.getTurn();
	}
	
	/**
	 * Get heuristic
	 * @return heuristic
	 */
	public int getHeuristic(){
		return heuristic;
	}
	
	/**
	 * Get chosen child
	 * @return child
	 */
	public int getChosenChild(){
		return chosenChild;
	}
	
	/**
	 * To string
	 */
	public String toString(){
		return board.toString();
	}
	
	/////////////////////// HEURISTIC METHODS /////////////////////////////////
	
	/**
	 * Calculate heuristic
	 * @param gameID
	 */
	private void calcHeuristic(int gameID){
		int[][] boardp = aiGraph.games[gameID].board.board;
		
		// Checks if game is over after the 6th move. VERY IMPORTANT TO RETURN OTHERWISE INTERGER OVERFLOW AND INCORRECT RESULTS
		if(totalMoves > 6){
			
			int gameOverH = gameOver(gameID);

			// If gameover
			if(gameOverH != 0){
				
				// If tie
				if(aiGraph.games[gameID].isTie()){
					aiGraph.games[gameID].heuristic = Integer.MAX_VALUE-1;
					return;
				}
				
				aiGraph.games[gameID].heuristic += gameOverH;
				return;
			}
		}
		
		// AI made its move, adjust heuristic
		for(int row = 0; row < Board.MAX_SIZE; row++){
			for(int col = 0; col < Board.MAX_SIZE; col++){
				
				aiGraph.games[gameID].heuristic += minMoves(row, col, gameID, -1);
				aiGraph.games[gameID].heuristic += maxMoves(row, col, gameID, +1);
				aiGraph.games[gameID].heuristic += oppositeBordersEmpty(row, col, boardp, 2);
				aiGraph.games[gameID].heuristic += inBetween(row, col, boardp, 2);
				aiGraph.games[gameID].heuristic += cornersOnBorders(row, col, boardp, -1);
			}
			
			// Find a long pattern
			aiGraph.games[gameID].heuristic += blockOpponent(row, boardp, 2); // Slow counter, row or col doesn't matter
		}
		
		// Count empty spots surrounded by non empty spots
//		aiGraph.games[gameID].heuristic += emptySpots(boardp, 4); // Very slow counter
	}
	
	/**
	 * Checks if the MIN can place
	 * @param row
	 * @param col
	 * @param gameID
	 * @return h
	 */
	private int minMoves(int row, int col, int gameID, int heuristic){
		
		// Keep track of the original turn
		int orgTurn = aiGraph.games[gameID].getTurn();
		
		// If MAX turn, force temp switch to MIN
		if(isMaxTurn(aiGraph.games[gameID]))
			aiGraph.games[gameID].setTurn(getOppositeTurn(aiGraph.games[gameID]));
		
		// If opponent can place
		if(aiGraph.games[gameID].canPlace(row, col)){
			aiGraph.games[gameID].setTurn(orgTurn); // Fix turn
			return heuristic;
		}
		
		// Fix turn
		aiGraph.games[gameID].setTurn(orgTurn);
		return 0;
	}
	
	/**
	 * Checks if the MAX can place
	 * @param row
	 * @param col
	 * @param gameID
	 * @return h
	 */
	private int maxMoves(int row, int col, int gameID, int heuristic){
		
		// Keep track of the original turn
		int orgTurn = aiGraph.games[gameID].getTurn();
		
		// If MIN turn, force temp switch to MIN
		if(! isMaxTurn(aiGraph.games[gameID]))
			aiGraph.games[gameID].setTurn(getOppositeTurn(aiGraph.games[gameID]));
		
		// If opponent can place
		if(aiGraph.games[gameID].canPlace(row, col)){
			aiGraph.games[gameID].setTurn(orgTurn); // Fix turn
			return heuristic;
		}
		
		// Fix turn
		aiGraph.games[gameID].setTurn(orgTurn);
		return 0;
	}
	
	/**
	 * Top and bottom cells if empty
	 * @param row
	 * @param col
	 * @param h
	 */
	private int oppositeBordersEmpty(int row, int col, int[][] boardp, int heuristic){
		
		// Black token turn
		if(turn == BLACK){
			if(BoardHelper.isInner(row, col) || BoardHelper.isBorder(row, col) == BoardHelper.LEFT){
				if	(
						boardp[row-1][col] == Board.EMPTY && boardp[row-1][col+1] == Board.EMPTY &&
						boardp[row][col] == BLACK && boardp[row][col+1] == BLACK &&
						boardp[row+1][col] == Board.EMPTY && boardp[row+1][col+1] == Board.EMPTY
					)
					return heuristic;
			}
		
		// White token turn
		} else if(turn == WHITE){
			if(BoardHelper.isInner(row, col) || BoardHelper.isBorder(row, col) == BoardHelper.TOP){
				if	(		
						boardp[row][col-1] == Board.EMPTY && boardp[row][col] == WHITE && boardp[row][col+1] == Board.EMPTY &&
						boardp[row+1][col-1] == Board.EMPTY && boardp[row+1][col] == WHITE && boardp[row+1][col+1] == Board.EMPTY
					)
					return heuristic;
			}
		}
		
		return 0;
	}
	
	/**
	 * Check if the game is over
	 * @param gameID
	 * @return h
	 */
	private int gameOver(int gameID){
		if(aiGraph.games[gameID].gameOver()){
			
			// If MIN Turn after game over, than a guarantee win for MAX
			if(! isMaxTurn(aiGraph.games[gameID]))
				return Integer.MAX_VALUE;
			return Integer.MIN_VALUE;
		}
		return 0;
	}
	
	/**
	 * Check for in between case
	 * @param row
	 * @param col
	 * @param boardp
	 */
	public int inBetween(int row, int col, int[][] boardp, int heuristic){
		
		// If black
		if(turn == BLACK) {
			if((row >= 0 && row <= Board.MAX_SIZE-2) && (col < Board.MAX_SIZE - 3)){
				if(
						(
							boardp[row][col] == WHITE && boardp[row][col+1] == BLACK && boardp[row][col+2] == BLACK && boardp[row][col+3] == WHITE &&
							boardp[row+1][col] == WHITE && boardp[row+1][col+1] == Board.EMPTY && boardp[row+1][col+2] == Board.EMPTY && boardp[row+1][col+3] == WHITE
						) || (
							boardp[row][col] == WHITE && boardp[row][col+1] == Board.EMPTY && boardp[row][col+2] == Board.EMPTY && boardp[row][col+3] == WHITE &&
							boardp[row+1][col] == WHITE && boardp[row+1][col+1] == BLACK && boardp[row+1][col+2] == BLACK && boardp[row+1][col+3] == WHITE
						)
					)
					return heuristic;
			}
		
		// If white
		} else if(turn == WHITE) {
			if((col >= 0 && col <= Board.MAX_SIZE-2) && (row < Board.MAX_SIZE - 3)){
				if(
						(
							boardp[row][col] == BLACK && boardp[row][col+1] == BLACK &&
							boardp[row+1][col] == WHITE && boardp[row+1][col+1] == Board.EMPTY && 
							boardp[row+2][col] == WHITE && boardp[row+2][col+1] == Board.EMPTY && 
							boardp[row+3][col] == BLACK && boardp[row+3][col+1] == BLACK
						) || (
							boardp[row][col] == BLACK && boardp[row][col+1] == BLACK &&
							boardp[row+1][col] == Board.EMPTY && boardp[row+1][col+1] == WHITE && 
							boardp[row+2][col] == Board.EMPTY && boardp[row+2][col+1] == WHITE && 
							boardp[row+3][col] == BLACK && boardp[row+3][col+1] == BLACK
						)
					)
					return heuristic;
			}
		}
		
		return 0;
	}
	
	/**
	 * Blocking opponent moves for odd space moves
	 * @param row
	 * @param col
	 * @param gameID
	 */
	public int blockOpponent(int rowCol, int[][] boardp, int heuristic){
		
		int whiteC = 0;
		int blackC = 0;
		
		if(turn == BLACK){
			for(int row = 0; row < Board.MAX_SIZE; row++){
				if(boardp[row][rowCol] == WHITE){
					whiteC++;
				} else if(boardp[row][rowCol] == BLACK){
					blackC++;
				}
			}
			
			if(whiteC == 4 && blackC == 1){

				if(
						!(boardp[2][rowCol] == BLACK && boardp[5][rowCol] == Board.EMPTY) &&
						!(boardp[2][rowCol] == Board.EMPTY && boardp[5][rowCol] == BLACK) &&
						!(boardp[0][rowCol] == BLACK && boardp[3][rowCol] == Board.EMPTY) &&
						!(boardp[0][rowCol] == Board.EMPTY && boardp[3][rowCol] == BLACK)
						
						){
					return heuristic;
				}
			}
			
		} else if(turn == WHITE){
			for(int col = 0; col < Board.MAX_SIZE; col++){
				if(boardp[rowCol][col] == WHITE){
					whiteC++;
				} else if(boardp[rowCol][col] == BLACK){
					blackC++;
				}
			}
			
			if(whiteC == 1 && blackC == 4){

				if(
						!(boardp[rowCol][2] == WHITE && boardp[rowCol][5] == Board.EMPTY) &&
						!(boardp[rowCol][2] == Board.EMPTY && boardp[rowCol][5] == WHITE) &&
						!(boardp[rowCol][0] == WHITE && boardp[rowCol][3] == Board.EMPTY) &&
						!(boardp[rowCol][0] == Board.EMPTY && boardp[rowCol][3] == WHITE)
						
						){
					return heuristic;
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Check for corners on boarder
	 * @param row
	 * @param col
	 * @param boardp
	 * @return
	 */
	public int cornersOnBorders(int row, int col, int[][] boardp, int heuristic){
		
		// Corners
		if(BoardHelper.isBorder(row, col) == BoardHelper.TOP_LEFT){
			if(BoardHelper.LShapeRightBottom(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp))
				return heuristic;
			
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.TOP_RIGHT){
			if(BoardHelper.LShapeBottomLeft(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp))
				return heuristic;
		
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM_RIGHT){
			if(BoardHelper.LShapeLeftTop(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp))
				return heuristic;
		
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM_LEFT){
			if(BoardHelper.LShapeTopRight(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp))
				return heuristic;
		
		// Borders
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.TOP){
			if(
					BoardHelper.LShapeBottomLeft(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp) ||
					BoardHelper.LShapeRightBottom(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp)
				)
				return heuristic;
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.RIGHT){
			if(
					BoardHelper.LShapeLeftTop(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp) ||
					BoardHelper.LShapeBottomLeft(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp)
				)
				return heuristic;
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM){
			if(
					BoardHelper.LShapeTopRight(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp) ||
					BoardHelper.LShapeLeftTop(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp)
				)
				return heuristic;
		} else if(BoardHelper.isBorder(row, col) == BoardHelper.LEFT){
			if(
					BoardHelper.LShapeTopRight(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp) ||
					BoardHelper.LShapeRightBottom(row, col, Board.EMPTY, Board.EMPTY, Board.EMPTY, boardp)
				)
				return heuristic;
		} 
		
		return 0;
	}
	
	/**
	 * Count empty spots surrounded by non empty spots
	 * @param boardp
	 * @param heuristic
	 * @return heuristic
	 */
	public int emptySpots(int[][] boardp, int heuristic){
		int empty = 0;
		for(int row = 0; row < Board.MAX_SIZE; row++){
			for(int col = 0; col < Board.MAX_SIZE; col++){
				if(BoardHelper.isInner(row, col)){
					if(		boardp[row][col] == 0 && 
							boardp[row-1][col] != 0 &&
							boardp[row][col-1] != 0 &&
							boardp[row-1][col-1] != 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row][col+1] != 0 &&
							boardp[row+1][col+1] != 0 &&
							boardp[row-1][col+1] != 0 &&
							boardp[row+1][col-1] != 0){
						
						empty++;
					}
						
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.TOP_LEFT){
					if(		boardp[row][col] == 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row+1][col+1] != 0 &&
							boardp[row][col+1] != 0){
						empty++;
					}
				
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.TOP_RIGHT){
					if(		boardp[row][col] == 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row+1][col-1] != 0 &&
							boardp[row][col-1] != 0){
						empty++;
					}
				
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM_LEFT){
					if(		boardp[row][col] == 0 &&
							boardp[row-1][col] != 0 &&
							boardp[row-1][col+1] != 0 &&
							boardp[row][col+1] != 0){
						empty++;
					}
				
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM_RIGHT){
					if(		boardp[row][col] == 0 &&
							boardp[row-1][col] != 0 &&
							boardp[row-1][col-1] != 0 &&
							boardp[row][col-1] != 0){
						empty++;
					}
				
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.TOP){
					if(		boardp[row][col] == 0 &&
							boardp[row][col-1] != 0 &&
							boardp[row+1][col-1] != 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row+1][col+1] != 0 &&
							boardp[row][col+1] != 0){
						empty++;
					}
				
				} else if(BoardHelper.isBorder(row, col) == BoardHelper.BOTTOM){
					if(		boardp[row][col] == 0 &&
							boardp[row][col-1] != 0 &&
							boardp[row-1][col-1] != 0 &&
							boardp[row-1][col] != 0 &&
							boardp[row-1][col+1] != 0 &&
							boardp[row][col+1] != 0){
						empty++;
					}
					
				}  else if(BoardHelper.isBorder(row, col) == BoardHelper.LEFT){
					if(		boardp[row][col] == 0 &&
							boardp[row-1][col] != 0 &&
							boardp[row-1][col+1] != 0 &&
							boardp[row][col+1] != 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row+1][col+1] != 0){
						empty++;
					}
					
				}  else if(BoardHelper.isBorder(row, col) == BoardHelper.RIGHT){
					if(		boardp[row][col] == 0 &&
							boardp[row-1][col] != 0 &&
							boardp[row-1][col-1] != 0 &&
							boardp[row][col-1] != 0 &&
							boardp[row+1][col] != 0 &&
							boardp[row+1][col-1] != 0){
						empty++;
					}
				}
			}
		}
		
		// If not empty, then skip
		if(empty == 0)
			return empty;
		
		// If first player: [1-2] [5-6]
		if(turn == p1Token){
			if( ((empty - 1) / 2) % 2 == 0)
				return heuristic;
			else
				return -heuristic;
		
		// If second player
		} else {
			if( ((empty - 1) / 2) % 2 == 0)
				return -heuristic;
			else
				return heuristic;
		}
	}
}