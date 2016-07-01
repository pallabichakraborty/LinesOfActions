package com.loa.engine.board;
/*****************************************************************************************
 * Class Name: NullMove
 * Description: Class for Illegal Move, extends Move Class
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


public class NullMove extends Move {

	//Constructor with invalid move coordinates
	public NullMove() {
		super(null, -1, -1, null);
	}
	//Exception if move tried to be executed
	public Board execute()
	{
		throw new RuntimeException("Cannot execute Null Move");
	}

}
