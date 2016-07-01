package com.loa.engine.player;
/*****************************************************************************************
 * Class Name: Player
 * Description: Player Class to instantiate a player
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
import com.loa.engine.board.MoveStatus;
import com.loa.engine.piece.Piece;
import com.loa.engine.player.ai.MoveStrategy;


public abstract class Player {

	protected final Board board;
	protected final Collection<Move> legalMoves;
	protected final Collection<Move> opponentMoves;
	private MoveStrategy strategy;

	//Constructor
	Player(final Board board,
			final Collection<Move> legalMoves,
			final Collection<Move> opponentMoves)
	{
		this.board=board;
		this.legalMoves=legalMoves;
		this.opponentMoves=opponentMoves;
	}
	
	//Getter for the legalMoves
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	//Getter for the strategy
	public MoveStrategy getMoveStrategy() {

		return this.strategy;
	}

	//setter for the strategy
	public void setMoveStrategy(MoveStrategy strategy) {
		this.strategy=strategy;

	}

	//Get the Active Pieces of the Player
	public abstract Collection<Piece> getActivePieces();

	//Get the color of the Player
	public abstract Alliance getAlliance();

	//Get the Opponent Player
	public abstract Player getOpponent();

	//Check of the Move is available in the legal moves of the player
	public boolean isMoveLegal(final Move m)
	{
		return this.legalMoves.contains(m);
	}

	//Generate the MoveTransition object due to a new move
	public MoveTransition makeMove(final Move move)
	{
		//If the move is not legal then generate a Illegal MoveTransition
		if (!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		//If the move is legal then generate a successful move board
		final Board transitionedBoard = move.execute();
		return new MoveTransition(transitionedBoard, move, MoveStatus.DONE);
	}


}


