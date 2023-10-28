import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame {
    private MemoryCard[][] cards = new MemoryCard[4][4];
    private JButton[][] buttons = new JButton[4][4];
    private JButton startButton;
    private JButton quitButton;
    private MemoryCard firstCard;
    private MemoryCard secondCard;
    private Timer showCardsTimer;

    public MemoryGame() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 400);
        getContentPane().setBackground(Color.decode("#F0EAD6"));
        initializeGame();
    }

    private void initializeGame() {
        JPanel gamePanel = new JPanel(new GridLayout(4, 4));
        gamePanel.setBackground(Color.decode("#F0EAD6"));
        initializeCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.decode("#8ECae6"));
                buttons[i][j].addActionListener(new CardActionListener(i, j));
                gamePanel.add(buttons[i][j]);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.decode("#F0EAD6"));
        startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            showAllCardsForSeconds(3);
        });
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(startButton);
        controlPanel.add(quitButton);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initializeCards() {
        char[] cardValues = {'E', 'A', 'B', 'F', 'G', 'A', 'D', 'H', 'F', 'C', 'D', 'H', 'E', 'G', 'B', 'C'};
        ArrayList<Character> cardList = new ArrayList<>();

        for (char value : cardValues) {
            cardList.add(value);
        }

        Collections.shuffle(cardList);

        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cards[i][j] = new MemoryCard(cardList.get(index++));
            }
        }
    }

    private void showAllCardsForSeconds(double seconds) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j].setText(String.valueOf(cards[i][j].getValue()));
            }
        }

        showCardsTimer = new Timer((int) (seconds * 1000), e -> {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    buttons[i][j].setText("");
                }
            }
            showCardsTimer.stop();
        });
        showCardsTimer.setRepeats(false);
        showCardsTimer.start();
    }

    private class CardActionListener implements ActionListener {
        private final int row;
        private final int col;

        public CardActionListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (firstCard == null) {
                firstCard = cards[row][col];
                buttons[row][col].setText(String.valueOf(firstCard.getValue()));
                buttons[row][col].setEnabled(false);
            } else if (secondCard == null && firstCard != cards[row][col]) {
                secondCard = cards[row][col];
                buttons[row][col].setText(String.valueOf(secondCard.getValue()));
                buttons[row][col].setEnabled(false);

                Timer timer = new Timer(1000, event -> {
                    if (firstCard.getValue() == secondCard.getValue()) {
                        firstCard.setGuess(true);
                        secondCard.setGuess(true);
                        buttons[row][col].setBackground(Color.GREEN);
                        int firstCardRow = -1, firstCardCol = -1;
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                if (cards[i][j] == firstCard) {
                                    firstCardRow = i;
                                    firstCardCol = j;
                                    break;
                                }
                            }
                            if (firstCardRow != -1) break;
                        }
                        buttons[firstCardRow][firstCardCol].setBackground(Color.GREEN);
                    } else {
                        buttons[row][col].setText("");
                        buttons[row][col].setEnabled(true);
                        int firstCardRow = -1, firstCardCol = -1;
                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 4; j++) {
                                if (cards[i][j] == firstCard) {
                                    firstCardRow = i;
                                    firstCardCol = j;
                                    break;
                                }
                            }
                            if (firstCardRow != -1) break;
                        }
                        buttons[firstCardRow][firstCardCol].setText("");
                        buttons[firstCardRow][firstCardCol].setEnabled(true);
                    }
                    firstCard = null;
                    secondCard = null;
                });
                timer.setRepeats(false);
                timer.start();

                if (isGameCompleted()) {
                    JOptionPane.showMessageDialog(MemoryGame.this, "Congratulations! You completed the game!");
                    startButton.setEnabled(true);
                }
            }
        }
    }

    private boolean isGameCompleted() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!cards[i][j].isGuess()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryGame game = new MemoryGame();
            game.setVisible(true);
        });
    }

}

