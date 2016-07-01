package com.loa.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.loa.engine.Alliance;
import com.loa.engine.piece.Checker;
import com.loa.engine.piece.Piece;
import com.loa.engine.player.BlackPlayer;
import com.loa.engine.player.Player;
import com.loa.engine.player.WhitePlayer;
/*****************************************************************************************
 * Class Name: Board
 * Description: Class to generate the Board for the LOA with the functionalities of LOA,
 * this calls the functionality related methods
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


public class Board 
{
	//Array to contain the counts of the column wise pieces
	final static int[] verticalPieces=new int[BoardUtils.BOARD_SIZE];
	//Array to contain the counts of the row wise pieces
	final static int[] horizontalPieces=new int[BoardUtils.BOARD_SIZE];
	//Array to  contain the counts of Diagonally in forward direction
	final static int[] Diag1Pieces=new int[BoardUtils.BOARD_SIZE*2];
	//Array to  contain the counts of Diagonally in backward direction
	final static int[] Diag2Pieces=new int[BoardUtils.BOARD_SIZE*2];
	
	//Variables to store White and Black Pieces
	final Collection<Piece> whitePieces;
	final Collection<Piece> blackPieces;
	
	//Variables to store the legal moves for White and Black Pieces
	final Collection<Move> whiteStandardLegalMoves;
	final Collection<Move> blackStandardLegalMoves;
	
	//Variables for storing Players
	final WhitePlayer whitePlayer;
	final BlackPlayer blackPlayer;
	final Player currentPlayer;
	
	//Variables to store if the Winner is White or Black
	final private boolean isWhiteWinner;
	final private boolean isBlackWinner;
	
	//Variables to contain the maximum counts for White and Black
	final private int whiteMaxConnectedCount;
	final private int blackMaxConnectedCount;
	
	//Variable for gameboard, each tile to contain one position
	private final Tile[][] gameBoard;
	
	public Board(Builder builder) {
		//Create game board using Builder Class with configured tiles
		this.gameBoard=createGameBoard(builder);
		//Get the active White Pieces on the board
		this.whitePieces=getActivePieces(this.gameBoard,Alliance.WHITE);
		//Get the active Black Pieces on the board
		this.blackPieces=getActivePieces(this.gameBoard,Alliance.BLACK);
		//Get all the White Legal Moves for the board position
		this.whiteStandardLegalMoves=getLegalMoves(this.whitePieces);
		//Get all the Black Legal Moves for the board position
		this.blackStandardLegalMoves=getLegalMoves(this.blackPieces);
		//Instantiate a white player
		this.whitePlayer=new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
		//Instantiate a black player
		this.blackPlayer=new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
		//Compute the current player
		this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer(),this.blackPlayer);
		//If white is the winner
		this.isWhiteWinner=isWhiteWinner(this);
		//If black is the winner
		this.isBlackWinner=isBlackWinner(this);
		//Compute the maximum number of connected pieces for white
		this.whiteMaxConnectedCount=getMaxConnectedCount(this.whitePieces,Alliance.WHITE,this);
		//Compute the maximum number of connected pieces for black
		this.blackMaxConnectedCount=getMaxConnectedCount(this.blackPieces,Alliance.BLACK,this);
	}

	//Getter for variable isWhiteWinner
	public boolean getIsWhiteWinner()
	{
		return this.isWhiteWinner;
	}
	
	//Getter for variable isBlackWinner
	public boolean getIsBlackWinner()
	{
		return this.isBlackWinner;
	}
	
	//Getter for variable blackPieces
	public Collection<Piece> getBlackPieces()
	{
		return this.blackPieces;
	}
	
	//Getter for variable whitePieces
	public Collection<Piece> getWhitePieces()
	{
		return this.whitePieces;
	}
	
	//Get the current player
	public Player currentPlayer() {
        return this.currentPlayer;
    }
	
	//Getter for blackMaxConnectedCount
	public int getBlackMaxConnectedCount()
	{
		return this.blackMaxConnectedCount;
	}
	
	//Getter for whiteMaxConnectedCount
	public int getWhiteMaxConnectedCount()
	{
		return this.whiteMaxConnectedCount;
	}
	
	//Getter for blackPlayer
	public Player blackPlayer() {
		return this.blackPlayer;
	}

	//Getter for whitePlayer
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	//Get all the legal moves allowed for a particular board position for a particular color
	public Collection<Move> getLegalMoves(Collection<Piece> pieces) 
	{
		//List for the legal moves
        final List<Move> legalMoves=new ArrayList<Move>();
		
        if(pieces!=null)
        {
        	//Get the piece level Legal Moves
        	 for(final Piece piece:pieces)
     		{
             		legalMoves.addAll(piece.getLegalMoves(this));
     			
     		}
        }
       

		return ImmutableList.copyOf(legalMoves);
	}

	//Overloaded method for board so that it can be run on the command prompt
	public String toString()
	{
		final StringBuilder builder=new StringBuilder();
		for(int i=0;i<BoardUtils.BOARD_SIZE;i++)
		{
			for(int j=0;j<BoardUtils.BOARD_SIZE;j++)
			{
				final String tileText=gameBoard[i][j].toString();
				builder.append(String.format("%3s",tileText));
				
				
			}
			builder.append("\n");
			
		}
		
		return builder.toString();
	}
	
	
	/*
	 * Method to get all the active pieces of a particular color
	 */
	private Collection<Piece> getActivePieces(Tile[][] gameBoard2, Alliance alliance) 
	{
		//List for all active pieces
		final List<Piece> activePieces=new ArrayList<Piece>();
		// Check if the Tile is occupied and if occupied  then of the particular color
		for(int i=0;i<BoardUtils.BOARD_SIZE;i++)
		{
			for(int j=0;j<BoardUtils.BOARD_SIZE;j++)
			{
				if(gameBoard2[i][j].isTileOccupied() && gameBoard2[i][j].getPiece().getPieceAlliance()==alliance)
				{
					activePieces.add(gameBoard2[i][j].getPiece());
				}
			}
		}
		return ImmutableList.copyOf(activePieces);
	}

	//Create Game board and calculate the counts of the vertical, horizontal and diagonal counts
	private Tile[][] createGameBoard(Builder builder) 
	{
		//Create an array of tiles with BOARD_SIZE*BOARD_SIZE
		Tile[][] tiles=new Tile[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];

		//Initialize the counts for a new board
		for ( int i = 0; i < BoardUtils.BOARD_SIZE; i++ ) 
		{
			Board.verticalPieces[i] =0;
			Board.horizontalPieces[i] =0;
			Board.Diag1Pieces[i] = 0;
			Board.Diag2Pieces[i] = 0;
			Board.Diag1Pieces[i+BoardUtils.BOARD_SIZE-1] = 0;
			Board.Diag2Pieces[i+BoardUtils.BOARD_SIZE-1] = 0;
		}
		
		//Create the tiles for the complete tile
		for(int i=0;i<BoardUtils.BOARD_SIZE;i++)
		{
			for(int j=0;j<BoardUtils.BOARD_SIZE;j++)
			{
				//Create a tile
				tiles[i][j]=Tile.createTile(i, j, builder.boardConfig.get(Integer.parseInt(Integer.toString(i)+Integer.toString(j))));
				//If the tile is occupied, then add in the counts
				if(tiles[i][j].isTileOccupied())
				{
					verticalPieces[i]++;
					horizontalPieces[j]++;
					Diag1Pieces[i + (BoardUtils.BOARD_SIZE-1 - j)]++;
					Diag2Pieces[i + j]++;
				}
			}
		}
		return tiles;
	}
	
	//Generate the first instance of the Board
	public static Board createStandardBoard()
	{
		final Builder builder=new Builder();
		
		//Black pieces row -> 0 and BOARD_SIZE-1 leaving the corner tiles
		//White pieces column -> 0 and BOARD_SIZE-1 leaving the corner tiles
		for(int i=0;i<BoardUtils.BOARD_SIZE;i++)
		{
			for(int j=0;j<BoardUtils.BOARD_SIZE;j++)
			{
				if((i==0 && j>0 && j<BoardUtils.BOARD_SIZE-1)|| (i==BoardUtils.BOARD_SIZE-1 && j>0 && j<BoardUtils.BOARD_SIZE-1))
				{
					builder.setPiece(new Checker(i,j,Alliance.BLACK));
				}
				else if((i>0 && i<BoardUtils.BOARD_SIZE-1 && j==0)|| (i>0 && i<BoardUtils.BOARD_SIZE-1 && j==BoardUtils.BOARD_SIZE-1))
				{
					builder.setPiece(new Checker(i,j,Alliance.WHITE));
				}

			}
		}

		/*Players alternate moves, with Black having the first move.*/
		builder.setNextMoveMaker(Alliance.BLACK);
		//Generate a board using builder Class
		return builder.build();
	}
	
	//Get the tile for a particular tiles coordinates
	public Tile getTile(final int tileCoordinateX,final int tileCoordinateY)
	{
		return gameBoard[tileCoordinateX][tileCoordinateY];
	}
	
		
	// Check if White is winner
	public static boolean isWhiteWinner(Board board)
	{
		if(isAllPieces8Connected(board.whitePieces,Alliance.WHITE,board)) 
		{
			return true;
		}
		
		return false;
	}
	
	// Check if Black is winner
	 
	public static boolean isBlackWinner(Board board)
	{
		if(isAllPieces8Connected(board.blackPieces,Alliance.BLACK,board)) 
		{
			return true;
		}
		
		return false;
	}
	
	// Count of connected tiles

	public static int connected_tiles(Alliance alliance,int positionX, int positionY, Piece piece,Board board,List<Piece> visited) 
	{
        int total = 1;
        //Check in all the 8 directions of the piece being considered
         for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int newPositionX = piece.getPiecePosition()/10 + i;
                int newPositionY = piece.getPiecePosition()%10 + j;
                //Out of Bounds
                if (BoardUtils.IllegalMoveOutOfBounds(newPositionX) || BoardUtils.IllegalMoveOutOfBounds(newPositionY))
                {
                	continue;
                }
                //Piece of a different colour
                else if (board.getTile(newPositionX, newPositionY).isTileOccupied() 
                		&& (board.getTile(newPositionX, newPositionY).getPiece().getPieceAlliance()
	                			!=alliance))
                {
                	continue;
                }
                //Piece already counted in connectedness
                else if (board.getTile(newPositionX, newPositionY).isTileOccupied() 
                		&& visited.contains(board.getTile(newPositionX, newPositionY).getPiece()))
                {
                	continue;
                }
                //Piece of same colour and not visited
                else if (board.getTile(newPositionX, newPositionY).isTileOccupied() 
                		&& (board.getTile(newPositionX, newPositionY).getPiece().getPieceAlliance()
                			==alliance))
                {
                	//Add the piece checked for connectedness to visited list
                	visited.add(board.getTile(newPositionX, newPositionY).getPiece());
                	//Add to the connect
                	total += connected_tiles(alliance,newPositionX, newPositionY, board.getTile(newPositionX, newPositionY).getPiece(),board,visited);
                	
                }
                
            }
        return total;
    }
	
	// Check for 8 connectedness
	public static boolean isAllPieces8Connected(Collection<Piece> pieces,Alliance alliance,Board board)
	{
		//Variable to indicate if all the pieces are 8 connected
		boolean is8Connected=false;
		//Number of pieces on the board
		int totalPieces=pieces.size();
		int connectedSet=0;
		
		//Visited pieces
		List<Piece> visited=new ArrayList<Piece>();
		//Get the first piece from the list of pieces passed to the method
		Piece piece=Iterables.get(pieces, 0);
		//Add to visited as already considered for computation
		visited.add(piece);
		//Get the connected pieces count
		connectedSet=connected_tiles(alliance,piece.getPiecePosition()/10, piece.getPiecePosition()%10, piece,board,visited);
		//If there are no pieces or if the connected set count is equal to the total number of pieces for a paticular color then true else false
		if((connectedSet==0 && totalPieces==0)||(connectedSet==totalPieces))
		{
			is8Connected=true;
		}
		
		return is8Connected;
	}
	
	// Get Maximum Connectedness of pieces
	public static int getMaxConnectedCount(Collection<Piece> pieces,Alliance alliance,Board board)
	{	//At least one piece is connected in itself
		int maxConnected=1;
		int connectedSet=0;
		//Check for the pieces set
		for(Piece piece:pieces)
		{
			List<Piece> visited=new ArrayList<Piece>();
			connectedSet=0;
			//Add to already Visited set
			visited.add(piece);
			//Get the count of connected tiles for a particular set of connected pieces
			connectedSet=connected_tiles(alliance,piece.getPiecePosition()/10, piece.getPiecePosition()%10, piece,board,visited);
			// If the count is greater than the maximum count of connected pieces then assign the count to max count
			if(connectedSet>maxConnected)
			{
				maxConnected=connectedSet;
			}
		}
		
		return maxConnected;
	}
	
	
	
	
}
