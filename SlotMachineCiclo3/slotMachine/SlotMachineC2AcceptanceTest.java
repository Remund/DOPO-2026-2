import javax.swing.JOptionPane;

/**
 * Pruebas de aceptación del SEGUNDO CICLO (requisitos 9 a 12). Cada
 * prueba arma una máquina VISIBLE y va llamando sus métodos con pausas,
 * para que el usuario vea en pantalla el efecto de cada operación.
 * En BlueJ: clic derecho sobre la clase y ejecutar cada método estático.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachineC2AcceptanceTest{

    private static final int PAUSE = 1200;

    /**
     * Aceptación 1 (requisitos 9 y 10): intercambiar ruedas y fijarlas.
     * Muestra que una rueda fija (marco rojo) no gira ni se intercambia,
     * y que al soltarla vuelve a funcionar hasta lograr el jackpot.
     */
    public static void swapAndLockWheels(){
        SlotMachine machine = createVisibleMachine(3, new String[]{"red", "blue", "green"});
        announce("Configuración inicial: red, blue, green");
        machine.spin(new String[]{"red", "blue", "green"});
        pause();
        announce("swap(1, 3): se intercambian la primera y la última rueda");
        machine.swap(1, 3);
        pause();
        announce("lock(2): la rueda del medio queda fija (marco rojo)");
        machine.lock(2);
        pause();
        announce("spin(): giran todas menos la fija");
        machine.spin();
        pause();
        announce("swap(2, 3): debe fallar porque la rueda 2 está fija");
        machine.swap(2, 3);
        pause();
        announce("unlock(2) y spin({blue, blue, blue}): jackpot (marco dorado)");
        machine.unlock(2);
        machine.spin(new String[]{"blue", "blue", "blue"});
        pause();
    }

    /**
     * Aceptación 2 (requisitos 11 y 12): rotar ruedas por pasos. Cada
     * rotación se ve paso a paso, hacia adelante, hacia atrás y dando
     * la vuelta completa al catálogo.
     */
    public static void spinWheelsBySteps(){
        String[] colors = {"red", "orange", "yellow", "green", "blue"};
        SlotMachine machine = createVisibleMachine(4, colors);
        announce("Configuración inicial: todas en red");
        machine.spin(new String[]{"red", "red", "red", "red"});
        pause();
        announce("spin(1, 3): la rueda 1 avanza 3 pasos (red -> green)");
        machine.spin(1, 3);
        pause();
        announce("spin(2, -2): la rueda 2 retrocede 2 pasos (red -> green)");
        machine.spin(2, -2);
        pause();
        announce("spin(3, 8): la rueda 3 da la vuelta y queda en green");
        machine.spin(3, 8);
        pause();
        announce("spin(4, 3): la rueda 4 queda en green -> jackpot");
        machine.spin(4, 3);
        pause();
    }

    /**
     * Crea una máquina visible con las ruedas y símbolos dados.
     * @param wheels número de ruedas
     * @param colors colores de los símbolos, en orden
     * @return la máquina creada, ya visible
     */
    private static SlotMachine createVisibleMachine(int wheels, String[] colors){
        Canvas.getCanvas().clear();
        SlotMachine machine = new SlotMachine();
        for(int i = 1; i <= wheels; i++) machine.addWheel(i);
        for(int i = 0; i < colors.length; i++) machine.addSymbol(i + 1, colors[i]);
        machine.makeVisible();
        return machine;
    }

    /**
     * Le indica al usuario qué va a pasar a continuación.
     * @param message descripción del siguiente paso
     */
    private static void announce(String message){
        JOptionPane.showMessageDialog(null, message, "Prueba de aceptación - Ciclo 2",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pausa para que el efecto de la operación se alcance a ver.
     */
    private static void pause(){
        Canvas.getCanvas().wait(PAUSE);
    }
}