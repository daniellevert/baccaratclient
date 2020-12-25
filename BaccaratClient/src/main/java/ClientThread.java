import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

/*
*
* BaccaratClient - Project 3 for CS324, Fall Semester - 2020
* Authors: Daniel LeVert, Adam Sammakia
* UIN Adam:  659002242 Daniel: 673238527
*
*/

public class ClientThread extends Thread{
	
	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	private Consumer<Serializable> callback;
	BaccaratInfo message;
	Integer portnumber;
	String IPAddr;
	
	//
	// constructor 
	//
	ClientThread(String IPAddr, Integer portnumber, Consumer<Serializable> call){		
		this.callback = call;
		this.IPAddr = IPAddr;
		this.portnumber = portnumber;
		
	}
	
	// run
	//
	// Connect to the server and send a BaccaratInfo object to clientGUI
	public void run() {
		try {
			socketClient = new Socket(IPAddr, portnumber); 
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			System.exit(1);
		} 
		while(true) {
			try {
				// wait for BaccaratInfo object to send to ClientGUI
				message = (BaccaratInfo) in.readObject();
				
				// send message to ClientGUI
				callback.accept(message);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			} 
		}		
	}
	
	// send
	//
	// Send a BaccaratInfo object to the server through a socket
	public void send(BaccaratInfo message) {
		try {
			out.writeObject(message);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);			
		}
	}
}