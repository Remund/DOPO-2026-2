import java.awt.*;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * Extendida con moveTo(x,y) y getters para el proyecto slotMachine.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.1 (slotMachine)
 */
public class Circle{

    public static final double PI=3.1416;

    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    public Circle(){
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }

    public void makeVisible(){
        isVisible = true;
        draw();
    }

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition,
                diameter, diameter));
            canvas.wait(10);
        }
    }

    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    public void moveRight(){ moveHorizontal(20); }
    public void moveLeft(){ moveHorizontal(-20); }
    public void moveUp(){ moveVertical(-20); }
    public void moveDown(){ moveVertical(20); }

    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve el círculo a una posición absoluta.
     * Extensión del paquete shapes para posicionar figuras directamente.
     * @param x nueva coordenada horizontal
     * @param y nueva coordenada vertical
     */
    public void moveTo(int x, int y){
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    public void slowMoveHorizontal(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }
        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    public void slowMoveVertical(int distance){
        int delta;
        if(distance < 0) { delta = -1; distance = -distance; }
        else { delta = 1; }
        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }

    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    public int getDiameter(){ return diameter; }
    public int getXPosition(){ return xPosition; }
    public int getYPosition(){ return yPosition; }
    public String getColor(){ return color; }
    public boolean isVisible(){ return isVisible; }
}