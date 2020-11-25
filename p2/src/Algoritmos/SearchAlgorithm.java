package Algoritmos;

import Juegos.Joc;
import Juegos.Node;
import org.javatuples.Pair;

public abstract class SearchAlgorithm {
    public final int maxDepth;
    public final int MAX;
    public final int MIN;
    public Joc joc;

    public SearchAlgorithm(Joc joc, int machine, int user, int nivel_max){
        this.joc = joc;
        this.MAX = machine;
        this.MIN = user;
        this.maxDepth = nivel_max;
    }

    public abstract Pair<Integer, Node> findBest(Node current_node, int nivel);

    public int checkPlayer(int nivel) {
        if (nivel%2==0) return MAX; //Machine player always starts
        else return MIN;
    }

}
