package speek;

import java.util.List;

public class IfInstruction implements Instruction {

    private Expression<Boolean> condition;
    private List<Instruction> thenBody;
    private List<Instruction> elseBody;

    public IfInstruction(Expression<Boolean> condition,
                         List<Instruction> thenBody,
                         List<Instruction> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public void execute(Environment<?> env) {

        @SuppressWarnings("unchecked")
        Environment<Boolean> boolEnv = (Environment<Boolean>) env;

        Boolean result = condition.evaluate(boolEnv);

        if (result != null && result) {
            for (Instruction inst : thenBody) {
                inst.execute(env);
            }
        } else {
            for (Instruction inst : elseBody) {
                inst.execute(env);
            }
        }
    }
}
