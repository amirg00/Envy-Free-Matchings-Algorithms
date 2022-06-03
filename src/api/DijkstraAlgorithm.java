package api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Class explanation:
 *
 * This class supports the implementation of Dijkstra's algorithm to
 * find the shortest path from src vertex towards destination vertex.
 *
 * The implementation's structure applied is a priority queue of nodes
 * of the class NodeCompare, which supports a comparator of nodes weights
 * comparison.
 *
 * Time complexity: in terms of time complexity this implementation of a
 * priority queue by weights, guarantees time complexity of O(|V|+|E|*log(|V|)).
 *
 *
 */
public class DijkstraAlgorithm {

    private DirectedWeightedGraph g;
    private UndirectedBipartiteGraph bg;
    private HashMap<Integer,Double> dist;
    private HashMap<Integer, Integer> parent;
    private FibonacciHeap<Integer> dists;
    private ArrayList<NodeData> augmentPath;
    private int src;
    private final Double INF = Double.MAX_VALUE;

    public DijkstraAlgorithm(DirectedWeightedGraph g, UndirectedBipartiteGraph bg) {
        // Properties initialization //
        this.g = g;
        this.bg = bg;

        // Create an artificial vertex s
        double x = 35, y = 32, z = 0.0;
        NodeData s = new NodeData(g.nodeSize(), -1, "s", 0, new GeoLocation(x, y, z));
        g.addNode(s);

        // Connect zero edges from s to unsaturated vertices in A
        for (NodeData v : bg.getDisjointSet_A()){
            if (!bg.isVertexInMatches(v)){
                g.connect(s.getKey(), v.getKey(), 0);
            }
        }

        // Traverse from s to find min distances for
        // all reachable nodes from the artificial vertex s.
        src = s.getKey();

        // Clear graph's properties
        clear();

        // Run dijkstra's algorithm from s
        findMinDist();

        // Get current minimum augmenting path
        augmentPath = findMinAugmentPath();
        if (augmentPath != null) {
            System.out.println(augmentPath.size());
        }
        else {
            System.out.println("-1");
        }

        // Update edges weight at the end
        updateEdgesWeight();
    }
    public enum Tags{
        VISITED(1),
        UNVISITED(0);

        private final int value;

        Tags(int value) {this.value = value;}
    }

    public void clear(){
        // Properties initialization //
        this.dists = new FibonacciHeap<>();
        this.dist = new HashMap<>();
        this.parent = new HashMap<>();
        // Updates source vertex related properties
        setTags(Tags.UNVISITED.value);
        dist.put(src,0.0);
        dists.enqueue(src,0.0);
    }

    public boolean checkVisitedInMatchComplete(NodeData v, ArrayList<NodeData> group){
        return  v.getTag() == Tags.VISITED.value
                && !bg.isVertexInMatches(v)
                &&  bg.isVertexInGroup(v, group);
    }

    /**
     * Method updates edges' weight to the original weight minus
     * the potentials/height/weight/labels.
     */
    private void updateEdgesWeight(){
        for (EdgeData e : bg.getEdges()){
            int src = e.getSrc(), dest = e.getDest();
            NodeData srcNode = bg.getNode(src), destNode = bg.getNode(dest);
            e.setWeight(e.getWeight() - srcNode.getWeight() - destNode.getWeight());
        }
    }

    public ArrayList<NodeData> findMinAugmentPath(){
        int minNodeID = -1;
        double minDist = Double.MAX_VALUE;

        // Search for minimum augmenting path
        for (Integer node_id : dist.keySet()){
            if (node_id == bg.nodeSize()) {continue;}
            double currNodeDist = dist.get(node_id);
            NodeData currNode = bg.getNode(node_id);
            currNode.setWeight(bg.isVertexIdInGroup(node_id, bg.getDisjointSet_A()) ? (-1) * currNodeDist : currNodeDist);
            if (bg.isVertexInMatches(currNode)){continue;} // if matched then continue
            if (bg.isVertexIdInGroup(node_id, bg.getDisjointSet_A())) { continue;} // if in A then continue

            // vertex is an unsaturated vertex in B
            if (currNodeDist < minDist){
                minDist = currNodeDist;
                minNodeID = node_id;
            }
        }
        System.out.println("Answer: " + minNodeID);
        return getAugmentingPath(minNodeID);
    }

    public void init(){
        // Initialize all nodes' height to 0.
        Iterator<NodeData> nodes = g.nodeIter();
        while (nodes.hasNext()){
            //height.put(nodes.next().getKey(), 0);
        }

        // Negate edges' weight: Multiply edges' weight by -1.
        // Then after applying the minimum unbalanced problem,
        // by minimum hungarian method gives max. weighted matching.
        Iterator<EdgeData> edges = g.edgeIter();
        while (edges.hasNext()){
            EdgeData currEdge = edges.next();
            currEdge.setWeight(currEdge.getWeight() * (-1));
        }
    }

    /**
     * The method here visits all vertices to find the optimal path's cost, and return it.
     * The method starts with the source vertex, if it is the destination vertex, then it saves the pointer,
     * for the setOptimalPath method.
     *
     * In addition, if the current check node hasn't visited yet, then it adds the vertex's id
     * to the visited nodes list, and also check for his neighbours with the currentNeighbours,
     * to see which one of the neighbours is the optimal one (exactly as doing in greedy approach).
     *
     * @return the optimal path's cost from src to dest.
     */
    private void findMinDist() {
        Iterator<NodeData> nodes = g.nodeIter();
        while (nodes.hasNext()){
            NodeData curr = nodes.next();
            int curr_id = curr.getKey();
            if (curr.getKey() != src) {
                dist.put(curr_id, INF);
                dists.enqueue(curr_id, INF);
            }
            parent.put(curr_id,-1);
        }

        while (!dists.isEmpty()){
            System.out.println("1");
            int check = dists.dequeueMin().getValue();
            NodeData Check = g.getNode(check);
            Check.setTag(Tags.VISITED.value);

            Iterator<EdgeData> neighbours = g.edgeIter(check);
            while (neighbours.hasNext()){
                System.out.println("2");
                EdgeData curr = neighbours.next();
                int curr_id = curr.getDest();
                NodeData currN = g.getNode(curr_id);
                if (currN.getTag()!=Tags.VISITED.value) {
                    double newDist = dist.get(check) + curr.getWeight();
                    if (newDist < dist.get(curr_id)) {
                        dist.put(curr_id,newDist);
                        parent.put(curr_id,check);
                        dists.enqueue(curr_id, newDist);
                    }
                }
            }
        }
        System.out.println("3");
    }

    /**
     * The method sets the optimal path, using the parent property of each nodeCompare.
     * The method sort of going back to the linked parent of the node, until it gets
     * to the ancestor (root), and for each nodeCompare the method adds at the BEGINNING the NodeData
     * object which belongs to the current nodeCompare.
     *
     * @implNote At the end, we will get the objects list with the following order: {src, ..., dest}.
     */
    public ArrayList<NodeData> getAugmentingPath(int dest){
        if (dest == -1) {return new ArrayList<>();}

        ArrayList<NodeData> path = new ArrayList<>();
        int curr_parent = dest;

        while(curr_parent!=src){
            System.out.println(curr_parent);
            path.add(0,g.getNode(curr_parent));
            curr_parent = parent.get(curr_parent);
        }
        System.out.println("src: " + src);
        //path.add(0,g.getNode(src));
        return path;
    }

    /**
     * This method sets the tags.
     * @param value a given value
     */
    public void setTags(int value){
        Iterator<NodeData> nodes = g.nodeIter();
        while(nodes.hasNext()){nodes.next().setTag(value);}
    }
    /**
     * Prints all paths from src to a specific dest vertex.
     * @param parent the array of the parents nodes' id.
     * @param src the source index.
     * @param dest the destination index.
     */
    public void printDijkstraOptimalPath(int[] parent, int src, int dest){
        System.out.println("Dijkstra Algorithm: (With src-dest path)");
        System.out.print(" " + src + " --> " +  dest + ": distance = " + dist.get(dest) + "  Path : ");
        printOptimalPathUtil(parent, dest);
        System.out.println();
    }
    /**
     * Prints all paths from src to each of the other nodes.
     * @param parent the parent array of id's.
     * @param src the source index.
     */
    public void printDijkstraAllPaths(int[] parent, int src){
        System.out.println("Dijkstra Algorithm: (With src-dests paths)");
        for (int dest = 0; dest < dist.size(); dest++) {
            System.out.print(" " + src + " --> " + dest + ": distance = " + dist.get(dest) + "  Path : ");
            printOptimalPathUtil(parent, dest);
            System.out.println();
        }
    }
    /**
     * This utility helps with recursion to go back in the hierarchy, and print
     * by the original order the vertices' id.
     * @param parent the parent array of id's.
     * @param dest the destination index.
     */
    public void printOptimalPathUtil(int[] parent, int dest){
        if(parent[dest] == -1) { System.out.print(src + " "); return;}
        printOptimalPathUtil(parent, parent[dest]);
        System.out.print(dest + " ");
    }

    public ArrayList<NodeData> getAugmentPath() {return augmentPath;}
}
