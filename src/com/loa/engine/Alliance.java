package com.loa.engine;
/*****************************************************************************************
 * Class Name: Alliance
 * Description: Enum with the two colours of Players and related methods
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

import com.loa.engine.player.BlackPlayer;
import com.loa.engine.player.Player;

//Enum for colours for the players
public enum Alliance {
	//Starting Black
	BLACK {
		//isBlack to be true for Black
		@Override
		public boolean isBlack() {
			return true;
		}
		//isWhite to be false for Black
		@Override
		public boolean isWhite() {
			return false;
		}
		//Black chooses Black Player
		public  Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer)
		{
			return blackPlayer;
		}
	},
	WHITE {
		//isBlack to be false for White
		@Override
		public boolean isBlack() {
			return false;
		}
		
		//isWhite to be true for White
		@Override
		public boolean isWhite() {
			return true;
		}
		
		//White chooses White Player
		public  Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer)
		{
			return whitePlayer;
		}
	};
	
	//Abstract method to check if the Player is black
	public abstract boolean isBlack();
	//Abstract method to check if the Player is white
	public abstract boolean isWhite();
	//Abstract method for choice of the Player for a particular colour
	public abstract Player choosePlayer(Player whitePlayer, BlackPlayer blackPlayer);

}
