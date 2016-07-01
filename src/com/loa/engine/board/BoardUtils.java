package com.loa.engine.board;

/*****************************************************************************************
 * Class Name: BoardUtils
 * Description: Class containing the BOARD_SIZE which decides the board size and other
 * utility functions to compute the legal moves in the board
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


public class BoardUtils {
	
	//Variable which decides the Board dimensions
	public static int BOARD_SIZE=5;
	
	
	/*
	 * Check if the Move is Legal of not
	 */
	public static boolean isMoveLegal(Move resultMove)
	{
		//If the move is out of bounds of the board
		if(IllegalMoveOutOfBounds(resultMove.destCoordinateX)||IllegalMoveOutOfBounds(resultMove.destCoordinateY))
		{
			return false;
		}
		//If the move is blocked by a piece of opposite color
		else if(IllegalMoveBlockedByOpponentPiece(resultMove))
		{
			return false;
		}
		//If there is no movement
		else if(IllegalMoveNoMove(resultMove))
		{
			return false;
		}
		//If the movement distance does not match the direction of movement and pieces in the direction
		else if(IllegalMoveInvalidDistance(resultMove))
		{
			return false;
		}
		//If the destination move is on a piece of same colour
		else if(IllegalMoveMoveOnSameColor(resultMove))
		{
			return false;
		}
		else
		{
			return true;
		}
		
		
	}
	/*
	 * Check whether the coordinates passed are within the Board
	 */
	public static boolean IllegalMoveOutOfBounds(int coordinateValue)
	{
		if(coordinateValue>=BOARD_SIZE)
		{
			return true;
		}
		else if(coordinateValue<0)
		{
			return true;
		}
		return false;
		
	}
	
	/*
	 * A checker may not jump over an enemy checker. 
	 */
	public static boolean IllegalMoveBlockedByOpponentPiece(Move m)
	{
		int directionX=0;
		int directionY=0;
		
		/*Move Direction-X*/		
		if(m.destCoordinateX-m.getPieceOnCoordinate().getPiecePosition()/10>0)
		{
			directionX=1;
		}
		else if(m.destCoordinateX-m.getPieceOnCoordinate().getPiecePosition()/10<0)
		{
			directionX=-1;
		}
		
		/*Move Direction-Y*/	
		if(m.destCoordinateY-m.getPieceOnCoordinate().getPiecePosition()%10>0)
		{
			directionY=1;
		}
		else if(m.destCoordinateY-m.getPieceOnCoordinate().getPiecePosition()%10<0)
		{
			directionY=-1;
		}
		
		//Calculate the distance the piece can move
		int dist=calcDistance(m.pieceOnCoordinate.getPiecePosition()/10, m.pieceOnCoordinate.getPiecePosition()%10, directionX, directionY);
		
		//Check for all distances which are lesser than the final distance which needs to be covered
		 for (int i = 1; i < dist; i++) 
		 {
			 	//getPiecePosition gives the value in the form 10x+y
	            int xx = m.pieceOnCoordinate.getPiecePosition()/10 + i * directionX;
	            int yy = m.pieceOnCoordinate.getPiecePosition()%10 + i * directionY;
	            if (xx>=0 
	            	&& yy>=0 
	            	&& xx<BoardUtils.BOARD_SIZE 
	            	&& yy< BoardUtils.BOARD_SIZE
	            	&& m.board.getTile(xx, yy).isTileOccupied())
	            {
	            	//If the piece in between not of the same color
	            	if(m.board.getTile(xx, yy).getPiece().getPieceAlliance()!= 
	            	   m.board.getTile(m.pieceOnCoordinate.getPiecePosition()/10, 
	            			           m.pieceOnCoordinate.getPiecePosition()%10)
	            		       .getPiece()
	            		       .getPieceAlliance())
	            	{
	            		return true;
	            	}
	            	
	            }
	                
	    }
		 
		return false;
		
	}
	
	/*
	 * Invalid Distance
	 */
	public static boolean IllegalMoveInvalidDistance(Move m)
	{
		int directionX=0;
		int directionY=0;
		
		/*Move Direction-X*/		
		if(m.destCoordinateX-m.getPieceOnCoordinate().getPiecePosition()/10>0)
		{
			directionX=1;
		}
		else if(m.destCoordinateX-m.getPieceOnCoordinate().getPiecePosition()/10<0)
		{
			directionX=-1;
		}
		
		/*Move Direction-Y*/	
		if(m.destCoordinateY-m.getPieceOnCoordinate().getPiecePosition()%10>0)
		{
			directionY=1;
		}
		else if(m.destCoordinateY-m.getPieceOnCoordinate().getPiecePosition()%10<0)
		{
			directionY=-1;
		}
		//Calculate distance 
		int dist=calcDistance(m.pieceOnCoordinate.getPiecePosition()/10, m.pieceOnCoordinate.getPiecePosition()%10, directionX, directionY);
		//If the destination coordiantes does not match the computed values
		if (((m.destCoordinateX - m.getPieceOnCoordinate().getPiecePosition()/10) != dist * directionX) ||((m.destCoordinateY - m.getPieceOnCoordinate().getPiecePosition()%10) != dist * directionY))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Trying to move back to the initial starting position
	 */
	public static boolean IllegalMoveNoMove(Move m)
	{
		if(m.getPieceOnCoordinate().getPiecePosition()/10==m.destCoordinateX && m.getPieceOnCoordinate().getPiecePosition()%10==m.destCoordinateY)
		{
			return true;
		}
		
		return false;
	}
	
	/*
	 * Cannot move on the tile occupied by piece of same colour
	 */
	public static boolean IllegalMoveMoveOnSameColor(Move m)
	{
		if(m.board.getTile(m.destCoordinateX, m.destCoordinateY).isTileOccupied() && m.board.getTile(m.destCoordinateX, m.destCoordinateY).getPiece().getPieceAlliance()==m.getPieceOnCoordinate().getPieceAlliance())
		{
			return true;
		}
		return false;
	}

	/*
	 * Calculate the number of tiles the piece needs to move on the basis of the direction of movement and the number of pieces present in the direction
	 */
	public static int calcDistance(int x, int y, int dx, int dy) {
	      if (dx == 0 )
	        return Board.verticalPieces[x];
	      if ( dy == 0 )
	        return Board.horizontalPieces[y];
	      if ( dx+dy == 0 )
	        return Board.Diag2Pieces[x+y];
	      return Board.Diag1Pieces[x+(BoardUtils.BOARD_SIZE-1-y)];
	    }
	
}
