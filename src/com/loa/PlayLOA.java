package com.loa;
/*****************************************************************************************
 * Class Name: PlayLOA
 * Description: Invoker Class with the main function which instantiates the GUI generating 
 * class
 * Reference
 * -----------
 * 1) https://www.youtube.com/watch?v=Cm70y54cDIo
 * 
 * Changes Done
 * --------------
 * Description of Changes		           Date of Modification         Modification Done By
 * Initial Draft							23-Mar-2016					Pallabi
 *******************************************************************************************/
import com.loa.gui.LoaGui;
//Invoker class for running the LOA game
public class PlayLOA {
	
	//Main class just instantiates the actual GUI class which generates the GUI and performs all the operations
	public static void main(String[] args) {
		
		@SuppressWarnings("unused")
		LoaGui gui=new LoaGui();
		
	}

}
