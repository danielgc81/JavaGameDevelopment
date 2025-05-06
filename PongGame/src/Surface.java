
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.Timer;

public class Surface extends Canvas implements KeyListener {
	private static final long serialVersionUID = 1L;
	private Thread t;
	private boolean paused;
	private Ball ball;
   private BufferStrategy bufferStrategy;

   private Line leftLine;
   private Line rightLine;
   private final int moveStep = 5;

   private int score1 = 0, score2 = 0;

   private boolean wPressed = false;
   private boolean sPressed = false;
   private boolean upPressed = false;
   private boolean downPressed = false;
   private Timer lineMovement;

	public Surface(int w, int h) {
		setPreferredSize(new Dimension(w, h));
		setBackground(Color.BLACK);
		ball = new Ball(this, (w - 15) / 2d, (h - 15) / 2d, 15, 45d, 300d, Color.WHITE);
      leftLine = new Line(5, (h - 70) / 2, 10, 70, Color.BLUE);
      rightLine = new Line(w - 15, (h - 70) / 2, 10, 70, Color.RED);
      addKeyListener(this);
      setFocusable(true);

      lineMovement = new Timer(16, e -> updateLinePositions());
      lineMovement.start();
	}

	private void run() {
      createBufferStrategy(2);
      bufferStrategy = getBufferStrategy();
		long t0 = System.nanoTime(), t1;
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (this) {
				if (paused) {
					try {
						wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					t0 = System.nanoTime();
				}
			}
			next((t1 = System.nanoTime()) - t0);
			drawFrame();
			t0 = t1;
		}
	}

	public void start() {
		t = new Thread(this::run);
		t.start();
	}

	public void stop() {
		t.interrupt();
		try {
			t.join();
		} catch (InterruptedException e) {
		}
	}

	public synchronized void pause() {
		paused = true;
	}

	public synchronized void resume() {
		if (paused) {
			paused = false;
			notify();
		}
	}

	public synchronized boolean isPaused() {
		return paused;
	}

	private void next(long lapse) {
      ball.move(lapse);
	}

	private void drawFrame() {
		Graphics g = null;
      try {
         g = (Graphics2D) bufferStrategy.getDrawGraphics();
         paint(g);
         if (!bufferStrategy.contentsLost()) {
            bufferStrategy.show();
         }
      } finally {
         System.out.println("Parpadeo eliminado");
      }
		g.dispose();
	}

   public Line getLeftLine () {
      return leftLine;
   }

   public Line getRightLine () {
      return rightLine;
   }

   public Ball getBall () {
      return ball;
   }

   private void updateLinePositions () {
      if (wPressed) {
         leftLine.move(- moveStep, getHeight());
      }
      if (sPressed) {
         leftLine.move(moveStep, getHeight());
      }
      if (upPressed) {
         rightLine.move(-moveStep, getHeight());
      }
      if (downPressed) {
         rightLine.move(moveStep, getHeight());
      }
      repaint();
   }

   public void resetBall () {
      if (ball.getX() + ball.getSize() > getWidth()) {
         score1++;
      }
      if (ball.getX() <= 0) {
         score2++;
      }
      ball = new Ball(this, getWidth() / 2, getHeight() / 2, 15, Math.random() > 0.5 ? 1 : -1, 250, Color.WHITE);
   }

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, getWidth(), getHeight());
      g2d.setColor(Color.WHITE);
      g2d.fillRect(0, 0, getWidth(), 1);
      g2d.setColor(Color.WHITE);
      g2d.setFont(new Font("Arial", Font.BOLD, 25));
      g2d.drawString(score1 + " | " + score2, getWidth() / 2 - 30, 30);
      leftLine.paint(g2d);
      rightLine.paint(g2d);
		ball.paint(g2d);
	}

   @Override
   public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      if (key == KeyEvent.VK_W) {
         wPressed = true;
      } else if (key == KeyEvent.VK_S) {
         sPressed = true;
      }

      if (key == KeyEvent.VK_UP) {
         upPressed = true;
      } else if (key == KeyEvent.VK_DOWN) {
         downPressed = true;
      }

      if (key == KeyEvent.VK_P) {
         if (isPaused()) {
            resume();
         } else {
            pause();
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();

      if (key == KeyEvent.VK_W) {
         wPressed = false;
      } else if (key == KeyEvent.VK_S) {
         sPressed = false;
      }

      if (key == KeyEvent.VK_UP) {
         upPressed = false;
      } else if (key == KeyEvent.VK_DOWN) {
         downPressed = false;
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {}
}
