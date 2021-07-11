import java.util.Random;

public class OffspringVariator {

    public static Agent variate(Agent parent){
        Random geneRandom = new Random();
        double mean = 0.0;
        double stdDev = 0.1;
    
        double childMin = parent.min + (geneRandom.nextGaussian()*stdDev + mean);
        double childMax = parent.max + (geneRandom.nextGaussian()*stdDev + mean);
        double childMid = parent.mid + (geneRandom.nextGaussian()*stdDev + mean);

        if(childMin < 0)
            childMin = 0;
        else if (childMin > 1)
            childMin = 1;

        if(childMax < 0)
            childMax = 0;
        else if (childMax > 1)
            childMax = 1;

        if(childMid < 0)
            childMid = 0;
        else if (childMid > 1)
            childMid = 1;

        return new Agent(childMin, childMax, childMid);
    }
}
