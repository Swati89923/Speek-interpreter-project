package speek;

public interface Expression {
    Object evaluate(Environment env);
}