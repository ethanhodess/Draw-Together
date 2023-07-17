import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles communication between the server and one client, for SketchServer
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			for(Integer id : server.getSketch().map.keySet()){
				out.println("addingID "+ id + " " +server.getSketch().map.get(id));
			}

			// Keep getting and handling messages from the client
			String line;
			while ((line = in.readLine()) != null) {
				String[] input = line.split(" ");
				if(input[0].equals("adding")){
					if(input[1].equals("ellipse")){
						server.getSketch().addShape(new Ellipse(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("rectangle")){
						server.getSketch().addShape(new Rectangle(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("segment")){
						server.getSketch().addShape(new Segment(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("freehand")){
						ArrayList<Point> m = new ArrayList<>();
						for (int i = 2; i < input.length-1; i+=2){
							m.add(new Point(Integer.parseInt(input[i]), Integer.parseInt(input[i+1])));
						}
						server.getSketch().addShape(new Polyline(m, new Color(Integer.parseInt(input[input.length-1]))));
					}
				}
				else if(input[0].equals("moving")){
					server.getSketch().moveShape(Integer.parseInt(input[1]), Integer.parseInt(input[2]), Integer.parseInt(input[3]));
				}
				else if(input[0].equals("recoloring")){
					server.getSketch().recolor(Integer.parseInt(input[1]), new Color(Integer.parseInt(input[2])));

				}
				else if(input[0].equals("deleting")){
					server.getSketch().delete(Integer.parseInt(input[1]));
				}
				server.broadcast(line);
			}

			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
