import java.util.*;

public class A_STAR implements Algorithm {
    Game game;
    int num = 1;// count the the nodes.

    A_STAR(Game game) {
        this.game = game;
    }
    @Override
    public Result run() {

        Node start = new Node(game.puzzle, "",  null, 0, 0);
        int indexOfNode = 1;

        /* priority queue by the heuristic function and the Node key
         (cheap heuristic value first and if equals -  younger key first) */
        Queue<Node> lQ = new PriorityQueue<>((o1, o2) -> {
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
        Hashtable<String, Node> lH = new Hashtable<>();
        Hashtable<String, Node> closeList = new Hashtable<>();

        lQ.add(start);
        lH.put(Operators.createMatKey(start.puzzle), start);
        while (!lQ.isEmpty()) {
            if(game.withOpen){
                Operators.PrintOpenList(lH);
            }
            Node n = lQ.poll();
            lH.remove(Operators.createMatKey(n.puzzle));
            n.key = indexOfNode;

            if (Operators.equalMatrix(n.puzzle, game.goalState)) {
                return new Result(n.states.substring(0, n.states.length() - 1), num, n.sum);
            }

            closeList.put(Operators.createMatKey(n.puzzle), n);

            Vector<Node> operators = Operators.operators(n, game.numberOfEmptyBlocks);
            num += operators.size();

            for (Node operNode : operators) {
                operNode.key = ++indexOfNode;
                operNode.sum += n.sum;
                operNode.states = n.states + operNode.states;
                String operNodeKey = Operators.createMatKey(operNode.puzzle);

                if (!lH.containsKey(operNodeKey) && !closeList.containsKey(operNodeKey)) {

                    lQ.add(operNode);
                    lH.put(operNodeKey, operNode);

                } else if (lH.containsKey(operNodeKey)) {
                    Node temp = lH.get(operNodeKey);
                    if (operNode.sum < temp.sum) {
                        lH.remove(operNodeKey);
                        lQ.removeIf(node -> Operators.createMatKey(node.puzzle).equals(operNodeKey));

                        lQ.add(operNode);
                        lH.put(operNodeKey, operNode);
                    }
                }
            }
        }
        return new Result("no path",num,0);
    }
}
