package com.loa.engine.board;

/*****************************************************************************************
 * Class Name: OccupiedTile
 * Description: Class for a Tile with a piece on it, extends Tile Class
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

public final class OccupiedTile extends Tile 
{
	//Piece on the tile
	Piece pieceOnTile;

	OccupiedTile(int tileCoordinateX,int tileCoordinateY,Piece pieceOnTile) 
	{
		super(tileCoordinateX,tileCoordinateY);
		this.pieceOnTile=pieceOnTile;
	}

	//Tile Occupied = True
	@Override
	public boolean isTileOccupied() {
		return true;
	}

	//Getter for pieceOnTile
	@Override
	public Piece getPiece() {
		return this.pieceOnTile;
	}
	
	//Method for command line output of the Board, if Piece White then W else if Black then B
	@Override
	public String toString()
	{
		return getPiece().getPieceAlliance().isBlack()?"B":"W";
	}
	
}