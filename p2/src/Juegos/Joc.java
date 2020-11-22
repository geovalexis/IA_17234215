package Juegos;

import java.util.ArrayList;

public interface Joc {

    boolean isTerminal(Node node, int player);

    int calcularHeuristica(Node node);

    ArrayList<Node> nextMoves(Node node, int player);
}

