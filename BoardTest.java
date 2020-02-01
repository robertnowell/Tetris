package edu.stanford.cs108.tetris;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.cs108.tetris.*;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class BoardTest {
	Piece[] ps;
	Board b1020;
	@Before
	public void setUp() throws Exception {
		ps = Piece.getPieces();
		b1020 = new Board(10, 20);

	}

	/**
	 * 

		One simple strategy is to create a 3 x 6 board, 
			and then place a few rotations of the pyramid in it. 
			
		Call dropHeight() with a few different pieces
			and x values to see that it returns the right thing. 
			
		Call place() one or two times, 
			and then spot check a few qualities of the resulting board, 
			checking the results of calls to getColumnHeight(), getRowWidth(), getMaxHeight(), getGrid(). 
			
		Set up a board with two or more filled rows, 
			call clearRows(), 
			and then spot check the resulting board with the getters. 
		
		Do a place()/clearRows() series, 
			and then an undo() to see that the board gets back to the right state.

		You are free to arrange the unit-test coverage as you like, 
			so long as overall there are at least 
				10 calls each to 
					getColumnHeight() and 
					getRowWidth(), 
				5 calls to 
					dropHeight() and 
					getMaxHeight(), 
			and at least 
				2 calls of
					undo()
					clearRows()
					place()
					getGrid()
					sanityCheck()
		
		Your more advanced Board tests need to be more complex than just placing a single piece in a board. 
			Stack up a few operations, 
				checking a few board metrics (getGrid(), getColumnHeight(), ...) at each stage, 
			like this: 
				Add a pyramid to a board. 
				Add a second shape. 
				Now row clear. 
				Check post row clear that the state is right. 
				Undo, and check the state again. 
				With and without the undo, try adding a third piece 
					to make sure the undo/rowClear operations haven't broken some part of the internal structure. 
				
			When calling getColumnHeight() etc., you don't need to be comprehensive
				just check a couple columns or rows, and 
				that will get the vast majority of bugs. 
			
			Note also that the provided board code includes a toString(), so you can println the Board state, which can be helpful to see a time-series of boards.
	 */
	@Test
	public void testPlaceFullRowSquares() {
		assertEquals(0, b1020.dropHeight(ps[Piece.SQUARE], 0));
		int res = b1020.place(ps[Piece.SQUARE], 0, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(2, b1020.getColumnHeight(0));
		assertEquals(2, b1020.getColumnHeight(1));
		assertEquals(0, b1020.getColumnHeight(2));
		assertEquals(2, b1020.dropHeight(ps[Piece.SQUARE], 0));
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getRowWidth(0));
		assertEquals(2, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));

		res = b1020.place(ps[Piece.SQUARE], 2, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getColumnHeight(2));
		assertEquals(2, b1020.getColumnHeight(3));
		assertEquals(0, b1020.getColumnHeight(4));
		assertEquals(4, b1020.getRowWidth(0));
		assertEquals(4, b1020.getRowWidth(1));

		
		res = b1020.place(ps[Piece.SQUARE], 4, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(6, b1020.getRowWidth(0));
		assertEquals(6, b1020.getRowWidth(1));
		
		res = b1020.place(ps[Piece.SQUARE], 6, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(8, b1020.getRowWidth(0));
		assertEquals(8, b1020.getRowWidth(1));

		res = b1020.place(ps[Piece.SQUARE], 8, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		b1020.commit();
		assertEquals(2, b1020.getColumnHeight(8));
		assertEquals(2, b1020.getColumnHeight(9));
		assertEquals(10, b1020.getRowWidth(0));
		assertEquals(10, b1020.getRowWidth(1));

		res = b1020.place(ps[Piece.SQUARE], 8, 18);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(20, b1020.dropHeight(ps[Piece.SQUARE], 8));
		assertEquals(19, b1020.getMaxHeight());
		assertEquals(20, b1020.getColumnHeight(8));
		assertEquals(20, b1020.getColumnHeight(9));
		assertEquals(0, b1020.getRowWidth(17));
		assertEquals(2, b1020.getRowWidth(18));
		assertEquals(2, b1020.getRowWidth(19));
	}
	
	@Test
	public void testPlaceNonSquare() {
		assertEquals(0, b1020.dropHeight(ps[Piece.L1], 0));
		int res = b1020.place(ps[Piece.L1], 0, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(3, b1020.getColumnHeight(0));
		assertEquals(1, b1020.getColumnHeight(1));
		assertEquals(0, b1020.getColumnHeight(2));
		assertEquals(3, b1020.dropHeight(ps[Piece.L1], 0));
		assertEquals(1, b1020.dropHeight(ps[Piece.L1], 1));
		assertEquals(0, b1020.dropHeight(ps[Piece.L1], 2));
		assertEquals(2, b1020.getMaxHeight());
		assertEquals(2, b1020.getRowWidth(0));
		assertEquals(1, b1020.getRowWidth(1));
		assertEquals(1, b1020.getRowWidth(2));

		res = b1020.place(ps[Piece.L1], 8, 17);

		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(20, b1020.dropHeight(ps[Piece.L1], 8));
		assertEquals(19, b1020.getMaxHeight());
		assertEquals(20, b1020.getColumnHeight(8));
		assertEquals(18, b1020.getColumnHeight(9));
		assertEquals(2, b1020.getRowWidth(17));
		assertEquals(1, b1020.getRowWidth(18));
		assertEquals(1, b1020.getRowWidth(19));
	}

	@Test
	public void testPlaceBad() {
		int res = b1020.place(ps[Piece.SQUARE], 0, 0);
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 0, 0);
		assertEquals(res, Board.PLACE_BAD);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], -1, 0);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 10, 0);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 0, 20);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 0, -1);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 9, 0);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		b1020.undo();
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 0, 19);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);

	}
	
	/**
	 * case
	 * 	levels is 3
	 * 	width  is 2
	 * 	clear only second level
	 */
	@Test
	public void testClear1() {
		Board b = new Board(2, 3);
		Piece s22 = ps[Piece.S2].fastRotation();
		int res = b.place(s22, 0,0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = b.clearRows();
		assertEquals(1, res);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	/*
	clear only first level
	*/
	@Test
	public void testClear2() {
		Board b = new Board(4, 5);
		Piece st1 = ps[Piece.STICK];
		Piece st2 = st1.fastRotation();
		int res = b.place(st1, 0,1);
		assertEquals(Board.PLACE_OK, res);
		b.commit();
		res = b.place(st2, 0, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = b.clearRows();
		assertEquals(1, res);
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getColumnHeight(3));
		assertEquals(3, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
	}

	/*
		* clear only final level
		width is 2
		levels is 3
	*/
	@Test
	public void testClear3() {
		Board b = new Board(2, 3);
		Piece l3 = ps[Piece.L1].fastRotation().fastRotation();
		int res = b.place(l3, 0,0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		b.commit();
		res = b.clearRows();
		assertEquals(1, res);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
/*
		clear when only one level
	* clear when width is large
	* clear when levels is 1 */
	@Test
	public void testClear4() {
		Board b = new Board(24, 1);
		Piece st2 = ps[Piece.STICK].fastRotation();
		for (int i = 0; i < 20; i += 4) {
			int res = b.place(st2, i, 0);
			assertEquals(Board.PLACE_OK, res);
			b.commit();
		}
		int res = b.place(st2, 20, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = b.clearRows();
		assertEquals(1, res);
		for (int i = 0; i < 24; i++) {
			assertEquals(0, b.getColumnHeight(i));
		}
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
	}

	/*
	 * case
	 * 	clear all levels
	 * 	clear two levels in a row
	 * 	two levels total
	 * 	width two
	 * */
	@Test
	public void testClear5() {
		Board b = new Board(2, 2);
		Piece sq1 = ps[Piece.SQUARE];
		int res = b.place(sq1, 0, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = b.clearRows();
		assertEquals(2, res);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(0));
	}

	/*
	 * case clear no levels
	 * */
	@Test
	public void testClear6() {
		Board b = new Board(2, 2);
		int res = b.clearRows();
		assertEquals(0, res);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(0));
	}

	@Test
	public void testDropHeight() {
		Board b = new Board(3, 10);
		Piece pyr1 = ps[Piece.PYRAMID];
		Piece pyr3 = pyr1.fastRotation().fastRotation();
		b.place(pyr1, 0,0);
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(2, b.dropHeight(pyr3, 0));
	}
	/* 
	* clear skip levels
	* clear when levels is large */ 
	@Test
	public void testClear7() {
		Board b = new Board(3, 200);
		Piece pyr1 = ps[Piece.PYRAMID];
		Piece pyr3 = pyr1.fastRotation().fastRotation();
		for (int y = 0; y < 200; y += 2) {
			int res;
			if (y % 10 == 0) {
				res = b.place(pyr3, 0, y);				
			} else {
				res = b.place(pyr1, 0, y);
			}
			assertEquals(Board.PLACE_ROW_FILLED, res);
			b.commit();
		}
		int res = b.clearRows();
		assertEquals(100, res);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(100, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(99, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
	}
	
	@Test
	public void testUndo() {
		assertEquals(0, b1020.dropHeight(ps[Piece.SQUARE], 0));
		int res = b1020.place(ps[Piece.SQUARE], 0, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(2, b1020.getColumnHeight(0));
		assertEquals(2, b1020.getColumnHeight(1));
		assertEquals(0, b1020.getColumnHeight(2));
		assertEquals(2, b1020.dropHeight(ps[Piece.SQUARE], 0));
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getRowWidth(0));
		assertEquals(2, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));

		res = b1020.place(ps[Piece.SQUARE], 2, 0);
		assertEquals(Board.PLACE_OK, res);
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getColumnHeight(2));
		assertEquals(2, b1020.getColumnHeight(3));
		assertEquals(0, b1020.getColumnHeight(4));
		assertEquals(4, b1020.getRowWidth(0));
		assertEquals(4, b1020.getRowWidth(1));

		b1020.undo();
		assertEquals(2, b1020.getColumnHeight(0));
		assertEquals(2, b1020.getColumnHeight(1));
		assertEquals(0, b1020.getColumnHeight(2));
		assertEquals(2, b1020.dropHeight(ps[Piece.SQUARE], 0));
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getRowWidth(0));
		assertEquals(2, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));

		res = b1020.place(ps[Piece.SQUARE], 2, 0);
		assertEquals(Board.PLACE_OK, res);
		assertEquals(1, b1020.getMaxHeight());
		assertEquals(2, b1020.getColumnHeight(2));
		assertEquals(2, b1020.getColumnHeight(3));
		assertEquals(0, b1020.getColumnHeight(4));
		assertEquals(4, b1020.getRowWidth(0));
		assertEquals(4, b1020.getRowWidth(1));
		b1020.commit();

		res = b1020.place(ps[Piece.SQUARE], 4, 0);
		b1020.commit();
		res = b1020.place(ps[Piece.SQUARE], 6, 0);
		assertEquals(Board.PLACE_OK, res);
		b1020.commit();
		assertEquals(8, b1020.getRowWidth(0));
		assertEquals(8, b1020.getRowWidth(1));

		res = b1020.place(ps[Piece.SQUARE], 8, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		assertEquals(2, b1020.getColumnHeight(8));
		assertEquals(2, b1020.getColumnHeight(9));
		assertEquals(10, b1020.getRowWidth(0));
		assertEquals(10, b1020.getRowWidth(1));

		b1020.undo();
		assertEquals(2, b1020.getColumnHeight(6));
		assertEquals(2, b1020.getColumnHeight(7));
		assertEquals(0, b1020.getColumnHeight(8));
		assertEquals(0, b1020.getColumnHeight(9));
		assertEquals(8, b1020.getRowWidth(0));
		assertEquals(8, b1020.getRowWidth(1));

		res = b1020.place(ps[Piece.SQUARE], 8, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		assertEquals(2, b1020.getColumnHeight(8));
		assertEquals(2, b1020.getColumnHeight(9));
		assertEquals(10, b1020.getRowWidth(0));
		assertEquals(10, b1020.getRowWidth(1));
		b1020.commit();

		b1020.clearRows();
		assertEquals(0, b1020.getRowWidth(0));
		assertEquals(0, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));
		assertEquals(0, b1020.getMaxHeight());
		for (int x = 0; x < 10; x++) {
			assertEquals(0, b1020.getColumnHeight(x));
		}

		b1020.undo();
		assertEquals(10, b1020.getRowWidth(0));
		assertEquals(10, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));
		assertEquals(1, b1020.getMaxHeight());
		for (int x = 0; x < 10; x++) {
			assertEquals(2, b1020.getColumnHeight(x));
		}
		
		b1020.clearRows();
		assertEquals(0, b1020.getRowWidth(0));
		assertEquals(0, b1020.getRowWidth(1));
		assertEquals(0, b1020.getRowWidth(2));
		assertEquals(0, b1020.getMaxHeight());
		for (int x = 0; x < 10; x++) {
			assertEquals(0, b1020.getColumnHeight(x));
		}

	}

	@Test
	public void testDropH2() {
		Board b = new Board(3, 6); 
		Piece p1 = ps[Piece.PYRAMID];
		Piece s12 = ps[Piece.S1].fastRotation();
		b.place(p1, 0, 0);
		b.commit();
		System.out.println(b);
		assertEquals(2, b.dropHeight(p1.fastRotation().fastRotation(), 0));
		assertEquals(2, b.dropHeight(p1, 0));
		assertEquals(1, b.dropHeight(s12, 1));
	}
}
