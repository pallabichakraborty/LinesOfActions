package com.loa.engine.board;
/*****************************************************************************************
 * Class Name: EmptyTile
 * Description: Class to instantiate an empty tile, extends Tile Class
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

public final class EmptyTile extends Tile
{

	//Constructor, generates a tile object
	EmptyTile(final int tileCoordinateX,final int tileCoordinateY) {
		super(tileCoordinateX,tileCoordinateY);
	}

	//As Empty Tile will be empty so isTileOccupied to be false
	@Override
	public boolean isTileOccupied() {
		return false;
	}

	//As Empty Tile will be empty so there will be no pieces on it
	@Override
	public Piece getPiece() {
		return null;
	}
	
}
