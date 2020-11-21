package Juegos;
import Utils.*;

import java.util.ArrayList;

public class Damas implements Joc{

    @Override
    public boolean isTerminal(Node node){
        return false;
    }

    @Override
    public int calcularHeuristica(Node node) {
        return 0;
    }

    @Override
    public ArrayList<Node> nextMoves(Node node) {
        return null;
    }
}
