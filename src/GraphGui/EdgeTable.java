package GraphGui;

import api.EdgeData;
import api.UndirectedBipartiteGraph;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class EdgeTable extends JFrame implements ActionListener {
    private JTable edgeTable;
    private JButton closeButton;
    private UndirectedBipartiteGraph graph;
    private Object[][] data;
    private String[] columnNames;
    private JScrollPane scrollPane;

    public EdgeTable(UndirectedBipartiteGraph graph){
        this.graph = graph;
        closeButton = new JButton("Close");
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new FlowLayout());
        this.columnNames = new String[]{"First Node", "Second Node", "Weight"};
        this.data = new Object[graph.edgeSize()][3];
        insert();
        this.edgeTable = new JTable(data, columnNames);
        edgeTable.setPreferredScrollableViewportSize(new Dimension(500,300));
        this.scrollPane = new JScrollPane(edgeTable);
        scrollPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centreWindow(this);
        this.add(scrollPane);
        this.add(closeButton);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(600,400);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("Edges Table");
        closeButton.addActionListener(this);
        edgeTable.setDefaultEditor(Object.class, null); //make the table's cells not editable.
        setColumnValuesCenter();
    }

    public void insert(){
        Iterator<EdgeData> edges = graph.getEdges().iterator();
        int m = 0;
        while(edges.hasNext()){
            EdgeData curr = edges.next();
            int src = curr.getSrc();
            int dest = curr.getDest();
            double weight = curr.getWeight();
            data[m][0] = src; data[m][1] = dest; data[m++][2] = weight;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton){dispose();}
    }
    /**
     * This method puts the Column values to be located at the center.
     */
    public void setColumnValuesCenter(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        edgeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        edgeTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        edgeTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }
    /**
     * This method centre the new window opening.
     * @param frame the frame to set its location.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 3.2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 3.2);
        frame.setLocation(x, y);
    }
}