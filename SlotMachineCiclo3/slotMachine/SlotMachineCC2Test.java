import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de pruebas COMPARTIDAS del segundo ciclo (creación colectiva
 * del curso, construida a través del wiki). Cada grupo aporta sus
 * casos de prueba y los identifica en el nombre del método, usando las
 * iniciales de los autores: inicial del primer apellido seguida de la
 * primera letra del segundo apellido, en orden alfabético.
 *
 * Aporte de este grupo: Mora Casas (Mc),
 * por lo que sus casos de prueba se llaman accordingMcShould...
 * y accordingMcShouldNot...
 *
 * Como en el resto de las pruebas, la máquina se usa en modo
 * INVISIBLE para que no aparezcan diálogos modales.
 *
 * @author  Mora Casas (y demás autores del curso)
 * @version 1.0 (ciclo 2)
 */
public class SlotMachineCC2Test{

    private SlotMachine machine;

    /**
     * Crea el conjunto de pruebas. JUnit se encarga de instanciarlo
     * una vez por cada caso de prueba.
     */
    public SlotMachineCC2Test(){
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
     * Arma una máquina con tres ruedas y un catálogo de tres símbolos:
     * red (0), blue (1) y green (2).
     */
    private void prepareThreeByThree(){
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
    }

    /**
     * Al intercambiar dos ruedas, la máquina debería quedar con los
     * símbolos de esas dos ruedas permutados y las demás intactas.
     */
    @Test
    public void accordingMcShouldSwapTwoWheelsKeepingTheRest(){
        prepareThreeByThree();
        machine.spin(new String[]{"red", "blue", "green"});

        machine.swap(1, 2);

        assertArrayEquals(new String[]{"blue", "red", "green"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * Al rotar una rueda un número de pasos, la máquina debería
     * avanzar cíclicamente dentro del catálogo, sin salirse de él.
     */
    @Test
    public void accordingMcShouldRotateOneWheelCyclically(){
        prepareThreeByThree();
        machine.spin(new String[]{"red", "red", "red"});

        machine.spin(2, 4); // 4 pasos sobre 3 símbolos -> equivale a 1

        assertArrayEquals(new String[]{"red", "blue", "red"}, machine.configuration());
        assertTrue(machine.ok());
    }

    /**
     * Una rueda fija NO debería cambiar de símbolo, ni al girar esa
     * rueda, ni al girar toda la máquina.
     */
    @Test
    public void accordingMcShouldNotMoveALockedWheel(){
        prepareThreeByThree();
        machine.spin(new String[]{"red", "red", "red"});
        machine.lock(2);

        machine.spin(2);
        assertFalse(machine.ok());

        machine.spin(2, 1);
        assertFalse(machine.ok());

        machine.spin();
        assertEquals("red", machine.configuration()[1]);
    }

    /**
     * La máquina NO debería quedar en una configuración parcialmente
     * aplicada: si algún color pedido no está en el catálogo, todas
     * las ruedas conservan su símbolo anterior.
     */
    @Test
    public void accordingMcShouldNotApplyAnIncompleteConfiguration(){
        prepareThreeByThree();
        machine.spin(new String[]{"red", "red", "red"});

        machine.spin(new String[]{"blue", "green", "purple"}); // purple no existe

        assertFalse(machine.ok());
        assertArrayEquals(new String[]{"red", "red", "red"}, machine.configuration());
    }
}
