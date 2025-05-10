import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Entidad> entidades;
    private ArchivoJuego archivo;
    private boolean teclaPresionada;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        jugador = new Jugador(50, 500, 40, 40);
        entidades = new ArrayList<>();
        archivo = new ArchivoJuego("progreso.txt");

        entidades.add(new Plataforma(0, 580, 800, 20));
        entidades.add(new Plataforma(200, 450, 120, 20));
        entidades.add(new EnemigoTerrestre(300, 540, 40, 40));
        entidades.add(new EnemigoVolador(500, 300, 40, 40));

        archivo.cargar(jugador);

        timer = new Timer(16, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        jugador.actualizar();
        jugador.verificarColisiones(entidades);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        jugador.dibujar(g);
        for (Entidad ent : entidades) {
            ent.dibujar(g);
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

    public void keyTyped(KeyEvent e) {}
}
