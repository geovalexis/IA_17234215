package Utils;

public class Node{
    int X;
    int Y;
    int value;

    public Node(int X, int Y, int value){
        this.X = X;
        this.Y = Y;
        this.value = value;
    }

    public int getX() {return this.X;}

    public int getY() {return this.Y;}

    public int getValue() {return this.value;}

    @Override
    public boolean equals(Object object){
        boolean same = false;
        if (object != null && object instanceof Node) {
            same = (this.X == ((Node) object).getX()) && (this.Y == ((Node) object).getY())
                    && (this.value == ((Node) object).getValue());
        }
        return same;
    }

    @Override
    public int hashCode() {return this.X * this.Y * this.value;}

    public String toString() {
        return "(x: "+this.X+", y: "+this.Y+", value: "+this.value+")";
    }

}