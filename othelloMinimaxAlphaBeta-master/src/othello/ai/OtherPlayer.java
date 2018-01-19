package othello.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import othello.model.Board;

// your AI here. currently will choose first possible move
public class OtherPlayer extends ReversiAI {

	/*@Override
	public Point nextMove(Board b) {
		for (int j = 0; j < size; j++)
			for (int i = 0; i < size; i++)
				if (b.move(i, j))
					return new Point(i, j);
		return null;
	*/
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

    private int depth ;

    //////////////////////////////////////////////////////////instructor
    public OtherPlayer(){depth = 8;}

    //////////////////////////////////////////////////////////getName
    @Override
    public String getName() {
        //IMPORTANT: your student number here
        return new String("9332283");
    }
    //////////////////////////////////////////////////////////nextMove
    @Override
    public Point nextMove(Board prev) {

        Point moveToMake = null ;

        // make a copy of board
        Board b = new Board(prev);

        long startTime = System.nanoTime();

        //call alpha beta search to get the move to make
        moveToMake = alphaBetaSearch(b, this.depth);

        long endTime = System.nanoTime();
        long duration = ((endTime - startTime)/1000000);
        System.out.println("Alpha Beta took " + duration + " milliseconds.");

        return moveToMake;
    }

    /////////////////////////////////////////////////////////alphaBetaSearch
    public Point alphaBetaSearch(Board b, int depth){
        //check if depth reached or end of game
        if (depth <= 0 || b.gameOver()){
            return null;
        }
        else{

            Board bCopy1 = new Board(b);
            //for tracking best score from MAX-Value
            double bestScore = maxValue(bCopy1, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

            //to get the move that had the best score
            Point bestMove = null;

            //generate all moves of current b
            //List<Point> moves = b.generateMoves();
            List<Point> moves = new ArrayList<Point>();

            for (int j = 0; j < b.getSize(); j++) {
                for (int i = 0; i < b.getSize(); i++) {
                    Board bCopy2 = new Board(b);
                    if (bCopy2.move(i, j)) // valid move
                    {
                        Point pathPoint = new Point(i, j);
                        moves.add(pathPoint);
                    }
                }
            }

            for (Point pathPoint : moves){
                Board bCopy3 = new Board(b);
                bCopy3.move((int) pathPoint.getX() , (int) pathPoint.getY());
                double moveScore = minValue(bCopy3, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE); //hold the move's maxValue score
                if (moveScore == bestScore){ //find the move with value of bestScore
                    bestMove = pathPoint;
                }
            }
            return bestMove;
        }
    }


    /////////////////////////////////////////////////////////////maxValue
    public double maxValue(Board prev, int depth, double a, double b){

        //check if depth reached or end of game
        if (depth <= 0 || prev.gameOver()){
            return prev.getScore();
        }
        else {
            double bestScore = Integer.MIN_VALUE; //keep track of best score for Max. Start at negative infinite

            // generate possible moves for this state
            List<Board> moves = new ArrayList<Board>() ;

            for (int j = 0; j < prev.getSize(); j++) {
                for (int i = 0; i < prev.getSize(); i++) {
                    Board bCopy1 = new Board(prev);
                    if (bCopy1.move(i, j)) // valid move
                    {
                        moves.add(bCopy1);
                    }
                }
            }

/*
		for (Board move : moves) {
			if (minValue(move, depth - 1, a, b) > bestScore) { //get the maximum value
				bestScore = minValue(move, depth - 1, a, b);
			}
			if (bestScore < b) {
				bestScore = b;
			}
			a = Math.max(a, bestScore);
		}
		return bestScore;
	}
	*/
            double temp;
            for (Board move : moves) {
                if (b > a) { //get the maximum value
                    temp = minValue(move, depth - 1, a, b);
                    if (temp > bestScore) bestScore = temp;
                }
                else break;
                if (bestScore > a) {
                    a = bestScore;
                }

            }
            return bestScore;
        }
    }


    //////////////////////////////////////////////////////////////minValue
    public double minValue(Board prev, int depth, double a, double b){

        //check if depth reached or end of game
        if (depth <= 0 || prev.gameOver()){
            return prev.getScore();
        }
        else{
            double bestScore = Integer.MAX_VALUE; //keep track of best score for Min. Start at positive infinite

            // generate possible moves for this state
            //List<Board> moves = state.generateMoves();
            List<Board> moves = new ArrayList<Board>() ;

            for (int j = 0; j < prev.getSize(); j++) {
                for (int i = 0; i < prev.getSize(); i++) {
                    Board newMove = new Board(prev);
                    if (newMove.move(i, j)) // valid move
                    {
                        moves.add(newMove);
                    }
                }
            }

/*
		for (Board move: moves){
			if (maxValue(move, depth - 1, a, b) < bestScore){ //get the minimum value
				bestScore = maxValue(move, depth - 1, a, b);
			}
			if (bestScore > a){
				bestScore = a;
			}
			b = Math.min(b, bestScore);
		}
		*/
            double temp;
            for (Board move: moves){
                if (a < b){ //get the minimum value
                    temp = maxValue(move, depth - 1, a, b);
                    if (temp < bestScore) bestScore = temp;
                }
                else break;
                if (bestScore < b){
                    b = bestScore;
                }
            }
            return bestScore;
        }
    }
}

////////////////////////////////////////////////////////////////eval
/*
public double eval(Board b){
	String binaryActiveBoard = "01111110" +"10000001"+"10000001"+"10000001"+"10000001"+"10000001"+"10000001" + "01111110";
	long cornersMask = Long.parseLong(binaryActiveBoard, 2);
	long cornerPieces = b.getActive()
}*/