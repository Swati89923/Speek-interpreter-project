package speek;

public class NumberNode implements Expression<Double> {

    private double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public Double evaluate(Environment<?> env) {
        return value;
    }
}