package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public static ConcurrentHashMap<String, ServerThread> clientsList = new ConcurrentHashMap<String, ServerThread>();

	public static void main(String[] args) throws IOException {

		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(5555);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("--Internal-- A new connection socket has been opened with a client");
			ServerThread serverThread = new ServerThread(connectionSocket);
			serverThread.start();
		}

	}
}