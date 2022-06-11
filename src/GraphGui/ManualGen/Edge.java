package GraphGui.ManualGen;

import java.awt.*;
import java.io.Serializable;


public class Edge implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Node node1, node2;
	private Color color;
	
	private double A, C;
	private final double B = -1;
	
	private float stroke = 2.0f;
	
	public Edge(Node n1, Node n2){
		this.node1 = n1;
		this.node2 = n2;
		n1.addEdge(this);
		n2.addEdge(this);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(stroke));
		g2d.drawLine(node1.getX(), node1.getY(), node2.getX(), node2.getY());
		g2d.setStroke(new BasicStroke(1.0f));
		g.setColor(Color.black);
	}

    /**
     * Method moves the edge by given point.
	 * Moving the edge corresponds to moving both edge's nodes
	 * then update the line between them.
	 * @param dx given point's x coordinate
	 * @param dy given point's y coordinate
	 */
	public void move(int dx, int dy) {
		node1.setX(node1.getX()+dx); node1.setY(node1.getY()+dy);
		node2.setX(node2.getX()+dx); node2.setY(node2.getY()+dy);
	}

    /**
	 * Method checks if mouse near to the current edge.
	 * @param mx mouse's x coordinate
	 * @param my mouse's y coordinate
	 * @return true iff mouse is near the current edge, o.w., return false.
	 */
	public boolean isMouseOver(int mx, int my) {
		int x1 = node1.getX(); int y1 = node1.getY();
		int x2 = node2.getX(); int y2 = node2.getY();
		updateLinearParameters(x1, y1 ,x2, y2);

		if((mx>x1 && my>y1 && mx<x2 && my<y2) || (mx>x2 && my>y2 && mx<x1 && my<y1) ||
		   (mx>x1 && my<y1 && mx<x2 && my>y2) || (mx>x2 && my<y2 && mx<x1 && my>y1)) {
			double range = 7;
			return Math.abs(A * mx + B * my + C)/Math.sqrt(A * A + B * B) <= range;
		}
		return false;
	}

	private void updateLinearParameters(int x1, int y1, int x2, int y2) {
		// Ax + By + C = 0
		A = (double)(y1-y2)/(x1-x2);
		C = y1 - A * x1;
	}

	/***  Getters & setters ***/
	public void setColor(Color c) {this.color = c;}

	public Color getColor() {return color;}

	public void setStroke(float stroke) {this.stroke = stroke;}

	public int getSrc() {return node1.getNode_id();}

	public int getDest() {return node2.getNode_id();}

	public Node getNode1() {return node1;}

	public Node getNode2() {return node2;}

	@Override
	public String toString() {
		return "(node1,node2) = ("+ node1.toString() +","+node2.toString()+")";
	}
}
