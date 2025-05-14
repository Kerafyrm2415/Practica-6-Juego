package clases;

import java.awt.*;

public abstract class Entidad {
    protected int x, y, ancho, alto;
    protected boolean esVisible;

    public Entidad(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, ancho, alto);
    }
    public boolean getEsVisible(){
        return esVisible;
    }
    public abstract void dibujar(Graphics g);
}