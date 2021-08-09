import java.util.Collections;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class DFBnB implements Algorithm {

    Game game;
    int num = 1;// count the the nodes.

    DFBnB(Game game) {
        this.game = game;
    }

    @Override
    public Result run() {
        Node start = new Node(game.puzzle, "", null, 0, 0);
        Hashtable<String, Node> lH = new Hashtable<>();
        Stack<Node> lS = new Stack<Node>();
        lH.put(Operators.createMatKey(start.puzzle), start);
        lS.push(start);
        Result result = null;
        int threshold = Integer.MAX_VALUE;
        int indexOfNode = 1;
        while (!lS.isEmpty()) {
            if(game.withOpen){
                Operators.PrintOpenList(lH); //print the open list
            }
            Node n = lS.pop();
            n.key = indexOfNode;
            if (n.out == true) {
                lH.remove(Operators.createMatKey(n.puzzle));
            } else {
                n.out = true;
                lS.push(n);
                Vector<Node> operators = Operators.operators(n, game.numberOfEmptyBlocks);
                num += operators.size();
                for (Node node : operators) {
                    node.key = (++indexOfNode);
                    node.sum += n.sum;
                }

                /* sort the vector by the heuristic function and the Node key
                    (cheap heuristic value first and if equals -  younger key first) */
                Collections.sort(operators, (o1, o2) -> {
                    int distA = Heuristic.heuristic(o1, game);
                    int distB = Heuristic.heuristic(o2, game);
                    if (distA > distB) {
                        return 1;
                    } else if (distA < distB) {
                        return -1;
                    } else {
                        if (o1.key > o2.key) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });

                for (int i = 0; i < operators.size(); i++) {
                    Node operNode = operators.get(i);

                    int hOnOperNode = Heuristic.heuristic(operNode, game);

                    if (hOnOperNode >= threshold) {
                        while (i < operators.size()) {
                            operators.remove(i);
                        }
                    } else if (lH.containsKey(Operators.createMatKey(operNode.puzzle))
                            && lH.get(Operators.createMatKey(operNode.puzzle)).out) {

                        operators.remove(operNode);

                    } else if (lH.containsKey(Operators.createMatKey(operNode.puzzle))
                            && !lH.get(Operators.createMatKey(operNode.puzzle)).out) {

                        if (Heuristic.heuristic(lH.get(Operators.createMatKey(operNode.puzzle)), game) <= hOnOperNode) {

                            operators.remove(operNode);

                        } else {

                            lH.remove(Operators.createMatKey(operNode.puzzle));
                            lS.removeIf(node -> Operators.createMatKey(node.puzzle).equals(Operators.createMatKey(operNode.puzzle)));

                        }
                    } else if (Operators.equalMatrix(operNode.puzzle, game.goalState)) {
                        threshold = hOnOperNode;
                        String path = "";
                        for (Node state : lS) {
                            if (state.out == true) {
                                path += state.states;
                            }
                        }
                        path+= operNode.states;
                        result = new Result(path.substring(0, path.length() - 1), num, operNode.sum);
                        while (i < operators.size()) {
                            operators.remove(i);
                        }
                    }
                }
                Collections.reverse(operators);
                for (Node operNode : operators) {
                    lH.put(Operators.createMatKey(operNode.puzzle), operNode);
                    lS.push(operNode);
                }
            }
        }
        if(result == null){
            result = new Result("no path",num,0);
        }
        return result;
    }
}