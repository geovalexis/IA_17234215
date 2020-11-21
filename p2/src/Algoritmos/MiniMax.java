package Algoritmos;

import Utils.Node;
import javafx.util.Pair;
import Juegos.*;

import java.util.ArrayList;

public class MiniMax {
    private final int maxDepth;
    private final short MAX=0;
    private final short MIN=1;
    private Joc joc;

    public MiniMax(int nivel_max, Joc joc){
        this.maxDepth = nivel_max;
        this.joc = joc;
    }

    public Pair<Integer, Node> findBest(Node current_node, int nivel){
        short current_player = checkPlayer(nivel);
        if (joc.isTerminal(current_node)) {
            return new Pair<Integer, Node>(current_player==this.MAX ? Integer.MAX_VALUE : Integer.MIN_VALUE, null);
        }
        else if (nivel==this.maxDepth) return new Pair<Integer, Node>(joc.calcularHeuristica(current_node), current_node);
        else {
            Node best = null;
            int maxmin_value = current_player==this.MAX ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            ArrayList<Node> sucesores = joc.nextMoves(current_node);
            for (Node succ: sucesores){
                Pair<Integer, Node> new_pair = findBest(succ, nivel+1);
                int value = new_pair.getKey();
                Node new_node = new_pair.getValue();
                if (current_player==MAX) {
                    //Find the maximum
                    if (value >= maxmin_value) {
                        maxmin_value = value;
                        best = new_node;
                    }
                } else { //MIN player
                    //Find the minimum
                    if (value <= maxmin_value){
                        maxmin_value = value;
                        best = new_node;
                    }
                }

            }
            return new Pair<Integer, Node>(maxmin_value, best);
        }

    }

    private short checkPlayer(int nivel) {
        if (nivel%2==0) return MAX;
        else return MIN;
    }
}
