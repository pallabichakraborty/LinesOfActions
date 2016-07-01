package com.loa.engine.player;

/*****************************************************************************************
 * Class Name: BlackPlayer
 * Description: Class for Black Player with implementation of the abstract methods of Player Class
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

import java.util.Collection;

import com.loa.engine.Alliance;
import com.loa.engine.board.Board;
import com.loa.engine.board.Move;
import com.loa.engine.piece.Piece;

public class BlackPlayer extends Player
{
	//Constructor
	public BlackPlayer(final Board board, 
	                   final Collection<Move> whiteStandardLegalMoves,
	                   final Collection<Move> blackStandardLegalMoves) 
	{
		super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
	}

	//Active Pieces - All Black Pieces
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	//Alliance - Black
	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	//Opponent - White
	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

}
