package speek;

public class AssignInstruction<T> implements Instruction {

    private String variableName;
    private Expression<T> expression;

    public AssignInstruction(String variableName, Expression<T> expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public void execute(Environment env){
        @SuppressWarnings("unchecked")
        Environment<T> typedEnv = (Environment<T>) env;

        T value = expression.evaluate(typedEnv);
        typedEnv.set(variableName, value);
    }
}
