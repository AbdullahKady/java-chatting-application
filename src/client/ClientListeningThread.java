package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListeningThread extends Thread {
	Client client;
	Socket clientSocket;
	boolean terminateThread;

	public void setTerminateThread(boolean terminateThread) {
		this.terminateThread = terminateThread;
	}

	public ClientListeningThread(Client client, Socket clientSocket) {
		this.client = client;
		this.clientSocket = clientSocket;
		terminateThread = false;
	}

	public void run() {

		String messageReceived;
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (!terminateThread) {
				messageReceived = inFromServer.readLine();
				if (messageReceived != null) {
					if (messageReceived.startsWith("#MEMBERS")) {
						String[] list = messageReceived.substring(8).split(",");
						this.client.gui.choice_member.removeAll();
						for (int i = 0; i < list.length; i++) {
							if (!list[i].equals(this.client.username))
								this.client.gui.choice_member.add(list[i]);
						}
					} else if (this.client.isReady())
						this.client.print(messageReceived);
				}
			}

			clientSocket.close();
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
