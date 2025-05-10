import java.awt.*;

public class Limite extends Entidad {
    public Limite(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void dibujar(Graphics g) {
        g.fillRect(x, y, ancho, alto);
    }
}
