package com.loa.engine.player;

/*****************************************************************************************
 * Class Name: PlayerType
 * Description: Enum for the Player Types
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


public enum PlayerType {
	//Human Player
	HUMAN {
		@Override
		public String toString() {
			return "Human";
		}
	},
	//Computer Player
	COMPUTER {
		@Override
		public String toString() {
			return "Computer";
		}
	};

}
