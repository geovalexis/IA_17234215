package Algoritmos;

import org.javatuples.Pair;
import Juegos.*;

import java.util.ArrayList;

public class MiniMax {
    private final int maxDepth;
    private final short MAX;
    private final short MIN;
    private Joc joc;

    public MiniMax(Joc joc, short machine, short user, int nivel_max){
        this.joc = joc;
        this.MAX = machine;
        this.MIN = user;
        this.maxDepth = nivel_max;
    }

    public Pair<Integer, Node> findBest(Node current_node, int nivel){
        short current_player = checkPlayer(nivel);
        if (joc.isTerminal(current_node, current_player)) {
            return new Pair<Integer, Node>(current_player==this.MAX ? Integer.MAX_VALUE : Integer.MIN_VALUE, null);
        }
        else if (nivel==this.maxDepth) return new Pair<Integer, Node>(joc.calcularHeuristica(current_node), current_node);
        else {
            Node best = null;
            int maxmin_value = current_player==this.MAX ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            ArrayList<Node> sucesores = joc.nextMoves(current_node, current_player);
            for (Node succ: sucesores){
                Pair<Integer, Node> new_pair = findBest(succ, nivel+1);
                int value = new_pair.getValue0();
                Node new_node = new_pair.getValue1();
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
        if (nivel%2==0) return MAX; //Machine player always starts
        else return MIN;
    }
}
