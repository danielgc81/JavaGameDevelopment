import javax.swing.JFrame;

public class App {
   public static void main(String[] args) throws Exception {
      int boardWidth = 360;
      int boardHeight = 640;

      JFrame frame = new JFrame("Flappy Bird");
      frame.setSize(boardWidth, boardHeight);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);

      FlappyBird flappyBird = new FlappyBird();
      frame.add(flappyBird);
      flappyBird.requestFocus();
      frame.pack();
      frame.setVisible(true);
   }
}
