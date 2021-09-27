/**********************************************************
 * Program Name   :  Gamepaly
 * Author         :  Adam Yerden
 * Date           :	 5/15/2019
 * Course/Section :  CSC-264-501
 * Program Description: This program will create a game that the
 *		user controls a snake arouns the screen and collects the
 *		target every time the snake runs into the target the snake
 *		gets longer and the user continues to play until they run
 *		into themself or the wall the player si scores by how many
 *		targets it collects and keeps track of the high score
 *
 * Methods:
 * --------------------------------------------------------
 * Gameplay - constructor
 * paint - draws and colors the game
 * actionPerformed - keeps track of the timers
 * keyPressed - keeps track of when the buttons on the keyboard are pressed
 * moveTarget - moves target to a new random location
 * gameOver - determines what dead snake face to display
 *  		  displays game over and checks if high score wa beat
 **********************************************************/
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	private int[] targetXPos = {25,50,75,100,125,150,175,200,225,		//array that holds the x positions of the target
			250,275,300,325,350,375,400,425,450,475,500,525,550,
			575,600,625,650,675,700,725,750,775,800,825,850};
	private int[] targetYPos = {75,100,125,150,175,200,225,250,275,		//array that holds the y positions of the target
			300,325,350,375,400,425,450,475,500,525,550,575,600,625};
	private int[] snakeXLength = new int [750];							//array that holds the x position of the snake pieces
	private int[] snakeYLength = new int [750];							//array that holds the y position of the snake pieces
	private boolean left = false;		//true when snake is going left
	private boolean right = false;		//true when snake is going right
	private boolean up = false;			//true when snake is going up
	private boolean down = false;		//true when snake is going down
	private boolean leftFail = false;	//true if snake was going left when it crashed
	private boolean rightFail = false;	//true if snake was going right when it crashed
	private boolean upFail = false;		//true if snake was going up when it crashed
	private boolean downFail = false;	//true if snake was going down when it crashed
	private boolean collision = false;	//true when the snake has a collision with wall or itself used to keep head in view and keep snake same length
	private boolean endGame = false;	//if true displayes GAME OVER
	private boolean movable = false;	//used to make sure snake does not turn 360 degrees if arrows are hit quickly
	private ImageIcon leftMouth;		//snake head facing to the left
	private ImageIcon rightMouth;		//snake head facing to the right
	private ImageIcon upMouth;			//snake head facing to the up
	private ImageIcon downMouth;		//snake head facing to the down
	private ImageIcon snakeImage;		//snake body
	private ImageIcon targetImage;		//the snake's target
	private ImageIcon deadLeft;			//dead snake head when collides going left
	private ImageIcon deadRight;		//dead snake head when collides going right
	private ImageIcon deadUp;			//dead snake head when collides going up
	private ImageIcon deadDown;			//dead snake head when collides going down
	private Timer timer;				//slows down the snake
	private Timer end;					//pauses the GAME OVER display
	private int score = 0;				//keeps track of user score
	private int highScore = 0;			//keeps track of high score
	private int snakeLength = 3;		//keeps track of the length of the snake
	private int moves = 0;				//keeps track of number of moves
	private int count = 0;				//makes GAME OVER Display diapeare after spacebar is hit
	private final int SNAKE_DELAY = 60;	//speed of the snake
	private final int DELAY = 400;		//GAME OVER dispaly delay time
	private Random rand = new Random();	//Random int generator
	private int xPos = rand.nextInt(34);//random x array position for target
	private int yPos = rand.nextInt(23);//random y array position for target

	/**********************************************************
	* Method Name    : Gameplay
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: constructor method
	*
	* BEGIN Gameplay
	*	adds Key Listener
	*	sets focus to window
	*	sets timer for snake to SNAKE_DELAY miliseconds
	*	sets end for game over to DELAY miliseconds
	* END Gamepaly
	**********************************************************/
	public Gameplay()
	{
		//adds Key Listener
		addKeyListener(this);

		//sets focus to window
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		//sets timer for snake to SNAKE_DELAY miliseconds
		timer = new Timer(SNAKE_DELAY, this);
		timer.start();

		//sets end for game over to DELAY miliseconds
		end = new Timer(DELAY, this);
	}
	/**********************************************************
	* Method Name    : paint
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: paints the game board and paints the moving snake
	*
	* BEGIN paint
	*	draw and background
	*	draw paly area
	*	draw score and high score
	*	draw title
	*	IF game is reset
	*		reset booleans to false
	*		set x position fro first tree parts of the snake
	*		set y position fro first tree parts of the snake
	*		draw snake
	*	END IF game is reset
	*	FOR each part of the snake
	*		IF it is the first piece of the snake and right is true
	*			draw snake head right at position snakeXLength[a], snakeYLength[a]
	*		ELSE IF it is the first piece of the snake and left is true
	*			draw snake head left at position snakeXLength[a], snakeYLength[a]
	*		ELSE IF it is the first piece of the snake and up is true
	*			draw snake head up at position snakeXLength[a], snakeYLength[a]
	*		ELSE IF it is the first piece of the snake and down is true
	*			draw snake head down at position snakeXLength[a], snakeYLength[a]
	*		ELSE IF it is the first piece of the snake
	*			draw snake body at position snakeXLength[a], snakeYLength[a]
	*		END IF it is the first piece of the snake and right is true
	*	END FOR each part of the snake
	*	IF rightFail is true
	*		draw snake head facing right
	*	ELSE IF leftFail is true
	*		draw snake head facing left
	*	ELSE IF upFail is true
	*		draw snake head facing up
	*	ELSE IF downFail is true
	*		draw snake head facing down
	*	END IF rightFail is true
	*	IF game is over
	*		IF count equals 1
	*			sets color to red and Font and writes GAME OVER
	*			sets color to black and font and writes SCORE
	*			sets color to blue and writes the score
	*			sets color to black and writes HIGH SCORE
	*			sets color to blue and writes the high score
	*			sets color to black and writes instructions to hit spacebar to rest
	*		END IF count equals 1
	*	END IF game is over
	*	IF snake has collided with wall or itself
	*		draw new body piece at END of snake to keep snake at correct size
	*		start timer
	*	END IF snake has collided with wall or itself
	*	creates new image of snake target
	*	IF the snake runs into the target
	*		add 1 to score and snake length
	*		call move target method and repaint
	*	END IF the snake runs into the target
	*	FOR all snake parts
	*		IF the head of the snake hits the body of the snake
	*			call game over method
	*		END IF the head of the snake hits the body of the snake
	*	END FOR all snake parts
	*	dispose and repaint
	* END paint
	**********************************************************/
	public void paint(Graphics g)
	{
		//draw and background
		g.setColor(new Color(210,76,0));
		g.fillRect(0, 0, 905, 700);

		//draw paly area
		g.setColor(new Color(0,204,0));
		g.fillRect(25, 75, 850, 575);

		//draw score and high score
		g.setColor(Color.yellow);
		g.setFont(new Font("Serif", Font.BOLD, 30));
		g.drawString("SCORE: " + score, 25, 50);
		g.drawString("HIGH SCORE: " + highScore , 625,50);

		//draw title
		g.setColor(new Color(222,189,255));
		g.setFont(new Font("Serif", Font.BOLD, 50));
		g.drawString("SNAKE", 325,50);

		//if game is reset
		if(moves == 0)
		{
			//reset booleans to false
			leftFail = false;
			rightFail = false;
			upFail = false;
			downFail = false;
			collision = false;

			//set x position fro first tree parts of the snake
			snakeXLength[2] = 50;
			snakeXLength[1] = 75;
			snakeXLength[0] = 100;

			//set y position fro first tree parts of the snake
			snakeYLength[2] = 100;
			snakeYLength[1] = 100;
			snakeYLength[0] = 100;

			//draw snake
			rightMouth = new ImageIcon("right.png");
			rightMouth.paintIcon(this, g, snakeXLength[0], snakeYLength[0]);

		}//end if game is reset

		//for each part of the snake
		for(int a = 0; a < snakeLength; a++)
		{
			//if it is the first piece of the snake and right is true
			if(a == 0 && right)
			{
				//draw snake head right at position snakeXLength[a], snakeYLength[a]
				rightMouth = new ImageIcon("right.png");
				rightMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
			}

			//else if it is the first piece of the snake and left is true
			else if(a == 0 && left)
			{
				//draw snake head left at position snakeXLength[a], snakeYLength[a]
				leftMouth = new ImageIcon("left.png");
				leftMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
			}

			//else if it is the first piece of the snake and up is true
			else if(a == 0 && up)
			{
				//draw snake head up at position snakeXLength[a], snakeYLength[a]
				upMouth = new ImageIcon("up.png");
				upMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
			}
			//else if it is the first piece of the snake and down is true
			else if(a == 0 && down)
			{
				//draw snake head down at position snakeXLength[a], snakeYLength[a]
				downMouth = new ImageIcon("down.png");
				downMouth.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
			}
			//else if it is the first piece of the snake
			else if(a!=0)
			{
				//draw snake body at position snakeXLength[a], snakeYLength[a]
				snakeImage = new ImageIcon("body.png");
				snakeImage.paintIcon(this, g, snakeXLength[a], snakeYLength[a]);
			}//end if it is the first piece of the snake and right is true
		}//end for each part of the snake

		//if rightFail is true
		if(rightFail)
		{
			//draw snake head facing right
			deadRight = new ImageIcon("deadRight.png");
			deadRight.paintIcon(this, g, snakeXLength[1], snakeYLength[1]);
		}
		//else if leftFail is true
		else if(leftFail)
		{
			//draw snake head facing left
			deadLeft = new ImageIcon("deadLeft.png");
			deadLeft.paintIcon(this, g, snakeXLength[1], snakeYLength[1]);
		}
		//else if upFail is true
		else if(upFail)
		{
			//draw snake head facing up
			deadUp = new ImageIcon("deadUp.png");
			deadUp.paintIcon(this, g, snakeXLength[1], snakeYLength[1]);
		}
		//else if downFail is true
		else if(downFail)
		{
			//draw snake head facing down
			deadDown = new ImageIcon("deadDown.png");
			deadDown.paintIcon(this, g, snakeXLength[1], snakeYLength[1]);
		}//end if rightFail is true

		//if game is over
		if(endGame)
		{
			//if count equals 1
			if(count == 1)
			{
				//sets color to red and Font and writes GAME OVER
				g.setColor(Color.red);
				g.setFont(new Font("Serif", Font.BOLD, 135));
				g.drawString("GAME OVER", 25,300);

				//sets color to black and font and writes SCORE
				g.setColor(Color.black);
				g.setFont(new Font("Serif", Font.BOLD, 50));
				g.drawString("SCORE:", 50,350);

				//sets color to blue and writes the score
				g.setColor(Color.blue);
				g.drawString(""+score, 250, 350);

				//sets color to black and writes HIGH SCORE
				g.setColor(Color.black);
				g.drawString("HIGH SCORE:", 450,350);

				//sets color to blue and writes the high score
				g.setColor(Color.blue);
				g.drawString(""+highScore, 800, 350);

				//sets color to black and writes instructions to hit spacebar to rest
				g.setColor(Color.black);
				g.setFont(new Font("Serif", Font.BOLD, 25));
				g.drawString("Press Spacebar to Play Again", 300,400);

			}//end if count equals 1
		}//end if game is over

		//if snake has collided with wall or itself
		if(collision)
		{
			//draw new body piece at end of snake to keep snake at correct size
			snakeImage = new ImageIcon("body.png");
			snakeImage.paintIcon(this, g, snakeXLength[snakeLength], snakeYLength[snakeLength]);

			//start timer
			end.start();
		}//end if snake has collided with wall or itself

		//creates new image of snake target
		targetImage = new ImageIcon("flower.png");
		targetImage.paintIcon(this, g, targetXPos[xPos], targetYPos[yPos]);

		//if the snake runs into the target
		if (targetXPos[xPos] == snakeXLength[0] && targetYPos[yPos] == snakeYLength[0])
		{
			//add 1 to score and snake length
			score++;
			snakeLength ++;

			//call move target method and repaint
			moveTarget();
			repaint();
		}//end if the snake runs into the target

		//for all snake parts
		for(int b = 1; b < snakeLength; b++)
		{
			//if the head of the snake hits the body of the snake
			if(snakeXLength[b] == snakeXLength[0] && snakeYLength[b] == snakeYLength[0])
			{
				//call game over method
				gameOver();
			}//end if the head of the snake hits the body of the snake
		}//end for all snake parts

		//dispose and repaint
		g.dispose();
		repaint();
	}//end paint

	/**********************************************************
	* Method Name    : actionPerformed
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: controls the timers
	*
	* BEGIN actionPerformed
	*	start timer
	*	IF right
	*		FOR all snake parts
	*			move snake part one space right
	*		END FOR all snake parts
	*		FOR all snake parts
	*			IF r is 0
	*				move head right 25 pixels
	*				move snake piece to the previous position in array
	*			END IF r is 0
	*			IF the snake hits the wall
	*				call game over method
	*			END IF the snake hits the wall
	*		END FOR all snake parts
	*		repaint
	*	ELSE IF left is true
	*		FOR all snake parts
	*			move snake part one space left
	*		END FOR all snake parts
	*		FOR all snake parts
	*			IF r is 0
	*				move head left 25 pixels
	*				move snake part back in array position
	*			END IF r is 0
	*			IF snake hits the wall
	*				call game over method
	*		END FOR all snake parts
	*		repaint
	*	ELSE IF down is true
	*		FOR all snake parts
	*			move snake part one space down
	*		END FOR all snake parts
	*		FOR all snake parts
	*			IF r is 0
	*				move snake head 25 pixels down
	*				move body piece over one in array
	*			END IF r is 0
	*			IF snake hits the wall
	*				call game over method
	*		END FOR all snake parts
	*	ELSE IF up is true
	*		FOR all snake parts
	*			move snake part one space up
	*		END FOR all snake parts
	*		FOR all snake parts
	*			IF r is 0
	*				move snake head up 25 pixels
	*			ELSE
	*				move snake body back in array position
	*			END IF r is 0
	*			IF snake hits the wall
	*				call game over method
	*			END IF snake hits the wall
	*		END FOR all snake parts
	*		repaint
	*	END IF left is true
	*	IF END timer starts
	*		when END timer stops END game is true
	*	END IF END timer starts
	*	set movable to true
	* END actionPerFORmed
	**********************************************************/
	public void actionPerformed(ActionEvent e)
	{
		//start timer
		timer.start();

		//if right
		if(right)
		{
			//for all snake parts
			for(int r = snakeLength - 1; r >= 0; r--)
			{
				//move snake part one space right
				snakeYLength[r+1] = snakeYLength[r];
			}//end for all snake parts
			//for all snake parts
			for(int r = snakeLength; r >= 0; r--)
			{
				//if r is 0
				if(r == 0)
				{
					//move head right 25 pixels
					snakeXLength[r] = snakeXLength[r] + 25;
				}
				else
				{
					//move snake piece to the previous position in array
					snakeXLength[r] = snakeXLength[r - 1];
				}//end if r is 0

				//if the snake hits the wall
				if(snakeXLength[r] > 850)
				{
					//call game over method
					gameOver();
				}//end if the snake hits the wall
			}//end for all snake parts

			//repaint
			repaint();
		}
		//else if left is true
		else if(left)
		{
			//for all snake parts
			for(int r = snakeLength - 1; r >= 0; r--)
			{
				//move snake part one space left
				snakeYLength[r+1] = snakeYLength[r];
			}//end for all snake parts
			//for all snake parts
			for(int r = snakeLength; r >= 0; r--)
			{
				//if r is 0
				if(r == 0)
				{
					//move head left 25 pixels
					snakeXLength[r] = snakeXLength[r] - 25;
				}
				else
				{
					//move snake part back in array position
					snakeXLength[r] = snakeXLength[r - 1];
				}//end if r is 0
				//if snake hits the wall
				if(snakeXLength[r] < 25)
				{
					//call game over method
					gameOver();
				}
			}//end for all snake parts
			//repaint
			repaint();
		}
		//else if down is true
		else if(down)
		{
			//for all snake parts
			for(int r = snakeLength - 1; r >= 0; r--)
			{
				//move snake part one space down
				snakeXLength[r+1] = snakeXLength[r];
			}//end for all snake parts
			//for all snake parts
			for(int r = snakeLength; r >= 0; r--)
			{
				//if r is 0
				if(r == 0)
				{
					//move snake head 25 pixels down
					snakeYLength[r] = snakeYLength[r] + 25;
				}
				else
				{
					//move body piece over one in array
					snakeYLength[r] = snakeYLength[r - 1];
				}//end if r is 0
				//if snake hits the wall
				if(snakeYLength[r] > 625)
				{
					//call game over method
					gameOver();
				}
			}//end for all snake parts
			repaint();
		}
		//else if up is true
		else if(up)
		{
			//for all snake parts
			for(int r = snakeLength - 1; r >= 0; r--)
			{
				//move snake part one space up
				snakeXLength[r+1] = snakeXLength[r];
			}//end for all snake parts
			//for all snake parts
			for(int r = snakeLength; r >= 0; r--)
			{
				//if r is 0
				if(r == 0)
				{
					//move snake head up 25 pixels
					snakeYLength[r] = snakeYLength[r] - 25;
				}
				else
				{
					//move snake body back in array position
					snakeYLength[r] = snakeYLength[r - 1];
				}//end if r is 0
				//if snake hits the wall
				if(snakeYLength[r] < 75)
				{
					//call game over method
					gameOver();
				}//end if snake hits the wall
			}//end for all snake parts

			//repaint
			repaint();
		}//end if left is true
		//if end timer starts
		if(e.getSource() == end)
		{
			//when end timer stops end game is true
			end.stop();
			endGame = true;
		}//end if end timer starts

		//set movable to true
		movable = true;
	}//end ActionListener

	/**********************************************************
	* Method Name    : keyPressed
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: controls what happens when buttons are pressed
	*
	* BEGIN keyPressed
	*	 set key equal to key code
	*	IF the snake collided with something
	*		IF spacebar is hit
	*			reset game
	*		END IF spacebar is hit
	*	ELSE IF the snake has move one space after turning
	*		IF left arow key is pressed
	*			add 1 to moves
	*			IF the snake was not going in the oposite direction
	*				left is true
	*			ELSE
	*				left is false right is true(continues going right)
	*			END IF the snake was not going in the oppisite direction
	*			set other direction booleans to false
	*		ELSE IF the right arrow key is pressed
	*			add one to moves
	*			IF the snake is not going in the opposite direction
	*				right is true
	*				right is false left is true
	*			END IF the snake is not going in the opposite direction
	*			set other direction booleans to false
	*		ELSE IF the up arrow is pressed
	*			add one to moves
	*			IF the snake is not going in the opposite direction
	*				up is true
	*			ELSE
	*				up is false down is true
	*			END IF the snake is not going in the opposite direction
	*			set the rest of the directional booleans to false
	*		ELSE IF the down arrow key is pressed
	*			add one to moves
	*			IF the snake is not going in the opposite direction
	*				down is true
	*			ELSE
	*				down is false up is true
	*			set the rest of the directional booleans to false
	*		END IF right arrow key is pressed
	*		set movable to false
	*	END IF the snake collided with something
	* END keyPressed
	**********************************************************/
	public void keyPressed(KeyEvent e)
	{
		// set key equal to key code
		int key = e.getKeyCode();

		//if the snake collided with something
		if(rightFail || downFail || upFail || leftFail)
		{
			//if spacebar is hit
			if (key == KeyEvent.VK_SPACE)
			{
				//reset game
				collision = false;
				endGame = false;
				left = false;
				right = false;
				up = false;
				down = false;
				leftFail = false;
				rightFail = false;
				upFail = false;
				downFail = false;
				moves = 0;
				score = 0;
				count = 0;
				snakeLength = 3;
				repaint();
			}//end if spacebar is hit
		}
		//else if the snake has move one space after turning
		else if(movable)
		{
			//if left arow key is pressed
			if (key == KeyEvent.VK_LEFT)
			{
				//add 1 to moves
				moves++;

				//if the snake was not going in the oposite direction
				if(!right)
				{
					//left is true
					left= true;
				}
				else
				{
					//left is false right is true(continues going right)
					left = false;
					right = true;
				}//end if the snake was not going in the oppisite direction

				//set other direction booleans to false
				up = false;
				down = false;
				leftFail = false;
				rightFail = false;
				upFail = false;
				downFail = false;
			}
			//else if the right arrow key is pressed
			else if (key == KeyEvent.VK_RIGHT)
			{
				//add one to moves
				moves++;

				//if the snake is not going in the opposite direction
				if(!left)
				{
					//right is true
					right = true;
				}
				else
				{
					//right is false left is true
					right = false;
					left = true;
				}//end if the snake is not going in the opposite direction
				//set other direction booleans to false
				up = false;
				down = false;
				leftFail = false;
				rightFail = false;
				upFail = false;
				downFail = false;
			}
			//else if the up arrow is pressed
			else if (key == KeyEvent.VK_UP)
			{
				//add one to moves
				moves++;

				//if the snake is not going in the opposite direction
				if(!down)
				{
					//up is true
					up = true;
				}
				else
				{
					//up is false down is true
					up = false;
					down = true;
				}//end if the snake is not going in the opposite direction
				//set the rest of the directional booleans to false
				right = false;
				left = false;
				leftFail = false;
				rightFail = false;
				upFail = false;
				downFail = false;
			}
			//else if the down arrow key is pressed
			else if (key == KeyEvent.VK_DOWN)
			{
				//add one to moves
				moves++;

				//if the snake is not going in the opposite direction
				if(!up)
				{
					//down is true
					down = true;
				}
				else
				{
					//down is false up is true
					down = false;
					up = true;
				}
				//set the rest of the directional booleans to false
				right = false;
				left = false;
				leftFail = false;
				rightFail = false;
				upFail = false;
				downFail = false;
			}//end if right arrow key is pressed

			//set movable to false
			movable = false;

		}//end if the snake collided with something
	}//end keyListener

	//remaining keyListeners
	public void keyReleased(KeyEvent e)
	{

	}
	public void keyTyped(KeyEvent e)
	{

	}

	/**********************************************************
	* Method Name    : moveTarget
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: moves target to a new random location
	*
	* BEGIN moveTarget
	*	creates new randomly generated x and y target positions
	*	FOR all of the snake pieces
	*		IF the target respawns on the snake
	*			call the method again
	*		END IF the target respawns on the snake
	*	END FOR all the parts of the snake
	* END moveTarget
	**********************************************************/
	public void moveTarget()
	{
		//creates new randomly generated x and y target positions
		xPos = rand.nextInt(34);
		yPos = rand.nextInt(23);

		//for all of the snake pieces
		for(int b = 1; b < snakeLength; b++)
		{
			//if the target respawns on the snake
			if (targetXPos[xPos] == snakeXLength[b] && targetYPos[yPos] == snakeYLength[b])
			{
				//call the method again
				moveTarget();
			}//end if the target respawns on the snake
		}//end for all the parts of the snake
	}//end move target

	/**********************************************************
	* Method Name    : gameOver
	* Author         : Adam Yerden
	* Date           : 5/15/2019
	* Course/Section : CSC-264-501
	* Program Description: moves target to a new random location
	*
	* BEGIN gameOver
	*	IF the snake collided going to the right
	*		rightFail is true
	*	ELSE IF the snake collided going to the left
	*		leftFail is true
	*	ELSE IF the snake collided going up
	*		upFail is true
	*	IF the snake collided going down
	*		downFail is true
	*	END IF the snake collided going to the right
	*	IF the player beats their high score
	*		update the high score with current score
	*	END IF player beats their high score
	*	set count to 1 and collition to true
	*	set directional booleans to false
	* END gameOver
	**********************************************************/
	public void gameOver()
	{
		//if the snake collided going to the right
		if(right)
		{
			//rightFail is true
			rightFail = true;
		}
		//else if the snake collided going to the left
		else if(left)
		{
			//leftFail is true
			leftFail = true;
		}
		//else if the snake collided going up
		else if(up)
		{
			//upFail is true
			upFail = true;
		}
		//if the snake collided going down
		else if(down)
		{
			//downFail is true
			downFail = true;
		}//end if the snake collided going to the right
		//if the player beats their high score
		if(score > highScore)
		{
			//update the high score with current score
			highScore = score;
		}//end if player beats their high score

		//set count to 1 and collition to true
		count = 1;
		collision = true;

		//set directional booleans to false
		right = false;
		left = false;
		up = false;
		down = false;

	}//end gameOver
}//end Gameplay