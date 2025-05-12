package clases;

import javax.swing.*;
import java.awt.*;

public class Meta extends Entidad {
    private Image imagen;

    public Meta(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(rutaImagen).getImage();
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, ancho, alto, null);
    }
}