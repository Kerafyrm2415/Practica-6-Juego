package clases;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Meta extends Entidad {
    private Image imagen;
    boolean metaEsVisible = true;
    boolean redibujar = false;
    private int baseY;  // posición original
    private double desplazamiento = 0;
    private int direccion = 1; // 1 = sube, -1 = baja
    private int maxDesplazamiento = 10;
    private double velocidad = 0.2;

    public Meta(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto);
        imagen = new ImageIcon(rutaImagen).getImage();
        this.baseY = y;
    }
    public void actualizar() {
        desplazamiento += direccion * velocidad;

        if (Math.abs(desplazamiento) >= maxDesplazamiento) {
            direccion *= -1; // cambia dirección
        }

        y = baseY + (int) desplazamiento;
    }

    public boolean tocarMeta() {
        metaEsVisible = false;
        return metaEsVisible;
    }

    public boolean esVisible() {
        return metaEsVisible;
    }

    public boolean redibujarMeta() {
        metaEsVisible = true;
        return metaEsVisible;
    }

    @Override
    public void dibujar(Graphics g) {
        if (metaEsVisible) {
            g.drawImage(imagen, x, y, ancho, alto, null);
        }
    }

    public void reproducirSonidoMeta() {
        try {
            File archivo = new File("recursos/ohhbanana.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}