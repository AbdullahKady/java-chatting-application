package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ExtraServer {
	public static ConcurrentHashMap<String, ExtraServerThread> clientsList = new ConcurrentHashMap<String, ExtraServerThread>();
	public static ExtraServerListener extraServerListener;
	public static ArrayList<String> otherServerClients;
	public static ExtraServerInitiator extraServerInitiator;

	public static void main(String[] args) throws IOException {
		otherServerClients = new ArrayList<String>();
		System.out.println("========= SERVER 2 =========");
		Socket internalConnection = new Socket("127.0.0.1", 5555);

		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(4444);
		ExtraServer.startServerListener(internalConnection);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("--Internal-- A new connection socket has been opened with a client");
			ExtraServerThread serverThread = new ExtraServerThread(connectionSocket);
			serverThread.start();
		}

	}

	public static void startServerListener(Socket socket) throws IOException {
		System.out.println("--INTENRAL -- STARTING CONNECTION WITH 2ND SERVER");
		ExtraServerListener thread = new ExtraServerListener(socket);
		ExtraServer.extraServerListener = thread;
		thread.start();
		ExtraServer.extraServerInitiator = new ExtraServerInitiator(socket);

	}
}

class ExtraServerInitiator {
	Socket connectionSocket;

	public ExtraServerInitiator(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void sendMemebers() throws IOException {
		DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
		if (ExtraServer.clientsList.isEmpty()) {
			outToServer.writeBytes("MEMBERSEMPTY\n");
			return;
		}
		Object[] temp = ExtraServer.clientsList.keySet().toArray();
		String tempMsg = "";
		for (int i = 0; i < temp.length; i++) {
			tempMsg = tempMsg + temp[i] + ",";
		}
		outToServer.writeBytes("MEMBERS" + tempMsg + "\n");
	}

	public void route(String chatMessage) {
		DataOutputStream outToServer;
		try {
			outToServer = new DataOutputStream(connectionSocket.getOutputStream());
			outToServer.writeBytes(chatMessage + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ExtraServerListener extends Thread {
	Socket connectionSocket;

	public ExtraServerListener(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void run() {
		try {
			String messageReceived;
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
			while (true) {

				messageReceived = inFromServer.readLine();
				if (messageReceived.startsWith("MEMBERS")) {
					String pureMembers = messageReceived.substring(7);
					ExtraServer.otherServerClients.clear();

					if (!pureMembers.equals("EMPTY")) {
						String[] tempClients = pureMembers.split(",");
						for (int i = 0; i < tempClients.length; i++)
							ExtraServer.otherServerClients.add(tempClients[i]);
					}

				} else if (messageReceived.startsWith("Chat(")) {
					String pureArguments = messageReceived.substring(5, messageReceived.length() - 1);
					String[] params = pureArguments.split(",");
					String src = params[0];
					String dest = params[1];
					String msg = params[2];
					// int TTL = Integer.parseInt(params[3]); USELESS!
					if (ExtraServer.clientsList.containsKey(dest)) {
						Socket tempSocket = ExtraServer.clientsList.get(dest).getClientSocket();
						DataOutputStream outToClient = new DataOutputStream(tempSocket.getOutputStream());
						outToClient.writeBytes(src + " : " + msg + "\n");

					} else {
						outToServer.writeBytes("Chat(ERROR," + src
								+ ",Wrong destination specified. you can type 'GetMemberList()' to get a list of the current online members,0)\n");
					}

				}
			}
		} catch (Exception e) {

		}
	}
}