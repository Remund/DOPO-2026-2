import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * Versión extendida para el proyecto slotMachine: se agrega soporte
 * para todos los colores estándar CSS (no sólo los 7 colores básicos),
 * ya que los símbolos de la máquina tragamonedas se identifican con
 * nombres de color CSS.
 *
 * @author  Bruce Quig, Michael Kolling (mik) - base
 * @author  Extendido para slotMachine
 * @version 2.1 (slotMachine)
 */
public class Canvas{

    private static Canvas canvasSingleton;

    /**
     * Factory method para obtener el singleton del canvas.
     */
    public static Canvas getCanvas(){
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("Slot Machine Simulator", 1000, 220,
                                         Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    /**
     * Mapa con los colores estándar CSS soportados por el canvas.
     */
    private static final Map<String, Color> CSS_COLORS = buildCssColors();

    /**
     * Construye el mapa de colores CSS (nombre -> java.awt.Color).
     * @return mapa completo de colores CSS soportados
     */
    private static Map<String, Color> buildCssColors(){
        Map<String, Color> m = new HashMap<String, Color>();
        m.put("aliceblue", new Color(0xF0F8FF));
        m.put("antiquewhite", new Color(0xFAEBD7));
        m.put("aqua", new Color(0x00FFFF));
        m.put("aquamarine", new Color(0x7FFFD4));
        m.put("azure", new Color(0xF0FFFF));
        m.put("beige", new Color(0xF5F5DC));
        m.put("bisque", new Color(0xFFE4C4));
        m.put("black", new Color(0x000000));
        m.put("blanchedalmond", new Color(0xFFEBCD));
        m.put("blue", new Color(0x0000FF));
        m.put("blueviolet", new Color(0x8A2BE2));
        m.put("brown", new Color(0xA52A2A));
        m.put("burlywood", new Color(0xDEB887));
        m.put("cadetblue", new Color(0x5F9EA0));
        m.put("chartreuse", new Color(0x7FFF00));
        m.put("chocolate", new Color(0xD2691E));
        m.put("coral", new Color(0xFF7F50));
        m.put("cornflowerblue", new Color(0x6495ED));
        m.put("cornsilk", new Color(0xFFF8DC));
        m.put("crimson", new Color(0xDC143C));
        m.put("cyan", new Color(0x00FFFF));
        m.put("darkblue", new Color(0x00008B));
        m.put("darkcyan", new Color(0x008B8B));
        m.put("darkgoldenrod", new Color(0xB8860B));
        m.put("darkgray", new Color(0xA9A9A9));
        m.put("darkgreen", new Color(0x006400));
        m.put("darkkhaki", new Color(0xBDB76B));
        m.put("darkmagenta", new Color(0x8B008B));
        m.put("darkolivegreen", new Color(0x556B2F));
        m.put("darkorange", new Color(0xFF8C00));
        m.put("darkorchid", new Color(0x9932CC));
        m.put("darkred", new Color(0x8B0000));
        m.put("darksalmon", new Color(0xE9967A));
        m.put("darkseagreen", new Color(0x8FBC8F));
        m.put("darkslateblue", new Color(0x483D8B));
        m.put("darkslategray", new Color(0x2F4F4F));
        m.put("darkturquoise", new Color(0x00CED1));
        m.put("darkviolet", new Color(0x9400D3));
        m.put("deeppink", new Color(0xFF1493));
        m.put("deepskyblue", new Color(0x00BFFF));
        m.put("dimgray", new Color(0x696969));
        m.put("dodgerblue", new Color(0x1E90FF));
        m.put("firebrick", new Color(0xB22222));
        m.put("floralwhite", new Color(0xFFFAF0));
        m.put("forestgreen", new Color(0x228B22));
        m.put("fuchsia", new Color(0xFF00FF));
        m.put("gainsboro", new Color(0xDCDCDC));
        m.put("ghostwhite", new Color(0xF8F8FF));
        m.put("gold", new Color(0xFFD700));
        m.put("goldenrod", new Color(0xDAA520));
        m.put("gray", new Color(0x808080));
        m.put("green", new Color(0x008000));
        m.put("greenyellow", new Color(0xADFF2F));
        m.put("honeydew", new Color(0xF0FFF0));
        m.put("hotpink", new Color(0xFF69B4));
        m.put("indianred", new Color(0xCD5C5C));
        m.put("indigo", new Color(0x4B0082));
        m.put("ivory", new Color(0xFFFFF0));
        m.put("khaki", new Color(0xF0E68C));
        m.put("lavender", new Color(0xE6E6FA));
        m.put("lavenderblush", new Color(0xFFF0F5));
        m.put("lawngreen", new Color(0x7CFC00));
        m.put("lemonchiffon", new Color(0xFFFACD));
        m.put("lightblue", new Color(0xADD8E6));
        m.put("lightcoral", new Color(0xF08080));
        m.put("lightcyan", new Color(0xE0FFFF));
        m.put("lightgoldenrodyellow", new Color(0xFAFAD2));
        m.put("lightgray", new Color(0xD3D3D3));
        m.put("lightgreen", new Color(0x90EE90));
        m.put("lightpink", new Color(0xFFB6C1));
        m.put("lightsalmon", new Color(0xFFA07A));
        m.put("lightseagreen", new Color(0x20B2AA));
        m.put("lightskyblue", new Color(0x87CEFA));
        m.put("lightslategray", new Color(0x778899));
        m.put("lightsteelblue", new Color(0xB0C4DE));
        m.put("lightyellow", new Color(0xFFFFE0));
        m.put("lime", new Color(0x00FF00));
        m.put("limegreen", new Color(0x32CD32));
        m.put("linen", new Color(0xFAF0E6));
        m.put("magenta", new Color(0xFF00FF));
        m.put("maroon", new Color(0x800000));
        m.put("mediumaquamarine", new Color(0x66CDAA));
        m.put("mediumblue", new Color(0x0000CD));
        m.put("mediumorchid", new Color(0xBA55D3));
        m.put("mediumpurple", new Color(0x9370DB));
        m.put("mediumseagreen", new Color(0x3CB371));
        m.put("mediumslateblue", new Color(0x7B68EE));
        m.put("mediumspringgreen", new Color(0x00FA9A));
        m.put("mediumturquoise", new Color(0x48D1CC));
        m.put("mediumvioletred", new Color(0xC71585));
        m.put("midnightblue", new Color(0x191970));
        m.put("mintcream", new Color(0xF5FFFA));
        m.put("mistyrose", new Color(0xFFE4E1));
        m.put("moccasin", new Color(0xFFE4B5));
        m.put("navajowhite", new Color(0xFFDEAD));
        m.put("navy", new Color(0x000080));
        m.put("oldlace", new Color(0xFDF5E6));
        m.put("olive", new Color(0x808000));
        m.put("olivedrab", new Color(0x6B8E23));
        m.put("orange", new Color(0xFFA500));
        m.put("orangered", new Color(0xFF4500));
        m.put("orchid", new Color(0xDA70D6));
        m.put("palegoldenrod", new Color(0xEEE8AA));
        m.put("palegreen", new Color(0x98FB98));
        m.put("paleturquoise", new Color(0xAFEEEE));
        m.put("palevioletred", new Color(0xDB7093));
        m.put("papayawhip", new Color(0xFFEFD5));
        m.put("peachpuff", new Color(0xFFDAB9));
        m.put("peru", new Color(0xCD853F));
        m.put("pink", new Color(0xFFC0CB));
        m.put("plum", new Color(0xDDA0DD));
        m.put("powderblue", new Color(0xB0E0E6));
        m.put("purple", new Color(0x800080));
        m.put("red", new Color(0xFF0000));
        m.put("rosybrown", new Color(0xBC8F8F));
        m.put("royalblue", new Color(0x4169E1));
        m.put("saddlebrown", new Color(0x8B4513));
        m.put("salmon", new Color(0xFA8072));
        m.put("sandybrown", new Color(0xF4A460));
        m.put("seagreen", new Color(0x2E8B57));
        m.put("seashell", new Color(0xFFF5EE));
        m.put("sienna", new Color(0xA0522D));
        m.put("silver", new Color(0xC0C0C0));
        m.put("skyblue", new Color(0x87CEEB));
        m.put("slateblue", new Color(0x6A5ACD));
        m.put("slategray", new Color(0x708090));
        m.put("snow", new Color(0xFFFAFA));
        m.put("springgreen", new Color(0x00FF7F));
        m.put("steelblue", new Color(0x4682B4));
        m.put("tan", new Color(0xD2B48C));
        m.put("teal", new Color(0x008080));
        m.put("thistle", new Color(0xD8BFD8));
        m.put("tomato", new Color(0xFF6347));
        m.put("turquoise", new Color(0x40E0D0));
        m.put("violet", new Color(0xEE82EE));
        m.put("wheat", new Color(0xF5DEB3));
        m.put("white", new Color(0xFFFFFF));
        m.put("whitesmoke", new Color(0xF5F5F5));
        m.put("yellow", new Color(0xFFFF00));
        m.put("yellowgreen", new Color(0x9ACD32));
        return m;
    }

    /**
     * Indica si el nombre dado corresponde a un color válido del
     * estándar CSS.
     * @param colorName nombre del color (no sensible a mayúsculas)
     * @return true si el color existe en el estándar CSS
     */
    public static boolean isValidColor(String colorName){
        return colorName != null && CSS_COLORS.containsKey(colorName.toLowerCase());
    }

    /**
     * Retorna la lista ordenada de todos los colores CSS soportados.
     * Uso interno: alimenta el selector desplegable (chooser) cuando
     * el usuario ingresa un color inválido.
     * @return arreglo de nombres de colores CSS
     */
    static String[] getAvailableColors(){
        List<String> sortedList = new ArrayList<String>(CSS_COLORS.keySet());
        Collections.sort(sortedList);
        return sortedList.toArray(new String[0]);
    }

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;

    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }

    public void setVisible(boolean visible){
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Cierra definitivamente la ventana del canvas.
     */
    public void closeCanvas(){
        frame.dispose();
    }

    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Borra del lienzo todas las figuras dibujadas hasta ahora (por
     * ejemplo, las de máquinas creadas en ejecuciones anteriores).
     * Agregado al paquete shapes en el ciclo 3 para las pruebas de
     * aceptación.
     */
    public void clear(){
        objects.clear();
        shapes.clear();
        redraw();
    }

    public void erase(Object referenceObject){
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Fija el color de dibujo actual a partir de un nombre CSS.
     * @param colorString nombre del color (estándar CSS)
     */
    public void setForegroundColor(String colorString){
        Color color = CSS_COLORS.get(colorString == null ? "" : colorString.toLowerCase());
        graphic.setColor(color != null ? color : Color.black);
    }

    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // se ignora
        }
    }

    private void redraw(){
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }

    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    private class CanvasPane extends JPanel{
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    private class ShapeDescription{
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color){
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic){
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}