package edu.stanford.cs108.tetris;

public class TetrisBrainLogic extends TetrisLogic {
    DefaultBrain brain;
    boolean useBrain;

    public void setBrainMode(boolean val){
        this.useBrain = val;
    }

    public TetrisBrainLogic(TetrisUIInterface uiInterface) {
        super(uiInterface);
        brain = new DefaultBrain();
    }

    /**
     * The strategy is to override tick(),
     *  so that every time the system calls tick(DOWN)
     *  to move the piece down one, TetrisBrainLogic
     *  takes the opportunity to move the piece a bit first.
     *  Our rule is that the brain may do up to one rotation
     *  and one left/right move each time tick(DOWN) is called:
     *  rotate the piece one rotation and move it left or right one position.
     *
     *  With the brain on, the piece should drift down to its correct place.
     *      After the brain does its changes, the tick(DOWN) should have its usual
     *      effect of trying to lower the piece by one.
     *
     *      So on each tick, the brain will move the piece a little,
     *          and the piece will drop down one row.
     *
     *      The user should still be able to use the keyboard to move the piece around while the brain is playing,
     *          but the brain will move the piece back on course.
     *
     *      As the board gets full, the brain may fail to get the piece over fast enough.
     *          That's ok.

     * • When a tick occurs, it should use the brain to compute,
     *      where the brain says the piece should go -- the "goal".
     *      The brain needs the board in a committed state before doing its computation.
     *
     *      Therefore, do a board.undo() before using the brain.
     *      You can see that this won't disrupt the TetrisLogic logic,
     *      since TetrisLogic.tick() does board.undo() itself.
     * @param verb
     */
    @Override
    protected void tick(int verb) {
        if (verb == DOWN && useBrain){
            board.undo();
            Brain.Move goal = this.brain.bestMove(board, currentPiece, HEIGHT, null);
            // TODO issue: only one of the two between rotate and left/right move will occur with each down tick, but both should occur
            if (goal != null){
                if (goal.x != currentX){
                    super.tick(goal.x < currentX ? LEFT : RIGHT);
                }
                if (!goal.piece.equals(currentPiece)){
                    super.tick(ROTATE);
                }
            }
        }
        super.tick(verb);
    }
}
