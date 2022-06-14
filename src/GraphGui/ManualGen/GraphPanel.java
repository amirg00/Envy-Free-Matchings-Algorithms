package GraphGui.ManualGen;

import api.GeoLocation;
import api.UndirectedBipartiteGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;


enum PressState{
	EDGE("edge"),
	NODE("node"),
	NONE("none");
	
	String state = "";
	PressState(String state) {
		this.state=state;
	}
	
	public PressState find(String state) {
		for(PressState st : values())
			if(state.toLowerCase().equals(state))
				return st;
		return null;
	}
}

public class GraphPanel extends JPanel implements MouseMotionListener,MouseListener{
	private static final long serialVersionUID = 1L;
	private Graph graph;
	private int mouseX = 0;
	private int mouseY = 0;
	private boolean buttonMouseLeft = false;
	private boolean buttonMouseRight = false;
	private boolean mouseDragged = false;
	private boolean activeLine = false;
	private int cursor = Cursor.DEFAULT_CURSOR;

	private Node currentNode = null;
	private Edge currentEdge = null;
	
	private Node nodeFrom = null;
	
	private PressState pressState = PressState.NONE;
	
	public GraphPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		requestFocus();
	}

	@Override
	protected void paintComponent(Graphics g) {
//		if (activeLine){
//			drawLine(g, mouseX, mouseY);
//		}
		super.paintComponent(g);
		if(graph == null) return;
		graph.draw(g);
	}
	
	public void setGraph(Graph graph) {this.graph = graph;}
	public Graph getGraph() {return graph;}

	private void moveNode(int dx, int dy, Node node) {	graph.moveNode(dx,dy,node);	}   // Mouse dragged on node
	private void moveAllNodes(int dx, int dy) {	graph.moveAllNodes(dx,dy);	}		    // Mouse dragged not on node
	private void moveEdge(int dx, int dy, Edge edge) { graph.moveEdge(dx, dy, edge);}
	
	private Node findNode(int mx, int my, MouseEvent event) {
		Node nodeFound = null;
		try {
			nodeFound = graph.findNode(mx,my);;
		}
		catch (Exception e){
			/* Graph is empty!*/
		}
		return nodeFound;
	}

	private Edge findEdge(int mx, int my, MouseEvent event) {
		Edge edgeFound = null;
		try {
			edgeFound = graph.findEdge(mx, my);
		}
		catch (Exception e){
			/* Graph is empty!*/
		}
		return edgeFound;
	}
	
	private void setMouseCursor(MouseEvent e) {
		if(mouseDragged)
			cursor = Cursor.HAND_CURSOR;
		else
			cursor = Cursor.DEFAULT_CURSOR;
		if(findEdge(mouseX, mouseY, e) != null)
			cursor = Cursor.CROSSHAIR_CURSOR;
		if(findNode(mouseX, mouseY, e) != null) {
			cursor = Cursor.HAND_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(cursor));
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	private void currentNodeOnPress(MouseEvent e) {
		if(currentNode!=null) {
			currentNode.setPressed(false);
			currentNode = null;
			pressState = PressState.NONE;
		}
		currentNode = findNode(mouseX, mouseY, e);
		if(currentNode!=null && !currentNode.isPressed()) {
			currentNode.setPressed(true);
			pressState = PressState.NODE;
		}
	}


	private void currentEdgeOnPress(MouseEvent e) {
		if(currentEdge != null) {
		currentEdge = null;
		pressState = PressState.NONE;		
	}
		currentEdge = findEdge(mouseX, mouseY, e);
		if(currentEdge != null) {
			pressState = PressState.EDGE;
		}
	}
	
	private void connectNodes(MouseEvent e) {
		if(nodeFrom!=null) {
			Node nodeTo = findNode(mouseX, mouseY, e);
			if(nodeTo!=null) {
				graph.addEdge(new Edge(nodeFrom, nodeTo));
				//activeLine = false;
			}
			nodeFrom = null;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		currentNodeOnPress(e);
		if(pressState == PressState.NODE) {
			activeLine = true;
			nodeFrom = currentNode;
			System.out.println("EDGE!!! from Node: " + currentNode.getX() + ", " + currentNode.getY());
		}
		else if(pressState == PressState.NONE) {
			graph.addNode(new Node(e.getX(), e.getY()));
			repaint();
		}
		System.out.println("x : " + e.getX() + ", y: " + e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		currentEdgeOnPress(e);
		currentNodeOnPress(e);
		if(e.getButton() == 1) { // Left button pressed
			connectNodes(e);
			buttonMouseLeft = true;
		}
		else if(e.getButton() == 3) { // Right button pressed
			buttonMouseRight = true;
			if(pressState == PressState.NODE)
				createPopupMenu(e,currentNode);
			else if(pressState == PressState.EDGE)
				createPopupMenu(e,currentEdge);
		}
		setMouseCursor(e);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==1) {
			buttonMouseLeft = false;
		}
		else if(e.getButton()==3) {
			buttonMouseRight = false;
		}
		if(mouseDragged) {
			mouseDragged = false;
		}
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(buttonMouseLeft) {
			if(!mouseDragged) mouseDragged = true;
			
			if(pressState == PressState.NODE) {
				moveNode(e.getX() - mouseX, e.getY()-mouseY , currentNode);
			}
			else if(pressState == PressState.EDGE){
				moveEdge(e.getX() - mouseX, e.getY() - mouseY, currentEdge);
			}
			else if(pressState == PressState.NONE){
				moveAllNodes(e.getX() - mouseX,e.getY() - mouseY);
			}
			setMouseCursor(e);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouseCursor(e);
	}


	private void createPopupMenu(MouseEvent e, Node node) {
		JPopupMenu popupMenu = new JPopupMenu();
	//change color	
		JMenuItem item = new JMenuItem("color") {
			private static final long serialVersionUID = 1L;
		};
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));
		item.addActionListener((action) -> {//lambda expression actionListener to get new node color from JColorChooser
			Color newColor = JColorChooser.showDialog(this, "Change color window.", node.getColor());
			if(newColor!=null)
				node.setColor(newColor);
			repaint();
		});
		popupMenu.add(item);

		/*Node text*/
		item = new JMenuItem("set text");
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));
		item.addActionListener((action) -> {
			String text = JOptionPane.showInputDialog(this, "Enter the text for the pressed vertex:");
			node.setText(text);
			repaint();
		});
		popupMenu.add(item);

		/*remove node*/
		item = new JMenuItem("remove");
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));
		popupMenu.add(item);
		item.addActionListener((action) -> {
			graph.removeNode(node);
			repaint();
		});
		popupMenu.show(this, e.getX() , e.getY());	
	}
	
	private void createPopupMenu(MouseEvent e, Edge edge) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("remove");
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// lambda expression actionListener to get new node color from JColorChooser
		item.addActionListener((action) -> {
			graph.removeEdge(edge);
			repaint();
		});
		popupMenu.add(item);
		//set edge color
		item = new JMenuItem("color");
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));
		item.addActionListener((action) -> {
			Color newColor = JColorChooser.showDialog(this, "Change color window.", edge.getColor());
			if(newColor!=null)
				edge.setColor(newColor);
			repaint();
		});
		popupMenu.add(item);

		// set edge stroke
		item = new JMenuItem("width");
		item.setCursor(new Cursor(Cursor.HAND_CURSOR));
		item.addActionListener((action) -> {
		try {
			float width =  Float.parseFloat((String) JOptionPane.showInputDialog(this,
			        "Text",
			        "Enter width '1-15", JOptionPane.INFORMATION_MESSAGE,
			        null,
			        null,
			        "Enter width '1-15"));
			if(width >= 1) edge.setStroke(width);
			}
			catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Width must be float!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			repaint();
		});
		popupMenu.add(item);
		popupMenu.show(this, e.getX() , e.getY());	
	}

	public void drawLine(Graphics g, int mouseX, int mouseY) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine(nodeFrom.getX(), nodeFrom.getY(), mouseX, mouseY);
		g2d.setStroke(new BasicStroke(1.0f));
		g.setColor(Color.black);
	}

	public UndirectedBipartiteGraph constructGraph(){
		UndirectedBipartiteGraph constructedGraph = new UndirectedBipartiteGraph();
		constructedGraph.clear();

		// Set nodes' id before connecting pairs
		int nodeCnt = 0;
		for (Node node : graph.getNodes()){
			node.setNode_id(nodeCnt++);
		}

		for (Edge e : graph.getEdges()){
			double rand = new Random().nextDouble();
			System.out.println("EDGE::: " + e.getSrc() + ", " + e.getDest());
			int src = e.getSrc(), dest = e.getDest();

			// Unexpected behaviour
			if (src == dest) {continue;}

			boolean isSrcInA = constructedGraph.isVertexIdInGroup(src, constructedGraph.getDisjointSet_A()),
					isSrcInB = constructedGraph.isVertexIdInGroup(src, constructedGraph.getDisjointSet_B()),
					isDestInA = constructedGraph.isVertexIdInGroup(dest, constructedGraph.getDisjointSet_A()),
					isDestInB = constructedGraph.isVertexIdInGroup(dest, constructedGraph.getDisjointSet_B());

			// both nodes don't exist
			if (!isSrcInA && !isSrcInB && !isDestInA && !isDestInB){
				Node srcNode = e.getNode1(), destNode = e.getNode2();
				constructedGraph.addNode(srcNode.getNode_id(), "A", new GeoLocation(srcNode.getX(), srcNode.getY(), 0));
				constructedGraph.addNode(destNode.getNode_id(), "B", new GeoLocation(destNode.getX(), destNode.getY(), 0));
				constructedGraph.connect(src, dest, rand * 5);
			}
			/* One of them doesn't exist */
			else if (!isSrcInA && !isSrcInB){ // src doesn't exist
				Node srcNode = e.getNode1();
				if (isDestInA){ // dest in A group
					constructedGraph.addNode(srcNode.getNode_id(), "B", new GeoLocation(srcNode.getX(), srcNode.getY(), 0));
				}
				else{ // src in B group
					constructedGraph.addNode(srcNode.getNode_id(), "A", new GeoLocation(srcNode.getX(), srcNode.getY(), 0));
				}
				constructedGraph.connect(src, dest, rand * 5);
			}

			else if (!isDestInA && !isDestInB){ // dest doesn't exist
				Node srcNode = e.getNode2();
				if (isSrcInA){ // src in A group
					constructedGraph.addNode(srcNode.getNode_id(), "B", new GeoLocation(srcNode.getX(), srcNode.getY(), 0));
				}
				else { // src in B group
					constructedGraph.addNode(srcNode.getNode_id(), "A", new GeoLocation(srcNode.getX(), srcNode.getY(), 0));
				}
				constructedGraph.connect(src, dest, rand * 5);
			}
			else{ // both nodes are in the graph - just connect between them.
				if (constructedGraph.isEdgeInEdges(src, dest)) {continue;}
				constructedGraph.connect(src, dest, rand * 5);
			}
		}
		return constructedGraph;
	}
}
