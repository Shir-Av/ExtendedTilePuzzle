import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class IDA_STAR  implements Algorithm{

    Game game;
    int num = 1;// count the the nodes.
    IDA_STAR(Game game) {
        this.game = game;
    }

    @Override
    public Result run() {
        Node start = new Node(game.puzzle, "", null, 0, 0);
        Hashtable<String, Node> lH = new Hashtable<>();
        Stack<Node> lS=new Stack<Node>();
        int threshold = (int)(Heuristic.manhattanDistance(start.puzzle,game.goalState,game.numberOfEmptyBlocks));
        while(threshold != Integer.MAX_VALUE) {
            int minF = Integer.MAX_VALUE;
            start.out = false;
            lH.put(Operators.createMatKey(start.puzzle),start);
            lS.push(start);

            while (!lS.isEmpty()){
                if(game.withOpen){
                    Operators.PrintOpenList(lH);
                }
                Node n = lS.pop();
                if(n.out == true){
                    lH.remove(Operators.createMatKey(n.puzzle));
                }
                else{
                    n.out = true;
                    lS.push(n);

                    Vector<Node> operators = Operators.operators(n, game.numberOfEmptyBlocks);
                    num += operators.size();

                    for (Node operNode : operators) {
                        operNode.sum += n.sum;
                        operNode.states = n.states + operNode.states;
                        int hOnOperNode = Heuristic.heuristic(operNode,game);
                        if(hOnOperNode > threshold){
                            minF = Math.min(minF,hOnOperNode);
                            continue;
                        }

                        if(lH.containsKey(Operators.createMatKey(operNode.puzzle)) && lH.get(Operators.createMatKey(operNode.puzzle)).out == true) {
                            continue;
                        }

                        if(lH.containsKey(Operators.createMatKey(operNode.puzzle)) && lH.get(Operators.createMatKey(operNode.puzzle)).out == false) {
                                if (Heuristic.heuristic(lH.get(Operators.createMatKey(operNode.puzzle)),game) > hOnOperNode){
                                    lH.remove(Operators.createMatKey(operNode.puzzle));
                                    lS.removeIf(node -> Operators.createMatKey(node.puzzle).equals(Operators.createMatKey(operNode.puzzle)));
                                }
                                else continue;
                        }
                        if (Operators.equalMatrix(operNode.puzzle, game.goalState)) {
                            return new Result(operNode.states.substring(0,operNode.states.length()-1),num,operNode.sum);
                        }
                        lS.push(operNode);
                        lH.put(Operators.createMatKey(operNode.puzzle),operNode);
                    }
                }
            }
            threshold = minF;
        }
        return new Result("no path",num,0);
    }
}