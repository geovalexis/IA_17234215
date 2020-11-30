import Juegos.Damas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import Juegos.Joc;

public class Practica2 {


    public static void main(String[] args) {
        Integer[][] initial_board = readTSV("initial_board.tsv");
        //System.out.print(Arrays.deepToString(initial_board).replace("],", "],\n"));

        Damas damas_juego = new Damas(initial_board, Joc.MACHINEvsUSER, 4); // -1 means no limit
        damas_juego.play();

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
