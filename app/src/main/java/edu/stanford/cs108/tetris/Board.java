// Board.java
package edu.stanford.cs108.tetris;

/**
 * CS108 Tetris Board. Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing. Has an "undo" feature that
 * allows clients to add and remove pieces efficiently. Does not do any drawing
 * or have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private int[] widths;
	private int[] heights;
	private boolean[][] grid;

	private boolean[][] backupGrid;
	private int[] backupWidths;
	private int[] backupHeights;
	private int backupMaxHeight;

	private boolean DEBUG = true;
	boolean committed;

	private void copy(boolean[][] src, boolean[][] target) {
		for (int x = 0; x < width; x++) {
			System.arraycopy(src[x], 0, target[x], 0, height);
		}
	}

	/**
	 * Creates an empty board of the given width and height measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new boolean[width][height];
		this.backupGrid = new boolean[width][height];
		committed = true;
		this.maxHeight = 0;
		this.backupMaxHeight = 0;
		this.widths = new int[height];
		this.backupWidths = new int[height];
		this.heights = new int[width];
		this.backupHeights = new int[width];
	}

	/**
	 * Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the max y value for a filled in cell in the board. For an empty board
	 * this is 0.
	 */
	public int getMaxHeight() {
		return this.maxHeight;
	}

	private void setMaxHeight(int y) {
		this.maxHeight = y;
	}

	/**
	 * Checks the board for internal consistency -- used for debugging.
	 *
	 * The Board has a lot of internal redundancy between the grid, the widths, the
	 * heights, and maxHeight.
	 *
	 * Write a sanityCheck() method that verifies the internal correctness of the
	 * board structures: iterate over the whole grid to verify that the widths and
	 * heights arrays have the right numbers, and that the maxHeight is correct.
	 * Throw an exception if the board is not sane: throw new
	 * RuntimeException("description").
	 *
	 * Call sanityCheck() at the bottom of place(), clearRows() and undo().
	 *
	 * A boolean static constant DEBUG in your board class should control
	 * sanityCheck(). If DEBUG is true, sanityCheck does its checks. Otherwise it
	 * just returns. Turn your project in with DEBUG=true.
	 *
	 * Put the sanityCheck() code in early. It will help you debug the rest of the
	 * board. There's one tricky case: do not call sanityCheck() in place() if the
	 * placement is bad -- the board may not be in a sane state, but it's allowed to
	 * be not-sane in that case.
	 */
	public void sanityCheck() {
		if (!DEBUG) {
			return;
		}
		int[] newWidths = new int[this.getHeight()];
		int[] newHeights = new int[this.getWidth()];
		int newMaxHeight = 0;
		for (int x = 0; x < this.getWidth(); x++) {
			newHeights[x] = 0;
			for (int y = 0; y < this.getHeight(); y++) {
				if (grid[x][y]) {
					newWidths[y]++;
					if (y > newHeights[x]) {
						newHeights[x] = y;
					}
					if (y > newMaxHeight) {
						newMaxHeight = y;
					}
				}
			}
		}
		for (int x = 0; x < this.getWidth(); x++) {
			if (newHeights[x] != heights[x]) {
				throw new RuntimeException(
						"heights mismatch for x: " + x + ", expected: " + newHeights[x] + ", actual: " + heights[x]);
			}

		}
		for (int y = 0; y < this.getHeight(); y++) {
			if (newWidths[y] != widths[y]) {
				throw new RuntimeException(
						"widths mismatch for y: " + y + ", expected: " + newWidths[y] + ", actual: " + widths[y]);
			}
		}
		if (newMaxHeight != getMaxHeight()) {
			throw new RuntimeException("max height mismatch. expected: " + newMaxHeight + ", actual: " + maxHeight);
		}
	}

	/**
	 * Given a piece and an x, returns the y value where the piece would come to
	 * rest if it were dropped straight down at that x.
	 *
	 * <p>
	 * Implementation: use the skirt and the col heights to compute this fast --
	 * O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int maxY = 0;
		int[] skirt = piece.getSkirt();
		for (int i = 0; i < skirt.length; i++) {
			int restingY = getColumnHeight(x + i) - skirt[i];
			if (restingY > maxY) {
				maxY = restingY;
			}
		}
		return maxY;
	}

	/**
	 * Returns the height of the given column -- i.e. the y value of the highest
	 * block + 1. The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		if (this.heights[x] == 0) {
			if (!getGrid(x, 0)) {
				return 0;
			}
		}
		return this.heights[x] + 1;
	}

	/**
	 * Returns the number of filled blocks in the given row.
	 */
	public int getRowWidth(int y) {
		return this.widths[y];
	}

	/**
	 * Returns true if the given block is filled in the board. Blocks outside of the
	 * valid width/height area always return true.
	 */
	public boolean getGrid(int x, int y) {
		if (y < 0 || y >= height || x < 0 || x >= width) {
			return true;
		}
		;
		return this.grid[x][y];
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 * Attempts to add the body of a piece to the board. Copies the piece blocks
	 * into the board grid. Returns PLACE_OK for a regular placement, or
	 * PLACE_ROW_FILLED for a regular placement that causes at least one row to be
	 * filled.
	 *
	 * must start in committed state must end in uncommitted state update private
	 * int maxHeight; private int[] widths; private int[] heights; private
	 * boolean[][] grid;
	 * <p>
	 * Error cases: A placement may fail in two ways. First, if part of the piece
	 * may falls out of bounds of the board, PLACE_OUT_BOUNDS is returned. Or the
	 * placement may collide with existing blocks in the grid in which case
	 * PLACE_BAD is returned. In both error cases, the board may be left in an
	 * invalid state. The client can use undo(), to recover the valid, pre-place
	 * state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed)
			throw new RuntimeException("place commit problem");
		this.committed = false;

		int result = PLACE_OK;

		for (TPoint point : piece.getBody()) {
			int xToSet = point.x + x;
			int yToSet = point.y + y;
			if (getGrid(xToSet, yToSet)) {
				if (xToSet < 0 || yToSet < 0 || xToSet >= this.getWidth() || yToSet >= this.getHeight()) {
					return Board.PLACE_OUT_BOUNDS;
				}
				return Board.PLACE_BAD;
			}
			grid[xToSet][yToSet] = true;
			if (yToSet > getMaxHeight()) {
				setMaxHeight(yToSet);
			}
			if (yToSet > heights[xToSet]) {
				heights[xToSet] = yToSet;
			}
			widths[yToSet] += 1;
			if (widths[yToSet] == getWidth()) {
				result = Board.PLACE_ROW_FILLED;
			}
		}
		sanityCheck();
		return result;
	}

	/**
	 * second attempt to implement clearRows, perform only a single pass through
	 * grid, and do not utilize extra storage
	 */
	public int clearRows() {
		this.committed = false;
		// find the "row" above which we want all false values
		int initialMaxHeight = this.getMaxHeight();
		int index = copyLevels();
		clearRemainingLevels(index, initialMaxHeight);
		setHeights(index - 1);
		sanityCheck();
		return initialMaxHeight + 1 - index;
	}

	private void setHeights(int maxIndex) {
		maxHeight = 0;
		heights = new int[this.getWidth()];
		for (int y = maxIndex; y > 0; y--) {
			for (int x = 0; x < getWidth(); x++) {
				if (this.getGrid(x, y)) {
					if (y > heights[x]) {
						heights[x] = y;
					}
					if (y > maxHeight) {
						maxHeight = y;
					}
				}
			}
		}
	}

	private int copyLevels() {
		int clearIndex = 0;
		int copyIndex = findNextLevelToCopy(clearIndex);
		while (copyIndex <= this.getMaxHeight()) {
			if (clearIndex != copyIndex) {
				copyLevel(clearIndex, copyIndex);
			}
			clearIndex++;
			copyIndex = findNextLevelToCopy(copyIndex + 1);
		}
		return clearIndex;
	}

	private void copyLevel(int clearIndex, int copyIndex) {
		for (int i = 0; i < this.getWidth(); i++) {
			grid[i][clearIndex] = grid[i][copyIndex];
		}
		widths[clearIndex] = widths[copyIndex];
	}

	private int findNextLevelToCopy(int copyIndex) {
		for (int i = copyIndex; i <= this.getMaxHeight(); i++) {
			if (shouldKeepLevel(i)) {
				return i;
			}
		}
		return this.getHeight();
	}

	private boolean shouldKeepLevel(int y) {
		for (int x = 0; x < this.getWidth(); x++) {
			if (grid[x][y] == false) {
				return true;
			}
		}
		return false;
	}

	private void clearRemainingLevels(int startIndex, int maxIndex) {
		for (int j = startIndex; j <= maxIndex; j++) {
			for (int i = 0; i < this.getWidth(); i++) {
				grid[i][j] = false;
			}
			widths[j] = 0;
		}
	}

	private void backup() {
		copy(grid, backupGrid);
		System.arraycopy(widths, 0, backupWidths, 0, this.getHeight());
		System.arraycopy(heights, 0, backupHeights, 0, this.getWidth());
		backupMaxHeight = this.getMaxHeight();
	}

	private void restoreBackup() {
		copy(backupGrid, grid);
		System.arraycopy(backupWidths, 0, widths, 0, this.getHeight());
		System.arraycopy(backupHeights, 0, heights, 0, this.getWidth());
		this.setMaxHeight(backupMaxHeight);
	}

	/**
	 * Reverts the board to its state before up to one place and one clearRows(); If
	 * the conditions for undo() are not met, such as calling undo() twice in a row,
	 * then the second undo() does nothing. See the overview docs.
	 */
	public void undo() {
		restoreBackup();
		sanityCheck();
		committed = true;
	}

	/**
	 * Puts the board in the committed state.
	 */
	public void commit() {
		backup();
		committed = true;
	}

	/*
	 * Renders the board state as a big String, suitable for printing. This is the
	 * sort of print-obj-state utility that can help see complex state change over
	 * time. (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
}
