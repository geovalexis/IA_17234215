package Juegos;

import java.util.ArrayList;

public interface Joc {
    int MACHINEvsUSER=1;
    int MACHINEvsMACHINE=2;
    int USERvsMACHINE=3;
    int USERvsUSER=4; //NOT IMPLEMENTED

    int MINIMAX=0;
    int ALFABETA=1;

    int HEURISTIC_V1=1;
    int HEURISTIC_V2=2;
    int HEURISTIC_V3=3;

    boolean isTerminal(Node node, int player);

    int calcularHeuristica(Node node, int player_id);

    ArrayList<Node> nextMoves(Node node, int player);
}

