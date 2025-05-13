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
    private Plataforma plataforma6, plataforma7, plataforma8, plataforma9, plataforma10;
    private EnemigoVolador enemigoVolador1, enemigoVolador2, enemigoVolador3, enemigoVolador4;
    private EnemigoTerrestre enemigoTerrestre1, enemigoTerrestre2, enemigoTerrestre3;
    private Meta meta;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        inicializarMensajes("¡Has muerto!");

        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("recursos/nivel1.jpg").getImage();
        jugador = new Jugador(400, 530, 40, 60, "recursos/avatar.png");
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
                inicializarMensajes("¡Has muerto!");
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

        //Musica de fondo
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
        this.setLayout(null); //Importante para posicionar con coordenadas
        mensaje.setBounds(250, 20, 300, 50); //Posición en pantalla
        add(mensaje);
        return;
    }

    protected void inicializarNivel() {
        //Suelo
        suelo=new Plataforma(0, 580, 800, 20, "recursos/suelo1.png");

        //Plataformas
        plataforma1 = new Plataforma(450, 420, 100, 30, "recursos/plataforma1.png");
        plataforma2 = new Plataforma(250, 420, 100, 30, "recursos/plataforma1.png");
        plataforma3 = new Plataforma(150, 220, 100, 30, "recursos/plataforma1.png");
        plataforma4 = new Plataforma(350, 100, 100, 30, "recursos/plataforma1.png");
        plataforma5 = new Plataforma(550, 220, 100, 30, "recursos/plataforma1.png");

        //Meter las plataformas en la entidad
        entidades.add(plataforma1);
        entidades.add(plataforma2);
        entidades.add(plataforma3);
        entidades.add(plataforma4);
        entidades.add(plataforma5);
        entidades.add(suelo);

        // esto es el techo
        entidades.add(new Limite(0, -20, 800, 20));
        entidades.add(new Limite(0, -20, 800, 20));
        entidades.add(new Limite(-20, 0, 20, 600));
        entidades.add(new Limite(820, 0, 20, 600));

        //Champiñon
        entidades.add(new Champiñon(30, 520, 60, 70));
        entidades.add(new Champiñon(710, 520, 60, 70));

        //esto es la meta, podemos cambiar la imagen con rutaImagen, pero tambien debes cambiarle el tipo de archivo si es .png o .jpg
        entidades.add(new Meta(375, 50, 50, 50, "recursos/ohhbanana.png"));
        entidades.add(new EnemigoTerrestre(300, 540, 40, 40,"recursos/temmie.png"));
        enemigoTerrestre1 = new EnemigoTerrestre(300, 540, 40, 40,"recursos/temmie.png");

        enemigoVolador1 = new EnemigoVolador(280, 300, 40, 40,"recursos/temmie.png");
        enemigoVolador2 = new EnemigoVolador(480, 300, 40, 40,"recursos/temmie.png");

        entidades.add(enemigoVolador1);
        entidades.add(enemigoVolador2);
        entidades.add(enemigoTerrestre1);

        //QUISERA MOVIMIENTOS PERSONALIZADOS
        //enemigoTerrestre1.actualizar(95,705);
    }

    public void setFondo(String ruta) {
        fondoPantalla = new ImageIcon(ruta).getImage();
        repaint();
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
                plataforma5.actualizar(nivel);
                suelo.actualizarSuelo(nivel);
                repaint();
                break;
            case 2:
                //entidades.removeIf(entidad -> entidad instanceof Champiñon);

                //enemigoTerrestre1.actualizar(10,790);

                setFondo("recursos/nivel2.jpg");
                musica.stop();
                reproducirMusica("recursos/musica2.wav");
                plataforma1.actualizar(nivel);
                plataforma1.setPosicion(100,470);
                plataforma2.actualizar(nivel);
                plataforma2.setPosicion(200,370);
                plataforma3.actualizar(nivel);
                plataforma3.setPosicion(50,200);
                plataforma4.actualizar(nivel);
                plataforma4.setPosicion(250,200);
                plataforma5.actualizar(nivel);
                plataforma5.setPosicion(450,200);

               // plataforma6 = new Plataforma(600, 470, 100, 30, "recursos/plataforma2.png");
               // plataforma7 = new Plataforma(500, 370, 100, 30, "recursos/plataforma2.png");
               // plataforma8 = new Plataforma(650, 200, 100, 30, "recursos/plataforma2.png");
               // entidades.add(plataforma6);
               // entidades.add(plataforma7);
               // entidades.add(plataforma8);

                //EN ESTA ZONA QUIERO QUE EL AVE VUELE HORIZONTAL PARA QUE ESTORBE EN LA PARTE DE ARRIBA
                //enemigoVolador3 = new EnemigoVolador(200, 550, 40, 40,"recursos/temmie.png");
                //entidades.add(enemigoVolador3);
                //enemigoVolador3.vueloHorizontal();

                suelo.actualizarSuelo(nivel);
                repaint();
                break;
            case 3:
                //entidades.removeIf(plataforma6 -> plataforma6 instanceof Plataforma);
                //entidades.removeIf(plataforma7 -> plataforma7 instanceof Plataforma);
                //entidades.removeIf(plataforma8 -> plataforma8 instanceof Plataforma);

                setFondo("recursos/nivel3.png");
                musica.stop();
                reproducirMusica("recursos/balatroBalatrezEstaEscuchandoBalatro.wav");
                plataforma1.actualizar(nivel);
                plataforma2.actualizar(nivel);
                plataforma3.actualizar(nivel);
                plataforma4.actualizar(nivel);
                plataforma5.actualizar(nivel);
                suelo.actualizarSuelo(nivel);
                repaint();
                break;
            default:
                break;
        }
    }


    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
        jugador.verificarColisiones(entidades);

        for (Entidad ent : entidades) {
            if (ent instanceof EnemigoTerrestre enemigoT) {
                enemigoT.actualizar(95,705); // actualiza su movimiento
            }
            if (ent instanceof EnemigoVolador enemigoV) {
                enemigoV.vueloVertical();
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

}