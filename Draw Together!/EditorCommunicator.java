import java.io.*;
import java.net.Socket;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;


/**
 * Handles communication to/from the server for the editor
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}


	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			String line;
			while ((line = in.readLine()) != null) {
				String[] input = line.split(" ");
				if(input[0].equals("adding")){
					if(input[1].equals("ellipse")){
						editor.getSketch().addShape(new Ellipse(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("rectangle")){
						editor.getSketch().addShape(new Rectangle(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("segment")){
						editor.getSketch().addShape(new Segment(Integer.parseInt(input[2]), Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]), new Color(Integer.parseInt(input[6]))));
					}
					else if(input[1].equals("freehand")){
						ArrayList<Point> m = new ArrayList<>();
						for (int i = 2; i < input.length-1; i+=2){
							m.add(new Point(Integer.parseInt(input[i]), Integer.parseInt(input[i+1])));
						}
						editor.getSketch().addShape(new Polyline(m, new Color(Integer.parseInt(input[input.length-1]))));
					}
				}
				else if(input[0].equals("moving")){
					editor.getSketch().moveShape(Integer.parseInt(input[1]), Integer.parseInt(input[2]), Integer.parseInt(input[3]));
				}
				else if(input[0].equals("recoloring")){
					editor.getSketch().recolor(Integer.parseInt(input[1]), new Color(Integer.parseInt(input[2])));

				}
				else if(input[0].equals("deleting")){
					editor.getSketch().delete(Integer.parseInt(input[1]));
				}
				else if (input[0].equals("addingID")){
					if(input[2].equals("ellipse")){
						editor.getSketch().addShape(Integer.parseInt(input[1]), new Ellipse(Integer.parseInt(input[3]), Integer.parseInt(input[4]),Integer.parseInt(input[5]),Integer.parseInt(input[6]), new Color(Integer.parseInt(input[7]))));
					}
					else if(input[2].equals("rectangle")){
						editor.getSketch().addShape(Integer.parseInt(input[1]), new Rectangle(Integer.parseInt(input[3]), Integer.parseInt(input[4]),Integer.parseInt(input[5]),Integer.parseInt(input[6]), new Color(Integer.parseInt(input[7]))));
					}
					else if(input[2].equals("segment")){
						editor.getSketch().addShape(Integer.parseInt(input[1]), new Segment(Integer.parseInt(input[3]), Integer.parseInt(input[4]),Integer.parseInt(input[5]),Integer.parseInt(input[6]), new Color(Integer.parseInt(input[7]))));
					}
					else if(input[2].equals("freehand")){
						ArrayList<Point> m = new ArrayList<>();
						for (int i = 3; i < input.length-1; i+=2){
							m.add(new Point(Integer.parseInt(input[i]), Integer.parseInt(input[i+1])));
						}
						editor.getSketch().addShape(Integer.parseInt(input[1]), new Polyline(m, new Color(Integer.parseInt(input[input.length-1]))));
					}
				}
				editor.repaint();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}	

	// Send editor requests to the server

}
