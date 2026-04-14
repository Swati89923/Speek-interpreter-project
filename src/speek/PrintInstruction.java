package speek;

public class PrintInstruction<T> implements Instruction {

    private Expression<T> expression;

    public PrintInstruction(Expression<T> expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment<?> env) {
        @SuppressWarnings("unchecked")
        Environment<T> typedEnv = (Environment<T>) env;

        T value = expression.evaluate(typedEnv);
        System.out.println(value);
    }
}