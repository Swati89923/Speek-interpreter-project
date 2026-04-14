package speek;

public interface Instruction {
    void execute(Environment<?> env);
}