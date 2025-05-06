
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends WindowAdapter {
	private final JFrame frame;
	private Surface surface = new Surface(650, 450);

	public Main() {
      frame = new JFrame("Pong Game");
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.addWindowListener(this);
      frame.add(surface, BorderLayout.CENTER);
      frame.pack();
		frame.setLocationRelativeTo(null);
	}

	public void iniciar() {
		frame.setVisible(true);
		surface.start();
	}

	@Override
	public void windowClosing(java.awt.event.WindowEvent e) {
		surface.stop();
		frame.dispose();
		System.exit(0);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main()::iniciar);
	}
}
