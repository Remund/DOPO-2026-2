import javax.swing.JOptionPane;

/**
 * Pruebas de aceptación del TERCER CICLO (requisitos 13 a 15). Cada
 * paso primero ejecuta la operación (se ve en pantalla), hace una pausa
 * y luego muestra un mensaje explicando lo que acaba de pasar.
 * En BlueJ: clic derecho sobre la clase y ejecutar cada método estático.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachineContestAcceptanceTest{

    private static final int PAUSE = 1200;

    /**
     * Aceptación 1 (requisito 13): la máquina como "testing tool". Crea
     * una máquina de 5 ruedas y 5 símbolos aleatoria, rota algunas ruedas
     * y reporta k (símbolos distintos visibles) después de cada acción.
     */
    public static void useMachineAsTestingTool(){
        Canvas.getCanvas().clear();
        SlotMachine machine = new SlotMachine(5);
        machine.makeVisible();
        pause();
        announce("SlotMachine(5): se creó una máquina de 5 ruedas y 5 colores,\n"
            + "inicializada al azar.\nk = " + machine.distinctSymbols());
        for(int wheel = 1; wheel <= 3; wheel++){
            machine.spin(wheel, wheel);
            pause();
            announce("spin(" + wheel + ", " + wheel + "): la rueda " + wheel + " avanzó "
                + wheel + " paso(s).\nk = " + machine.distinctSymbols());
        }
        machine.makeInvisible();
        pause();
        announce("makeInvisible(): la máquina dejó de verse, pero sigue funcionando.\nk = "
            + machine.distinctSymbols());
    }

    /**
     * Aceptación 2 (requisitos 14 y 15): simular y resolver. Primero
     * simula la solución de una máquina visible hasta el jackpot y luego
     * muestra las acciones que retorna solve (máquina invisible).
     */
    public static void simulateAndSolve(){
        Canvas.getCanvas().clear();
        SlotMachineContest.simulate(6);
        pause();
        announce("simulate(6): se mostró una máquina de 6 ruedas al azar y cada\n"
            + "rueda rotó paso a paso hasta el jackpot (marco dorado).");
        int[][] actions = SlotMachineContest.solve(5);
        announce("solve(5): resolvió otra máquina, esta vez invisible.\n"
            + "Acciones {rueda, pasos}: " + toText(actions));
    }

    /**
     * Convierte las acciones a texto legible.
     * @param actions acciones {rueda, pasos}
     * @return las acciones como texto
     */
    private static String toText(int[][] actions){
        String text = "";
        for(int[] a : actions) text += "{" + a[0] + ", " + a[1] + "} ";
        return actions.length == 0 ? "ninguna (ya estaba en jackpot)" : text;
    }

    /**
     * Le explica al usuario lo que acaba de pasar.
     * @param message mensaje para el usuario
     */
    private static void announce(String message){
        JOptionPane.showMessageDialog(null, message, "Prueba de aceptación - Ciclo 3",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pausa para que el efecto de la operación se alcance a ver.
     */
    private static void pause(){
        Canvas.getCanvas().wait(PAUSE);
    }
}