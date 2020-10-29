import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public class BestFirst extends CercaInformada {

    public BestFirst(ArrayList<Integer> Matriz) {
        super(Matriz);
    }

    public static void main(String[] args) {
        ArrayList<String[]> map = readTSV("matriz_1.tsv");
        map.forEach(array -> System.out.println(Arrays.toString(array)));
        //System.out.print("WTF");
    }

    @Override
    protected Comparator<Object> getComparator() {
        Comparator<Triplete> sortOn3rdValue = new Comparator<Triplete>() {
            @Override
            public int compare(Triplete o1, Triplete o2) {
                Integer v1, v2;
                v1 =  o1.valorHeuristico;
                v2 = o2.valorHeuristico;
                return v1.compareTo(v2);  //(o1.node.X * o1.node.Y) < o2.v3 ? -1 : o1.v3 > o2.v3 ? 1 : 0;
            }
        };
        return null;
    }

    @Override
    public Triplete next_trip(Set<Triplete> ListaPendientes) {
        return null;
    }

    @Override
    public int calcular_heuristica(Nodo nodo) {
        return 0;
    }

    @Override
    public void add(Triplete trip, Set<Triplete> ListaPendientes) {

    }

    @Override
    public void delete_node(Triplete trip, Set<Triplete> ListaPendientes) {

    }


}
