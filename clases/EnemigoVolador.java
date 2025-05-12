package clases;

import java.awt.*;

public class EnemigoVolador extends Enemigo {
    private int dy = 2; // velocidad vertical
    private int limiteArriba = 100; // límite superior del movimiento
    private int limiteAbajo = 300; // límite inferior del movimiento
    private int xOriginal = x;
    private int yOriginal = y;

    public EnemigoVolador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void actualizar() {
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
        g.setColor(Color.MAGENTA);
        g.fillOval(x, y, ancho, alto);
    }
}
