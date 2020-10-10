import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // start data.
        int amountOfThings = 100;
        int size = 250;
        int species = 100;
        int iterations = 1000;

        int[][] items = new int[amountOfThings][2];
        generateItems(items);

        Data maxData = new Data();
        int loop = 1;
        double steps = 0;
        for (int i = 0; i < loop; i++) {
            KnapsackProblem knapsackProblem = new KnapsackProblem(size, items, species);
            Data data = knapsackProblem.startGeneticAlgorithm(iterations);

            if(data.value > maxData.value)
                maxData = data;
            steps += data.numberOfSteps;
        }

        System.out.println("Best species after " + loop + " cycles " + iterations + " iterations each:");
        System.out.println("==================");
        System.out.println(maxData);
        System.out.println("==================");
        System.out.println("Average num of steps to reach final answer: " + (steps/loop));
    }

    public static void generateItems(int[][] items){
        for (int i = 0; i < items.length; i++) {
            int random_value = (int)(Math.random() * (30 - 2 + 1) + 2);
            int random_size = (int)(Math.random() * (10 - 1 + 1) + 1);

            items[i][0] = random_value;
            items[i][1] = random_size;
        }
    }
}
