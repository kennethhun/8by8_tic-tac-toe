package kennethHung_4200p3;

import java.util.ArrayList;
import java.util.Arrays;

public class state {
    int[][] board;

	public state()
	{
		board = new int[8][8];
	}

	private state(int[][] board)
	{
		this.board = board;
	}

	public state(state copyState)
	{
		this.board = Arrays.stream(copyState.board).map(r -> r.clone()).toArray(int[][]::new);
	}

	//checks for current agent, human or computer
	public int isComputer(boolean computer) {
		if (computer) {
			return 1; //computer is agent
		} else {
			return 2; //human is agnet
		}	
	}

	//Compares the computer's and players scores
	public int computerComparer()
	{
		return (checker(true) - checker(false));
	}

	//Our perverbial hat-trick. This function will figure out whether the current player has won, as well as the heuristic values for the AI
	private int checker(boolean computer)
	{
		//Our heuristic value. Will change depending on board state, as well as whether or not the computer winners.
		int heuristic = 0;

		int player = isComputer(computer);

		//Checks for a winner, if computer winners, rewards them generously
		if (winner(computer))
		heuristic += 1000000000;

		//Goes through each column and row to check for empty and occupied spaces
		//need to check up, down, left and right 3 spaces
		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == 0)
				{
					// 0 = up, 1 = down, 2 = left, 3 = right
					int[] empty = new int[4]; 
					int[] yours = new int[4];
					int[] enemy = new int[4];
					//boolean[] isOccupied = new boolean[4];

					//Checks 4 directions by 3 spaces
					for (int i = 1; i <= 3; i++)
					{
						if (row - i >= 0) { // in bounds
							//======================================
							//going up
							int next = board[row - i][column];
							//empty
							if (next == 0) {
								empty[0] += 1;
							}
							//enemy
							else if (next != player) {
								enemy[0] +=1;
								//isOccupied[0] = true;
							}
							//you silly
							else {
								yours[0] +=1;
							}
						}

						if (row + i < 8) { // In bounds?
							//======================================
							//going down
							int next = board[row + i][column];
							//If there is nothing, increment empty spaces
							if (next == 0) {
								empty[1] += 1;
							}
							//If there's something there' it's the enemy
							else if (next != player) {
								enemy[1] +=1;
								//isOccupied[1] = true;
							}
							//It's your space
							else {
								yours[1] +=1;
							}
						}

						if (column - i >= 0) {
							//======================================
							//going left
							int next = board[row][column - i];
							//empty
							if (next == 0) {
								empty[2]++;
							}
							//enemy
							else if (next != player) {
								enemy[2]++;
								//isOccupied[2] = true;
							}
							//yours
							else {
								yours[2]++;
							}
						}

						if (column + i < 8) {
							//======================================
							//going right
							int next = board[row][column + i];
							//empty
							if (next == 0) {
								empty[3]++;
							}
							//enemy
							else if (next != player) {
								enemy[3]++;
								//isOccupied[3] = true;
							}
							//yours
							else {
								yours[3]++;
							}
						}
					}
					//Takes the data we just learned to add points to the heuristic
					for(int i = 0; i < 4; i++) {
						heuristic += heuristicCalc(empty[i], yours[i], enemy[i]);
					}
				}
			}
		}

		return heuristic;
	}

		//Returns who the winner is
		public boolean winner() {
			return winner(true) || winner(false);
		}
	
		//Takes in the whether or not the current player is a computer, Will then return
		public boolean winner(boolean computer) {
			boolean winner = false;
	
			int player = isComputer(computer); // determine if we're working with the player or computer
	
			for (int row = 0; row < 8; row++) {
				for (int column = 0; column < 8; column++) {
					int currentSpace = board[row][column];
	
					if (currentSpace == player) {
						//up and down wins
						if (row <= 8 - 4 && board[row + 1][column] == player && 
							board[row + 2][column] == player && 
							board[row + 3][column] == player) {
	
							winner = true;
						}
						//left and right wins
						if (column <= 8 - 4 && board[row][column + 1] == player && 
							board[row][column + 2] == player && 
							board[row][column + 3] == player) {
								
							winner = true;
						}
					}
				}
			}
	
			return winner;
		}

	//calculates our heuristic. Our program wants as many points as possible
	private static int heuristicCalc(int emptySpaces, int yourSpaces, int enemySpaces) {
		if (yourSpaces == 3) { //victory move
			return 1000000; // the higher the value of that state, the higher the number returned
		}

		else if (yourSpaces == 2) { //halfway there
			if (enemySpaces == 0) { //enemy not in way
				return 1000;
			} else { //roadblock
				return 100;
			}
		}
		
		else if (yourSpaces == 1)//just started your path to victory
		{
			if (enemySpaces == 0) {//enemy not in way
				return 250;
			} else {
				return 25;
			}
		}

		if (enemySpaces == 3) { //blcok move
			return 10000;
		}
		
		else if (enemySpaces == 2) { //that orange muppet is planning ahead
			return 1500;
		}
		
		else if (enemySpaces == 1) { //maybe you should block them while you have the chance
			return 100;
		}
		
		//Incase everything is empty
		return 25;
	}

	//Checks to see if board state is empty
	public boolean isEmpty() {
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (board[row][column] != 0) {
					return false;
				}
			}
		}

		return true;
	}

	//Gets the next move
	public Move getMove(state nextState)
	{
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				int currentSpace = board[row][column];

				if (currentSpace == 0 && nextState.board[row][column] == 1)
				return new Move(1, row + 1, column + 1);
			}
		}
		return null;
	}

	//Looks for next state to get to
	public ArrayList<state> successors(boolean computer)
	{
		ArrayList<state> results = new ArrayList<>();

		int player = isComputer(computer); // check who current player is

		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == 0)
				{
					int[][] newState = Arrays.stream(board).map(r -> r.clone()).toArray(int[][]::new);

					newState[row][column] = player;
					results.add(new state(newState));
				}
			}
		}

		return results;
	}

	//a child state is only 1 difference away from parent
	public boolean isChild(state state, boolean computer) {
		int differences = 0;

		int player = isComputer(computer); //check who the player is

		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				if (state.board[row][column] != board[row][column]) { //difference found
					if (state.board[row][column] == 0 && board[row][column] == player) { //difference correlated to active agent
						differences += 1; //add to differences between states
					} else {
						return false;
					}
				}
			}
		}
		//exactly 1 difference means it is a child
		if (differences == 1) {
			return true;
		} else {
			return false;
		}
	}
}
