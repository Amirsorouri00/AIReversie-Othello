package othello.ai;

import java.awt.*;
import java.util.List;
import java.util.Vector;

import othello.model.Board;

// your AI here. currently will choose first possible move
public class MyPlayerAI extends ReversiAI {

    public List generateMoves(Board state) {
        List<Point> pointList = new Vector<>();

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                Board tempState = new Board(state);
                if (tempState.move(i, j))
                    pointList.add(new Point(i, j));
            }
        }
        return pointList;
    }

    public OthelloMove getMove(OthelloState state) {
        //
        // Time the search
        //
        OthelloMove moveToMake = null; //used to let timer be reachable
        long startTime = System.nanoTime();

        //call minimax search to get the move to make
        moveToMake = minimax(state, this.depth);

        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println("Minimax took " + duration + " milliseconds.");

        return moveToMake;
    }


    public Point minimax(Board state, int depth) {
        //check if depth reached or end of game
        if (depth <= 0 || state.gameOver()) {
            return null;
        } else {
            //for tracking best score from Min-Value
            //use negative infinite since we want the maximum minValue
            double currentScore = Integer.MIN_VALUE;

            //to get the move that had the best score
            Point currentMove = null;
            List<Point> moves = generateMoves(state);
            //generate all moves of current state

            for (Point move : moves) {
                Board tempBoard = new Board(state);
                tempBoard.move(move.x, move.y); //apply move into clone
                // Point newState = state.applyMoveCloning(move); //apply move into clone
                double moveScore = minValue(tempBoard, depth); //hold the move's minValue score
                if (moveScore > currentScore) { //always use the highest minValue
                    currentScore = moveScore;
                    currentMove = move;
                }
            }
            return currentMove;
        }
    }


    // return utility value of Max (this player)
//    public double maxValue(OthelloState state, int depth) {
//        //check if depth reached or end of game
//        if (depth <= 0 || state.gameOver()) {
//            return state.evaluation();
//        } else {
//            double bestScore = Integer.MIN_VALUE; //keep track of best score for Max. Start at negative infinite
//            List<OthelloMove> moves = state.generateMoves(); //generate possible moves for this state
//            for (OthelloMove move : moves) {
//                OthelloState moveState = state.applyMoveCloning(move); //apply move into state clone
//                if (minValue(moveState, depth - 1) > bestScore) { //get the maximum value
//                    bestScore = minValue(moveState, depth - 1);
//                }
//            }
//            return bestScore;
//        }
//    }
//
//
//    //return utility value of Min (opponent)
//    public double minValue(OthelloState state, int depth) {
//        //check if depth reached or end of game
//        if (depth <= 0 || state.gameOver()) {
//            return state.evaluation();
//        } else {
//            double bestScore = Integer.MAX_VALUE; //keep track of best score for Min. Start at positive infinite
//            List<OthelloMove> moves = state.generateMoves(); //generate possible moves for this state
//            for (OthelloMove move : moves) {
//                OthelloState moveState = state.applyMoveCloning(move); //apply move into state clone
//                if (maxValue(moveState, depth - 1) < bestScore) { //get the minimum value
//                    bestScore = maxValue(moveState, depth - 1);
//                }
//            }
//            return bestScore;
//        }
//    }
//

    @Override
    public Point nextMove(Board b) {
        for (int j = 0; j < size; j++)
            for (int i = 0; i < size; i++)
                if (b.move(i, j))
                    return new Point(i, j);
        return null;

		/*{
			b.isCapturedByMe(x, y);					// square (x, y) is mine
			b.isCapturedByMyOppoenet(x, y);			// square (x, y) is for my opponent
			b.isEmptySquare(x, y);					// square (x, y) is empty
			b.move(x, y);							// attempt to place a piece at specified coordinates, and update
													// the board appropriately, or return false if not possible
			b.turn();								// end current player's turn
			b.print();								// ASCII printout of the current board
			if(b.getActive() == Board.WHITE)		//I am White
			if(b.getActive() == Board.BLACK)		//I am Black
			
			b.getMoveCount(true);					//number of possible moves for me
			b.getMoveCount(false);					//number of possible moves for my opponent
			b.getTotal(true);						//number of cells captured by me
			b.getTotal(false);						//number of cells captured by my opponent
			this.size;								//board size (always 8)
		}*/
    }

    @Override
    public String getName() {
        //IMPORTANT: your student number here
        return new String("9300000");
    }
}
