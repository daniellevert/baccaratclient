
import java.util.Hashtable;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
*
* BaccaratClient - Project 3 for CS324, Fall Semester - 2020
* Authors: Daniel LeVert, Adam Sammakia
* UIN Adam:  659002242 Daniel: 673238527
* 
* A server with a graphical user interface for the game of Baccarat. 
* Can host two clients playing the game, and clients can connect or 
* disconnect at will. The GUI also displays when a client has joined or left 
* the game. The GUI also displays each round a client plays how much money
* they have won or lost that hand. Contains all of the game logic for gameplay.
*
*/

public class ClientGUI extends Application {
	/*
	 * Declaring all fields that will be used in various functions
	 * throughout the program so all functions have access
	 */
	Hashtable<String, Scene> sceneMap;
	Hashtable<String, ImageView> imageMap;
	TextField portNum, ipAddress, bidField;
	Button connect, exit, play, playAgain, send;
	RadioButton betP, betB, betD;
	ToggleGroup betGroup;
	Integer totalWinnings, result;
	Text title, enterPortText, enterIPText, outputText;
	Text pCard1, pCard2, pCard3, bCard1, bCard2, bCard3;
	String p1, p2, p3, b1, b2, b3;
	BaccaratInfo message;
	ClientThread clientConnection;
	PauseTransition pause;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		/*
		 * Initializing fields that will be used in and out of start()
		 */
		PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
		message = new BaccaratInfo();
		bidField = new TextField(); // Added
		exit = new Button();
		play = new Button();
		playAgain = new Button();
		send = new Button();
		outputText = new Text();
		betP = new RadioButton();
		betD = new RadioButton();
		betB = new RadioButton();
		betGroup = new ToggleGroup();
		pCard1 = new Text();
		pCard2 = new Text();
		pCard3 = new Text();
		bCard1 = new Text();
		bCard2 = new Text();
		bCard3 = new Text();
		p1 = "";
		p2 = "";
		p3 = "";
		b1 = "";
		b2 = "";
		b3 = "";

		primaryStage.setTitle("Baccarat (Client)");
		
		betGroup.getToggles().addAll(betP, betD, betB);
		
		/*
		 * Creates a start screen to be displayed upon program launch
		 */
		title = new Text("BACCARAT CLIENT");
		enterPortText = new Text("Enter port number: ");
		enterIPText = new Text("Enter IP address (in format 000.0.0.0): ");
		connect = new Button("Connect");
		
		title.setStyle("-fx-font-size: 25;");
		
		portNum = new TextField();
		ipAddress = new TextField();
		
		portNum.setMaxWidth(300);
		ipAddress.setMaxWidth(300);
		
		VBox screenItems = new VBox(title, enterPortText, portNum, enterIPText, ipAddress, connect);
		screenItems.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(screenItems, 700,700);
		
		connect.setDisable(true);
		
		
		/*
		 * Stores port number upon enter key being pressed
		 */
		this.portNum.setOnKeyPressed(e -> {if(e.getCode().equals(KeyCode.ENTER)){
			portNum.setDisable(true);
			
			if (ipAddress.isDisable() == true) {
				connect.setDisable(false);
			}
			System.out.println("DEBUG: Port num is: " + portNum.getText());
			}
		});
		
		/*
		 * Stores port number upon enter key being pressed
		 */
		this.ipAddress.setOnKeyPressed(e -> {if(e.getCode().equals(KeyCode.ENTER)){
			ipAddress.setDisable(true);
			
			if (portNum.isDisable() == true) {
				connect.setDisable(false);
			}
			System.out.println("DEBUG: IP address is: " + ipAddress.getText());
			}
		});
		
		/*
		 * Initializes the main play scene.
		 * Sets primaryStage to be mainScene and starts 
		 * client connection when the connect is clicked
		 */
		Scene mainScene = createClientScreen();
		
		this.connect.setOnAction(e->{ 
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Baccarat (Client)");
			mainScene.getStylesheets().add("style.css"); 
			clientConnection = new ClientThread(ipAddress.getText(),Integer.parseInt(portNum.getText()), 
			data -> {
						Platform.runLater(()->{message = (BaccaratInfo) data;});
					});
			clientConnection.start();
		});
		System.out.println("DEBUG: message.bid: " + message.bid);
		
		/*
		 * Stores input from bidField text field upon enter being pressed
		 */
		bidField.setOnKeyPressed(e -> {if(e.getCode().equals(KeyCode.ENTER)){
				message.bid = Double.parseDouble(bidField.getText());
				System.out.println("DEBUG: Bid is: " + bidField.getText());

				bidField.setDisable(true);
				betP.setDisable(false);
				betD.setDisable(false);
				betB.setDisable(false);
			}
		});
		
		/*
		 * Gets user input from radio buttons and stores whether
		 * player, draw, or banker was selected into message to be
		 * passed to server
		 */
		this.betP.setOnAction(e->{ message.betOn = betP.getText();
									System.out.println("DEBUG: Bet on is: " + message.betOn);
									send.setDisable(false);
									System.out.println("DEBUG: Selected: " + betP.getText());
			
		});
		
		this.betD.setOnAction(e->{ message.betOn = betD.getText();
									System.out.println("DEBUG: Bet on is: " + message.betOn);
									send.setDisable(false);
									System.out.println("DEBUG: Selected: " + betD.getText());
		
		});
		
		this.betB.setOnAction(e->{ message.betOn = betB.getText();
									System.out.println("DEBUG: Bet on is: " + message.betOn);
									send.setDisable(false);
									System.out.println("DEBUG: Selected: " + betB.getText());
		
		});
		
		/*
		 * Sends server a BaccaratInfo message every time send button
		 * is pressed
		 */
		this.send.setOnAction(e->{
			clientConnection.send(message);
			play.setDisable(false);
			send.setDisable(true);
			});
		
		/*
		 * Completes steps for appropriately displaying game states and cards,
		 * makes appropriate validation checks for output, and disables and
		 * enables the appropriate buttons upon play being clicked
		 */
		this.play.setOnAction(e-> { 
			
			betP.setDisable(true);
			betD.setDisable(true);
			betB.setDisable(true);
			

			p1 = message.playerHandSuits.get(0) + "-" + message.playerHandVals.get(0).toString() + "  ";
			pCard1.setText(p1);
			p2 = message.playerHandSuits.get(1) + "-" + message.playerHandVals.get(1).toString() + "  ";
			pCard2.setText(p2);
			
			b1 = message.bankerHandSuits.get(0) + "-" + message.bankerHandVals.get(0).toString() + "  ";
			bCard1.setText(b1);
			b2 = message.bankerHandSuits.get(1) + "-" + message.bankerHandVals.get(1).toString() + "  ";
			bCard2.setText(b2);
			
			// pause transition for smooth UX
			pause.play();
			
			if (message.natural == true) {
					if (message.winningsThisHand > 0) {
						outputText.setText("WINNINGS THIS ROUND: $" + message.winningsThisHand + " | TOTAL WINNINGS: $" + message.totalWinnings + " | RESULT: You won! (Natural)");
					}
					else {
						outputText.setText("WINNINGS THIS ROUND: $" + message.winningsThisHand + " | TOTAL WINNINGS: $" + message.totalWinnings + " | RESULT: You lost :( (Natural)");
					}
				
					playAgain.setDisable(false);
			}
			else { // no natural win so inform user and continue
				outputText.setText("Sorry, not a natural win.");
				
				// pause transition for smooth UX
				pause.play();
				
				// Determines if player drew a card and displays if so
				if (message.playerHandSuits.size() > 2) {
					outputText.setText("Player drew another card.");
					p3 = message.playerHandSuits.get(2) + "-" + message.playerHandVals.get(2).toString() + "  ";
					pCard3.setText(p3);
				}
				else {
					outputText.setText("Player DID NOT draw another card.");
				}
				
				// pause transition for smooth UX
				pause.play();
				
				// Determines if banker drew a card and displays if so
				if (message.bankerHandSuits.size() > 2) {
					outputText.setText("Banker drew another card.");
					b3 = message.bankerHandSuits.get(2) + "-" + message.bankerHandVals.get(2).toString() + "  ";
					bCard3.setText(b3);
				}
				else {
					outputText.setText("Banker DID NOT draw another card.");
				}
				
				// pause transition for smooth UX
				pause.play();
				
				// Prints the results of the non-natural win or lose game
				if (message.winningsThisHand > 0) {
						outputText.setText("WINNINGS THIS ROUND: $" + message.winningsThisHand + " | TOTAL WINNINGS: $" + message.totalWinnings + " | RESULT: You won!");
				}
				else {
					outputText.setText("WINNINGS THIS ROUND: $" + message.winningsThisHand + " | TOTAL WINNINGS: $" + message.totalWinnings + " | RESULT: You lost :(");
				}
			}
			
			play.setDisable(true);
			playAgain.setDisable(false);
			
		});
		
		/*
		 * Resets the scene and fields and re-enables bidField if
		 * the user presses button to play again 
		 */
		this.playAgain.setOnAction(e->{ 
			primaryStage.setScene(createClientScreen());
			
			bidField.clear();
			betP.setSelected(false);
			betD.setSelected(false);
			betB.setSelected(false);
			
			bidField.setDisable(false);
										
		});
		
		// Exits game when exit button is clicked
		this.exit.setOnAction(e->{ System.exit(0);});
		
		/*
		 * Exits system correctly upon window being closed
		 */
		primaryStage.setOnCloseRequest(e->{System.out.println("DEBUG: Exiting window...");
			System.exit(0);
											
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public Scene createClientScreen() {
		
		title = new Text("BACCARAT");
		title.setStyle("-fx-font-size: 30;");
		// Center title
		StackPane root1 = new StackPane();
		title.setTextAlignment(TextAlignment.CENTER);
		root1.getChildren().add(title);
		StackPane.setAlignment(title, Pos.CENTER);
		
		Text playerTitle = new Text("Player");
		Text bankerTitle = new Text("Banker");
		playerTitle.setStyle("-fx-font-size: 15;");
		bankerTitle.setStyle("-fx-font-size: 15;");

		// line to divide player cards from banker cards
		Line line = new Line();
		line.setStartX(100.0);
		line.setStartY(0.0);
		line.setEndX(100.0);
		line.setEndY(100.0);
		line.setStrokeWidth(3);
		
		pCard1.setText("");
		pCard2.setText("");
		pCard3.setText("");
		bCard1.setText("");
		bCard2.setText("");
		bCard3.setText("");
		
		HBox playerHandDisplay = new HBox(pCard1, pCard2, pCard3); // display images of player's hand
		HBox bankerHandDisplay = new HBox(bCard1, bCard2, bCard3); // display images of banker's hand
		
		VBox playerDisplay = new VBox(playerTitle, playerHandDisplay); // displays player's hand and title together
		VBox bankerDisplay = new VBox(bankerTitle, bankerHandDisplay); // displays banker's hand and title together
		
		HBox firstBlock = new HBox(playerDisplay, line, bankerDisplay); // first block to be displayed
		firstBlock.setAlignment(Pos.CENTER);
		
		bidField.setPromptText("Enter bid");
		bidField.setAlignment(Pos.CENTER);
	
		Text betOnTitle = new Text("BET ON   ");
		betP.setText("Player");
		betD.setText("Draw");
		betB.setText("Banker");
		
		HBox betChoices = new HBox(betOnTitle, betP, betD, betB); // third block
		betChoices.setAlignment(Pos.CENTER);
		
		outputText.setText("WINNINGS THIS ROUND: $"+ message.winningsThisHand + " | TOTAL WINNINGS: $" + message.totalWinnings + " | RESULT: None"); // fourth block
		// Center text in display
		StackPane root0 = new StackPane();
		outputText.setTextAlignment(TextAlignment.CENTER);
		root0.getChildren().add(outputText);
		StackPane.setAlignment(outputText, Pos.CENTER);

		play.setText("Play");
		playAgain.setText("Play Again");
		send.setText("Send");
		

		play.setDisable(true);
		playAgain.setDisable(true);
		send.setDisable(true);
		
		betP.setDisable(true);
		betD.setDisable(true);
		betB.setDisable(true);
		
		exit.setText("Exit");

		
		HBox gameActions = new HBox(send, play, playAgain, exit); // fifth block
		gameActions.setAlignment(Pos.CENTER);
		
		VBox screen = new VBox(root1, firstBlock, bidField, betChoices, root0, gameActions);
		screen.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(screen, 700,700);
		scene.getStylesheets().add("/style.css");
		
		return scene;
	}
}