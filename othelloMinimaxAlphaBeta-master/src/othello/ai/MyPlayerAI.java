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

    public Point miniMax(Board state, int depth) {
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
                double moveScore = minValue(tempBoard, depth, -999999, state); //hold the move's minValue score
                if (uncheck(state, move)){
                    moveScore-=300000;
                }
                if (moveScore > currentScore) { //always use the highest minValue
                    currentScore = moveScore;
                    currentMove = move;
                }
                if (check(move)){
                    //                    currentScore = moveScore;
//                    currentMove = move;
                    return move;
                }
            }
            if (currentMove == null){
                currentMove = moves.get(0);
            }
            return currentMove;
        }
    }

    public double maxValue(Board state, int depth, double pass, Board origin) {
        //check if depth reached or end of game
        double a1 = -99999999;
        if (depth <= 0 || state.gameOver() || ((state.getMoveCount(true) == 0) && (state.getMoveCount(false) == 0))) {
            return (getScore(state, origin));
        } else {
            int counter = 0;
            double bestScore = Integer.MIN_VALUE; //keep track of best score for Max. Start at negative infinite
            List<Point> moves = generateMoves(state); //generate possible moves for this state
            for (Point move : moves) {
                if (check(move)){
                    continue;
                }
                Board moveState = this.applyMoveCloning(move, state); //apply move into state clone
                double s = minValue(moveState, depth - 1, pass, origin);
                if (counter == 0){
                    pass = s;
                    counter++;
                }
                else {
                    if (s > pass) {
                        return pass;
                    }else {

                        pass = s;
                    }
                }
                if (s > bestScore) {
                    bestScore = s;
                }
            }
            return bestScore;
        }
    }

    /*
     * Creates a new game state that has the result of applying move 'move'
     */
    //return utility value of Min (opponent)
    public double minValue(Board state, int depth, double pass, Board origin) {
        //check if depth reached or end of game
        if (depth <= 0 || state.gameOver() || ((state.getMoveCount(true) == 0) && (state.getMoveCount(false) == 0))) {
            return (getScore(state, origin));
        } else {
            int counter = 0;
            double bestScore = Integer.MAX_VALUE; //keep track of best score for Min. Start at positive infinite
            //List<OthelloMove> moves = state.generateMoves(); //generate possible moves for this state
            List<Point> moves = this.generateMoves(state);
            for (Point move : moves) {
                Board moveState = this.applyMoveCloning(move, state); //apply move into state clone
                double s = maxValue(moveState, depth - 1, pass, origin);

                if (counter == 0){
                    pass = s;
                    counter++;
                }
                else {
                    if (s > pass) {
                        return pass;
                    } else {

                        pass = s;
                    }
                }
                if (s < bestScore) { //get the minimum value
                    bestScore = s;
                }

            }
            return bestScore;
        }
    }

    @Override
    public Point nextMove(Board b) {
        long startTime = System.nanoTime();
        Point t = miniMax(b, 2);
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println("Minimax took " + duration + " milliseconds.");
        return t;
    }

    @Override
    public String getName() {
        //IMPORTANT: your student number here
        return new String("9327303");
    }

    public Board applyMoveCloning(Point move, Board state) {
        Board newState = clone(state);
        newState.move(move.x, move.y);
        return newState;
    }

    /*
     * Makes a copy of a game state
     */
    public Board clone(Board state) {
        Board newState = new Board(state);
        return newState;
    }

    public int getScore(Board state, Board origin) {
        //return active_count - state.inactive_count;
        int result;

        /*
            ------------- UP Side ----------------
         */
        //int u1 = scoreTwoSix(state, 0, 2, 6, 20);
        //result+=u1;

        //int u2 = scoreAngles(state, 0, 1, 6, 20);
        //result+=u2;

        /*
            ------------- Down Side ----------------
         */
        //int d1 = scoreTwoSix(state, 7, 2, 6, 20);
        //result+=d1;

        //int d2 = scoreAngles(state, 7, 1, 6, 20);
        //result+=d2;

        /*
            ------------- Left Side ----------------
         */
        //int l1 = scoreTwoSix(state, 0, 2, 6, 10);
        //result+=l1;

        //int l2 = scoreAngles(state, 0, 1, 6, 10);
        //result+=l2;

        /*
            ------------- Right Side ----------------
         */
        int r1 = scoreTwoSix(state, 7, 2, 6, 10);
        //result+=r1;

        int r2 = scoreAngles(state, 7, 1, 6, 10);
        //result+=r2;

        /*
            ------------- Middle Side ----------------
         */
/*
        int tmp = 0;
        int tmp1 = 0;
        for (int i = 1; i<=6; i++){
            for (int j = 1; j<=6; j++){
                if ( /*i == 1 || i == 6 || j == 1 || j == 6 true){
                    if (state.isCapturedByMe(i,j) == true){
                        tmp++;
                    }
                    else if(state.isCapturedByMyOppoenet(i, j) == true){
                        tmp1++;
                    }
                }
            }
        }
        int m = tmp - tmp1;
        result+= (m);
        int mobilityH = (state.getMoveCount(true) - state.getMoveCount(false) );
        result+=20*mobilityH;
*/
        /* ------------------------------------- Board Changed state ------------------------------------------ */
        List<Integer> stateAfter = scoreEval(state);
        int numAf = stateAfter.get(0);
        int moveAf = stateAfter.get(1);
        int cornerAf = stateAfter.get(2);
        int sideAf = stateAfter.get(3);

        /* ------------------------------------- Board Current state ------------------------------------------ */
        List<Integer> stateBefore = scoreEval(origin);
        int numBf = stateBefore.get(0);
        int moveBf = stateBefore.get(1);
        int cornerBf = stateBefore.get(2);
        int sideBf = stateBefore.get(3);

        int num = numAf - numBf;
        int move = moveAf - moveBf;
        int corner = cornerAf - cornerBf;
        int side = sideAf - sideBf;
        result = 40*num + 20*move + 500*corner + 400*side;

        /*if (func(tempb)){
            tot-=1500;
        }*/
        return result;
    }

    public int scoreTwoSix(Board state, int stati, int minDynam, int maxDynam, int manage){
        int tmp = 0;
        int tmp1 = 0;
        for (int j = minDynam; j < maxDynam; j++) {
            for (int i = stati; i == stati; i++) {
                /*
                 * Left Or Right
                 * */
                if (manage == 10) {
                    if (state.isCapturedByMe(j, i) == true) {
                        //
                        tmp++;
                    } else if (state.isCapturedByMyOppoenet(j, i) == true) {
                        //
                        tmp1++;
                    }
                }
                /*
                 * Up Or Down
                 * */
                else if (manage == 20) {
                    if (state.isCapturedByMe(i, j) == true) {
                        //
                        tmp++;
                    } else if (state.isCapturedByMyOppoenet(i, j) == true) {
                        //
                        tmp1++;
                    }
                }
            }
        }
        int result = tmp - tmp1;
        return (6 * result);
    }

    public int scoreAngles(Board state, int stati, int firstAngle, int lastAngle, int manage){
        int tmp = 0;
        int tmp1 = 0;
        for (int j = firstAngle; j==firstAngle || j==lastAngle; j+=(lastAngle - firstAngle)) {
            for (int i = stati; i == stati; i++) {
                /*
                * Left Or Right
                * */
                if (manage == 10) {
                    if (state.isCapturedByMe(j,i) == true){
                        //
                        tmp++;
                    }
                    else if (state.isCapturedByMyOppoenet(j,i) == true){
                        //
                        tmp1++;
                    }
                }
                /*
                * Up Or Down
                * */
                else if (manage == 20) {
                    if (state.isCapturedByMe(i,j) == true){
                        //
                        tmp++;
                    }
                    else if (state.isCapturedByMyOppoenet(i,j) == true){
                        //
                        tmp1++;
                    }
                }
            }
        }
        int result = tmp - (tmp1);
        return (50 * result);
    }

    public List<Integer> scoreEval(Board state){
        Board board = new Board(state);
        int cornerM = 0;
        int cornerO = 0;
        int sideM = 0;
        int sideO = 0;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                if ((x == 0 || x == size - 1) && (y == 1 || y == size - 2)) {
                    if (board.isCapturedByMe(x, y)) {
                        cornerM ++;
                    }
                    else if (board.isCapturedByMyOppoenet(x, y)) {
                        cornerO ++;
                    }
                }
                else if ((x == 1 || x == size - 2) && (y == 0 || y == size - 1)){
                    if (board.isCapturedByMe(x, y)) {
                        cornerM ++;
                    }
                    else if (board.isCapturedByMyOppoenet(x, y)) {
                        cornerO ++;
                    }
                }
                else if ((x == 0 || x == size - 1) && (y == 2 || y == size - 3));
                else if ((x == 2 || x == size - 3) && (y == 0 || y == size - 1));
                else if ((x == 1 || x == size - 2) && (y == 0 || y == size - 1)){
                    if (board.isCapturedByMe(x, y)) {
                        cornerM ++;
                    }
                    else if (board.isCapturedByMyOppoenet(x, y)) {
                        cornerO ++;
                    }
                }
                if ((x == 0 || x == size - 1) || (y == 0 || y == size - 1)){
                    if (board.isCapturedByMe(x, y)) {
                        sideM ++;
                    }
                    else if (board.isCapturedByMyOppoenet(x, y)) {
                        sideO ++;
                    }
                }
            }
        }

        int myDiff = (board.getTotal(true) - board.getTotal(false) );
        int moveDiff = (board.getMoveCount(true) - board.getMoveCount(false) );
        int cornerDiff = (cornerM - cornerO);
        int sideDiff = (sideM - sideO);

        List<Integer> evaluations = new Vector<>();

        evaluations.add(myDiff);
        evaluations.add(moveDiff);
        evaluations.add(cornerDiff);
        evaluations.add(sideDiff);

        return evaluations;
    }

    public boolean check(Point state){
        if ((state.x == 0 && state.y == 1) || (state.x == 0 && state.y == 6) || (state.x == 7 && state.y == 1)
                || (state.x == 7 && state.y == 6) || (state.x == 1 && state.y == 0) || (state.x == 6 && state.y == 0)
                || (state.x == 1 && state.y == 7) || (state.x == 6 && state.y == 7)){
            return true;
        }
        return false;
    }

    public boolean uncheck(Board b, Point state){
        if ((state.x == 1 && state.y==1) && (!(state.x == 0 && state.y==1) || !(state.x == 1 && state.y==0)) ||
                (state.x == 1 && state.y==6) && (!(state.x == 1 && state.y==7) || !(state.x == 0 && state.y==6)) ||
                (state.x == 6 && state.y==1) && (!(state.x == 7 && state.y==1) || !(state.x == 6 && state.y==0)) ||
                (state.x == 6 && state.y==6) && (!(state.x == 7 && state.y==6) || !(state.x == 6 && state.y==7))){
            return true;
        }
        if (((state.x == 1 && state.y == 2) && b.isEmptySquare(0,0)) ||
                ((state.x == 2 && state.y == 1) && b.isEmptySquare(1,0)) ||
                ((state.x == 1 && state.y == 5) && b.isEmptySquare(0,6)) ||
                ((state.x == 2 && state.y == 6) && b.isEmptySquare(1,7)) ||
                ((state.x == 5 && state.y == 1) && b.isEmptySquare(6,0)) ||
                ((state.x == 6 && state.y == 2) && b.isEmptySquare(7,1)) ||
                ((state.x == 5 && state.y == 6) && b.isEmptySquare(6,7)) ||
                ((state.x == 6 && state.y == 5) && b.isEmptySquare(7,6))){
            return true;
        }
        if (((state.x == 2 && state.y == 0) && b.isEmptySquare(1,0)) ||
                ((state.x == 5 && state.y == 0) && b.isEmptySquare(6,0)) ||
                ((state.x == 0 && state.y == 2) && b.isEmptySquare(0,1)) ||
                ((state.x == 0 && state.y == 5) && b.isEmptySquare(0,6)) ||
                ((state.x == 7 && state.y == 2) && b.isEmptySquare(7,1)) ||
                ((state.x == 7 && state.y == 5) && b.isEmptySquare(7,6)) ||
                ((state.x == 2 && state.y == 7) && b.isEmptySquare(1,7)) ||
                ((state.x == 5 && state.y == 7) && b.isEmptySquare(1,7))){
            return true;
        }
        return false;
    }

    public boolean func(Board state){
        for (int x = 0; x<size; x++){
            for (int y = 0; y<size; y++){
                if (state.isCapturedByMe(x, y)){
                    if ((x == 1 && y==1) && (!(x == 0 && y==1) || !(x == 1 && y==0)) ||
                            (x == 1 && y==6) && (!(x == 1 && y==7) || !(x == 0 && y==6)) ||
                            (x == 6 && y==1) && (!(x == 7 && y==1) || !(x == 6 && y==0)) ||
                            (x == 6 && y==6) && (!(x == 7 && y==6) || !(x == 6 && y==7))){
                        return true;
                    }
                    if (((x == 1 && y == 2) && state.isEmptySquare(0,0)) ||
                            ((x == 2 && y == 1) && state.isEmptySquare(1,0)) ||
                            ((x == 1 && y == 5) && state.isEmptySquare(0,6)) ||
                            ((x == 2 && y == 6) && state.isEmptySquare(1,7)) ||
                            ((x == 5 && y == 1) && state.isEmptySquare(6,0)) ||
                            ((x == 6 && y == 2) && state.isEmptySquare(7,1)) ||
                            ((x == 5 && y == 6) && state.isEmptySquare(6,7)) ||
                            ((x == 6 && y == 5) && state.isEmptySquare(7,6))){
                        return true;
                    }
                    if (((x == 2 && y == 0) && state.isEmptySquare(1,0)) ||
                            ((x == 5 && y == 0) && state.isEmptySquare(6,0)) ||
                            ((x == 0 && y == 2) && state.isEmptySquare(0,1)) ||
                            ((x == 0 && y == 5) && state.isEmptySquare(0,6)) ||
                            ((x == 7 && y == 2) && state.isEmptySquare(7,1)) ||
                            ((x == 7 && y == 5) && state.isEmptySquare(7,6)) ||
                            ((x == 2 && y == 7) && state.isEmptySquare(1,7)) ||
                            ((x == 5 && y == 7) && state.isEmptySquare(1,7))){
                        return true;
                    }

                }
            }
        }
        return false;
    }

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
