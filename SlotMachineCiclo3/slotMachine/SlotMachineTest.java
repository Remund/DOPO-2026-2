import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias del PRIMER CICLO de SlotMachine: creación de la
 * máquina, manejo de ruedas (add, del), manejo de símbolos (add, del),
 * ubicación manual de símbolos, giro aleatorio, consultas (symbols,
 * distinctSymbols, configuration, isJackpot), visibilidad y salida.
 *
 * Las pruebas del segundo ciclo (swap, lock, unlock, spin por pasos y
 * spin por configuración) están en SlotMachineC2Test.
 *
 * Cada operación tiene al menos un caso should (lo que DEBERÍA hacer) y
 * un caso shouldNot (lo que NO DEBERÍA hacer). En los casos shouldNot la
 * máquina debe dejar ok() en false y no cambiar su estado.
 *
 * Nota: las pruebas se ejecutan con la máquina en modo INVISIBLE para
 * evitar que aparezcan diálogos JOptionPane durante la ejecución
 * automática (el comportamiento del JOptionPane en modo visible debe
 * verificarse manualmente, ya que es una ventana modal). Por la misma
 * razón exit() no se prueba aquí: cierra el simulador y terminaría
 * también el ejecutor de pruebas.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachineTest{

    private SlotMachine machine;

    /**
     * Crea el conjunto de pruebas. JUnit se encarga de instanciarlo
     * una vez por cada caso de prueba.
     */
    public SlotMachineTest(){
        super();
    }

    /**
     * Prepara una máquina nueva, vacía e invisible, antes de cada prueba.
     */
    @Before
    public void setUp(){
        machine = new SlotMachine();
    }

    // ---------- Creación de la máquina ----------

    /**
     * Una máquina recién creada debería estar vacía: sin ruedas, sin
     * símbolos y con la última operación en éxito.
     */
    @Test
    public void shouldCreateAnEmptyMachine(){
        assertEquals(0, machine.wheelCount());
        assertEquals(0, machine.symbols().length);
        assertEquals(0, machine.configuration().length);
        assertTrue(machine.ok());
    }

    /**
     * Una máquina recién creada NO debería estar visible ni considerarse
     * ganadora.
     */
    @Test
    public void shouldNotStartVisibleOrInJackpot(){
        assertFalse(machine.isVisible());
        assertFalse(machine.isJackpot());
    }

    // ---------- addWheel ----------

    /**
     * addWheel debería insertar la rueda en la posición pedida.
     */
    @Test
    public void shouldAddWheelsAtTheGivenPosition(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        assertEquals(3, machine.wheelCount());
        assertTrue(machine.ok());
    }

    /**
     * addWheel debería ajustar al rango válido las posiciones fuera de
     * rango, en vez de fallar: menores que 1 se vuelven 1 y mayores al
     * máximo se vuelven el máximo.
     */
    @Test
    public void shouldLimitThePositionWhenAddingAWheel(){
        machine.addWheel(-3);   // menor que 1 -> se usa 1
        machine.addWheel(50);   // mayor al máximo -> se usa el máximo
        assertEquals(2, machine.wheelCount());
        assertTrue(machine.ok());
    }

    // ---------- delWheel ----------

    /**
     * delWheel debería eliminar exactamente la rueda de la posición
     * indicada, dejando las demás en su orden.
     */
    @Test
    public void shouldDeleteTheWheelAtTheGivenPosition(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.delWheel(1);

        assertEquals(1, machine.wheelCount());
        assertArrayEquals(new String[]{"blue"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * delWheel debería ajustar al máximo una posición fuera de rango y
     * eliminar la última rueda.
     */
    @Test
    public void shouldLimitThePositionWhenDeletingAWheel(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        machine.delWheel(99); // fuera de rango -> elimina la última

        assertArrayEquals(new String[]{"red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * delWheel NO debería hacer nada sobre una máquina sin ruedas.
     */
    @Test
    public void shouldNotDeleteWheelsWhenThereAreNone(){
        machine.delWheel(1);
        assertFalse(machine.ok());
        assertEquals(0, machine.wheelCount());
    }

    // ---------- addSymbol ----------

    /**
     * addSymbol debería respetar la posición de inserción dentro del
     * catálogo y ajustar al final las posiciones fuera de rango.
     */
    @Test
    public void shouldAddSymbolsRespectingTheOrder(){
        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");    // se inserta antes de red
        machine.addSymbol(10, "green");  // fuera de rango -> al final
        assertArrayEquals(new String[]{"blue", "red", "green"}, machine.symbols());
        assertTrue(machine.ok());
    }

    /**
     * addSymbol NO debería aceptar un color que ya existe: los símbolos
     * deben ser de colores diferentes.
     */
    @Test
    public void shouldNotAddARepeatedSymbol(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "RED ");
        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    /**
     * addSymbol NO debería cambiar lo que muestran las ruedas, aunque el
     * nuevo símbolo quede antes en el catálogo (refactor con Symbol).
     */
    @Test
    public void shouldNotChangeTheWheelsWhenAddingASymbol(){
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "blue");

        machine.addSymbol(1, "green");

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * addSymbol NO debería aceptar un color que no exista en el estándar
     * CSS; el catálogo debe quedar intacto.
     */
    @Test
    public void shouldNotAddASymbolThatIsNotACssColor(){
        machine.addSymbol(1, "notacolor");
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    // ---------- delSymbol ----------

    /**
     * delSymbol debería eliminar del catálogo el símbolo indicado.
     */
    @Test
    public void shouldDeleteTheGivenSymbol(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.delSymbol("red");

        assertArrayEquals(new String[]{"blue"}, machine.symbols());
        assertTrue(machine.ok());
    }

    /**
     * delSymbol NO debería modificar el catálogo cuando el símbolo
     * pedido no está en él.
     */
    @Test
    public void shouldNotDeleteASymbolThatIsNotInTheCatalog(){
        machine.addSymbol(1, "red");

        machine.delSymbol("purple");

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }

    // ---------- placeSymbol ----------

    /**
     * placeSymbol debería dejar en cada rueda exactamente el símbolo
     * solicitado.
     */
    @Test
    public void shouldPlaceTheGivenSymbolOnTheGivenWheel(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "red");

        assertArrayEquals(new String[]{"blue", "red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * placeSymbol NO debería ubicar un símbolo que no esté en el
     * catálogo, y la rueda debe conservar el que tenía.
     */
    @Test
    public void shouldNotPlaceASymbolThatIsNotInTheCatalog(){
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");

        machine.placeSymbol(1, "green");

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * placeSymbol NO debería funcionar sobre una máquina sin ruedas.
     */
    @Test
    public void shouldNotPlaceASymbolWhenThereAreNoWheels(){
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }

    // ---------- spin(rueda) ----------

    /**
     * spin(rueda) debería dejar en esa rueda alguno de los símbolos del
     * catálogo.
     */
    @Test
    public void shouldSpinOneWheel(){
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.spin(1);

        assertTrue(machine.ok());
        String result = machine.configuration()[0];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    /**
     * spin(rueda) NO debería girar una rueda cuando el catálogo está
     * vacío: no hay símbolo que mostrar.
     */
    @Test
    public void shouldNotSpinOneWheelWithoutSymbols(){
        machine.addWheel(1);

        machine.spin(1);

        assertFalse(machine.ok());
        assertNull(machine.configuration()[0]);
    }

    // ---------- spin() ----------

    /**
     * spin() debería dejar un símbolo válido en todas las ruedas.
     */
    @Test
    public void shouldSpinEveryWheel(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.spin();

        assertTrue(machine.ok());
        for(String color : machine.configuration()){
            assertNotNull(color);
        }
    }

    /**
     * spin() NO debería funcionar si falta alguno de los dos
     * ingredientes: ruedas o símbolos.
     */
    @Test
    public void shouldNotSpinWithoutWheelsOrSymbols(){
        machine.spin(); // sin ruedas ni símbolos
        assertFalse(machine.ok());

        machine.addWheel(1);
        machine.spin(); // hay rueda pero no hay símbolos
        assertFalse(machine.ok());
    }

    // ---------- symbols() ----------

    /**
     * symbols debería entregar los colores del catálogo en el orden en
     * que fueron definidos.
     */
    @Test
    public void shouldListTheSymbolsInOrder(){
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        assertArrayEquals(new String[]{"red", "blue", "green"}, machine.symbols());
    }

    /**
     * symbols NO debería entregar ningún color cuando el catálogo está
     * vacío.
     */
    @Test
    public void shouldNotListAnySymbolWhenTheCatalogIsEmpty(){
        assertEquals(0, machine.symbols().length);
    }

    // ---------- distinctSymbols() ----------

    /**
     * distinctSymbols debería contar los símbolos diferentes que se ven
     * en las ruedas (el k de la maratón).
     */
    @Test
    public void shouldCountTheDifferentSymbolsShown(){
        for(int i = 1; i <= 3; i++) machine.addWheel(i);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.spin(new String[]{"red", "blue", "green"});
        assertEquals(3, machine.distinctSymbols());
    }

    /**
     * distinctSymbols NO debería contar dos veces un símbolo que se ve
     * en dos ruedas, ni los símbolos del catálogo que no se ven.
     */
    @Test
    public void shouldNotCountRepeatedOrHiddenSymbols(){
        for(int i = 1; i <= 3; i++) machine.addWheel(i);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.spin(new String[]{"red", "red", "blue"});
        assertEquals(2, machine.distinctSymbols());
    }

    // ---------- configuration() ----------

    /**
     * configuration debería reportar, de izquierda a derecha, el símbolo
     * que muestra cada rueda.
     */
    @Test
    public void shouldReportWhatEveryWheelIsShowing(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        assertArrayEquals(new String[]{"red", "blue"}, machine.configuration());
    }

    /**
     * configuration NO debería reportar un símbolo para una rueda que
     * todavía no tiene ninguno, porque el catálogo está vacío.
     */
    @Test
    public void shouldNotReportASymbolForAWheelWithoutOne(){
        machine.addWheel(1);
        assertEquals(1, machine.configuration().length);
        assertNull(machine.configuration()[0]);
    }

    // ---------- isJackpot() ----------

    /**
     * isJackpot debería ser verdadero cuando todas las ruedas muestran
     * el mismo símbolo.
     */
    @Test
    public void shouldDetectAJackpot(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");

        assertTrue(machine.isJackpot());
    }

    /**
     * isJackpot NO debería ser verdadero si alguna rueda muestra un
     * símbolo distinto.
     */
    @Test
    public void shouldNotDetectAJackpotWithDifferentSymbols(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");

        assertFalse(machine.isJackpot());
    }

    /**
     * isJackpot NO debería ser verdadero en una máquina sin ruedas: no
     * hay nada que pueda coincidir.
     */
    @Test
    public void shouldNotDetectAJackpotWithoutWheels(){
        assertFalse(machine.isJackpot());
    }

    // ---------- makeVisible() / makeInvisible() ----------

    /**
     * makeVisible y makeInvisible deberían alternar el estado de
     * visibilidad del simulador.
     */
    @Test
    public void shouldTurnVisibilityOnAndOff(){
        assertFalse(machine.isVisible());
        machine.makeVisible();
        assertTrue(machine.isVisible());
        machine.makeInvisible();
        assertFalse(machine.isVisible());
    }

    /**
     * makeInvisible NO debería detener el simulador: sigue funcionando
     * en modo invisible.
     */
    @Test
    public void shouldNotStopWorkingWhenInvisible(){
        machine.makeVisible();
        machine.makeInvisible();
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
        assertEquals(1, machine.wheelCount());
    }

    // ---------- exit() ----------
    //
    // exit() cierra el simulador y termina la ejecucion, asi que no se
    // puede probar automaticamente: al invocarlo se cerraria tambien el
    // ejecutor de pruebas. Se verifica a mano, comprobando que la
    // ventana se cierra y que el objeto desaparece del banco de objetos.
}
