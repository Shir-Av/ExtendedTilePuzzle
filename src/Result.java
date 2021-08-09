public class Result {
    /* this class hols the result of the algoritms */
    String path;
    int num;
    int cost;
    double time;

    public Result(String path, int num, int cost){
        this.path = path;
        this.num = num;
        this.cost = cost;
        this.time = time;
    }

    public String toString(boolean withTime){
        String ans = this.path +"\n"+
                "Num: "+this.num+"\n"+
                "Cost: "+this.cost;
        if (withTime){
            ans += "\n"+ this.time + " seconds";
        }
        return ans;
    }
}
