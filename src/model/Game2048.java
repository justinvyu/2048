package model;

import java.util.Scanner;

public class Game2048 {
	private Board board;
	private int score;

	public Game2048() {
		board = new Board();
	}
	
	public Game2048(int row, int col) {
		board = new Board(row, col);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getScore() {
		return board.getScoreTotal();
	}
	
	public void handleKeyEvent(Board.Direction direction) {
		System.out.println(board);
		board.spawnRandomTile();
	
		System.out.println(board);
		board.shift(direction);
	}

}
