public class MemoryCard {
    private char value;
    private boolean guess = false;
    public MemoryCard(char value) {
        this.value = value;
    }
    public char getValue() {
        return value;
    }
    public void setValue(char value) {
        this.value = value;
    }
    public boolean isGuess() {
        return guess;
    }
    public void setGuess(boolean guess) {
        this.guess = guess;
    }


}
