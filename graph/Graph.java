package graph;

import java.util.LinkedList;
import java.util.Queue;

import game.Board;
import game.GangiHo;

public class Graph{
	public int[][] graph; 		// Adjacency List
	public int[] degree; 		// Number of children
	public int NODES;	// Graph predefined nodes STILL NOT FIXED!!
	private int nodeID = 0;		// Unique nodeID for each node
	public GangiHo[] games;		// All games
	
	/**
	 * Constructor
	 */
	public Graph(int allNodes, int allPoss) {
		this.NODES = allNodes;
		graph = new int[NODES][allPoss];
		degree = new int[NODES];
		games = new GangiHo[NODES];
	}
	
	/**
	 * Add edge
	 * @param from
	 * @param to
	 */
	public void addEdge(int from, int to){
		graph[from][degree[from]++] = to;
	}
	
	/**
	 * Add game
	 * @param game
	 * @return
	 */
	public int addGame(GangiHo game){
		games[nodeID] = game;
		return nodeID++; 
	}
	
	/**
	 * Print graph
	 */
	public void print(){
		for(int row = 0; row < graph.length; row++){
			System.out.print(row + " : ");
			for(int col = 0; col < degree[row]; col++){
				System.out.print(graph[row][col] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Breadth first traversal
	 * */
	public void BFS(){
		Queue<Integer> q = new LinkedList<Integer>();
		int[] visit = new int[degree.length];
		q.offer(0);
		visit[0] = 1;
		while(!q.isEmpty()){
			int tmp = q.poll();
			System.out.println(games[tmp]);
			for(int j=0; j<degree[tmp];j++){
				int neighbor = graph[tmp][j];
				if(visit[neighbor] == 0){
					q.offer(neighbor);
					visit[neighbor] = 1;
				}
			}
		}
	}
	
	/**
	 * Get nodes number
	 * @return
	 */
	public int size(){
		return nodeID;
	}
}