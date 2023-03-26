package robotWar_Summative;

import java.awt.*;
import becker.robots.*;

public class JiangMFighterRobot extends FighterRobot{

	private static final int ATTACK = 5;
	private static final int DEFENCE = 3;
	private static final int MOVES = 2;
	public static final int WIDTH = 20;
	private final int ENERGY_OF_MOVES = 5;
	private final int ENERGY_OF_ATTACKS = 10;
	private int health;
	private JiangMOppData [] extendedData = new JiangMOppData[9];
	
	/**
	 * The Ultimate Fighter Bot constructor to make robots of this type
	 * @param c			City
	 * @param a			Avenue
	 * @param s			Street
	 * @param d			Direction
	 * @param id		Player ID
	 * @param health	Health of player
	 */
	public JiangMFighterRobot(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, ATTACK, DEFENCE, MOVES);
		this.health = health;

		for (int i = 0; i < extendedData.length; i++) {
			extendedData[i] = new JiangMOppData(i, 0, 0, 0, 0);
		}
		
		this.setLabel();
	}

	/**
	 * Displays the robot's ID and health changes the colour to black if it dies
	 */
	public void setLabel() {

		//Checks if it still has health
		if (health > 0) {
			this.setLabel("ID: " + this.getID() + " H: " + health);
			this.setColor(Color.MAGENTA);
		}

		//Sets the colour to black if it's dead
		else {
			this.setLabel("ID: " + this.getID() + " H: " + health);
			this.setColor(Color.BLACK);
		}
	}

	/**
	 * Instructs the robot to move to a coordinate
	 * @param a	The avenue coordinate
	 * @param s	The street coordinate
	 */
	public void goToLocation(int a, int s) {

		//Robot faces west if the current avenue it's on is greater than the destination's
		if (this.getAvenue() > a) {

			//Turns around if it's facing east
			if (this.getDirection() == Direction.EAST)
				this.turnAround();

			//Turns left if it's facing north
			else if (this.getDirection() == Direction.NORTH)
				this.turnLeft();

			//Turns right if it's facing south
			else if (this.getDirection() == Direction.SOUTH)
				this.turnRight();
		}

		//Robot faces east if the current avenue it's on is smaller than the destination's
		else if (a > this.getAvenue()) {

			//Turns around if it's facing west
			if (this.getDirection() == Direction.WEST)
				this.turnAround();

			//Turns right if it's facing north
			else if (this.getDirection() == Direction.NORTH)
				this.turnRight();

			//Turns left if it's facing south
			else if (this.getDirection() == Direction.SOUTH)
				this.turnLeft();
		}

		//Moves the number of spaces that is determined by the difference between the current avenue and the destination
		this.move(Math.abs(this.getAvenue()-a));

		//Robot faces north is the current street is greater than the destination's
		if (this.getStreet() > s) {

			//Turns left if it's facing east
			if (this.getDirection() == Direction.EAST)
				this.turnLeft();

			//Turns around if it's facing south
			else if (this.getDirection() == Direction.SOUTH)
				this.turnAround();

			//Turns right if it's facing west
			else if (this.getDirection() == Direction.WEST)
				this.turnRight();
		}

		//Robot faces south if the current street is smaller than the destination's
		else if (s > this.getStreet()) {

			//Turns right if it's facing east
			if (this.getDirection() == Direction.EAST)
				this.turnRight();

			//Turns around if it's facing north
			else if (this.getDirection() == Direction.NORTH)
				this.turnAround();

			//Turns left if it's facing west
			else if (this.getDirection() == Direction.WEST)
				this.turnLeft();
		}

		//Moves the number of spaces that is determined by the difference between the current street and the destination
		this.move(Math.abs(this.getStreet()-s));
	}

	/**
	 * Determines what to do on the robot's turn
	 * @param energy	The amount of energy the robot has left
	 * @param data		All the information of the other robots
	 */
	public TurnRequest takeTurn(int energy, OppData[] data) {

		//Variable declaration
		int opponentIndex = 0;
		int movesLeft = MOVES;

		//Goes through each opponent data
		for (int i = 0; i < data.length; i++) {
			int robotIndex = extendedData[i].getID();
			
			//Updates the extended data with the new avenue, street, and health
			extendedData[i].setAvenue(data[robotIndex].getAvenue());
			extendedData[i].setStreet(data[robotIndex].getStreet());
			extendedData[i].setHealth(data[robotIndex].getHealth());
		}
		
		//Goes through each opponent data to find itself
		for (int i = 0; i < extendedData.length; i++) {

			//Finds the matching ID to get its own health
			if (extendedData[i].getID() == this.getID()) {
				health = extendedData[i].getHealth();
			}
		}

		//Checks if there is enough energy to move and carry out at least one attack
		if (energy < ENERGY_OF_MOVES*MOVES + ENERGY_OF_ATTACKS) {

			//Checks if there's enough energy to move at least once
			if (energy > ENERGY_OF_MOVES) {

				boolean escape = false;

				//Goes through each of the opponent's data
				for (int i = 0; i < extendedData.length; i++) {

					//Checks if there is an alive opponent at the same location
					if (this.getAvenue() == extendedData[i].getAvenue() && this.getStreet() == extendedData[i].getStreet() && this.getID() != extendedData[i].getID() && extendedData[i].getHealth() > 0) {
						//Changes boolean variable to true to have the robot run away from the opponent
						escape = true;
						break;
					}
				}

				//Checks if the robot needs to escape
				if (escape == true) {

					//Checks if the robot can move to the right without hitting the wall
					if (this.getAvenue()+MOVES < WIDTH) {

						//Moves to the right by the maximum number of moves it has
						if (energy > ENERGY_OF_MOVES*MOVES) {
							return new TurnRequest(this.getAvenue()+MOVES, this.getStreet(), -1, ATTACK);
						}

						//Moves to the right by as many steps as it can from the energy it has left 
						else {
							return new TurnRequest(this.getAvenue()+energy/ENERGY_OF_MOVES, this.getStreet(), -1, ATTACK);
						}

					}

					//Moves to the left instead to avoid hitting the wall
					else {

						//Moves to the left by the maximum number of moves it has
						if (energy > ENERGY_OF_MOVES*MOVES) {
							return new TurnRequest(this.getAvenue()-MOVES, this.getStreet(), -1, ATTACK);
						}

						//Moves to the left by as many steps as it can from the energy it has left 
						else
							return new TurnRequest(this.getAvenue()-energy/ENERGY_OF_MOVES, this.getStreet(), -1, ATTACK);
					}
				}

				//Stays put if there isn't another robot on the same spot
				else
					return new TurnRequest(this.getAvenue(), this.getStreet(), -1, ATTACK);
			}
			
			//Stays put if it doesn't have enough energy to move
			else
				return new TurnRequest(this.getAvenue(), this.getStreet(), -1, ATTACK);
		}

		//Moves and/or attacks if there's enough energy
		else {
			
			//Sorts opponent data
			sortOppData(extendedData);

			//Goes through each element of the sorted opponent's data
			for (int i = 0; i < extendedData.length; i++) {

				//Ensures that the robot targeted is not itself or dead
				if (extendedData[i].getID() != this.getID() && extendedData[i].getHealth() > 0) { 
					opponentIndex = i;
					break;
				}
			}

			//Checks if there's enough moves for the robot to travel to the opponent's avenue
			if (Math.abs(extendedData[opponentIndex].getAvenue()-this.getAvenue()) <= movesLeft) {

				movesLeft -= Math.abs(extendedData[opponentIndex].getAvenue()-this.getAvenue());

				//Checks if there's enough moves for the robot to travel to the opponent's final position
				if (Math.abs(extendedData[opponentIndex].getStreet()-this.getStreet()) <= movesLeft) {

					//Checks if there's enough energy to carry out all of the attacks
					if (energy > (MOVES - movesLeft)*ENERGY_OF_MOVES + ENERGY_OF_ATTACKS*ATTACK) {
						return new TurnRequest(extendedData[opponentIndex].getAvenue(), extendedData[opponentIndex].getStreet(), extendedData[opponentIndex].getID(), ATTACK);
					}

					//Attacks as many times as it can without risking losing all of its energy
					else
						return new TurnRequest(extendedData[opponentIndex].getAvenue(), extendedData[opponentIndex].getStreet(), extendedData[opponentIndex].getID(), (energy-(MOVES - movesLeft)*ENERGY_OF_MOVES)/ENERGY_OF_ATTACKS);
				}

				//Sets the robot's destination to as far as it can go down the street
				else {

					//Checks if the opponent's street is greater than the one the robot is currently on to determine the coordinates
					if (extendedData[opponentIndex].getStreet() > this.getStreet())
						return new TurnRequest(extendedData[opponentIndex].getAvenue(), this.getStreet()+movesLeft, -1, ATTACK); //Robot will move down

					//Robot will move up if the opponent's street is smaller than the current one
					else
						return new TurnRequest(extendedData[opponentIndex].getAvenue(), this.getStreet()-movesLeft, -1, ATTACK);
				}
			}

			//Sets the robot's destination to as far as it can go down the avenue
			else {

				//Checks if the opponent's avenue is greater than the one the robot is currently on to determine the avenue coordinate
				if (extendedData[opponentIndex].getAvenue() > this.getAvenue())
					return new TurnRequest(this.getAvenue()+movesLeft, this.getStreet(), -1, ATTACK); //Robot will move to the right side

				//Robot will move to the left side if the opponent's street is smaller than the current one
				else
					return new TurnRequest(this.getAvenue()-movesLeft, this.getStreet(), -1, ATTACK);
			}
		}
	}

	/**
	 * Gives statistics from the battle
	 * @param healthLost		The amount of health the robot lost itself
	 * @param oppID				Opponent's ID
	 * @param oppHealthLost		The amount of health the opponent lost
	 * @param numRoundsFought	The amount of rounds fought in the battle
	 */
	public void battleResult(int healthLost, int oppID, int oppHealthLost, int numRoundsFought) {

		//Keeps track of the health by subtracting what it lost in the battle
		health -= healthLost;

		//Will set the robot to black if it's now dead
		if (health == 0) {
			this.setLabel();
		}
		
		//Goes through each of the opponent's data
		for (int i = 0; i < extendedData.length; i++) {
			
			//Checks if the ID is matching to the opponent's ID
			if (extendedData[i].getID() == oppID) {
				extendedData[i].setHealthStats(healthLost-oppHealthLost); //Updates the statistics of health lost to the opponent
			}
		}
	}

	/**
	 * Sorts the opponent data from lowest to highest score (based on proximity and health) using insertion sort
	 * @param data	Opponent data
	 */
	private void sortOppData(JiangMOppData [] data) {

		//Runs through the array the number of times there are opponents
		for (int i = 0; i<data.length-1; i++) {

			//Checks each opponent starting from the unsorted portion
			for (int n = i+1; n>0; n--) {
				//Calculates the scores for the current robot and the robot that is currently in front
				int scoreCalculation1 = (Math.abs(this.getAvenue()-data[n].getAvenue()) + Math.abs(this.getStreet()-data[n].getStreet()))*2 + data[n].getHealth() + data[n].getHealthStats()*3;
				int scoreCalculation2 = (Math.abs(this.getAvenue()-data[n-1].getAvenue()) + Math.abs(this.getStreet()-data[n-1].getStreet()))*2 + data[n-1].getHealth() + data[n-1].getHealthStats()*3;

				//Checks if the score (based on proximity and health) of one opponent is smaller than the one before it
				if (scoreCalculation1 < scoreCalculation2) {
					//The two opponents swap places
					JiangMOppData smallerOpp = data[n];
					data[n] = data[n-1];
					data[n-1] = smallerOpp;
				}

				//Stops checking once the opponent data is in the correct place
				else
					break;
			}
		}
	}

}
