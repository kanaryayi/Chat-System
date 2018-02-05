
/**
 * 
 */
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * @author HTC
 *
 */
public class ChatServer {
	/***
	 * 	When we call the Constructor server always checks if a new client is trying to 
	 * connect to the server.
	 * @param port
	 * @throws IOException
	 */
	public ChatServer(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Socket client = server.accept();
			System.out.println("Accepted from " + client.getInetAddress());
			ChatHandler c = new ChatHandler(client);
			c.start();
		}
	}

	public static void main(String[] args) throws RuntimeException, IOException {
		if (args.length != 1)
			throw new RuntimeException("Syntax: ChatServer <port>");
		new ChatServer(Integer.parseInt(args[0]));
	}

}
