package com.loa.engine.board;
/*****************************************************************************************
 * Class Name: Builder
 * Description: Builder Class to generate Board Class
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

import java.util.HashMap;
import java.util.Map;

import com.loa.engine.Alliance;
import com.loa.engine.piece.Piece;

public class Builder
{
	//Variable to store the positions of the pieces on the board
	Map<Integer,Piece> boardConfig;
	//Colour of the next move maker
	Alliance nextMoveMaker;
	
	//Constructor
	public Builder()
	{
		this.boardConfig=new HashMap<>();
	}
	
	//Set the position of the board using the position of the piece on the board
	//Return the builder class after applying the setting
	public Builder setPiece(final Piece piece)
	{
		this.boardConfig.put(piece.getPiecePosition(), piece);
		return this;
	}
	
	//Setter for the nextMoveMaker
	//Return the builder class after applying the setting
	public Builder setNextMoveMaker(Alliance nextMoveMaker)
	{
		this.nextMoveMaker=nextMoveMaker;
		return this;
	}
	
	//generate a Board Class using the builder class settings
	public Board build()
	{
		return new Board(this);
	}
}


