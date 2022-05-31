package api;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Brief explanation of my implementation: I have taken a hashmap to support as the collection to have the graph's properties,
 * because it is considered the best time complexity for collection's operations with keys (O(1) for the standard operations).
 * In order to know where exactly a certain edge from src to dest located in the hashmap,
 * we use map of maps where each location of the map is a vertex's neighbours.
 * Additionally, the vertices are ordered with keys by their id.
 */

public class DirectedWeightedGraph {

    private HashMap<Integer, NodeData> Vertices;
    private HashMap<Integer,HashMap<Integer, EdgeData>> Edges;
    private int MC, edgeSize, nodeSize;

    /**
     *  This constructor gets a JSON_Operation object, to initialize the graph's properties (edges and vertices),
     *  from the json file which constructed in DirectedWeightedGraphAlgorithmsImpl class.
     *  The method also initializes a deep copy of the edges for the class' methods.
     */
    public DirectedWeightedGraph() {
        MC = 0; edgeSize = 0; nodeSize=0;
        Vertices = new HashMap<>();
        Edges = new HashMap<>();
    }

    public DirectedWeightedGraph(HashMap<Integer, NodeData> Vertices, HashMap<Integer,HashMap<Integer, EdgeData>> Edges) {
        MC = 0; edgeSize = Edges.size(); nodeSize = Vertices.size();
        this.Edges = Edges;
        this.Vertices = Vertices;
    }
    /**
     * This method gets a graph - g and performs a deep copy.
     * @param g a given directed weighted graph to (deep) copy.
     * @return the (deep) copy of the graph - g.
     */
    public DirectedWeightedGraph deepCopy(DirectedWeightedGraph g) {
        Iterator<EdgeData> edges = g.edgeIter(); Iterator<NodeData> nodes = g.nodeIter();
        DirectedWeightedGraph deepCopyGraph = new DirectedWeightedGraph(new HashMap<>(),new HashMap<>());
        while (nodes.hasNext()){
            NodeData vertex = nodes.next();
            NodeData newVertex = new NodeData(vertex);
            deepCopyGraph.addNode(newVertex);
        }
        while (edges.hasNext()){
            EdgeData edge = edges.next();
            deepCopyGraph.connect(edge.getSrc(),edge.getDest(),edge.getWeight());
        }
        deepCopyGraph.edgeSize = g.edgeSize();
        deepCopyGraph.MC = g.getMC();
        return deepCopyGraph;
    }

    public NodeData getNode(int key) {return Vertices.get(key);}


    public EdgeData getEdge(int src, int dest) {
        if (!isEdge(src,dest)) {return null;}
        return Edges.get(src).get(dest);
    }

    public void addNode(NodeData n) {
        int node_id = n.getKey();
        if (Vertices.containsKey(node_id)) {return;}
        Vertices.put(node_id, n);
        Edges.put(node_id,new HashMap<>());
        nodeSize++;
        MC++;
    }

    /**
     * The method connects an edge between the two vertices as written in the interface.
     * @implNote note that if there is already an edge between them, then the method overrides
     * the old one, and connects a new edge, for GUI purposes.
     * @param src the source of the edge.
     * @param dest the destination of the edge.
     * @param w positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    public void connect(int src, int dest, double w) {
        if (src == dest) return;
        String info = "Src: "+src+", "+"Dest: "+dest+", "+"Weight: "+w;
        EdgeData connectedEdge = new EdgeData(src, dest, Color.BLUE.getRGB(), w, info);
        if (getEdge(src,dest) != null){removeEdge(src,dest);}
        Edges.get(src).put(dest,connectedEdge);
        edgeSize++;
        MC++;
    }

    public Iterator<NodeData> nodeIter() {return new Iterator_Operation<>(Vertices.values().iterator(),MC);}

    public Iterator<EdgeData> edgeIter() {
        ArrayList<EdgeData> edges = new ArrayList<>();
        for(HashMap<Integer,EdgeData> map: Edges.values()){
            for (EdgeData e: map.values()){
                if (e!=null){
                    edges.add(e);
                }
            }
        }
        return new Iterator_Operation<>(edges.iterator(),MC);
    }

    /**
     * As the method "removeNode", this method removes, allegedly, the vertex.
     * It is actually removes all edges starts or ends with the given node.
     * However, instead of removing it from the graph, because it isn't what explicitly written in the interface,
     * it removes it from a copy already constructed in the constructor.
     * Then, returns the iterator for this copy edges collection.
     *
     * @param node_id a given node's id.
     * @return an iterator of the edges after removing all edges start or end in this given vertex.
     */
    public Iterator<EdgeData> edgeIter(int node_id) {
        if (!Edges.containsKey(node_id)){return null;}
        return new Iterator_Operation<>(Edges.get(node_id).values().iterator(),MC);
    }


    /**
     *
     * The method goes over all vertices and checks which vertex has an edge with the given node.
     * If it finds such vertex it removes the edge between them.
     * Finally, the method removes the vertex from the vertices' collection (Hashmap).
     *
     * In terms of time complexity, we here got O(V) rather than O(k).
     * The reason for that is that our implementation works the way in which an edge from src to dest,
     * could be found in the hash map of the edges under the key = "src + dest".
     * E.g: given an edge from 0 to 1, then the key for this edge would be the following string "01".
     *
     * Therefore, we have to go through all strings which starts with the given key, or ends there,
     * and that's the reason why we need to go over all vertices.
     * The different between O(V) and O(k) is a bit of a pain, but with a further look
     * it makes no different if the graph is complete, or if it's a standard graph with
     * high amount of edges.
     *
     * @param key a given id of a node.
     * @return the removed node.
     */
    public NodeData removeNode(int key) {
        if (!Vertices.containsKey(key)) {return null;}
        Iterator<NodeData> nodes = nodeIter();
        while(nodes.hasNext()){
            int curr = nodes.next().getKey();
            if (key == curr) {continue;}
            removeEdge(key,curr);
            removeEdge(curr,key);
        }
        nodeSize--;
        MC++;
        return Vertices.remove(key);
    }

    public EdgeData removeEdge(int src, int dest) {
        if (isEdge(src, dest)) {
            edgeSize--;
            MC++;
            return Edges.get(src).remove(dest);
        }
        return null;
    }

    public int nodeSize() {
        return nodeSize;
    }

    public int edgeSize() {
        return edgeSize;
    }

    public int getMC() {
        return MC;
    }

    /**
     * The method checks if there is an edge between the source vertex to the destination vertex.
     * @param src source vertex index.
     * @param dest destination vertex index.
     * @return if there is an edge from src to dest.
     */
    public boolean isEdge(int src, int dest){
        return  Vertices.containsKey(src) &&
                Vertices.containsKey(dest) &&
                Edges.containsKey(src) &&
                Edges.get(src).containsKey(dest) &&
                src != dest;
    }

    // getters & setters //
    public HashMap<Integer, NodeData> getVertices() {
        return Vertices;
    }

    public void setVertices(HashMap<Integer, NodeData> vertices) {
        Vertices = vertices;
    }

    public HashMap<Integer,HashMap<Integer, EdgeData>>  getEdges() {
        return Edges;
    }

    public void setEdges(HashMap<Integer,HashMap<Integer, EdgeData>>  edges) {
        Edges = edges;
    }
}
