public class Field {
    private final String name;
    private final double weight;

    Field(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}