public class Node {
    /* Node represent a state/operator of the game */
    String[][] puzzle;
    String states = "";
    Node prevState;
    int cost;
    int sum ;
    Boolean out = false;
    Boolean TwoAdjacentcells = false;
    int key;

    Node( String[][] puzzle, String states, Node prevState, int cost, int sum){
        this.puzzle = puzzle;
        this.states = states;
        this.prevState = prevState;
        this.cost = cost;
        this.sum = sum;
    }
}
