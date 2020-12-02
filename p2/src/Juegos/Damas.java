package Juegos;

import Algoritmos.AlfaBeta;
import Algoritmos.MiniMax;
import Algoritmos.SearchAlgorithm;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;


public class Damas implements Joc{
    private DamasNode node;
    private final int black_tokens=1; //The player at the top
    private final int white_tokens=2; //The player at the bottom
    private final int empty_cell=0; //Black cells
    private final int forbid_cell=-1; //White cells
    private final int tokens_per_user =12;
    private SearchAlgorithm black_player;
    private SearchAlgorithm white_player;

    //Este constructor solo es válido para generar una clase hija con un método calcularHeuristica modificado.
    public Damas(DamasNode initial_node){
        this.node = initial_node;
    }

    public Damas(Integer[][] board, int game_mode, int searchMode_m1, int searchMode_m2, int max_depth) {
        this.node = new DamasNode(board, tokens_per_user, tokens_per_user, black_tokens, white_tokens);
        setGameMode(game_mode, searchMode_m1, searchMode_m2, max_depth,0,0);
    }

    //Manual configurable mode
    public Damas(Integer[][] board, int game_mode, int searchMode_m1, int searchMode_m2, int heuristicMode_m1, int heuristicMode_m2, int max_depth){
        this.node = new DamasNode(board, tokens_per_user, tokens_per_user, black_tokens, white_tokens);
        setGameMode(game_mode, searchMode_m1, searchMode_m2, max_depth,heuristicMode_m1,heuristicMode_m2);
    }


    public void setGameMode(int gameMode, int searchMode_m1, int searchMode_m2, int max_depth, int heuristicMode_m1, int heuristicMode_m2){
        Damas damas_heuristic_m1=this, damas_heuristic_m2=this;
        if (heuristicMode_m1!=0) damas_heuristic_m1=heuriscticFactory(this.node.clone(), heuristicMode_m1);
        if (heuristicMode_m2!=0) damas_heuristic_m2=heuriscticFactory(this.node.clone(), heuristicMode_m2);
        switch (gameMode)
        {
            case MACHINEvsUSER:
                black_player= searchMode_m1==MINIMAX ? new MiniMax(damas_heuristic_m1, black_tokens, white_tokens, max_depth) : new AlfaBeta(damas_heuristic_m1, black_tokens, white_tokens, max_depth);
                white_player=null;
                break;
            case MACHINEvsMACHINE:
                black_player=searchMode_m1==MINIMAX ? new MiniMax(damas_heuristic_m1, black_tokens, white_tokens, max_depth) : new AlfaBeta(damas_heuristic_m1, black_tokens, white_tokens, max_depth);
                white_player=searchMode_m2==MINIMAX ? new MiniMax(damas_heuristic_m2, white_tokens, black_tokens, max_depth) : new AlfaBeta(damas_heuristic_m2, white_tokens, black_tokens, max_depth);
                break;
            case USERvsMACHINE:
                black_player=null;
                white_player=searchMode_m2==MINIMAX ? new MiniMax(damas_heuristic_m2, white_tokens, black_tokens, max_depth) : new AlfaBeta(damas_heuristic_m2, white_tokens, black_tokens, max_depth);
                break;
            case USERvsUSER:
                //throw new ExecutionControl.NotImplementedException("Functionality still unavailable");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gameMode);
        }
    }

    public Damas heuriscticFactory(DamasNode initial_node, int heuristic_version){
        Damas damas_with_heuristic=null;
        switch(heuristic_version){
            case HEURISTIC_V1:
                damas_with_heuristic = new DamasH1(initial_node);
                break;
            case HEURISTIC_V2:
                damas_with_heuristic = new DamasH2(initial_node);
                break;
            case HEURISTIC_V3:
                damas_with_heuristic = new DamasH3(initial_node);
                break;
        }
        return  damas_with_heuristic;
    }

    public void play(){
        int rounds=1;
        int current_player;
        while (true) {
            System.out.printf("\nRONDA %d: ", rounds);
            current_player = rounds%2!=0 ? black_tokens : white_tokens;
            round(current_player);
            if (isTerminal(this.node, current_player)) break;
            rounds++;
        }
        System.out.printf("\nGAME OVER! El jugador %d ha ganado! Felicidades!\n", current_player);
    }

    public void round(int player){
        SearchAlgorithm search_alg = player==black_tokens ? black_player : white_player;
        if (search_alg!=null) roundMachine(player, search_alg);
        else roundUser(player);
    }

    public void roundMachine(int player, SearchAlgorithm search_alg){
        System.out.printf("TURNO DE LA MAQUINA (%d)", player);
        Pair<Integer, Node> best_play = search_alg.findBest(this.node, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        DamasNode temp = (DamasNode) best_play.getValue1();
        if (temp==null){
            System.out.print("\nWARNING! No se ha podido mover ficha. Es posible que el jugador se encuentre bloqueado.\n");
        }
        else {
            this.node=temp;
            System.out.print(this.node.toString());
        }
    }

    public void roundUser(int player){
        System.out.printf("TURNO DEL HUMANO (%d)", player);
        Scanner input = new Scanner(System.in);
        int orig_col, orig_row;
        while (true) {
            System.out.print(this.node.toString());
            System.out.print("Por favor, introduzca la posición de la ficha que está interesado en mover (Ex. C4, B6...): ");
            String orig_cell = input.next();
            if (orig_cell.matches("\\w\\d")) {
                orig_col = (int) orig_cell.toUpperCase().charAt(0) - (int) 'A';
                orig_row = Character.getNumericValue(orig_cell.charAt(1))  - 1 ;
                if (!this.node.isOutOfBound(orig_row, orig_col)) break;
            }
            System.out.print("\nERROR!! Debe de seguir el siguiente formato ColumnaFila, sin espacios ni guiones (Ejemplos: A2, C6, E4...) \n");
        }

        int dest_col, dest_row;
        while (true) {
            System.out.printf("Por favor, seleccione la celda a la que le gustaría moverse: ");
            String dest_cell = input.next();
            if (dest_cell.matches("\\w\\d")) {
                dest_col = (int) dest_cell.toUpperCase().charAt(0) - (int) 'A';
                dest_row = Character.getNumericValue(dest_cell.charAt(1))  - 1 ;
                if (!this.node.isOutOfBound(dest_row, dest_col)) break;
            }
            System.out.print("\nERROR!!Debe de seguir el siguiente formato ColumnaFila, sin espacios ni guiones (Ejemplos: A2, C6, E4...) \n");
        }

        DamasNode temp = checkMoveAndDestroy(orig_row, orig_col, dest_row, dest_col, this.node, player);
        if (temp == null) {
            System.out.print("\nWARNING! No se ha podido mover la ficha. Por favor, inténtelo de nuevo más tarde.\n");
            //roundUser(player);
        }
        else{
            this.node=temp;
            System.out.print(this.node.toString());
        }

    }


    @Override
    public boolean isTerminal(Node current_node, int player){
        //DEVUELVE TRUE SI VICTORIA SEGURA PARA PLAYER
        int opponent = player== black_tokens ? white_tokens : black_tokens;
        boolean isTerminal=false;
        if ((((DamasNode) current_node).getNumberOfTokens(player)==0 || ((DamasNode) current_node).getNumberOfTokens(opponent)==0) || (nextMoves(current_node, player).isEmpty() && nextMoves(current_node, opponent).isEmpty())){
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
        Triplet<Integer, Integer, Integer> board_limits = getBoardBorders(player_, damas_node_.getBoardSize());
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
    public int calcularHeuristica(Node node, int player_id) {
        //return calcularHeuristicaV1((DamasNode) node, player_id);
        //return calcularHeuristicaV2((DamasNode) node, player_id);
        return calcularHeuristicaV3((DamasNode) node, player_id); //DEFAULT HEURISTIC
    }

    public int calcularHeuristicaV1(DamasNode node, int player_id){
        //Favorecer aquellos nodos en los que haya más damas
        Triplet<Integer, Integer, Integer> board_limits = this.getBoardBorders(player_id, node.getBoardSize());
        int end = board_limits.getValue2();
        return node.getNumberOfCellsOf(end, player_id);
    }

    public int calcularHeuristicaV2(DamasNode node, int player_id){
        //Favorecer la eliminación de fichas del contrario -> aquellos que tengan mas diferencia de fichas y en más oponentes alrededor se verán favorecidos.
        int opponent = player_id== black_tokens ? white_tokens : black_tokens;
        int delta_nTokens=node.getNumberOfTokens(player_id)-node.getNumberOfTokens(opponent);
        int opponent_tokens=0;
        for (int row=0; row < node.getBoardSize(); row++){
            for (int col=0; col < node.getBoardSize(); col++){
                if (node.getCell(row, col) == player_id){
                    //Mirar alrededores
                    if (!node.isOutOfBound(row+1, col+1) &&  node.getCell(row+1, col+1)==opponent) opponent_tokens++; //Esquina inferior derecha
                    if (!node.isOutOfBound(row+1, col-1) &&  node.getCell(row+1, col-1)==opponent) opponent_tokens++; //Esquina inferior izquierda
                    if (!node.isOutOfBound(row-1, col+1) &&  node.getCell(row-1, col+1)==opponent) opponent_tokens++; //Esquina superior derecha
                    if (!node.isOutOfBound(row-1, col-1) &&  node.getCell(row-1, col-1)==opponent) opponent_tokens++; //Esquina superior izquierda
                }
            }
        }
        return (int) Math.pow(delta_nTokens, 3)+opponent_tokens; //Es mucho mas importante que el jugador tenga más fichas a que este mas cerca de contrarios
    }


    public int calcularHeuristicaV3(DamasNode node, int player_id) {
        //Favorecer similitud con un tablero "ideal" utilizando alguna distancia métrica (Hamming distance en este caso)
        int hamming_distance=0;
        Integer[][] currentBoard = node.getBoard();
        Integer[][] goalBoard = createGoalBoard(node, player_id);
        for (int row=0; row < node.getBoardSize(); row++){
            for (int col=0; col < node.getBoardSize(); col++){
                if (currentBoard[row][col] != goalBoard[row][col]){
                    hamming_distance++;
                }
            }
        }
        return -hamming_distance; //Alta distancia de Hamming significa baja similitud, y en este caso queremos lo contrario, por eso el negativo.
    }

    public Integer[][] createGoalBoard(DamasNode node, int player_id){
        //Crea un tablero "ideal" en el cual todas las fichas del jugador actual estan en el lado contrario y todo el resto son celdas vacías.
        Integer[][] goalBoard = new Integer[node.getBoardSize()][node.getBoardSize()];
        for (Integer[] row: goalBoard) Arrays.fill(row, this.empty_cell); //it is not neccesary in this case as empty_cell is also 0 but it's nice to know.
        Triplet<Integer, Integer, Integer> board_limits = this.getBoardBorders(player_id, node.getBoardSize());
        int end = board_limits.getValue2();
        int row=end;
        int n=this.tokens_per_user;
        while (n > 0){
            int col_start = (row % 2 == 0) ? 1 : 0;  //So that we only look in black cells
            for (int col=col_start; col < node.getBoardSize(); col+=2){
                goalBoard[row][col]=player_id;
                n--;
            }
            row = Math.abs(row-1);
        }
        return goalBoard;
    }

    @Override
    public ArrayList<Node> nextMoves(Node node_, int player) {
        DamasNode damas_node = (DamasNode) node_;
        ArrayList<Node> nextNodes=new ArrayList<Node>();
        Triplet<Integer, Integer, Integer> board_limits = this.getBoardBorders(player, damas_node.getBoardSize());
        int inc = board_limits.getValue0();
        int start = board_limits.getValue1();
        int end = board_limits.getValue2();
        int opponent = player== black_tokens ? white_tokens : black_tokens;
        DamasNode nextNode;
        for (int row=start; Math.abs(start-row)<=Math.abs(start-end); row+=inc){
            int col_start= (row%2==0) ? 1 : 0;  //So that we only look in black cells
            for (int col=col_start; col<damas_node.getBoardSize(); col+=2){
                if (damas_node.getCell(row, col)==player) {
                    nextNode = checkAndMove(row, col, row + inc, col + 1, damas_node); //Mirar en la esquina superior derecha
                    if (nextNode != null) nextNodes.add(nextNode);
                    nextNode = checkAndMove(row, col, row + inc, col - 1, damas_node); //Mirar en la esquina superior izquierda
                    if (nextNode != null) nextNodes.add(nextNode);
                }
                else if (damas_node.getCell(row, col)==opponent) {
                    // Mirar si en la esquina inferior izquierda hay alguna ficha del jugador actual que pueda saltar y comerse al oponente:
                    nextNode = checkMoveAndDestroy(row-inc, col-1, row+inc,  col+1, damas_node, player);
                    if (nextNode != null) nextNodes.add(nextNode);
                    // Mirar si en la esquina inferior derecha hay alguna ficha del jugador actual que pueda saltar y comerse al oponente:
                    nextNode = checkMoveAndDestroy(row-inc, col+1, row+inc, col-1, damas_node, player);
                    if (nextNode != null) nextNodes.add(nextNode);
                }
            }
        }
        return nextNodes;
    }

    public DamasNode checkAndMove(int current_row, int current_col, int dest_row, int dest_col, DamasNode current_node){
        if (!current_node.isOutOfBound(dest_row, dest_col) && current_node.getCell(dest_row, dest_col)==this.empty_cell){
            DamasNode restNode = current_node.clone();
            restNode.moveToken(current_row, current_col, dest_row, dest_col);
            return restNode;
        }
        else return null;
    }

    public DamasNode checkMoveAndDestroy(int orig_row, int orig_col, int dest_row, int dest_col, DamasNode current_node, int current_player) {
        int opponent = (current_player== black_tokens) ? white_tokens : black_tokens;
        if (!current_node.isOutOfBound(orig_row, orig_col) && current_node.getCell(orig_row, orig_col)==current_player) {
            DamasNode destNode = checkAndMove(orig_row, orig_col, dest_row, dest_col, current_node);
            if (destNode!= null) { //Significa que se ha movido la ficha
                if (Math.abs(dest_row-orig_row)>1 && Math.abs(dest_col-orig_col)>1){ //Significa que nos hemos movido dos posiciones y hay que eliminar la ficha del oponente de por medio
                    if (current_node.getCell((orig_row+dest_row)/2, (orig_col+dest_col)/2)==opponent) {
                        destNode.setCell((orig_row+dest_row)/2, (orig_col+dest_col)/2, this.empty_cell); // Este check es redudante en el caso de la máquina porque llamo a esta funcion solo si se ha detectado una ficha del oponente de por medio.
                        destNode.decreaseToken(opponent);
                    }
                    else System.out.print("WARNING! Movimiento prohibido\n");
                }
            }
            return  destNode;
        }
        else return null;
    }

    public Triplet<Integer, Integer, Integer> getBoardBorders(int player, int board_size){
        // @return Triplet<Incremento, Start, End>
        if (player== black_tokens){
            return new Triplet<Integer, Integer, Integer>(+1, 0, board_size-1);
        }
        else return new Triplet<Integer, Integer, Integer>( -1, board_size-1, 0);
    }
}

class DamasH1 extends Damas{

    public DamasH1(DamasNode initial_node){
        super(initial_node);
    }

    @Override
    public int calcularHeuristica(Node node, int player_id) {
        return super.calcularHeuristicaV1((DamasNode) node, player_id);
    }
}

class DamasH2 extends Damas{

    public DamasH2(DamasNode initial_node){
        super(initial_node);
    }

    @Override
    public int calcularHeuristica(Node node, int player_id) {
        return super.calcularHeuristicaV2((DamasNode) node, player_id);
    }
}

class DamasH3 extends Damas{

    public DamasH3(DamasNode initial_node){
        super(initial_node);
    }

    @Override
    public int calcularHeuristica(Node node, int player_id) {
        return super.calcularHeuristicaV3((DamasNode) node, player_id);
    }
}



class DamasNode implements Node {
    private Integer[][] board;
    private int size;
    private int nTokens_u1, nTokens_u2, tokenU1, tokenU2;

    public DamasNode(Integer[][] board, int nTokens_u1, int nTokens_u2, int tokenU1, int tokenU2){
        this.board = board;
        this.size = board.length; //We assume it's square matrix
        this.nTokens_u1 = nTokens_u1;
        this.nTokens_u2 = nTokens_u2;
        this.tokenU1 = tokenU1;
        this.tokenU2 = tokenU2;
    }

    public Integer[][] getBoard(){
        return this.board;
    }

    public int getNumberOfTokens(int token) {
        if (this.tokenU1 ==token) return this.nTokens_u1;
        else if (this.tokenU2==token) return this.nTokens_u2;
        else return -1;
    }

    public void increaseToken(int token) {
        if (this.tokenU1 ==token) this.nTokens_u1++;
        else if (this.tokenU2==token) this.nTokens_u2++;
    }

    public void decreaseToken(int token) {
        if (this.tokenU1 ==token) this.nTokens_u1--;
        else if (this.tokenU2==token) this.nTokens_u2--;
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

    public void setBoard(Integer[][] newBoard){
        this.board = newBoard;
    }

    public DamasNode clone(){
        Integer [][] board_copy = new Integer[this.size][];
        for (int i =0; i < this.size; i++){
            board_copy[i] = this.board[i].clone();
        }
        return new DamasNode(board_copy, this.nTokens_u1, this.nTokens_u2, this.tokenU1, this.tokenU2);
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
        for (char c='A'; c <(int) 'A'+this.size; c++) builder.append(c+"   ");
        builder.append("\n");
        int cont=1;
        for (Integer[] row: this.board){
            builder.append(cont+"\t");
            for (int col: row) builder.append(col+"   ");
            builder.append("\n");
            cont++;
        }
        return "\n"+builder.toString()+"Fichas negras (1) restantes: "+this.nTokens_u1 +"\n"+
                "Fichas blancas (2) restantes: "+this.nTokens_u2+"\n";
    }
}

