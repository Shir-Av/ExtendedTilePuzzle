import java.util.Arrays;
/* the class holds all the given game data*/
public class Game {
    String algoType;
    int n;
    int m;

    boolean withTime;
    boolean withOpen;

    int numberOfEmptyBlocks;

    String[][] puzzle;
    String[][] goalState;

    Game(String algoType, int n, int m, boolean withTime, boolean withOpen, int numberOfEmptyBlocks, String[][] puzzle, String[][] goalState) {
        this.algoType = algoType;
        this.n = n;
        this.m = m;
        this.withTime = withTime;
        this.withOpen = withOpen;
        this.numberOfEmptyBlocks = numberOfEmptyBlocks;
        this.puzzle = puzzle;
        this.goalState = goalState;
    }

    @Override
    public String toString() {
        String puzzleStr = "";
        String goalStateStr = "";
        for (String[] s : puzzle) {
            puzzleStr += Arrays.toString(s) + "\n";
        }
        for (String[] s : goalState) {
            goalStateStr += Arrays.toString(s) + "\n";
        }

        return "Game { \n" +
                "\talgoType='" + algoType + '\'' +
                "\n\tn=" + n +
                "\n\tm=" + m +
                "\n\twithTime=" + withTime +
                "\n\twithOpen=" + withOpen +
                "\n\tnumberOfEmptyBlocks=" + numberOfEmptyBlocks +
                "\n\tpuzzle= \n" + puzzleStr +
                "\n\tgoalState= \n" + goalStateStr +
                "\n}";
    }
}