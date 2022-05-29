package api;

public class NodeData{

    private GeoLocation Node;
    private final int key;
    private int tag;
    private double weight;
    private String info;

    public NodeData(int key, int tag, String info, double weight, GeoLocation Node) {
        this.key = key;
        this.tag = tag;
        this.info = info;
        this.weight = weight;
        this.Node = Node;
    }
    // A copy constructor
    public NodeData(NodeData other){ this(other.getKey(), other.getTag(), other.getInfo(), other.getWeight(), other.getLocation());}

    public int getKey() {
        return key;
    }

    public GeoLocation getLocation() {
        return Node;
    }

    public void setLocation(GeoLocation p) {this.Node = new GeoLocation(p);}

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String s) {this.info = s;}

    public int getTag() {
        return this.tag;
    }

    public void setTag(int t) {
        this.tag = t;
    }

    // equals method
    public boolean equals(NodeData other){
        return this.key == other.key;
    }

    @Override
    public String toString(){
        return "key: " + this.key;
    }

}
