package com.loa.engine.board;

import java.util.Collection;

/*****************************************************************************************
 * Class Name: Move
 * Description: Class to implement the Move of the pieces
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
import com.loa.engine.piece.Piece;

public  class Move 
{ 
	Board board;
	// Destination X coordinate
	int destCoordinateX;
	//Destination Y coordinate
	int destCoordinateY;
	//Piece on the source coordinate
	Piece pieceOnCoordinate;

	//Create a final object for NullMove i.e. a invalid move
	public static final Move NULL_MOVE = new NullMove();

	//Constructor
	public Move( Board board, 
			int destCoordinateX, 
			int destCoordinateY, 
			Piece pieceOnCoordinate)
	{
		this.board=board;
		this.destCoordinateX=destCoordinateX;
		this.destCoordinateY=destCoordinateY;
		this.pieceOnCoordinate=pieceOnCoordinate;

	}
	
	//Getter for pieceOnCoordinate
	public Piece getPieceOnCoordinate()
	{
		return this.pieceOnCoordinate;
	}

	//Getter for destCoordinateX
	public int getDestCoordinateX()
	{
		return this.destCoordinateX;
	}

	//Getter for destCoordinateY
	public int getDestCoordinateY()
	{
		return this.destCoordinateY;
	}

	//Getter for board
	public Board getBoard()
	{
		return this.board;
	}
	
	//Overrided hashCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + destCoordinateX;
		result = prime * result + destCoordinateY;
		result = prime * result + ((pieceOnCoordinate == null) ? 0 : pieceOnCoordinate.hashCode());
		return result;
	}

	//Override equal method for Move
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (destCoordinateX != other.destCoordinateX)
			return false;
		if (destCoordinateY != other.destCoordinateY)
			return false;
		if (pieceOnCoordinate == null) {
			if (other.pieceOnCoordinate != null)
				return false;
		} else if (!pieceOnCoordinate.equals(other.pieceOnCoordinate))
			return false;
		return true;
	}

	//Method used to execute a move which will again generate a new board
	public Board execute()
	{
		//Create new Board Instance
		final Builder builder = new Builder();
		//Loop through all the active pieces of the Current Player
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) 
		{
			//If the piece on the source coordinate of the move is not the piece checked then set the board with the piece
			if (!this.pieceOnCoordinate.equals(piece)) 
			{
				builder.setPiece(piece);
			}
		}
		//Set the opponent pieces as is with Active Pieces
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) 
		{
			builder.setPiece(piece);
		}
		
		//Set the new move by creating a new object of Checker object
		builder.setPiece(this.pieceOnCoordinate.movePiece(this));
		//Set the next move maker to be the opponent of the current player
		builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		return builder.build();
	}

	//Create a new move
	//If the new move coincides with the legal moves then go ahead with the mobe else generate a Null Move
	public static Move createMove(final Board board,
			final int sourceCoordinateX,
			final int sourceCoordinateY,
			final int destCoordinateX,
			final int destCoordinateY) 
	{
		//Fetch all the legal moves of the current user
		for (final Move move : board.currentPlayer().getLegalMoves()) 
		{
			//Check if the move matches the legal move
			if (move.getPieceOnCoordinate().getPiecePosition()/10 == sourceCoordinateX &&
					move.getPieceOnCoordinate().getPiecePosition()%10 == sourceCoordinateY &&	
					move.destCoordinateX == destCoordinateX &&
					move.destCoordinateY == destCoordinateY) 
			{
				return move;
			}
		}
		//no matching legal moves then return a Null Move
		return Move.NULL_MOVE;
	}


	//Find commulative distance between the pieces on the board
	public float checkForDistanceBetweenPieces()
	{

		int playerDist = getDistanceBetweenPieces(board.currentPlayer.getActivePieces());
		//Higher the distance penalize the player
		int result = -1*playerDist;

		return result;

	}
	
	//Calculate the distance of the pieces on the board
	private int getDistanceBetweenPieces(Collection<Piece> pieces){
		int sum = 0;
		for(Piece piece:pieces){
			for(Piece refpiece:pieces){
				sum += calcDistanceBetweenPieces(piece, refpiece);
			}
		}
		return sum;
	}

	//Calculate Euclidean Distance between 2 positions
	private double calcDistanceBetweenPieces(Piece move1, Piece move2){
		int x = (int) Math.pow(move1.getPiecePosition()/10-move2.getPiecePosition()/10,2);
		int y = (int) Math.pow(move1.getPiecePosition()%10-move2.getPiecePosition()%10,2);
		return Math.sqrt(x+y);
	}


}
