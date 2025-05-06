import java.awt.Color;
import java.awt.Graphics2D;

public class Line {
   private int x;
   private int y;
   private int lineWidth;
   private int lineHeight;
   private Color color;

   public Line (int x, int y, int lineWidth, int lineHeight, Color color) {
      this.x = x;
      this.y = y;
      this.lineWidth = lineWidth;
      this.lineHeight = lineHeight;
      this.color = color;
   }

   public void move (int dy, int surfaceHeight) {
      y += dy;
      if (y <= 5) {
         y = 2;
      }
      if (y + lineHeight - 5 > surfaceHeight) {
         y = surfaceHeight - lineHeight - 5;
      }
   }

   public void paint (Graphics2D g2d) {
      g2d.setColor(color);
      g2d.fillRect(x, y, lineWidth, lineHeight);
   }

   public int getX () {
      return x;
   }

   public int getY () {
      return y;
   }

   public int getWidth () {
      return lineWidth;
   }

   public int getHeight () {
      return lineHeight;
   }

   public void setY (int y) {
      this.y = y;
   }
}
