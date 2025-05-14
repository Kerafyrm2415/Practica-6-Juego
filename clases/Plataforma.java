package clases;

import javax.swing.*;
import java.awt.*;

public class Plataforma extends Entidad {
    private Image imagen;
    private int dx=2, direccion=1;
    protected boolean esMovil;

    public Plataforma(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(rutaImagen).getImage();
    }

    public void dibujar(Graphics g){
        g.drawImage(imagen, x, y, ancho, alto, null);
    }

    public void actualizar(int nivel) {
        if (nivel == 1) {
            imagen = new ImageIcon("recursos/plataforma1.png").getImage();
        }
        else if (nivel == 2) {
            imagen = new ImageIcon("recursos/plataforma2.png").getImage();
        }
        else if (nivel == 3) {
            imagen = new ImageIcon("recursos/plataforma3.png").getImage();
        }
    }

    public void actualizarSuelo(int nivel) {
        if (nivel == 1) {
            imagen = new ImageIcon("recursos/suelo1.png").getImage();
        }
        else if (nivel == 2) {
            imagen = new ImageIcon("recursos/suelo2.png").getImage();
        }
        else if (nivel == 3) {
            imagen = new ImageIcon("recursos/suelo3.png").getImage();
        }
    }

    public void setPosicion(int nuevaX, int nuevaY) {
        x = nuevaX;
        y = nuevaY;
    }

    public void mover(int limiteIzq, int limiteDer) {
        //esPlataformaMovil=true;
        if (x + dx > limiteDer || x + dx < limiteIzq) {
            dx *= -1; // Cambia de dirección si se pasa del límite
        }
        x += dx;
    }

    public void setMovil(boolean valor) {
        esMovil = valor;
    }

    public boolean esMovil() {
        return esMovil;
    }

    public int getDX() {
        return dx;
    }
}