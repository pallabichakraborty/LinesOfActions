package com.loa.engine.piece;

/*****************************************************************************************
 * Class Name: Checker
 * Description: Checker Class extends Piece, instantiates the pieces on the board
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


import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.loa.engine.Alliance;
import com.loa.engine.board.Board;
import com.loa.engine.board.BoardUtils;
import com.loa.engine.board.Move;


public class Checker extends Piece{

	//Constructor
	public Checker(final int piecePositionX, final int piecePositionY, final Alliance pieceAlliance) 
	{
		super(piecePositionX, piecePositionY, pieceAlliance);
	}

	//Calculate the legal moves for the piece on the Board
	public List<Move> getLegalMoves(Board board) 
	{
		final List<Move> legalMoves=new ArrayList<>();
		//Moves can be up,down,left,right,or 45 degree,135 degree,225 degree,315 degrees*/
		for ( int x = -1; x <= 1; x++ )
		 {
	          for (int y = -1; y <= 1; y++ ) 
	          {
	        	//No Move
	            if ( x == 0 && y == 0 )
	              continue;
	            //Calculate the distance which needs to be moved
	            int dist = BoardUtils.calcDistance( this.piecePositionX, this.piecePositionY, x, y );
	            //Generate a move by calculating the distance
	            Move resultMove=new Move(board,((this.piecePositionX)+(dist*x)),((this.piecePositionY)+(dist*y)), this);
	            //Check if the move generated is legal then all to legal moves list
	            if (BoardUtils.isMoveLegal(resultMove))
	            {
	            	//Add the move to the list if found legal
	            	legalMoves.add(resultMove);
	            }
	            	
	          }
	     }
		
		return ImmutableList.copyOf(legalMoves);
	}


	//Generate a piece using the destination X,Y coordinates and the piece to be added
	@Override
	public Checker movePiece(Move move) 
	{
		return new Checker(move.getDestCoordinateX(), move.getDestCoordinateY(), move.getPieceOnCoordinate().getPieceAlliance());
		
	}

}
