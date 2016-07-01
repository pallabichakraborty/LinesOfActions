package com.loa.engine.board;

/*****************************************************************************************
 * Class Name: MoveStatus
 * Description: Enum for the move status
 * 
 * Reference
 * -----------
 * 1) https://www.youtube.com/watch?v=Cm70y54cDIo
 * 
 * Changes Done
 * --------------
 * Description of Changes		           Date of Modification         Modification Done By
 * Initial Draft							23-Mar-2016					Pallabi
 *******************************************************************************************/


public enum MoveStatus {
	//Successful Move, isDone=True
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    //Illegal Move, isDone=False
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    };

	//method to indicate if the move is successful
    public abstract boolean isDone();

}
