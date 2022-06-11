package GraphGui.ManualGen;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Graph implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<Node> nodes;
	private List<Edge> edges;
	
	public Graph() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	public void addNodes(Node ... ns) {
		nodes.addAll(Arrays.asList(ns));
	}
	
	public void addEdges(Edge ... es) {
		edges.addAll(Arrays.asList(es));
	}
	
	public void removeNode(Node node) {
		nodes.remove(node);
		edges.removeAll(node.getEdges());
	}
	
	public void removeEdge(Edge e) {edges.remove(e);}
	
	public void draw(Graphics g) {
		for(Edge edge : edges) edge.draw(g);
		for(Node node : nodes) node.draw(g);
	}
	
	public void moveNode(int dx, int dy, Node node) {
		node.setX(node.getX()+dx);
		node.setY(node.getY()+dy);
	}
	
	public void moveEdge(int dx, int dy, Edge edge) {
		edge.move(dx, dy);
	}
	
	public void moveAllNodes(int dx, int dy) {
		for(Node node : nodes) moveNode(dx, dy, node);
	}
	
	public Node findNode(int mx, int my) {
		for(Node node : nodes)
			if(node.isMouseOver(mx, my))
				return node;
		return null;
	}
	
	public Edge findEdge(int mx, int my) {
		for(Edge edge : edges)
			if(edge.isMouseOver(mx, my))
				return edge;
		return null;
	}
	
	public List<Node> getNodes(){return nodes;}
	public List<Edge> getEdges(){return edges;}
	
	public void setNodes(List<Node> ns) {nodes.addAll(ns);}

	public void setEdges(List<Edge> es) {edges.addAll(es);}
	
	@Override
	public String toString() {
		String str = "";
		int counter=0;
		for(Node node : nodes)
			str += "Node["+counter++ +"]=("+node.toString()+"\n";
		counter=0;
		if(!edges.isEmpty())
			for(Edge edge : edges)
				str += "\nEdge["+counter++ +"]=("+edge.toString()+"\n";
		return str;
	}
}

