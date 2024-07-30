package kennethHung_4200p3;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.toLowerCase;

public class puzzle {

    private static ArrayList<Pair> pairs = new ArrayList<>();
	private static ArrayList<Pair> finalPairs = new ArrayList<>();
    private static puzzleBoard ticTacBoard;	
	private static int compTime;
	private static long startTime;
	private static boolean winFound = false;

    public static void main(String[] args)
	{
        boolean computerAgent = true; // true if computer goes first, false if human goes first
        compTime = 5; // seconds
        Scanner userInput = new Scanner(System.in);

        System.out.println("Welcome to the tic-tac-toe puzzle");

        System.out.println("Would you like the computer to go first? (y/n)");
        String firstInput = userInput.nextLine();

        while (!(firstInput.equals("y") || firstInput.equals("n"))) {
            System.out.println("Please choose either option y or n");
            System.out.println("Would you like the computer to go first? (y/n)");
            firstInput = userInput.nextLine();
        }
        if(firstInput.equals("n")) {
            computerAgent = false;
        }

        ticTacBoard = new puzzleBoard(computerAgent);
		ticTacBoard.print();

        // moves continue as long as there's no winner
        boolean compWins = false;

        while (!ticTacBoard.currentState.winner()) {
			//print move and empty board state
			if (computerAgent) {
				Move computerMove = computerTurn();
				ticTacBoard.currentState.board[computerMove.row - 1][computerMove.column - 1] = 1;
				ticTacBoard.addMove(computerMove);

				//checks for victory
				ticTacBoard.print();
				if (ticTacBoard.currentState.winner(true)) {
					compWins = true;
					break;
				}
			}

			else if (!ticTacBoard.currentState.isEmpty()) {
				Move computerMove = computerTurn();
				ticTacBoard.currentState.board[computerMove.row - 1][computerMove.column - 1] = 1;
				ticTacBoard.addMove(computerMove);

				//case for victory
				ticTacBoard.print();
				if (ticTacBoard.currentState.winner(true)) {
					compWins = true;
					break;
				}
			}

			//player move
			int[] move = playerTurn(ticTacBoard.currentState.board);
			ticTacBoard.currentState.board[move[0]][move[1]] = 2;
			ticTacBoard.addMove(new Move(2, move[0] + 1, move[1] + 1));
			ticTacBoard.print();
		}

		if (compWins) { //computer win
			System.out.println("Computer wins!");
			System.out.println("Game Over!");
            userInput.close();
		}
		else { //player win
			System.out.println("Player wins!");
			System.out.println("Game Over!");
            userInput.close();
		}
    }

    //Timer function
	private static double timer() {
		return (((System.nanoTime() - startTime))/1000000000.0);
	}
	
	//Function to see if we've reached the max amount of seconds.
	private static boolean outOfTime() {
		return timer() > compTime;
	}

    //check move legality
	//needs to be in the board and not occupied
	private static boolean isLegal(int row, int column, int[][] board) {
		//check bounds + case for empty board
		if ((row < 0 || row >= 8 || column < 0 || column >= 8) && (row == -1 && column == -1)) {
			return false;
		}

		//check for bounds
		if (row < 0 || row >= 8 || column < 0 || column >= 8) {
			System.out.println("Error: space is out of bounds");
			return false;
		}

		//check for occupied
		if (board[row][column] == 1 || board[row][column] == 2) {
			System.out.println("Error: Space already occupied. Please try another space.");
			return false;
		}
	
		return true;
	}

    @SuppressWarnings("resource")
	//takes player turn
    private static int[] playerTurn(int[][] board) {
		int row = -1;
		int column = -1;

		while (!isLegal(row, column, board)) {
			String moveIn = "";
			while(moveIn.length() != 2) {
				System.out.print("Choose your move: ");
				moveIn = new Scanner(System.in).nextLine();
			}
			System.out.println();
			//Cheeky fix to allow for lowercase values
			row = (int) toLowerCase(moveIn.charAt(0)) - 97;
			column = getNumericValue(moveIn.charAt(1)) - 1;
		}
		return new int[]{row, column};
	}	

	//computer turn
	private static Move computerTurn() {
		search(ticTacBoard.currentState);
		return getBestMove();
	}

    private static Move getBestMove()
	{
		state bestState = finalPairs.get(0).currentState;
		int bestValue = finalPairs.get(0).value;
		for (Pair pair : finalPairs) {
			if (pair.value > bestValue) {
				bestValue = pair.value;
				bestState = pair.currentState;
			}
			
			else if (pair.value == bestValue && pair.currentState.computerComparer() > bestState.computerComparer()) {
				bestState = pair.currentState;
			}
		}
		Move bestMove = ticTacBoard.currentState.getMove(bestState);

		return bestMove;
	}

    //search for best move
	private static void search(state root)
	{
		startTime = System.nanoTime();
		for (int i = 1; i <= 500; i++) {
			pairs.clear();
			int result = alphaBeta(root, root, i, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

			//terminate when timer reached
			if (result == -1) {
				System.out.println("Max time reached");
				break;
			}

			//Goes through as many depths as possible in order to get the best option
			finalPairs.clear();
			for (Pair pair : pairs) {
				finalPairs.add(new Pair(pair));
			}
			if (finalPairs.size() > 0) {
					//stare into the depths
					//System.out.println("Depth " + i + ": " + timer() + " seconds");
				}
			if (winFound) {
				winFound = false;
				break;
			}
		}
		System.out.printf("Optimal move found in: %.3f Seconds\n", timer());
		System.out.println();
	}

    //alpha beta
	private static int alphaBeta(state root, state currentState, int depth, int alpha, int beta, boolean computer) {
		if (currentState.winner(!computer)) {
			winFound = true;
			if (currentState.isChild(root, !computer)) {
					pairs.add(new Pair(999999999, currentState)); //all the points
				}

			return currentState.computerComparer();
		}

		if (depth == 0) {
			if (currentState.isChild(root, !computer)) {
					pairs.add(new Pair(currentState.computerComparer(), currentState));
				}

			return currentState.computerComparer();
		}

		//Using our list of next moves
		ArrayList<state> successors = currentState.successors(computer);

		int value;
		//If and else statement for whether or not the current player is a computer
		if (computer) {
			value = Integer.MIN_VALUE; //reset
			for (state child : successors) {
				value = Math.max(value, alphaBeta(root, child, depth - 1, alpha, beta, false));
				alpha = Math.max(alpha, value);

				if (outOfTime()) {
                    return -1;
                }
				if (alpha >= beta) { //snip snip
                    break;
                }
			}
		} else {
			value = Integer.MAX_VALUE;
			for (state child : successors) {
				value = Math.min(value, alphaBeta(root, child, depth - 1, alpha, beta, true));
				beta = Math.min(beta, value);

				if (outOfTime()) {
					return -1;
				}
				if (alpha >= beta) {
					break;
				}
			}
		}

		if (currentState.isChild(root, !computer))
		pairs.add(new Pair(value, currentState));

		return value;
	}

}

//Class for pair. Used to easily find any pairs
class Pair {
    int value;
    state currentState;

    public Pair(int value, state currentState) {
        this.value = value;
        this.currentState = currentState;
    }

    public Pair(Pair copyPair) {
        this.value = copyPair.value;
        this.currentState = new state(copyPair.currentState);
    }
}

//defines a move on the board (X, O) and placement
class Move {
    @SuppressWarnings("unused")
    private int marker;
    int row;
    int column;

    public Move(int marker, int row, int column) {
        this.marker = marker;
        this.row = row;
        this.column = column;
    }

    //uses ASCII val to return row + column defined as a move ex. A5
    public String translator() {
        return (char) (65 + row - 1) + Integer.toString(column);
    }
}
