package edu.stanford.cs108.tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece py1, py2, py3, py4, py5;
	private Piece stick1, stick2, stick3, stick4, stick5;
	private Piece l11, l12, l13, l14, l15;
	private Piece s21, s22, s23;
	private Piece square1, square2;
	private Piece s, sRotated;

	@Before
	public void setUp() throws Exception {
		py1 = new Piece(Piece.PYRAMID_STR);
		py2 = py1.computeNextRotation();
		py3 = py2.computeNextRotation();
		py4 = py3.computeNextRotation();
		py5 = py4.computeNextRotation();

		stick1 = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();
		stick3 = stick2.computeNextRotation();
		stick4 = stick3.computeNextRotation();
		stick5 = stick4.computeNextRotation();

		l11 = new Piece(Piece.L1_STR);
		l12 = l11.computeNextRotation();
		l13 = l12.computeNextRotation();
		l14 = l13.computeNextRotation();
		l15 = l14.computeNextRotation();

		s21 = new Piece(Piece.S2_STR);
		s22 = s21.computeNextRotation();
		s23 = s22.computeNextRotation();

		square1 = new Piece(Piece.SQUARE_STR);
		square2 = square1.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}

	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, py1.getWidth());
		assertEquals(2, py1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, py2.getWidth());
		assertEquals(3, py2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, py1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, py3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	/**
	 * public Piece(TPoint[] points) { public int getWidth() { public int
	 * getHeight() { public int[] getSkirt() { public Piece computeNextRotation() {
	 * public Piece fastRotation() { private static Piece makeFastRotations(Piece
	 * root) { public boolean equals(Object obj) {
	 * 
	 * public static final String STICK_STR = ; public static final String L1_STR =
	 * "0 0 0 1 0 2 1 0"; public static final String L2_STR = "0 0 1 0 1 1 1 2";
	 * public static final String S1_STR = "0 0 1 0 1 1 2 1"; public static final
	 * String S2_STR = "0 1 1 1 1 0 2 0"; public static final String SQUARE_STR = "0
	 * 0 0 1 1 0 1 1"; public static final String PYRAMID_STR = "0 0 1 0 1 1 2 0";
	 */
	@Test
	public void testPieceConstructor() {
		// STICK
		Piece p1 = new Piece(Piece.STICK_STR);
		assertEquals(1, p1.getWidth());
		assertEquals(4, p1.getHeight());
		int[] skirtExp1 = { 0 };
		assert (Arrays.equals(p1.getSkirt(), skirtExp1));

		// L1
		Piece p2 = new Piece(Piece.L1_STR);
		assertEquals(2, p2.getWidth());
		assertEquals(3, p2.getHeight());
		int[] skirtExp2 = { 0, 0 };
		assert (Arrays.equals(p2.getSkirt(), skirtExp2));

		// L2
		Piece p3 = new Piece(Piece.L2_STR);
		assertEquals(2, p3.getWidth());
		assertEquals(3, p3.getHeight());
		int[] skirtExp3 = { 0, 0 };
		assert (Arrays.equals(p3.getSkirt(), skirtExp3));

		// S1
		Piece p4 = new Piece(Piece.S1_STR);
		assertEquals(3, p4.getWidth());
		assertEquals(2, p4.getHeight());
		int[] skirtExp4 = { 0, 0, 1 };
		assert (Arrays.equals(p4.getSkirt(), skirtExp4));

		// S2
		Piece p5 = new Piece(Piece.S2_STR);
		assertEquals(3, p5.getWidth());
		assertEquals(2, p5.getHeight());
		int[] skirtExp5 = { 1, 0, 0 };
		assert (Arrays.equals(p5.getSkirt(), skirtExp5));

		// SQUARE
		Piece p6 = new Piece(Piece.SQUARE_STR);
		assertEquals(2, p6.getWidth());
		assertEquals(2, p6.getHeight());
		int[] skirtExp6 = { 0, 0 };
		assert (Arrays.equals(p6.getSkirt(), skirtExp6));

		// PYRAMID
		Piece p7 = new Piece(Piece.PYRAMID_STR);
		assertEquals(3, p7.getWidth());
		assertEquals(2, p7.getHeight());
		int[] skirtExp7 = { 0, 0, 0 };
		assert (Arrays.equals(p7.getSkirt(), skirtExp7));
	}

	@Test
	public void testPieceComputeNextRotation() {
		// STICK
		assertEquals(4, stick2.getWidth());
		assertEquals(1, stick2.getHeight());
		int[] skirtExp1 = { 0, 0, 0, 0 };
		assert (Arrays.equals(skirtExp1, stick2.getSkirt()));

		assertEquals(1, stick3.getWidth());
		assertEquals(4, stick3.getHeight());
		int[] skirtExp2 = { 0 };
		assert (Arrays.equals(skirtExp2, stick3.getSkirt()));

		assertEquals(4, stick4.getWidth());
		assertEquals(1, stick4.getHeight());
		int[] skirtExp3 = { 0, 0, 0, 0 };
		assert (Arrays.equals(skirtExp3, stick4.getSkirt()));

		assertEquals(1, stick5.getWidth());
		assertEquals(4, stick5.getHeight());
		int[] skirtExp4 = { 0 };
		assert (Arrays.equals(skirtExp4, stick5.getSkirt()));
		assert (Arrays.deepEquals(stick5.getBody(), stick1.getBody()));
	}
	
	@Test
	public void testPieceComputeNextRotation2() {
		// L1
		assertEquals(3, l12.getWidth());
		assertEquals(2, l12.getHeight());
		int[] skirtExpl12 = { 0, 0, 0 };
		assert (Arrays.equals(l12.getSkirt(), skirtExpl12));

		assertEquals(2, l13.getWidth());
		assertEquals(3, l13.getHeight());
		int[] skirtExpl13 = { 2, 0 };
		assert (Arrays.equals(l13.getSkirt(), skirtExpl13));

		assertEquals(3, l14.getWidth());
		assertEquals(2, l14.getHeight());
		int[] skirtExpl14 = { 0, 1, 1 };
		assert (Arrays.equals(l14.getSkirt(), skirtExpl14));

		assertEquals(2, l15.getWidth());
		assertEquals(3, l15.getHeight());
		int[] skirtExpl15 = { 0, 0 };
		assert (Arrays.equals(l15.getSkirt(), skirtExpl15));
		
	}
	@Test
	public void testPieceComputeNextRotation3() {
		// S2
		assertEquals(2, s22.getWidth());
		assertEquals(3, s22.getHeight());
		int[] skirtExpS22 = { 0, 1 };
		assert (Arrays.equals(s22.getSkirt(), skirtExpS22));

		// back to s21
		assertEquals(s21.getWidth(), s23.getWidth());
		assertEquals(s21.getHeight(), s23.getHeight());
		assert (Arrays.equals(s21.getSkirt(), s23.getSkirt()));
		
		// square
		assertEquals(square1.getWidth(), square2.getWidth());
		assertEquals(square2.getWidth(), square2.getHeight());
		assert (Arrays.equals(square1.getSkirt(), square2.getSkirt()));
		
		// pyramid
		assertEquals(2, py2.getWidth());
		assertEquals(3, py2.getHeight());
		int[] skirtExpPy2 = { 1, 0 };
		assert (Arrays.equals(py2.getSkirt(), skirtExpPy2));

		assertEquals(3, py3.getWidth());
		assertEquals(2, py3.getHeight());
		int[] skirtExpPy3 = { 1, 0, 1 };
		assert (Arrays.equals(py3.getSkirt(), skirtExpPy3));

		assertEquals(2, py4.getWidth());
		assertEquals(3, py4.getHeight());
		int[] skirtExpPy4 = { 0, 1 };
		assert (Arrays.equals(py4.getSkirt(), skirtExpPy4));

		assertEquals(py1.getWidth(), py5.getWidth());
		assertEquals(py1.getHeight(), py5.getHeight());
		assert (Arrays.equals(py5.getSkirt(), py1.getSkirt()));
	}
	
		@Test
		public void testPieceComputeNextRotation5() {
		// weird pieces
		// large cross
		/**		 
		 * 		 +
		 *    +++++
		 *    	 +
		 */
		TPoint c1 = new TPoint(1,0);
		TPoint c2 = new TPoint(0,3);
		TPoint c3 = new TPoint(1,1);
		TPoint c4 = new TPoint(1,2);
		TPoint c5 = new TPoint(1,3);
		TPoint c6 = new TPoint(1,4);
		TPoint c7 = new TPoint(2,3);
		
		TPoint[] points = {c1, c2, c3, c4, c5, c6, c7};
		
		Piece lc1 = new Piece(points);
		Piece lc2 = lc1.computeNextRotation();
		Piece lc3 = lc2.computeNextRotation();
		Piece lc4 = lc3.computeNextRotation();
		Piece lc5 = lc4.computeNextRotation();

		assertEquals(3, lc1.getWidth());
		assertEquals(5, lc1.getHeight());
		int[] skirtExpLc1 = { 3, 0, 3 };
		assert (Arrays.equals(lc1.getSkirt(), skirtExpLc1));
		assert (Arrays.deepEquals(lc1.getBody(), points));

		assertEquals(5, lc2.getWidth());
		assertEquals(3, lc2.getHeight());
		int[] skirtExpLc2 = { 1, 0, 1, 1, 1 };
		assert (Arrays.equals(lc2.getSkirt(), skirtExpLc2));

		assertEquals(3, lc3.getWidth());
		assertEquals(5, lc3.getHeight());
		int[] skirtExpLc3 = { 1, 0, 1 };
		assert (Arrays.equals(lc3.getSkirt(), skirtExpLc3));

		assertEquals(5, lc4.getWidth());
		assertEquals(3, lc4.getHeight());
		int[] skirtExpLc4 = { 1, 1, 1, 0, 1 };
		assert (Arrays.equals(lc4.getSkirt(), skirtExpLc4));
		assert (!Arrays.equals(lc4.getBody(), lc1.getBody()));

		assertEquals(lc1.getWidth(), lc5.getWidth());
		assertEquals(lc1.getHeight(), lc5.getHeight());
		assert (Arrays.equals(lc5.getSkirt(), lc1.getSkirt()));
		assert (Arrays.equals(lc5.getBody(), lc1.getBody()));
		
		
		}
		@Test
		public void testPieceComputeNextRotation6() {
			
		// piece: four
		/**
		 * 	  * *
		 *    ***
		 *      *
		 */
		TPoint fp1 = new TPoint(0,0);
		TPoint fp2 = new TPoint(0,2);
		TPoint fp3 = new TPoint(1,0);
		TPoint fp4 = new TPoint(1,1);
		TPoint fp5 = new TPoint(1,2);
		TPoint fp6 = new TPoint(2,2);
		
		TPoint[] fpoints = {fp1, fp2, fp3, fp4, fp5, fp6};
		

		Piece f1 = new Piece(fpoints);
		Piece f2 = f1.computeNextRotation();
		Piece f3 = f2.computeNextRotation();
		Piece f4 = f3.computeNextRotation();
		Piece f5 = f4.computeNextRotation();

		assertEquals(3, f1.getWidth());
		assertEquals(3, f1.getHeight());
		int[] skirtExpF1 = { 0, 0, 2 };
		assert (Arrays.equals(f1.getSkirt(), skirtExpF1));
		assert (Arrays.deepEquals(f1.getBody(), fpoints));

		assertEquals(3, f2.getWidth());
		assertEquals(3, f2.getHeight());
		int[] skirtExpF2 = { 0, 1, 0 };
		assert (Arrays.equals(f2.getSkirt(), skirtExpF2));

		assertEquals(3, f3.getWidth());
		assertEquals(3, f3.getHeight());
		int[] skirtExpF3 = { 0, 0, 0 };
		assert (Arrays.equals(f3.getSkirt(), skirtExpF3));

		assertEquals(3, f4.getWidth());
		assertEquals(3, f4.getHeight());
		int[] skirtExpF4 = { 1, 1, 0 };
		assert (Arrays.equals(f4.getSkirt(), skirtExpF4));
		assert (!Arrays.deepEquals(f1.getBody(), f4.getBody()));

		assertEquals(f5.getWidth(), f1.getWidth());
		assertEquals(f5.getWidth(), f1.getHeight());
		assert (Arrays.equals(f1.getSkirt(), f5.getSkirt()));
		assert (Arrays.deepEquals(f1.getBody(), f5.getBody()));
		
		}
		@Test
		public void testPieceComputeNextRotation7() {
		// piece: triangle
		/**
		 * 	  *
		 *    **
		 *    ***
		 */
		TPoint tp1 = new TPoint(0,0);
		TPoint tp2 = new TPoint(1,0);
		TPoint tp3 = new TPoint(2,0);
		TPoint tp4 = new TPoint(1,1);
		TPoint tp5 = new TPoint(2,1);
		TPoint tp6 = new TPoint(2,2);
		
		TPoint[] tpoints = {tp1, tp2, tp3, tp4, tp5, tp6};

		Piece t1 = new Piece(tpoints);
		Piece t2 = t1.computeNextRotation();
		Piece t3 = t2.computeNextRotation();
		Piece t4 = t3.computeNextRotation();
		Piece t5 = t4.computeNextRotation();

		assertEquals(3, t1.getWidth());
		assertEquals(3, t1.getHeight());
		int[] skirtExpT1 = { 0, 0, 0 };
		assert (Arrays.equals(t1.getSkirt(), skirtExpT1));
		assert (Arrays.deepEquals(t1.getBody(), tpoints));

		assertEquals(3, t2.getWidth());
		assertEquals(3, t2.getHeight());
		int[] skirtExpT2 = { 2, 1, 0 };
		assert (Arrays.equals(t2.getSkirt(), skirtExpT2));

		assertEquals(3, t3.getWidth());
		assertEquals(3, t3.getHeight());
		int[] skirtExpT3 = { 0, 1, 2 };
		assert (Arrays.equals(t3.getSkirt(), skirtExpT3));

		assertEquals(3, t4.getWidth());
		assertEquals(3, t4.getHeight());
		int[] skirtExpT4 = { 0, 0, 0 };
		assert (Arrays.equals(t1.getSkirt(), skirtExpT4));

		assertEquals(t5.getWidth(), t1.getWidth());
		assertEquals(t5.getWidth(), t1.getHeight());
		assert (Arrays.equals(t1.getSkirt(), t5.getSkirt()));
		assert (Arrays.deepEquals(t1.getBody(), t5.getBody()));
	}

	/**
	 * check that the getPieces() structure looks correct and has the right number
	 * of rotations for a couple pieces. * You can use the constants, such as
	 * PYRAMID, to access specific pieces in the array. The pictures on page 2 show
	 * the first "root" rotation of each piece.
		public static final int STICK = 0;
		public static final int L1	  = 1;
		public static final int L2	  = 2;
		public static final int S1	  = 3;
		public static final int S2	  = 4;
		public static final int SQUARE	= 5;
		public static final int PYRAMID = 6;
	 */
	@Test
	public void testGetPieces() {
		Piece[] pieces = Piece.getPieces();
		Piece square1 = pieces[5];
		assert(square1.equals(square1.fastRotation()) && square1.fastRotation().fastRotation().equals(square1));

		Piece _s12 = pieces[3].fastRotation();
		Piece s11 = new Piece(Piece.S1_STR);
		Piece s12 = s11.computeNextRotation();
		assert(s12.equals(_s12));
		
		Piece _stick1 = pieces[0];
		Piece _stick2 = _stick1.fastRotation();
		Piece _stick3 = _stick2.fastRotation();
		Piece _stick4 = _stick3.fastRotation();
		Piece __stick1 = _stick4.fastRotation();
		Piece __stick2 = __stick1.fastRotation();
		Piece __stick3 = __stick2.fastRotation();

		assert(stick3.equals(__stick3));
		

		
		Piece _py4 = pieces[6].fastRotation().fastRotation().fastRotation().fastRotation().fastRotation().fastRotation().fastRotation();
		assert(py4.equals(_py4));
	}

}
