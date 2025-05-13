import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EnemigoTerrestre extends Enemigo {
    private int dx = 2; // velocidad horizontal
   // private int limiteIzq = 5; // límite izquierdo del patrullaje
   // private int limiteDer = 500; // límite derecho del patrullaje
    private int xOriginal = x;
    private int yOriginal = y;
    private Image imagen;

    public void actualizar(int limiteIzq, int limiteDer) {
        x += dx;

        if (x <= limiteIzq || x + ancho >= limiteDer) {
            dx *= -1; // cambia de dirección
        }
    }

    public void reiniciarPosicion() {
        x = xOriginal;
        y = yOriginal;
    }

    public EnemigoTerrestre(int x, int y, int ancho, int alto, String RutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(RutaImagen).getImage();
    }

    public void verificarColisiones(List<Entidad> entidades) {
        for (Entidad e : entidades) {
            if (e instanceof Champiñon) {
                dx *= -1; // cambia dirección si toca algo que no es Plataforma
                x += dx * 2; // lo mueve un poco en la nueva dirección para evitar "pegado"
            }
        }
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen,x, y, ancho, alto,null);
    }
}