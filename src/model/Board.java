package model;

public class Board {
	
	public enum Direction {
		UP,
		RIGHT,
		DOWN,
		LEFT
	}
	
	public static final int numberOfStartingTiles = 2;
	
	private int numRows;
	private int numCols;
	private Tile[][] tiles;
	private int scoreTotal;
	private boolean hasChanged = false;
	
	// MARK: Constructors
	
	public Board() {
		this(4, 4);
	}
	
	public Board(int rows, int cols) {
		this.numRows = rows;
		this.numCols = cols;
		tiles = new Tile[rows][cols];
		
		spawnRandomTile(Board.numberOfStartingTiles);
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				if(tiles[row][col] == null) {
					tiles[row][col] = new EmptyTile();
				}
			}
		}
	}
	
	// MARK: Functionality 
	
	public void shift(Direction direction) {
		int changeTotal = 0;
		switch(direction) {
		case UP:
		case DOWN:
			for(int col = 0; col < numCols; col++) {
				changeTotal += clumpColumn(col, direction);
				changeTotal += collapseColumn(col, direction);
			}
			break;
		case LEFT:
		case RIGHT:
			for(int row = 0; row < numRows; row++) {
				changeTotal += clumpRow(row, direction);
				changeTotal += collapseRow(row, direction);
			}
			break;
		}
		this.hasChanged = changeTotal != 0;
	}
	
	// MARK: Functionality - Clumping
	
	public int clumpRow(int row, Direction direction) {
		int numChanges = 0; // Change counter
		
		switch(direction) {
		case LEFT:
			for(int col = 0; col < numCols; col++) {
				int tempCol = col;
				while(tempCol < numCols - 1 && !doesNumberTileExistAtCell(row, tempCol)) {
					tempCol++;
				}
				if(tempCol == col) {
					continue;
				} else {
					swapTiles(row, col, row, tempCol);
					if(!(getTileAtCell(row, col) instanceof EmptyTile)) {
						numChanges++;
					}
				}
			}
			break;
		case RIGHT:
			for(int col = numCols - 1; col >= 0; col--) {
				int tempCol = col;
				while(tempCol > 0 && !doesNumberTileExistAtCell(row, tempCol)) {
					tempCol--;
				}
				if(tempCol == col) {
					continue;
				} else {
					swapTiles(row, col, row, tempCol);
					if(!(getTileAtCell(row, col) instanceof EmptyTile)) {
						numChanges++;
					}
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Row can only be clumped "
					+ "horizontally. Direction: " + direction);
		}
		
		return numChanges;
	}
	
	public int clumpColumn(int col, Direction direction) {
		int numChanges = 0;
		
		switch(direction) {
		case UP:
			for(int row = 0; row < numRows; row++) {
				int tempRow = row;
				while(tempRow < numRows - 1 && !doesNumberTileExistAtCell(tempRow, col)) {
					tempRow++;
				}
				if(tempRow == row) {
					continue;
				} else {
					swapTiles(row, col, tempRow, col);
					if(!(getTileAtCell(row, col) instanceof EmptyTile)) {
						numChanges++;
					}
				}
			}
			break;
		case DOWN:
			for(int row = numRows - 1; row >= 0; row--) {
				int tempRow = row;
				while(tempRow > 0 && !doesNumberTileExistAtCell(tempRow, col)) {
					tempRow--;
				}
				if(tempRow == row) {
					continue;
				} else {
					swapTiles(row, col, tempRow, col);
					if(!(getTileAtCell(row, col) instanceof EmptyTile)) {
						numChanges++;
					}
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Column can only be clumped "
					+ "vertically. Direction: " + direction);
		}
		
		return numChanges;
	}
	
	// MARK: Functionality - Collapsing
	
	public int collapseRow(int row, Direction direction) {
		int numChanges = 0;
		
		switch(direction) {
		case LEFT:
			for(int col = 0; col < numCols - 1; col++) {
				if(tiles[row][col] instanceof EmptyTile) { continue; }
				
				NumberTile tile = (NumberTile)tiles[row][col];
				
				if(tile.equals(tiles[row][col + 1])) {
					tile.merge(tiles[row][col + 1]);
					tiles[row][col + 1] = new EmptyTile();
					clumpRow(row, direction); // Re-clump tiles after merge
					numChanges++;
					scoreTotal += tile.getValue();
				}
			}
			break;
		case RIGHT:
			for(int col = numCols - 1; col > 0; col--) {
				if(tiles[row][col] instanceof EmptyTile) { continue; }
				
				NumberTile tile = (NumberTile)tiles[row][col];
				
				if(tile.equals(tiles[row][col - 1])) {
					tile.merge(tiles[row][col - 1]);
					tiles[row][col - 1] = new EmptyTile();
					clumpRow(row, direction); // Re-clump tiles after merge
					numChanges++;
					scoreTotal += tile.getValue();
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Row can only be collapsed "
					+ "horizontally. Direction: " + direction);
		}
		return numChanges;
	}
	
	public int collapseColumn(int col, Direction direction) {
		int numChanges = 0;
		
		switch(direction) {
		case UP:
			for(int row = 0; row < numRows - 1; row++) {
				if(tiles[row][col] instanceof EmptyTile) { continue; }
				
				NumberTile tile = (NumberTile)tiles[row][col];
				
				if(tile.equals(tiles[row + 1][col])) {
					tile.merge(tiles[row + 1][col]);
					tiles[row + 1][col] = new EmptyTile();
					clumpColumn(row, direction); // Re-clump tiles after merge
					numChanges++;
					scoreTotal += tile.getValue();
				}
			}
			break;
		case DOWN:
			for(int row = numRows - 1; row > 0; row--) {
				if(tiles[row][col] instanceof EmptyTile) { continue; }
				
				NumberTile tile = (NumberTile)tiles[row][col];
				
				if(tile.equals(tiles[row - 1][col])) {
					tile.merge(tiles[row - 1][col]);
					tiles[row - 1][col] = new EmptyTile();
					clumpColumn(row, direction); // Re-clump tiles after merge
					numChanges++;
					scoreTotal += tile.getValue();
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Column can only be collapsed "
					+ "vertically. Direction: " + direction);
		}
		
		return numChanges;
	}
	
	/**
	 * Checks whether there is an open space or any adjacent tile pairs.
	 * @return
	 */
	public boolean doesMoveExist() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {

				Tile tile = tiles[i][j];
				if(tile.getValue() == 0) {
					return true;
				}
				if(i < numRows - 1) { // Check bottom
					Tile bottomTile = tiles[i + 1][j];
					if(bottomTile instanceof EmptyTile || tile.equals(bottomTile)) {
						return true;
					}
			    }
				if(j < numCols - 1) { // Check right
					Tile rightTile = tiles[i][j + 1];
					if(rightTile instanceof EmptyTile || tile.equals(rightTile)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean doesNumberTileExistAtCell(int row, int col) {
		if(row < 0 || row > numRows || col < 0 || col > numCols) {
			throw new IllegalArgumentException("Invalid row and/or column. "
					+ "Row: " + row + ", Col: " + col);
		}
		return tiles[row][col] instanceof NumberTile;
	}
	
	public boolean doesValueExist(int value) {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(getValueAtCell(row, col) == value) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @precondition  Board must have an available space and the game
	 * 				  must not be over.
	 */
	public void spawnRandomTile() {
		int randomRow, randomCol;
		do {
			randomRow = getRandomRow();
			randomCol = getRandomCol();
		} while(doesNumberTileExistAtCell(randomRow, randomCol));
		tiles[randomRow][randomCol] = new NumberTile();
//		System.out.println("Tile spawned at (" + randomRow + ", " + randomCol + ")");
	}
	
	public void spawnRandomTile(int num) {
		for(int i = 0; i < num; i++) {
			spawnRandomTile();
		}
	}
	
	public void swapTiles(int row1, int col1, int row2, int col2) {
		Tile temp = tiles[row1][col1];
		tiles[row1][col1] = tiles[row2][col2];
		tiles[row2][col2] = temp;
	}
	
	// MARK: Getters and Setters
	
	public int getRandomRow() {
		return (int)(Math.random() * numRows);
	}
	
	public int getRandomCol() {
		return (int)(Math.random() * numCols);
	}
	
	public int getNumRows() {
		return this.numRows;
	}
	
	public int getNumCols() {
		return this.numCols;
	}
	
	public Tile getTileAtCell(int row, int col) {
		if(row < 0 || row > numRows - 1 || col < 0 || col > numCols - 1) {
			throw new IllegalArgumentException("Invalid row/col");
		}
		return tiles[row][col];
	}
	
	public int getValueAtCell(int row, int col) {
		return getTileAtCell(row, col).getValue();
	}
	
	public int getScoreTotal() {
		return scoreTotal;
	}
	
	public boolean getHasChanged() {
		return hasChanged;
	}
	
	@Override
	public String toString() {
		String output = "";
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				output += getValueAtCell(i, j) + "\t";
			}
			output += "\n";
		}
		return output;
	}
}
