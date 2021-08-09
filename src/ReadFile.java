import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    final static String BFS = "BFS";
    final static String DFID = "DFID";
    final static String A_STAR = "A*";
    final static String IDA_STAR = "IDA*";
    final static String DFBnB = "DFBnB";

    final static String WITH_TIME = "with time";
    final static String WITH_OPEN = "with open";

    final static String GOAL_STATE = "Goal state:";

    final static String EMPTY_CELL = "_";


    static String algoType;
    static int n;
    static int m;
    static boolean withTime;
    static boolean withOpen;
    static int numberOfEmptyBlocks = 0;
    static String[][] puzzle;
    static String[][] goalState;

/*these function the input file and returns a game with all the given data */
    public static Game readFile(String filename) {
        Game game;
        try {
            File input = new File(filename);
            Scanner sc = new Scanner(input);
            int lineIndex = 0;
            String data;
            // line 1
            algoType = sc.nextLine();

            // line 2
            data = sc.nextLine();
            withTime = data.trim().equals(WITH_TIME) ? true : false;

            // line 3
            data = sc.nextLine();
            withOpen = data.trim().equals(WITH_OPEN) ? true : false;

            // line 4
            data = sc.nextLine();
            String[] sizeNxM = data.split("x");
            n = Integer.parseInt(sizeNxM[0]);
            m = Integer.parseInt(sizeNxM[1]);

            // line 5
            puzzle = new String[n][m];
            data = sc.nextLine();
            int i = 0;
            while (i < n) {
                String[] row = data.split(",");
                puzzle[i] = row;
                i++;
                data = sc.nextLine();
            }

            // line 5 + n
            i = 0;
            goalState = new String[n][m];
            while (sc.hasNextLine()) {
                data = sc.nextLine();
                String[] row = data.split(",");
                goalState[i] = row;
                i++;

                // Counting the amount of empty blocks
                for (String s : row) {
                    if (s.equals(EMPTY_CELL)) {
                        numberOfEmptyBlocks++;
                    }
                }
            }
            game = new Game(algoType, n, m, withTime, withOpen, numberOfEmptyBlocks, puzzle, goalState);

            return game;
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the requested file");
            e.printStackTrace();
            return null;
        }
    }
}
