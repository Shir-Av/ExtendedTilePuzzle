import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;

import java.util.Hashtable;
import java.util.Vector;

public class DFID  implements Algorithm{

    Game game;
    int num = 1;// count the the nodes.
    DFID(Game game) {
        this.game = game;
    }

    @Override
    public Result run() {
        Node start = new Node(game.puzzle, "", null, 0,0);
        int depth = 0;
        while (true){
            depth++;
            Hashtable<String, Node> lH = new Hashtable<>();
            Result result = limited_DFS(start, depth, lH);
            if(game.withOpen){
                Operators.PrintOpenList(lH);
            }
            if (!result.path.equals("CutOff")){
                return result;
            }
        }
    }

    private Result limited_DFS(Node n, int limit, Hashtable<String, Node> lH){
        if (Operators.equalMatrix(n.puzzle, game.goalState)) {
            return new Result(n.states.substring(0,n.states.length()-1),num,n.sum);
        }
        else if (limit == 0){
            return new Result("CutOff",num,n.sum);
        }
        else{
            lH.put(Operators.createMatKey(n.puzzle), n);
            boolean isCutOff = false;

            Vector<Node> operators = Operators.operators(n, game.numberOfEmptyBlocks);
            for (Node operNode : operators) {
                num++;
                operNode.sum += n.sum;
                operNode.states = n.states + operNode.states;
                String operNodeKey = Operators.createMatKey(operNode.puzzle);
                if (lH.containsKey(operNodeKey)){
                    continue;
                }
                Result result = limited_DFS(operNode, limit-1, lH);
                if (result.path.equals("CutOff")){
                    isCutOff = true;
                }
                else if(!result.path.equals("Fail")){
                    return result;
                }
            }
            lH.remove(Operators.createMatKey(n.puzzle));
            if (isCutOff == true){
                return new Result("CutOff",num,n.sum);
            }
            else{
                return new Result("no path",num,0);
            }
        }

    }

}
