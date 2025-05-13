package clases;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EnemigoTerrestre extends Enemigo {
    private int dx = 2; // velocidad horizontal
    private int limiteIzq = 95; // límite izquierdo del patrullaje
    private int limiteDer = 705; // límite derecho del patrullaje
    private int xOriginal = x;
    private int yOriginal = y;
    private Image imagen;

    public void actualizar() {
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
            if (e instanceof EnemigoTerrestre) {
                dx *= -1;
                x += dx * 2;
            }
            if (e instanceof Limite) {
                dx *= -1;
                x += dx * 2;
            }
        }
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

    public void setPosicionesOrignales(int xOriginal, int yOriginal) {
        this.xOriginal = xOriginal;
        this.yOriginal = yOriginal;
    }
}