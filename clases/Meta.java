import javax.swing.*;
import java.awt.*;

public class Meta extends Entidad {
    private Image imagen;
    boolean esInvisible;

    public Meta(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(rutaImagen).getImage();
    }

    public boolean plataformaEsVisible() {
        return getEsVisible();
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, ancho, alto, null);
    }
}