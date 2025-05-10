import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Entidad> entidades;
    private ArchivoJuego archivo;
    private boolean teclaPresionada;
    private Image fondoPantalla;
    private Clip musica;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        // Fondo de la pantalla, ahorita es de balatro
        fondoPantalla = new ImageIcon("balatreroBalatrez.png").getImage();
        jugador = new Jugador(50, 500, 40, 40);
        // Esto se lo pedi a chat para poder reproducir el sonido al tocar la meta, usa al jugador como un Listener y de ahi se reproduce el sonido
        jugador.setListener(new Jugador.GameListener() {
            @Override
            public void nivelCompletado() {
                    reproducirSonidoMeta();
            }
        });
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        entidades.add(new Plataforma(0, 580, 800, 20));
        entidades.add(new Plataforma(200, 450, 120, 20));
        // esto es el techo
        entidades.add(new Limite(0, -20, 800, 20));
        //esto es la meta, podemos cambiar la imagen con rutaImagen, pero tambien debes cambiarle el tipo de archivo si es .png o .jpg
        entidades.add(new Meta(700, 400, 50, 50, "ohhbanana.png"));
        entidades.add(new EnemigoTerrestre(300, 540, 40, 40));
        entidades.add(new EnemigoVolador(500, 300, 40, 40));

        archivo.cargar(jugador);
        //Musica de fondo, ahorita es el tema de balatro
        reproducirMusica("balatroBalatrezEstaEscuchandoBalatro.wav");

        timer = new Timer(16, this);
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
        jugador.verificarColisiones(entidades);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo

        jugador.dibujar(g);
        for (Entidad ent : entidades) {
            ent.dibujar(g); // Limite no dibuja nada, así que no se ve
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(true);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(true);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) jugador.saltar();
        if (e.getKeyCode() == KeyEvent.VK_P) {
            archivo.guardar(jugador);
        }
        // Reiniciar posición del jugador usando la R suena como si se lo hubiera pedido a chat XDDDDDDDD
        if (e.getKeyCode() == KeyEvent.VK_R && !teclaPresionada) {
            jugador.reiniciarPosicion(true);
            teclaPresionada = true;
        }
    }


    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(false);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(false);
        if (e.getKeyCode() == KeyEvent.VK_R) {
            jugador.reiniciarPosicion(false);
            teclaPresionada = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    private void reproducirMusica(String rutaArchivo) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY); // Repite infinitamente
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void reproducirSonidoMeta() {
        try {
            File archivo = new File("ohhbanana.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

