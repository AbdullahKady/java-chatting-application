package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws Exception {
		System.out.println("========= CLIENT#1 SERVER2 =========");

		Socket clientSocket = new Socket("127.0.0.1", 4444);
		ClientListeningThread clientListeningThread = new ClientListeningThread(clientSocket);
		clientListeningThread.start(); // Thread for listening to server, and
										// printing it out.

		// Listening to the user input, and sending it to the server
		String messageToBeSent;
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			messageToBeSent = inFromUser.readLine();
			outToServer.writeBytes(messageToBeSent + '\n');
			if (messageToBeSent.equals("Quit")) {
				clientListeningThread.setTerminateThread(true); // This ensures
																// closing the
																// socket as
																// well inside
																// the thread.
				System.out.println("See you later :)");
				break;
			}
		}
	}
}
