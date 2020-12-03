# Práctica 2 IA: JUEGO DE LAS DAMAS

### Ejecución
Basta con ejecutar el main disponible en src/Practica2.java

### Configuración del modo de juego
Es necesario modificar la linea 17 del main. Por defecto contiene lo siguiente:
```
Damas damas_juego = new Damas(initial_board, Joc.MACHINEvsMACHINE, Joc.ALFABETA, Joc.MINIMAX, Joc.HEURISTIC_V2, Joc.HEURISTIC_V3, 6, 6);
``` 
Las opciones de juego y algoritmos disponibles se encuentran en src/Juegos/Joc.java. En concreto hay 4 modos de juego:
* MACHINEvsUSER
* MACHINEvsMACHINE
* USERvsMACHINE
* USERvsUSER (No implementado)

Dos algoritmos de busqueda:
* MINIMAX
* ALFABETA

Y 3 heurísticas diferentes:
* HEURISTIC_V1
* HEURISTIC_V2
* HEURISTIC_V3

Para configurar alguno de estos modos de juego bastaría con pasarle al constructor de ```Damas``` el modo seleccionado.

> **NOTA:** También es necesario pasarle el nivel máximo de profundidad para el algoritmo en concreto (entre 5 a 7 tarda menos de 1 segundo). Si no se quiere limitar la profundida habría que pasarle un -1.

Existe tambíen un constructor para un modo de juego simple (sin tanta configurabilidad):
```
Damas damas_juego = new Damas(initial_board, Joc.MACHINEvsMACHINE, Joc.MINIMAX, Joc.HEURISTIC_V3, 6);
```

