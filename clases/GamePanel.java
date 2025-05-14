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
    ImageIcon icono = new ImageIcon("recursos/ohhbanana.png");
    JLabel conteoDeMetas = new JLabel(icono);
    private int nivelActual = 1;
    private Plataforma plataforma1,plataforma2,plataforma3,plataforma4,plataforma5, suelo;
    private Plataforma plataforma6, plataforma7, plataforma8, plataforma9;
    private EnemigoVolador enemigoVolador1, enemigoVolador2;
    private Meta meta1, meta2, meta3;
    private EnemigoTerrestre enemigoTerrestre1;
    private Champiñon champiñon1, champiñon2;
    private boolean hielo=false, fuego=false, juegoTerminado=false;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("recursos/nivel1.jpg").getImage();
        jugador = new Jugador(35, 550, 40, 60, "recursos/avatar.png");
        Meta metaFinal = jugador.getUltimaMetaTocada();

        // Esto se lo pedi a chat para poder reproducir el sonido al tocar la meta, usa al jugador como un Listener y de ahi se reproduce el sonido
        jugador.setListener(new Jugador.GameListener() {
            @Override
            public void nivelCompletado() {
                if (contarMetasRestantes() == 0) {

                    for (Entidad ent : entidades) {
                        if (metaFinal != null) {
                            metaFinal.redibujarMeta(); // Solo esta se redibuja
                        }
                        if (ent instanceof Meta meta) {
                            meta.redibujarMeta();
                        }
                    }

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
                    jugador.reiniciarPosicion();
                    switch (nivelActual) {
                        case 1:
                            timer.stop();
                            inicializarMensajes("¡Ganaste el nivel!");
                            mensaje.setVisible(true);
                            t.start();
                            nivelActual = 2;
                            break;
                        case 2:
                            timer.stop();
                            inicializarMensajes("¡Ganaste el nivel!");
                            mensaje.setVisible(true);
                            t.start();
                            nivelActual = 3;
                            break;
                        case 3:
                            juegoTerminado = true; // Marcar que el juego terminó
                            musica.stop();
                            inicializarMensajes("¡JUEGO TERMINADO!");
                            mensaje.setVisible(true);
                            timer.stop();
                            break;
                    }
                    cambiarNivel(nivelActual);
                }
            }
        });
        jugador.setPanelListener(new Jugador.GamePanelListener() {
            @Override
            public void mostrarMensajeMuerte() {
                for (Entidad ent : entidades) {
                    if (metaFinal != null) {
                        metaFinal.redibujarMeta(); // Solo esta se redibuja
                    }
                    if (ent instanceof Meta meta) {
                        meta.redibujarMeta();
                    }
                }
                conteoDeMetas.setText(" " + contarMetasRestantes());
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
        ImageIcon imagenBotonReiniciar = new ImageIcon("recursos/reiniciar.png"); // Cambia esto por la ruta de tu imagen
        Image reiniciarEscalado = imagenBotonReiniciar.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon imagenReiniciarEscalado = new ImageIcon(reiniciarEscalado);
        JButton botonReiniciar = new JButton(imagenReiniciarEscalado);
        botonReiniciar.setContentAreaFilled(false);
        botonReiniciar.setBorderPainted(false);
        botonReiniciar.setFocusPainted(false);
        botonReiniciar.setBounds(700, 10, 64, 64);

        botonReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jugador.reiniciarPosicion();
                for (Entidad ent : entidades) {
                    if (metaFinal != null) {
                        metaFinal.redibujarMeta(); // Solo esta se redibuja
                    }
                    if (ent instanceof Meta meta) {
                        meta.redibujarMeta();
                    }
                }
                for (Entidad ent : entidades) {
                    if (ent instanceof EnemigoTerrestre enemigoT) {
                        enemigoT.reiniciarPosicion(); // actualiza su movimiento
                    }
                    if (ent instanceof EnemigoVolador enemigoV) {
                        enemigoV.reiniciarPosicion();
                    }
                }
                conteoDeMetas.setText(" " + contarMetasRestantes());
            }
        });
        add(botonReiniciar);
        ImageIcon banana = new ImageIcon("recursos/ohhbanana.png");
        Image imagenEscalada = banana.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon iconobanana = new ImageIcon(imagenEscalada);
        conteoDeMetas = new JLabel("0", iconobanana, JLabel.LEFT);
        conteoDeMetas.setFont(new Font("Arial", Font.BOLD, 18));
        conteoDeMetas.setBounds(10, 10, 160, 40);
        conteoDeMetas.setForeground(Color.WHITE);

// Posicionar el texto al lado de la imagen
        conteoDeMetas.setHorizontalTextPosition(SwingConstants.RIGHT);
        conteoDeMetas.setVerticalTextPosition(SwingConstants.CENTER);

        this.setLayout(null);
        add(conteoDeMetas);

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
        plataforma4 = new Plataforma(350, 120, 100, 30, "recursos/plataforma1.png");
        plataforma5 = new Plataforma(550, 220, 100, 30, "recursos/plataforma1.png");
        plataforma6 = new Plataforma(800, 600, 100, 30, "recursos/plataforma2.png");
        plataforma7 = new Plataforma(800, 600, 100, 30, "recursos/plataforma2.png");
        plataforma8 = new Plataforma(800, 600, 100, 30, "recursos/plataforma2.png");
        plataforma9 = new Plataforma(800, 600, 100, 30, "recursos/plataforma1.png");
        //esto es la meta, podemos cambiar la imagen con rutaImagen, pero tambien debes cambiarle el tipo de archivo si es .png o .jpg
        meta1 = new Meta(375, 70, 50, 50, "recursos/ohhbanana.png");
        meta2 = new Meta(35, 50, 50, 50, "recursos/ohhbanana.png");
        meta3 = new Meta(710, 200, 50, 50, "recursos/ohhbanana.png");
        //Meter las plataformas en la entidad
        entidades.add(plataforma1);
        entidades.add(plataforma2);
        entidades.add(plataforma3);
        entidades.add(plataforma4);
        entidades.add(plataforma5);
        entidades.add(plataforma6);
        entidades.add(plataforma7);
        entidades.add(plataforma8);
        entidades.add(plataforma9);
        entidades.add(suelo);

        // esto es el techo
        entidades.add(new Limite(0, -20, 800, 20));
        //entidades.add(new Limite(0, -20, 800, 20));
        entidades.add(new Limite(-20, 0, 20, 600));
        entidades.add(new Limite(820, 0, 20, 600));

        //Champiñon
        champiñon1 = new Champiñon(30, 520, 60, 70);
        champiñon2 = new Champiñon(710, 520, 60, 70);
        entidades.add(champiñon1);
        entidades.add(champiñon2);
        entidades.add(meta1);
        entidades.add(meta2);
        entidades.add(meta3);

        int metasTotales = 0;
        for (Entidad ent : entidades) {
            if (ent instanceof Meta) {
                metasTotales++;
            }
        }

        conteoDeMetas.setText("" + contarMetasRestantes());

        enemigoTerrestre1 = new EnemigoTerrestre(300, 540, 40, 40);
        enemigoVolador1 = new EnemigoVolador(280, 300, 40, 40,"recursos/vampiro_derecha.png");
        enemigoVolador2 = new EnemigoVolador(480, 300, 40, 40,"recursos/vampiro_izquierda.png");

        entidades.add(enemigoVolador1);
        entidades.add(enemigoVolador2);
        entidades.add(enemigoTerrestre1);

        //QUISERA MOVIMIENTOS PERSONALIZADOS
        //enemigoTerrestre1.actualizar(95,705);
    }


    public void setFondo(String ruta) {
        fondoPantalla = new ImageIcon(ruta).getImage();
        conteoDeMetas.setText(" " + contarMetasRestantes());
        repaint();
    }

    protected void cambiarNivel(int nivel) {
        switch (nivel) {
            case 1:
                fuego=false;
                setFondo("recursos/nivel1.jpg");
                musica.stop();
                reproducirMusica("recursos/musica.wav");

                plataforma1.actualizar(nivel);
                plataforma1.setPosicion(450,420);
                plataforma2.actualizar(nivel);
                plataforma2.setPosicion(250,420);
                plataforma3.actualizar(nivel);
                plataforma3.setPosicion(150,220);
                plataforma4.actualizar(nivel);
                plataforma4.setPosicion(350,120);
                plataforma5.actualizar(nivel);
                plataforma5.setPosicion(550,220);
                plataforma9.actualizar(nivel);
                plataforma9.setPosicion(800, 600);
                suelo.actualizarSuelo(nivel);
                enemigoVolador1.actualizar(nivel,"recursos/vampiro_derecha.png");
                enemigoVolador1.setPosicion(280,300);
                enemigoVolador1.setPosicionesOrignales(280,300);
                enemigoVolador2.actualizar(nivel,"recursos/vampiro_izquierda.png");
                enemigoVolador2.setPosicion(480,300);
                enemigoVolador2.setPosicionesOrignales(480,300);
                enemigoTerrestre1.actualizarNivel(nivel);
                enemigoTerrestre1.setPosicion(300,540);
                champiñon1.setPosicion(30,520);
                champiñon2.setPosicion(710,520);
                meta2.setPosicion(35, 200);
                meta3.setPosicion(710, 200);

                conteoDeMetas.setText(" " + contarMetasRestantes());
                repaint();
                break;
            case 2:

                //enemigoTerrestre1.actualizar(10,790);
                setFondo("recursos/nivel2.jpg");
                musica.stop();
                reproducirMusica("recursos/musica2.wav");

                hielo= true;
                jugador.setPosicion(360,410);
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
                plataforma6.actualizar(nivel);
                plataforma6.setPosicion(600,470);
                plataforma7.actualizar(nivel);
                plataforma7.setPosicion(500,370);
                plataforma8.actualizar(nivel);
                plataforma8.setPosicion(650,200);
                plataforma9.actualizar(nivel);
                plataforma9.setPosicion(350, 470);
                suelo.actualizarSuelo(nivel);
                enemigoTerrestre1.actualizarNivel(nivel);
                enemigoVolador1.actualizar(nivel,"recursos/hada_derecha.png");
                enemigoVolador1.setPosicion(180,300);
                enemigoVolador1.setPosicionesOrignales(180,300);
                enemigoVolador2.actualizar(nivel,"recursos/hada_izquierda.png");
                enemigoVolador2.setPosicion(580,300);
                enemigoVolador2.setPosicionesOrignales(580,300);
                champiñon2.setPosicion(800,600);
                champiñon1.setPosicion(800,600);
                meta2.setPosicion(35, 250);
                meta3.setPosicion(710, 250);
                conteoDeMetas.setText(" " + contarMetasRestantes());
                repaint();
                break;
            case 3:
                setFondo("recursos/nivel3.png");
                musica.stop();
                reproducirMusica("recursos/balatroBalatrezEstaEscuchandoBalatro.wav");

                hielo=false;
                fuego=true;
                jugador.setPosicion(35,490);
                plataforma1.actualizar(nivel);
                plataforma1.setPosicion(350,450);
                plataforma2.actualizar(nivel);
                plataforma2.setPosicion(350,350);
                plataforma3.actualizar(nivel);
                plataforma3.setPosicion(350,250);
                plataforma4.actualizar(nivel);
                plataforma4.setPosicion(350,150);
                plataforma5.actualizar(nivel);
                plataforma5.setPosicion(800,600);
                plataforma6.actualizar(nivel);
                plataforma6.setPosicion(800,600);
                plataforma7.actualizar(nivel);
                plataforma7.setPosicion(800,600);
                plataforma8.actualizar(nivel);
                plataforma8.setPosicion(800,600);
                plataforma9.setPosicion(800,600);
                meta2.setPosicion(35, 200);
                meta3.setPosicion(710,200);

                suelo.actualizarSuelo(nivel);
                enemigoTerrestre1.actualizarNivel(nivel);
                enemigoVolador1.actualizar(nivel,"recursos/flama_derecha.png");
                enemigoVolador2.actualizar(nivel,"recursos/flama_izquierda.png");
                conteoDeMetas.setText(" " + contarMetasRestantes());
                repaint();
                break;
            default:
                break;
        }
    }

    public int contarMetasRestantes() {
        int contador = 0;
        for (Entidad e : entidades) {
            if (e instanceof Meta meta && meta.esVisible()) {
                contador++;
            }
        }
        return contador;
    }

    public void actionPerformed(ActionEvent e) {
        conteoDeMetas.setText(" " + contarMetasRestantes());
        if (juegoTerminado==true) {
            return;
        }

        if(hielo==true){
            jugador.resbalar(hielo);
        }
        else{
            jugador.actualizar();
        }
        jugador.verificarColisiones(entidades);

        for (Entidad ent : entidades) {
            if (ent instanceof EnemigoTerrestre enemigoT) {
                enemigoT.actualizar(); // actualiza su movimiento
            }
            if (ent instanceof EnemigoVolador enemigoV) {
                enemigoV.vueloVertical();
            }
            if (ent instanceof Meta meta) {
                meta.actualizar();
            }
        }

        if(fuego==true){
            plataforma1.mover(50, 350); // rango corto
            plataforma1.setMovil(true);
            plataforma3.mover(50, 350);
            plataforma3.setMovil(true);
            plataforma2.mover(350, 650); // rango largo
            plataforma2.setMovil(true);
            plataforma4.mover(350, 650);
            plataforma4.setMovil(true);
        } else {
            plataforma1.setMovil(false);
            plataforma2.setMovil(false);
            plataforma3.setMovil(false);
            plataforma4.setMovil(false);
        }

        repaint();
        GamePanel.this.requestFocusInWindow();
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

        if (juegoTerminado==true) {
            return;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) jugador.setIzquierda(false);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) jugador.setDerecha(false);
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

}