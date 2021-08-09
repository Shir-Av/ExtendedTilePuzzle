import java.util.*;

public class BFS implements Algorithm {

    Game game;
    int num = 1; // count the the nodes.

    BFS(Game game) {
        this.game = game;
    }

    @Override
    public Result run() {
        Node start = new Node(game.puzzle, "", null,0,0);
        Queue<Node> lQ = new LinkedList<>();
        Hashtable<String, Node> lH = new Hashtable<>();
        Hashtable<String, Node> closeList = new Hashtable<>();

        lQ.add(start);
        lH.put(Operators.createMatKey(start.puzzle), start);
        while (!lQ.isEmpty()) {
            Node n = lQ.poll();
            if(game.withOpen){
                Operators.PrintOpenList(lH);
            }
            lH.remove(Operators.createMatKey(n.puzzle));
            closeList.put(Operators.createMatKey(n.puzzle), n);

            Vector<Node> operators = Operators.operators(n, game.numberOfEmptyBlocks);
            for (Node operNode : operators) {
                operNode.sum += n.sum;
                operNode.states = n.states + operNode.states;
                String operNodeKey = Operators.createMatKey(operNode.puzzle);
                num++;
                if (!lH.containsKey(operNodeKey) && !closeList.containsKey(operNodeKey)) {
                    if (Operators.equalMatrix(operNode.puzzle, game.goalState)) {
                        return new Result(operNode.states.substring(0,operNode.states.length()-1),num,operNode.sum);
                    }
                    lQ.add(operNode);
                    lH.put(operNodeKey, operNode);
                }
            }
        }
        return new Result("no path",num,0);
    }
}

