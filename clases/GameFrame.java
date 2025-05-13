import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Juego de Plataforma 2D");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
