package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	ClientGUI gui;
	ClientListeningThread clientListeningThread;
	Socket clientSocket;
	private boolean ready;
	String username;
	private String[] members;

	public void setGUI(ClientGUI gui) {
		this.gui = gui;
		this.ready = true;
	}

	public Client(String username, int port) throws UnknownHostException, IOException, usernameException {
		this.username = username;
		this.clientSocket = new Socket("127.0.0.1", port);
		DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String messageReceived;
		outToServer.writeBytes("Join(" + username + ")\n");
		messageReceived = inFromServer.readLine();
		System.out.println("message: " + messageReceived);
		if (messageReceived.startsWith("Username already in use")) {
			throw new usernameException("USED");
		}
		clientListeningThread = new ClientListeningThread(this, clientSocket);
		clientListeningThread.start();
	}

	public void sendMessage(String messageToBeSent) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
		outToServer.writeBytes(messageToBeSent + '\n');
		if (messageToBeSent.equals("Quit")) {
			this.clientListeningThread.setTerminateThread(true);
			return;
		}
	}

	public void print(String messageReceived) {
		this.gui.msgArea.setText(this.gui.msgArea.getText() + '\n' + messageReceived);
	}

	public boolean isReady() {
		return this.ready;
	}

	public void setList(String[] split) {
		this.members = split;

	}

	public class usernameException extends Exception {
		public usernameException(String arg0) {
			super(arg0);
		}
	}
}
