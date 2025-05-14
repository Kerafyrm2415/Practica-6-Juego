import javax.swing.*;
import java.awt.*;

public class Champiñon extends Entidad {
    private Image imagen;

    public Champiñon(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon("recursos/champiñon.png").getImage();
    }

    @Override
    public void dibujar(Graphics g) {
            g.drawImage(imagen, x, y, ancho, alto, null);
    }

    public void setPosicion(int nuevaX, int nuevaY) {
        x = nuevaX;
        y = nuevaY;
    }
}