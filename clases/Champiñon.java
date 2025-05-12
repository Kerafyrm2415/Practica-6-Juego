package clases;

import javax.swing.*;
import java.awt.*;

public class Champiñon extends Entidad {
    private Image imagen;
    boolean esInvisible;

    public Champiñon(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon("recursos/champiñon.jpg").getImage();
    }

    public boolean plataformaEsVisible() {
        return getEsVisible();
    }



    @Override
    public void dibujar(Graphics g) {
            g.drawImage(imagen, x, y, ancho, alto, null);
        }
}
