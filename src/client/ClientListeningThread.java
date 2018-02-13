package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListeningThread extends Thread {
	Socket clientSocket;
	boolean terminateThread;
	public void setTerminateThread(boolean terminateThread) {
		this.terminateThread = terminateThread;
	}
	public ClientListeningThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		terminateThread = false;
	}

	
	public void run() {
		
		String messageReceived;
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (!terminateThread) {
				messageReceived = inFromServer.readLine();
				if (messageReceived != null) //To avoid printing a null after quitting from the client's side
					System.out.println(messageReceived); 
			}
			
			clientSocket.close();
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
