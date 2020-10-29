import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public abstract class CercaInformada {

    ArrayList<Integer> Matriz;

    public CercaInformada(ArrayList<Integer> Matriz){
        this.Matriz = Matriz;
    }

    public static void main(String[] args) {
        ArrayList<String[]> map = readTSV("matriz_1.tsv");
        map.forEach(array -> System.out.println(Arrays.toString(array)));
        //System.out.print("WTF");
    }

    public HashSet<Nodo> CercaInformada(Nodo nodo_inicial, Nodo nodo_final){
        Set<Triplete> ListaPendientes = new TreeSet<>(getComparator());
        HashSet<Nodo> ListaTratados = new HashSet<>();

        Triplete inicio = new Triplete(nodo_inicial, ListaTratados, calcular_heuristica(nodo_inicial));
        ListaPendientes.add(inicio);
        HashSet<Nodo> solucion = null;
        boolean found=false;
        while (!found && (ListaPendientes.retainAll(ListaTratados) ) ) {
            Triplete current_trip = next_trip(ListaPendientes);
            Nodo current_node = current_trip.getEstado();
            HashSet<Nodo> current_camino = current_trip.getCamino();
            int current_valor_heur = current_trip.getValorHeuristico();
            delete_node(current_trip, ListaPendientes);

            if (current_node == nodo_final){
                found=true;
                solucion = current_camino;
            }
            else {
                ArrayList<Nodo> sucesores = getSucesores(current_node);
                for(Nodo succ: sucesores){
                    if (!ListaTratados.contains(succ) && !ListaPendientes.contains(succ)){
                        HashSet<Nodo> new_camino = current_camino;
                        new_camino.add(succ);
                        add(new Triplete(succ, new_camino, calcular_heuristica(succ)), ListaPendientes);
                    }
                }
                ListaTratados.add(current_node);
            }
        }

        return solucion;
    }

    protected abstract Comparator<Object> getComparator();

    public void buscar(){

    }

    public abstract Triplete next_trip(Set<Triplete> ListaPendientes);

    public abstract int calcular_heuristica(Nodo node);

    public abstract void add(Triplete trip, Set<Triplete> ListaPendientes);

    public abstract void delete_node(Triplete trip, Set<Triplete> ListaPendientes);

    public ArrayList<Nodo> getSucesores(Nodo nodo_actual){

        return null;
    }


    public static ArrayList<String[]> readTSV(String filename) {
        File fileObject = new File(filename);
        ArrayList<String[]> Data = new ArrayList<>(); //initializing a new ArrayList out of String[]'s
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(fileObject))) {
            String line;
            while ((line = TSVReader.readLine()) != null) {
                String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
                Data.add(lineItems); //adding the splitted line array to the ArrayList
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        return Data;
    }
}

class Triplete{
    Nodo node;
    HashSet<Nodo> camino;
    int valorHeuristico;

    public Triplete(Nodo estado, HashSet<Nodo> camino, int valorHeuristico){
        this.node = estado;
        this.camino = camino;
        this.valorHeuristico = valorHeuristico;
    }

    public Nodo getEstado(){
        return this.node;
    }

    public HashSet<Nodo> getCamino(){
        return this.camino;
    }

    public int getValorHeuristico(){
        return this.valorHeuristico;
    }

}

class Nodo{
    int X;
    int Y;

   public Nodo(int X, int Y){
        this.X = X;
        this.Y = Y;
    }
}
