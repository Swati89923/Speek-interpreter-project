package speek;

public class StringNode implements Expression<String> {

    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public String evaluate(Environment<?> env) {
        return value;
    }
}