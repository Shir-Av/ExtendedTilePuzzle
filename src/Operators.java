import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class Operators {
    final static String RIGHT = "R";
    final static String DOWN = "D";
    final static String LEFT = "L";
    final static String UP = "U";
    static Node puzzleNode;
    static int numOfEmpty;

    /*
    This function returns vector of all the possible operators for a given node
                                                                                */
    public static Vector<Node> operators(Node n, int numOfEmptyCells) {
        puzzleNode = n;
        numOfEmpty = numOfEmptyCells;
        String[][] puzzle = n.puzzle;
        if (numOfEmptyCells == 1) {
            for (int i = 0; i < puzzle.length; i++) {
                for (int j = 0; j < puzzle[i].length; j++) {
                    if (puzzle[i][j].equals("_")) {
                        Point p = new Point(i, j);
                        return oneCellsOperator(puzzle, p); // calculate operators for puzzle with one empty tile
                    }
                }
            }
        } else {
            int countCells = 0;
            Point p1 = null;
            for (int i = 0; i < puzzle.length; i++) {
                for (int j = 0; j < puzzle[i].length; j++) {
                    if (puzzle[i][j].equals("_")) {
                        countCells++;
                        if (countCells == 1) {
                            p1 = new Point(i, j);
                        } else {
                            Point p2 = new Point(i, j);
                            return twoCellsOperator(puzzle, p1, p2);// calculate operators for puzzle with two empty tiles
                        }

                    }
                }
            }
        }
        return new Vector<>();
    }

    /*
      This function returns vector of operators for any kind of "tile position" (i.e. the tile position is the left top corner)
      the function calls another function that builds the operators named 'operator',
          and adding it to a vector, if the function 'operator' returns null, the function deletes it from the vector.
                                                                                                                       */
    public static Vector<Node> oneCellsOperator(String[][] puzzle, Point p) {
        int row = puzzle.length;
        int col = puzzle[0].length;
        Vector<Node> operators = new Vector<>();
        if (p.x == 0 && p.y == 0) { //operators for left top corner
            if(col > 1) operators.add(operator(puzzle, p, new Point(0, 1), LEFT));
            if(row > 1) operators.add(operator(puzzle, p, new Point(1, 0), UP));
            operators.removeAll(Collections.singleton(null));  //operators.indexOf(null);
            return operators;
        }
        if (p.x == 0 && p.y == puzzle[0].length - 1) { //operators for right top corner
            if(row > 1) operators.add(operator(puzzle, p, new Point(1, puzzle[0].length - 1), UP));
            operators.add(operator(puzzle, p, new Point(0, puzzle[0].length - 2), RIGHT));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if (p.x == puzzle.length - 1 && p.y == 0) { //operators for left bottom corner
            if(col > 1) operators.add(operator(puzzle, p, new Point(puzzle.length - 1, 1), LEFT));
            operators.add(operator(puzzle, p, new Point(puzzle.length - 2, 0), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if (p.x == puzzle.length - 1 && p.y == puzzle[0].length - 1) { //operators for Right bottom corner
            operators.add(operator(puzzle, p, new Point(puzzle.length - 1, puzzle[0].length - 2), RIGHT));
            operators.add(operator(puzzle, p, new Point(puzzle.length - 2, puzzle[0].length - 1), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if (p.x == 0 && (p.y > 0 && p.y < puzzle[0].length - 1)) { //operators for top line
            operators.add(operator(puzzle, p, new Point(0, p.y + 1), LEFT));
            if(row > 1) operators.add(operator(puzzle, p, new Point(1, p.y), UP));
            operators.add(operator(puzzle, p, new Point(0, p.y - 1), RIGHT));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if ((p.x > 0 && p.x < puzzle.length - 1) && p.y == 0) { //operators left top line
            if(col > 1) operators.add(operator(puzzle, p, new Point(p.x, 1), LEFT));
            operators.add(operator(puzzle, p, new Point(p.x + 1, 0), UP));
            operators.add(operator(puzzle, p, new Point(p.x - 1, 0), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if (p.x == puzzle.length - 1 && (p.y > 0 && p.y < puzzle[0].length - 1)) { //operators bottom top line
            operators.add(operator(puzzle, p, new Point(puzzle.length - 1, p.y + 1), LEFT));
            operators.add(operator(puzzle, p, new Point(puzzle.length - 1, p.y - 1), RIGHT));
            operators.add(operator(puzzle, p, new Point(puzzle.length - 2, p.y), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        if ((p.x > 0 && p.x < puzzle.length - 1) && p.y == puzzle[0].length - 1) { //operators for right line
            operators.add(operator(puzzle, p, new Point(p.x + 1, puzzle[0].length - 1), UP));
            operators.add(operator(puzzle, p, new Point(p.x, puzzle[0].length - 2), RIGHT));
            operators.add(operator(puzzle, p, new Point(p.x - 1, puzzle[0].length - 1), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
        else { //operators for tile somewhere in the middle
            operators.add(operator(puzzle, p, new Point(p.x, p.y + 1), LEFT));
            operators.add(operator(puzzle, p, new Point(p.x + 1, p.y), UP));
            operators.add(operator(puzzle, p, new Point(p.x, p.y - 1), RIGHT));
            operators.add(operator(puzzle, p, new Point(p.x - 1, p.y), DOWN));
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
    }

    /*
      This function returns vector of operators for any kind of "2 tiles position" (i.e. the tiles position is the left top corner).
        the function calls 2 functions that builds the operators : 'operatorForTwo' and 'operator',
          and adding it to a vector, if one of the functions returns null the function deletes it from the vector.
                                                                                                                    */
    public static Vector<Node> twoCellsOperator(String[][] puzzle, Point p1, Point p2) {
        int row = puzzle.length;
        int col = puzzle[0].length;
        Vector<Node> operators = new Vector<>();

        if ((Math.abs(p1.x - p2.x) == 1) && (p1.y == p2.y)) { // if the tiles are Adjacent vertically
            if ((p1.x == 0 && p1.y == 0)) { //operators for Left top corner
                if(col > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                    operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                    operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                }
                if(row > 2) operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));//can fall on oob
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == 0 && p1.y == puzzle[0].length - 1)) {//operators for Right top corner
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                if(row > 2) operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));//can fall on oob
                operators.add(operator(puzzle, p2, new Point(p2.x , p2.y - 1), RIGHT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == puzzle.length - 2 && p1.y == 0)) {//operators for Left Bottom corner
                if(col > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                    operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                    operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                    operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                }
                else operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == puzzle.length - 2 && p1.y == puzzle[0].length - 1)) {//operators for Right Bottom corner
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x , p2.y - 1), RIGHT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if (p1.x == 0 && (p1.y > 0 && p1.y < puzzle[0].length - 1)) {//operators for Top line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                if(row > 2) operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.add(operator(puzzle, p2, new Point(p2.x , p2.y - 1), RIGHT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x > 0 && p1.x < puzzle.length - 1) && p1.y == 0) {//operators for Left line
                if(col > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                    operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                }
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                if(col > 1) operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if (p1.x == puzzle.length - 2 && (p1.y > 0 && p1.y < puzzle[0].length - 1)) {//operators for Bottom line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x , p2.y - 1), RIGHT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x > 0 && p1.x < puzzle.length - 1) && p1.y == puzzle[0].length - 1) {//operators for Right line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            else{//operators for 2 tile somewhere in the middle
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y + 1),new Point(p2.x , p2.y + 1), LEFT));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x, p1.y - 1),new Point(p2.x , p2.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y + 1), LEFT));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p2.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.add(operator(puzzle, p2, new Point(p2.x , p2.y - 1), RIGHT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
        }

        if ((Math.abs(p1.y - p2.y) == 1) && (p1.x == p2.x)) {// if the tiles are Adjacent horizontally
            if ((p1.x == 0 && p1.y == 0)) { //operators for Left top corner
                if(row > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1, p2.y), UP));
                    operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                    operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                }
                if(col > 2) operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == 0 && p1.y == puzzle[0].length - 2)) { //operators for Right top corner
                if(row > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1 , p2.y), UP));
                    operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                }
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                if(row > 1) operators.add(operator(puzzle, p2, new Point(p2.x + 1 , p2.y), UP));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == puzzle.length - 1 && p1.y == 0)) { //operators for Left bottom corner
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                if(col > 2) operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1, p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x == puzzle.length - 1 && p1.y == puzzle[0].length - 2)) { //operators for Right bottom corner
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1 , p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if (p1.x == 0 && (p1.y > 0 && p1.y < puzzle[0].length - 1)) { //operators for Top line
                if(row > 1){
                    operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1 , p2.y), UP));
                    operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                }
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                if(row > 1) operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x > 0 && p1.x < puzzle.length - 1) && p1.y == 0) {//operators for Left line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1 , p2.y), UP));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                if(col > 2) operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1 , p2.y), UP));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1 , p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if (p1.x == puzzle.length - 1 && (p1.y > 0 && p1.y < puzzle[0].length - 1)) {//operators for Bottom line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1 , p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            if ((p1.x > 0 && p1.x < puzzle.length - 1) && p1.y == puzzle[0].length - 2) {//operators for right line
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1 , p2.y), UP));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1 , p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
            else{ //operators for 2 tile somewhere in the middle
                printmat(puzzle);
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x + 1, p1.y),new Point(p2.x + 1 , p2.y), UP));
                operators.add(operatorForTwo(puzzle,p1,p2,new Point(p1.x - 1, p1.y),new Point(p2.x - 1 , p2.y), DOWN));
                operators.add(operator(puzzle, p1, new Point(p1.x + 1, p1.y), UP));
                operators.add(operator(puzzle, p1, new Point(p1.x, p1.y - 1), RIGHT));
                operators.add(operator(puzzle, p1, new Point(p1.x - 1, p1.y), DOWN));//can fall on oob
                operators.add(operator(puzzle, p2, new Point(p2.x, p2.y + 1), LEFT));
                operators.add(operator(puzzle, p2, new Point(p2.x + 1, p2.y), UP));
                operators.add(operator(puzzle, p2, new Point(p2.x - 1 , p2.y), DOWN));
                operators.removeAll(Collections.singleton(null));
                return operators;
            }
        }
        else { //operators for two Separated tiles
            Vector<Node> v1 = oneCellsOperator(puzzle , p1);
            Vector<Node> v2 = oneCellsOperator(puzzle , p2);
            operators.addAll(v1);
            operators.addAll(v2);
            operators.removeAll(Collections.singleton(null));
            return operators;
        }
    }

    /*
      This function gets puzzle, two points that are the indexs of the empty tile and the number tile and the direction,
       the function swap the tiles and returns new node of the new operator with it's state and cost.
        the function also check if the previous operator equals to the current operator and returns null if they are equals.
                                                                                                                            */
    public static Node operator(String[][] puzzle, Point empty, Point number, String direction) {
        String[][] oper = copyPuzzle(puzzle);
        oper[empty.x][empty.y] = puzzle[number.x][number.y];
        oper[number.x][number.y] = puzzle[empty.x][empty.y];
        if(puzzleNode.prevState != null && equalMatrix(oper,puzzleNode.prevState.puzzle)){
            return null;
        }
        String state = puzzle[number.x][number.y].trim() + direction+"-" ;
        Node newNode = new Node(oper, state, puzzleNode,5,5);

        if(numOfEmpty == 2){
            if(puzzleNode.prevState != null){
                String d = "";
                d += puzzleNode.states.charAt(puzzleNode.states.length()-2);
                if((d.equals(RIGHT) && direction.equals(LEFT)) || (d.equals(LEFT) && direction.equals(RIGHT))
                        || (d.equals(UP) && direction.equals(DOWN)) || (d.equals(DOWN) && direction.equals(UP))) return null;
            }
            newNode.TwoAdjacentcells = neighbors(oper,number.x,number.y);
        }

        return newNode;
    }
    /*
       This function gets puzzle, four points that are the indexs of the 2 empty tile and the 2 number tile and a direction,
        the function swap the tiles and returns new node of the new operator with it's state and cost.
         the function also check if the previous operator equals to the current operator and returns null if they are equals.
                                                                                                                              */
    public static Node operatorForTwo(String[][] puzzle, Point empty1, Point empty2, Point number1, Point number2, String direction) {
        String[][] oper = copyPuzzle(puzzle);
        oper[empty1.x][empty1.y] = puzzle[number1.x][number1.y];
        oper[number1.x][number1.y] = puzzle[empty1.x][empty1.y];

        oper[empty2.x][empty2.y] = puzzle[number2.x][number2.y];
        oper[number2.x][number2.y] = puzzle[empty2.x][empty2.y];

        if(puzzleNode.prevState != null && equalMatrix(oper,puzzleNode.prevState.puzzle)){
            return null;
        }

        String state = puzzle[number1.x][number1.y].trim() + "&" + puzzle[number2.x][number2.y].trim() + direction+"-";
        int cost;
        if(direction.equals(UP) || direction.equals(DOWN)){
            cost = 7;
        }
        else {
            cost = 6;
        }
        Node newNode = new Node(oper, state, puzzleNode, cost, cost);
        newNode.TwoAdjacentcells = true;
        return newNode;
    }

    /*
       This function gets puzzle and returns a copy of it
                                                         */
    public static String[][] copyPuzzle(String[][] puzzle) {
        String[][] oper = new String[puzzle.length][puzzle[0].length];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                oper[i][j] = puzzle[i][j];
            }
        }
        return oper;
    }

    /*
      This function gets puzzle and returns it as a string,
      used for the HashTable key.
                                                           */
    public static String createMatKey(String[][] mat) {
        String matStr = "";
        for (String[] row : mat) {
            matStr += Arrays.toString(row);
        }
        return matStr;
    }

    /*
      This function gets 2 puzzles and returns true if both equals,
      used when check if we reached the goal
                                                                   */

    public static boolean equalMatrix(String[][] matA, String[][] matB) {
        boolean match = true;
        for (int i = 0; i < matA.length; i++) {
            for (int j = 0; j < matA[i].length; j++)
                if (!matA[i][j].equals(matB[i][j])) {
                    match = false;
                }
        }

        return match;
    }

    /*
          This function gets puzzle and prints it,
          used to print the open list.
                                                 */
    public static void printmat(String [][]mat){
        for (int i=0 ; i<mat.length ; i++) {
            System.out.println(Arrays.toString( mat[i]));
        }
        System.out.println();
    }

    /*
          This function gets Hash Table and prints it's values,
          used to print the open list.
                                                 */
    public static void PrintOpenList(Hashtable<String, Node> open){
        System.out.println("--------------------------OPEN LIST--------------------------");
        for(Node n  : open.values()){
            printmat(n.puzzle);
        }
        System.out.println("-------------------------------------------------------------");
    }

    /*
      This function gets puzzle and index and return true if there is an empty cell adjacent to the given point,
                                                                                                                 */
    public static boolean neighbors(String mat [][], int i, int j){
        if(i-1 >= 0 && mat[i-1][j].equals("_"))return true;
        if(i+1 < mat.length && mat[i+1][j].equals("_"))return true;
        if(j-1 >= 0 && mat[i][j-1].equals("_"))return true;
        if(j+1 < mat[0].length && mat[i][j+1].equals("_"))return true;
        return false;
    }
}

