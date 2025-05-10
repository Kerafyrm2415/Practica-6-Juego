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
                // Teletransporte horizontal
                if (e.getRect().width <= 25 && e.getRect().height >= 600) { // pared delgada y alta
                    if (e.getRect().x <= 0) {
                        x = 780; // Aparece casi del lado derecho
                    } else if (e.getRect().x >= 780) {
                        x = 5;   // Aparece casi del lado izquierdo
                    }
                }

                // Limite superior (techo)
                if (e.getRect().height <= 25 && dy < 0) { // techo delgado
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

    public void setDy(int dy) {
        this.dy = dy;
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
