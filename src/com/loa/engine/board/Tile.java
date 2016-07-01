package com.loa.engine.board;

/*****************************************************************************************
 * Class Name: OccupiedTile
 * Description: Class for a Tile with a piece on it, extends Tile Class
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


import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.loa.engine.piece.Piece;

public abstract class Tile {
	//X Coordinate of the Tile
	int tileCoordinateX;
	//Y Coordinate of the Tile
	int tileCoordinateY;
	
	//Constructor
	Tile(final int tileCoordinateX,final int tileCoordinateY)
	{
		this.tileCoordinateX=tileCoordinateX;
		this.tileCoordinateY=tileCoordinateY;
	}
	
	//Method to indicate if the Tile is occupied
	public abstract boolean isTileOccupied();
	
	//If Tile is occupied then fetch the piece else get null
	public abstract Piece getPiece();
	
	//Get no piece then generate empty tiles 
	public static final Map<Integer,EmptyTile> EMPTY_TILES_CACHE=retrieveAllEmptyTiles();
	
	//Getter for tileCoordinateX
	public int getTileCoordinateX() {
        return this.tileCoordinateX;
    }
	
	//Getter for tileCoordinateY
	public int getTileCoordinateY() {
        return this.tileCoordinateY;
    }

	//Generate Empty Tiles if no piece on a tile
	private static Map<Integer, EmptyTile> retrieveAllEmptyTiles() 
	{
      final Map<Integer,EmptyTile> emptyTileMap=new HashMap<>();
		
		for(int i=0;i<BoardUtils.BOARD_SIZE*BoardUtils.BOARD_SIZE;i++)
		{
			for(int j=0;j<BoardUtils.BOARD_SIZE*BoardUtils.BOARD_SIZE;j++)
			{
				emptyTileMap.put(Integer.parseInt(Integer.toString(i)+Integer.toString(j)), new EmptyTile(i,j));
			}
		}
		
		return ImmutableMap.copyOf(emptyTileMap);
	}
	
	//Create new Tiles
	public static Tile createTile(final int tileCoordinateX,final int tileCoordinateY,final Piece piece)
	{
		//If Piece is occupied, then generate Occupied tile
		if (piece!=null)
			return new OccupiedTile(tileCoordinateX,tileCoordinateY,piece);
		// If there is no piece, create empty tile
		else
			return EMPTY_TILES_CACHE.get(Integer.parseInt(Integer.toString(tileCoordinateX)+Integer.toString(tileCoordinateY)));
	}
	
	//If empty tile then output a hyphen
	@Override
	public String toString()
	{
		return "-";
	}
	
	

}
