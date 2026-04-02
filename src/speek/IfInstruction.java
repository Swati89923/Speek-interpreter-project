package speek;

import java.util.List;

public class IfInstruction implements Instruction {

    private Expression condition;
    private List<Instruction> thenBody;
    private List<Instruction> elseBody;

    public IfInstruction(Expression condition,
                         List<Instruction> thenBody,
                         List<Instruction> elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public void execute(Environment env) {

        Object result = condition.evaluate(env);

        if (result instanceof Boolean && (Boolean) result) {

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