package com.loa.gui;

/*****************************************************************************************
 * Class Name: GameSetup
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
 * Initial Draft							23-Mar-2016					Pallabi
 *******************************************************************************************/


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import com.loa.engine.Alliance;
import com.loa.engine.player.Player;
import com.loa.engine.player.PlayerType;
import com.loa.engine.player.ai.SearchDepthType;


public class GameSetup extends JDialog {

	private static final long serialVersionUID = 1L;
	//Player type
	private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    //Maximum Depth to be searched upon
    private int depth;
    
    //Player type, human or computer. Values picked using PlayerType Class
    private final String[]  playerType={PlayerType.HUMAN.toString(),PlayerType.COMPUTER.toString()};
    //Difficulty level, different values of depth till which search to be done
    private final String[]  searchDepthType={SearchDepthType.DUMB.toString(),SearchDepthType.MODERATE.toString(),SearchDepthType.EXPERT.toString()};

    GameSetup(final JFrame frame, boolean modal) {
        super(frame, modal);
        final JPanel gamePanel = new JPanel(new GridLayout(0, 1));
        /*
         * White Player Dropdown
         */
        final JComboBox<String> whiteComboBox=new JComboBox<String>(playerType);
        whiteComboBox.setSelectedIndex(0);
        whiteComboBox.addActionListener(new ActionListener()
						        		{
											@Override
											public void actionPerformed(ActionEvent e) 
											{
												@SuppressWarnings("unchecked")
												JComboBox<String> whitePlayer = (JComboBox<String>) e.getSource();
												if(((String)whitePlayer.getSelectedItem()).equals(PlayerType.HUMAN.toString()))
												{
													whitePlayerType=PlayerType.HUMAN;
												}
												else
												{
													whitePlayerType=PlayerType.COMPUTER;
												}
												
											}
						        		});
        /*
         * Black Player Dropdown
         */
        final JComboBox<String> blackComboBox=new JComboBox<String>(playerType);
        blackComboBox.setSelectedIndex(0);
        blackComboBox.addActionListener(new ActionListener()
						        		{
											@Override
											public void actionPerformed(ActionEvent e) 
											{
												@SuppressWarnings("unchecked")
												JComboBox<String> blackPlayer = (JComboBox<String>) e.getSource();
												if(((String)blackPlayer.getSelectedItem()).equals(PlayerType.HUMAN.toString()))
												{
													blackPlayerType=PlayerType.HUMAN;
												}
												else
												{
													blackPlayerType=PlayerType.COMPUTER;
												}
												
											}
						        		});
        
        /*
         * Difficulty Level DropDown
         */
        final JComboBox<String> difficultyLevel=new JComboBox<String>(searchDepthType);
        difficultyLevel.setSelectedIndex(0);
        difficultyLevel.addActionListener(new ActionListener()
        		{
		        	@Override
					public void actionPerformed(ActionEvent e) 
					{
		        		@SuppressWarnings("unchecked")
		        		JComboBox<String> selectedDifficultyLevel = (JComboBox<String>) e.getSource();
		        		System.out.println((String)selectedDifficultyLevel.getSelectedItem());
		        		if(((String)selectedDifficultyLevel.getSelectedItem()).equals(SearchDepthType.DUMB.toString()))
		        		{
		        			depth=SearchDepthType.DUMB.getDepth();
		        		}
		        		else if(((String)selectedDifficultyLevel.getSelectedItem()).equals(SearchDepthType.MODERATE.toString()))
		        		{
		        			depth=SearchDepthType.MODERATE.getDepth();
		        		}
		        		else if(((String)selectedDifficultyLevel.getSelectedItem()).equals(SearchDepthType.EXPERT.toString()))
		        		{
		        			depth=SearchDepthType.EXPERT.getDepth();
		        		}
		        		
					}
        		});
        
        getContentPane().add(gamePanel);
        
        gamePanel.add(new JLabel("White"));
        gamePanel.add(whiteComboBox);
        gamePanel.add(new JLabel("Black"));
        gamePanel.add(blackComboBox);

        gamePanel.add(new JLabel("Difficulty Level, if playing with Computer"));
        gamePanel.add(difficultyLevel);

        //Cancel and Ok Button
        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        gamePanel.add(cancelButton);
        gamePanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    //Return the appropriate color for the Computer
    boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    //Getter for whitePlayerType
    PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    //Getter for blackPlayerType
    PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    //Getter for depth
    int getSearchDepth() {
    	if(this.depth==0)
    		return SearchDepthType.DUMB.getDepth();
        return this.depth;
    }
}
