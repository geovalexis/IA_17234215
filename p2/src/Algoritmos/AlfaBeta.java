package Algoritmos;

import Juegos.Joc;
import Juegos.Node;
import org.javatuples.Pair;

import java.util.ArrayList;

public class AlfaBeta extends SearchAlgorithm{

    public AlfaBeta(Joc joc, int machine, int user, int nivel_max){
        super(joc, machine, user, nivel_max);
    }

    public Pair<Integer, Node> findBest(Node current_node, int nivel, int alfa, int beta){
        int current_player = this.checkPlayer(nivel);
        if (joc.isTerminal(current_node, current_player)) {
            return new Pair<Integer, Node>(current_player==this.MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE, null);
        }
        else if (nivel==this.maxDepth) {
            return new Pair<Integer, Node>(joc.calcularHeuristica(current_node, current_player), null);
        }
        else {
            Node best = null;
            ArrayList<Node> sucesores = joc.nextMoves(current_node, current_player);
            if (alfa<beta) {
                for (Node succ : sucesores) {
                    Pair<Integer, Node> new_pair = findBest(succ, nivel + 1, alfa, beta);
                    int value = new_pair.getValue0();
                    if (current_player == MAX) {
                        //Find the maximum
                        if (value >= alfa) {
                            alfa = value;
                            best = succ;
                        }
                    } else { //MIN player
                        //Find the minimum
                        if (value <= beta) {
                            beta = value;
                            best = succ;
                        }
                    }

                }
            }
            if (current_player==MAX) return new Pair<Integer, Node>(alfa, best);
            else return new Pair<Integer, Node>(beta, best);
        }

    }

}
