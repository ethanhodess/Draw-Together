import java.util.TreeMap;
import java.awt.Point;
import java.awt.Color;



public class Sketch {
    TreeMap<Integer, Shape> map = new TreeMap<>();
    Integer nextId = 0;

    public Integer clicked(Point p){
        for(Integer id1 : map.keySet()){
            if(map.get(id1).contains((int)p.getX(), (int)p.getY())){
                return id1;
            }
        }
        return -1;
    }

    public synchronized void addShape (Shape s) {
        map.put(nextId, s);
        nextId++;
    }

    public synchronized void addShape (Integer id, Shape s) {
        map.put(id, s);
        nextId = id + 1;
    }

    public synchronized void moveShape (int id, int dx, int dy){
        map.get(id).moveBy(dx, dy);
    }

    public synchronized void recolor (int id, Color color){
        map.get(id).setColor(color);
    }

    public synchronized void delete (int id){
        map.remove(id);
    }
}
