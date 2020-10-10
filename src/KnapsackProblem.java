import java.security.InvalidParameterException;
import java.util.*;

public class KnapsackProblem {
    private boolean[][] population;
    Species[] speciesArray;

    //conditions
    private final int size;
    private final int[][] itemCharacteristics;
    private final int amountOfSpecies;

    /**
     * The number of percents to grab from 1th parent in the crossbreeding.
     */
    private final double CROSS_INDEX = 70;

    /**
     *
     * @param size size of the knapsack;
     * @param items array of characteristics of the items;
     * @param species amount of the species;
     */
    public KnapsackProblem(int size, int[][] items, int species) {

        //TODO fix this!
        if(items.length != species)
            throw new InvalidParameterException();

        this.size = size;
        this.itemCharacteristics = items;
        this.amountOfSpecies = species;

        this.population = new boolean[species][items.length];
        onStartPopulation();

        this.speciesArray = new Species[species];
        recalculateValues();
    }


    /**
     *
     * @param iterations amount of iterations of the algorithm.
     * @return Data class with information about the best species.
     * @see Data
     */
    public Data startGeneticAlgorithm(int iterations){
        Random rand = new Random();
        int record = 0;
        int numOfSteps = 0;

        for (int i = 0; i<iterations; i++) {

            //crossbreeding
            int random = rand.nextInt(population.length);
            int best = speciesArray[amountOfSpecies-1].id;
            boolean[] child = crossbreeding(population[best], population[random]);
            if(!isAlive(child))
                continue;

            //mutation
            if((rand.nextInt(100) < 5)) {
                boolean[] mutChild = mutation(child);
                if (isAlive(mutChild))
                    child = mutChild;
            }

            //locale improving

            localImprovingRandom();
            localImprovingBW();
            //localImprovingDeleteSecond();


            int toKill = speciesArray[0].id;
            population[toKill] = child;

            recalculateValues();

            if(speciesArray[amountOfSpecies-1].value > record) {
                record = speciesArray[amountOfSpecies-1].value;
                numOfSteps = i;
                //System.out.println(record);
            }

            //TODO TEMP DATA!
            if(i % 20 == 0)
                System.out.println(i + ": " + record);

        }
        return new Data(speciesArray[amountOfSpecies-1].size, speciesArray[amountOfSpecies-1].value, numOfSteps,
                population[speciesArray[amountOfSpecies-1].id]);
    }


    /**
     *
     * @param i 1th Species to crossbreeding.
     * @param k 2th Species to crossbreeding.
     *
     */
    private boolean[] crossbreeding(boolean[] i, boolean[] k){
        boolean[] child = new boolean[i.length];
        int th1 = (int)(CROSS_INDEX/100 * amountOfSpecies);
        int th2 = (int)((100 - CROSS_INDEX)/100 * amountOfSpecies);
        System.arraycopy(i, 0, child, 0, th1);
        System.arraycopy(k, th1, child, th1, th2);

        return child;
    }

    private void localImprovingBW(){
        int bestId = speciesArray[amountOfSpecies-1].id;
        int worstId = speciesArray[0].id;

        boolean[] child = crossbreeding(population[bestId], population[worstId]);
        if(isAlive(child))
            population[worstId] = child;
    }

    private void localImprovingRandom(){
        Random rand = new Random();

        int bestId = speciesArray[amountOfSpecies-1].id;
        int r = rand.nextInt(population[bestId].length);
        population[bestId][r] = !population[bestId][r];
        if(!isAlive(population[bestId]))
            population[bestId][r] = !population[bestId][r];
    }

    /*private void localImprovingDeleteSecond(){
        Species species = speciesArray.pollLast();
        if(speciesArray.peekLast().value + 1 == species.value) {
            int i = speciesArray.pollLast().id;
            Arrays.fill(population[i], false);
        }

        speciesArray.addLast(species);
    }*/


    /**
     *
     * @param i genes of the species to mutate.
     * @return genes of the new mutated species.
     */
    private boolean[] mutation(boolean[] i){
        boolean[] newChild = i.clone();

        Random rand = new Random();
        int rand1 = rand.nextInt(i.length);
        int rand2 = rand.nextInt(i.length);
        boolean t = newChild[rand1];
        newChild[rand1] = newChild[rand2];
        newChild[rand2] = t;

        return newChild;
    }

    /**
     * The method to recalculate values of the species in the core array.
     */
    private void recalculateValues(){
        Arrays.fill(speciesArray, null);

        for (int i = 0; i < population.length; i++) {
            int value = 0;
            int size = 0;
            for (int j = 0; j < population[i].length; j++) {
                if(population[i][j]) {
                    value+=itemCharacteristics[j][0];
                    size+=itemCharacteristics[j][1];
                }
            }
            speciesArray[i] = new Species(i, value, size);
        }

        Arrays.sort(speciesArray);
    }

    /**
     *
     * @param i the genes to check for living.
     * @return result is the current genes alive.
     */
    private boolean isAlive(boolean[] i){
        int size = 0;
        boolean empty = true;
        for (int j = 0; j < i.length; j++) {
            if(i[j]) {
                size += itemCharacteristics[j][1];
                empty = false;
            }
        }

        return (!empty && size <= this.size);
    }

    private int count(boolean[] i){
        int size = 0;
        for (int j = 0; j < i.length; j++) {
            if(i[j])
                size+=itemCharacteristics[j][1];
        }

        return size;
    }

    /**
     * The method fills start population with "true" on the diagonal.
     */
    private void onStartPopulation(){
        for (int i = 0; i < population.length; i++) {
            population[i][i] = true;
        }
    }

    /**
     * The class of species with default variables: value, id, size;
     */
    private class Species implements Comparable<Species>{
        int value;
        int id;
        int size;

        public Species(int id, int value, int size) {
            this.value = value;
            this.id = id;
            this.size = size;
        }

        @Override
        public int compareTo(Species o) {
            return Integer.compare(this.value, o.value);
        }

        @Override
        public String toString() {
            return "Species{" +
                    "value=" + value +
                    ", id=" + id +
                    '}';
        }
    }

}
