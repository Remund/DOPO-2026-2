import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de pruebas COMPARTIDAS del tercer ciclo (creación colectiva del
 * curso). Los casos de este grupo usan las iniciales del autor:
 * Mora Casas (Mc) -> accordingMc...
 *
 * @author  Mora Casas (y demás autores del curso)
 * @version 1.0 (ciclo 3)
 */
public class SlotMachineContestCTest{

    /**
     * Crea el conjunto de pruebas.
     */
    public SlotMachineContestCTest(){
        super();
    }

    /**
     * Para cualquier n, aplicar las acciones de solve debería llevar a
     * una configuración ganadora.
     */
    @Test
    public void accordingMcShouldWinWithTheActionsOfSolve(){
        for(int n = 3; n <= 50; n++){
            SlotMachine machine = new SlotMachine(n);
            SlotMachineContest.solve(machine, n);
            assertEquals(1, machine.distinctSymbols());
        }
    }

    /**
     * solve NO debería usar más de una acción por rueda ni rotar más de
     * media vuelta (las acciones son las necesarias).
     */
    @Test
    public void accordingMcShouldNotReturnUnnecessaryActions(){
        int n = 50;
        int[][] actions = SlotMachineContest.solve(n);
        assertTrue(actions.length <= n);
        for(int[] a : actions){
            assertTrue(Math.abs(a[1]) <= n / 2);
        }
    }
}
