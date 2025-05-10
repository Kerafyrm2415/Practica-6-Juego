import java.awt.*;
import java.util.List;

public class Jugador extends Entidad {
    private int dy = 0;
    private boolean izquierda = false, derecha = false, enSuelo = false;
    private boolean dobleSalto = true, puedeDobleSalto, sonidoReproducido = false;
    private GameListener listener;

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void actualizar() {
        if (izquierda) x -= 4;
        if (derecha) x += 4;
        dy += 1;
        y += dy;
    }

    public void verificarColisiones(List<Entidad> entidades) {
        enSuelo = false;
        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                y = e.getRect().y - alto;
                dy = 0;
                enSuelo = true;
                puedeDobleSalto = true;
                sonidoReproducido = false;
            }
            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                x = 50; y = 500; dy = 0;
            }
            if (e instanceof Limite && getRect().intersects(e.getRect())) {
                // Si viene subiendo (dy negativo), lo detienes justo debajo del techo
                if (dy < 0) {
                    y = e.getRect().y + e.getRect().height;
                    dy = 0;
                }
            }
            if (e instanceof Meta && getRect().intersects(e.getRect())) {
                if (!sonidoReproducido) {
                    if (listener != null) listener.nivelCompletado(); // si usas listener
                    sonidoReproducido = true;
                }
            }
        }
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public interface GameListener {
        void nivelCompletado();
    }

    public void reiniciarPosicion(boolean sePresionoTeclaR) {
        if (sePresionoTeclaR) {
            x = 50; y = 500; dy = 0;
        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, ancho, alto);
    }

    public void saltar() {
        if (enSuelo) {
            dy = -15;
            puedeDobleSalto = true; // Se habilita el doble salto después de un salto normal
        } else if (puedeDobleSalto) {
            dy = -15;
            puedeDobleSalto = false; // Ya no puede volver a hacer doble salto
        }
    }

    //Saltos infinitos
//    public void dobleSalto() {
//        if (dobleSalto = true) {
//            dy = -15;
//        }
//        dobleSalto = false;
//    }

    public void setIzquierda(boolean b) { izquierda = b; }
    public void setDerecha(boolean b) { derecha = b; }

    public int getX() { return x; }
    public int getY() { return y; }
}
