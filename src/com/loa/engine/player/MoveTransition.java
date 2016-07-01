package com.loa.engine.player;
/*****************************************************************************************
 * Class Name: MoveTransition
 * Description: Class to get the transition board with move and its status generating it
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
import com.loa.engine.board.MoveStatus;

public class MoveTransition {
	
	private Board transitionBoard;
	private Move move;
	private MoveStatus status;
	
	//Constructor
	public MoveTransition(Board transitionBoard,
			      Move move,
			      MoveStatus status)
	{
		this.transitionBoard=transitionBoard;
		this.move=move;
		this.status=status;
	}
	
	//Getter for transitionBoard
	 public Board getTransitionBoard() {
         return this.transitionBoard;
    }

	//Getter for status
    public MoveStatus getMoveStatus() {
        return this.status;
    }
    
    //Getter for move
    public Move getMove()
    {
    	return this.move;
    }

	

}
