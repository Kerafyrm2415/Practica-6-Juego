package clases;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Jugador extends Entidad {
    public interface GamePanelListener {
        void mostrarMensajeMuerte();
    }
    private int dy = 0, xOriginal, yOriginal;
    private boolean izquierda = false, derecha = false, enSuelo = false;
    private boolean puedeDobleSalto, sonidoReproducido = false;
    private GameListener listener;
    private GamePanelListener panelListener;
    private Image imagen, imagenIzquierda, imagenDerecha, imagenSalto, imagenIdle;
    private Meta ultimaMetaTocada;

    private GamePanel panel;


    public Jugador(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        this.xOriginal = x;
        this.yOriginal = y;
        imagen = new ImageIcon(rutaImagen).getImage();
        imagenIdle = new ImageIcon("recursos/avatar.png").getImage();
        imagenDerecha = new ImageIcon("recursos/avatar_derecha.png").getImage();
        imagenIzquierda = new ImageIcon("recursos/avatar_izquierda.png").getImage();
        imagenSalto = new ImageIcon("recursos/avatar.png").getImage();
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void actualizar() {
        if (izquierda) {
            x -= 4;
            imagen = imagenIzquierda;
        }
        if (derecha) {
            x += 4;
            imagen = imagenDerecha;
        }
        if (izquierda == derecha) {
            imagen = imagenIdle;
        }
        dy += 1;
        y += dy;
        if (y > 800) {
            reiniciarPosicion();
        }
    }

    public void resbalar(boolean hielo) {
        if(hielo==true) {
            if (izquierda) {
                x -= 9;
                imagen = imagenIzquierda;
            }
            if (derecha) {
                x += 9;
                imagen = imagenDerecha;
            }
            if (izquierda == derecha) {
                imagen = imagenIdle;
            }
            dy += 1;
            y += dy;
        }
    }

    public void verificarColisiones(List<Entidad> entidades) {
        enSuelo = false;
        for (Entidad e : entidades) {
            if (e instanceof Plataforma plataforma && getRect().intersects(e.getRect())) {
                Rectangle rJugador = getRect();
                Rectangle rEntidad = e.getRect();

                // Viene cayendo
                if (dy >= 0 && rJugador.y + alto - dy <= rEntidad.y) {
                    y = rEntidad.y - alto;
                    dy = 0;
                    enSuelo = true;
                    puedeDobleSalto = true;
                    sonidoReproducido = false;
                    if (plataforma.esMovil()) {
                        x += plataforma.getDX(); // ¡solo si es móvil!
                    }
                }
            }
            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                reiniciarPosicion();
                if (panelListener != null) {
                    panelListener.mostrarMensajeMuerte();
                }
            }
            if (e instanceof Limite && getRect().intersects(e.getRect())) {
                // Teletransporte horizontal
                if (e.getRect().width <= 25 && e.getRect().height >= 600) { // pared delgada y alta
                    if (e.getRect().x <= 0) {
                        x = 680; // Aparece casi del lado derecho
                    } else if (e.getRect().x >= 780) {
                        x = 65;   // Aparece casi del lado izquierdo
                    }
                }

                // clases.Limite superior (techo)
                if (e.getRect().height <= 25 && dy < 0) { // techo delgado
                    y = e.getRect().y + e.getRect().height;
                    dy = 0;
                }
            }
            if (e instanceof Meta meta && getRect().intersects(e.getRect())) {
                if (!sonidoReproducido && meta.esVisible()) {
                    meta.tocarMeta(); // Oculta la meta
                    meta.reproducirSonidoMeta();
                    ultimaMetaTocada = meta; // Guardamos la última meta tocada
                    if (listener != null) listener.nivelCompletado();
                    sonidoReproducido = true;
                }
            }
            if (e instanceof Champiñon && getRect().intersects(e.getRect())) {
                saltar(true);
            }
        }
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }
    public void setPanelListener(GamePanelListener listener) {
        this.panelListener = listener;
    }

    public interface GameListener {
        void nivelCompletado();
    }


    public void reiniciarPosicion() {
        x = xOriginal;
        y = (yOriginal-40);
        dy = 0;
        enSuelo=true;
    }
    public void setPosicion(int nuevaX, int nuevaY) {
        x = nuevaX;
        y = nuevaY;
        xOriginal = nuevaX;
        y = nuevaY;
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, ancho, alto, null);
    }

    public void saltar(boolean saltoChampiñon) {
        if (enSuelo) {
            dy = -17;
            puedeDobleSalto = true; // Se habilita el doble salto después de un salto normal
            imagen = imagenSalto;
        } else if (puedeDobleSalto) {
            dy = -13;
            puedeDobleSalto = false; // Ya no puede volver a hacer doble salto
            imagen = imagenSalto;
        }
        if (saltoChampiñon) {
            dy = -25;
            puedeDobleSalto = true;
            imagen = imagenSalto;
        }
    }
    public Meta getUltimaMetaTocada() {
        return ultimaMetaTocada;
    }

    public void setIzquierda(boolean b) { izquierda = b; }
    public void setDerecha(boolean b) { derecha = b; }

}