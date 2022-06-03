package api;

import java.util.*;

public class UndirectedBipartiteGraph {
    private HashMap<Integer,HashMap<Integer, NodeData>> neighbors;
    private ArrayList<EdgeData> matches, edges;
    private ArrayList<NodeData> disjointSet_A, disjointSet_B, vertices;
    private int node_size, edge_size;

    public UndirectedBipartiteGraph(){
        clear();
        int randVerticesAmount = 10 + (int) (Math.random() * 30);
        createRandomBipartiteGraph(randVerticesAmount);
    }

    /**
     * A copy constructor
     * @param bGraph a given bipartite graph.
     */
    public UndirectedBipartiteGraph(UndirectedBipartiteGraph bGraph){
        clear();

        // Add nodes in deep copy
        // Set A
        for (NodeData node : bGraph.getDisjointSet_A()){
            NodeData copyNode = new NodeData(node);
            neighbors.put(node.getKey(), new HashMap<>());
            disjointSet_A.add(copyNode);
            vertices.add(copyNode);
            node_size++;
        }
        // Set B
        for (NodeData node : bGraph.getDisjointSet_B()){
            NodeData copyNode = new NodeData(node);
            neighbors.put(node.getKey(), new HashMap<>());
            disjointSet_B.add(copyNode);
            vertices.add(copyNode);
            node_size++;
        }

        // Add edges in a deep copy
        for (EdgeData e : bGraph.edges){
            int src = e.getSrc(), dest = e.getDest();
            if ((isVertexIdInGroup(src, disjointSet_A) && isVertexIdInGroup(dest, disjointSet_B))
                    || (isVertexIdInGroup(src, disjointSet_B) && isVertexIdInGroup(dest, disjointSet_A))){
                EdgeData copyEdge = new EdgeData(e);
                neighbors.get(src).put(dest, getNode(dest));
                neighbors.get(dest).put(src, getNode(src));
                edges.add(copyEdge);
                edge_size++;
            }
        }
    }

    /**
     * Initialize the current graph edges and vertices according to the given two sets
     * of vertices that are given.
     * @implNote this isn't a deep copy!
     * @param graph a given bipartite graph.
     * @param X a given first set of vertices.
     * @param Y a given second set of vertices.
     */
    public UndirectedBipartiteGraph(UndirectedBipartiteGraph graph, ArrayList<NodeData> X, ArrayList<NodeData> Y){
        clear();

        // Add to group A
        addNodes(X, disjointSet_A);

        // Add to group B
        addNodes(Y, disjointSet_B);

        // Add edges
        connectEdges(graph);
    }

    public void addNodes(ArrayList<NodeData> set, ArrayList<NodeData> targetSet){
        for (NodeData node : set){
            targetSet.add(node);
            vertices.add(node);
            node_size++;
        }
    }

    public void connectEdges(UndirectedBipartiteGraph graph){
        for (EdgeData e : graph.edges){
            int src = e.getSrc(), dest = e.getDest();
            if ((isVertexIdInGroup(src, disjointSet_A) && isVertexIdInGroup(dest, disjointSet_B))
                || (isVertexIdInGroup(src, disjointSet_B) && isVertexIdInGroup(dest, disjointSet_A))){
                edges.add(e);
                edge_size++;
            }
        }
    }

    /**
     * Method clears the graph.
     */
    public void clear(){
        node_size = 0; edge_size = 0;
        edges = new ArrayList<>();
        matches = new ArrayList<>();
        disjointSet_A = new ArrayList<>();
        disjointSet_B = new ArrayList<>();
        vertices = new ArrayList<>();
        neighbors = new HashMap<>();
    }



    /**
     * Creates a random bipartite graph by a given amount of vertices.
     * @param verticesAmount given amount of vertices to create.
     */
    public void createRandomBipartiteGraph(int verticesAmount){

        Random rand = new Random();
        for (int node_id = 0; node_id < verticesAmount; node_id++) {
            NodeData randVertex = random_vertex_generator(node_id);
            if (rand.nextDouble() <= 0.5){ // 50% to insert it to A
                disjointSet_A.add(randVertex);
            }
            else{ // O.W. goes to B - also 50%
                disjointSet_B.add(randVertex);
            }
            neighbors.put(node_id, new HashMap<>());
            vertices.add(randVertex);
            node_size++;
        }
        // Create random edges
        random_edges_generator();
    }

    public NodeData random_vertex_generator(int key) {
        int tag = 1 + (int) (Math.random() * 10);
        double weight = 0; // for heights algorithms
        double EPSILON = .000000001;
        double x = 35 + Math.random() * (1- EPSILON), y = 32 + Math.random() * (1- EPSILON), z = 0.0;
        GeoLocation Node = new GeoLocation (x, y, z);
        String info = "Key:" + key + "\n" + "Tag:" + tag + "\n" + "Weight" + weight + "\n" + "GeoLocation:" + Node;
        return new NodeData(key, tag, info, weight, Node);
    }

    /**
     * Creates the graph's edges in random.
     */
    public void random_edges_generator() {
        for (NodeData nodeData : disjointSet_A) {
            int randEdgesAmount = 5 + (int) (Math.random() * (disjointSet_B.size()/10));
            int[] distinct_nums = distinct_random_numbers(randEdgesAmount, disjointSet_B.size() - 1, disjointSet_B);
            for (int distinct_num : distinct_nums) {
                int src = nodeData.getKey();
                if (isEdgeInEdges(src, distinct_num)) {continue;}
                neighbors.get(src).put(distinct_num, getNode(distinct_num));
                neighbors.get(distinct_num).put(src, getNode(src));
                edges.add(random_edge_generator(src, distinct_num));
                edge_size++;
            }
        }

        for (NodeData nodeData : disjointSet_B) {
            int randEdgesAmount = 5 + (int) (Math.random() * (disjointSet_A.size()/10));
            int[] distinct_nums = distinct_random_numbers(randEdgesAmount, disjointSet_A.size() - 1, disjointSet_A);
            for (int distinct_num : distinct_nums) {
                int src = nodeData.getKey();
                if (isEdgeInEdges(src, distinct_num)) {continue;}
                neighbors.get(src).put(distinct_num, getNode(distinct_num));
                neighbors.get(distinct_num).put(src, getNode(src));
                edges.add(random_edge_generator(src, distinct_num));
                edge_size++;
            }
        }

    }

    /**
     * Generates a random edge with a given source and destination node's id.
     * @param src a given source id.
     * @param dest a given destination id.
     * @return a random edge.
     */
    public EdgeData random_edge_generator(int src, int dest){
        int tag = 1 + (int) (Math.random() * 10);
        double weight = Math.random() * 10;
        String info = "Src:" + src + "\n" + "Dest:" + dest + "\n" + "Tag" + tag + "\n" + "Weight:" + weight;
        return new EdgeData(src, dest, tag, weight, info);
    }


    /**
     * This method creates certain amount of unique/distinct random numbers in range: [0,max]
     * The advantage of it, is that the data structure can find the two distinct random numbers
     * very fast for large range.
     * @param Max the maximum number of the range.
     * @return 'amount' distinct random numbers of the range.
     */
    public int[] distinct_random_numbers(int amount, int Max, ArrayList<NodeData> group){
        BitSet bs = new BitSet(Max);
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i <= Max; i++) { indices.add(group.get(i).getKey());}
        Collections.shuffle(indices);

        int cardinality = 0;
        while(cardinality < amount) {
            int v = indices.get(cardinality);
            bs.set(v);
            cardinality++;
        }
        return bs.stream().toArray();
    }

    //TODO: check this function, check isn't good...
    public boolean isVertexInMatches(NodeData v){
        for (EdgeData currMatch : matches) {
            if (currMatch.getSrc() == v.getKey() || currMatch.getDest() == v.getKey()) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method returns the neighbors of the node, returned as NodeData object.
     * @param node_id node's id.
     * @return
     */
    public ArrayList<NodeData> edgesOut(int node_id){
        HashMap<Integer, NodeData> N = neighbors.get(node_id);
        Collection<NodeData> nodes = N.values();
        return new ArrayList<>(nodes);
    }


    public boolean isDirectEdgeInMatches(int src, int dest){
        for (EdgeData currMatch : matches){
            if ((currMatch.getSrc() == src && currMatch.getDest() == dest)
                    || (currMatch.getSrc() == dest && currMatch.getDest() == src)){
                return true;
            }
        }
        return false;
    }

    public void removeDirectEdgeInMatches(int src, int dest){
        matches.removeIf(currMatch ->
                   (currMatch.getSrc() == src && currMatch.getDest() == dest)
                || (currMatch.getSrc() == dest && currMatch.getDest() == src));
    }

    public boolean isVertexInGroup(NodeData v, ArrayList<NodeData> group){
        for(NodeData v_A : group){
            if(v_A.equals(v)){
                return true;
            }
        }
        return false;
    }

    public boolean isVertexIdInGroup(int node_id, ArrayList<NodeData> group){
        for(NodeData v : group){
            if(v.getKey() == node_id){
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeInEdges(int src, int dest){
        for (EdgeData e : edges){
            if ((e.getSrc() == src && e.getDest() == dest) || (e.getSrc() == dest && e.getDest() == src)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        UndirectedBipartiteGraph g = new UndirectedBipartiteGraph();
        System.out.println(Arrays.toString(g.disjointSet_A.toArray()));
        System.out.println(Arrays.toString(g.disjointSet_B.toArray()));
        System.out.println(Arrays.toString(g.edges.toArray()));

    }

    /**
     * Get node's object by node's key-id.
     * @param key node's key.
     * @return instance for the requested node.
     */
    public NodeData getNode(int key){
        for (NodeData v : vertices){
            if (key == v.getKey()){
                return v;
            }
        }
        return null;
    }


    public ArrayList<NodeData> getDisjointSet_A() {return disjointSet_A;}

    public ArrayList<NodeData> getDisjointSet_B() {return disjointSet_B;}

    public int nodeSize() {return node_size;}

    public int edgeSize() {return edge_size;}

    public ArrayList<EdgeData> getMatches() {return matches;}

    public ArrayList<EdgeData> getEdges() {return edges;}

    public ArrayList<NodeData> getVertices() {return vertices;}

    public void setMatches(ArrayList<EdgeData> matches) {this.matches = matches;}
}


