package com.loa.engine.player.ai;
/*****************************************************************************************
 * Class Name: SearchDepthType
 * Description: Enum to get the difficulty level of the game
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


public enum SearchDepthType {
	
	 DUMB {
		 //Overriden toString
		@Override
		public String toString() {
			return "Dumb";
		}

		//Maximum Depth to be searched upon
		@Override
		public int getDepth() {
			return 2;
		}
	}
    ,MODERATE {
    	 //Overriden toString
		@Override
		public String toString() {
			return "Moderate";
		}

		//Maximum Depth to be searched upon
		@Override
		public int getDepth() {
			return 10;
		}
	}
	,EXPERT {
		 //Overriden toString
		@Override
		public String toString() {
			return "Expert";
		}

		//Maximum Depth to be searched upon
		@Override
		public int getDepth() {
			return 50;
		}
	};
	
	//Depth till which the search to run
	public abstract int getDepth();
	

}
