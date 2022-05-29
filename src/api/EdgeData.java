package api;

public class EdgeData {
    private int src, dest, tag;
    private double weight;
    private String info;

    public EdgeData(int src, int dest, int tag, double weight, String info){
        try {
            if (weight >= 0){
                this.weight = weight;
            }
            else{
                throw new Exception("ERROR: Weight got is a negative number");
            }
        }
        catch (Exception c){
            c.printStackTrace();
        }
        this.src = src;
        this.dest = dest;
        this.tag = tag;
        this.info = info;
    }
    // A copy constructor.
    public EdgeData(EdgeData other){this(other.getSrc(), other.getDest(), other.getTag(), other.getWeight(), other.getInfo());}

    public int getSrc() {
        return this.src;
    }

    public int getDest() {
        return this.dest;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String s) {
        this.info = s;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int t) {
        this.tag = t;
    }


    // equals method for edges
    public boolean equals(EdgeData other){
        return this.src == other.src && this.dest == other.dest;
    }

    @Override
    public String toString(){
        return "src: " + src + ", " + "dest: " + dest;
    }
}
