package kennethHung_4200p3;

import java.util.ArrayList;

public class puzzleBoard {
    private ArrayList<Move> movementList = new ArrayList<>();
	public state currentState = new state();
	private boolean computerAgent;

	public puzzleBoard(boolean computerAgent)
	{
		this.computerAgent = computerAgent; //true if computer goes first
	}

	//Turns the board into a string
	//  1 2 3 4 5 6 7 8
	//A - - - - - - - -
	//B - - - - - - - -
	//C - - - - - - - -
	//D - - - - - - - -
	//E - - - - - - - -
	//F - - - - - - - -
	//G - - - - - - - -
	//H - - - - - - - -
	private String boardString() {
		StringBuilder result = new StringBuilder();
		//top row column names
		result.append("  ");
		//top is numbers with spaces
		for (int i = 1; i <= 8; i++) {
			result.append(i + " ");
		}
		result.append("\n");

		for (int i = 0; i < 8; i++) {
			//not the most elegant
			//quick and dirty solution since board is set at 8 by 8
			switch (i) {
				case 0:
					result.append("A ");
					break;
				case 1:
					result.append("B ");
					break;
				case 2:
					result.append("C ");
					break;
				case 3:
					result.append("D ");
					break;
				case 4:
					result.append("E ");
					break;
				case 5:
					result.append("F ");
					break;
				case 6:
					result.append("G ");
					break;
				case 7:
					result.append("H ");
					break;
				default:
					result.append("- ");
					break;
			}
			//put either -, O, or X followed by space
			for (int j = 0; j < 8; j++) {
				char marker = whoMoved(currentState.board[i][j]);
				result.append(marker + " ");
			}
			// next row
			result.append("\n");
		}
		result.append("\n");

		return result.toString();
	}

	//list of moves
	private String moveList(boolean computerFirst)
	{
		StringBuilder result = new StringBuilder();

		//Title depending on who moves first
		if (!computerFirst) {
			result.append("Player vs. Computer");
		} else {
			result.append("Computer vs. Player");
		}

		for (int i = 0; i < movementList.size(); i++) {
			String listOfMoves = movementList.get(i).translator();
			//lists moves, slight different depending on if even or odd
			if (i % 2 == 0) {
				result.append("\n  " + (i / 2 + 1) + ". " + listOfMoves);
			} else {
				result.append(" " + listOfMoves);
			}
		}

		return result.toString();
	}

	//merge board and move lists
	private String stringAdjacent(String string1, String string2) {
		//breaks up strings based on this
		String splitter = "\n";
		//placement for Title
		String inserter = "  ";

		//split strings for merging
		String[] string1Split = string1.split(splitter);
		String[] string2Split = string2.split(splitter);

		//combines string parts
		StringBuilder adjString = new StringBuilder();
		int largerLength = Math.max(string1Split.length, string2Split.length);
		for (int i = 0; i < largerLength; i++) {
			//add string1
			if (i < string1Split.length) {
				adjString.append(string1Split[i]);
			}
			//add string2
			if (i < string2Split.length) {
				if (i >= string1Split.length) {
					//buffer to maintain appearance
					adjString.append("                  ");
				}
				adjString.append(inserter).append(string2Split[i]);
			}

			adjString.append("\n");
		}

		return adjString.toString();
	}

	//print function
	public void print() {
		String combo = stringAdjacent(boardString(), moveList(computerAgent));
		System.out.println(combo);
	}
	//adds move to list
	public void addMove(Move move) {
		movementList.add(move);
	}

	//Takes movement data and will display a symbol on the board according to whether the player moved or computer moved
	private static char whoMoved(int mover) {
		char result;
		//No Moves
		switch(mover) {
			//No move inhabits tile
			case 0:
				result = '-';
				break;
			// player 1 or 2 rep
			case 1: 
				result = 'X';
				break;
			case 2:
				result = 'O';
				break;
			//Something wrong happened
			default:
				result = ' ';
				break;
		}
		return result;
	}

}
