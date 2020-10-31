import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Practica1 {

    public static void main(String[] args) {
        Integer[][] map = readTSV("matriz_1.tsv");
        //System.out.print(Arrays.deepToString(map));
        //map.forEach(array -> System.out.println(Arrays.toString(array)));
        Operator[] ops = new Operator[4];
        ops[0] = new Operator(1,0,"abajo");
        ops[1] = new Operator(-1,0,"arriba");
        ops[2] = new Operator(0,-1,"izquierda");
        ops[3] = new Operator(0,1,"derecha");

        HashSet<Integer> forbiddenCells = new HashSet<>() {{add(-1);}};
        BestFirst bf = new BestFirst(map, ops, forbiddenCells);
        ArrayList<Nodo> camino_solucion = bf.buscarNodo(new Nodo(0,0,map[0][0]), new Nodo(9,9,map[9][9]));

        if (camino_solucion != null){
            camino_solucion.forEach(Nodo -> System.out.println(Nodo.toString()));
        }
        else{
            System.out.print("No se ha podido encontrar la solución");
        }
    }

    public static Integer[][] readTSV(String filename) {
        File fileObject = new File(filename);
        StringTokenizer lineItems;
        ArrayList<Integer[]> data = new ArrayList<>(); //initializing a new ArrayList out of String[]'s
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(fileObject))) {
            String line;
            while ((line = TSVReader.readLine()) != null) {
                lineItems = new StringTokenizer(line, "\t");
                Integer[] n = new Integer[lineItems.countTokens()];
                for (int i = 0; i < n.length; i++) {
                    n[i] = Integer.valueOf(lineItems.nextToken());
                }
                //String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
                data.add(n); //adding the splitted line array to the ArrayList
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        Integer[][] list2D = new Integer[data.size()][data.get(0).length];
        for (int i = 0; i < data.size(); i++) {
            list2D[i] = data.get(i);
        }
        return list2D;
    }
}
