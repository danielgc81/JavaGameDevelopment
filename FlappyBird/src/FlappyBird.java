import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
   int boardWidth = 360;
   int boardHeight = 640;

   // imagenes
   Image backgroundImg;
   Image birdImg;
   Image bottomPipeImg;
   Image topPipeImg;

   // Configuracion del flappyBird
   int birdX = boardWidth / 8;
   int birdY = boardHeight / 2;
   int birdWidth = 34;
   int birdHeight = 24;

   class Bird {
      int x = birdX;
      int y = birdY;
      int width = birdWidth;
      int height = birdHeight;
      Image img;

      Bird (Image img) {
         this.img = img;
      }
   }

   // Pipes
   int pipeX = boardWidth;
   int pipeY = 0;
   int pipeWidth = 64;
   int pipeHeight = 512;

   class Pipe {
      int x = pipeX;
      int y = pipeY;
      int width = pipeWidth;
      int height = pipeHeight;
      Image img;
      boolean passed = false;

      Pipe (Image img) {
         this.img = img;
      }
   }

   // Logica del juego
   Bird bird;
   int velocityX = -4;
   int velocityY = 0;
   int gravity = 1;

   ArrayList<Pipe> pipes;
   Random r = new Random();

   Timer gameLoop;
   Timer placePipesTimer;

   boolean gameOver = false;
   double score = 0;

   FlappyBird () {
      this.setPreferredSize(new Dimension(boardWidth, boardHeight));
      this.setFocusable(true);
      this.addKeyListener(this);

      // Cargando las imagenes del juego
      backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
      birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
      bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
      topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();

      // Pajaro
      bird = new Bird(birdImg);
      pipes = new ArrayList<>();

      // Tiempo de creación de los pipes
      placePipesTimer = new Timer(1200, new ActionListener() {
         @Override
         public void actionPerformed (ActionEvent e) {
            placePipes();
         }
      });

      placePipesTimer.start();

      // tiempo del juego
      gameLoop = new Timer(1000 / 60, this);
      gameLoop.start();
   }

   public void paintComponent (Graphics g) {
      super.paintComponent(g);
      draw(g);
   }

   public void placePipes () {
      int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
      int openingSpace = boardHeight / 5;

      Pipe topPipe = new Pipe(topPipeImg);
      topPipe.y = randomPipeY;
      pipes.add(topPipe);

      Pipe bottomPipe = new Pipe(bottomPipeImg);
      bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
      pipes.add(bottomPipe);
   }

   public void draw (Graphics g) {
      // Pintando la imagen del fondo
      g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

      // Pintando el pajaro
      g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

      // Pintando los pipes
      for (int i = 0; i < pipes.size(); i++) {
         Pipe pipe = pipes.get(i);
         g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
      }

      // Pintando la puntuación
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 32));
      if (gameOver) {
         g.drawString("GAME OVER: " + String.valueOf((int) score), 10, 35);
      } else {
         g.drawString(String.valueOf((int) score), 10, 35);
      }
   }

   public void move () {
      velocityY += gravity;
      bird.y += velocityY;
      bird.y = Math.max(bird.y, 0);

      for (int i = 0; i < pipes.size(); i++) {
         Pipe pipe = pipes.get(i);
         pipe.x += velocityX;

         if (!pipe.passed && bird.x > pipe.x + pipe.width) {
            pipe.passed = true;
            score += 0.5;
         }

         if (collision(bird, pipe)) {
            gameOver = true;
         }
      }

      if (bird.y > boardHeight) {
         gameOver = true;
      }
   }

   public boolean collision (Bird b, Pipe p) {
      return b.x < p.x + p.width &&
            b.x + b.width > p.x &&
            b.y < p.y + p.height &&
            b.y + b.height > p.y;
   }

   @Override
   public void actionPerformed (ActionEvent e) {
      move();
      repaint();
      if (gameOver) {
         placePipesTimer.stop();
         gameLoop.stop();
      }
   }

   @Override
   public void keyPressed (KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
         velocityY = -9;
         if (gameOver) {
            // Reiniciar el juego
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();
         }
      }
   }

   @Override
   public void keyTyped (KeyEvent e) {}

   @Override
   public void keyReleased (KeyEvent e) {}
}
