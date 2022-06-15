package GraphGui;

import GraphGui.ManualGen.GraphEditor;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.UndirectedBipartiteGraph;
import api.UndirectedBipartiteGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FrameGraph extends JFrame implements ActionListener {

    private GraphEditor graphEditor;
    private UndirectedBipartiteGraph graph;
    private PanelBipartiteGraph panel;
    private JMenuBar menuBar;
    private JMenu fileMenu, runMenu, editMenu,  helpMenu, viewMenu;
    private JMenuItem RandomGraph, HungarianMethodMenu, EnvyFreeMaxCardMenu, EnvyFreeMaxWeightMenu, EdgeTable, VertexTable,
    newGraph, loadNewGraph;

    private Engine runner;

    public FrameGraph(UndirectedBipartiteGraph graph) {
        this.graph = graph;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the app
        this.setTitle("Envy Free"); // title
        this.setResizable(true); // prevent this to resize
        this.setVisible(true);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        viewMenu = new JMenu("View");
        runMenu = new JMenu("Run");
        helpMenu = new JMenu("Help");

        /* Add cursors for menus*/
        fileMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        runMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /*Files*/
        RandomGraph = new JMenuItem("Create Random Graph");
        RandomGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fileMenu.add(RandomGraph);
        RandomGraph.addActionListener(this);

        newGraph = new JMenuItem("New");
        newGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fileMenu.add(newGraph);
        newGraph.addActionListener(this);

        loadNewGraph = new JMenuItem("Load Last Graph");
        loadNewGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fileMenu.add(loadNewGraph);
        loadNewGraph.addActionListener(this);


        /* Algorithm which belongs to the run menu*/
        HungarianMethodMenu = new JMenuItem("Hungarian Method");
        runMenu.add(HungarianMethodMenu);
        HungarianMethodMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        HungarianMethodMenu.addActionListener(this);

        EnvyFreeMaxCardMenu = new JMenuItem("Envy Free Max Cardinality");
        runMenu.add(EnvyFreeMaxCardMenu);
        EnvyFreeMaxCardMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        EnvyFreeMaxCardMenu.addActionListener(this);

        EnvyFreeMaxWeightMenu = new JMenuItem("Envy Free Max Weight");
        runMenu.add(EnvyFreeMaxWeightMenu);
        EnvyFreeMaxWeightMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        EnvyFreeMaxWeightMenu.addActionListener(this);

        /* Add each menu to the menu bar.*/
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(runMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);

        /*Tables: */
        EdgeTable = new JMenuItem("Edges Table");
        EdgeTable.addActionListener(this);
        EdgeTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        VertexTable = new JMenuItem("Vertices Table");
        VertexTable.addActionListener(this);
        VertexTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewMenu.add(VertexTable);
        viewMenu.add(EdgeTable);

        this.panel = new PanelBipartiteGraph(graph);
        this.add(panel);
        centreWindow(this);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RandomGraph){
            graph.clear();
            graph.createRandomBipartiteGraph(30);
            panel.setGraph(graph);
            panel.repaint();
        }

        else if (e.getSource() == newGraph){
            graphEditor = new GraphEditor();
        }

        else if (e.getSource() == loadNewGraph){
            if (graphEditor.getGraph() != null){
                graph = graphEditor.getGraph();
                panel.setGraph(graph);
                panel.repaint();
            }
        }

        else if (e.getSource() == HungarianMethodMenu){
            runner = new Engine(graph, this, states.HUNGARIAN);
            runner.start();
        }

        else if (e.getSource() == EnvyFreeMaxCardMenu){
            runner = new Engine(graph, this, states.ENVY_FREE_MAX_CARDINALITY);
            runner.start();
        }

        else if (e.getSource() == EnvyFreeMaxWeightMenu){
            runner = new Engine(graph, this, states.ENVY_FREE_MAX_WEIGHT);
            runner.start();
        }

        else if (e.getSource() == VertexTable){
            new VertexTable(graph);
        }

        else if (e.getSource() == EdgeTable){
            new EdgeTable(graph);
        }
    }

    /**
     * This method centre the new window opening.
     * @param frame the frame to set its location.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 8);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 20);
        frame.setLocation(x, y);
    }

    public void delay(int millis) {
        panel.repaint();
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public PanelBipartiteGraph getPanel() {
        return panel;
    }


    public static void main(String[] args) {
        new FrameGraph(new UndirectedBipartiteGraph());
    }
}