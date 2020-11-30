package Algoritmos;

import org.javatuples.Pair;
import Juegos.*;

import java.util.ArrayList;

public class MiniMax extends SearchAlgorithm{

    public MiniMax(Joc joc, int machine, int user, int nivel_max){
        super(joc, machine, user, nivel_max);
    }

    public Pair<Integer, Node> findBest(Node current_node, int nivel){
        int current_player = this.checkPlayer(nivel);
        ArrayList<Node> sucesores = joc.nextMoves(current_node, current_player);
        if (sucesores.isEmpty()) {
            return new Pair<Integer, Node>(current_player==this.MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE, null);
        }
        else if (nivel==this.maxDepth) return new Pair<Integer, Node>(joc.calcularHeuristica(current_node), null);
        else {
            Node best = null;
            int maxmin_value = current_player==this.MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            //ArrayList<Node> sucesores = joc.nextMoves(current_node, current_player);
            for (Node succ: sucesores){
                Pair<Integer, Node> new_pair = findBest(succ, nivel+1);
                int value = new_pair.getValue0();
                if (current_player==MAX) {
                    //Find the maximum
                    if (value >= maxmin_value) {
                        maxmin_value = value;
                        best = succ;
                    }
                } else { //MIN player
                    //Find the minimum
                    if (value <= maxmin_value){
                        maxmin_value = value;
                        best = succ;
                    }
                }

            }
            return new Pair<Integer, Node>(maxmin_value, best);
        }

    }

}
