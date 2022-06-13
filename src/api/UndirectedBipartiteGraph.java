package api;

import java.util.*;

public class UndirectedBipartiteGraph {
    private HashMap<Integer,HashMap<Integer, EdgeData>> neighbors;
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
                neighbors.get(src).put(dest, copyEdge);
                neighbors.get(dest).put(src, copyEdge);
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

    /**
     * Method adds a set of nodes to a targeted set.
     * @param set a given set of nodes to add.
     * @param targetSet target nodes set.
     */
    public void addNodes(ArrayList<NodeData> set, ArrayList<NodeData> targetSet){
        for (NodeData node : set){
            targetSet.add(node);
            vertices.add(node);
            node_size++;
        }
    }

    /**
     * The method adds the graph's edges to the current graph.
     * @param graph a given undirected bipartite graph.
     */
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
     * Method adds random node with the given node's id to the current graph,
     * using the addNode utility.
     * @param node_id a given node's id
     * @param set a given string representing set of nodes ("A" or "B").
     */
    public void addNode(int node_id, String set){
        NodeData randNode = random_vertex_generator(node_id);
        addNodeUtil(randNode, set);
    }

    /**
     * Method adds random node with the given node's id to the current graph,
     * using the addNode utility, with a given location.
     * @implNote note that this method adds the given location.
     * @param node_id a given node's id.
     * @param set a given string representing set of nodes ("A" or "B").
     * @param location a given geo-location object.
     */
    public void addNode(int node_id, String set, GeoLocation location){
        NodeData randNode = random_vertex_generator(node_id, location);
        addNodeUtil(randNode, set);
    }

    /**
     * Utility method for adding node to a given set - represented as a string.
     * @param randNode a given random node.
     * @param set "A" for set A and "B" for set B.
     */
    public void addNodeUtil(NodeData randNode, String set){
        if (set.equals("A")) {
            disjointSet_A.add(randNode);
        }
        else if (set.equals("B")){
            disjointSet_B.add(randNode);
        }
        neighbors.put(randNode.getKey(), new HashMap<>());
        vertices.add(randNode);
        node_size++;
    }

    /**
     * Method connects the undirected edge where first vertex in src, and second is dest,
     * with the given weight.
     * @implNote connects without weight!
     * @param src given source id.
     * @param dest given destination id.
     * @param weight a given weight.
     */
    public void connect(int src, int dest, double weight){
        EdgeData e = random_edge_generator(src, dest);
        e.setWeight(weight);
        neighbors.get(src).put(dest, e);
        neighbors.get(dest).put(src, e);
        edges.add(e);
        edge_size++;
    }

    /**
     * Method connects the undirected edge where first vertex in src, and second is dest.
     * @implNote connects without weight!
     * @param src given source id.
     * @param dest given destination id.
     */
    public void connect(int src, int dest){
        EdgeData e = random_edge_generator(src, dest);
        neighbors.get(src).put(dest, e);
        neighbors.get(dest).put(src, e);
        edges.add(e);
        edge_size++;
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

    /**
     * Same but with a given geo-location.
     * @param key a given key.
     * @return the node's instance.
     */
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
     * Same but with a given geo-location.
     * @param key a given key.
     * @param location a given geo-location of the node.
     * @return the node's instance.
     */
    public NodeData random_vertex_generator(int key, GeoLocation location) {
        int tag = 1 + (int) (Math.random() * 10);
        double weight = 0; // for heights algorithms
        String info = "Key:" + key + "\n" + "Tag:" + tag + "\n" + "Weight" + weight + "\n" + "GeoLocation:" + location;
        return new NodeData(key, tag, info, weight, location);
    }

    /**
     * Creates the graph's edges in random.
     */
    public void random_edges_generator() {
        // Generate random edges for set A
        for (NodeData nodeData : disjointSet_A) {
            int randEdgesAmount = 5 + (int) (Math.random() * (disjointSet_B.size()/10));
            int[] distinct_nums = distinct_random_numbers(randEdgesAmount, disjointSet_B.size() - 1, disjointSet_B);
            for (int distinct_num : distinct_nums) {
                int src = nodeData.getKey();
                if (isEdgeInEdges(src, distinct_num)) {continue;}
                EdgeData randEdge = random_edge_generator(src, distinct_num);
                neighbors.get(src).put(distinct_num, randEdge);
                neighbors.get(distinct_num).put(src, randEdge);
                edges.add(randEdge);
                edge_size++;
            }
        }

        // Generate random edges for set B
        for (NodeData nodeData : disjointSet_B) {
            int randEdgesAmount = 5 + (int) (Math.random() * (disjointSet_A.size()/10));
            int[] distinct_nums = distinct_random_numbers(randEdgesAmount, disjointSet_A.size() - 1, disjointSet_A);
            for (int distinct_num : distinct_nums) {
                int src = nodeData.getKey();
                if (isEdgeInEdges(src, distinct_num)) {continue;}
                EdgeData randEdge = random_edge_generator(src, distinct_num);
                neighbors.get(src).put(distinct_num, randEdge);
                neighbors.get(distinct_num).put(src, randEdge);
                edges.add(randEdge);
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

    /**
     * @param v a given node's reference.
     * @return true iff a given node's reference is saturated, o.w., return false.
     */
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
     * @return the array list with those neighbors.
     */
    public ArrayList<EdgeData> edgesOut(int node_id){
        HashMap<Integer, EdgeData> N = neighbors.get(node_id);
        Collection<EdgeData> edges = N.values();
        return new ArrayList<>(edges);
    }

    /**
     * @param src a given source id.
     * @param dest a given destination id.
     * @return true iff the directed edge can be found in matches set,
     * which means that src and dest can be found in the matches set as an edge.
     */
    public boolean isDirectEdgeInMatches(int src, int dest){
        for (EdgeData currMatch : matches){
            if ((currMatch.getSrc() == src && currMatch.getDest() == dest)
                    || (currMatch.getSrc() == dest && currMatch.getDest() == src)){
                return true;
            }
        }
        return false;
    }

    /**
     * Method removes a directed edge in matches.
     * @param src a given source id.
     * @param dest a given destination id.
     */
    public void removeDirectEdgeInMatches(int src, int dest){
        matches.removeIf(currMatch ->
                   (currMatch.getSrc() == src && currMatch.getDest() == dest)
                || (currMatch.getSrc() == dest && currMatch.getDest() == src));
    }

    /**
     * @param v node's reference
     * @param group a given nodes set
     * @return true iff a given node's reference can be found in the given set.
     */
    public boolean isVertexInGroup(NodeData v, ArrayList<NodeData> group){
        for(NodeData v_A : group){
            if(v_A.equals(v)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param node_id node's id
     * @param group a given nodes set
     * @return true iff the given node with the node's id can be found in the given set.
     */
    public boolean isVertexIdInGroup(int node_id, ArrayList<NodeData> group){
        for(NodeData v : group){
            if(v.getKey() == node_id){
                return true;
            }
        }
        return false;
    }

    /**
     * @param src src node's id
     * @param dest dest node's id
     * @return true iff the given edge is in the current edges set.
     */
    public boolean isEdgeInEdges(int src, int dest){
        return isEdgeInSet(src, dest, edges);
    }

    /**
     * Checks if an undirected edge is in a certain set of the graph.
     * @param src first vertex
     * @param dest second vertex
     * @param set a given set of edges
     * @return true if an undirected edge is in a certain set of the graph, o.w., return false.
     */
    public boolean isEdgeInSet(int src, int dest, ArrayList<EdgeData> set){
        for (EdgeData e : set){
            if ((e.getSrc() == src && e.getDest() == dest) || (e.getSrc() == dest && e.getDest() == src)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param e a given edge
     * @return true iff the given edge in the matches list, o.w., return false.
     */
    public boolean isEdgeInMatch(EdgeData e){
        for (EdgeData match : matches){
            if (match.equals(e)){
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


