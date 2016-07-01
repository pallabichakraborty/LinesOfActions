package com.loa.gui;
/*****************************************************************************************
 * Class Name: LoaGui
 * Description: Class extending JDialog generating the Game setup dialog box
 * 
 * Reference
 * -----------
 * 1) https://www.youtube.com/watch?v=Cm70y54cDIo
 * 2) Java Chess Programming https://www.youtube.com/watch?v=5SKOOG3TwVU
 * 
 * Changes Done
 * --------------
 * Description of Changes		           Date of Modification         Modification Done By
 * Initial Draft							31-Mar-2016					Pallabi
 *******************************************************************************************/


import com.loa.engine.Alliance;
import com.loa.engine.board.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.loa.engine.board.BoardUtils;
import com.loa.engine.board.Move;
import com.loa.engine.board.Tile;
import com.loa.engine.piece.Piece;
import com.loa.engine.player.MoveTransition;
import com.loa.engine.player.ai.AlphaBetaPruner;
import com.utility.ResourceLoader;


public class LoaGui extends Observable{
	
	//Variable for the outer frame of the game board
	private static final Dimension OUTER_FRAME_DIMENSIONS = new Dimension(BoardUtils.BOARD_SIZE*75,BoardUtils.BOARD_SIZE*75);
	//Variable for the Board Actual Dimensions
	private static final Dimension BOARD_PANEL_DIMENSION=new Dimension(BoardUtils.BOARD_SIZE*70,BoardUtils.BOARD_SIZE*70);
	//Variable for each Board Tiles
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	
	//Variables to define the colours of the tiles, light tile and dark tile
	private Color lightColor=Color.decode("#FFE4C4");;
	private Color darkColor = Color.decode("#CD853F");
	
	//Variable for the JFrame for the Board
	private final JFrame gameFrame;
	//Variable to generate the BoardPanel for the game
	private final BoardPanel boardPanel;
	//Board Variable
	private Board loaBoard;
	
	//Path where the jpeg files for the checkers are kept
	public String pieceIconPath="files/checkers/";
	
	//Variable to define the source tile for a particular Move
	private Tile sourceTile;
	//Variable to define the destination tile for a particular Move
	private Tile destinationTile;
	//Variable to capture the piece moved by human player
	private Piece humanMovedPiece;
	//Variable to capture any game setting done on the game
	private final GameSetup gameSetup;
	
	
	public LoaGui()
	{
		//Setup the Game Frame
		this.gameFrame=new JFrame("Pallabi's Lines of Actions");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar menuBar=new JMenuBar();
		populateMenuBar(menuBar);
		this.gameFrame.setJMenuBar(menuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSIONS);
		this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Instantiate a new board
		this.loaBoard=Board.createStandardBoard();
		//Add the observer for the class, as the class has been specified as a Observable Class
		this.addObserver(new GameObserver());
		//Create a board panel
		this.boardPanel=new BoardPanel();
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		//Instantiate a game setup menu in the main menu
		this.gameSetup = new GameSetup(this.gameFrame, true);
		//Make the game frame visible
		this.gameFrame.setVisible(true);
		
	}
	
	//In case of any changes on the board, intimate the Observer class
	public void notifyUpdateBoard(Board board)
	{
		setChanged();
        notifyObservers(board);
	}
	
	//In case of any changes in the game setup, intiamte the Observer Class
	public void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

	//Method to populate the Menu Bar
	private void populateMenuBar(JMenuBar menuBar) {
		menuBar.add(createFileMenu());
		menuBar.add(SetupMenu());
		
	}
	
	//Getter for the gameSetup variable
	public  GameSetup getGameSetup() {
        return this.gameSetup;
    } 

	//Method to generate file menu
	private JMenu createFileMenu() {
		final JMenu fileMenu=new JMenu("File");
		final JMenuItem quitMenuItem=new JMenuItem("Exit");
		quitMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					}
				});
		
		fileMenu.add(quitMenuItem);
		return fileMenu;
	}
	
	//Method to generate the game setup menu
	private JMenu SetupMenu()
	{
		final JMenu optionsMenu = new JMenu("Setup");
		final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
               getGameSetup().promptUser();
               setupUpdate(getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);

        return optionsMenu;
	}
	
	//Class to generate the basic board structure with all the tiles on it
	@SuppressWarnings("serial")
	private class BoardPanel extends JPanel
	{
		//Generate an array for board tiles as per the Board Size
		final TilePanel[][] boardTiles=new TilePanel[BoardUtils.BOARD_SIZE][BoardUtils.BOARD_SIZE];

		//Constructor
		BoardPanel()
		{
			super(new GridLayout(BoardUtils.BOARD_SIZE,BoardUtils.BOARD_SIZE));

			for(int i=0;i<BoardUtils.BOARD_SIZE;i++)
			{
				for(int j=0;j<BoardUtils.BOARD_SIZE;j++)
				{
					final TilePanel tilePanel=new TilePanel(this,i,j);
					boardTiles[i][j]=tilePanel;
					add(tilePanel);
				}
			}
			
			//Setup size, colour etc. and validate
			setPreferredSize(BOARD_PANEL_DIMENSION);
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			setBackground(Color.decode("#8B4513"));
			validate();
		}
		
		//Draw the board with the tile panels
		//First remove all the tilepanels and redraw the board
		//For every new redraw of the board, intimate the observer class
		public void drawBoard(final Board board) 
		{
            removeAll();
            for (final TilePanel[] boardTile : boardTiles) {
            	for(final TilePanel tilepanel:boardTile)
            	{
            		tilepanel.drawTile(board);
                    add(tilepanel);
            	}
                
            }
            
            validate();
            repaint();
            notifyUpdateBoard(board);
        }
		
		
	}
	
	//Class to generate an individual tile on the board with all its functionalities
	//Also contains the action where mouse is clicked on the piece and the action to be taken by the program for this
	//To select a piece - Left Click on the piece
	//to unselect on a piece- Right Click on the piece
	//Once a piece is selected, it will stay in memory till it is placed in another position as per legal moves
	@SuppressWarnings("serial")
	private class TilePanel extends JPanel
	{
		//Variables to indicate the tile X coordinate and tile Y coordinate
		private final int tileCoordinateX;
		private final int tileCoordinateY;
		
		//Constructor
		TilePanel(final BoardPanel boardPanel,
				  final int tileCoordinateX,
				  final int tileCoordinateY)
		{
			super(new GridBagLayout());
			//Set the tile coordinates
			this.tileCoordinateX=tileCoordinateX;
			this.tileCoordinateY=tileCoordinateY;
			//Set the tile dimensions
			setPreferredSize(TILE_PANEL_DIMENSION);
			//Set the tile color
			assignTileColor();
			//Set the tile pieces if any
			assignTilePieceIcon(loaBoard);
			//Add tile borders
			highlightTileBorder(loaBoard);
			//Mouse Events
			addMouseListener(new MouseListener()
			{
				//Mouse Click event
				@Override
				public void mouseClicked(final MouseEvent event) 
				{
					//If Right Mouse Click then remove all move details
					if(javax.swing.SwingUtilities.isRightMouseButton(event))
					{
						sourceTile=null;
						destinationTile=null;
						humanMovedPiece=null;
					}
					//If Left Mouse Click then if there is no move saved then save the Tile and the piece on it to be used for moving to a different destination
					else if(javax.swing.SwingUtilities.isLeftMouseButton(event))
					{
						//first click
						if(sourceTile==null)
						{
							sourceTile=loaBoard.getTile(tileCoordinateX, tileCoordinateY);
							humanMovedPiece=sourceTile.getPiece();
							//If there is no piece on a tile, then flush out the tile details
							if(humanMovedPiece==null)
							{
								sourceTile=null;
							}
						}
						//If the source tile details are already saved, then the next click to be taken as the destination tile where the piece is to be moved
						else
						{
							//Assign the destination tile
							destinationTile=loaBoard.getTile(tileCoordinateX, tileCoordinateY);
							//Create a new move using the source and destination tiles detauls
							final Move move=Move.createMove(loaBoard, sourceTile.getTileCoordinateX(), sourceTile.getTileCoordinateY(), destinationTile.getTileCoordinateX(),  destinationTile.getTileCoordinateY());
							//Generate a move and execute
						    final MoveTransition transition=loaBoard.currentPlayer().makeMove(move);
						    //Output the move details
						    String player=loaBoard.currentPlayer().getAlliance()==Alliance.WHITE?"White":"Black";
						    System.out.println("Move by Human "+player+" player :(" +move.getPieceOnCoordinate().getPiecePosition()/10+","+move.getPieceOnCoordinate().getPiecePosition()%10+") to ("+move.getDestCoordinateX()+","+move.getDestCoordinateY()+")");
						    System.out.println("********************************************");
						    //If the move is successful, then flush out the source tile, destination tile and human moved piece details
						    if(transition.getMoveStatus().isDone())
						    {
						    	//Assign the resultant board to be the new board
						    	loaBoard=transition.getTransitionBoard();
						    	sourceTile=null;
							    destinationTile=null;
							    humanMovedPiece=null;					
						    } 	    
						}
						//Redraw the board
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								boardPanel.drawBoard(loaBoard);
							}
						});
					}
				}

				//Unused Method
				@Override
				public void mousePressed(final MouseEvent e) {}
				//Unused Method
				@Override
				public void mouseReleased(final MouseEvent e) {}
				//Unused Method
				@Override
				public void mouseEntered(final MouseEvent e) {}
				//Unused Method
				@Override
				public void mouseExited(final MouseEvent e) {}
				
			});
			
			validate();
		}
		
		//Assign color to a tile
		private void assignTileColor()
		{
			//If it is even X coordinate, even Y gets light color and odd Y color gets dark color
			if(this.tileCoordinateX%2==0)
			{
				if(this.tileCoordinateY%2==0)
				{
					this.setBackground(lightColor);
				}
				else
				{
					setBackground(darkColor);
				}
			}
			//If it is odd X coordinate, even Y gets a dark color and odd Y gets light color
			else if(this.tileCoordinateX%2!=0)
			{
				if(this.tileCoordinateY%2==0)
				{
					setBackground(darkColor);
				}
				else
				{
					setBackground(lightColor);
				}
			}
		}
		
		//Assign the piece on the tile if it exists
		private void assignTilePieceIcon(Board board)
		{
			//Flush out any GUI already there
			this.removeAll();
			//If Tile is occupeid, then according to the color of the piece on the tile, assign the appropriate jpeg
			if(board.getTile(tileCoordinateX, tileCoordinateY).isTileOccupied())
			{
				try 
				{
					final BufferedImage image=ImageIO.read(ResourceLoader.load(pieceIconPath+board.getTile(tileCoordinateX, tileCoordinateY).getPiece().getPieceAlliance().toString()+".gif"));
					add(new JLabel(new ImageIcon(image)));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		//Get all the legal moves on the basis of the selected piece
		private Collection<Move> pieceLegalMoves(final Board board) 
		{
			//If the piece is of the same color as the player then return all the legal moves as per the piece
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) 
            {
                return humanMovedPiece.getLegalMoves(board);
            }
            return Collections.emptyList();
        }
		
		//When a piece is clicked to be moved, then show all the legal moves for the piece so that the human player knows their choices
		private void highlightLegals(final Board board) 
		{
			//For all legal moves, add a green dot
			for (final Move move : pieceLegalMoves(board)) 
			{
				if (move.getDestCoordinateX()== this.tileCoordinateX && move.getDestCoordinateY()==this.tileCoordinateY) 
				{
					try 
					{
						add(new JLabel(new ImageIcon(ImageIO.read(ResourceLoader.load("files/misc/green_dot.png")))));
					}
					catch (final IOException e) 
					{
						e.printStackTrace();
					}
				}
            }
        }
		
		//Draw tile with all the functionalities
		public void drawTile(final Board board)
		{
			assignTileColor();
			assignTilePieceIcon(board);
			highlightTileBorder(board);
			highlightLegals(board);
			validate();
			repaint();
		}
		
		//Highlight the tile border
		//Show if the tile piece is selected with a different color than the default color
		private void highlightTileBorder(final Board board) 
		{

			//If the tile is selected, set the tile border with CYAN color
			if(humanMovedPiece != null &&
					humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance() &&
					humanMovedPiece.getPiecePosition()/10 == this.tileCoordinateX &&
					humanMovedPiece.getPiecePosition()%10==this.tileCoordinateY) 
			{
				setBorder(BorderFactory.createLineBorder(Color.cyan));
			} 
			//Else default color
			else 
			{
				setBorder(BorderFactory.createLineBorder(Color.decode("#A0522D")));
			}
	     }
		
	}
	
	//Observer Class
	//Observes all the observable classes and sends an appropriate output
	public  class GameObserver implements Observer
	{

		@Override
		public void update(Observable o, Object arg) 
		{
			//If the board has the black player as the winner then output
			if(loaBoard.getIsBlackWinner())
			{
				JOptionPane.showMessageDialog(gameFrame,"Black Wins","Game Over",JOptionPane.PLAIN_MESSAGE);
				System.out.println("Black Wins");
				System.exit(0);
			}
			//If the board has the white player as the winner then output
			else if(loaBoard.getIsWhiteWinner())
			{
				JOptionPane.showMessageDialog(gameFrame,"White Wins","Game Over",JOptionPane.PLAIN_MESSAGE);
				System.out.println("White Wins");
				System.exit(0);
			}
			
			//If the game has Computer playing and the turn is for the Computer assigned player color, then instantiate the AIWorker Class to generate a
			//Alphabeta pruning tree and generate a move
			if(getGameSetup().isAIPlayer(loaBoard.currentPlayer()))
			{
				//Generate a move and execute
				System.out.println(loaBoard.currentPlayer().getAlliance()+" is set to AI, Computer is working on its move");
				final AIWorker worker = new AIWorker();
				worker.execute();
			}
			 
		}
		
	}
	
	//Class to instantiate the Alpha- Beta pruner and get the move and execute it
	public class AIWorker extends SwingWorker<Move, String> 
	{
		//Get the deduced move as per Alpha beta evaluation
		@Override
		protected Move doInBackground() throws Exception 
		{
			//Call AlphaBetaPruner class to generate a move
			final AlphaBetaPruner strategy = new AlphaBetaPruner();
	        loaBoard.currentPlayer().setMoveStrategy(strategy);
	        System.out.println("LOA board depth:"+getGameSetup().getSearchDepth());
	        //Generate a deduced Move
	        Move deducedMove = loaBoard.currentPlayer().getMoveStrategy().execute(loaBoard, getGameSetup().getSearchDepth());
	        //Output the deduced move
	        System.out.println("Move by Computer:(" +deducedMove.getPieceOnCoordinate().getPiecePosition()/10+","+deducedMove.getPieceOnCoordinate().getPiecePosition()%10+") to ("+deducedMove.getDestCoordinateX()+","+deducedMove.getDestCoordinateY()+")");
	        System.out.println("********************************************");
	        return deducedMove;
		}
		
		//Add the move on the board and redraw the board
		@Override
		public void done()
		{
			try 
			{
				//Get the deduced move
				final Move deducedMove = get();
				//Generate a transition board using the deduced move
			    loaBoard=loaBoard.currentPlayer().makeMove(deducedMove).getTransitionBoard();
			    //Redraw the board
			    boardPanel.drawBoard(loaBoard);
			    //Intimate the Observer Class
			    notifyUpdateBoard(loaBoard);
			    
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}

		
	}

}

