package GraphicsProgram2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Random;


public class BouncingVertexText {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drphnd Bouncing Text");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new AnimationPanel()); 
            frame.pack(); 
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true);
        });
    }
}
class AnimationPanel extends JPanel implements ActionListener {

    private final String textToDraw = "DRPHND"; // Nama yang akan digambar
    private static final int LETTER_WIDTH = 60;
    private static final int LETTER_HEIGHT = 100;
    private static final int LETTER_SPACING = 15;
    private final int textBlockWidth;
    private final int textBlockHeight = LETTER_HEIGHT;

    private int x, y; 
    private int dx = 4, dy = 4; 
    private final Timer animationTimer;
    private final Random random = new Random();

    private float hue = 0.0f;

    public AnimationPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        textBlockWidth = (textToDraw.length() * LETTER_WIDTH) + ((textToDraw.length() - 1) * LETTER_SPACING);

        x = 100;
        y = 100;

        setFocusable(true); 

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dx = random.nextInt(13) - 6; // Kecepatan acak antara -6 dan +6
                dy = random.nextInt(13) - 6;
                if (dx == 0 && dy == 0) dx = 1;
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), e.getKeyChar());
            }
        });
        animationTimer = new Timer(16, this); // ~60 FPS
        animationTimer.start();
    }

    private void handleKeyPress(int keyCode, char keyChar) {
        int currentSpeed = Math.max(Math.abs(dx), Math.abs(dy));
        if (currentSpeed == 0) currentSpeed = 3;

        switch (keyCode) {
            case KeyEvent.VK_UP:    dy = -currentSpeed; dx = 0; break;
            case KeyEvent.VK_DOWN:  dy = currentSpeed;  dx = 0; break;
            case KeyEvent.VK_LEFT:  dx = -currentSpeed; dy = 0; break;
            case KeyEvent.VK_RIGHT: dx = currentSpeed;  dy = 0; break;
        }

        if (keyChar == '+' || keyCode == KeyEvent.VK_EQUALS) {
            dx = (dx > 0) ? dx + 1 : (dx < 0 ? dx - 1 : dx);
            dy = (dy > 0) ? dy + 1 : (dy < 0 ? dy - 1 : dy);
        } else if (keyChar == '-' || keyCode == KeyEvent.VK_MINUS) {
            dx = (dx > 1) ? dx - 1 : (dx < -1 ? dx + 1 : dx);
            dy = (dy > 1) ? dy - 1 : (dy < -1 ? dy + 1 : dy);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        x += dx;
        y += dy;

        if (x <= 0) {
            x = 0;
            dx = -dx;
        } else if (x + textBlockWidth >= getWidth()) {
            x = getWidth() - textBlockWidth;
            dx = -dx;
        }

        if (y <= 0) {
            y = 0;
            dy = -dy;
        } else if (y + textBlockHeight >= getHeight()) {
            y = getHeight() - textBlockHeight;
            dy = -dy;
        }

        hue += 0.005f;
        if (hue > 1.0f) {
            hue = 0.0f;
        }

        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color1 = Color.getHSBColor(hue, 1.0f, 1.0f); // Warna awal (berubah)
        Color color2 = Color.getHSBColor(hue + 0.15f, 1.0f, 1.0f); // Warna akhir (berubah)
        GradientPaint gradient = new GradientPaint(x, y, color1, x + textBlockWidth, y + textBlockHeight, color2);
        g2d.setPaint(gradient);

        AffineTransform originalTransform = g2d.getTransform();

        g2d.translate(x, y);

        int currentXOffset = 0;
        for (char c : textToDraw.toCharArray()) {
            Shape letterShape = getLetterShape(c);

            // Pindahkan kanvas ke posisi huruf saat ini
            AffineTransform tx = AffineTransform.getTranslateInstance(currentXOffset, 0);
            g2d.fill(tx.createTransformedShape(letterShape));
            
            // Tambahkan offset untuk huruf berikutnya
            currentXOffset += LETTER_WIDTH + LETTER_SPACING;
        }

        // Kembalikan kanvas ke posisi semula
        g2d.setTransform(originalTransform);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Klik = Arah Acak | Panah = Kontrol | +/- = Kecepatan", 10, 20);
    }
    private Shape getLetterShape(char c) {
        GeneralPath path = new GeneralPath();
        switch (Character.toUpperCase(c)) {
            case 'D':
                path.moveTo(10, 5); path.lineTo(35, 5); path.quadTo(60, 20, 60, 50);
                path.quadTo(60, 80, 35, 95); path.lineTo(10, 95);
                path.closePath(); path.moveTo(22, 18); path.lineTo(22, 82);
                path.lineTo(35, 82); path.quadTo(48, 75, 48, 50);
                path.quadTo(48, 25, 35, 18); path.closePath();
                break;
            case 'R':
                path.moveTo(10, 5); path.lineTo(10, 95); path.lineTo(22, 95);
                path.lineTo(22, 50); path.lineTo(38, 50); path.quadTo(60, 50, 60, 30);
                path.quadTo(60, 5, 38, 5); path.closePath(); path.moveTo(22, 15);
                path.lineTo(38, 15); path.quadTo(48, 15, 48, 30);
                path.quadTo(48, 40, 38, 40); path.lineTo(22, 40);
                path.closePath(); path.moveTo(35, 50); path.lineTo(55, 95);
                path.lineTo(45, 95); path.lineTo(30, 58); path.closePath();
                break;
            case 'P':
                path.moveTo(10, 5); path.lineTo(10, 95); path.lineTo(22, 95);
                path.lineTo(22, 50); path.lineTo(38, 50); path.quadTo(60, 50, 60, 30);
                path.quadTo(60, 5, 38, 5); path.closePath(); path.moveTo(22, 15);
                path.lineTo(38, 15); path.quadTo(48, 15, 48, 30);
                path.quadTo(48, 40, 38, 40); path.lineTo(22, 40); path.closePath();
                break;
            case 'H':
                path.moveTo(10, 5); path.lineTo(22, 5); path.lineTo(22, 40);
                path.lineTo(48, 40); path.lineTo(48, 5); path.lineTo(60, 5);
                path.lineTo(60, 95); path.lineTo(48, 95); path.lineTo(48, 50);
                path.lineTo(22, 50); path.lineTo(22, 95); path.lineTo(10, 95);
                path.closePath();
                break;
            case 'N':
                path.moveTo(10, 95); path.lineTo(10, 5); path.lineTo(25, 5);
                path.lineTo(50, 70); path.lineTo(50, 5); path.lineTo(65, 5);
                path.lineTo(65, 95); path.lineTo(50, 95); path.lineTo(25, 20);
                path.lineTo(25, 95);
                path.closePath();
                break;
        }
        return path;
    }
}