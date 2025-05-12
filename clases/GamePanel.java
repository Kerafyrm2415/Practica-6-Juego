package clases;

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
    private JLabel mensaje;
    private int nivelActual = 1;
    private Plataforma plataforma1,plataforma2,plataforma3,plataforma4,plataforma5, suelo;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        inicializarMensajes("¡Has muerto!");

        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("recursos/nivel1.jpg").getImage();
        jugador = new Jugador(50, 530, 40, 60, "recursos/avatar.png");
        // Esto se lo pedi a chat para poder reproducir el sonido al tocar la meta, usa al jugador como un Listener y de ahi se reproduce el sonido
        jugador.setListener(new Jugador.GameListener() {
            @Override
            public void nivelCompletado() {
                reproducirSonidoMeta();
                // Elimina la meta del juego
                // entidades.removeIf(entidad -> entidad instanceof Meta);


                // Mensaje de victoria
                inicializarMensajes("¡Ganaste el nivel!");
                mensaje.setVisible(true);
                // Detiene el juego
                timer.stop();
                // Después de 2 segundos, oculta el mensaje y reanuda
                Timer t = new Timer(2000, e -> {
                    mensaje.setVisible(false);
                    musica.start();
                    timer.start();
                    musica.loop(Clip.LOOP_CONTINUOUSLY);
                });
                t.setRepeats(false);
                t.start();
                jugador.reiniciarPosicion(true);
                switch (nivelActual) {
                    case 1:
                        nivelActual = 2;
                        break;
                    case 2:
                        nivelActual = 3;
                        break;
                    case 3:
                        nivelActual = 1;
                        break;
                }
                cambiarNivel(nivelActual);
            }
        });
        jugador.setPanelListener(new Jugador.GamePanelListener() {
            @Override
            public void mostrarMensajeMuerte() {
                mensaje.setVisible(true);
                // Pausa el juego y la música
                timer.stop();
                musica.stop();

                // Después de 2 segundos, oculta el mensaje y reanuda
                Timer t = new Timer(2000, e -> {
                    mensaje.setVisible(false);
                    timer.start();
                    musica.start();
                    musica.loop(Clip.LOOP_CONTINUOUSLY);
                });
                t.setRepeats(false);
                t.start();
                for (Entidad ent : entidades) {
                    if (ent instanceof EnemigoTerrestre enemigoT) {
                        enemigoT.reiniciarPosicion(); // actualiza su movimiento
                    }
                    if (ent instanceof EnemigoVolador enemigoV) {
                        enemigoV.reiniciarPosicion();
                    }
                }
            }
        });
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        inicializarNivel();

        archivo.cargar(jugador);
        //Musica de fondo, ahorita es el tema de balatro
        reproducirMusica("recursos/musica.wav");

        timer = new Timer(16, this);
        timer.start();
    }

    protected void inicializarMensajes(String texto) {
        mensaje = new JLabel(texto);
        mensaje.setFont(new Font("Arial", Font.BOLD, 24));
        if (nivelActual == 3) mensaje.setForeground(Color.WHITE);
        else mensaje.setForeground(Color.BLACK);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setVisible(false);
        this.setLayout(null); // Importante para posicionar con coordenadas
        mensaje.setBounds(250, 20, 300, 50); // Posición en pantalla
        add(mensaje);
        return;
    }

    protected void inicializarNivel() {
        //Suelo
        suelo=new Plataforma(0, 580, 800, 20, "recursos/plataforma1.png");
        //entidades.add(new Plataforma(0, 580, 800, 20, "plataforma1.png"));
        //Plataformas
        plataforma1 = new Plataforma(200, 450, 100, 30, "recursos/plataforma1.png");
        plataforma2 = new Plataforma(100, 300, 100, 30, "recursos/plataforma1.png");
        plataforma3 = new Plataforma(250, 300, 100, 30, "recursos/plataforma1.png");
        plataforma4 = new Plataforma(300, 100, 100, 30, "recursos/plataforma1.png");
        //Meter las plataformas en la entidad
        entidades.add(plataforma1);
        entidades.add(plataforma2);
        entidades.add(plataforma3);
        entidades.add(plataforma4);
        entidades.add(suelo);
        // esto es el techo
        entidades.add(new Limite(0, -20, 800, 20));
        entidades.add(new Limite(0, -20, 800, 20));
        entidades.add(new Limite(-20, 0, 20, 600));
        entidades.add(new Limite(820, 0, 20, 600));
        entidades.add(new Champiñon(550, 520, 50, 70));
        //esto es la meta, podemos cambiar la imagen con rutaImagen, pero tambien debes cambiarle el tipo de archivo si es .png o .jpg
        entidades.add(new Meta(700, 100, 50, 50, "recursos/ohhbanana.png"));
        entidades.add(new EnemigoTerrestre(300, 540, 40, 40));
        entidades.add(new EnemigoVolador(500, 300, 40, 40));

    }

    public void setFondo(String ruta) {
        fondoPantalla = new ImageIcon(ruta).getImage();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
        jugador.verificarColisiones(entidades);

        for (Entidad ent : entidades) {
            if (ent instanceof EnemigoTerrestre enemigoT) {
                enemigoT.actualizar(); // actualiza su movimiento
            }
            if (ent instanceof EnemigoVolador enemigoV) {
                enemigoV.actualizar();
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo

        jugador.dibujar(g);
        for (Entidad ent : entidades) {
            ent.dibujar(g); // clases.Limite no dibuja nada, así que no se ve
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(true);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(true);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) jugador.saltar(false);
        if (e.getKeyCode() == KeyEvent.VK_P) {
            archivo.guardar(jugador);
        }
        // Metodo rapido para pasar de un nivel a otro
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            nivelActual++;
            if (nivelActual > 3) {
                nivelActual = 1;
            }
            cambiarNivel(nivelActual);
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
            musica.stop();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY); // Repite infinitamente
        } catch (Exception e) {
            e.printStackTrace();
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
    protected void cambiarNivel(int nivel) {
        switch (nivel) {
            case 1:
                setFondo("recursos/nivel1.jpg");
                musica.stop();
                reproducirMusica("recursos/musica.wav");
                plataforma1.actualizar(nivel);
                plataforma2.actualizar(nivel);
                plataforma3.actualizar(nivel);
                plataforma4.actualizar(nivel);
                suelo.actualizar(nivel);
                repaint();
                break;
            case 2:
                setFondo("recursos/nivel2.jpg");
                musica.stop();
                reproducirMusica("recursos/musica2.wav");
                plataforma1.actualizar(nivel);
                plataforma2.actualizar(nivel);
                plataforma3.actualizar(nivel);
                plataforma4.actualizar(nivel);
                suelo.actualizar(nivel);
                repaint();
                break;
            case 3:
                setFondo("recursos/nivel3.png");
                musica.stop();
                reproducirMusica("recursos/balatroBalatrezEstaEscuchandoBalatro.wav");
                plataforma1.actualizar(nivel);
                plataforma2.actualizar(nivel);
                plataforma3.actualizar(nivel);
                plataforma4.actualizar(nivel);
                suelo.actualizar(nivel);
                repaint();
                break;
            default:
                break;
        }
    }
}