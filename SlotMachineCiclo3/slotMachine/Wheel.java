import java.util.ArrayList;
import java.util.Random;

/**
 * Wheel representa una rueda (reel) individual de la máquina
 * tragamonedas. Cada rueda conoce el catálogo de símbolos (Symbol) del
 * que se alimenta, sabe cuál símbolo está mostrando (guarda una
 * referencia al objeto Symbol, no sólo su posición) y sabe cómo girar:
 * la máquina sólo le pide que gire, no le indica cómo hacerlo. Todas las
 * ruedas comparten el mismo catálogo, es decir, tienen los mismos
 * símbolos en el mismo orden (como en el problema de la maratón).
 *
 * Responsabilidades de la rueda:
 *   - saber qué símbolo muestra y mostrarlo en pantalla
 *   - girar al azar, rotar un número de pasos y ubicar un símbolo dado
 *   - respetar su propio estado de fijado (una rueda fija no gira)
 *   - visualizar su movimiento paso a paso cuando está visible
 *
 * Reutiliza los componentes gráficos del paquete shapes (Circle y
 * Rectangle) para su representación visual: un marco (Rectangle) y
 * el símbolo (Circle) dentro de él. Detrás del marco hay un segundo
 * rectángulo rojo, apenas más grande, que sólo se dibuja cuando la
 * rueda está fija y que asoma alrededor del marco.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class Wheel{

    private static final int SIZE = 50;
    private static final int MARGIN = 10;
    private static final int LOCK_MARGIN = 6;
    private static final int Y_POSITION = 60;
    private static final int STEP_DELAY = 120;

    private ArrayList<Symbol> catalog;
    private Symbol current;
    private Rectangle lockFrame;
    private Rectangle frame;
    private Circle symbol;
    private Random random;
    private int currentIndex;
    private boolean visible;
    private boolean locked;

    /**
     * Crea una rueda que toma sus símbolos del catálogo indicado, en la
     * posición horizontal dada. La rueda queda mostrando el primer
     * símbolo del catálogo, o vacía si el catálogo aún no tiene ninguno.
     * @param catalog   catálogo de símbolos de la máquina a la que pertenece
     * @param xPosition posición horizontal inicial de la rueda
     */
    public Wheel(ArrayList<Symbol> catalog, int xPosition){
        this.catalog = catalog;
        current = null;
        currentIndex = -1;
        visible = false;
        locked = false;
        random = new Random();
        lockFrame = new Rectangle();
        lockFrame.changeSize(SIZE + MARGIN + LOCK_MARGIN, SIZE + MARGIN + LOCK_MARGIN);
        lockFrame.changeColor("red");
        frame = new Rectangle();
        frame.changeSize(SIZE + MARGIN, SIZE + MARGIN);
        frame.changeColor("gainsboro");
        symbol = new Circle();
        symbol.changeSize(SIZE);
        symbol.changeColor("white");
        reposition(xPosition);
        refresh();
    }

    /**
     * Reubica la rueda en una nueva posición horizontal. Se usa
     * cuando se agregan o eliminan ruedas y las demás deben
     * desplazarse para mantenerse ordenadas de izquierda a derecha.
     * @param x nueva posición horizontal
     */
    public void reposition(int x){
        lockFrame.moveTo(x - LOCK_MARGIN/2, Y_POSITION - MARGIN/2 - LOCK_MARGIN/2);
        frame.moveTo(x, Y_POSITION - MARGIN/2);
        symbol.moveTo(x + MARGIN/2, Y_POSITION);
    }

    /**
     * Gira la rueda: escoge al azar uno de los símbolos del catálogo y
     * lo deja a la vista. Una rueda fija no gira.
     * @return true si la rueda giró; false si está fija o si el
     *         catálogo no tiene símbolos
     */
    public boolean spin(){
        if(locked || catalog.isEmpty()) return false;
        show(random.nextInt(catalog.size()));
        return true;
    }

    /**
     * Rota la rueda el número de pasos indicado dentro del catálogo:
     * avanza con pasos positivos, retrocede con pasos negativos y da la
     * vuelta de forma cíclica. Si la rueda está visible, el movimiento
     * se ve paso a paso, mostrando cada símbolo intermedio. Una rueda
     * fija no rota.
     * @param steps cantidad de pasos a rotar (puede ser negativo)
     * @return true si la rueda rotó; false si está fija o si el
     *         catálogo no tiene símbolos
     */
    public boolean spin(int steps){
        if(locked || catalog.isEmpty()) return false;
        int index = (current == null) ? 0 : catalog.indexOf(current);
        int direction = (steps >= 0) ? 1 : -1;
        int remaining = Math.abs(steps);
        for(int i = 0; i < remaining; i++){
            index = Math.floorMod(index + direction, catalog.size());
            show(index);
            pauseStep();
        }
        return true;
    }

    /**
     * Deja a la vista el símbolo indicado, siempre que exista en el
     * catálogo. Una rueda fija no cambia de símbolo.
     * @param color color del símbolo que debe quedar visible
     * @return true si la rueda no está fija, el símbolo existe en el
     *         catálogo y quedó ubicado
     */
    public boolean place(String color){
        if(locked) return false;
        for(int i = 0; i < catalog.size(); i++){
            if(catalog.get(i).hasColor(color)){
                show(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Consulta el símbolo que la rueda está mostrando.
     * @return el color visible en la rueda, o null si no tiene símbolo
     */
    public String currentSymbol(){
        return (current == null) ? null : current.getColor();
    }

    /**
     * Consulta si la rueda está mostrando el símbolo dado.
     * @param aSymbol símbolo a comparar
     * @return true si la rueda muestra exactamente ese símbolo
     */
    public boolean isShowing(Symbol aSymbol){
        return current != null && current == aSymbol;
    }

    /**
     * Sincroniza la rueda con el catálogo después de que la máquina
     * agregue o elimine símbolos. Si el símbolo que muestra sigue en el
     * catálogo, lo conserva (aunque haya cambiado de posición). Si fue
     * eliminado, muestra el que quedó en su antigua posición (ajustada
     * al rango). Si el catálogo quedó vacío, la rueda queda sin símbolo.
     */
    public void refresh(){
        if(catalog.isEmpty()){
            show(-1);
            return;
        }
        int index = (current == null) ? -1 : catalog.indexOf(current);
        if(index == -1) index = currentIndex;
        if(index < 0) index = 0;
        if(index >= catalog.size()) index = catalog.size() - 1;
        show(index);
    }

    /**
     * Fija la rueda: mientras está fija no cambia su símbolo al girar.
     */
    public void lock(){
        locked = true;
        restack();
    }

    /**
     * Suelta la rueda: vuelve a poder girar normalmente.
     */
    public void unlock(){
        locked = false;
        restack();
    }

    /**
     * Consulta si la rueda está fija.
     * @return true si la rueda está actualmente fija
     */
    public boolean isLocked(){
        return locked;
    }

    /**
     * Hace visible la rueda en el lienzo: dibuja tanto su marco como
     * el símbolo que muestra actualmente.
     */
    public void makeVisible(){
        visible = true;
        frame.makeVisible();
        symbol.makeVisible();
        restack();
    }

    /**
     * Hace invisible la rueda: la borra del lienzo sin alterar el
     * símbolo que tiene asignado ni su estado de fijado.
     */
    public void makeInvisible(){
        lockFrame.makeInvisible();
        frame.makeInvisible();
        symbol.makeInvisible();
        visible = false;
    }

    /**
     * Consulta el estado de visibilidad de la rueda.
     * @return true si la rueda está actualmente visible en el lienzo
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * Resalta visualmente la rueda (usado cuando la máquina llega
     * a un estado ganador).
     * @param highlighted true para resaltar, false para el estado normal
     */
    public void highlight(boolean highlighted){
        frame.changeColor(highlighted ? "gold" : "gainsboro");
        restack();
    }

    /**
     * Vuelve a dibujar las tres piezas de la rueda en su orden correcto:
     * primero el cuadro rojo que indica que está fija (sólo si lo está),
     * encima el marco y de último el símbolo, que siempre queda arriba.
     */
    private void restack(){
        if(locked && visible){
            lockFrame.makeVisible();
            lockFrame.changeColor("red");
        } else {
            lockFrame.makeInvisible();
        }
        frame.changeColor(frame.getColor());
        symbol.changeColor(symbol.getColor());
    }

    /**
     * Deja a la vista el símbolo que está en el índice indicado del
     * catálogo. Un índice fuera de rango deja la rueda en blanco.
     * @param index posición del símbolo dentro del catálogo
     */
    private void show(int index){
        currentIndex = index;
        boolean inRange = index >= 0 && index < catalog.size();
        current = inRange ? catalog.get(index) : null;
        String color = currentSymbol();
        symbol.changeColor(color == null ? "white" : color);
    }

    /**
     * Introduce una pausa breve entre dos pasos consecutivos de una
     * rotación, para que el movimiento sea perceptible. Sólo tiene
     * efecto cuando la rueda está visible.
     */
    private void pauseStep(){
        if(!visible) return;
        Canvas.getCanvas().wait(STEP_DELAY);
    }
}
