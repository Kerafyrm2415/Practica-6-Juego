import java.awt.*;
import javax.swing.*;

public class EnemigoVolador extends Enemigo {
    private int dy = 2, dx=2; // velocidad vertical
    private int limiteArriba = 100; // límite superior del movimiento
    private int limiteAbajo = 300; // límite inferior del movimiento
    private int limiteIzq = 5; // límite izquierdo del patrullaje
    private int limiteDer = 600;
    private int xOriginal = x;
    private int yOriginal = y;
    private Image imagen;

    public EnemigoVolador(int x, int y, int ancho, int alto, String RutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(RutaImagen).getImage();
    }

    public void vueloVertical() {
        y += dy;  // Mueve al enemigo verticalmente

        // Si el enemigo llega al límite superior, cambia la dirección a hacia abajo
        if (y <= limiteArriba) {
            dy = Math.abs(dy);  // Asegúrate de que la dirección sea hacia abajo
        }
        // Si el enemigo llega al límite inferior, cambia la dirección a hacia arriba
        else if (y + alto >= limiteAbajo) {
            dy = -Math.abs(dy);  // Asegúrate de que la dirección sea hacia arriba
        }
    }

    public void vueloHorizontal() {
        x += dx;

        if (x <= limiteIzq || x + ancho >= limiteDer) {
            dx *= -1; // cambia de dirección
        }
    }

    public void reiniciarPosicion() {
        x = xOriginal; y = yOriginal;
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen,x, y, ancho, alto,null);
    }

    public void actualizar(int nivel) {
        if (nivel == 1) {
            imagen = new ImageIcon("recursos/temmie.png").getImage();
        }
        else if (nivel == 2) {
            imagen = new ImageIcon("recursos/temmie.png").getImage();
        }
        else if (nivel == 3) {
            imagen = new ImageIcon("recursos/temmie.png").getImage();
        }
    }

    public void setPosicion(int nuevaX, int nuevaY) {
        x = nuevaX;
        y = nuevaY;
    }

}