/**********************************************************
 * Program Name   :  SnakeMain
 * Author         :  Adam Yerden
 * Date           :	 5/15/2019
 * Course/Section :  CSC-264-501
 * Program Description: This program will creat a new jframe.
 *			Create a new Gameplay and add it to the panel then diplay it
 *
 * Methods:
 * -------
 * main - mainn method
 **********************************************************/
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class SnakeMain
{
	/**********************************************************
	* Method Name    : main
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: creates a new jframe and creates a new gameplay
	*			in the jframe
	*
	* BEGIN main
	*	name the JFrame
	*	create new Gameplay
	*	set frame bounds
	*	set frame to not be resizeable
	*	set frame to be visible
	*	set default close opperations
	*	add Gameplay
	* END main
	**********************************************************/
	public static void main(String[] args)
	{
		//name the JFrame
		JFrame frame = new JFrame ("Snake");

		//create new Gameplay
		Gameplay Gameplay = new Gameplay();

		//set frame bounds
		frame.setBounds(10, 10, 905, 700);

		//set frame to not be resizeable
		frame.setResizable(false);

		//set frame to be visible
		frame.setVisible(true);

		//set default close opperations
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//add Gameplay
		frame.add(new Gameplay());
	}//end main
}//end SnakeMain