package GraphGui;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import api.UndirectedBipartiteGraph;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class PanelBipartiteGraph extends JPanel {
    private UndirectedBipartiteGraph graph;
    private HashMap<Integer,GraphPoint> points;
    private List<GraphEdge> edges;
    private int radius = 1; //good for 100 vertices.
    private double Phi = Math.toRadians(40);
    private double virtualScale = 1.0;
    private Graphics2D g2d;
    private Point2D minRange;
    private Point2D maxRange;
    private double insets;
    private static boolean stopButtonPressed = false;




    PanelBipartiteGraph(UndirectedBipartiteGraph graph) {
        this.setPreferredSize(new Dimension(1100,750));
        this.setBackground(Color.white);
        this.points = new HashMap<>();
        this.graph = graph;
        pointInit();
        EdgeInit();
        setMinMaxRange();
        int numberOfZeros = (int) Math.log10(graph.nodeSize());
        if (numberOfZeros>2){radius = (int) (radius/Math.pow(2,numberOfZeros-2));}

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,750);
    }

    /********************************************************************************************************
     * <paintComponent>
     * paintComponent method draw the all edges and points in the graph.
     * </paintComponent>
     *********************************************************************************************************/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g.create();
        FontMetrics fm = g2d.getFontMetrics();
        insets = fm.getHeight() + radius;


        // Background:
        //Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GraphGui/Icons/background.png"));
        //g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        /* Draw arena section: */
        // Paint edges
        ArrayList<String> LineSave = new ArrayList<>();
        for (GraphEdge ed : edges) {
            ArrayList<GraphPoint> p = ed.getPoints();
            LineSave.add(""+p.get(0).getId()+"-"+p.get(1).getId());
            paintLine(g2d, p.get(0), p.get(1), insets, ed.getWeight(),LineSave, ed.getTag(),ed.getStroke(),ed.getTag_2());
        }
        // paint vertices
        for (GraphPoint gp : points.values()) {
            paintPoint(g2d, gp, insets, gp.getTag(), gp.getTag_2());
        }

        g2d.dispose();
    }



    private boolean CheckerEdgeDraw(ArrayList<String> check,String name){
        for (int i = 0; i < check.size()-1; i++) {
            if(Objects.equals(check.get(i), name))return true;
        }
        return false;
    }


    protected double angleBetween(Point2D from, Point2D to) {
        double x = from.getX();
        double y = from.getY();
        double deltaX = to.getX() - x;
        double deltaY = to.getY() - y;
        double rotation = -Math.atan2(deltaX, deltaY);
        rotation = Math.toRadians(Math.toDegrees(rotation) + 180);
        return rotation;
    }

    protected Point2D getPointOnCircle(Point2D center, double radians) {
        double x = center.getX();
        double y = center.getY();
        radians = radians - Math.toRadians(90.0);
        double xPosy = Math.round((float) (x + Math.cos(radians) * radius));
        double yPosy = Math.round((float) (y + Math.sin(radians) * radius));
        return new Point2D.Double(xPosy, yPosy);
    }

    /********************************************************************************************************
     * @name: paintLine Method take the graphics2D, 2 points and weight.
     * we calculate the scale of the points in the graph because we want the graph accuracy.
     * we calculate the angel between two point in the graph and draw line to the tip of the point.
     *********************************************************************************************************/

    private void paintLine(Graphics2D g2d, GraphPoint from, GraphPoint to, double insets, String weight, ArrayList<String> list, Color color, int stroke, Color color_2) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //boolean flag = CheckEdge(to,from);
        boolean flag = CheckerEdgeDraw(list,to.getId()+"-"+from.getId());
        Point2D fromPoint = translate(from, insets);
        Point2D toPoint = translate(to, insets);
        double fromp = angleBetween(fromPoint, toPoint);
        double top = angleBetween(toPoint, fromPoint);
        Point2D pointFrom = getPointOnCircle(fromPoint, fromp);
        Point2D pointTo = getPointOnCircle(toPoint, top);
        Line2D line = new Line2D.Double(pointFrom, pointTo);

        g2d.setStroke(new BasicStroke(stroke));
        g2d.setColor(color);
        g2d.draw(line);
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, double angle, String text) {
        g2d.translate((float)x,(float)y);
        g2d.rotate(angle);
        g2d.drawString(text,0,0);
        g2d.rotate(-angle);
        g2d.translate(-(float)x,-(float)y);
    }


    private static double[][] Verticle(double x1, double y1, double m_Segment, int length){
        double m = -1/m_Segment;
        // y = mx - mx1 + y1
        // System.out.println(1/Math.pow(m_Segment,2));
        double k = 1 + (1/Math.pow(m_Segment,2));
        // System.out.println(k);
        double x1_ans = x1 + (length/Math.sqrt(k));
        double x2_ans = x1 - (length/Math.sqrt(k));

        // finds the y for the resulted points.
        double y1_ans = m*x1_ans - m*x1 + y1;
        double y2_ans = m*x2_ans - m*x1 + y1;

        return new double[][]{{x1_ans,y1_ans},{x2_ans,y2_ans}};
    }

    void paintPoint(Graphics2D g2d, GraphPoint gp, double insets, Color color, Color color_2) {
        Graphics2D g2 = (Graphics2D) g2d.create();
        Point2D translated = translate(gp, insets);

        double xPos = translated.getX();
        double yPos = translated.getY();

        double offset = radius;

        g2.translate(xPos - offset, yPos - offset);
        g2.setPaint(color);
        g2.fill(new Ellipse2D.Double(0, 0, offset * 2, offset * 2));
        g2.setPaint(Color.black);
        g2.draw(new Ellipse2D.Double(0, 0, offset * 2, offset * 2));

        // draws node's id above the node
        FontMetrics fm = g2d.getFontMetrics();
        String text = gp.getId();
        double x = xPos - (fm.stringWidth(text) / 2);
        double y = (yPos - radius - fm.getHeight()) + fm.getAscent();
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Cabin", Font.BOLD, 14));
        g2d.drawString(text, (float) x, (float) y);
        g2.dispose();
    }




    protected Point2D translate(GraphPoint gp, double insets) {
        double xRange = maxRange.getX() - minRange.getX();
        double yRange = maxRange.getY() - minRange.getY();

        double offset = insets;
        double width = getWidth() - (offset * 2);
        double height = getHeight() - (offset * 2);

        double xScale = width / xRange;
        double yScale = height / yRange;

        Point2D original = gp.getPoint();

        double x = offset + ((original.getX() - minRange.getX()) * xScale);
        double y = offset + ((original.getY() - minRange.getY()) * yScale);

        // System.out.println(gp.getId() + " " + x + " x " + y);

        return new Point2D.Double(x, y);
    }

    /*
     * the for loop is init all the edges existed in the graph and take each point already exists,
     * make a new arraylist of points and know to the correct edges between them.
     * */
    public void EdgeInit() {
        edges = new ArrayList<>();
        for(EdgeData curr : graph.getEdges()){
            ArrayList<GraphPoint> temp = new ArrayList<>();
            temp.add(new GraphPoint(String.valueOf(curr.getSrc()),
                     new Point2D.Double(graph.getNode(curr.getSrc()).getLocation().x(),
                     graph.getNode(curr.getSrc()).getLocation().y()),Color.red, Color.red));

            temp.add(new GraphPoint(String.valueOf(curr.getDest()),
                     new Point2D.Double(graph.getNode(curr.getDest()).getLocation().x(),
                            graph.getNode(curr.getDest()).getLocation().y()),Color.red,Color.red));
            edges.add(new GraphEdge(String.valueOf(curr.getWeight()), temp,new Color(204, 204, 204), 2, Color.red));
        }
    }

    /*
     * the for loop is init all the points in the graph.
     * By given a name of the vertex and the positions x,y.
     * */
    public void pointInit() {
        points = new HashMap<>();
        for (NodeData curr : graph.getVertices()){
            if (curr != null) {
                String currKey = String.valueOf(curr.getKey());
                double currPosX = curr.getLocation().x();
                double currPosY = curr.getLocation().y();
                points.put(curr.getKey(),
                           new GraphPoint(currKey, new Point2D.Double(currPosX, currPosY),
                           Color.blue, Color.red));
            }
        }
    }

    /*
     * search if the point is equal to the string id  and get the point from the points objects.
     * draw each line have edges.
     */
    public void setMinMaxRange() {

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (GraphPoint gp : points.values()) {
            minX = Math.min(minX, gp.getPoint().getX());
            maxX = Math.max(maxX, gp.getPoint().getX());
            minY = Math.min(minY, gp.getPoint().getY());
            maxY = Math.max(maxY, gp.getPoint().getY());
        }

        minRange = new Point2D.Double(minX, minY);
        maxRange = new Point2D.Double(maxX, maxY);
    }

    public Graphics2D getG2d() {
        return g2d;
    }
    public double get_Insets(){
        return insets;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public HashMap<Integer,GraphPoint> getPoints() {
        return points;
    }

    public void setGraph(UndirectedBipartiteGraph g){
        this.graph = g;
        pointInit();
        EdgeInit();
        setMinMaxRange();
    }

    public void drawEdge(EdgeData e, Color color, int stroke){
        for (GraphEdge ge: edges){
            if ((ge.getPoints().get(0).getId().equals(String.valueOf(e.getSrc()))
                && ge.getPoints().get(1).getId().equals(String.valueOf(e.getDest())))
                || ge.getPoints().get(0).getId().equals(String.valueOf(e.getDest()))
                   && ge.getPoints().get(1).getId().equals(String.valueOf(e.getSrc()))){
                ge.setTag(color);
                ge.setStroke(stroke);
                return;
            }

        }
    }

    public void drawAllEdges(Color color, int stroke){
        for (GraphEdge ge : edges){
            ge.setTag(color);
            ge.setStroke(stroke);
        }
    }

    public void setStopButtonPressed(boolean stopButtonPressed) {
        this.stopButtonPressed = stopButtonPressed;
    }

    public static boolean isStopButtonPressed() {
        return stopButtonPressed;
    }
}