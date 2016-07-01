package com.loa.engine.player.ai;

import java.util.Collection;
import java.util.Comparator;

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


import java.util.Observable;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.loa.engine.Alliance;
import com.loa.engine.board.Board;
import com.loa.engine.board.Move;
import com.loa.engine.player.MoveTransition;

//Class extends Observable so that the results can observed by a Observer Class and catch the actions done by the class
public class AlphaBetaPruner extends Observable implements MoveStrategy {
	//Number of Moves Evaluated
	int numMovesEvaluated;
	//Number of Moves Pruned in Max Method
	int numMaxMovesPruned;
	//Number of Moves Pruned in Min Method
	int numMinMovesPruned;
	//Number of times evaluation method called in Max Method
	int numMaxEvalCalled;
	//number of times evaluation method called in Min Method
	int numMinEvalCalled;
	//Starting depth the search started
	int startingDepth;
	//Depth till which the search was done
	int endDepth;
	//Maximum Depth till which search to be done
	int maxDepth;
	//Object of Evaluator class
	BoardEvaluator evaluator;
	//start time, end time of the search
	long startTime;
	long endTime;
	MoveSorter moveSorter;
	
	
	//Constructor
	public AlphaBetaPruner()
	{
		this.numMovesEvaluated=0;
		this.numMaxMovesPruned=0;
		this.numMinMovesPruned=0;
		this.numMaxEvalCalled=0;
		this.numMinEvalCalled=0;
		this.moveSorter = MoveSorter.SORT;
		this.evaluator=new BoardEvaluator();
		this.startTime=System.currentTimeMillis();
	}

	//Getter for numMovesEvaluated
	@Override
	public long getNumMovesEvaluated() {
		return this.numMovesEvaluated;
	}

	/*
	 * White being Max
	 * Black being Min
	 */
	@Override
	public Move execute(Board board, int providedDepth) {
		/*Set Alpha and Beta with maximum and minimum values of Integers*/
		int alpha=Integer.MIN_VALUE;
		int beta=Integer.MAX_VALUE;
		
		// v value
		int currentValue;
		//Instantiate a Null or Illegal Move
		Move deducedMove=Move.NULL_MOVE;
		
		//Starting Depth=0
		this.startingDepth=0;
		//Maximum Depth as specified by the function
		this.maxDepth=providedDepth;
		
		//For all legal moves of the player
		for (final Move move : this.moveSorter.sort(board.currentPlayer().getLegalMoves())) 
		{
			//System.out.println("Move is:"+move.getPieceOnCoordinate().getPiecePosition()+" to "+move.getDestCoordinateX()+","+move.getDestCoordinateY());
			//Run alpha beta pruning
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) 
            {
				currentValue= max(moveTransition.getTransitionBoard(), alpha, beta, 0,board.currentPlayer().getAlliance());
				//System.out.println("Current Score:"+currentValue);
				if(alpha<currentValue)
				{
					alpha = currentValue;
		            deducedMove = move;
		            setChanged();
		            notifyObservers(deducedMove);   
				}
            }
            
        }
		
		System.out.println("Maximum depth of game tree reached:" +(endDepth+1-startingDepth)); 
		System.out.println("Total number of nodes generated:"+numMovesEvaluated);
		System.out.println("Number of times the evaluation function was called within the MAX-VALUE function:"+numMaxEvalCalled);
		System.out.println("Number of times the evaluation function was called within the MIN-VALUE function:"+numMinEvalCalled);
		System.out.println("Number of times pruning occurred within the MAX-VALUE function:"+numMaxMovesPruned);
		System.out.println("Number of times pruning occurred within the MIN-VALUE function:"+numMinMovesPruned);

		return deducedMove;
	}
	
	
	//Max function for Alpha beta pruning
	private int max(Board transitionBoard,  int alpha, int beta, int depth,Alliance maxPlayer) 
	{
		//System.out.println("Max:"+depth);
		int v=Integer.MIN_VALUE;

		//If terminal condition is reached then run the utility function
        if (terminalTest(transitionBoard))
		{
        	//System.out.println("Terminal Score:"+utility(transitionBoard,maxPlayer));
        	return utility(transitionBoard,maxPlayer);
		}
        //Run the cut off if the cut off time is elapsed or the maximum depth is exceeded
		else if(cutOffTimeElapsed()||depth>=this.maxDepth)
		{
			this.numMovesEvaluated++;
			this.numMaxEvalCalled++;
			//System.out.println("Eval Score:"+this.evaluator.evaluate(transitionBoard, depth, maxPlayer));
			return this.evaluator.evaluate(transitionBoard, depth, maxPlayer);
		}
		else
		{
			if(this.endDepth<depth)
			{
				this.endDepth=depth;
			}
			for (final Move move : this.moveSorter.sort(transitionBoard.currentPlayer().getLegalMoves())) 
			{
				this.numMovesEvaluated++;
				//System.out.println("Move Data:"+move.getPieceOnCoordinate().getPiecePosition()/10+","+move.getPieceOnCoordinate().getPiecePosition()%10+" to "+move.getDestCoordinateX()+","+move.getDestCoordinateY() );
				
				//Generate a move transition for move picked
				final MoveTransition moveTransition = transitionBoard.currentPlayer().makeMove(move);
				if (moveTransition.getMoveStatus().isDone()) 
				{
					//Get the maximum of the values returned by the min method
					v = Math.max(v,min(moveTransition.getTransitionBoard(), alpha, beta, depth+1,maxPlayer));
					//System.out.println("Score out of max:"+v);
					//If v is greater than beta then prune
					if (v>=beta) 
					{
						this.numMaxMovesPruned++;
						return v;
					}
					//else assign max between alpha and v to alpha
					else
					{
						//System.out.println("alpha:"+alpha+" v:"+v);
						alpha=Math.max(alpha, v);
					}
				}
			}
		}
        //System.out.println("Score:"+v);
		return v;
	}

	//Min function for Alpha beta pruning
	private int min(Board transitionBoard,  int alpha, int beta, int depth,Alliance maxPlayer) 
	{
		//System.out.println("Min:"+depth);
		//Take v as the maximum possible vale
        int v=Integer.MAX_VALUE;
        
        //If terminal condition is reached then run the utility function
        if (terminalTest(transitionBoard))
		{
        	//System.out.println("Terminal Score:"+utility(transitionBoard,maxPlayer));
        	return utility(transitionBoard,maxPlayer);
		}
		else
		{
			if(this.endDepth<depth)
			{
				this.endDepth=depth;
			}
			
			//for all legal moves for the current user
			for (final Move move : this.moveSorter.sort(transitionBoard.currentPlayer().getLegalMoves())) 
			{
				//Increment the number of moves evaluated
				this.numMovesEvaluated++;
				//System.out.println("Move Data:"+move.getPieceOnCoordinate().getPiecePosition()/10+","+move.getPieceOnCoordinate().getPiecePosition()%10+" to "+move.getDestCoordinateX()+","+move.getDestCoordinateY() );
				
				//Generate a move transition for move picked
                final MoveTransition moveTransition = transitionBoard.currentPlayer().makeMove(move);
                if (moveTransition.getMoveStatus().isDone()) 
                {
                	//Get the maximum of the values returned by the min method
                    v = Math.min(v,max(moveTransition.getTransitionBoard(),alpha, beta, depth+1,maxPlayer));
                    //System.out.println("Score out of min:"+v);
                    //Prune if v is lesser than alpha
                    if (v <= alpha) 
                    {
                        this.numMinMovesPruned++;
                        return v;
                    }
                  //else assign min between alpha and v to beta
                    else
                    {
                    	//System.out.println("beta:"+beta+" v:"+v);
                    	beta=Math.min(beta, v);
                    }
                }
			}
		}
        
        //System.out.println("Score:"+v);
		return v;
	}
	
	//Run the Terminal test to check if someone has won the game
	private boolean terminalTest(Board board)
	{
		return board.getIsBlackWinner()||board.getIsWhiteWinner();
	}
	
	//Check if the cut off elapsed
	public boolean cutOffTimeElapsed()
	{
		long currTime=System.currentTimeMillis();
		if ((currTime-this.startTime)/1000>=10)
		{
			return true;
		}
		
		return false;
	}
	
	//Utility function to be run if the terminal test passes
	private int utility(Board board,Alliance maxPlayer)
	{
		//If the Black is the max player and Black wins then give a higher score and if white is the max player then give higher score to
		//white player
		if(maxPlayer==Alliance.BLACK)
    	{

    		if(board.getIsBlackWinner())
    		{
    			return 100;
    		}
    		else
    		{
    			return -100;
    		}

    	}
    	else
    	{
    		if(board.getIsBlackWinner())
    		{
    			return -100;
    		}
    		else
    		{
    			return 100;
    		}
    	}
	}
	
	//Class to sort the Moves so that the more favourable moves can be first run
	private enum MoveSorter {

        SORT {
            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return Ordering.from(MAX_SORT).immutableSortedCopy(moves);
            }
        };

        public static Comparator<Move> MAX_SORT= new Comparator<Move>() {
            @Override
            public int compare(final Move move1, final Move move2) {
                return ComparisonChain.start()
                        .compare(move2.checkForDistanceBetweenPieces(), move1.checkForDistanceBetweenPieces())
                        .result();
            }
        };

        abstract Collection<Move> sort(Collection<Move> moves);
    }

	
}
