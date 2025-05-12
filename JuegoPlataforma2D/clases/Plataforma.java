package clases;

import javax.swing.*;
import java.awt.*;

public class Plataforma extends Entidad {
    private Image imagen;
    public Plataforma(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(rutaImagen).getImage();
    }

    public void dibujar(Graphics g) {
        if (imagen != null) {
            g.drawImage(imagen, x, y, ancho, alto, null);
        }else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, ancho, alto);
        }
    }
}
