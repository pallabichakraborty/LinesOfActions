package com.loa.engine.player.ai;

/*****************************************************************************************
 * Class Name: MoveStrategy
 * Description: Interface for Move Strategy, to be used for AI moves
 * 
 * Reference
 * -----------
 * 1) https://www.youtube.com/watch?v=Cm70y54cDIo
 * 2) Java Chess Programming https://www.youtube.com/watch?v=5SKOOG3TwVU
 * 
 * Changes Done
 * --------------
 * Description of Changes		           Date of Modification         Modification Done By
 * Initial Draft							23-Mar-2016					Pallabi
 *******************************************************************************************/


import com.loa.engine.board.Board;
import com.loa.engine.board.Move;

public interface MoveStrategy {
	
	//Number of Moves evaluated
	long getNumMovesEvaluated();
	//Move to be executed for a board with the search depth 
    Move execute(Board board, int depth);

}
