package GraphGui.ManualGen;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable{
	private static final long serialVersionUID = 1L;

	private int node_id;

	private int x, y, r;  // x, y node's geo-location, and r=radius.

	private Color color;

	private Color previousColor;

	private boolean isPressed = false;
	
	private String text ="";
	
	private List<Edge> edges;
	
	public Node(int x, int y) {
		this.x=x;
		this.y=y;
		this.r=10;
		this.color = Color.lightGray;
		edges = new ArrayList<>();
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.fillOval(x-r, y-r, 2*r, 2*r);
		g.setColor(Color.black);
		g.drawOval(x-r, y-r, 2*r, 2*r);
		FontRenderContext fontRenderContext = ((Graphics2D)g).getFontRenderContext();
        Rectangle2D bounds = new Font("arial", Font.BOLD, 14).getStringBounds(text, fontRenderContext);
        g.drawString(text, this.x - (int)bounds.getWidth() / 2, this.y + (int)bounds.getHeight() / 4);
	}
	
	public boolean isMouseOver(int mx, int my){
		return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
	}

	public List<Edge> getEdges() {return edges;}
	public void addEdge(Edge e) { 
		//check if is repetition
		if(edges.contains(e)) return;
		edges.add(e);
	}
	
//setters
	public void setX(int x) {this.x=x;}
	public void setY(int y) {this.y=y;}
	public void setR(int r) {this.r=r;}
	public void setColor(Color color) {this.color = color; this.previousColor=color;}
	public void setText(String name) {
		if(text != null) this.text = name;
		else this.text = "";
	}

	public void setPressed(boolean h) {
		if(h && !isPressed){
			isPressed = true;
			previousColor = color;
			color = Color.yellow;
		}
		else if(isPressed){
			isPressed = false;
			color = previousColor;
		}
	}
//getters	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getR() {return r;}
	public Color getColor() {return color;}
	public String getText() {return text;}
	public boolean isPressed() {	return isPressed;	}

	public void setNode_id(int node_id) {this.node_id = node_id;}

	public int getNode_id() {return node_id;}

	@Override
	public String toString() {
		return "(x,y,r) = (" + x+","+y+","+r+")";
	}
}
