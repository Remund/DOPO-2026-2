import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * SlotMachine simula una máquina tragamonedas. La máquina mantiene
 * un catálogo de símbolos (Symbol, todos de colores diferentes) y un
 * conjunto de ruedas (Wheel). Todas las ruedas comparten el catálogo,
 * es decir, tienen los mismos símbolos en el mismo orden; cada rueda
 * apunta al símbolo que muestra en un momento dado.
 *
 * Desde el ciclo 3 la máquina también se usa como "testing tool" del
 * problema de la maratón (SlotMachine(n), spin(wheel, steps) y
 * distinctSymbols()) y como simulador de la solución (SlotMachineContest).
 *
 * Reglas generales de posición: las posiciones se enumeran a partir
 * de 1; si la posición es menor a 1 se usa 1; si es mayor al máximo
 * de elementos posibles se usa el máximo.
 *
 * Usabilidad: cuando el simulador está visible y el usuario ingresa
 * un color o símbolo inválido, se le presenta un selector desplegable
 * (JOptionPane) con las opciones válidas disponibles.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class SlotMachine{

    private static final int BASE_X = 30;
    private static final int SPACING = 70;
    private static final int BODY_Y = 30;
    private static final int BODY_HEIGHT = 100;
    private static final String[] RESERVED_COLORS =
        {"white", "gainsboro", "gold", "dimgray", "red"};

    private ArrayList<Symbol> catalog;
    private ArrayList<Wheel> wheels;
    private Rectangle body;
    private boolean visible;
    private boolean ok;

    /**
     * Crea una máquina tragamonedas vacía (sin ruedas ni símbolos),
     * inicialmente invisible.
     */
    public SlotMachine(){
        catalog = new ArrayList<Symbol>();
        wheels = new ArrayList<Wheel>();
        visible = false;
        ok = true;
        body = new Rectangle();
        body.changeSize(BODY_HEIGHT, SPACING);
        body.changeColor("dimgray");
        body.moveTo(BASE_X - 20, BODY_Y);
    }

    /**
     * Crea una máquina de n ruedas y n símbolos (de colores diferentes,
     * escogidos al azar), inicializada aleatoriamente: cada rueda queda
     * mostrando un símbolo al azar. Como en la maratón, la configuración
     * inicial nunca es ganadora (si n es mayor que 1). La máquina queda
     * invisible.
     * @param n número de ruedas y de símbolos (se ajusta a 1..colores disponibles)
     */
    public SlotMachine(int n){
        this();
        String[] colors = randomColors(limitPosition(n, 1, maxSymbols()));
        for(int i = 0; i < colors.length; i++){
            catalog.add(new Symbol(colors[i]));
            wheels.add(new Wheel(catalog, 0));
        }
        repositionWheels();
        do{
            for(Wheel w : wheels) w.spin();
        } while(wheels.size() > 1 && isJackpot());
        updateAppearance();
    }

    /**
     * Agrega una rueda vacía en la posición indicada.
     * @param pos posición (1..numRuedas+1) donde se inserta la rueda
     */
    public void addWheel(int pos){
        int insertPos = limitPosition(pos, 1, wheels.size() + 1);
        Wheel wheel = new Wheel(catalog, 0);
        wheels.add(insertPos - 1, wheel);
        repositionWheels();
        updateAppearance();
        if(visible) wheel.makeVisible();
        reportSuccess();
    }

    /**
     * Elimina la rueda ubicada en la posición indicada. No se puede
     * eliminar una rueda fija: primero hay que soltarla con unlock().
     * @param pos posición (1..numRuedas) de la rueda a eliminar
     */
    public void delWheel(int pos){
        if(wheels.isEmpty()){
            reportError("No hay ruedas para eliminar.");
            return;
        }
        int delPos = limitPosition(pos, 1, wheels.size());
        if(wheels.get(delPos - 1).isLocked()){
            reportError("La rueda está fija; suéltala antes de eliminarla.");
            return;
        }
        Wheel wheel = wheels.remove(delPos - 1);
        wheel.makeInvisible();
        repositionWheels();
        updateAppearance();
        reportSuccess();
    }

    /**
     * Intercambia dos ruedas de la máquina. No se copian atributos de
     * una rueda a otra: lo que se intercambia son las ruedas mismas
     * dentro de la lista, de modo que cada una se lleva consigo todo
     * lo que conoce de sí misma. Después sólo se reubican en pantalla.
     * Ninguna de las dos ruedas puede estar fija: una rueda fija no
     * puede moverse de posición, así que si cualquiera de las dos lo
     * está, la operación falla y no se modifica nada.
     * @param wheel1 posición (1..numRuedas) de la primera rueda
     * @param wheel2 posición (1..numRuedas) de la segunda rueda
     */
    public void swap(int wheel1, int wheel2){
        if(wheels.size() < 2){
            reportError("No hay suficientes ruedas para intercambiar.");
            return;
        }
        int i1 = limitPosition(wheel1, 1, wheels.size()) - 1;
        int i2 = limitPosition(wheel2, 1, wheels.size()) - 1;

        if(wheels.get(i1).isLocked() || wheels.get(i2).isLocked()){
            reportError("No se puede intercambiar: una de las ruedas está fija.");
            return;
        }

        Wheel tmp = wheels.get(i1);
        wheels.set(i1, wheels.get(i2));
        wheels.set(i2, tmp);

        repositionWheels();
        updateAppearance();
        reportSuccess();
    }

    /**
     * Fija una rueda en su símbolo actual: mientras esté fija, no
     * cambiará de símbolo al girar la máquina (con spin).
     * @param wheel posición (1..numRuedas) de la rueda a fijar
     */
    public void lock(int wheel){
        if(wheels.isEmpty()){
            reportError("No hay ruedas en la máquina.");
            return;
        }
        int wheelPos = limitPosition(wheel, 1, wheels.size());
        wheels.get(wheelPos - 1).lock();
        updateAppearance();
        reportSuccess();
    }

    /**
     * Suelta una rueda previamente fijada, para que vuelva a poder
     * girar normalmente.
     * @param wheel posición (1..numRuedas) de la rueda a soltar
     */
    public void unlock(int wheel){
        if(wheels.isEmpty()){
            reportError("No hay ruedas en la máquina.");
            return;
        }
        int wheelPos = limitPosition(wheel, 1, wheels.size());
        wheels.get(wheelPos - 1).unlock();
        updateAppearance();
        reportSuccess();
    }

    /**
     * Agrega un símbolo (color) al catálogo de la máquina. No se admiten
     * dos símbolos del mismo color. Agregar un símbolo no cambia lo que
     * muestran las ruedas. Si el color es inválido y la máquina está visible, se abre un selector
     * desplegable con los colores CSS disponibles.
     * @param pos   posición (1..numSímbolos+1) dentro del catálogo
     * @param color nombre del color (estándar CSS)
     */
    public void addSymbol(int pos, String color){
        String target = Symbol.normalize(color);

        if(!Canvas.isValidColor(target)){
            target = chooseColor(color);
            if(target == null){
                reportError("El color '" + color + "' no es un color CSS válido.");
                return;
            }
        }

        if(findSymbol(target) != null){
            reportError("Ya existe un símbolo de color '" + target + "': los símbolos deben ser de colores diferentes.");
            return;
        }
        int insertPos = limitPosition(pos, 1, catalog.size() + 1);
        catalog.add(insertPos - 1, new Symbol(target));
        refreshAllWheels();
        reportSuccess();
    }

    /**
     * Elimina del catálogo el símbolo indicado. No se puede eliminar un
     * símbolo que esté mostrando una rueda fija (eso cambiaría la rueda).
     * @param symbol color del símbolo a eliminar
     */
    public void delSymbol(String symbol){
        Symbol target = findSymbol(symbol);
        if(target == null){
            reportError("El símbolo '" + symbol + "' no existe en la máquina.");
            return;
        }
        for(Wheel w : wheels){
            if(w.isLocked() && w.isShowing(target)){
                reportError("Una rueda fija muestra '" + symbol + "'; suéltala antes de eliminarlo.");
                return;
            }
        }
        catalog.remove(target);
        refreshAllWheels();
        reportSuccess();
    }

    /**
     * Ubica manualmente un símbolo del catálogo en una rueda específica.
     * Si el símbolo no existe y la máquina está visible, se abre un
     * selector desplegable con los símbolos actualmente en el catálogo.
     * No se puede ubicar un símbolo en una rueda fija.
     * @param wheel  posición (1..numRuedas) de la rueda
     * @param symbol color del símbolo a ubicar
     */
    public void placeSymbol(int wheel, String symbol){
        if(wheels.isEmpty()){
            reportError("No hay ruedas en la máquina.");
            return;
        }

        int wheelPos = limitPosition(wheel, 1, wheels.size());
        Wheel targetWheel = wheels.get(wheelPos - 1);
        if(targetWheel.isLocked()){
            reportError("La rueda está fija; suéltala antes de cambiar su símbolo.");
            return;
        }

        String target = Symbol.normalize(symbol);
        if(findSymbol(target) == null){
            target = chooseSymbol(symbol);
        }

        if(!targetWheel.place(target)){
            reportError("El símbolo '" + symbol + "' no existe en el catálogo.");
            return;
        }
        updateAppearance();
        reportSuccess();
    }

    /**
     * Gira (al azar) una única rueda de la máquina.
     * @param wheel posición (1..numRuedas) de la rueda a girar
     */
    public void spin(int wheel){
        if(wheels.isEmpty() || catalog.isEmpty()){
            reportError("No es posible girar: faltan ruedas o símbolos.");
            return;
        }
        int wheelPos = limitPosition(wheel, 1, wheels.size());
        if(!wheels.get(wheelPos - 1).spin()){
            reportError("La rueda está fija; suéltala antes de girarla.");
            return;
        }
        updateAppearance();
        reportSuccess();
    }

    /**
     * Gira (al azar) todas las ruedas de la máquina que no estén
     * fijas. Las ruedas fijas mantienen su símbolo actual.
     */
    public void spin(){
        if(wheels.isEmpty() || catalog.isEmpty()){
            reportError("No es posible girar: faltan ruedas o símbolos.");
            return;
        }
        for(Wheel w : wheels){
            w.spin();
        }
        updateAppearance();
        reportSuccess();
    }

    /**
     * Le pide a una rueda que rote el número de pasos indicado. Es la
     * rueda la que sabe cómo rotar (incluida la visualización paso a
     * paso exigida por el requisito de usabilidad); la máquina sólo
     * escoge a cuál rueda pedírselo y reporta el resultado.
     * @param wheel posición (1..numRuedas) de la rueda a rotar
     * @param steps cantidad de pasos a rotar (puede ser negativo)
     */
    public void spin(int wheel, int steps){
        if(wheels.isEmpty() || catalog.isEmpty()){
            reportError("No es posible rotar: faltan ruedas o símbolos.");
            return;
        }
        int wheelPos = limitPosition(wheel, 1, wheels.size());
        if(!wheels.get(wheelPos - 1).spin(steps)){
            reportError("La rueda está fija; suéltala antes de rotarla.");
            return;
        }
        updateAppearance();
        reportSuccess();
    }

    /**
     * Deja la máquina en la configuración de símbolos dada: cada
     * posición del arreglo indica el color que debe quedar visible
     * en la rueda correspondiente (misma cantidad y orden que las
     * ruedas de la máquina). Si algún color no existe en el catálogo,
     * si la cantidad no coincide con el número de ruedas, o si alguna
     * de las ruedas involucradas está fija, la operación falla y no
     * se modifica nada.
     * @param setSymbols colores deseados, uno por cada rueda, en orden
     */
    public void spin(String[] setSymbols){
        String[] colors = setSymbols;
        if(colors == null || colors.length != wheels.size()){
            reportError("La configuración debe tener exactamente un color por cada rueda.");
            return;
        }

        String[] targets = new String[colors.length];
        for(int i = 0; i < colors.length; i++){
            if(wheels.get(i).isLocked()){
                reportError("No se puede aplicar la configuración: la rueda " + (i + 1) + " está fija.");
                return;
            }
            targets[i] = Symbol.normalize(colors[i]);
            if(findSymbol(targets[i]) == null){
                reportError("El símbolo '" + colors[i] + "' no existe en el catálogo.");
                return;
            }
        }

        for(int i = 0; i < targets.length; i++){
            wheels.get(i).place(targets[i]);
        }
        updateAppearance();
        reportSuccess();
    }

    /**
     * Consulta el catálogo de símbolos de la máquina.
     * @return los colores de los símbolos del catálogo, en el orden
     *         en que están definidos, comenzando por la posición 1.
     */
    public String[] symbols(){
        String[] colors = new String[catalog.size()];
        for(int i = 0; i < catalog.size(); i++){
            colors[i] = catalog.get(i).getColor();
        }
        return colors;
    }

    /**
     * Cuenta cuántos símbolos diferentes se ven en este momento en las
     * ruedas de la máquina (es el valor k que reporta la "amiga" en el
     * problema de la maratón). Las ruedas sin símbolo no cuentan.
     * @return la cantidad de símbolos distintos visibles en las ruedas
     */
    public int distinctSymbols(){
        HashSet<String> seen = new HashSet<String>();
        for(String color : configuration()){
            if(color != null) seen.add(color);
        }
        return seen.size();
    }

    /**
     * Consulta la configuración actual de la máquina.
     * @return los colores actualmente visibles en todas las ruedas de
     *         la máquina, ordenados de izquierda a derecha
     */
    public String[] configuration(){
        String[] config = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            config[i] = wheels.get(i).currentSymbol();
        }
        return config;
    }

    /**
     * Indica si la máquina está en una configuración ganadora.
     * @return true si la configuración actual es ganadora (todas las
     *         ruedas muestran el mismo símbolo y hay al menos una rueda)
     */
    public boolean isJackpot(){
        String[] config = configuration();
        if(config.length == 0) return false;
        for(String s : config){
            if(s == null || !s.equals(config[0])) return false;
        }
        return true;
    }

    /**
     * Hace visible el simulador en pantalla.
     */
    public void makeVisible(){
        visible = true;
        body.makeVisible();
        for(Wheel w : wheels) w.makeVisible();
        updateAppearance();
    }

    /**
     * Hace invisible el simulador (sigue funcionando internamente).
     */
    public void makeInvisible(){
        body.makeInvisible();
        for(Wheel w : wheels) w.makeInvisible();
        visible = false;
    }

    /**
     * Cierra el simulador. Borra las figuras del lienzo, cierra su
     * ventana y termina la ejecución: no hay forma de volver a poner el
     * simulador en uso, así que después de exit() no queda ningún objeto
     * al que pedirle nada.
     */
    public void exit(){
        if(visible){
            makeInvisible();
            Canvas.getCanvas().closeCanvas();
        }
        System.exit(0);
    }

    /**
     * Consulta el resultado de la última operación ejecutada.
     * @return true si la última operación de comando se realizó con éxito
     */
    public boolean ok(){
        return ok;
    }

    /**
     * Consulta el estado de visibilidad del simulador.
     * @return true si el simulador está actualmente visible
     */
    public boolean isVisible(){
        return visible;
    }

    /**
     * Consulta cuántas ruedas tiene la máquina.
     * @return la cantidad de ruedas que tiene actualmente la máquina
     */
    public int wheelCount(){
        return wheels.size();
    }

    /**
     * Ajusta una posición al rango válido: si es menor que el mínimo
     * se usa el mínimo, y si supera el máximo se usa el máximo.
     * @param pos posición solicitada por el usuario
     * @param min posición mínima permitida
     * @param max posición máxima permitida
     * @return la posición ya ajustada al rango
     */
    private int limitPosition(int pos, int min, int max){
        if(pos < min) return min;
        if(pos > max) return max;
        return pos;
    }

    /**
     * Reubica todas las ruedas de izquierda a derecha según su orden
     * actual y ajusta el tamaño del cuerpo de la máquina.
     */
    private void repositionWheels(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).reposition(BASE_X + i * SPACING);
        }
        int width = Math.max(1, wheels.size()) * SPACING + 20;
        body.changeSize(BODY_HEIGHT, width);
        body.moveTo(BASE_X - 20, BODY_Y);
    }

    /**
     * Sincroniza las ruedas con el catálogo después de agregar o
     * eliminar símbolos: si el catálogo quedó vacío las ruedas quedan
     * sin símbolo, y si un índice quedó fuera de rango se ajusta.
     */
    private void refreshAllWheels(){
        for(Wheel w : wheels){
            w.refresh();
        }
        updateAppearance();
    }

    /**
     * Actualiza la apariencia de las ruedas: las resalta cuando la
     * configuración actual es un jackpot.
     */
    private void updateAppearance(){
        boolean jackpot = isJackpot();
        for(Wheel w : wheels) w.highlight(jackpot);
    }

    /**
     * Abre el selector desplegable (chooser) de colores CSS disponibles.
     * Sólo se activa si la máquina está visible.
     * @param invalidColor color inválido ingresado por el usuario
     * @return el color elegido, o null si canceló o no está visible
     */
    private String chooseColor(String invalidColor){
        if(!visible) return null;
        String[] available = Canvas.getAvailableColors();
        Object selected = JOptionPane.showInputDialog(
            null,
            "El color '" + invalidColor + "' no es válido.\n" +
            "Selecciona uno de los colores CSS disponibles:",
            "Selector de Colores",
            JOptionPane.QUESTION_MESSAGE,
            null,
            available,
            available.length > 0 ? available[0] : null
        );
        return (selected == null) ? null : selected.toString();
    }

    /**
     * Abre el selector desplegable (chooser) con los símbolos que sí
     * existen actualmente en el catálogo. Sólo se activa si la máquina
     * está visible y hay símbolos cargados.
     * @param invalidSymbol símbolo inválido ingresado por el usuario
     * @return el símbolo elegido, o null si canceló
     */
    private String chooseSymbol(String invalidSymbol){
        if(!visible || catalog.isEmpty()) return null;
        String[] existing = symbols();
        Object selected = JOptionPane.showInputDialog(
            null,
            "El símbolo '" + invalidSymbol + "' no existe en el catálogo.\n" +
            "Selecciona uno de los símbolos disponibles:",
            "Selector de Símbolos",
            JOptionPane.QUESTION_MESSAGE,
            null,
            existing,
            existing[0]
        );
        return (selected == null) ? null : selected.toString();
    }

    /**
     * Busca en el catálogo el símbolo del color indicado.
     * @param color color buscado
     * @return el símbolo de ese color, o null si no existe
     */
    private Symbol findSymbol(String color){
        for(Symbol s : catalog){
            if(s.hasColor(color)) return s;
        }
        return null;
    }

    /**
     * Calcula cuántos símbolos distintos puede tener como máximo una
     * máquina creada con SlotMachine(n).
     * @return cantidad de colores CSS utilizables para los símbolos
     */
    private int maxSymbols(){
        return Canvas.getAvailableColors().length - RESERVED_COLORS.length;
    }

    /**
     * Escoge al azar la cantidad indicada de colores CSS diferentes,
     * evitando los colores que usa la máquina para dibujarse.
     * @param amount cantidad de colores a escoger
     * @return arreglo con los colores escogidos
     */
    private String[] randomColors(int amount){
        ArrayList<String> pool = new ArrayList<String>();
        for(String c : Canvas.getAvailableColors()) pool.add(c);
        for(String reserved : RESERVED_COLORS) pool.remove(reserved);
        Random random = new Random();
        String[] chosen = new String[amount];
        for(int i = 0; i < amount; i++){
            chosen[i] = pool.remove(random.nextInt(pool.size()));
        }
        return chosen;
    }

    /**
     * Registra que la última operación falló y, si el simulador está
     * visible, se lo informa al usuario en un diálogo.
     * @param message descripción del error para el usuario
     */
    private void reportError(String message){
        ok = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message,
                "Slot Machine", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Registra que la última operación se realizó con éxito.
     */
    private void reportSuccess(){
        ok = true;
    }
}
