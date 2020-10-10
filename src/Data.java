public class Data {
    int size;
    int value;
    int numberOfSteps;
    boolean[] answer;

    public Data(int size, int value, int numberOfSteps, boolean[] answer) {
        this.size = size;
        this.value = value;
        this.numberOfSteps = numberOfSteps;
        this.answer = answer;
    }

    public Data() {
    }

    @Override
    public String toString() {
        return "Data{" +
                "size=" + size +
                ", value=" + value +
                ", numberOfSteps=" + numberOfSteps + "\n" +
                "result=" + booleanOutput() +
                '}';
    }

    private String booleanOutput(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < answer.length; i++) {
            if(answer[i])
                sb.append(i).append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
