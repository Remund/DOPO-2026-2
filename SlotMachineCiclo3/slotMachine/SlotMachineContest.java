import java.util.ArrayList;

/**
 * SlotMachineContest resuelve y simula el Problem "Slot Machine" de la
 * maratón ICPC 2025: hay n ruedas, cada una con los mismos n símbolos en
 * el mismo orden; sólo se puede rotar una rueda a la vez y la única
 * información disponible es cuántos símbolos distintos se ven (k). Hay
 * que lograr que todas las ruedas muestren el mismo símbolo (k = 1).
 *
 * Estrategia (a lo sumo 2n^2 + 2n consultas, menos de 10 000 para n <= 50):
 *   1. Separar: rueda por rueda, se prueba cada posición y se deja la
 *      rueda donde k es máximo. Al terminar, todas las ruedas muestran
 *      símbolos diferentes (k = n).
 *   2. Medir: se rota la rueda 1 un paso. Ahora el símbolo original de la
 *      rueda 1 es el único que no se ve; al probar cada posición de otra
 *      rueda, el máximo de k aparece justo cuando esa rueda muestra ese
 *      símbolo. Así se conoce cuánto rotar cada rueda.
 *   3. Alinear: se devuelve la rueda 1 y se rota cada rueda lo medido.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachineContest{

    /**
     * Resuelve el problema para una máquina de n ruedas y n símbolos,
     * inicializada aleatoriamente. La máquina permanece invisible.
     * @param n número de ruedas y de símbolos
     * @return la secuencia de acciones {i, j} necesarias para ganar:
     *         rotar la rueda i, j posiciones (j negativo = hacia atrás)
     */
    public static int[][] solve(int n){
        SlotMachine machine = new SlotMachine(n);
        return solve(machine, n);
    }

    /**
     * Simula la solución para una máquina de n ruedas y n símbolos: la
     * resuelve en modo invisible, la regresa a su configuración inicial
     * y, ya visible, ejecuta paso a paso las acciones para ganar.
     * @param n número de ruedas y de símbolos
     */
    public static void simulate(int n){
        SlotMachine machine = new SlotMachine(n);
        int[][] actions = solve(machine, n);
        for(int[] action : actions){
            machine.spin(action[0], -action[1]);
        }
        machine.makeVisible();
        Canvas.getCanvas().wait(1000);
        for(int[] action : actions){
            machine.spin(action[0], action[1]);
        }
    }

    /**
     * Resuelve el problema sobre una máquina dada (sin hacerla visible) y
     * la deja en una configuración ganadora.
     * @param machine máquina de n ruedas y n símbolos
     * @param n       número de ruedas y de símbolos
     * @return la secuencia de acciones {i, j} necesarias para ganar
     */
    static int[][] solve(SlotMachine machine, int n){
        int[] rotations = new int[n + 1];
        if(machine.distinctSymbols() > 1){
            separateWheels(machine, n, rotations);
            int[] offsets = measureOffsets(machine, n, rotations);
            alignWheels(machine, n, rotations, offsets);
        }
        return toActions(rotations, n);
    }

    /**
     * Deja todas las ruedas mostrando símbolos diferentes. Cada
     * rueda se rota una vuelta completa, paso a paso, recordando la
     * posición con mayor k; luego se deja en esa posición.
     */
    private static void separateWheels(SlotMachine machine, int n, int[] rotations){
        for(int wheel = 1; wheel <= n; wheel++){
            int best = 0;
            int bestK = machine.distinctSymbols();
            for(int r = 1; r < n; r++){
                rotate(machine, wheel, 1, rotations);
                int k = machine.distinctSymbols();
                if(k > bestK){
                    best = r;
                    bestK = k;
                }
            }
            rotate(machine, wheel, 1, rotations);
            if(best != 0) rotate(machine, wheel, best, rotations);
        }
    }

    /**
     * Mide cuánto hay que rotar cada rueda para que muestre el
     * símbolo original de la rueda 1.
     * @return offsets[i] = pasos que debe rotar la rueda i (i >= 2)
     */
    private static int[] measureOffsets(SlotMachine machine, int n, int[] rotations){
        int[] offsets = new int[n + 1];
        rotate(machine, 1, 1, rotations);
        for(int wheel = 2; wheel <= n; wheel++){
            int bestK = -1;
            for(int r = 1; r < n; r++){
                rotate(machine, wheel, 1, rotations);
                int k = machine.distinctSymbols();
                if(k > bestK){
                    offsets[wheel] = r;
                    bestK = k;
                }
            }
            rotate(machine, wheel, 1, rotations);
        }
        rotate(machine, 1, -1, rotations);
        return offsets;
    }

    /**
     * Rota cada rueda lo medido; todas quedan con el símbolo de
     * la rueda 1.
     */
    private static void alignWheels(SlotMachine machine, int n, int[] rotations, int[] offsets){
        for(int wheel = 2; wheel <= n; wheel++){
            rotate(machine, wheel, offsets[wheel], rotations);
        }
    }

    /**
     * Ejecuta una acción sobre la máquina y acumula la rotación neta de
     * la rueda.
     */
    private static void rotate(SlotMachine machine, int wheel, int steps, int[] rotations){
        machine.spin(wheel, steps);
        rotations[wheel] += steps;
    }

    /**
     * Convierte las rotaciones netas en la lista de acciones necesarias:
     * una por cada rueda que deba moverse, por el camino más corto.
     */
    private static int[][] toActions(int[] rotations, int n){
        ArrayList<int[]> actions = new ArrayList<int[]>();
        for(int wheel = 1; wheel <= n; wheel++){
            int steps = Math.floorMod(rotations[wheel], n);
            if(steps > n / 2) steps -= n;
            if(steps != 0) actions.add(new int[]{wheel, steps});
        }
        return actions.toArray(new int[0][]);
    }
}
