import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Agent {
    double min, max, mid;
    ArrayList<Integer> pastGuesses = new ArrayList<Integer>();

    public Agent(double min, double max, double mid){
        this.min = min;
        this.max = max;
        this.mid = mid;
    }

    public void clearPastGuesses(){
        pastGuesses.clear();
    }

    public int generateGuess(HashMap<Integer, Integer> inputProfile){
        ArrayList<Integer> boundGroup1 = new ArrayList<Integer>();
        ArrayList<Integer> boundGroup0 = new ArrayList<Integer>();

        int resp = 0;
        int lowerMin, higherMin, lowerMax, higherMax;
        int minRange, maxRange, midRange;
        int definiteMin, definiteMax, definiteMid;

        int pastGuessVariant = 0;



        

        for (Integer item : inputProfile.keySet()) {
            resp = inputProfile.get(item);
            if(resp == 1){
                boundGroup1.add(item);
            }else{
                boundGroup0.add(item);
            }
        }

        Collections.sort(boundGroup0);
        Collections.sort(boundGroup1);


        if(boundGroup1.size() != 0){
            lowerMin = boundGroup1.get(0);
            higherMin = boundGroup1.get(boundGroup1.size()-1);
            minRange = higherMin-lowerMin;

            definiteMin = (int) (lowerMin + (double) minRange * min);
        }else{
            definiteMin = 1;
        }

        if(boundGroup0.size() != 0){
            lowerMax = boundGroup0.get(0);
            higherMax = boundGroup0.get(boundGroup0.size()-1);
            maxRange = higherMax-lowerMax;

            definiteMax = (int) (lowerMax + (double) maxRange * max);
        }else{
            definiteMax = 100;
        }
        
        

        midRange = definiteMax-definiteMin;

        definiteMid = (int) (definiteMin + (double) midRange * mid);
        
        while(true){

            //System.out.println("Want to guess " + (definiteMid+pastGuessVariant));

            if(definiteMid + pastGuessVariant <= 100){
                if(!pastGuesses.contains(definiteMid+pastGuessVariant)){ //doesnt have above
                    pastGuesses.add(definiteMid+pastGuessVariant);
                    //System.out.println("chose " + (definiteMid+pastGuessVariant));
                    return definiteMid+pastGuessVariant;
                }
            }

            //System.out.println("Want to guess " + (definiteMid-pastGuessVariant));

            if(definiteMid-pastGuessVariant >= 1){
                if(!pastGuesses.contains(definiteMid-pastGuessVariant)){ //doesnt have above
                    pastGuesses.add(definiteMid-pastGuessVariant);
                    //System.out.println("chose " + (definiteMid-pastGuessVariant));
                    return definiteMid-pastGuessVariant;
                }
            }


            pastGuessVariant++;
        }
        
        
    }
    
}
