import java.awt.*;
import javax.swing.*;

public class EnemigoVolador extends Enemigo {
    private int dy = 2; // velocidad vertical
    private int limiteArriba = 100; // límite superior del movimiento
    private int limiteAbajo = 300; // límite inferior del movimiento
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

    public void reiniciarPosicion() {
        x = xOriginal; y = yOriginal;
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen,x, y, ancho, alto,null);
    }

    public void actualizar(int nivel, String RutaImagen) {
        if (nivel == 1) {
            imagen = new ImageIcon(RutaImagen).getImage();
        }
        else if (nivel == 2) {
            imagen = new ImageIcon(RutaImagen).getImage();
        }
        else if (nivel == 3) {
            imagen = new ImageIcon(RutaImagen).getImage();
        }
    }

    public void setPosicion(int nuevaX, int nuevaY) {
        x = nuevaX;
        y = nuevaY;
    }

    public void setPosicionesOrignales(int xOriginal, int yOriginal) {
        this.xOriginal = xOriginal;
        this.yOriginal = yOriginal;
    }
}