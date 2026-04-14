package speek;

public class VariableNode<T> implements Expression<T> {

    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T evaluate(Environment<?> env) {
        return (T) ((Environment<?>) env).get(name);
    }
}