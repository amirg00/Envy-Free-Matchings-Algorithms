package GraphGui.ManualGen;

import api.UndirectedBipartiteGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GraphEditor extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Manual Graph Generator";


	private UndirectedBipartiteGraph graph;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuGraph = new JMenu("Graph");
	private JMenuItem menuNew = new JMenuItem("New");
	private JMenuItem menuExit = new JMenuItem("Exit");
	private JMenuItem menuGetGraph = new JMenuItem("Get Graph Exit");
	private GraphPanel graphPanel = new GraphPanel();
	
	public GraphEditor() {
		addActionListeners();
		setMenu();
		setFrame();
		setCursors();
		setContentPane(graphPanel);
		setVisible(true);
	}
	
	private void addActionListeners() {
		menuNew.addActionListener(this);
		menuGetGraph.addActionListener(this);
		menuExit.addActionListener(this);
	}
	
	private void setMenu() {
		setJMenuBar(menuBar);
		menuBar.add(menuGraph);
		menuGraph.add(menuNew);
		menuGraph.addSeparator();
		menuGraph.add(menuGetGraph);
		menuGraph.addSeparator();
		menuGraph.add(menuExit);
	}

	private void setCursors(){
		menuBar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		menuGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
		menuNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
		menuGetGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
		menuExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

	}
	
	private void setFrame() {
		setTitle(TITLE);
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if(source == menuNew) {
			graphPanel.setGraph(new Graph());
		}

		else if(source == menuGetGraph){
			graph = graphPanel.constructGraph();
			this.dispose();
		}

		else if(source == menuExit) {
			this.dispose();
		}
	 	graphPanel.repaint();
	}

	public UndirectedBipartiteGraph getGraph() {return graph;}
}
