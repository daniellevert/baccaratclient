import java.io.Serializable;
import java.util.ArrayList;

/*
*
* BaccaratClient - Project 3 for CS324, Fall Semester - 2020
* Authors: Daniel LeVert, Adam Sammakia
* UIN Adam:  659002242 Daniel: 673238527
*
 * BaccaratInfo class for CLIENT
 * Allows data to be passed back and forth between the client server
 */

public class BaccaratInfo implements Serializable {
//	ArrayList<Card> playerHand;
//	ArrayList<Card> bankerHand;
	ArrayList<Integer> playerHandVals;
	ArrayList<String> playerHandSuits;
	ArrayList<Integer> bankerHandVals;
	ArrayList<String> bankerHandSuits;
	String betOn;  // changed to static so we can access from BaccaratGame
	Double winningsThisHand;
	Double totalWinnings;
	Double bid;
	Boolean natural;
	// TODO: Add constructor. Also maybe add method to clear everything except 
	// total winnings? Or should this just happen in the BaccaratGame class? 
	// Or maybe even just never?
	
	public BaccaratInfo(){
		this.natural = false;
		this.playerHandVals = new ArrayList<Integer>();
		this.playerHandSuits = new ArrayList<String>();
		this.bankerHandVals = new ArrayList<Integer>();
		this.bankerHandSuits = new ArrayList<String>();
		this.winningsThisHand = 0.0;
		this.totalWinnings = 0.0;
		this.bid = -1.0;
	}

	// clearHands
	//
	// clears the hands in message so they can be loaded again
	public void clearHands() {
		this.playerHandSuits.clear();
		this.playerHandVals.clear();
		this.bankerHandSuits.clear();
		this.bankerHandVals.clear();
	}
}