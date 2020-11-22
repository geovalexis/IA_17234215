package Juegos;

import org.javatuples.Triplet;
import java.util.ArrayList;
import java.util.Iterator;


public class Damas implements Joc{
    private Node node;
    private final int user1=1; //The player at the top
    private final int user2=2; //The player at the bottom
    private final int empty_cell=0; //Black cells
    private final int forbid_cell=-1; //White cells
    private final int tokens_per_user =12;

    public Damas(int[][] board) {
        this.node = new DamasNode(board, tokens_per_user, tokens_per_user);
        
    }

    @Override
    public boolean isTerminal(Node current_node, int player){
        //VICTORIA SEGURA PARA PLAYER
        //NOTE: Es un nodo terminal si esta la ultima fila llena? Que pasa si no tenemos suficiente fichas? Que pasa
        // con las otras 4 fichas restantes?
        int opponent = player==user1 ? user2 : user1;
        boolean isTerminal=false;
        if (nextMoves(current_node, player).isEmpty() && nextMoves(current_node, opponent).isEmpty()){
            DamasNode damas_node = (DamasNode) current_node;
            Iterator<Integer> damas4player, damas4opponent;
            damas4player = getNumberOfDamasFor(damas_node, player);
            damas4opponent = getNumberOfDamasFor(damas_node, opponent);
            int total_damas4player=0;
            int total_damas4opponent=0;
            while (damas4player.hasNext() && damas4opponent.hasNext()){
                total_damas4player+=damas4player.next();
                total_damas4opponent+=damas4opponent.next();
                if (total_damas4player > total_damas4opponent){
                    isTerminal=true;
                    break;
                }
            }
        }
        return isTerminal;
    }

    public Iterator<Integer> getNumberOfDamasFor(DamasNode damas_node_, int player_){
        Triplet<Integer, Integer, Integer> board_limits = getBoardLimits(player_, damas_node_.getBoardSize());
        return new Iterator<Integer>() {
            int player=player_;
            DamasNode damas_node = damas_node_;
            int inc = board_limits.getValue0();
            int start = board_limits.getValue1();
            int end = board_limits.getValue2();
            int current_row=end;

            @Override
            public boolean hasNext() {
                //Habrá siguiente si no me he movido mas filas que el tamaño de la matriz.
                return Math.abs(current_row-end)<=Math.abs(end-start);
            }

            @Override
            public Integer next() {
                int nDamas = damas_node.getNumberOfCellsOf(current_row, player);
                current_row-=inc; //The increment is the opposite as we start from the end
                return nDamas;
            }
        };
    }

    @Override
    public int calcularHeuristica(Node node) {
        return 0;
    }

    @Override
    public ArrayList<Node> nextMoves(Node node, int player) {
        DamasNode damas_node = (DamasNode) node;
        Triplet<Integer, Integer, Integer> board_limits = this.getBoardLimits(player, damas_node.getBoardSize());
        return null;
    }

    public Triplet<Integer, Integer, Integer> getBoardLimits(int player, int board_size){
        // @return Triplet<Incremento, Start, End>
        if (player==user1){
            return new Triplet<Integer, Integer, Integer>(+1, 0, board_size-1);
        }
        else return new Triplet<Integer, Integer, Integer>( -1, board_size-1, 0);
    }
}

class DamasNode implements Node {
    private int[][] board;
    private int size;
    private int tokens_u1, tokens_u2;

    public DamasNode( int[][] board, int tokens_u1, int tokens_u2){
        this.board = board;
        this.size = board.length; //We assume it's square matrix
        this.tokens_u1 = tokens_u1;
        this.tokens_u2 = tokens_u2;
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int getCell(int row, int col){
        return this.board[row][col];
    }

    public int getNumberOfCellsOf(int row, int type){
        int ocurrences=0;
        for (int col=0; col< this.size; col++){
            if (this.board[row][col] == type) ocurrences++;
        }
        return ocurrences;
    }

    public int getBoardSize(){
        return this.size;
    }

    public void setBoard(int [] newBoard){
        //TODO
        //this.board = newBoard;
    }

    //TODO
    public boolean equals(Object object){
        return false;
    }

    //TODO
    public int hashCode(){
        return 0;
    }

    //TODO
    public String toString(){
        return "";
    }
}

