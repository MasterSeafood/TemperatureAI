import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        int numGenerations = 1000;
        int populationSize = 100;
        int survivorsPopSize = 5;
        int numTrials = 20;

        Agent[] population = new Agent[populationSize];
        Agent[] survivorBuffer = new Agent[survivorsPopSize];

        TempGenerator generator = new TempGenerator();
        int guessCounter;
        int guess, response;
        int[][] agentScores = new int[numTrials][populationSize];
        double[] avgAgentScores = new double[populationSize];

        double lowestAvg;
        int lowestAgentIndex;
        ArrayList<Integer> survivorIndexRecord = new ArrayList<Integer>();

        double sumMinGene, sumMaxGene, sumMidGene;
        double meanMinGene = 0, meanMaxGene = 0, meanMidGene = 0;

        
            
        //READ GENESTORAGE 
        try {
            File geneStorageFile = new File("src/GeneStorage.txt");
            Scanner geneStorageReader = new Scanner(geneStorageFile);

            while (geneStorageReader.hasNextLine()) {

                //Store the min
                String data = geneStorageReader.nextLine();
                double minGeneStorage = Double.parseDouble(data.split(" ")[1]);
                
                //Store the max
                data = geneStorageReader.nextLine();
                double maxGeneStorage = Double.parseDouble(data.split(" ")[1]);

                //Store the mid
                data = geneStorageReader.nextLine();
                double midGeneStorage = Double.parseDouble(data.split(" ")[1]);

                // minGeneStorage = 0.0;
                // maxGeneStorage = 1.0;
                // midGeneStorage = 0.0;

                //populate the survivorBuffer with min, max, and mid genes
                for (int i = 0; i < survivorBuffer.length; i++) {
                    survivorBuffer[i] = new Agent(minGeneStorage, maxGeneStorage, midGeneStorage);
                }

            }
            geneStorageReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        for (int numGen = 0; numGen < numGenerations; numGen++) {//For every generation


            //VARIATE OFFSPRING
            for (int i = 0; i < survivorBuffer.length; i++) { //for each survivor
                for (int j = 0; j < populationSize/survivorsPopSize; j++) {
                    population[(populationSize/survivorsPopSize)*i + j] = OffspringVariator.variate(survivorBuffer[i]);
                }
            }
            
            //TEST EACH AGENT
            for (int nTrial = 0; nTrial < numTrials; nTrial++) {
                for (int i = 0; i < population.length; i++) {
                    guessCounter = 1;
                    generator.generateTemp();
                    HashMap<Integer, Integer> profile = new HashMap<Integer, Integer>();

                    while(true){
                        
                        guess = population[i].generateGuess(profile);
                        response = generator.answerGuess(guess);
                        if(response == 2){
                            break;
                        }else{
                            profile.put(guess, response);
                            guessCounter++;
                        }
                        
                        if(guessCounter>100){
                            break;
                        }
                        
                    }

                    agentScores[nTrial][i] = guessCounter;
                    population[i].clearPastGuesses();
                    //System.out.println("Gen " + numGen + " Trial " + nTrial + " Agent " + i + " took " + guessCounter + " guesses.");

                }
            }

            //average of each agents scores across each trial
            for (int i = 0; i < population.length; i++) {
                int sum = 0;
                for (int j = 0; j < agentScores.length; j++) {
                    sum+=agentScores[j][i];
                }

                double argScore = sum/agentScores.length;

                avgAgentScores[i] = argScore;
                
            }

            //REMOVE WEAK AGENTS
            survivorIndexRecord.clear();
            for (int i = 0; i < survivorBuffer.length; i++) { //for the amount of spots in survivorBuffer
                lowestAvg = 100;
                lowestAgentIndex = 0;
                for (int j = 0; j < avgAgentScores.length; j++) { //loop through test scores
                    if(avgAgentScores[j] < lowestAvg){ //if agent score is lower

                        if(!survivorIndexRecord.contains(j)){ //not on record
                            lowestAgentIndex = j;
                            lowestAvg = avgAgentScores[j];
        
                        }
                        
                    }
                }

                survivorIndexRecord.add(lowestAgentIndex);
                //System.out.println("Gen " + numGen + " " + i);
            }

            sumMinGene = 0;
            sumMaxGene = 0;
            sumMidGene = 0;

            meanMinGene = 0.0;
            meanMaxGene = 0.0;
            meanMidGene = 0.0;

            int avgSurvivorScore = 0;

            for (int i = 0; i < survivorBuffer.length; i++) {
                survivorBuffer[i] = population[survivorIndexRecord.get(i)];
                sumMinGene += survivorBuffer[i].min;
                sumMaxGene += survivorBuffer[i].max;
                sumMidGene += survivorBuffer[i].mid;
                avgSurvivorScore += avgAgentScores[survivorIndexRecord.get(i)];
            }

            avgSurvivorScore /= survivorBuffer.length;

            System.out.print("Gen " + numGen + " ");

            for (int i = 0; i < avgSurvivorScore; i++) {
                System.out.print("%");
            }
            System.out.println("");
            meanMinGene = sumMinGene/survivorBuffer.length;
            meanMaxGene = sumMaxGene/survivorBuffer.length;
            meanMidGene = sumMidGene/survivorBuffer.length;
            
            //WRITE TO GENE STORAGE

            // if(numGen % 5 == 0){
            //     try {
            //         FileWriter geneStorageWriter = new FileWriter("src/GeneStorage.txt");
            //         // geneStorageWriter.write("MIN: " + survivorBuffer[0].min + "\n");
            //         // geneStorageWriter.write("MAX: " + survivorBuffer[0].max + "\n");
            //         // geneStorageWriter.write("MID: " + survivorBuffer[0].mid);

            //         geneStorageWriter.write("MIN: " + meanMinGene + "\n");
            //         geneStorageWriter.write("MAX: " + meanMaxGene + "\n");
            //         geneStorageWriter.write("MID: " + meanMidGene);

            //         geneStorageWriter.close();
            //         //System.out.println("Successfully wrote to the file.");
            //     } catch (IOException e) {
            //         System.out.println("An error occurred.");
            //         e.printStackTrace();
            //     }
            // }
        }

        try {
            FileWriter geneStorageWriter = new FileWriter("src/GeneStorage.txt");
            // geneStorageWriter.write("MIN: " + survivorBuffer[0].min + "\n");
            // geneStorageWriter.write("MAX: " + survivorBuffer[0].max + "\n");
            // geneStorageWriter.write("MID: " + survivorBuffer[0].mid);

            geneStorageWriter.write("MIN: " + meanMinGene + "\n");
            geneStorageWriter.write("MAX: " + meanMaxGene + "\n");
            geneStorageWriter.write("MID: " + meanMidGene);

            geneStorageWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
}
