package com.loa.engine.player;
/*****************************************************************************************
 * Class Name: WhitePlayer
 * Description: Class for White Player
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

public class WhitePlayer extends Player{

	//Constructor
	public WhitePlayer(final Board board, 
			           final Collection<Move> whiteStandardLegalMoves,
			           final Collection<Move> blackStandardLegalMoves) 
	{
		super(board,whiteStandardLegalMoves,blackStandardLegalMoves);

	}

	//Get all the active pieces of the White Player
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	// Color - White
	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	//Opponent - Black
	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}

}
