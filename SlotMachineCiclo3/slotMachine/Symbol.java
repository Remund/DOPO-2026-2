/**
 * Symbol representa un símbolo de la máquina tragamonedas. Cada símbolo
 * se identifica por su color (nombre del estándar CSS) y es único dentro
 * del catálogo de la máquina: no puede haber dos símbolos del mismo color.
 *
 * Las ruedas apuntan directamente al objeto Symbol que muestran (y no a
 * una posición del catálogo); así, agregar o eliminar otros símbolos no
 * cambia lo que cada rueda está mostrando.
 *
 * @author  Mora Casas
 * @version 3.0 (ciclo 3)
 */
public class Symbol{

    private String color;

    /**
     * Crea un símbolo del color indicado.
     * @param color nombre del color CSS del símbolo
     */
    public Symbol(String color){
        this.color = normalize(color);
    }

    /**
     * Consulta el color del símbolo.
     * @return nombre del color CSS del símbolo
     */
    public String getColor(){
        return color;
    }

    /**
     * Indica si el símbolo tiene el color dado (sin importar mayúsculas
     * ni espacios alrededor).
     * @param otherColor color a comparar
     * @return true si el símbolo es de ese color
     */
    public boolean hasColor(String otherColor){
        return color.equals(normalize(otherColor));
    }

    /**
     * Normaliza un nombre de color: sin espacios alrededor y en minúsculas.
     * @param aColor nombre del color (puede ser null)
     * @return el color normalizado, o "" si era null
     */
    public static String normalize(String aColor){
        return (aColor == null) ? "" : aColor.trim().toLowerCase();
    }
}
