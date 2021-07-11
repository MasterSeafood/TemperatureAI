import java.util.Random;

public class TempGenerator {

    Random random = new Random();
    int intTemp;

    public TempGenerator(){
        intTemp = 0;
    }


    public void generateTemp(){
        intTemp = random.nextInt(100) + 1;
    }

    public int answerGuess(int intGuess){
        if(intGuess > intTemp) //guess is too high
            return 0;
        else if(intGuess < intTemp) //guess is too low
            return 1;
        else 
            return 2;
    }
}
