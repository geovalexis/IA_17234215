import java.util.*;

public class BestFirst extends CercaInformada {

    public BestFirst(Integer[][] Matriz, Operator[] Operadores, HashSet forbiddenCells) {
        super(Matriz, Operadores, forbiddenCells);
    }

    public Collection<Tupla> setNewStructure(){
        return new ArrayList<Tupla>();
    }

    protected Comparator<Tupla> getComparator() {
        Comparator<Tupla> comp = new Comparator<Tupla>() {
            @Override
            public int compare(Tupla o1, Tupla o2) {
                float v1, v2;
                v1 =  o1.getValorHeuristico();
                v2 = o2.getValorHeuristico();
                return Float.compare(v1, v2);  //(o1.node.X * o1.node.Y) < o2.v3 ? -1 : o1.v3 > o2.v3 ? 1 : 0;
            }
        };
        return comp;
    }

    @Override
    public Tupla next_trip(Collection<Tupla> ListaPendientes) {
        return ((ArrayList<Tupla>) ListaPendientes).get(0); //The list is sorted so at the first position will always be the maximun element.
    }

    @Override
    public void add(Tupla trip, Collection<Tupla> ListaPendientes) {
        ListaPendientes.add(trip);
        Collections.sort((ArrayList) ListaPendientes, getComparator());
    }

    @Override
    public void delete_node(Tupla trip, Collection<Tupla> ListaPendientes) {
        ((ArrayList) ListaPendientes).remove(0);
    }


}
