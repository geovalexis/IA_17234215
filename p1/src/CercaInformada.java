import java.util.*;


public abstract class CercaInformada {

    Integer[][] matriz; // (x,y)
    Operator[] operadores;
    HashSet<Integer> forbidCells;
    int maxX;
    int maxY;

    public CercaInformada(Integer[][] matriz, Operator[] operadores, HashSet forbiddenCells){
        this.matriz = matriz;
        this.operadores = operadores;
        this.forbidCells = forbiddenCells;
        this.maxX = matriz.length;
        this.maxY = matriz[0].length; // Asumme that all the rows have the same length
    }

    public ArrayList<Nodo> buscarNodo(Nodo nodo_inicial, Nodo nodo_final){
        Collection<Tupla> ListaPendientes = setNewStructure();
        HashSet<Nodo> ListaTratados = new HashSet<>();

        Nodo current_node = nodo_inicial;
        ArrayList<Nodo> current_camino = new ArrayList<>();
        float coste = 0;
        float valHeu = 0;
        Tupla current_tup = new Tupla(nodo_inicial, coste, current_camino, valHeu);
        ListaPendientes.add(current_tup);

        ArrayList<Nodo> solucion = null;
        boolean found=false;
        while (!found && !ListaPendientes.isEmpty()) {
            current_tup = next_trip(ListaPendientes);
            current_node = current_tup.getNodo();
            current_camino = current_tup.getCamino();

            delete_node(current_tup, ListaPendientes);

            if (current_node.equals(nodo_final)){
                found=true;
                solucion = current_camino;
                System.out.println("Nº de nodos tratados: "+ListaTratados.size());
                System.out.println("Coste (tiempo) total por este camino: "+current_tup.getCosteAcumulado());
            }
            else {
                ArrayList<Nodo> sucesores = getSucesores(current_node);
                for(Nodo succ: sucesores){
                    if (!ListaTratados.contains(succ)){
                        ArrayList<Nodo> new_camino = (ArrayList<Nodo>) current_camino.clone();
                        new_camino.add(current_node);
                        coste = current_tup.getCosteAcumulado() + calcular_coste(current_node, succ);
                        valHeu = calcular_heuristica(succ, nodo_final, coste);
                        add(new Tupla(succ, coste, new_camino,valHeu), ListaPendientes);
                    }
                }
                ListaTratados.add(current_node);
            }
        }

        return solucion;
    }

    public abstract Collection<Tupla> setNewStructure();

    public abstract Tupla next_trip(Collection<Tupla> ListaPendientes);

    public float calcular_heuristica(Nodo current_node, Nodo final_node, float costeAcumulado){
        //return calcular_heuristicaV1(current_node, final_node);
        return calcular_heuristicaV2(current_node, final_node);
        //return calcular_heuristicaV3(current_node, final_node);
    }

    public float calcular_heuristicaV1(Nodo current_node, Nodo final_node){
        int final_coors = final_node.getX() * final_node.getY();
        int current_coors = current_node.getX() * current_node.getY();
        return final_coors - current_coors;
    }

    public float calcular_heuristicaV2(Nodo current_node, Nodo final_node){
        return (float) Math.pow(final_node.getValue() - current_node.getValue(), 2);
    }

    public float calcular_heuristicaV3(Nodo current_node, Nodo next_node){
        return calcular_coste(current_node, next_node);
    }


    public float calcular_coste(Nodo nodo_orig, Nodo nodo_desti){
        int dt = nodo_orig.getValue() - nodo_desti.getValue();
        if ( dt >= 0){
            return 1+dt;
        }
        else {
            return (float) 0.5;
        }
    }

    public abstract void add(Tupla trip, Collection<Tupla> ListaPendientes);

    public abstract void delete_node(Tupla trip, Collection<Tupla> ListaPendientes);

    public ArrayList<Nodo> getSucesores(Nodo nodo_actual){
        ArrayList<Nodo> sucesores = new ArrayList<Nodo>();
        for (Operator oper: operadores){
            int next_x = nodo_actual.getX() + oper.getIncrX();
            int next_y =  nodo_actual.getY() + oper.getIncrY();
            if (next_x >= 0 && next_x < maxX && next_y >= 0 && next_y < maxY ){
                if (!forbidCells.contains(matriz[next_x][next_y])){
                    sucesores.add(new Nodo(next_x, next_y, matriz[next_x][next_y]));
                }
            }
        }
        return sucesores;
    }

}

class Tupla {
    Nodo node;
    ArrayList<Nodo> camino;
    float valorHeuristico;
    float costeAcumulado;

    public Tupla(Nodo estado, float costeAcumulado, ArrayList<Nodo> camino, float valorHeuristico){
        this.node = estado;
        this.costeAcumulado = costeAcumulado;
        this.camino = camino;
        this.valorHeuristico = valorHeuristico;
    }

    public Nodo getNodo(){
        return this.node;
    }

    public ArrayList<Nodo> getCamino(){
        return this.camino;
    }

    public float getCosteAcumulado() { return this.costeAcumulado;};

    public float getValorHeuristico(){
        return this.valorHeuristico;
    }

    @Override
    public boolean equals(Object object){
        boolean same = false;
        if (object != null){
            if (object instanceof Nodo) {
                same = (this.node.equals((Nodo) object));
            }
            else if (object instanceof Tupla){
                same = (this.node.equals(((Tupla) object).getNodo())) && this.camino.equals(((Tupla) object).getCamino());
                //This way of compare the caminos is dependable of the order, that means that it will return true
                //only if all the ordered pairs of Nodo instances are equal.
            }
        }

        return same;
    }

    @Override
    public int hashCode() { return this.node.hashCode() + this.camino.hashCode();}

}

class Nodo{
    int X;
    int Y;
    int value;

   public Nodo(int X, int Y, int value){
        this.X = X;
        this.Y = Y;
        this.value = value;
   }

   public int getX() {return this.X;};

   public int getY() {return this.Y;};

   public int getValue() {return this.value;};

   @Override
   public boolean equals(Object object){
       boolean same = false;
       if (object != null && object instanceof Nodo) {
           same = (this.X == ((Nodo) object).getX()) && (this.Y == ((Nodo) object).getY())
                   && (this.value == ((Nodo) object).getValue());
       }
       return same;
   }

   @Override
   public int hashCode() {return this.X * this.Y * this.value;}

   public String toString() {
       return "(x: "+this.X+", y: "+this.Y+", value: "+this.value+")";
   }

}

class Operator{
    int incrX;
    int incrY;
    String name;

    public Operator(int incrX, int incrY, String name){
        this.incrX = incrX;
        this.incrY = incrY;
        this.name = name;
    }

    public int getIncrX(){
        return this.incrX;
    }

    public int getIncrY(){
        return this.incrY;
    }

    public String getName() {
        return this.name;
    }
}
