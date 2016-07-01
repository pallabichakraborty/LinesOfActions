package com.loa.engine.piece;
/*****************************************************************************************
 * Class Name: Piece
 * Description: Class with the methods for a piece
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


import java.util.List;

import com.loa.engine.Alliance;
import com.loa.engine.board.Board;
import com.loa.engine.board.Move;


public abstract class Piece {
	
	//X,Y coordinates of the piece
	protected final int piecePositionX;
	protected final int piecePositionY;
	//Color of the Piece
	protected final Alliance pieceAlliance;
	
	//Constructor
	Piece(final int piecePositionX,final int piecePositionY,final Alliance pieceAlliance)
	{
		this.piecePositionX=piecePositionX;
		this.piecePositionY=piecePositionY;
		this.pieceAlliance=pieceAlliance;
	}
	
	//Calculate all the Legal Moves for the board
	public abstract List<Move> getLegalMoves(final Board board);
	
	//Generate a new Piece on the basis of a move
	public abstract Piece movePiece(Move move);
	
	//Generate Piece Position - 10x+y
	public Integer getPiecePosition() 
	{
		return Integer.parseInt(Integer.toString(this.piecePositionX)+Integer.toString(this.piecePositionY));
	}
	
	//Getter for pieceAlliance
	public Alliance getPieceAlliance()
	{
		return this.pieceAlliance;
	}

	//Overriden hashCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pieceAlliance == null) ? 0 : pieceAlliance.hashCode());
		result = prime * result + piecePositionX;
		result = prime * result + piecePositionY;
		return result;
	}

	//Overriden equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (pieceAlliance != other.pieceAlliance)
			return false;
		if (piecePositionX != other.piecePositionX)
			return false;
		if (piecePositionY != other.piecePositionY)
			return false;
		return true;
	}
	
}
