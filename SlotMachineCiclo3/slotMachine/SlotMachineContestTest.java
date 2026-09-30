import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad del TERCER CICLO: la máquina de n ruedas y n
 * símbolos, SlotMachine(n) (requisito 13), y la solución del problema
 * de la maratón, SlotMachineContest.solve (requisito 14). Todas en modo
 * INVISIBLE. simulate (requisito 15) se verifica en las pruebas de
 * aceptación, porque necesita el simulador visible.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachineContestTest{

    /**
     * Crea el conjunto de pruebas.
     */
    public SlotMachineContestTest(){
        super();
    }

    // ---------- Requisito 13: SlotMachine(n) ----------

    /**
     * SlotMachine(n) debería crear n ruedas y n símbolos.
     */
    @Test
    public void shouldCreateNWheelsAndNSymbols(){
        SlotMachine machine = new SlotMachine(5);
        assertEquals(5, machine.wheelCount());
        assertEquals(5, machine.symbols().length);
        assertTrue(machine.ok());
    }

    /**
     * SlotMachine(n) NO debería repetir colores en sus símbolos.
     */
    @Test
    public void shouldNotRepeatSymbolColors(){
        SlotMachine machine = new SlotMachine(20);
        String[] symbols = machine.symbols();
        for(int i = 0; i < symbols.length; i++){
            for(int j = i + 1; j < symbols.length; j++){
                assertNotEquals(symbols[i], symbols[j]);
            }
        }
    }

    /**
     * SlotMachine(n) NO debería empezar en jackpot ni visible.
     */
    @Test
    public void shouldNotStartInJackpotNorVisible(){
        for(int i = 0; i < 50; i++){
            SlotMachine machine = new SlotMachine(3);
            assertFalse(machine.isJackpot());
            assertFalse(machine.isVisible());
        }
    }

    /**
     * SlotMachine(n) debería dejar toda rueda mostrando un símbolo de su
     * catálogo.
     */
    @Test
    public void shouldShowASymbolOfTheCatalogOnEveryWheel(){
        SlotMachine machine = new SlotMachine(6);
        java.util.List<String> catalog = java.util.Arrays.asList(machine.symbols());
        for(String shown : machine.configuration()){
            assertTrue(catalog.contains(shown));
        }
    }

    // ---------- Requisito 14: solve ----------

    /**
     * solve debería dejar la máquina en jackpot (k = 1) para varios n.
     */
    @Test
    public void shouldSolveMachinesOfSeveralSizes(){
        for(int n = 2; n <= 50; n++){
            SlotMachine machine = new SlotMachine(n);
            SlotMachineContest.solve(machine, n);
            assertEquals(1, machine.distinctSymbols());
            assertTrue(machine.isJackpot());
        }
    }

    /**
     * solve debería retornar acciones que llevan de la configuración
     * inicial a una ganadora.
     */
    @Test
    public void shouldReturnTheActionsFromTheInitialConfiguration(){
        SlotMachine machine = new SlotMachine(8);
        String[] initial = machine.configuration();
        int[][] actions = SlotMachineContest.solve(machine, 8);
        for(int[] a : actions) machine.spin(a[0], -a[1]);
        assertArrayEquals(initial, machine.configuration());
        for(int[] a : actions) machine.spin(a[0], a[1]);
        assertTrue(machine.isJackpot());
    }

    /**
     * solve NO debería retornar acciones sobre ruedas inexistentes ni
     * más de una acción por rueda.
     */
    @Test
    public void shouldNotReturnInvalidActions(){
        int n = 10;
        int[][] actions = SlotMachineContest.solve(n);
        assertTrue(actions.length <= n);
        for(int[] a : actions){
            assertEquals(2, a.length);
            assertTrue(a[0] >= 1 && a[0] <= n);
            assertTrue(a[1] != 0 && Math.abs(a[1]) <= n / 2);
        }
    }

    /**
     * solve NO debería mover nada si la máquina ya está en jackpot.
     */
    @Test
    public void shouldNotMoveAMachineAlreadyInJackpot(){
        SlotMachine machine = new SlotMachine(4);
        String c = machine.configuration()[0];
        machine.spin(new String[]{c, c, c, c});
        int[][] actions = SlotMachineContest.solve(machine, 4);
        assertEquals(0, actions.length);
        assertTrue(machine.isJackpot());
    }

    /**
     * solve NO debería hacer visible la máquina.
     */
    @Test
    public void shouldNotMakeTheMachineVisibleWhenSolving(){
        SlotMachine machine = new SlotMachine(5);
        SlotMachineContest.solve(machine, 5);
        assertFalse(machine.isVisible());
    }
}
