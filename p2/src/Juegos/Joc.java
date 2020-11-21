package Juegos;
import Utils.*;

import java.util.ArrayList;

public interface Joc {

    boolean isTerminal(Node node);

    int calcularHeuristica(Node node);

    ArrayList<Node> nextMoves(Node node);
}
