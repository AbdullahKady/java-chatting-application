package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ExtraServerThread extends Thread {
	protected Socket clientSocket;
	protected String currentID;
	protected boolean terminateThread;

	public Socket getClientSocket() {
		return clientSocket;
	}

	public ExtraServerThread(Socket connectionSocket) {
		this.clientSocket = connectionSocket;
		this.terminateThread = false;
	}

	public void run() {
		this.welcomeClient();

		if (terminateThread) // In case user quits during the welcoming (before
								// joining)
			return;
		// Servicing a client normally.
		String messageReceived;
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

			while (true) {
				messageReceived = inFromClient.readLine();
				if (messageReceived.equals("Quit")) {
					ExtraServer.clientsList.remove(this.currentID);
					ExtraServer.extraServerInitiator.sendMemebers();
					System.out.println("--Internal-- Removed " + this.currentID + " from the member's list");
					clientSocket.close();
					return;
				}

				if (messageReceived.equals("GetMemberList()")) {
					Object[] tempClients = ExtraServer.clientsList.keySet().toArray();
					outToClient.writeBytes("The current online members are : \n");
					for (int i = 0; i < tempClients.length; i++) {
						outToClient.writeBytes("	 - " + tempClients[i] + "\n");
					}
					if (!ExtraServer.otherServerClients.isEmpty()) {
						outToClient.writeBytes("======= On the other server =======\n");
						for (int i = 0; i < ExtraServer.otherServerClients.size(); i++) {
							outToClient.writeBytes("	 - " + ExtraServer.otherServerClients.get(i) + "\n");
						}
					}

				} else if (messageReceived.startsWith("Chat(") && messageReceived.endsWith(")")) {
					String pureArguments = messageReceived.substring(5, messageReceived.length() - 1);
					String[] params = pureArguments.split(",");
					String src = params[0];
					String dest = params[1];
					String msg = params[2];
					int TTL = Integer.parseInt(params[3]);
					if (ExtraServer.clientsList.containsKey(dest)) {
						Socket tempSocket = ExtraServer.clientsList.get(dest).getClientSocket();
						DataOutputStream outToTemp = new DataOutputStream(tempSocket.getOutputStream());
						if (TTL > 0)
							outToTemp.writeBytes(currentID + " : " + msg + "\n");
						else
							outToClient.writeBytes("TTL specified is too small!\n");
					} else {
						ExtraServer.extraServerInitiator
								.route("Chat(" + src + "," + dest + "," + msg + "," + (TTL - 1) + ")");

					}
				} else {
					outToClient.writeBytes(
							"Since you didn't use any command, I will just capitalize your input and send it back ,_, : \n");
					outToClient.writeBytes(messageReceived.toUpperCase() + "\n");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void welcomeClient() {
		String messageReceived;
		try {
			DataOutputStream outToClient = new DataOutputStream(this.clientSocket.getOutputStream());
			outToClient.writeBytes("Connected to server! \nPlease use the Join(YourUserName) to join the server \n");

			// Waiting for the join message
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {
				messageReceived = inFromClient.readLine();
				if (messageReceived.equals("Quit")) {
					clientSocket.close();
					terminateThread = true;
					break;
				}
				String ID = messageReceived.substring(5, messageReceived.length() - 1);
				if (messageReceived.length() < 7 || !(messageReceived.substring(0, 5).equals("Join(")
						&& messageReceived.charAt(messageReceived.length() - 1) == ')')) {
					outToClient.writeBytes("Your message need to be of the format 'Join(Username)' \n");
				} else if (ExtraServer.clientsList.containsKey(ID) || ExtraServer.otherServerClients.contains(ID)) {
					outToClient.writeBytes("Username already in use, please choose another one \n");
				} else {
					this.currentID = messageReceived.substring(5, messageReceived.length() - 1);
					ExtraServer.clientsList.put(this.currentID, this);
					ExtraServer.extraServerInitiator.sendMemebers();
					System.out.println("--Internal-- Added " + this.currentID + " to the member's list");
					outToClient.writeBytes("You have successfully joined. Welcome " + this.currentID + "\n");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
