import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    private final int ALL_TILES = WIDTH * HEIGHT;
    private final int DELAY = 100;

    private final int x[] = new int[ALL_TILES];
    private final int y[] = new int[ALL_TILES];

    private int bodySize;
    private int appleX;
    private int appleY;
    private boolean running = true;
    private char direction = 'R';
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e);
                if (!running && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
            }
        });
        startGame();
    }

    private void startGame() {
        bodySize = 3;
        int startX = WIDTH / 2 * TILE_SIZE;
        int startY = HEIGHT / 2 * TILE_SIZE;

        for (int i = 0; i < bodySize; i++) {
            x[i] = startX - i * TILE_SIZE;
            y[i] = startY;
        }
        spawnApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnApple() {
        appleX = (int) (Math.random() * WIDTH) * TILE_SIZE;
        appleY = (int) (Math.random() * HEIGHT) * TILE_SIZE;
    }

    private void move() {
        for (int i = bodySize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= TILE_SIZE;
            case 'D' -> y[0] += TILE_SIZE;
            case 'L' -> x[0] -= TILE_SIZE;
            case 'R' -> x[0] += TILE_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = bodySize; i > 0; i--) {
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH * TILE_SIZE || y[0] < 0 || y[0] >= HEIGHT * TILE_SIZE) {
            running = false;
        }

        if (!running) {
            timer.stop();
            restartGame();
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodySize++;
            spawnApple();
        }
    }

    private void changeDirection(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
            direction = 'L';
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
            direction = 'R';
        } else if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
            direction = 'U';
        } else if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
            direction = 'D';
        }
    }

    private void restartGame() {
        bodySize = 3;
        direction = 'R';
        startGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(appleX, appleY, TILE_SIZE, TILE_SIZE);
            for (int i = 0; i < bodySize; i++) {
                g.setColor(i == 0 ? Color.GREEN : new Color(45, 180, 0));
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Game Over! Pressione Enter para reiniciar", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo da Cobrinha");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

