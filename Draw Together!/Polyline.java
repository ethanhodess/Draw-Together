import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 */
public class Polyline implements Shape {
	ArrayList<Point> points = new ArrayList<>();
	private Color color;

	public Polyline(Point p, Color color) {
		points.add(p);
		this.color = color;
	}

	public Polyline (ArrayList<Point> points, Color color){
		this.points = points;
		this.color = color;
	}

	@Override
	public void moveBy(int dx, int dy) {
		for(Point p : points){
			p.x = p.x + dx;
			p.y = p.y + dy;
		}
	}

	public void setEnd(int x2, int y2) {
		points.add(new Point(x2, y2));
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public boolean contains(int x, int y) {
		for(int i = 0; i < points.size()-1; i++){
			if(Segment.pointToSegmentDistance(x, y, (int)points.get(i).getX(), (int)points.get(i).getY(), (int)points.get(i+1).getX(), (int)points.get(i+1).getY()) <= 3){
				return true;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);

		for(int i = 0; i < points.size()-1; i++){
			g.drawLine((int)points.get(i).getX(), (int)points.get(i).getY(), (int)points.get(i+1).getX(), (int)points.get(i+1).getY());
		}
	}

	@Override
	public String toString() {
		String output = "polyline ";
		for (Point p: points){
			output += p.getX() + " " + p.getY() + " ";
		}
		output += color.getRGB();
		return output;
	}
}
