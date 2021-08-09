import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Ex1 {

    public static Result runGame(Game game) {
        Algorithm result;
        switch (game.algoType) {
            case ReadFile.BFS:
                result = new BFS(game);
                break;
            case ReadFile.DFID:
                result = new DFID(game);
                break;
            case ReadFile.A_STAR:
                result = new A_STAR(game);
                break;
            case ReadFile.IDA_STAR:
                result = new IDA_STAR(game);
                break;
            default:
                result = new DFBnB(game);
        }
        return result.run();
    }


    public static void main(String[] args) {

        Game game = ReadFile.readFile("input.txt");

        double startTime = System.nanoTime();
        Result ans = runGame(game);
        double endTime = System.nanoTime();

        double totalTime = (endTime - startTime) / 1000000000;

        DecimalFormat df = new DecimalFormat("#.###");
        String dx = df.format(totalTime);
        totalTime = Double.valueOf(dx);
        ans.time = totalTime;
        String answer;
        if (ans.path.equals("no path")){
            answer = "no path"+"\n"+
                    "Num: "+ans.num;
        }
        else answer = ans.toString(game.withTime);

        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(answer);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
