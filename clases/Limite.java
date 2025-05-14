package clases;

import java.awt.*;

public class Limite extends Entidad {
    public Limite(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }
    boolean esTeletransporte = false;
    public boolean getTeletransporte() {
        return esTeletransporte;
    }
    public void dibujar(Graphics g) {
        g.fillRect(x, y, ancho, alto);
    }
}