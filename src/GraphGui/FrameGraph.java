package GraphGui;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.UndirectedBipartiteGraph;
import api.UndirectedBipartiteGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FrameGraph extends JFrame implements ActionListener {

    //private DirectedWeightedGraph graph, copyGraph;
    //private DirectedWeightedGraphAlgorithms copyGraphAtBeginning;
    private UndirectedBipartiteGraph graph;
    private PanelBipartiteGraph panel;
    private Menu menuPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu, runMenu, editMenu,  helpMenu, viewMenu;
    private JMenuItem RandomGraph, HungarianMethodMenu;
    private Engine runner;

    public FrameGraph(UndirectedBipartiteGraph graph) {
        this.graph = graph;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit the app
        this.setTitle("Hungarian Method"); // title
        this.setResizable(true); // prevent this to resize
        //this.copyGraphAtBeginning = new DirectedWeightedGraphAlgorithms();
        //this.copyGraphAtBeginning.init(graph);
        //this.copyGraph = copyGraphAtBeginning.copy();
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

        RandomGraph = new JMenuItem("Create Random Graph");
        RandomGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fileMenu.add(RandomGraph);
        RandomGraph.addActionListener(this);



        /* Algorithm which belongs to the run menu*/
        HungarianMethodMenu = new JMenuItem("Hungarian Method");
        runMenu.add(HungarianMethodMenu);
        HungarianMethodMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        HungarianMethodMenu.addActionListener(this);


        /* Add each menu to the menu bar.*/
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(runMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);



        this.panel = new PanelBipartiteGraph(graph);
        //menuPanel.setVisible(false);
        this.add(panel);

        centreWindow(this);
        //this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/(add image).png")));
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
            System.out.println("...");
        }

        else if (e.getSource() == HungarianMethodMenu){
            System.out.println("************ Hungarian Method **************");
            runner = new Engine(graph, this);
            runner.start();
            System.out.println("************ Hungarian Method **************");

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

//    /**
//     * This method navigate between panels, to switch between the login panel to the stage panel.
//     * @param flag
//     */
//    public void PlayButtonPressed(boolean flag){
//        if (!flag){
//            this.menuPanel = new Menu();
//            this.setLocationRelativeTo(null);
//            this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/logo.png")));
//            this.setTitle("Login");
//            this.add(menuPanel);
//            centreWindow(this);
//            this.pack();
//        }
//        else{
//            this.panel = new PanelBipartiteGraph(graph);
//            menuPanel.setVisible(false);
//            this.add(panel);
//            this.setTitle("Pokémon v1.1");
//            this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/PokemonLogo.png")));
//            this.pack();
//            this.setVisible(true);
//        }
//    }


    public PanelBipartiteGraph getPanel() {
        return panel;
    }

    public boolean getPlayButtonState(){
       return this.menuPanel.getPlayButtonState();
    }


    public static void main(String[] args) {
        new FrameGraph(new UndirectedBipartiteGraph());
    }
}