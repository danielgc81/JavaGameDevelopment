import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Sudoku {
   class Tile extends JButton {
      int tileRow;
      int tileCol;

      Tile (int tileRow, int tileCol) {
         this.tileRow = tileRow;
         this.tileCol = tileCol;

         setOpaque(true);
         setContentAreaFilled(true);
      }

   }

   int boardWidth = 600;
   int boardHeight = 650;

   ArrayList<Tile> allTiles = new ArrayList<>();

   String[] puzzle = {
      "--74916-5",
      "2---6-3-9",
      "-----7-1-",
      "-586----4",
      "--3----9-",
      "--62--187",
      "9-4-7---2",
      "67-83----",
      "81--45---"
   };

   String[] solution = {
      "387491625",
      "241568379",
      "569327418",
      "758619234",
      "123784596",
      "496253187",
      "934176852",
      "675832941",
      "812945763"
   };

   // This frame will display three panels for the number of errors, the sudoku game and the numbers
   JFrame frame = new JFrame("Sudoku");

   JLabel errorsText = new JLabel();
   JPanel errorsPanel = new JPanel();

   JPanel boardPanel = new JPanel();

   JPanel numbersPanel = new JPanel();

   JButton numSelected = null;
   int errors = 0;
   ArrayList<Tile> guessTiles = new ArrayList<>();

   Sudoku () {
      errorsText.setFont(new Font("Arial", Font.BOLD, 30));
      errorsText.setHorizontalAlignment(JLabel.CENTER);
      errorsText.setText("SUDOKU: 0");

      errorsPanel.add(errorsText);

      boardPanel.setLayout(new GridLayout(9, 9));
      setupTiles();

      numbersPanel.setLayout(new GridLayout(1, 10));
      setupNumbersButton();

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
      frame.setLayout(new BorderLayout());
      frame.add(errorsPanel, BorderLayout.NORTH);
      frame.add(boardPanel, BorderLayout.CENTER);
      frame.add(numbersPanel, BorderLayout.SOUTH);
      frame.pack();
      frame.setVisible(true);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
   }

   public void setupTiles () {
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            Tile tile = new Tile(i, j);
            char tileChar = puzzle[i].charAt(j);
            if (tileChar != '-') {
               tile.setText(String.valueOf(tileChar));
               tile.setFont(new Font("Arial", Font.BOLD, 20));
               tile.setBackground(Color.LIGHT_GRAY);
               allTiles.add(tile);
               boardPanel.add(tile);
            } else {
               tile.setText("");
               tile.setFont(new Font("Arial", Font.BOLD, 20));
               tile.setBackground(Color.WHITE);
               allTiles.add(tile);
               boardPanel.add(tile);
            }

            // Creando bordes
            if ((i == 2 && j == 2) || (i == 2 && j == 5) || (i == 5 && j == 2) || (i == 5 && j == 5)) {
               tile.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.BLACK));
            } else if (i == 2 || i == 5) {
               tile.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 1, Color.BLACK));
            } else if (j == 2 || j == 5) {
               tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 4, Color.BLACK));
            } else {
               tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
               public void actionPerformed (ActionEvent e) {
                  Tile tile = (Tile) e.getSource();
                  int tileRow = tile.tileRow;
                  int tileCol = tile.tileCol;
                  if (numSelected != null) {
                     if (tile.getText() != "") {
                        return;
                     }
                     String numSelectedText = numSelected.getText();
                     String tileSolution = String.valueOf(solution[tileRow].charAt(tileCol));
                     if (tileSolution.equals(numSelectedText)) {
                        tile.setText(numSelectedText);
                        guessTiles.add(tile);
                     } else {
                        errors += 1;
                        errorsText.setText("SUDOKU: " + errors);
                     }

                     int counter = 0;
                     for (Tile t : allTiles) {
                        if (t.getText() != "") {
                           counter++;
                        }
                     }
                     if (counter == allTiles.size()) {
                        errorsText.setText("SUDOKU COMPLETED - " + errors + " ERRORS");
                     }
                  }
               }
            });
         }
      }
   }

   public void setupNumbersButton () {
      for (int i = 0; i < 9; i++) {
         int num = i + 1;
         JButton number = new JButton(String.valueOf(num));
         number.setOpaque(true);
         number.setContentAreaFilled(true);
         number.setFont(new Font("Arial", Font.PLAIN, 16));
         number.setFocusable(false);
         numbersPanel.add(number);

         number.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               JButton button = (JButton) e.getSource();
               if (numSelected != null) {
                  numSelected.setBackground(Color.WHITE);
               }
               numSelected = button;
               numSelected.setBackground(Color.LIGHT_GRAY);
            }
         });
      }

      JButton resetButton = new JButton("R");
      resetButton.setFont(new Font("Arial", Font.PLAIN, 16));
      resetButton.addActionListener(new ActionListener() {
         public void actionPerformed (ActionEvent e) {
            reset();
         }
      });
      numbersPanel.add(resetButton);
   }

   public void reset () {
      for (Tile guessTile : guessTiles) {
         guessTile.setText("");
      }
      errors = 0;
      errorsText.setText("SUDOKU: " + errors);
   }
}
