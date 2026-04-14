package speek;

public interface Expression<T> {
    T evaluate(Environment<?> env);
}