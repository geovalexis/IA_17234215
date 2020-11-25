package Juegos;

import Algoritmos.MiniMax;
import Algoritmos.SearchAlgorithm;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Damas implements Joc{
    private Node node;
    private final int black_tokens=1; //The player at the top
    private final int white_tokens=2; //The player at the bottom
    private final int empty_cell=0; //Black cells
    private final int forbid_cell=-1; //White cells
    private final int tokens_per_user =12;
    private SearchAlgorithm black_player;
    private SearchAlgorithm white_player;

    public Damas(Integer[][] board, int game_mode, int max_depth) {
        this.node = new DamasNode(board, tokens_per_user, tokens_per_user);
        setGameMode(game_mode, max_depth);
    }

    public void play(){
        int rounds=1;
        int current_player;
        while (true) {
            current_player = rounds%2!=0 ? black_tokens : white_tokens;
            round(current_player);
            if (isTerminal(this.node, current_player)) break;
            rounds++;
        }
        System.out.printf("Player %s! Congratulations!", (current_player == black_tokens) ? "black" : "white");
    }

    public void round(int player){
        SearchAlgorithm search_alg = player==black_tokens ? black_player : white_player;
        if (search_alg==null) roundMachine(search_alg);
        else roundUser(player);
    }

    public void roundMachine(SearchAlgorithm search_alg){
        Pair<Integer, Node> best_play = search_alg.findBest(this.node, 0);
        this.node = best_play.getValue1();
    }

    public void roundUser(int player){
        System.out.print(this.node.toString());
        Scanner input = new Scanner(System.in);
        while (true){
            System.out.printf("Please, select a cell you would like to move to (Ex. B6). You have tokens: %d", player);
            String cell = input.next();
            if (cell.matches("\\w\\d")) break;
            System.out.print("You must follow the following format COLUMN|ROW (Examples: A2, C6, E4... ");
        }

    }

    public void setGameMode(int gameMode, int max_depth){
        switch (gameMode)
        {
            case MACHINEvsUSER:
                black_player=new MiniMax(this, black_tokens, white_tokens, max_depth);
                white_player=null;
                break;
            case MACHINEvsMACHINE:
                black_player=new MiniMax(this, black_tokens, white_tokens, max_depth);
                white_player=new MiniMax(this, white_tokens, black_tokens, max_depth);
                break;
            case USERvsMACHINE:
                black_player=null;
                white_player=new MiniMax(this, white_tokens, black_tokens, max_depth);
                break;
            case USERvsUSER:
                //throw new ExecutionControl.NotImplementedException("Functionality still unavailable");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gameMode);
        }
    }

    @Override
    public boolean isTerminal(Node current_node, int player){
        //VICTORIA SEGURA PARA PLAYER
        //NOTE: Es un nodo terminal si esta la ultima fila llena? Que pasa si no tenemos suficiente fichas? Que pasa
        // con las otras 4 fichas restantes?
        int opponent = player== black_tokens ? white_tokens : black_tokens;
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
                return Math.abs(current_row-end)<=Math.abs(start-end);
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
    public ArrayList<Node> nextMoves(Node node_, int player) {
        DamasNode damas_node = (DamasNode) node_;
        ArrayList<Node> nextNodes=new ArrayList<Node>();
        Triplet<Integer, Integer, Integer> board_limits = this.getBoardLimits(player, damas_node.getBoardSize());
        int inc = board_limits.getValue0();
        int start = board_limits.getValue1();
        int end = board_limits.getValue2();
        int opponent = player== black_tokens ? white_tokens : black_tokens;
        for (int row=start; row <= end; row+=inc){
            int col_start= (row%2==0) ? 1 : 0;  //So that we only look in black cells
            for (int col=col_start; col<damas_node.getBoardSize(); col+=2){
                if (damas_node.getCell(row, col)==player) {
                    checkAndMove(row, col, row + inc, col + 1, damas_node, nextNodes); //Mirar en la esquina superior derecha
                    checkAndMove(row, col, row + inc, col - 1, damas_node, nextNodes); //Mirar en la esquina superior izquierda
                }
                else if (damas_node.getCell(row, col)==opponent) {
                    // Mirar si en la esquina inferior izquierda hay alguna ficha del jugador actual que pueda saltar y comerse al oponente:
                    checkMoveAndDestroy(row-inc, col-1, row+inc,  col+1, damas_node, nextNodes, player);
                    // Mirar si en la esquina inferior derecha hay alguna ficha del jugador actual que pueda saltar y comerse al oponente:
                    checkMoveAndDestroy(row-inc, col+1, row+inc, col-1, damas_node, nextNodes, player);
                }
            }
        }
        return nextNodes;
    }

    public int checkAndMove(int current_row, int current_col, int dest_row, int dest_col, DamasNode current_node, ArrayList<Node> nextNodes){
        int exit_code=0;
        if (!current_node.isOutOfBound(dest_row, dest_col) && current_node.getCell(dest_row, dest_col)==this.empty_cell){
            DamasNode nextNode = current_node.clone();
            nextNode.moveToken(current_row, current_col, dest_row, dest_col);
            nextNodes.add(nextNode);
            exit_code=1;
        }
        return exit_code;
    }

    public void checkMoveAndDestroy(int orig_row, int orig_col, int dest_row, int dest_col, DamasNode current_node, ArrayList<Node> nextNodes, int current_player) {
        if (!current_node.isOutOfBound(orig_row, orig_col) && current_node.getCell(dest_row, dest_col) == current_player) {
            if (checkAndMove(orig_row, orig_col, dest_row, dest_col, current_node, nextNodes) == 1) { //Significa que se ha movido la ficha
                DamasNode nextNode = (DamasNode) nextNodes.get(nextNodes.size() - 1);
                while (orig_row < dest_row && orig_col < dest_col) { //Solo funciona con movimientos en diagonal
                    nextNode.setCell(orig_row, orig_col, this.empty_cell); //Eliminar todas las fichas del contrario que queden de por medio
                }
            }
        }
    }

    public Triplet<Integer, Integer, Integer> getBoardLimits(int player, int board_size){
        // @return Triplet<Incremento, Start, End>
        if (player== black_tokens){
            return new Triplet<Integer, Integer, Integer>(+1, 0, board_size-1);
        }
        else return new Triplet<Integer, Integer, Integer>( -1, board_size-1, 0);
    }
}

class DamasNode implements Node {
    private Integer[][] board;
    private int size;
    private int tokens_u1, tokens_u2;

    public DamasNode( Integer[][] board, int tokens_u1, int tokens_u2){
        this.board = board;
        this.size = board.length; //We assume it's square matrix
        this.tokens_u1 = tokens_u1;
        this.tokens_u2 = tokens_u2;
    }

    public Integer[][] getBoard(){
        return this.board;
    }

    public void increaseToken(int token) {
        if (this.tokens_u1==token) this.tokens_u1++;
        else this.tokens_u2++;
    }

    public void decreaseToken(int token) {
        if (this.tokens_u1==token) this.tokens_u1--;
        else this.tokens_u2--;
    }

    public void moveToken(int old_row, int old_col, int new_row, int new_col){
        int aux = this.board[new_row][new_col];
        this.board[new_row][new_col] = this.board[old_row][old_col];
        this.board[old_row][old_col] = aux;
    }


    public int getCell(int row, int col){
        return this.board[row][col];
    }

    public void setCell(int row, int col, int value) {
        if (value==this.tokens_u1 || value==this.tokens_u2) increaseToken(value);
        if (this.board[row][col]==this.tokens_u1 || this.board[row][col]==this.tokens_u2) decreaseToken(value);
        this.board[row][col]=value;
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

    public boolean isOutOfBound(int row, int col){
        return isOutOfBound(row) || isOutOfBound(col);
    }

    public boolean isOutOfBound(int row_or_col){
        //We can do this because we assumed that the board is always SQUARE SHAPED.
        return (row_or_col<0) || (row_or_col>=this.size);
    }

    public void setBoard(int [] newBoard){
        //TODO
        //this.board = newBoard;
    }

    public DamasNode clone(){
        Integer [][] board_copy = new Integer[this.size][];
        for (int i =0; i < this.size; i++){
            board_copy[i] = board_copy[i].clone();
        }
        return new DamasNode(board_copy, this.tokens_u1, this.tokens_u2);
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
        StringBuilder builder = new StringBuilder();
        builder.append("\t");
        for (char c=(char) 65; c <=(char) 65+this.size; c++) builder.append(c+"   ");
        builder.append("\n");
        int cont=1;
        for (Integer[] row: this.board){
            builder.append(cont+"\t");
            for (int col: row) builder.append(col+"   ");
            builder.append("\n");
            cont++;
        }
        return builder.toString()+"Current black tokens: "+this.tokens_u1+"\n"+
                "Current white tokens: "+this.tokens_u2+"\n";
    }
}

