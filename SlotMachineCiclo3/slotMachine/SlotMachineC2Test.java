import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias del SEGUNDO CICLO de SlotMachine. Cubren los
 * cuatro requisitos funcionales de esta entrega:
 *
 *  9. Intercambiar dos ruedas ...................... swap(wheel1, wheel2)
 * 10. Fijar y soltar una rueda .................... lock(wheel) / unlock(wheel)
 * 11. Rotar una rueda un número de pasos .......... spin(wheel, steps)
 * 12. Dejar la máquina en una configuración dada .. spin(String[] setSymbols)
 *
 * Para cada operación se prueba tanto lo que DEBERÍA hacer como lo que
 * NO DEBERÍA hacer: cuando una operación no es válida, ok() debe quedar
 * en false y el estado de la máquina no debe cambiar.
 *
 * Nota: las pruebas se ejecutan con la máquina en modo INVISIBLE, tal
 * como lo exige el enunciado. Así no aparecen diálogos JOptionPane
 * (que son modales y bloquearían la ejecución automática) y la
 * visualización paso a paso de spin(wheel, steps) no introduce pausas.
 *
 * @author  Mora Casas
 * @version 1.0 (ciclo 2)
 */
public class SlotMachineC2Test{

    private SlotMachine machine;

    /**
     * Crea el conjunto de pruebas. JUnit se encarga de instanciarlo
     * una vez por cada caso de prueba.
     */
    public SlotMachineC2Test(){
        super();
    }

    /**
     * Prepara una máquina nueva, vacía e invisible, antes de cada prueba.
     */
    @Before
    public void setUp(){
        machine = new SlotMachine();
    }

    /**
     * Arma una máquina de prueba con el número de ruedas indicado y un
     * catálogo de tres símbolos: red (0), blue (1) y green (2).
     * @param numWheels cantidad de ruedas a crear
     */
    private void prepare(int numWheels){
        for(int i = 1; i <= numWheels; i++){
            machine.addWheel(i);
        }
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    // ---------- Requisito 9: intercambiar dos ruedas ----------

    /**
     * swap debería intercambiar los símbolos que muestran las dos
     * ruedas indicadas, dejando intactas las demás.
     */
    @Test
    public void shouldSwapTheSymbolsOfBothWheels(){
        prepare(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");

        machine.swap(1, 3);

        assertArrayEquals(new String[]{"green", "blue", "red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * swap NO debería funcionar si alguna de las dos ruedas está fija:
     * una rueda fija no puede cambiar de posición, así que hay que
     * soltarla (unlock) antes de poder intercambiarla.
     */
    @Test
    public void shouldNotSwapWhenEitherWheelIsLocked(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(1);

        machine.swap(1, 2);

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    /**
     * Una vez soltada (unlock), la rueda vuelve a poder participar en
     * un swap con normalidad.
     */
    @Test
    public void shouldSwapAgainAfterUnlock(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(1);
        machine.unlock(1);

        machine.swap(1, 2);

        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * swap debería ajustar al rango válido las posiciones fuera de
     * rango, en vez de fallar.
     */
    @Test
    public void shouldLimitPositionsOutOfRangeWhenSwapping(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.swap(-5, 99); // se ajustan a 1 y 2

        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * swap NO debería hacer nada cuando la máquina tiene menos de dos
     * ruedas.
     */
    @Test
    public void shouldNotSwapWithLessThanTwoWheels(){
        prepare(1);
        machine.placeSymbol(1, "red");

        machine.swap(1, 1);

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.configuration());
    }

    // ---------- Requisito 10: fijar y soltar una rueda ----------

    /**
     * Una rueda fija NO debería cambiar de símbolo al girarla con
     * spin(rueda).
     */
    @Test
    public void shouldNotSpinALockedWheel(){
        prepare(1);
        machine.placeSymbol(1, "red");

        machine.lock(1);
        machine.spin(1);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Una rueda fija NO debería rotar con spin(rueda, pasos).
     */
    @Test
    public void shouldNotRotateALockedWheelBySteps(){
        prepare(1);
        machine.placeSymbol(1, "red");

        machine.lock(1);
        machine.spin(1, 2);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * spin() sobre toda la máquina debería saltarse las ruedas fijas y
     * girar únicamente las libres.
     */
    @Test
    public void shouldSkipLockedWheelsWhenSpinningAll(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        machine.lock(1);
        machine.spin();

        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]); // fija: no cambió
    }

    /**
     * unlock debería devolver la rueda a su estado normal, de modo que
     * vuelva a poder girar y rotar.
     */
    @Test
    public void shouldSpinAgainAfterUnlock(){
        prepare(1);
        machine.placeSymbol(1, "red");

        machine.lock(1);
        machine.unlock(1);

        machine.spin(1);
        assertTrue(machine.ok());

        machine.spin(1, 1);
        assertTrue(machine.ok());
    }

    /**
     * Una rueda fija NO debería poder eliminarse con delWheel: hay
     * que soltarla primero.
     */
    @Test
    public void shouldNotDeleteALockedWheel(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(1);

        machine.delWheel(1);

        assertFalse(machine.ok());
        assertEquals(2, machine.wheelCount());
    }

    /**
     * Una rueda fija NO debería poder cambiar de símbolo con
     * placeSymbol.
     */
    @Test
    public void shouldNotPlaceASymbolOnALockedWheel(){
        prepare(1);
        machine.placeSymbol(1, "red");
        machine.lock(1);

        machine.placeSymbol(1, "blue");

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * spin(configuración) NO debería aplicar nada si alguna de las
     * ruedas involucradas está fija: la operación sigue siendo todo o
     * nada.
     */
    @Test
    public void shouldNotSetAConfigurationOverALockedWheel(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(2);

        machine.spin(new String[]{"green", "green"});

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    /**
     * lock NO debería funcionar sobre una máquina sin ruedas.
     */
    @Test
    public void shouldNotLockWhenThereAreNoWheels(){
        machine.lock(1);
        assertFalse(machine.ok());
    }

    /**
     * unlock NO debería funcionar sobre una máquina sin ruedas.
     */
    @Test
    public void shouldNotUnlockWhenThereAreNoWheels(){
        machine.unlock(1);
        assertFalse(machine.ok());
    }

    // ---------- Requisito 11: rotar una rueda un número de pasos ----------

    /**
     * spin(rueda, pasos) debería avanzar la rueda exactamente esa
     * cantidad de posiciones dentro del catálogo.
     */
    @Test
    public void shouldRotateTheGivenSteps(){
        prepare(1);
        machine.placeSymbol(1, "red"); // índice 0

        machine.spin(1, 2); // red(0) -> blue(1) -> green(2)

        assertEquals("green", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * spin(rueda, pasos) debería dar la vuelta de forma cíclica cuando
     * los pasos superan el tamaño del catálogo.
     */
    @Test
    public void shouldWrapAroundWhenRotating(){
        prepare(1);
        machine.placeSymbol(1, "red"); // índice 0

        machine.spin(1, 7); // 7 pasos sobre 3 símbolos -> equivale a 1

        assertEquals("blue", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * spin(rueda, pasos) con pasos negativos debería retroceder,
     * también de forma cíclica.
     */
    @Test
    public void shouldRotateBackwardsWithNegativeSteps(){
        prepare(1);
        machine.placeSymbol(1, "red"); // índice 0

        machine.spin(1, -1); // retrocede y da la vuelta al último símbolo

        assertEquals("green", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * spin(rueda, 0) NO debería mover la rueda, pero tampoco debería
     * considerarse un error.
     */
    @Test
    public void shouldNotMoveTheWheelWithZeroSteps(){
        prepare(1);
        machine.placeSymbol(1, "blue");

        machine.spin(1, 0);

        assertEquals("blue", machine.configuration()[0]);
        assertTrue(machine.ok());
    }

    /**
     * spin(rueda, pasos) sólo debería afectar a la rueda indicada.
     */
    @Test
    public void shouldNotRotateOtherWheels(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        machine.spin(2, 1);

        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * spin(rueda, pasos) NO debería funcionar cuando la máquina no
     * tiene símbolos en el catálogo.
     */
    @Test
    public void shouldNotRotateWithoutSymbols(){
        machine.addWheel(1);

        machine.spin(1, 3);

        assertFalse(machine.ok());
    }

    // ---------- Requisito 12: dejar la máquina en una configuración dada ----------

    /**
     * spin(configuración) debería dejar en cada rueda el símbolo
     * indicado en la posición correspondiente del arreglo.
     */
    @Test
    public void shouldSetTheGivenConfiguration(){
        prepare(3);

        machine.spin(new String[]{"green", "red", "blue"});

        assertArrayEquals(new String[]{"green", "red", "blue"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * spin(configuración) debería permitir construir un jackpot
     * dejando el mismo símbolo en todas las ruedas.
     */
    @Test
    public void shouldReachAJackpotWithAConfiguration(){
        prepare(3);

        machine.spin(new String[]{"blue", "blue", "blue"});

        assertTrue(machine.isJackpot());
        assertTrue(machine.ok());
    }

    /**
     * spin(configuración) NO debería aceptar un arreglo con una
     * cantidad de colores distinta al número de ruedas.
     */
    @Test
    public void shouldNotSetAConfigurationOfWrongSize(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        machine.spin(new String[]{"blue"}); // falta un color

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());
    }

    /**
     * spin(configuración) NO debería aplicar nada si alguno de los
     * colores no está en el catálogo: la operación es todo o nada.
     */
    @Test
    public void shouldNotSetAConfigurationPartially(){
        prepare(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");

        machine.spin(new String[]{"blue", "purple"}); // purple no existe

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "red"}, machine.configuration());
    }

    /**
     * spin(configuración) NO debería aceptar un arreglo nulo.
     */
    @Test
    public void shouldNotSetANullConfiguration(){
        prepare(1);

        machine.spin((String[]) null);

        assertFalse(machine.ok());
    }


    // ---------- Refactor ciclo 3: lock protege también los símbolos ----------

    /**
     * lock debería dejar la rueda fija (y unlock soltarla).
     */
    @Test
    public void shouldLockAndUnlockAWheel(){
        prepare(2);
        machine.lock(1);
        assertTrue(machine.ok());
        machine.unlock(1);
        assertTrue(machine.ok());
        machine.spin(1, 1);
        assertTrue(machine.ok());
    }

    /**
     * delSymbol NO debería eliminar el símbolo que muestra una rueda
     * fija, porque eso cambiaría la rueda.
     */
    @Test
    public void shouldNotDeleteTheSymbolOfALockedWheel(){
        prepare(2);
        machine.spin(new String[]{"blue", "red"});
        machine.lock(1);

        machine.delSymbol("blue");

        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * delSymbol debería poder eliminar un símbolo que no muestra ninguna
     * rueda fija, aunque haya ruedas fijas.
     */
    @Test
    public void shouldDeleteASymbolNotShownByLockedWheels(){
        prepare(2);
        machine.spin(new String[]{"blue", "red"});
        machine.lock(1);

        machine.delSymbol("green");

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }
}
