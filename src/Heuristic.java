
public class Heuristic {
    /*This function gets Node and Game and returns the heuristic function from this node to the game goal*/
    public static int heuristic (Node a ,Game game){
        return a.sum + (int)(manhattanDistance(a.puzzle,game.goalState, game.numberOfEmptyBlocks));
    }
    /*This function gets current node puzzle, the goal puzzle and the number of empty tiles,
        the function calculates the manhatten distance between the current node and the goal
            and multiple it by hypothesis cost */
    public static double manhattanDistance(String[][] mat1, String[][] mat2, int numOfEmptyBlocks) {
        int dist = 0;
        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[i].length; j++) {
                if (mat1[i][j].equals("_")){
                    continue;
                }
                dist += findAndCalcDist(mat1[i][j], i, j, mat2);
            }
        }
        if (numOfEmptyBlocks == 2){
            return dist*3;
        }
        return dist*5;
    }
    /*This function gets current tile String value, two integers that represent it's position and the goal puzzle,
            the function search the given tile position on the goal puzzle and calculate the manhatten distance between them. */

    private static int findAndCalcDist(String tile, int i, int j, String[][] mat2) {
        for (int k = 0; k < mat2.length; k++) {
            for (int l = 0; l < mat2[k].length; l++) {
                if (mat2[k][l].equals("_")) continue;
                if (mat2[k][l].equals(tile)) {
                    return Math.abs(i - k) + Math.abs(j - l);
                }
            }
        }
        return 0;
    }

}
