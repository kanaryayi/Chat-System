import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread {

	protected Socket socket;
	protected DataInputStream input; // Clients ->> Server
	protected DataOutputStream output; // Server ->> Clients ()

	public ChatHandler(Socket socket) throws IOException {
		this.socket = socket;
		input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	/***
	 * 
	 */
	protected static Vector handlers = new Vector();

	@Override
	public void run() {
		try {
			handlers.addElement(this);
			while (true) {
				Thread.sleep(100);
				String msg = input.readUTF();
				broadcast(msg);
			}
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			handlers.removeElement(this);
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	/***
	 * Builds a synchronized system to send message all the clients
	 * @param message
	 */
	protected static void broadcast(String message) {
		
		synchronized (handlers) {
			Enumeration e = handlers.elements();
			while (e.hasMoreElements()) {
				ChatHandler c = (ChatHandler) e.nextElement();
				try {
					synchronized (c.output) {
						c.output.writeUTF(message);
					}
					c.output.flush();
				} catch (IOException ex) {
					c.stop();
				}
			}
		}
	}

}
